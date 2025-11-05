package com.example.victor_ai.ui.map

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.repository.VisitedPlacesRepository
import com.example.victor_ai.ui.map.utils.LocationUtils
import com.example.victor_ai.ui.places.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.cos

/**
 * 🗺️ ViewModel для MapActivity
 *
 * Решает проблемы:
 * - Сохранение данных карты при пересоздании Activity (rotation, low memory)
 * - Фильтрация плохих GPS координат
 * - Управление состоянием загрузки
 */
class MapViewModel(
    private val placesApi: PlacesApi = RetrofitInstance.placesApi,
    private val repository: VisitedPlacesRepository? = null
) : ViewModel() {

    companion object {
        private const val TAG = "MapViewModel"
        private const val GPS_ACCURACY_THRESHOLD = 50f // Метры - игнорируем координаты хуже 50м
    }

    // Основные данные карты
    private val _mapBounds = MutableStateFlow<MapBounds?>(null)
    val mapBounds: StateFlow<MapBounds?> = _mapBounds.asStateFlow()

    private val _pois = MutableStateFlow<List<POI>>(emptyList())
    val pois: StateFlow<List<POI>> = _pois.asStateFlow()

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    private val _selectedPOI = MutableStateFlow<POI?>(null)
    val selectedPOI: StateFlow<POI?> = _selectedPOI.asStateFlow()

    // Состояние загрузки
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Поиск
    private val _searching = MutableStateFlow(false)
    val searching: StateFlow<Boolean> = _searching.asStateFlow()

    private val _searchStart = MutableStateFlow<Long?>(null)
    val searchStart: StateFlow<Long?> = _searchStart.asStateFlow()

    private val _elapsedSec = MutableStateFlow(0L)
    val elapsedSec: StateFlow<Long> = _elapsedSec.asStateFlow()

    private val _walkedMeters = MutableStateFlow(0.0)
    val walkedMeters: StateFlow<Double> = _walkedMeters.asStateFlow()

    private val _path = MutableStateFlow<List<LatLng>>(emptyList())
    val path: StateFlow<List<LatLng>> = _path.asStateFlow()

    private val _nearby = MutableStateFlow<List<POI>>(emptyList())
    val nearby: StateFlow<List<POI>> = _nearby.asStateFlow()

    private var lastPoint: LatLng? = null
    private var lastAccurateLocation: LatLng? = null // Последняя точная локация

    /**
     * Загружает данные карты вокруг указанной точки
     */
    fun loadMapData(location: LatLng, radiusMeters: Int = 10000) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                Log.d(TAG, "🔍 Начинаем загрузку карты для location: lat=${location.lat}, lon=${location.lon}, radius=${radiusMeters}м")

                val mapData = loadPlacesData(location, radiusMeters)

                _mapBounds.value = mapData.bounds
                _pois.value = mapData.pois
                _userLocation.value = mapData.userLocation

                Log.d(TAG, "✅ Карта загружена: ${mapData.pois.size} POI")

                if (mapData.pois.isEmpty()) {
                    Log.w(TAG, "⚠️ Бэкенд вернул 0 POI! Проверь данные на сервере или bbox параметры")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка загрузки карты", e)
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Обновляет позицию пользователя с фильтрацией по точности
     *
     * @param location Новая локация
     * @param accuracy Точность GPS в метрах (null = не фильтровать)
     * @return true если локация принята, false если отфильтрована
     */
    fun updateUserLocation(location: LatLng, accuracy: Float? = null): Boolean {
        // Фильтруем плохие координаты
        if (accuracy != null && accuracy > GPS_ACCURACY_THRESHOLD) {
            Log.w(TAG, "GPS координата отфильтрована: accuracy=$accuracy м (требуется <$GPS_ACCURACY_THRESHOLD м)")
            return false
        }

        _userLocation.value = location
        lastAccurateLocation = location

        // Если идёт поиск - обновляем путь
        if (_searching.value) {
            updateSearchPath(location)
        }

        return true
    }

    /**
     * Обновляет путь во время поиска
     */
    private fun updateSearchPath(newLocation: LatLng) {
        val prev = lastPoint
        if (prev != null) {
            val distance = LocationUtils.calculateDistance(prev, newLocation)

            // Фильтруем шум < 2.5 м
            if (distance > 2.5) {
                _walkedMeters.value += distance
                _path.value = _path.value + newLocation
            }
        } else {
            _path.value = listOf(newLocation)
        }
        lastPoint = newLocation
    }

    /**
     * Устанавливает выбранный POI
     */
    fun setSelectedPOI(poi: POI?) {
        _selectedPOI.value = poi
    }

    /**
     * Обновляет список POI
     */
    fun updatePOIs(newPOIs: List<POI>) {
        _pois.value = newPOIs
    }

    /**
     * Запускает поиск
     */
    fun startSearch(currentPOI: POI, allPOIs: List<POI>, radiusM: Int = 400, limit: Int = 6) {
        _searching.value = true
        _searchStart.value = System.currentTimeMillis()
        _elapsedSec.value = 0L
        _walkedMeters.value = 0.0
        lastPoint = _userLocation.value

        val userLoc = _userLocation.value
        _path.value = if (userLoc != null) listOf(userLoc) else emptyList()

        // Вычисляем nearby POI
        _nearby.value = calcNearby(currentPOI, allPOIs, radiusM, limit)
    }

    /**
     * Останавливает поиск
     */
    fun stopSearch() {
        _searching.value = false
        _searchStart.value = null
        lastPoint = null
    }

    /**
     * Обновляет elapsed секунды
     */
    fun updateElapsedTime(seconds: Long) {
        _elapsedSec.value = seconds
    }

    /**
     * Вычисляет ближайшие POI к выбранному
     */
    private fun calcNearby(centerPoi: POI, all: List<POI>, radiusM: Int, limit: Int): List<POI> {
        return all.asSequence()
            .filter { it.id != centerPoi.id }
            .filter { LocationUtils.calculateDistance(centerPoi.location, it.location) <= radiusM }
            .sortedBy { LocationUtils.calculateDistance(centerPoi.location, it.location) }
            .take(limit)
            .toList()
    }

    /**
     * Загружает места из API
     */
    private suspend fun loadPlacesData(
        location: LatLng,
        radiusMeters: Int
    ): MapData = withContext(Dispatchers.IO) {
        val bbox = calculateBoundingBox(location.lat, location.lon, radiusMeters)
        val bboxString = "${bbox.minLon},${bbox.minLat},${bbox.maxLon},${bbox.maxLat}"

        Log.d(TAG, "📦 Запрашиваем данные с bbox: $bboxString")

        val placesResponse = placesApi.getPlaces(
            limit = 15000,
            bbox = bboxString
        )

        Log.d(TAG, "📥 Ответ от бэкенда: count=${placesResponse.count}, items.size=${placesResponse.items.size}")

        val visitedPlaceIds = repository?.getVisitedPlaceIds() ?: emptySet()
        val bounds = MapBounds.fromCenterAndRadius(location, radiusMeters)

        val mapData = MapDataConverter.fromBackendResponse(
            response = placesResponse,
            bounds = bounds,
            visitedPlaceIds = visitedPlaceIds
        )

        Log.d(TAG, "🔄 После конвертации: pois.size=${mapData.pois.size}")

        // Обновляем POI с впечатлениями
        mapData.pois.forEach { poi ->
            if (poi.isVisited && repository != null) {
                poi.impression = repository.getImpression(poi.id)
                poi.visitDate = repository.getVisitDate(poi.id)
            }
        }

        mapData.copy(userLocation = location)
    }

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
        val latDelta = radiusMeters / 111_000.0
        val lonDelta = radiusMeters / (111_000.0 * cos(Math.toRadians(lat)))

        return BBox(
            minLat = lat - latDelta,
            minLon = lon - lonDelta,
            maxLat = lat + latDelta,
            maxLon = lon + lonDelta
        )
    }

    /**
     * Получает последнюю точную локацию (для восстановления после плохого GPS)
     */
    fun getLastAccurateLocation(): LatLng? = lastAccurateLocation
}

/**
 * Factory для создания MapViewModel с зависимостями
 */
class MapViewModelFactory(
    private val placesApi: PlacesApi,
    private val repository: VisitedPlacesRepository?
) : androidx.lifecycle.ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(placesApi, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
