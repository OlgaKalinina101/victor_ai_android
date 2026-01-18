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

package com.example.victor_ai.ui.map.managers

import android.util.Log
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.POI
import com.example.victor_ai.ui.map.utils.LocationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 🔍 Менеджер для управления поиском POI
 * 
 * Ответственность:
 * - Управление состоянием поиска (start/stop)
 * - Трекинг пути пользователя
 * - Подсчет пройденной дистанции
 * - Вычисление nearby POI
 * - Управление временем поиска
 */
class SearchManager {
    companion object {
        private const val TAG = "SearchManager"
    }

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

    /**
     * Запускает поиск
     */
    fun startSearch(
        currentPOI: POI,
        allPOIs: List<POI>,
        userLocation: LatLng?,
        radiusM: Int = 400,
        limit: Int = 6
    ) {
        Log.d(TAG, "🚀 Начинаем поиск для POI: ${currentPOI.name}")
        _searching.value = true
        _searchStart.value = System.currentTimeMillis()
        _elapsedSec.value = 0L
        _walkedMeters.value = 0.0
        lastPoint = userLocation

        _path.value = if (userLocation != null) listOf(userLocation) else emptyList()

        // Вычисляем nearby POI
        _nearby.value = calcNearby(currentPOI, allPOIs, radiusM, limit)
        Log.d(TAG, "✅ Поиск запущен. Nearby POI: ${_nearby.value.size}")
    }

    /**
     * Останавливает поиск
     * @return startTime для сохранения walk session
     */
    fun stopSearch(): Long? {
        Log.d(TAG, "🛑 stopSearch() вызван")
        Log.d(TAG, "   - searching: ${_searching.value}")
        Log.d(TAG, "   - walkedMeters: ${_walkedMeters.value}")
        Log.d(TAG, "   - path.size: ${_path.value.size}")

        val startTime = _searchStart.value

        _searching.value = false
        _searchStart.value = null
        lastPoint = null
        
        Log.d(TAG, "✅ stopSearch() завершен, startTime=$startTime")
        return startTime
    }

    /**
     * Обновляет elapsed секунды
     */
    fun updateElapsedTime(seconds: Long) {
        _elapsedSec.value = seconds
    }

    /**
     * Обновляет путь во время поиска
     */
    fun updateSearchPath(newLocation: LatLng) {
        if (!_searching.value) return

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
     * Вычисляет ближайшие POI к выбранному
     */
    private fun calcNearby(
        centerPoi: POI,
        all: List<POI>,
        radiusM: Int,
        limit: Int
    ): List<POI> {
        return all.asSequence()
            .filter { it.id != centerPoi.id }
            .filter { LocationUtils.calculateDistance(centerPoi.location, it.location) <= radiusM }
            .sortedBy { LocationUtils.calculateDistance(centerPoi.location, it.location) }
            .take(limit)
            .toList()
    }
}

