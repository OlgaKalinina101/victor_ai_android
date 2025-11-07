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
import com.example.victor_ai.ui.map.models.*
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
        private const val RETRY_RESET_DISTANCE = 500f // Уменьшим до 500м
        private const val MIN_RETRY_INTERVAL_MS = 10000L // Минимум 10 сек между попытками
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

    private var mapDataLoaded = false
    private var loadRetryCount = 0
    private var lastRetryLocation: LatLng? = null

    private var lastRetryTime = 0L

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

                // ДОБАВЬ ЭТО:
                if (mapData.pois.isNotEmpty()) {
                    mapDataLoaded = true
                    loadRetryCount = 0 // Сбрасываем счетчик при успехе
                    Log.d(TAG, "✅ Карта успешно загружена, больше не будем retry")
                }

                if (mapData.pois.isEmpty()) {
                    Log.w(TAG, "⚠️ Бэкенд вернул 0 POI! Проверь данные на сервере или bbox параметры")
                }

                // Загружаем посещенные места из journal
                loadVisitedPlacesFromJournal()
            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка загрузки карты (попытка #$loadRetryCount)", e)
                _error.value = e.message ?: "Неизвестная ошибка"
                // НЕ устанавливаем mapDataLoaded = true при ошибке!
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

                    // Парсим эмоции из текста
                    val visitedMap = mutableMapOf<String, VisitEmotion>()

                    entries.forEach { entry ->
                        entry.poi_name?.let { poiName ->
                            // Парсим эмодзи из текста типа "Посетил Тануки. Впечатление: Неплохо 🙂"
                            val emotion = parseEmotionFromText(entry.text)
                            if (emotion != null) {
                                visitedMap[poiName] = emotion
                                Log.d(TAG, "📍 Восстановлено посещение: $poiName -> ${emotion.name} ${emotion.emoji}")
                            }
                        }
                    }

                    // Обновляем состояние (переключаемся на Main thread)
                    withContext(Dispatchers.Main) {
                        _visitedPOIs.value = visitedMap
                        Log.d(TAG, "✅ Восстановлено ${visitedMap.size} посещенных мест")
                    }
                } else {
                    Log.e(TAG, "❌ Ошибка загрузки journal: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при загрузке journal", e)
            }
        }
    }

    /**
     * Парсит эмоцию из текста журнала
     * Формат: "Посетил {poi}. Впечатление: {name} {emoji}"
     */
    private fun parseEmotionFromText(text: String): VisitEmotion? {
        // Ищем эмодзи в конце текста
        val emojiRegex = "[\\p{So}\\p{Sk}]".toRegex()
        val matches = emojiRegex.findAll(text).toList()

        if (matches.isEmpty()) return null

        // Берем последний эмодзи
        val emoji = matches.last().value

        // Ищем соответствующую эмоцию в списке
        return VISIT_EMOTIONS.find { it.emoji == emoji }
    }

    /**
     * Обновляет позицию пользователя - принимает все координаты и выводит логи
     *
     * @param location Новая локация
     * @param accuracy Точность GPS в метрах (только для логов)
     * @return всегда true
     */
    fun updateUserLocation(location: LatLng, accuracy: Float? = null): Boolean {
        val qualityEmoji = when {
            accuracy == null -> "❓"
            accuracy < 10f -> "🎯"
            accuracy < 30f -> "✅"
            accuracy < 100f -> "🟡"
            accuracy < 200f -> "🟠"
            else -> "🔴"
        }

        Log.d(TAG, "📍 GPS получена: $qualityEmoji accuracy=${accuracy ?: "неизвестно"} м, координаты=${location.lat}, ${location.lon}")

        // Принимаем все координаты как есть
        _userLocation.value = location

        // Проверяем, далеко ли ушли от места последних попыток
        lastRetryLocation?.let { lastLoc ->
            val distance = LocationUtils.calculateDistance(location, lastLoc)
            if (distance > RETRY_RESET_DISTANCE) {
                Log.d(TAG, "🔄 Пользователь ушел на ${distance.toInt()}м - сбрасываем retry счетчик")
                loadRetryCount = 0
                lastRetryTime = 0 // Сбрасываем таймер
            }
        }

        val currentTime = System.currentTimeMillis()

        // УБИРАЕМ ВСЕ ЛИМИТЫ! Пытаемся до победного с интервалами
        if (!mapDataLoaded && (currentTime - lastRetryTime) > MIN_RETRY_INTERVAL_MS) {
            loadRetryCount++
            lastRetryLocation = location
            lastRetryTime = currentTime

            Log.d(TAG, "🔄 Попытка загрузки карты #$loadRetryCount (БЕЗ ЛИМИТОВ, до победного!)")
            loadMapData(location, 1000)
        } else if (!mapDataLoaded && (currentTime - lastRetryTime) <= MIN_RETRY_INTERVAL_MS) {
            val remainingMs = MIN_RETRY_INTERVAL_MS - (currentTime - lastRetryTime)
            Log.d(TAG, "⏳ Следующая попытка через ${remainingMs/1000} сек (retry #${loadRetryCount + 1})")
        }

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
                    account_id = "test_user",
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
                    text = "Сидели в ${poi.name}. Впечатление: ${emotion.name} ${emotion.emoji}",
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
