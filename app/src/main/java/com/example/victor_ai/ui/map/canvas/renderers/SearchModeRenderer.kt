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

package com.example.victor_ai.ui.map.canvas.renderers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.Log
import androidx.core.graphics.toColorInt
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.POI
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import kotlin.math.sin

/**
 * 🎨 Рендерер для отрисовки элементов режима поиска:
 * - Пунктирная линия от пользователя до цели
 * - Пульсирующая анимация на целевой точке
 */
class SearchModeRenderer {
    
    companion object {
        private const val TAG = "SearchModeRenderer"
    }

    // Paint для пунктирной линии до цели
    private val dashedLinePaint = Paint().apply {
        color = "#4A4A4A".toColorInt()
        style = Paint.Style.STROKE
        strokeWidth = 16f
        alpha = 220
        isAntiAlias = true
        pathEffect = DashPathEffect(floatArrayOf(30f, 20f), 0f)
        strokeCap = Paint.Cap.ROUND
    }

    // Paint для пульсирующего круга на цели
    private val pulseCirclePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    /**
     * Рисует пунктирную линию от пользователя до целевого POI
     */
    fun drawDashedLineToTarget(
        canvas: Canvas,
        userLocation: LatLng,
        selectedPOI: POI,
        converter: CoordinateConverter
    ) {
        if (!converter.isInBounds(userLocation)) {
            Log.w(TAG, "drawDashedLineToTarget(): userLocation не в bounds! $userLocation")
            return
        }

        if (!converter.isInBounds(selectedPOI.location)) {
            Log.w(TAG, "drawDashedLineToTarget(): target.location не в bounds! ${selectedPOI.location}")
            return
        }

        val (userX, userY) = converter.gpsToScreen(userLocation)
        val (targetX, targetY) = converter.gpsToScreen(selectedPOI.location)

        canvas.drawLine(userX, userY, targetX, targetY, dashedLinePaint)
    }

    /**
     * Рисует пульсирующую анимацию на целевой точке
     * 
     * @param canvas Canvas для отрисовки
     * @param selectedPOI Целевой POI
     * @param converter Конвертер координат
     * @param animationTime Текущее время анимации
     */
    fun drawPulsingTarget(
        canvas: Canvas,
        selectedPOI: POI,
        converter: CoordinateConverter,
        animationTime: Long
    ) {
        if (!converter.isInBounds(selectedPOI.location)) return

        val (x, y) = converter.gpsToScreen(selectedPOI.location)

        // Вычисляем радиус пульсации (от 50 до 80 пикселей)
        val time = animationTime % 1500 // Период 1.5 секунды
        val progress = time / 1500f
        val radius = 50f + 30f * sin(progress * Math.PI * 2).toFloat()
        val alpha = (255 * (1 - progress)).toInt().coerceIn(0, 255)

        pulseCirclePaint.alpha = alpha
        canvas.drawCircle(x, y, radius, pulseCirclePaint)
    }
}

