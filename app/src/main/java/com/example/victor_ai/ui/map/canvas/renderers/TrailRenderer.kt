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
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import kotlin.math.atan2
import kotlin.math.hypot

/**
 * 🎨 Рендерер для отрисовки следов пользователя 👣
 */
class TrailRenderer {
    
    private val footprintPaint = Paint().apply {
        textSize = 48f
        color = Color.GRAY
        alpha = 180
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    companion object {
        private const val MIN_FOOTPRINT_DISTANCE = 70f // Минимальное расстояние между следами
    }

    /**
     * Рисует след на карте
     */
    fun draw(
        canvas: Canvas,
        trailPoints: List<LatLng>,
        converter: CoordinateConverter
    ) {
        if (trailPoints.size < 2) return

        var prevX = 0f
        var prevY = 0f
        var isFirst = true

        // Отслеживаем последнюю позицию нарисованного следа
        var lastFootprintX = Float.MIN_VALUE
        var lastFootprintY = Float.MIN_VALUE

        for (point in trailPoints) {
            val (screenX, screenY) = converter.gpsToScreen(point)

            if (isFirst) {
                isFirst = false
            } else {
                // Считаем направление от предыдущей точки
                val dx = screenX - prevX
                val dy = screenY - prevY
                val distance = hypot(dx.toDouble(), dy.toDouble()).toFloat()

                // Шаг для следов
                val steps = (distance / MIN_FOOTPRINT_DISTANCE).toInt()

                // Рисуем следы вдоль пути
                for (step in 0..steps) {
                    val ratio = step.toFloat() / steps.coerceAtLeast(1)
                    val x = prevX + dx * ratio
                    val y = prevY + dy * ratio

                    // Проверяем расстояние до последнего нарисованного следа
                    val distanceFromLast = if (lastFootprintX == Float.MIN_VALUE) {
                        Float.MAX_VALUE // Первый след - всегда рисуем
                    } else {
                        hypot(
                            (x - lastFootprintX).toDouble(),
                            (y - lastFootprintY).toDouble()
                        ).toFloat()
                    }

                    // Рисуем только если расстояние достаточное
                    if (distanceFromLast >= MIN_FOOTPRINT_DISTANCE) {
                        // Поворачиваем след по направлению движения
                        canvas.save()
                        canvas.translate(x, y)
                        val angle = atan2(dy, dx) * 180 / Math.PI.toFloat()
                        canvas.rotate(angle)

                        // Рисуем серые пяточки 👣
                        canvas.drawText("👣", 0f, 0f, footprintPaint)

                        canvas.restore()

                        // Запоминаем позицию этого следа
                        lastFootprintX = x
                        lastFootprintY = y
                    }
                }
            }

            prevX = screenX
            prevY = screenY
        }
    }
}

