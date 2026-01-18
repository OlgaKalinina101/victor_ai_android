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
import android.graphics.Paint
import android.graphics.Path
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.POI
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import com.example.victor_ai.ui.map.utils.LocationUtils

/**
 * 🎨 Рендерер для отрисовки маркера пользователя в виде стрелки
 */
class UserMarkerRenderer {
    
    private val arrowPaint = Paint().apply {
        color = Color.parseColor("#4A4A4A")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val arrowStrokePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    companion object {
        private const val ARROW_SIZE = 32f
    }

    /**
     * Рисует маркер текущей позиции пользователя в виде стрелки
     * 
     * @param canvas Canvas для отрисовки
     * @param userLocation Текущая позиция пользователя
     * @param selectedPOI Выбранный POI (для направления стрелки)
     * @param converter Конвертер координат
     */
    fun draw(
        canvas: Canvas,
        userLocation: LatLng,
        selectedPOI: POI?,
        converter: CoordinateConverter
    ) {
        if (!converter.isInBounds(userLocation)) return

        val (x, y) = converter.gpsToScreen(userLocation)
        val bearing = selectedPOI?.let { poi ->
            LocationUtils.calculateBearing(userLocation, poi.location)
        } ?: 0f

        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(bearing)

        // Немного закругленные углы
        val arrowPath = Path().apply {
            moveTo(0f, -ARROW_SIZE)                          // верх
            lineTo(ARROW_SIZE * 0.7f, ARROW_SIZE * 0.4f)     // нижний правый угол
            lineTo(ARROW_SIZE * 0.4f, ARROW_SIZE * 0.4f)     // вырез под основание
            lineTo(ARROW_SIZE * 0.4f, ARROW_SIZE)            // край основания
            lineTo(-ARROW_SIZE * 0.4f, ARROW_SIZE)           // противоположный край основания
            lineTo(-ARROW_SIZE * 0.4f, ARROW_SIZE * 0.4f)    // вырез под основание
            lineTo(-ARROW_SIZE * 0.7f, ARROW_SIZE * 0.4f)    // нижний левый угол
            close()
        }

        canvas.drawPath(arrowPath, arrowStrokePaint)
        canvas.drawPath(arrowPath, arrowPaint)

        canvas.restore()
    }
}

