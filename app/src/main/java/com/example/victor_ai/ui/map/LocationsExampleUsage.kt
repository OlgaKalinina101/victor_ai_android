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
import com.example.victor_ai.data.repository.LocationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 📍 Примеры использования LocationsRepository
 * 
 * Этот файл показывает как работать с сохранёнными локациями
 */
object LocationsExampleUsage {
    
    private const val TAG = "LocationsExample"
    
    /**
     * Пример 1: Загрузка списка всех локаций пользователя
     */
    fun example1_LoadAllLocations(
        repository: LocationsRepository,
        scope: CoroutineScope
    ) {
        scope.launch {
            val locations = withContext(Dispatchers.IO) {
                repository.getLocations()
            }
            
            if (locations != null) {
                Log.d(TAG, "📍 Всего локаций: ${locations.size}")
                locations.forEach { location ->
                    Log.d(TAG, "  - ${location.name} (active=${location.is_active})")
                }
            } else {
                Log.e(TAG, "❌ Не удалось загрузить локации")
            }
        }
    }
    
    /**
     * Пример 2: Загрузка только активных локаций
     */
    fun example2_LoadActiveLocations(
        repository: LocationsRepository,
        scope: CoroutineScope
    ) {
        scope.launch {
            val activeLocations = withContext(Dispatchers.IO) {
                repository.getActiveLocations()
            }
            
            activeLocations?.forEach { location ->
                Log.d(TAG, "✅ Активная локация: ${location.name}")
            }
        }
    }
    
    /**
     * Пример 3: Загрузка деталей конкретной локации
     */
    fun example3_LoadLocationDetail(
        repository: LocationsRepository,
        scope: CoroutineScope,
        locationId: Int = 1
    ) {
        scope.launch {
            val location = withContext(Dispatchers.IO) {
                repository.getLocationDetail(locationId)
            }
            
            if (location != null) {
                Log.d(TAG, "📍 Локация: ${location.name}")
                Log.d(TAG, "  BBOX: S=${location.bbox_south}, W=${location.bbox_west}")
                Log.d(TAG, "       N=${location.bbox_north}, E=${location.bbox_east}")
                Log.d(TAG, "  Описание: ${location.description ?: "нет"}")
                Log.d(TAG, "  Сложность: ${location.difficulty ?: "не указана"}")
                Log.d(TAG, "  Тип: ${location.location_type ?: "не указан"}")
            }
        }
    }
    
    /**
     * Пример 4: Загрузка OSM данных для локации
     * (вместо запроса по координатам)
     */
    fun example4_LoadPlacesForLocation(
        repository: LocationsRepository,
        scope: CoroutineScope,
        locationId: Int = 1
    ) {
        scope.launch {
            // Сначала получаем детали локации
            val location = withContext(Dispatchers.IO) {
                repository.getLocationDetail(locationId)
            }
            
            if (location != null) {
                Log.d(TAG, "📦 Загружаем места для '${location.name}'...")
                
                // Получаем OSM данные для этой локации
                val placesResponse = withContext(Dispatchers.IO) {
                    repository.getLocationPlaces(locationId)
                }
                
                if (placesResponse != null) {
                    Log.d(TAG, "✅ Загружено ${placesResponse.count} элементов")
                    Log.d(TAG, "  Лимит: ${placesResponse.limit}, оффсет: ${placesResponse.offset}")
                    
                    // Можно использовать в MapDataConverter так же как обычные places
                    // val mapData = MapDataConverter.fromBackendResponse(placesResponse, bounds, visitedIds)
                }
            }
        }
    }
    
    /**
     * Пример 5: Получение центра и радиуса локации
     */
    fun example5_GetLocationBounds(
        repository: LocationsRepository,
        scope: CoroutineScope,
        locationId: Int = 1
    ) {
        scope.launch {
            val bounds = withContext(Dispatchers.IO) {
                repository.getLocationBounds(locationId)
            }
            
            if (bounds != null) {
                val (centerLat, centerLon) = bounds.center()
                val radius = bounds.approximateRadius()
                
                Log.d(TAG, "🎯 Центр локации: $centerLat, $centerLon")
                Log.d(TAG, "📏 Примерный радиус: ${radius.toInt()} метров")
                
                // Теперь можно загрузить карту для этих координат:
                // viewModel.loadMapData(LatLng(centerLat, centerLon), radius.toInt())
            }
        }
    }
    
    /**
     * Пример 6: Проверка существования локации
     */
    fun example6_CheckLocationExists(
        repository: LocationsRepository,
        scope: CoroutineScope,
        locationId: Int = 999
    ) {
        scope.launch {
            val exists = withContext(Dispatchers.IO) {
                repository.locationExists(locationId)
            }
            
            if (exists) {
                Log.d(TAG, "✅ Локация $locationId существует")
            } else {
                Log.d(TAG, "❌ Локация $locationId не найдена")
            }
        }
    }
    
    /**
     * Пример 7: Интеграция с MapViewModel
     * Показывает как загрузить карту для сохранённой локации
     */
    fun example7_LoadMapForSavedLocation(
        locationsRepository: LocationsRepository,
        mapViewModel: MapViewModel,
        scope: CoroutineScope,
        locationId: Int = 2 // "Парк Горького"
    ) {
        scope.launch {
            // 1. Получаем bounds локации
            val bounds = withContext(Dispatchers.IO) {
                locationsRepository.getLocationBounds(locationId)
            }
            
            if (bounds != null) {
                val (centerLat, centerLon) = bounds.center()
                val radius = bounds.approximateRadius().toInt()
                
                Log.d(TAG, "🗺️ Загружаем карту для локации...")
                Log.d(TAG, "  Центр: $centerLat, $centerLon")
                Log.d(TAG, "  Радиус: $radius м")
                
                // 2. Загружаем карту через ViewModel
                withContext(Dispatchers.Main) {
                    mapViewModel.loadMapData(
                        location = com.example.victor_ai.ui.map.models.LatLng(centerLat, centerLon),
                        radiusMeters = radius
                    )
                }
                
                Log.d(TAG, "✅ Карта загружена!")
            }
        }
    }
}

/**
 * 🎯 Как использовать в вашем коде:
 * 
 * ```kotlin
 * // В Activity или Fragment с Hilt:
 * @Inject lateinit var locationsRepository: LocationsRepository
 * 
 * // Загрузить список локаций:
 * lifecycleScope.launch {
 *     val locations = locationsRepository.getLocations()
 *     // Обработать список...
 * }
 * 
 * // Загрузить карту для локации:
 * lifecycleScope.launch {
 *     val bounds = locationsRepository.getLocationBounds(locationId = 2)
 *     if (bounds != null) {
 *         val (lat, lon) = bounds.center()
 *         val radius = bounds.approximateRadius().toInt()
 *         mapViewModel.loadMapData(LatLng(lat, lon), radius)
 *     }
 * }
 * ```
 */

