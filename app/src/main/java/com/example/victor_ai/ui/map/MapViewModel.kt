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

package com.example.victor_ai.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.network.UnlockedAchievement
import com.example.victor_ai.data.repository.StatsRepository
import com.example.victor_ai.data.repository.VisitedPlacesRepository
import com.example.victor_ai.ui.map.managers.LocationManager
import com.example.victor_ai.ui.map.managers.SearchManager
import com.example.victor_ai.ui.map.managers.VisitManager
import com.example.victor_ai.ui.map.managers.WalkSessionManager
import com.example.victor_ai.ui.map.models.*
import com.example.victor_ai.ui.map.repositories.MapDataRepository
import com.example.victor_ai.ui.map.utils.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 🗺️ ViewModel для MapActivity - главный оркестратор
 *
 * Решает проблемы:
 * - Сохранение данных карты при пересоздании Activity (rotation, low memory)
 * - Фильтрация плохих GPS координат
 * - Управление состоянием загрузки
 * 
 * Делегирует работу специализированным менеджерам:
 * - MapDataRepository - загрузка данных с API
 * - LocationManager - управление GPS и сохраненными локациями
 * - SearchManager - поиск POI и path tracking
 * - VisitManager - посещения и журнал
 * - WalkSessionManager - walk sessions
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    private val placesApi: PlacesApi,
    private val repository: VisitedPlacesRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MapViewModel"
        private const val RETRY_RESET_DISTANCE = 500f
        private const val MIN_RETRY_INTERVAL_MS = 10000L
    }

    // 📦 Менеджеры
    private val mapDataRepository = MapDataRepository(placesApi)
    private val locationManager = LocationManager(placesApi)
    private val searchManager = SearchManager()
    private val visitManager = VisitManager(placesApi)
    private val walkSessionManager = WalkSessionManager(placesApi, statsRepository)

    // Основные данные карты
    private val _mapBounds = MutableStateFlow<MapBounds?>(null)
    val mapBounds: StateFlow<MapBounds?> = _mapBounds.asStateFlow()

    private val _pois = MutableStateFlow<List<POI>>(emptyList())
    val pois: StateFlow<List<POI>> = _pois.asStateFlow()
    
    private val _backgroundElements = MutableStateFlow<List<BackgroundElement>>(emptyList())
    val backgroundElements: StateFlow<List<BackgroundElement>> = _backgroundElements.asStateFlow()

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    private val _selectedPOI = MutableStateFlow<POI?>(null)
    val selectedPOI: StateFlow<POI?> = _selectedPOI.asStateFlow()

    // Состояние загрузки
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Делегируем StateFlow из менеджеров
    val availableLocations = locationManager.availableLocations
    val currentLocationName = locationManager.currentLocationName
    val currentLocationId = locationManager.currentLocationId
    val isGPSMode = locationManager.isGPSMode
    
    val searching = searchManager.searching
    val searchStart = searchManager.searchStart
    val elapsedSec = searchManager.elapsedSec
    val walkedMeters = searchManager.walkedMeters
    val path = searchManager.path
    val nearby = searchManager.nearby
    
    val visitedPOIs = visitManager.visitedPOIs

    // Достижения
    private val _unlockedAchievements = MutableStateFlow<List<UnlockedAchievement>>(emptyList())
    val unlockedAchievements: StateFlow<List<UnlockedAchievement>> = _unlockedAchievements.asStateFlow()

    // Внутренние переменные для retry логики
    private var lastAccurateLocation: LatLng? = null
    private var mapDataLoaded = false
    private var loadRetryCount = 0
    private var lastRetryLocation: LatLng? = null
    private var lastRetryTime = 0L

    /**
     * Загружает данные карты вокруг указанной точки
     */
    fun loadMapData(location: LatLng, radiusMeters: Int = 2000) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                Log.d(TAG, "🔍 Начинаем загрузку карты для location: lat=${location.lat}, lon=${location.lon}, radius=${radiusMeters}м")

                val foundIds = repository.getVisitedPlaceIds()
                val mapData = mapDataRepository.loadPlacesAroundLocation(
                    location = location,
                    radiusMeters = radiusMeters,
                    visitedPlaceIds = foundIds
                )

                _mapBounds.value = mapData.bounds
                _pois.value = mapData.pois
                _backgroundElements.value = mapData.backgroundElements
                _userLocation.value = mapData.userLocation
                
                // 📍 Устанавливаем GPS режим
                locationManager.setGPSMode(location)

                Log.d(TAG, "✅ Карта загружена: ${mapData.pois.size} POI")

                if (mapData.pois.isNotEmpty()) {
                    mapDataLoaded = true
                    loadRetryCount = 0
                    Log.d(TAG, "✅ Карта успешно загружена, больше не будем retry")
                }

                if (mapData.pois.isEmpty()) {
                    Log.w(TAG, "⚠️ Бэкенд вернул 0 POI! Проверь данные на сервере или bbox параметры")
                }

                // Загружаем посещенные места из journal
                visitManager.loadVisitedPlacesFromJournal()
            } catch (e: Exception) {
                val errorMsg = when {
                    e is retrofit2.HttpException -> {
                        when (e.code()) {
                            503 -> "Сервер временно недоступен (503). Проверьте бэкенд или ngrok туннель."
                            404 -> "Локация не найдена (404)"
                            403 -> "Доступ запрещён (403)"
                            500 -> "Внутренняя ошибка сервера (500)"
                            else -> "Ошибка сервера: HTTP ${e.code()}"
                        }
                    }
                    e.message?.contains("timeout", ignoreCase = true) == true -> "Таймаут соединения"
                    e.message?.contains("Unable to resolve host", ignoreCase = true) == true -> "Нет соединения с интернетом"
                    else -> e.message ?: "Неизвестная ошибка"
                }
                Log.e(TAG, "❌ Ошибка загрузки карты (попытка #$loadRetryCount): $errorMsg", e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 📍 Возвращает к GPS режиму
     */
    fun returnToGPSMode() {
        val savedLocation = locationManager.getSavedGPSLocation()
        if (savedLocation != null) {
            Log.d(TAG, "🔙 Возврат к GPS режиму")
            loadMapData(savedLocation, 2000)
        } else {
            Log.w(TAG, "⚠️ GPS позиция не сохранена, невозможно вернуться")
        }
    }
    
    /**
     * 📍 Загружает список доступных локаций пользователя
     */
    fun loadLocations() {
        viewModelScope.launch {
            locationManager.loadLocations()
        }
    }
    
    /**
     * 🗑️ Удаляет локацию (soft delete)
     */
    fun deleteLocation(
        locationId: Int,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val result = locationManager.deleteLocation(locationId)
            
            result.onSuccess { message ->
                // Если удалили текущую локацию, возвращаемся к GPS
                if (locationManager.isCurrentLocation(locationId)) {
                    returnToGPSMode()
                }
                onSuccess(message)
            }.onFailure { error ->
                onError(error.message ?: "Неизвестная ошибка")
            }
        }
    }
    
    /**
     * 📍 Загружает карту для сохранённой локации (без привязки к GPS)
     */
    fun loadMapForLocation(locationId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val foundIds = repository.getVisitedPlaceIds()
                val (mapData, locationName) = mapDataRepository.loadPlacesForLocation(
                    locationId = locationId,
                    visitedPlaceIds = foundIds
                )
                val name = locationName ?: "Unknown"

                // 📍 Устанавливаем режим сохранённой локации
                locationManager.setSavedLocationMode(locationId, name)

                _mapBounds.value = mapData.bounds
                _pois.value = mapData.pois
                _backgroundElements.value = mapData.backgroundElements
                _userLocation.value = null // Нет привязки к GPS!

                Log.d(TAG, "✅ Карта для локации '$name' загружена: ${mapData.pois.size} POI")

            } catch (e: Exception) {
                val errorMsg = when {
                    e is retrofit2.HttpException -> {
                        when (e.code()) {
                            503 -> "Сервер временно недоступен (503). Проверьте бэкенд или ngrok туннель."
                            404 -> "Локация не найдена (404)"
                            403 -> "Доступ запрещён (403)"
                            500 -> "Внутренняя ошибка сервера (500)"
                            else -> "Ошибка сервера: HTTP ${e.code()}"
                        }
                    }
                    e.message?.contains("timeout", ignoreCase = true) == true -> "Таймаут соединения"
                    e.message?.contains("Unable to resolve host", ignoreCase = true) == true -> "Нет соединения с интернетом"
                    else -> e.message ?: "Неизвестная ошибка"
                }
                
                Log.e(TAG, "❌ Ошибка загрузки карты для локации: $errorMsg", e)
                _error.value = errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Обновляет позицию пользователя
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

        // 🔥 ЗАЩИТА ОТ РЕЗКИХ ПРЫЖКОВ В РЕЖИМЕ ПОИСКА
        if (searchManager.searching.value) {
            val currentLocation = _userLocation.value
            if (currentLocation != null) {
                val distance = LocationUtils.calculateDistance(currentLocation, location)
                if (distance > 1000) {
                    Log.w(TAG, "⚠️ ИГНОРИРУЕМ плохой GPS в режиме поиска: прыжок на ${distance.toInt()}м!")
                    return false
                }
            }
        }

        _userLocation.value = location

        // Проверяем, далеко ли ушли от места последних попыток
        lastRetryLocation?.let { lastLoc ->
            val distance = LocationUtils.calculateDistance(location, lastLoc)
            if (distance > RETRY_RESET_DISTANCE) {
                Log.d(TAG, "🔄 Пользователь ушел на ${distance.toInt()}м - сбрасываем retry счетчик")
                loadRetryCount = 0
                lastRetryTime = 0
            }
        }

        val currentTime = System.currentTimeMillis()

        // Retry логика для загрузки карты
        if (!mapDataLoaded && (currentTime - lastRetryTime) > MIN_RETRY_INTERVAL_MS) {
            loadRetryCount++
            lastRetryLocation = location
            lastRetryTime = currentTime

            Log.d(TAG, "🔄 Попытка загрузки карты #$loadRetryCount (БЕЗ ЛИМИТОВ, до победного!)")
            loadMapData(location, 2000)
        } else if (!mapDataLoaded && (currentTime - lastRetryTime) <= MIN_RETRY_INTERVAL_MS) {
            val remainingMs = MIN_RETRY_INTERVAL_MS - (currentTime - lastRetryTime)
            Log.d(TAG, "⏳ Следующая попытка через ${remainingMs/1000} сек (retry #${loadRetryCount + 1})")
        }

        // Если идёт поиск - обновляем путь
        if (searchManager.searching.value) {
            searchManager.updateSearchPath(location)
        }

        return true
    }

    /**
     * Устанавливает выбранный POI
     */
    fun setSelectedPOI(poi: POI?) {
        _selectedPOI.value = poi
        
        if (poi != null) {
            Log.d(TAG, "🎯 Выбран POI:")
            Log.d(TAG, "   id         = ${poi.id}")
            Log.d(TAG, "   name       = ${poi.name}")
            Log.d(TAG, "   type       = ${poi.type}")
            Log.d(TAG, "   isVisited  = ${poi.isVisited}")
            Log.d(TAG, "   impression = ${poi.impression}")
            Log.d(TAG, "   visitDate  = ${poi.visitDate}")
        }
    }

    /**
     * Запускает поиск
     */
    fun startSearch(currentPOI: POI, allPOIs: List<POI>, radiusM: Int = 400, limit: Int = 6) {
        searchManager.startSearch(currentPOI, allPOIs, _userLocation.value, radiusM, limit)
    }

    /**
     * Останавливает поиск и сохраняет walk session
     */
    fun stopSearch() {
        val startTime = searchManager.stopSearch()

        // Сохраняем walk session
        if (startTime != null) {
            Log.d(TAG, "💾 Сохраняем walk session с startTime=$startTime...")
            viewModelScope.launch {
                val result = walkSessionManager.saveWalkSession(
                    startTime = startTime,
                    walkedMeters = walkedMeters.value,
                    path = path.value,
                    visits = visitManager.currentSessionVisits
                )
                
                if (result != null) {
                    visitManager.setCurrentSessionId(result.sessionId)
                    
                    // Если есть разблокированные достижения - показываем диалог
                    if (result.unlockedAchievements.isNotEmpty()) {
                        Log.d(TAG, "🏆 Показываем достижения: ${result.unlockedAchievements.size}")
                        _unlockedAchievements.value = result.unlockedAchievements
                    }
                }
                
                visitManager.clearSessionVisits()
            }
        } else {
            Log.w(TAG, "⚠️ Walk session НЕ сохранена (startTime == null)")
        }
    }
    
    /**
     * Очищает список разблокированных достижений (закрывает диалог)
     */
    fun clearUnlockedAchievements() {
        _unlockedAchievements.value = emptyList()
    }

    /**
     * Обновляет elapsed секунды
     */
    fun updateElapsedTime(seconds: Long) {
        searchManager.updateElapsedTime(seconds)
    }

    /**
     * Отмечает POI как посещенное с эмоцией
     */
    fun markPOIAsVisited(poi: POI, emotion: VisitEmotion?) {
        viewModelScope.launch {
            visitManager.markPOIAsVisited(
                poi = poi,
                emotion = emotion,
                walkedMeters = walkedMeters.value,
                isSearching = searching.value
            ) { updatedPOI ->
                // Обновляем список POI в StateFlow
                val updatedPOIs = _pois.value.map { 
                    if (it.id == poi.id) updatedPOI else it 
                }
                _pois.value = updatedPOIs
                Log.d(TAG, "🔄 POI обновлен в списке: ${poi.name} (isVisited=${poi.isVisited})")
            }
        }
    }

    /**
     * Отмечает POI как "найденное" (без эмоций/журнала).
     *
     * В текущей модели это совпадает с флагом isVisited, но impression/visitDate не трогаем.
     */
    fun markPOIAsFound(poi: POI) {
        if (poi.isVisited) return

        poi.isVisited = true
        repository.markPlaceAsFound(poi.id)

        // Форсим эмит списка для Compose
        _pois.value = _pois.value.map { if (it.id == poi.id) poi else it }

        // Если открыт этот POI — обновим ссылку тоже
        if (_selectedPOI.value?.id == poi.id) {
            _selectedPOI.value = poi
        }

        Log.d(TAG, "✅ POI отмечен как найденный: ${poi.name} (id=${poi.id})")
    }

    /**
     * Проверяет, посещен ли POI
     */
    fun isPOIVisited(poiName: String): Boolean {
        return visitManager.isPOIVisited(poiName)
    }

    /**
     * Получает эмоцию для посещенного POI
     */
    fun getVisitEmotion(poiName: String): VisitEmotion? {
        return visitManager.getVisitEmotion(poiName)
    }
}
