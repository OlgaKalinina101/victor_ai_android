/**
Victor AI - Personal AI Companion for Android
Copyright (C) 2025-2026 Olga Kalinina

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.
 */

package com.example.victor_ai.ui.playlist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.MusicApiImpl
import com.example.victor_ai.data.network.getTracksPaged
import com.example.victor_ai.data.network.PlaylistMomentOut
import com.example.victor_ai.data.network.Track
import com.example.victor_ai.data.network.TrackDescriptionUpdate
import com.example.victor_ai.data.network.WaveTrack
import com.example.victor_ai.data.repository.TrackCacheRepository
import com.example.victor_ai.logic.AudioPlayer
import com.example.victor_ai.logic.MusicPlaybackService
import com.example.victor_ai.ui.playlist.components.TrackCacheState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Named

private const val TAG = "PlaylistViewModel"

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val apiService: ApiService,
    private val musicApi: MusicApiImpl,
    @Named("baseUrl") private val baseUrl: String,
    @Named("cacheDir") private val cacheDir: File,
    @ApplicationContext private val applicationContext: Context,
    private val trackCacheRepository: TrackCacheRepository
) : ViewModel() {

    // 🔐 accountId получаем из UserProvider и обновляем через reinitialize()
    private var accountId: String = UserProvider.getCurrentUserId()

    // ═══════════════════════════════════════════════════════════
    // 🎯 ЕДИНОЕ СОСТОЯНИЕ UI (вместо 8 отдельных StateFlow)
    // ═══════════════════════════════════════════════════════════
    
    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    // ═══════════════════════════════════════════════════════════
    // 🔥 ВНУТРЕННИЕ СОСТОЯНИЯ (не экспортируются в UI)
    // ═══════════════════════════════════════════════════════════
    
    private val _rawTracks = MutableStateFlow<List<Track>>(emptyList())
    private val _trackCacheStates = MutableStateFlow<Map<Int, TrackCacheState>>(emptyMap())
    private val _waveTracks = MutableStateFlow<List<WaveTrack>>(emptyList())
    
    private val audioPlayer = AudioPlayer(applicationContext)

    // ═══════════════════════════════════════════════════════════
    // 🔥 BroadcastReceiver для команд из уведомления
    // ═══════════════════════════════════════════════════════════
    
    private val mediaCommandReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                MusicPlaybackService.ACTION_PLAY -> {
                    Log.d(TAG, "📻 Received PLAY command from notification")
                    resumeTrack()
                }
                MusicPlaybackService.ACTION_PAUSE -> {
                    Log.d(TAG, "📻 Received PAUSE command from notification")
                    pauseTrack()
                }
                MusicPlaybackService.ACTION_NEXT -> {
                    Log.d(TAG, "📻 Received NEXT command from notification")
                    playNextTrack()
                }
                MusicPlaybackService.ACTION_PREVIOUS -> {
                    Log.d(TAG, "📻 Received PREVIOUS command from notification")
                    playPreviousTrack()
                }
            }
        }
    }

    init {
        Log.d(TAG, "🏗️ ViewModel created (init block), accountId=$accountId")
        
        // 🔥 Загружаем данные если есть accountId
        if (accountId.isNotBlank()) {
            Log.d(TAG, "🔄 Init: загружаем данные для accountId=$accountId")
            loadTracks()
            loadStats()
        } else {
            Log.d(TAG, "⏸️ Init: пропускаем загрузку данных, ждем reinitialize() с реальным accountId")
        }
        loadCacheStates()
        
        // 🔥 Запускаем обновление UI state при изменении источников данных
        startUiStateUpdater()
        
        // 🔥 ИСПРАВЛЕНО: Оптимизированный updater — работает только при isPlaying=true
        startPositionUpdater()
        startNotificationUpdater()

        // 🔥 Устанавливаем callbacks для AudioPlayer
        audioPlayer.setOnCompletionListener {
            playNextTrack()
        }

        audioPlayer.setOnPlayPauseListener { isPlaying ->
            _uiState.update { it.copy(isPlaying = isPlaying) }
            updateNotification()
        }

        audioPlayer.setOnNextListener {
            playNextTrack()
        }

        audioPlayer.setOnPreviousListener {
            playPreviousTrack()
        }

        // 🔥 Регистрируем BroadcastReceiver для команд из уведомления
        registerMediaCommandReceiver()
    }

    // ═══════════════════════════════════════════════════════════
    // 🎯 UI STATE UPDATER (реактивное обновление)
    // ═══════════════════════════════════════════════════════════
    
    /**
     * 🔥 НОВОЕ: Реактивное обновление UI state из источников данных
     * Автоматически пересчитывает tracks при изменении фильтров/сортировки/кеша
     */
    private fun startUiStateUpdater() {
        viewModelScope.launch {
            combine(
                _rawTracks,
                _trackCacheStates,
                _uiState
            ) { rawTracks, cacheStates, currentState ->
                // 🔥 Маппим Track → TrackUiModel с кеш-состоянием
                val tracksWithCache = rawTracks.map { track ->
                    track.toUiModel(cacheStates[track.id] ?: TrackCacheState.NOT_CACHED)
                }
                
                // 🔥 Фильтрация
                val filtered = tracksWithCache.filter { track ->
                    (currentState.energyFilter == null || track.energyDescription == currentState.energyFilter) &&
                    (currentState.temperatureFilter == null || track.temperatureDescription == currentState.temperatureFilter)
                }
                
                // 🔥 Сортировка
                val sorted = when (currentState.sortBy) {
                    "title" -> filtered.sortedBy { it.title }
                    "artist" -> filtered.sortedBy { it.artist }
                    "duration" -> filtered.sortedByDescending { it.duration }
                    else -> filtered.sortedByDescending { it.id } // recent
                }
                
                // 🔥 Вычисляем опции для фильтров
                val energyOptions = rawTracks
                    .mapNotNull { it.energyDescription }
                    .distinct()
                    .sorted()
                
                val temperatureOptions = rawTracks
                    .mapNotNull { it.temperatureDescription }
                    .distinct()
                    .sorted()
                
                // 🔥 Обновляем UI state
                currentState.copy(
                    tracks = sorted,
                    energyOptions = energyOptions,
                    temperatureOptions = temperatureOptions
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // 🎯 ПУБЛИЧНЫЕ МЕТОДЫ ДЛЯ UI
    // ═══════════════════════════════════════════════════════════

    /**
     * 🔐 Переинициализация для нового аккаунта.
     * Вызывается при смене demo_key/аккаунта без пересоздания ViewModel.
     */
    fun reinitialize(newAccountId: String) {
        Log.d(TAG, "🔄 reinitialize вызван: current=$accountId, new=$newAccountId")
        if (newAccountId == accountId) {
            if (_rawTracks.value.isEmpty()) {
                Log.d(TAG, "🔄 reinitialize: accountId не изменился, но треки пустые — загружаем")
                loadTracks()
                loadStats()
            } else {
                Log.d(TAG, "🔄 reinitialize: accountId не изменился ($accountId), пропускаем")
            }
            return
        }
        Log.d(TAG, "🔄 reinitialize: accountId изменился $accountId → $newAccountId, перезагружаем данные")
        accountId = newAccountId
        _rawTracks.value = emptyList()
        _uiState.value = PlaylistUiState()
        Log.d(TAG, "🔄 reinitialize: вызываем loadTracks() и loadStats()")
        loadTracks()
        loadStats()
    }

    fun loadTracks() {
        viewModelScope.launch {
            Log.d(TAG, "🔄 Начинаем загрузку треков для accountId=$accountId")
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val tracks = musicApi.getTracksPaged(accountId)
                _rawTracks.value = tracks
                Log.d(TAG, "✅ Loaded ${tracks.size} tracks")
            } catch (e: java.net.SocketTimeoutException) {
                Log.e(TAG, "⏰ Timeout при загрузке треков (бэкенд не ответил за 60 секунд)", e)
                _uiState.update { 
                    it.copy(
                        error = ErrorState.LoadingTracksError(
                            "Сервер не отвечает. Проверьте подключение к интернету или попробуйте позже."
                        )
                    )
                }
            } catch (e: java.net.UnknownHostException) {
                Log.e(TAG, "🌐 Не удалось подключиться к серверу", e)
                _uiState.update { 
                    it.copy(
                        error = ErrorState.LoadingTracksError(
                            "Не удалось подключиться к серверу. Проверьте настройки ngrok URL."
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error loading tracks: ${e.javaClass.simpleName}: ${e.message}", e)
                _uiState.update { 
                    it.copy(
                        error = ErrorState.LoadingTracksError(
                            e.message ?: "Не удалось загрузить треки"
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
                Log.d(TAG, "🏁 Загрузка треков завершена (success=${_rawTracks.value.isNotEmpty()})")
            }
        }
    }

    fun loadStats() {
        viewModelScope.launch {
            try {
                val result = musicApi.getTrackStats(accountId = accountId)
                _uiState.update { it.copy(stats = result) }
                Log.d(TAG, "✅ Loaded stats")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error loading stats", e)
                _uiState.update {
                    it.copy(
                        error = ErrorState.NetworkError(
                            "Не удалось загрузить статистику"
                        )
                    )
                }
            }
        }
    }

    /**
     * 🔥 НОВОЕ: Единая точка установки фильтров
     */
    fun updateEnergyFilter(energy: String?) {
        _uiState.update { it.copy(energyFilter = energy) }
        Log.d(TAG, "🔍 Energy filter: $energy")
    }

    fun updateTemperatureFilter(temperature: String?) {
        _uiState.update { it.copy(temperatureFilter = temperature) }
        Log.d(TAG, "🔍 Temperature filter: $temperature")
    }

    fun updateSortBy(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy) }
        Log.d(TAG, "🔍 Sort by: $sortBy")
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // ═══════════════════════════════════════════════════════════
    // 🎵 ПЛЕЕР: ВОСПРОИЗВЕДЕНИЕ
    // ═══════════════════════════════════════════════════════════

    fun playTrack(trackId: Int?) {
        if (trackId == null) return

        viewModelScope.launch {
            Log.d(TAG, "🎵 Starting playback: trackId=$trackId")

            // Получаем информацию о треке
            val track = _rawTracks.value.firstOrNull { it.id == trackId }
            if (track == null) {
                Log.e(TAG, "❌ Track not found: $trackId")
                _uiState.update {
                    it.copy(error = ErrorState.PlaybackError("Трек не найден"))
                }
                return@launch
            }

            // 🔥 Сначала пробуем воспроизвести трек из кеша
            val cachedPath = try {
                trackCacheRepository.getCachedTrackPath(trackId)
            } catch (e: Exception) {
                Log.e(TAG, "⚠️ Не удалось получить путь к кешированному треку", e)
                null
            }

            val streamUrl = buildStreamUrl(trackId)

            // Останавливаем текущий трек перед запуском нового
            val currentId = _uiState.value.currentPlayingTrackId
            if (currentId != null && currentId != trackId) {
                Log.d(TAG, "🛑 Stopping previous track: $currentId")
                audioPlayer.stop()
            }

            // 🔥 Обновляем метаданные в AudioPlayer для MediaSession
            audioPlayer.updateTrackMetadata(
                title = track.title,
                artist = track.artist ?: "Victor AI",
                duration = (track.duration * 1000).toLong() // секунды -> миллисекунды
            )

            try {
                // 🔥 Воспроизводим из кеша или по сети
                if (!cachedPath.isNullOrEmpty()) {
                    Log.d(TAG, "🎵 Воспроизводим трек из кеша: $cachedPath")
                    audioPlayer.playFromFile(cachedPath)
                } else {
                    Log.d(TAG, "🎵 Воспроизводим трек по сети: $streamUrl")
                    audioPlayer.playFromUrl(streamUrl)
                }
                
                _uiState.update { 
                    it.copy(
                        currentPlayingTrackId = trackId,
                        isPlaying = true,
                        currentPosition = 0f,
                        error = null
                    )
                }

                // 🔥 Запускаем Foreground Service с MediaStyle уведомлением
                val sessionToken = audioPlayer.getMediaSessionToken()
                Log.d(TAG, "🔑 MediaSession token: ${if (sessionToken != null) "✅ present" else "❌ null"}")

                MusicPlaybackService.startPlayback(
                    context = applicationContext,
                    trackTitle = track.title,
                    trackArtist = track.artist ?: "Victor AI",
                    isPlaying = true,
                    sessionToken = sessionToken,
                    duration = track.duration.toLong(),
                    position = 0
                )
                Log.d(TAG, "✅ Foreground service started with media notification")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Playback error", e)
                _uiState.update {
                    it.copy(
                        error = ErrorState.PlaybackError(
                            e.message ?: "Ошибка воспроизведения"
                        )
                    )
                }
            }
        }
    }

    fun pauseTrack() {
        Log.d(TAG, "⏸️ Pausing track")
        audioPlayer.pause()
        _uiState.update { it.copy(isPlaying = false) }
        updateNotification()
    }

    fun resumeTrack() {
        Log.d(TAG, "▶️ Resuming track")
        audioPlayer.resume()
        _uiState.update { it.copy(isPlaying = true) }
        updateNotification()
    }

    fun seekTo(position: Float) {
        audioPlayer.seekTo((position * 1000).toInt()) // секунды → миллисекунды
        _uiState.update { it.copy(currentPosition = position) }
    }

    /**
     * 🔥 Воспроизведение следующего трека
     */
    fun playNextTrack() {
        val filteredTracks = _uiState.value.tracks
        if (filteredTracks.isEmpty()) {
            Log.w(TAG, "⚠️ No tracks available for next")
            return
        }

        val currentId = _uiState.value.currentPlayingTrackId
        val currentIndex = filteredTracks.indexOfFirst { it.id == currentId }

        val nextIndex = if (currentIndex == -1) {
            0
        } else {
            (currentIndex + 1) % filteredTracks.size  // По кругу
        }

        val nextTrack = filteredTracks[nextIndex]
        Log.d(TAG, "⏭️ Playing next track: ${nextTrack.title}")
        playTrack(nextTrack.id)
    }

    /**
     * 🔥 Воспроизведение предыдущего трека
     */
    fun playPreviousTrack() {
        val filteredTracks = _uiState.value.tracks
        if (filteredTracks.isEmpty()) {
            Log.w(TAG, "⚠️ No tracks available for previous")
            return
        }

        val currentId = _uiState.value.currentPlayingTrackId
        val currentIndex = filteredTracks.indexOfFirst { it.id == currentId }

        val previousIndex = if (currentIndex <= 0) {
            filteredTracks.size - 1
        } else {
            currentIndex - 1
        }

        val previousTrack = filteredTracks[previousIndex]
        Log.d(TAG, "⏮️ Playing previous track: ${previousTrack.title}")
        playTrack(previousTrack.id)
    }

    // ═══════════════════════════════════════════════════════════
    // 🔥 ОПТИМИЗИРОВАННЫЕ UPDATER'Ы (работают только при необходимости)
    // ═══════════════════════════════════════════════════════════

    /**
     * 🔥 ИСПРАВЛЕНО: Обновляет позицию ТОЛЬКО когда isPlaying=true
     * Использует collectLatest вместо бесконечного while(isActive)
     */
    private fun startPositionUpdater() {
        viewModelScope.launch {
            _uiState.collectLatest { state ->
                if (state.isPlaying) {
                    // 🔥 Запускаем цикл обновления только при воспроизведении
                    var updateCounter = 0
                    while (isActive && _uiState.value.isPlaying) {
                        delay(100) // обновление каждые 100мс
                        val position = audioPlayer.getCurrentPosition()
                        _uiState.update { it.copy(currentPosition = position / 1000f) }

                        // Обновляем MediaSession каждую секунду
                        updateCounter++
                        if (updateCounter >= 10) {
                            updateCounter = 0
                            audioPlayer.updatePlaybackPosition(position.toLong())
                        }
                    }
                }
            }
        }
    }

    /**
     * 🔥 ИСПРАВЛЕНО: Обновляет notification только при изменении состояния
     * Использует combine вместо polling каждые 5 секунд
     */
    private fun startNotificationUpdater() {
        viewModelScope.launch {
            combine(
                _uiState,
                _rawTracks
            ) { state, tracks ->
                Pair(state, tracks)
            }.collect { (state, tracks) ->
                if (state.isPlaying && state.currentPlayingTrackId != null) {
                    val track = tracks.firstOrNull { it.id == state.currentPlayingTrackId }
                    if (track != null) {
                        updateNotificationInternal(track, state.isPlaying, state.currentPosition)
                    }
                }
            }
        }
    }

    /**
     * 🔥 Обновление уведомления при изменении состояния
     */
    private fun updateNotification() {
        val state = _uiState.value
        val currentTrack = _rawTracks.value.firstOrNull { it.id == state.currentPlayingTrackId }
        if (currentTrack != null) {
            updateNotificationInternal(currentTrack, state.isPlaying, state.currentPosition)
        }
    }

    private fun updateNotificationInternal(track: Track, isPlaying: Boolean, position: Float) {
        try {
            val currentPositionMs = (position * 1000).toLong()
            val currentPositionSec = currentPositionMs / 1000

            MusicPlaybackService.updateNotification(
                context = applicationContext,
                trackTitle = track.title,
                trackArtist = track.artist ?: "Victor AI",
                isPlaying = isPlaying,
                sessionToken = audioPlayer.getMediaSessionToken(),
                duration = track.duration.toLong(),
                position = currentPositionSec
            )
            Log.d(TAG, "🔄 Notification updated: ${track.title} (playing=$isPlaying)")
        } catch (e: Exception) {
            Log.e(TAG, "⚠️ Failed to update notification: ${e.message}")
        }
    }

    /**
     * 🔥 Синхронизация состояния UI с реальным состоянием плеера
     */
    fun syncPlayerState() {
        val realIsPlaying = audioPlayer.isPlaying()
        val currentState = _uiState.value

        Log.d(TAG, "🔄 syncPlayerState: realIsPlaying=$realIsPlaying, viewModelIsPlaying=${currentState.isPlaying}")

        if (currentState.isPlaying != realIsPlaying) {
            Log.d(TAG, "🔄 Syncing isPlaying: was=${currentState.isPlaying}, now=$realIsPlaying")
            _uiState.update { it.copy(isPlaying = realIsPlaying) }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // 🌊 WAVE ФУНКЦИОНАЛ
    // ═══════════════════════════════════════════════════════════

    // ==================== Streaming Log Sheet ====================

    fun showStreamingLogSheet() {
        _uiState.update { it.copy(showStreamingLogSheet = true) }
    }

    fun hideStreamingLogSheet() {
        _uiState.update { it.copy(showStreamingLogSheet = false) }
    }

    // ==================== Playlist Moments Sheet ====================

    fun showPlaylistMomentsSheet(limit: Int = 20) {
        _uiState.update { it.copy(showPlaylistMomentsSheet = true) }
        loadPlaylistMoments(limit = limit)
    }

    fun hidePlaylistMomentsSheet() {
        _uiState.update { it.copy(showPlaylistMomentsSheet = false) }
    }

    fun loadPlaylistMoments(limit: Int = 20) {
        viewModelScope.launch {
            _uiState.update { it.copy(isPlaylistMomentsLoading = true, playlistMomentsError = null) }
            try {
                val moments: List<PlaylistMomentOut> = musicApi.getPlaylistMoments(
                    accountId = accountId,
                    limit = limit
                )
                _uiState.update { it.copy(playlistMoments = moments) }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error loading playlist moments", e)
                _uiState.update {
                    it.copy(
                        playlistMomentsError = e.message ?: "Не удалось загрузить историю моментов",
                        playlistMoments = emptyList()
                    )
                }
            } finally {
                _uiState.update { it.copy(isPlaylistMomentsLoading = false) }
            }
        }
    }

    /**
     * 🎵 НОВОЕ: Streaming версия выбора трека с логами в реальном времени
     */
    fun runAssistantWaveStreaming(manual: Boolean = false) {
        viewModelScope.launch {
            Log.d(TAG, "🎵 Starting streaming wave: manual=$manual, accountId=$accountId")
            _uiState.update {
                it.copy(
                    isWaveLoading = true,
                    error = null,
                    streamingLog = "",
                    showStreamingLogSheet = true
                )
            }
            
            try {
                // 🔥 Вызываем streaming endpoint
                musicApi.runPlaylistChainStreaming(
                    accountId = accountId,
                    extraContext = if (manual) "manual" else "auto"
                ) { event ->
                    Log.d(TAG, "🎵 Stream event received: $event")
                    
                    // 🎵 Обрабатываем каждое событие из stream
                    when {
                        event.containsKey("log") -> {
                            val logText = event["log"] as? String ?: ""
                            Log.d(TAG, "📝 Stream log: $logText")
                            
                            // Обновляем streamingLog для анимации печати
                            _uiState.update { it.copy(streamingLog = logText) }
                            
                            // 🎯 КЛЮЧЕВОЕ: Ждём чтобы анимация успела отобразиться
                            // Время = длина текста * 50мс (скорость печати) + 1 секунда на чтение
                            val typingTime = (logText.length * 50L).coerceAtLeast(1000L)
                            val readingTime = 1500L
                            delay(typingTime + readingTime)
                        }
                        event.containsKey("track") -> {
                            val trackMap = event["track"] as? Map<*, *>
                            val trackId = (trackMap?.get("track_id") as? Number)?.toInt()
                            val trackName = trackMap?.get("track") as? String
                            
                            Log.d(TAG, "🎧 Stream received track: $trackName ($trackId)")
                            
                            if (trackId != null) {
                                playTrack(trackId)
                            } else {
                                Log.w(TAG, "⚠️ Track ID is null in event: $trackMap")
                            }
                        }
                        event.containsKey("done") -> {
                            Log.d(TAG, "✅ Stream completed")
                            // Очищаем streamingLog после завершения
                            delay(2000) // Даём время прочитать последний лог
                            _uiState.update { it.copy(streamingLog = "") }
                        }
                        event.containsKey("error") -> {
                            val errorMsg = event["error"] as? String ?: "Ошибка stream"
                            Log.e(TAG, "❌ Stream error: $errorMsg")
                            _uiState.update {
                                it.copy(
                                    error = ErrorState.WaveError(errorMsg),
                                    streamingLog = ""
                                )
                            }
                        }
                        else -> {
                            Log.w(TAG, "⚠️ Unknown stream event type: $event")
                        }
                    }
                }

                Log.d(TAG, "✅ Streaming wave completed successfully")

            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка streaming волны: ${e.javaClass.simpleName}: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        error = ErrorState.WaveError(
                            e.message ?: "Не удалось запустить волну"
                        ),
                        streamingLog = ""
                    )
                }
            } finally {
                Log.d(TAG, "🏁 Streaming wave finished (finally block)")
                _uiState.update { it.copy(isWaveLoading = false) }
            }
        }
    }

    /**
     * 🔥 Старая версия без streaming (для обратной совместимости)
     */
    fun runAssistantWave(manual: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isWaveLoading = true, error = null) }
            try {
                val response = musicApi.runPlaylistChain(
                    accountId = accountId,
                    extraContext = if (manual) "manual" else "auto"
                )

                Log.d(TAG, "Wave result: $response")

                val trackMap = response["track"] as? Map<*, *>
                val trackId = (trackMap?.get("track_id") as? Double)?.toInt()
                val trackName = trackMap?.get("track") as? String
                val contextText = response["context"] as? String

                if (trackId != null) {
                    Log.d(TAG, "🎧 Playing track $trackName ($trackId)")
                    playTrack(trackId)
                }

                Log.d(TAG, "🪶 Context: $contextText")

            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка запуска волны", e)
                _uiState.update {
                    it.copy(
                        error = ErrorState.WaveError(
                            e.message ?: "Не удалось запустить волну"
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isWaveLoading = false) }
            }
        }
    }

    fun runWave(energy: String?, temperature: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isWaveLoading = true, error = null) }
            try {
                Log.d(TAG, "🔥 Запуск волны: energy=$energy, temp=$temperature")

                val response = musicApi.runPlaylistWave(
                    accountId = accountId,
                    energy = energy,
                    temperature = temperature
                )

                val tracks = response.tracks

                if (tracks.isNotEmpty()) {
                    _waveTracks.value = tracks

                    val first = tracks.first()
                    playWaveTrack(first)
                }

                Log.d(TAG, "🔥 Ответ бэкенда: $response")

            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка запуска волны", e)
                _uiState.update {
                    it.copy(
                        error = ErrorState.WaveError(
                            e.message ?: "Не удалось запустить волну"
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isWaveLoading = false) }
            }
        }
    }

    private fun playWaveTrack(track: WaveTrack) {
        val streamUrl = buildStreamUrl(track.id)

        audioPlayer.updateTrackMetadata(
            title = track.title,
            artist = track.artist ?: "Victor AI",
            duration = (track.duration * 1000).toLong()
        )

        audioPlayer.playFromUrl(streamUrl)

        _uiState.update {
            it.copy(
                currentPlayingTrackId = track.id,
                isPlaying = true,
                currentPosition = 0f
            )
        }
    }

    // ═══════════════════════════════════════════════════════════
    // 🔥 КЕШИРОВАНИЕ ТРЕКОВ
    // ═══════════════════════════════════════════════════════════

    private fun loadCacheStates() {
        viewModelScope.launch {
            trackCacheRepository.getAllCachedTracks().collect { cachedTracks ->
                val invalidIds = mutableListOf<Int>()

                val states = cachedTracks.associate { cached ->
                    val expected = _rawTracks.value.firstOrNull { it.id == cached.trackId }?.fileSize
                        ?: cached.fileSize

                    val ok = trackCacheRepository.isCached(cached.trackId, expected)
                    if (!ok) invalidIds.add(cached.trackId)

                    cached.trackId to if (ok) TrackCacheState.CACHED else TrackCacheState.NOT_CACHED
                }

                _trackCacheStates.value = states

                if (invalidIds.isNotEmpty()) {
                    Log.w(TAG, "🧹 Найдены битые/обрезанные кеш-файлы, почистили: $invalidIds")
                }

                Log.d(TAG, "🎵 Загружено ${states.count { it.value == TrackCacheState.CACHED }} валидных кешированных треков")
            }
        }
    }

    fun cacheTrack(track: TrackUiModel) {
        viewModelScope.launch {
            // Устанавливаем состояние "загружается"
            _trackCacheStates.update { it + (track.id to TrackCacheState.DOWNLOADING) }

            // Находим оригинальный Track DTO
            val originalTrack = _rawTracks.value.firstOrNull { it.id == track.id }
            if (originalTrack == null) {
                Log.e(TAG, "❌ Original track not found for caching: ${track.id}")
                _trackCacheStates.update { it - track.id }
                return@launch
            }

            val result = trackCacheRepository.cacheTrack(
                track = originalTrack,
                accountId = accountId,
                baseUrl = baseUrl
            )

            result.fold(
                onSuccess = { path ->
                    Log.d(TAG, "✅ Трек закеширован: ${track.title}")
                    _trackCacheStates.update { it + (track.id to TrackCacheState.CACHED) }
                },
                onFailure = { error ->
                    Log.e(TAG, "❌ Ошибка кеширования: ${error.message}")
                    _trackCacheStates.update { it - track.id }
                }
            )
        }
    }

    fun removeCachedTrack(trackId: Int) {
        viewModelScope.launch {
            val result = trackCacheRepository.removeCachedTrack(trackId)

            result.fold(
                onSuccess = {
                    Log.d(TAG, "🗑️ Трек удален из кеша")
                    _trackCacheStates.update { it - trackId }
                },
                onFailure = { error ->
                    Log.e(TAG, "❌ Ошибка удаления: ${error.message}")
                }
            )
        }
    }

    // ═══════════════════════════════════════════════════════════
    // 🔧 ОБНОВЛЕНИЕ МЕТАДАННЫХ
    // ═══════════════════════════════════════════════════════════

    fun updateDescription(
        trackId: String,
        energy: String?,
        temperature: String?
    ) {
        viewModelScope.launch {
            try {
                musicApi.updateTrackDescription(
                    update = TrackDescriptionUpdate(
                        track_id = trackId,
                        energy_description = energy,
                        temperature_description = temperature
                    )
                )
                loadTracks()
            } catch (e: Exception) {
                Log.e(TAG, "Error updating description", e)
                _uiState.update {
                    it.copy(
                        error = ErrorState.NetworkError(
                            "Не удалось обновить описание трека"
                        )
                    )
                }
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // 🔧 LIFECYCLE
    // ═══════════════════════════════════════════════════════════

    @Suppress("UnspecifiedRegisterReceiverFlag")
    private fun registerMediaCommandReceiver() {
        val filter = IntentFilter().apply {
            addAction(MusicPlaybackService.ACTION_PLAY)
            addAction(MusicPlaybackService.ACTION_PAUSE)
            addAction(MusicPlaybackService.ACTION_NEXT)
            addAction(MusicPlaybackService.ACTION_PREVIOUS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.registerReceiver(
                mediaCommandReceiver,
                filter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            applicationContext.registerReceiver(mediaCommandReceiver, filter)
        }
    }

    private fun buildStreamUrl(trackId: Int): String {
        return "${baseUrl.trimEnd('/')}/tracks/stream/$trackId?account_id=$accountId"
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "💀 ViewModel onCleared() - DESTROYING")
        Log.d(TAG, "💀 Current state: trackId=${_uiState.value.currentPlayingTrackId}, isPlaying=${_uiState.value.isPlaying}")

        // 🔥 Отменяем регистрацию BroadcastReceiver
        try {
            applicationContext.unregisterReceiver(mediaCommandReceiver)
            Log.d(TAG, "✅ MediaCommandReceiver unregistered")
        } catch (e: Exception) {
            Log.e(TAG, "⚠️ Failed to unregister receiver: ${e.message}")
        }

        // 🔥 Полная очистка AudioPlayer (включая MediaSession)
        audioPlayer.release()

        // 🔥 Останавливаем Foreground Service при уничтожении ViewModel
        try {
            MusicPlaybackService.stopPlayback(applicationContext)
        } catch (e: Exception) {
            Log.e(TAG, "⚠️ Failed to stop foreground service: ${e.message}")
        }
    }
}
