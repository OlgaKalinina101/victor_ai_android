package com.example.victor_ai.ui.map

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.repository.VisitedPlacesRepository
import com.example.victor_ai.data.repository.StatsRepository
import com.example.victor_ai.ui.map.utils.LocationUtils
import com.example.victor_ai.ui.places.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.cos
import com.example.victor_ai.data.network.dto.*
import java.time.Instant
import java.time.format.DateTimeFormatter

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
    private val repository: VisitedPlacesRepository? = null,
    private val statsRepository: StatsRepository? = null
) : ViewModel() {

    companion object {
        private const val TAG = "MapViewModel"

        // 🔥 Реалистичные пороги GPS для города
        private const val GPS_ACCURACY_THRESHOLD = 300f // Метры - игнорируем координаты хуже 300м

        // Параметры для сглаживания GPS
        private const val GPS_EXCELLENT = 10f   // < 10м - отличная точность
        private const val GPS_GOOD = 30f        // < 30м - хорошая точность
        private const val GPS_FAIR = 100f       // < 100м - приемлемая точность
        private const val GPS_POOR = 200f       // < 200м - плохая точность
        // > 200м - очень плохая, сильное сглаживание
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

    // Посещенные POI с эмоциями (хранится только в текущей сессии)
    private val _visitedPOIs = MutableStateFlow<Map<String, VisitEmotion>>(emptyMap())
    val visitedPOIs: StateFlow<Map<String, VisitEmotion>> = _visitedPOIs.asStateFlow()

    // Список посещений для текущей walk session
    private val _currentSessionVisits = mutableListOf<POIVisit>()

    private var lastPoint: LatLng? = null
    private var lastAccurateLocation: LatLng? = null // Последняя точная локация
    private var currentSessionId: Int? = null // ID текущей walk session

    // 🔥 Для сглаживания GPS координат (Exponential Moving Average)
    private var smoothedLocation: LatLng? = null

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

                // Загружаем посещенные места из journal
                loadVisitedPlacesFromJournal()
            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка загрузки карты", e)
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Загружает посещенные места из journal
     */
    private fun loadVisitedPlacesFromJournal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = placesApi.getJournalEntries("test_user") // TODO: Получать из настроек
                if (response.isSuccessful) {
                    val entries = response.body() ?: emptyList()
                    Log.d(TAG, "✅ Загружено ${entries.size} записей из дневника")

                    // Пока не можем восстановить эмоции из journal (нужно расширить API)
                    // Просто пометим как посещенные без эмоций
                    // Это задел на будущее
                } else {
                    Log.e(TAG, "❌ Ошибка загрузки journal: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при загрузке journal", e)
            }
        }
    }

    /**
     * Обновляет позицию пользователя с фильтрацией по точности и сглаживанием
     *
     * @param location Новая локация
     * @param accuracy Точность GPS в метрах (null = не фильтровать)
     * @return true если локация принята, false если отфильтрована
     */
    fun updateUserLocation(location: LatLng, accuracy: Float? = null): Boolean {
        // Первую координату принимаем ВСЕГДА (чтобы хоть что-то показать)
        val isFirstLocation = smoothedLocation == null

        if (isFirstLocation) {
            Log.d(TAG, "📍 Первая GPS координата: accuracy=$accuracy м (принимаем всегда)")
            smoothedLocation = location
            _userLocation.value = location
            return true
        }

        // Отбрасываем только ОЧЕНЬ плохие координаты (> 300м)
        if (accuracy != null && accuracy > GPS_ACCURACY_THRESHOLD) {
            Log.w(TAG, "❌ GPS отфильтрована: accuracy=$accuracy м (порог $GPS_ACCURACY_THRESHOLD м)")
            // Используем последнюю сглаженную координату вместо скачков
            _userLocation.value = smoothedLocation
            return false
        }

        // 🔥 Сглаживание с Exponential Moving Average
        // Вычисляем вес (alpha) в зависимости от точности
        val alpha = when {
            accuracy == null -> 0.3f  // Неизвестная точность - сильное сглаживание
            accuracy < GPS_EXCELLENT -> 0.7f  // < 10м: отличная - большой вес новой точке
            accuracy < GPS_GOOD -> 0.5f       // < 30м: хорошая - средний вес
            accuracy < GPS_FAIR -> 0.3f       // < 100м: приемлемая - больше сглаживаем
            accuracy < GPS_POOR -> 0.15f      // < 200м: плохая - сильное сглаживание
            else -> 0.05f                     // 200-300м: очень плохая - максимальное сглаживание
        }

        val smoothedLat = alpha * location.lat + (1 - alpha) * smoothedLocation!!.lat
        val smoothedLon = alpha * location.lon + (1 - alpha) * smoothedLocation!!.lon
        val smoothed = LatLng(smoothedLat, smoothedLon)

        val qualityEmoji = when {
            accuracy == null -> "❓"
            accuracy < GPS_EXCELLENT -> "🎯"
            accuracy < GPS_GOOD -> "✅"
            accuracy < GPS_FAIR -> "🟡"
            accuracy < GPS_POOR -> "🟠"
            else -> "🔴"
        }

        Log.d(TAG, "📍 GPS сглажена: $qualityEmoji accuracy=$accuracy м, alpha=$alpha, смещение=${
            LocationUtils.calculateDistance(location, smoothed).toInt()
        }м")

        smoothedLocation = smoothed
        _userLocation.value = smoothed

        // Сохраняем точные координаты отдельно
        if (accuracy != null && accuracy < GPS_GOOD) {
            lastAccurateLocation = smoothed
        }

        // Если идёт поиск - обновляем путь
        if (_searching.value) {
            updateSearchPath(smoothed)
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
        Log.d(TAG, "🚀 Начинаем поиск для POI: ${currentPOI.name}")
        _searching.value = true
        _searchStart.value = System.currentTimeMillis()
        _elapsedSec.value = 0L
        _walkedMeters.value = 0.0
        lastPoint = _userLocation.value

        val userLoc = _userLocation.value
        _path.value = if (userLoc != null) listOf(userLoc) else emptyList()

        // Вычисляем nearby POI
        _nearby.value = calcNearby(currentPOI, allPOIs, radiusM, limit)
        Log.d(TAG, "✅ Поиск запущен. Nearby POI: ${_nearby.value.size}")
    }

    /**
     * Останавливает поиск и сохраняет walk session
     */
    fun stopSearch() {
        Log.d(TAG, "🛑 stopSearch() вызван")
        Log.d(TAG, "   - searching: ${_searching.value}")
        Log.d(TAG, "   - walkedMeters: ${_walkedMeters.value}")
        Log.d(TAG, "   - path.size: ${_path.value.size}")
        Log.d(TAG, "   - visits.size: ${_currentSessionVisits.size}")

        // 🔥 ВАЖНО: Сохраняем startTime ПЕРЕД обнулением!
        val startTime = _searchStart.value

        // Сохраняем walk session перед остановкой
        if (_searching.value && startTime != null) {
            Log.d(TAG, "💾 Сохраняем walk session с startTime=$startTime...")
            saveWalkSession(startTime)  // Передаем явно, чтобы избежать race condition
        } else {
            Log.w(TAG, "⚠️ Walk session НЕ сохранена (searching=${_searching.value}, searchStart=$startTime)")
        }

        _searching.value = false
        _searchStart.value = null  // Теперь можем безопасно обнулить
        lastPoint = null
        _currentSessionVisits.clear()
        Log.d(TAG, "✅ stopSearch() завершен")
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

    /**
     * Отмечает POI как посещенное с эмоцией
     */
    fun markPOIAsVisited(poi: POI, emotion: VisitEmotion?) {
        Log.d(TAG, "🏷️ markPOIAsVisited вызван")
        Log.d(TAG, "   - POI: ${poi.name} (id=${poi.id})")
        Log.d(TAG, "   - Эмоция: ${emotion?.name} ${emotion?.emoji}")

        if (emotion != null) {
            // Добавляем в карту посещенных
            val newMap = _visitedPOIs.value + (poi.name to emotion)
            _visitedPOIs.value = newMap

            Log.d(TAG, "✅ POI добавлен в посещенные")
            Log.d(TAG, "   - Текущая карта посещений: ${_visitedPOIs.value.keys}")
            Log.d(TAG, "   - Размер карты: ${_visitedPOIs.value.size}")

            // Если идет walk session, добавляем в список посещений
            if (_searching.value) {
                val visit = POIVisit(
                    poi_id = poi.id,
                    poi_name = poi.name,
                    distance_from_start = _walkedMeters.value.toFloat(),
                    found_at = Instant.now().toString(),
                    emotion_emoji = emotion.emoji,
                    emotion_label = emotion.name,
                    emotion_color = String.format("#%06X", (0xFFFFFF and emotion.color.value.toInt()))
                )
                _currentSessionVisits.add(visit)
                Log.d(TAG, "   - Добавлен в session visits (всего: ${_currentSessionVisits.size})")
            }

            // Сохраняем в journal
            saveJournalEntry(poi, emotion)
        } else {
            // Убираем из посещенных (если эмоция null)
            _visitedPOIs.value = _visitedPOIs.value - poi.name
            Log.d(TAG, "❌ POI удален из посещенных: ${poi.name}")
        }
    }

    /**
     * Проверяет, посещен ли POI
     */
    fun isPOIVisited(poiName: String): Boolean {
        val isVisited = _visitedPOIs.value.containsKey(poiName)
        Log.d(TAG, "🔍 isPOIVisited('$poiName') = $isVisited")
        Log.d(TAG, "   - Ключи в карте: ${_visitedPOIs.value.keys}")
        return isVisited
    }

    /**
     * Получает эмоцию для посещенного POI
     */
    fun getVisitEmotion(poiName: String): VisitEmotion? {
        return _visitedPOIs.value[poiName]
    }

    /**
     * Сохраняет запись в дневник о посещении POI
     */
    private fun saveJournalEntry(poi: POI, emotion: VisitEmotion) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Бэкенд требует только дату без времени: "2025-11-07"
                val dateOnly = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    java.time.LocalDate.now().toString()
                } else {
                    // Fallback для старых версий Android
                    java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                        .format(java.util.Date())
                }

                val entry = JournalEntryIn(
                    date = dateOnly,  // Только дата: "2025-11-07"
                    text = "Посетил ${poi.name}. Впечатление: ${emotion.name} ${emotion.emoji}",
                    photo_path = null,
                    poi_name = poi.name,
                    session_id = currentSessionId,
                    account_id = "test_user" // TODO: Получать из настроек/авторизации
                )

                val response = placesApi.createJournalEntry(entry)
                if (response.isSuccessful) {
                    Log.d(TAG, "✅ Запись в дневник сохранена для ${poi.name}")
                } else {
                    Log.e(TAG, "❌ Ошибка сохранения в дневник: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при сохранении в дневник", e)
            }
        }
    }

    /**
     * Сохраняет walk session на бэкенд
     *
     * @param startTime Время начала поиска (передается явно, чтобы избежать race condition)
     */
    private fun saveWalkSession(startTime: Long) {
        Log.d(TAG, "🔥 saveWalkSession() ВЫЗВАН с startTime=$startTime")

        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "🔥 saveWalkSession() корутина ЗАПУЩЕНА")

            try {
                val endTime = System.currentTimeMillis()

                Log.d(TAG, "📦 Подготовка walk session для отправки...")
                Log.d(TAG, "   - Дистанция: ${_walkedMeters.value} м")
                Log.d(TAG, "   - Время: ${(endTime - startTime) / 1000} сек")
                Log.d(TAG, "   - Путь: ${_path.value.size} точек")
                Log.d(TAG, "   - Посещения: ${_currentSessionVisits.size}")

                // Конвертируем path в StepPoint
                val stepPoints = _path.value.mapIndexed { index, latLng ->
                    StepPoint(
                        lat = latLng.lat,
                        lon = latLng.lon,
                        timestamp = Instant.ofEpochMilli(startTime + (index * 5000L)).toString() // примерно каждые 5 сек
                    )
                }

                // Примерный расчет шагов (1 шаг ≈ 0.75 метра)
                val steps = (_walkedMeters.value / 0.75).toInt()

                val walkSession = WalkSessionCreate(
                    account_id = "test_user", // TODO: Получать из настроек/авторизации
                    start_time = Instant.ofEpochMilli(startTime).toString(),
                    end_time = Instant.ofEpochMilli(endTime).toString(),
                    distance_m = _walkedMeters.value.toFloat(),
                    steps = steps,
                    mode = "search", // Режим поиска POI
                    notes = "Прогулка с поиском точек интереса",
                    poi_visits = _currentSessionVisits.toList(),
                    step_points = stepPoints
                )

                Log.d(TAG, "📡 Отправляем walk session на бэкенд:")
                Log.d(TAG, "   URL: POST /api/walk_sessions/")
                Log.d(TAG, "   account_id: ${walkSession.account_id}")
                Log.d(TAG, "   distance_m: ${walkSession.distance_m}")
                Log.d(TAG, "   steps: ${walkSession.steps}")
                Log.d(TAG, "   poi_visits: ${walkSession.poi_visits.size}")
                Log.d(TAG, "   step_points: ${walkSession.step_points.size}")

                val response = placesApi.createWalkSession(walkSession)

                Log.d(TAG, "📥 Ответ от бэкенда:")
                Log.d(TAG, "   HTTP код: ${response.code()}")
                Log.d(TAG, "   Успешно: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    currentSessionId = response.body()?.session_id
                    Log.d(TAG, "✅ Walk session сохранена с ID: $currentSessionId")

                    // Обновляем локальную статистику
                    statsRepository?.let {
                        Log.d(TAG, "💾 Обновляем локальную статистику...")
                        it.addTodayDistance(_walkedMeters.value.toFloat())
                        it.addTodaySteps(steps)
                        Log.d(TAG, "✅ Локальная статистика обновлена: +${_walkedMeters.value}м, +${steps} шагов")
                    } ?: Log.w(TAG, "⚠️ statsRepository == null, локальная статистика НЕ обновлена!")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "❌ Ошибка сохранения walk session:")
                    Log.e(TAG, "   HTTP код: ${response.code()}")
                    Log.e(TAG, "   Тело ошибки: $errorBody")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при сохранении walk session", e)
                Log.e(TAG, "   Exception: ${e.message}")
                Log.e(TAG, "   Тип: ${e.javaClass.simpleName}")
            }
        }
    }
}

/**
 * Factory для создания MapViewModel с зависимостями
 */
class MapViewModelFactory(
    private val placesApi: PlacesApi,
    private val repository: VisitedPlacesRepository?,
    private val statsRepository: StatsRepository? = null
) : androidx.lifecycle.ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(placesApi, repository, statsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
