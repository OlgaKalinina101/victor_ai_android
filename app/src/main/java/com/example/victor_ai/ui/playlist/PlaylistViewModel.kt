package com.example.victor_ai.ui.playlist

import android.content.Context
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

    init {
        Log.d("PlaylistViewModel", "🏗️ ViewModel created (init block)")
        loadTracks()
        startPositionUpdater()
        loadTracks()
        loadStats()
        // 🔥 Устанавливаем callback для автовоспроизведения следующего трека
        audioPlayer.setOnCompletionListener {
            playNextTrack()
        }
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
            while (isActive) {  // ✅ Проверка isActive - останавливаем при onCleared()
                delay(100) // обновление каждые 100мс
                if (_isPlaying.value) {
                    val position = audioPlayer.getCurrentPosition()
                    _currentPosition.value = position / 1000f // миллисекунды → секунды
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

        // ПРАВИЛЬНО: слэш между частями, & перед параметрами
        val streamUrl = "${RetrofitInstance.BASE_URL.trimEnd('/')}/assistant/stream/$trackId?account_id=$accountId"

        Log.d("PlaylistViewModel", "Stream URL: $streamUrl")

        // Останавливаем текущий трек перед запуском нового
        if (_currentPlayingTrackId.value != null && _currentPlayingTrackId.value != trackId) {
            Log.d("PlaylistViewModel", "🛑 Stopping previous track: ${_currentPlayingTrackId.value}")
            audioPlayer.stop()
        }

        // 🔥 НОВОЕ: Запускаем Foreground Service чтобы защититься от Doze mode
        // Примечание: Service показывает уведомление, но AudioPlayer остается в ViewModel
        // TODO: В будущем переместить AudioPlayer в Service для лучшей архитектуры
        try {
            MusicPlaybackService.startPlayback(applicationContext, streamUrl)
            Log.d("PlaylistViewModel", "✅ Foreground service started")
        } catch (e: Exception) {
            Log.e("PlaylistViewModel", "⚠️ Failed to start foreground service: ${e.message}")
            // Продолжаем воспроизведение даже если сервис не запустился
        }

        audioPlayer.playFromUrl(streamUrl)
        _currentPlayingTrackId.value = trackId
        _isPlaying.value = true
        _currentPosition.value = 0f
    }

    fun pauseTrack() {
        Log.d("PlaylistViewModel", "⏸️ Pausing track")
        audioPlayer.pause()
        _isPlaying.value = false  // ← ДОБАВЛЕНО
    }

    fun resumeTrack() {
        Log.d("PlaylistViewModel", "▶️ Resuming track")
        audioPlayer.resume()
        _isPlaying.value = true  // ← ДОБАВЛЕНО
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

    // 🔥 НОВОЕ: Автовоспроизведение следующего трека
    private fun playNextTrack() {
        val filteredTracks = getFilteredTracks()
        if (filteredTracks.isEmpty()) return

        val currentId = _currentPlayingTrackId.value
        val currentIndex = filteredTracks.indexOfFirst { it.id == currentId }

        // Выбираем следующий трек, если текущий не найден - начинаем с начала
        val nextIndex = if (currentIndex == -1) {
            0
        } else {
            (currentIndex + 1) % filteredTracks.size  // По кругу
        }

        val nextTrack = filteredTracks[nextIndex]
        Log.d("PlaylistViewModel", "Auto-playing next track: ${nextTrack.title}")
        playTrack(nextTrack.id)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("PlaylistViewModel", "💀 ViewModel onCleared() - DESTROYING")
        Log.d("PlaylistViewModel", "💀 Current state: trackId=${_currentPlayingTrackId.value}, isPlaying=${_isPlaying.value}")
        audioPlayer.stop()

        // 🔥 НОВОЕ: Останавливаем Foreground Service при уничтожении ViewModel
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

