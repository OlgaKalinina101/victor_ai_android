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

package com.example.victor_ai.data.repository

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

/**
 * Модель для отслеживания истории локаций
 */
data class TrackedLocation(
    val lat: Double,
    val lon: Double,
    val timestamp: Long,      // millis
    val accuracy: Float?,     // optional
    val source: String,       // "gps", "network", "manual" и т.п.
    val isManual: Boolean     // если берем «дом» или «по адресу», полезно знать, что оно не с GPS
)

/**
 * 📍 Репозиторий для хранения истории локаций и защиты от аномальных скачков
 * 
 * Функции:
 * - Хранит последние 10 геолокаций
 * - Проверяет на аномальные скачки (>20 км за 10 минут)
 * - Возвращает предыдущую локацию при обнаружении аномалии
 */
@Singleton
class LocationHistoryRepository @Inject constructor() {

    companion object {
        private const val TAG = "LocationHistory"
    }

    // Локальный репозиторий последних 10 локаций
    private val locationHistory = mutableListOf<TrackedLocation>()
    private val MAX_HISTORY_SIZE = 10

    // Защита от аномальных скачков
    private val MAX_JUMP_DISTANCE_KM = 20.0 // максимальное расстояние скачка
    private val MAX_JUMP_TIME_MS = 10 * 60 * 1000L // за 10 минут

    /**
     * Вычисляем расстояние между двумя координатами по формуле Haversine (в километрах)
     */
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371.0
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return earthRadiusKm * c
    }

    /**
     * Проверяем, нет ли аномального скачка локации
     * Возвращает последнюю валидную локацию из истории, если обнаружен скачок
     */
    private fun checkForAnomalousJump(newLat: Double, newLon: Double, timestamp: Long): TrackedLocation? {
        if (locationHistory.isEmpty()) return null

        // Берем последние локации за последние 10 минут (исключая ручные)
        val recentLocations = locationHistory.filter { 
            timestamp - it.timestamp <= MAX_JUMP_TIME_MS && !it.isManual
        }

        if (recentLocations.isEmpty()) return null

        // Проверяем расстояние до последней недавней локации
        val lastLocation = recentLocations.last()
        val distance = calculateDistance(lastLocation.lat, lastLocation.lon, newLat, newLon)
        val timeDiffMinutes = (timestamp - lastLocation.timestamp) / 60000.0

        Log.d(TAG, "Jump check: distance=${"%.2f".format(distance)}km in ${"%.1f".format(timeDiffMinutes)}min")

        // Если скачок больше 20 км за 10 минут - это аномалия
        if (distance > MAX_JUMP_DISTANCE_KM) {
            Log.w(TAG, "⚠️ Anomalous jump detected! Distance: ${"%.2f".format(distance)}km in ${"%.1f".format(timeDiffMinutes)}min. Using last known location.")
            return lastLocation
        }

        return null
    }

    /**
     * Добавляем локацию в историю (храним только последние 10)
     */
    private fun addToHistory(trackedLocation: TrackedLocation) {
        locationHistory.add(trackedLocation)
        
        // Ограничиваем размер истории
        if (locationHistory.size > MAX_HISTORY_SIZE) {
            locationHistory.removeAt(0)
        }
        
        Log.d(TAG, "History size: ${locationHistory.size}")
    }

    /**
     * Валидация и сохранение новой локации
     * 
     * @param lat широта
     * @param lon долгота
     * @param timestamp время получения локации
     * @param accuracy точность в метрах (optional)
     * @param source источник ("gps", "network" и т.п.)
     * @param isManual флаг ручной локации
     * @return TrackedLocation - валидированная локация (может быть из кеша если обнаружен скачок)
     */
    fun validateAndSave(
        lat: Double,
        lon: Double,
        timestamp: Long = System.currentTimeMillis(),
        accuracy: Float? = null,
        source: String,
        isManual: Boolean = false
    ): TrackedLocation {
        
        // Проверяем на аномальный скачок (только для автоматических локаций)
        val anomalousJump = if (!isManual) {
            checkForAnomalousJump(lat, lon, timestamp)
        } else {
            null
        }
        
        val finalLocation = if (anomalousJump != null) {
            // Используем старую локацию из кеша
            TrackedLocation(
                lat = anomalousJump.lat,
                lon = anomalousJump.lon,
                timestamp = timestamp,
                accuracy = accuracy,
                source = "${anomalousJump.source}_cached",
                isManual = false
            )
        } else {
            // Используем новую локацию
            TrackedLocation(
                lat = lat,
                lon = lon,
                timestamp = timestamp,
                accuracy = accuracy,
                source = source,
                isManual = isManual
            )
        }
        
        // Сохраняем в историю
        addToHistory(finalLocation)
        
        val jumpWarning = if (anomalousJump != null) " [ANOMALY FILTERED]" else ""
        Log.d(TAG, "✓ Location saved: ${finalLocation.lat}, ${finalLocation.lon} (source=${finalLocation.source})$jumpWarning")
        
        return finalLocation
    }

    /**
     * Получить историю локаций (последние 10)
     */
    fun getHistory(): List<TrackedLocation> = locationHistory.toList()

    /**
     * Получить последнюю локацию из истории
     */
    fun getLastLocation(): TrackedLocation? = locationHistory.lastOrNull()

    /**
     * Получить последнюю не-ручную локацию
     */
    fun getLastNonManualLocation(): TrackedLocation? = 
        locationHistory.lastOrNull { !it.isManual }

    /**
     * Очистка истории
     */
    fun clear() {
        locationHistory.clear()
        Log.d(TAG, "History cleared")
    }

    /**
     * Получить статистику по истории
     */
    fun getStatistics(): HistoryStatistics {
        if (locationHistory.isEmpty()) {
            return HistoryStatistics(
                totalCount = 0,
                manualCount = 0,
                gpsCount = 0,
                networkCount = 0,
                averageAccuracy = null,
                timeSpanMinutes = 0.0
            )
        }

        val manualCount = locationHistory.count { it.isManual }
        val gpsCount = locationHistory.count { it.source.contains("gps", ignoreCase = true) }
        val networkCount = locationHistory.count { it.source.contains("network", ignoreCase = true) }
        
        val accuracies = locationHistory.mapNotNull { it.accuracy }
        val averageAccuracy = if (accuracies.isNotEmpty()) {
            accuracies.average().toFloat()
        } else {
            null
        }

        val timeSpan = if (locationHistory.size > 1) {
            (locationHistory.last().timestamp - locationHistory.first().timestamp) / 60000.0
        } else {
            0.0
        }

        return HistoryStatistics(
            totalCount = locationHistory.size,
            manualCount = manualCount,
            gpsCount = gpsCount,
            networkCount = networkCount,
            averageAccuracy = averageAccuracy,
            timeSpanMinutes = timeSpan
        )
    }
}

/**
 * Статистика по истории локаций
 */
data class HistoryStatistics(
    val totalCount: Int,
    val manualCount: Int,
    val gpsCount: Int,
    val networkCount: Int,
    val averageAccuracy: Float?,
    val timeSpanMinutes: Double
)

