package com.example.victor_ai.ui.places

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.network.dto.PlaceDto
import com.example.victor_ai.data.network.dto.JournalEntry
import com.example.victor_ai.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelProvider
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.ui.map.models.PlaceElement
import kotlin.math.cos


class PlacesViewModel(
    private val placesApi: PlacesApi = RetrofitInstance.placesApi,
    private val statsRepository: StatsRepository? = null
) : ViewModel() {

    private val _places = mutableStateOf<List<PlaceElement>>(emptyList())
    val places: State<List<PlaceElement>> = _places

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // Статистика
    private val _stats = mutableStateOf<StatsRepository.LocalStats?>(null)
    val stats: State<StatsRepository.LocalStats?> = _stats

    private val _lastJournalEntry = mutableStateOf<JournalEntry?>(null)
    val lastJournalEntry: State<JournalEntry?> = _lastJournalEntry

    private val _statsLoading = mutableStateOf(false)
    val statsLoading: State<Boolean> = _statsLoading

    /**
     * Загружает места вокруг координаты
     */
    fun loadPlacesAround(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int = 1000,
        limit: Int = 1000
    ) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                // Вычисляем bbox
                val bbox = calculateBoundingBox(latitude, longitude, radiusMeters)
                val bboxString = "${bbox.minLon},${bbox.minLat},${bbox.maxLon},${bbox.maxLat}"

                // Запрос к API
                val response = placesApi.getPlaces(
                    limit = limit,
                    offset = 0,
                    bbox = bboxString
                )

                _places.value = response.items

                Log.d("PlacesVM", "Загружено мест: ${response.count}")

            } catch (e: Exception) {
                Log.e("PlacesVM", "Ошибка загрузки мест", e)
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Загружает все места (без фильтра)
     */
    fun loadAllPlaces(limit: Int = 1000) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val response = placesApi.getPlaces(
                    limit = limit,
                    offset = 0,
                    bbox = null // без фильтра
                )

                _places.value = response.items

            } catch (e: Exception) {
                Log.e("PlacesVM", "Ошибка загрузки мест", e)
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Вычисляет bounding box вокруг точки
     */
    private data class BBox(
        val minLat: Double,
        val minLon: Double,
        val maxLat: Double,
        val maxLon: Double
    )

    private fun calculateBoundingBox(
        lat: Double,
        lon: Double,
        radiusMeters: Int
    ): BBox {
        // 1° широты ≈ 111 км
        val latDelta = radiusMeters / 111_000.0

        // 1° долготы зависит от широты
        val lonDelta = radiusMeters / (111_000.0 * cos(Math.toRadians(lat)))

        return BBox(
            minLat = lat - latDelta,
            minLon = lon - lonDelta,
            maxLat = lat + latDelta,
            maxLon = lon + lonDelta
        )
    }

    /**
     * Загружает статистику пользователя
     */
    fun loadStats() {
        if (statsRepository == null) {
            Log.w("PlacesVM", "StatsRepository не инициализирован")
            return
        }

        viewModelScope.launch {
            _statsLoading.value = true

            try {
                // Сначала загружаем локальные данные
                val localStats = statsRepository.getLocalStats()
                _stats.value = localStats
                _lastJournalEntry.value = statsRepository.getLastJournalEntry()

                // Синхронизируем с API если нужно
                if (statsRepository.shouldSync()) {
                    val result = statsRepository.syncWithAPI()
                    result.onSuccess { updatedStats ->
                        _stats.value = updatedStats
                        _lastJournalEntry.value = statsRepository.getLastJournalEntry()
                        Log.d("PlacesVM", "✅ Статистика синхронизирована")
                    }
                }
            } catch (e: Exception) {
                Log.e("PlacesVM", "❌ Ошибка загрузки статистики", e)
            } finally {
                _statsLoading.value = false
            }
        }
    }
}

class PlacesViewModelFactory(
    private val placesApi: PlacesApi,
    private val statsRepository: StatsRepository? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlacesViewModel(placesApi, statsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}