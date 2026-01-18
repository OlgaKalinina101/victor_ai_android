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

/**
 * 🎨 Рендерер для отрисовки сетки на карте
 */
class GridRenderer(
    private val cellSize: Float = 200f
) {
    private val gridPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 1f
        isAntiAlias = true
    }

    /**
     * Рисует сетку на canvas
     */
    fun draw(canvas: Canvas, width: Float, height: Float) {
        // Вертикальные линии
        var x = 0f
        while (x <= width) {
            canvas.drawLine(x, 0f, x, height, gridPaint)
            x += cellSize
        }

        // Горизонтальные линии
        var y = 0f
        while (y <= height) {
            canvas.drawLine(0f, y, width, y, gridPaint)
            y += cellSize
        }
    }
}

