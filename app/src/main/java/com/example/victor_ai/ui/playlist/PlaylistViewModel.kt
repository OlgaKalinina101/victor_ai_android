package com.example.victor_ai.ui.playlist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.network.RetrofitInstance.api
import com.example.victor_ai.logic.AudioPlayer
import com.example.victor_ai.logic.MusicPlaybackService
import com.example.victor_ai.domain.model.Track
import com.example.victor_ai.domain.model.TrackDescriptionUpdate
import com.example.victor_ai.domain.model.TrackStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File


class PlaylistViewModel(
    private val apiService: ApiService,
    private val accountId: String,
    private val cacheDir: File,
    private val applicationContext: Context  // ✅ Application Context (не Activity!) - безопасно для ViewModel
) : ViewModel() {

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentPlayingTrackId = MutableStateFlow<Int?>(null)
    private val _currentPosition = MutableStateFlow(0f)

    val currentPosition: StateFlow<Float> = _currentPosition.asStateFlow()
    val currentPlayingTrackId: StateFlow<Int?> = _currentPlayingTrackId.asStateFlow()

    // ← ДОБАВЛЕНО: состояние воспроизведения
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    // 🔥 НОВОЕ: фильтры для автовоспроизведения
    private val _energyFilter = MutableStateFlow<String?>(null)
    private val _temperatureFilter = MutableStateFlow<String?>(null)
    private val _sortBy = MutableStateFlow("recent")

    private val audioPlayer = AudioPlayer(applicationContext)  // ✅ Передаём Application Context
    private val _stats = MutableStateFlow<TrackStats?>(null)
    val stats: StateFlow<TrackStats?> = _stats.asStateFlow()

    // 🔥 BroadcastReceiver для обработки команд из уведомления
    private val mediaCommandReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                MusicPlaybackService.ACTION_PLAY -> {
                    Log.d("PlaylistViewModel", "📻 Received PLAY command from notification")
                    resumeTrack()
                }
                MusicPlaybackService.ACTION_PAUSE -> {
                    Log.d("PlaylistViewModel", "📻 Received PAUSE command from notification")
                    pauseTrack()
                }
                MusicPlaybackService.ACTION_NEXT -> {
                    Log.d("PlaylistViewModel", "📻 Received NEXT command from notification")
                    playNextTrack()
                }
                MusicPlaybackService.ACTION_PREVIOUS -> {
                    Log.d("PlaylistViewModel", "📻 Received PREVIOUS command from notification")
                    playPreviousTrack()
                }
            }
        }
    }

    init {
        Log.d("PlaylistViewModel", "🏗️ ViewModel created (init block)")
        loadTracks()
        startPositionUpdater()
        startNotificationUpdater()  // 🔥 Периодическое обновление notification
        loadTracks()
        loadStats()

        // 🔥 Устанавливаем callbacks для AudioPlayer
        audioPlayer.setOnCompletionListener {
            playNextTrack()
        }

        audioPlayer.setOnPlayPauseListener { isPlaying ->
            _isPlaying.value = isPlaying
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

    /**
     * 🔥 Регистрация BroadcastReceiver для обработки команд из уведомления
     */
    private fun registerMediaCommandReceiver() {
        val filter = IntentFilter().apply {
            addAction(MusicPlaybackService.ACTION_PLAY)
            addAction(MusicPlaybackService.ACTION_PAUSE)
            addAction(MusicPlaybackService.ACTION_NEXT)
            addAction(MusicPlaybackService.ACTION_PREVIOUS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.registerReceiver(mediaCommandReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            applicationContext.registerReceiver(mediaCommandReceiver, filter)
        }

        Log.d("PlaylistViewModel", "✅ MediaCommandReceiver registered")
    }

    fun loadTracks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _tracks.value = apiService.getTracks(accountId)
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Error loading tracks", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun startPositionUpdater() {
        viewModelScope.launch {
            var updateCounter = 0
            while (isActive) {  // ✅ Проверка isActive - останавливаем при onCleared()
                delay(100) // обновление каждые 100мс
                if (_isPlaying.value) {
                    val position = audioPlayer.getCurrentPosition()
                    _currentPosition.value = position / 1000f // миллисекунды → секунды

                    // 🔥 Обновляем MediaSession каждую секунду (каждые 10 итераций по 100мс)
                    updateCounter++
                    if (updateCounter >= 10) {
                        updateCounter = 0
                        audioPlayer.updatePlaybackPosition(position.toLong())
                    }
                }
            }
        }
    }

    /**
     * 🔥 Периодическое обновление notification с прогрессом воспроизведения
     */
    private fun startNotificationUpdater() {
        viewModelScope.launch {
            while (isActive) {
                delay(5000) // обновление каждые 5 секунд
                if (_isPlaying.value && _currentPlayingTrackId.value != null) {
                    updateNotification()
                }
            }
        }
    }

    fun seekTo(position: Float) {
        audioPlayer.seekTo((position * 1000).toInt()) // секунды → миллисекунды
        _currentPosition.value = position
    }

    fun playTrack(trackId: Int?) {
        if (trackId == null) return

        Log.d("PlaylistViewModel", "🎵 Starting playback: trackId=$trackId")

        // Получаем информацию о треке
        val track = _tracks.value.firstOrNull { it.id == trackId }
        if (track == null) {
            Log.e("PlaylistViewModel", "❌ Track not found: $trackId")
            return
        }

        // ПРАВИЛЬНО: слэш между частями, & перед параметрами
        val streamUrl = "${RetrofitInstance.BASE_URL.trimEnd('/')}/assistant/stream/$trackId?account_id=$accountId"

        Log.d("PlaylistViewModel", "Stream URL: $streamUrl")

        // Останавливаем текущий трек перед запуском нового
        if (_currentPlayingTrackId.value != null && _currentPlayingTrackId.value != trackId) {
            Log.d("PlaylistViewModel", "🛑 Stopping previous track: ${_currentPlayingTrackId.value}")
            audioPlayer.stop()
        }

        // 🔥 Обновляем метаданные в AudioPlayer для MediaSession
        audioPlayer.updateTrackMetadata(
            title = track.title,
            artist = track.artist ?: "Victor AI",
            duration = (track.duration * 1000).toLong() // секунды -> миллисекунды
        )

        audioPlayer.playFromUrl(streamUrl)
        _currentPlayingTrackId.value = trackId
        _isPlaying.value = true
        _currentPosition.value = 0f

        // 🔥 Запускаем Foreground Service с MediaStyle уведомлением
        try {
            val sessionToken = audioPlayer.getMediaSessionToken()
            Log.d("PlaylistViewModel", "🔑 MediaSession token: ${if (sessionToken != null) "✅ present" else "❌ null"}")

            MusicPlaybackService.startPlayback(
                context = applicationContext,
                trackTitle = track.title,
                trackArtist = track.artist ?: "Victor AI",
                isPlaying = true,
                sessionToken = sessionToken,
                duration = track.duration.toLong(),  // 🔥 Длительность в секундах
                position = 0  // 🔥 Начинаем с 0
            )
            Log.d("PlaylistViewModel", "✅ Foreground service started with media notification")
        } catch (e: Exception) {
            Log.e("PlaylistViewModel", "⚠️ Failed to start foreground service: ${e.message}")
        }
    }

    fun pauseTrack() {
        Log.d("PlaylistViewModel", "⏸️ Pausing track")
        audioPlayer.pause()
        _isPlaying.value = false
        updateNotification()  // 🔥 Обновляем уведомление
    }

    fun resumeTrack() {
        Log.d("PlaylistViewModel", "▶️ Resuming track")
        audioPlayer.resume()
        _isPlaying.value = true
        updateNotification()  // 🔥 Обновляем уведомление
    }

    /**
     * 🔥 Воспроизведение следующего трека
     */
    fun playNextTrack() {
        val filteredTracks = getFilteredTracks()
        if (filteredTracks.isEmpty()) {
            Log.w("PlaylistViewModel", "⚠️ No tracks available for next")
            return
        }

        val currentId = _currentPlayingTrackId.value
        val currentIndex = filteredTracks.indexOfFirst { it.id == currentId }

        // Выбираем следующий трек, если текущий не найден - начинаем с начала
        val nextIndex = if (currentIndex == -1) {
            0
        } else {
            (currentIndex + 1) % filteredTracks.size  // По кругу
        }

        val nextTrack = filteredTracks[nextIndex]
        Log.d("PlaylistViewModel", "⏭️ Playing next track: ${nextTrack.title}")
        playTrack(nextTrack.id)
    }

    /**
     * 🔥 Воспроизведение предыдущего трека
     */
    fun playPreviousTrack() {
        val filteredTracks = getFilteredTracks()
        if (filteredTracks.isEmpty()) {
            Log.w("PlaylistViewModel", "⚠️ No tracks available for previous")
            return
        }

        val currentId = _currentPlayingTrackId.value
        val currentIndex = filteredTracks.indexOfFirst { it.id == currentId }

        // Выбираем предыдущий трек
        val previousIndex = if (currentIndex <= 0) {
            filteredTracks.size - 1  // Переход на последний трек
        } else {
            currentIndex - 1
        }

        val previousTrack = filteredTracks[previousIndex]
        Log.d("PlaylistViewModel", "⏮️ Playing previous track: ${previousTrack.title}")
        playTrack(previousTrack.id)
    }

    /**
     * 🔥 Обновление уведомления при изменении состояния
     */
    private fun updateNotification() {
        val currentTrack = _tracks.value.firstOrNull { it.id == _currentPlayingTrackId.value }
        if (currentTrack == null) {
            Log.w("PlaylistViewModel", "⚠️ No current track to update notification")
            return
        }

        try {
            val currentPositionMs = audioPlayer.getCurrentPosition()
            val currentPositionSec = (currentPositionMs / 1000).toLong()

            MusicPlaybackService.updateNotification(
                context = applicationContext,
                trackTitle = currentTrack.title,
                trackArtist = currentTrack.artist ?: "Victor AI",
                isPlaying = _isPlaying.value,
                sessionToken = audioPlayer.getMediaSessionToken(),
                duration = currentTrack.duration.toLong(),  // 🔥 Длительность в секундах
                position = currentPositionSec  // 🔥 Текущая позиция в секундах
            )
            Log.d("PlaylistViewModel", "🔄 Notification updated: ${currentTrack.title} (playing=${_isPlaying.value})")
        } catch (e: Exception) {
            Log.e("PlaylistViewModel", "⚠️ Failed to update notification: ${e.message}")
        }
    }

    // 🔥 НОВОЕ: синхронизация состояния UI с реальным состоянием плеера
    fun syncPlayerState() {
        val realIsPlaying = audioPlayer.isPlaying()
        val currentId = _currentPlayingTrackId.value

        Log.d("PlaylistViewModel", "🔄 syncPlayerState called: currentId=$currentId, realIsPlaying=$realIsPlaying, viewModelIsPlaying=${_isPlaying.value}")

        if (_isPlaying.value != realIsPlaying) {
            Log.d("PlaylistViewModel", "🔄 Syncing isPlaying: was=${_isPlaying.value}, now=$realIsPlaying")
            _isPlaying.value = realIsPlaying
        }
    }

    // 🔥 НОВОЕ: Установка фильтров
    fun setFilters(energy: String?, temperature: String?, sortBy: String) {
        _energyFilter.value = energy
        _temperatureFilter.value = temperature
        _sortBy.value = sortBy
    }

    // 🔥 НОВОЕ: Получение отфильтрованного списка треков
    private fun getFilteredTracks(): List<Track> {
        return _tracks.value
            .filter { track ->
                (_energyFilter.value == null || track.energyDescription == _energyFilter.value) &&
                (_temperatureFilter.value == null || track.temperatureDescription == _temperatureFilter.value)
            }
            .let { list ->
                when (_sortBy.value) {
                    "title" -> list.sortedBy { it.title }
                    "artist" -> list.sortedBy { it.artist }
                    "duration" -> list.sortedByDescending { it.duration }
                    else -> list.sortedByDescending { it.id } // recent
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("PlaylistViewModel", "💀 ViewModel onCleared() - DESTROYING")
        Log.d("PlaylistViewModel", "💀 Current state: trackId=${_currentPlayingTrackId.value}, isPlaying=${_isPlaying.value}")

        // 🔥 Отменяем регистрацию BroadcastReceiver
        try {
            applicationContext.unregisterReceiver(mediaCommandReceiver)
            Log.d("PlaylistViewModel", "✅ MediaCommandReceiver unregistered")
        } catch (e: Exception) {
            Log.e("PlaylistViewModel", "⚠️ Failed to unregister receiver: ${e.message}")
        }

        // 🔥 Полная очистка AudioPlayer (включая MediaSession)
        audioPlayer.release()

        // 🔥 Останавливаем Foreground Service при уничтожении ViewModel
        try {
            MusicPlaybackService.stopPlayback(applicationContext)
        } catch (e: Exception) {
            Log.e("PlaylistViewModel", "⚠️ Failed to stop foreground service: ${e.message}")
        }
    }

    fun updateDescription(
        trackId: String,
        energy: String?,
        temperature: String?
    ) {
        viewModelScope.launch {
            try {
                apiService.updateTrackDescription(
                    TrackDescriptionUpdate(
                        account_id = accountId,
                        track_id = trackId,
                        energy_description = energy,
                        temperature_description = temperature
                    )
                )
                loadTracks()
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Error updating description", e)
            }
        }
    }


    fun loadStats() {
        viewModelScope.launch {
            try {
                val result = apiService.getTrackStats(accountId = accountId)
                _stats.value = result
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Error loading stats", e)
            }
        }
    }

    fun runPlaylistWave(manual: Boolean = false) {
        viewModelScope.launch {
            try {
                val response = api.runPlaylistChain(
                    accountId = accountId,
                    extraContext = if (manual) "manual" else "auto"
                )

                Log.d("Playlist", "Wave result: $response")

                val trackMap = response["track"] as? Map<*, *>
                val trackId = (trackMap?.get("track_id") as? Double)?.toInt()
                val trackName = trackMap?.get("track") as? String
                val contextText = response["context"] as? String

                if (trackId != null) {
                    Log.d("Playlist", "🎧 Playing track $trackName ($trackId)")
                    // ✅ Запускаем через собственный плеер
                    playTrack(trackId)
                }

                Log.d("Playlist", "🪶 Context: $contextText")

            } catch (e: Exception) {
                Log.e("Playlist", "Ошибка запуска волны", e)
            }
        }
    }
}

