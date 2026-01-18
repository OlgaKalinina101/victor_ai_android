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

package com.example.victor_ai.ui.map.canvas.controllers

import android.util.Log
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.MapBounds
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import kotlin.math.abs
import kotlin.math.max

/**
 * 🗺️ Контроллер для управления картой (зум, панорамирование, bounds)
 */
class MapController(
    private val viewWidth: () -> Int,
    private val viewHeight: () -> Int,
    private val onStateChanged: () -> Unit
) {
    companion object {
        private const val TAG = "MapController"
        private const val MAX_ZOOM = 60f
        private const val MIN_ZOOM = 0.5f
    }

    var mapBounds: MapBounds? = null

    var currentZoom: Float = 300f

    private var initialLatRange: Double = 0.0
    private var initialLonRange: Double = 0.0

    private var isZoomInitialized: Boolean = false

    var coordinateConverter: CoordinateConverter? = null
        private set

    /**
     * Инициализирует контроллер с начальными границами карты
     */
    fun initialize(bounds: MapBounds) {
        this.mapBounds = bounds
        this.initialLatRange = bounds.maxLat - bounds.minLat
        this.initialLonRange = bounds.maxLon - bounds.minLon

        updateConverter()
    }

    /**
     * Сбрасывает зум к начальному значению
     */
    fun resetZoom() {
        Log.d(TAG, "🔄 resetZoom() - сбрасываем зум к 5f")
        isZoomInitialized = false
        onStateChanged()
    }

    /**
     * Устанавливает комфортный начальный зум (если еще не установлен)
     */
    fun applyInitialZoomIfNeeded(userLocation: LatLng?) {
        if (isZoomInitialized) {
            Log.d(TAG, "Зум уже установлен ($currentZoom) - сохраняем его")
            updateConverter()
            return
        }

        // ВАЖНО: СНАЧАЛА ЦЕНТРИРУЕМ НА ПОЛЬЗОВАТЕЛЕ
        userLocation?.let { panTo(it) }

        // Первая загрузка - устанавливаем комфортный зум
        zoomTo(5f)
        isZoomInitialized = true
        Log.d(TAG, "Первая загрузка - устанавливаем зум 5f")
    }

    /**
     * Центрирует карту на указанной локации
     */
    fun panTo(location: LatLng) {
        Log.d(TAG, "🧭 panTo() - location=$location")
        
        val currentLatRange = mapBounds?.let { it.maxLat - it.minLat } ?: return
        val currentLonRange = mapBounds?.let { it.maxLon - it.minLon } ?: return

        mapBounds = MapBounds(
            minLat = location.lat - currentLatRange / 2,
            maxLat = location.lat + currentLatRange / 2,
            minLon = location.lon - currentLonRange / 2,
            maxLon = location.lon + currentLonRange / 2
        )

        updateConverter()
        onStateChanged()
    }

    /**
     * Изменяет зум карты
     */
    fun zoomTo(zoom: Float) {
        Log.d(TAG, "🔍 zoomTo() - zoom=$zoom, currentZoom=$currentZoom")
        currentZoom = zoom.coerceIn(MIN_ZOOM, MAX_ZOOM)

        val center = getCurrentCenter()
        val newLatRange = initialLatRange / currentZoom
        val newLonRange = initialLonRange / currentZoom

        mapBounds = MapBounds(
            minLat = center.lat - newLatRange / 2,
            maxLat = center.lat + newLatRange / 2,
            minLon = center.lon - newLonRange / 2,
            maxLon = center.lon + newLonRange / 2
        )

        updateConverter()
        onStateChanged()
    }

    /**
     * Зумирует и центрирует карту так, чтобы обе точки были видны
     */
    fun zoomToIncludeBoth(loc1: LatLng, loc2: LatLng, paddingFactor: Float = 0.3f) {
        Log.d(TAG, "🎯 zoomToIncludeBoth() - loc1=$loc1, loc2=$loc2")

        // Вычисляем центр между двумя точками
        val centerLat = (loc1.lat + loc2.lat) / 2
        val centerLon = (loc1.lon + loc2.lon) / 2

        // Вычисляем необходимый диапазон (с отступами)
        val latDiff = abs(loc1.lat - loc2.lat)
        val lonDiff = abs(loc1.lon - loc2.lon)

        // Минимальное расстояние чтобы избежать слишком большого зума
        val minDistance = 0.002 // ~200 метров
        val effectiveLatDiff = max(latDiff, minDistance)
        val effectiveLonDiff = max(lonDiff, minDistance)

        val requiredLatRange = effectiveLatDiff * (1 + paddingFactor)
        val requiredLonRange = effectiveLonDiff * (1 + paddingFactor)

        // Вычисляем зум под этот диапазон
        val zoomForLat = (initialLatRange / requiredLatRange).toFloat()
        val zoomForLon = (initialLonRange / requiredLonRange).toFloat()
        val optimalZoom = minOf(zoomForLat, zoomForLon).coerceIn(1f, 15f)

        Log.d(TAG, "📐 Вычисленный зум: $optimalZoom")

        // Применяем зум
        currentZoom = optimalZoom
        val newLatRange = initialLatRange / currentZoom
        val newLonRange = initialLonRange / currentZoom

        mapBounds = MapBounds(
            minLat = centerLat - newLatRange / 2,
            maxLat = centerLat + newLatRange / 2,
            minLon = centerLon - newLonRange / 2,
            maxLon = centerLon + newLonRange / 2
        )

        updateConverter()
        onStateChanged()
    }

    /**
     * Применяет скролл к карте
     */
    fun applyScroll(distanceX: Float, distanceY: Float) {
        val bounds = mapBounds ?: return
        val width = viewWidth()
        val height = viewHeight()

        if (width <= 0 || height <= 0) return

        val deltaLat = (distanceY / height) * (bounds.maxLat - bounds.minLat)
        val deltaLon = (distanceX / width) * (bounds.maxLon - bounds.minLon)

        mapBounds = MapBounds(
            minLat = bounds.minLat + deltaLat,
            maxLat = bounds.maxLat + deltaLat,
            minLon = bounds.minLon - deltaLon,
            maxLon = bounds.maxLon - deltaLon
        )

        updateConverter()
        onStateChanged()
    }

    /**
     * Применяет масштабирование
     */
    fun applyScale(scaleFactor: Float) {
        val newZoom = currentZoom * scaleFactor
        zoomTo(newZoom.coerceIn(0.5f, 10f))
    }

    /**
     * Получает текущий центр карты
     */
    fun getCurrentCenter(): LatLng {
        val b = mapBounds ?: return LatLng(0.0, 0.0)
        return LatLng(
            (b.minLat + b.maxLat) / 2,
            (b.minLon + b.maxLon) / 2
        )
    }

    /**
     * Обновляет конвертер координат
     */
    private fun updateConverter() {
        val width = viewWidth()
        val height = viewHeight()
        val bounds = mapBounds

        if (width > 0 && height > 0 && bounds != null) {
            coordinateConverter = CoordinateConverter(
                bounds,
                width.toFloat(),
                height.toFloat()
            )
            Log.d(TAG, "✅ Converter обновлен")
        }
    }

    /**
     * Обновляет конвертер при изменении размера view
     */
    fun onSizeChanged() {
        updateConverter()
    }
}

