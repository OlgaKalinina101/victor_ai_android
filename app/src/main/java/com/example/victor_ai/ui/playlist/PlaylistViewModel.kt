package com.example.victor_ai.ui.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.logic.AudioPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File


class PlaylistViewModel(
    private val apiService: ApiService,
    private val accountId: String,
    private val cacheDir: File
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

    private val audioPlayer = AudioPlayer()

    init {
        loadTracks()
        startPositionUpdater()
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
            while (true) {
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

        // ПРАВИЛЬНО: слэш между частями, & перед параметрами
        val streamUrl = "${RetrofitInstance.BASE_URL.trimEnd('/')}/assistant/stream/$trackId?account_id=$accountId"

        Log.d("PlaylistViewModel", "Stream URL: $streamUrl")

        audioPlayer.playFromUrl(streamUrl)
        _currentPlayingTrackId.value = trackId
        _isPlaying.value = true
        _currentPosition.value = 0f
    }

    fun pauseTrack() {
        audioPlayer.pause()
        _isPlaying.value = false  // ← ДОБАВЛЕНО
    }

    fun resumeTrack() {
        audioPlayer.resume()
        _isPlaying.value = true  // ← ДОБАВЛЕНО
    }

    // ← ДОБАВЛЕНО: удобная функция для UI
    fun togglePlayPause() {
        if (_isPlaying.value) {
            pauseTrack()
        } else {
            resumeTrack()
        }
    }

    fun stopTrack() {
        audioPlayer.stop()
        _currentPlayingTrackId.value = null
        _isPlaying.value = false  // ← ДОБАВЛЕНО
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
        audioPlayer.stop()
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
}

