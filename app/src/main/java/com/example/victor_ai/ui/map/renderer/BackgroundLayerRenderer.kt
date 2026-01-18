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

package com.example.victor_ai.ui.map.renderer

import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.example.victor_ai.ui.map.models.BackgroundElement
import com.example.victor_ai.ui.map.models.BackgroundGeometry
import com.example.victor_ai.ui.map.models.BackgroundLayer
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import kotlin.random.Random

/**
 * 🎨 Рендерер фоновых слоев карты (игровой стиль)
 * 
 * Отрисовывает стилизованные фоновые элементы:
 * - Воду (озера, реки) - с пятнистой текстурой
 * - Зелень (парки, леса) - органичная заливка
 * - Дороги - мягкие линии
 * - Здания - десатурированные блоки
 * 
 * С эффектом размытия и органичной текстурой для игрового вида
 */
class BackgroundLayerRenderer {
    
    // Основная заливка (десатурированная, полупрозрачная)
    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        alpha = 150 // Еще более прозрачная для мягкости
        isDither = true // Включаем дизеринг для плавных переходов
    }
    
    // Пятнистая текстура (добавляет органичность)
    private val texturePaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        alpha = 40 // Очень слабая, только намек на текстуру
        isDither = true
    }
    
    // Мягкая обводка (почти невидимая)
    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        isAntiAlias = true
        alpha = 60 // Очень бледная обводка
        // Легкое размытие для мягкости краев
        maskFilter = BlurMaskFilter(2f, BlurMaskFilter.Blur.NORMAL)
    }
    
    // Для дорог (мягкие линии)
    private val roadPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f // Чуть тоньше
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        alpha = 100 // Очень бледные дороги
        isDither = true
        // Мягкое размытие
        maskFilter = BlurMaskFilter(1.5f, BlurMaskFilter.Blur.NORMAL)
    }
    
    // Генератор случайных чисел для текстуры (с фиксированным seed для стабильности)
    private val textureRandom = Random(42)
    
    /**
     * Рисует все фоновые слои в правильном порядке
     */
    fun drawBackgroundLayers(
        canvas: Canvas,
        elements: List<BackgroundElement>,
        converter: CoordinateConverter
    ) {
        if (elements.isEmpty()) return
        
        // Группируем по слоям и сортируем по z-index
        val sortedByLayer = elements
            .groupBy { it.layer }
            .toSortedMap(BackgroundLayer.comparator())
        
        // Рисуем слой за слоем (снизу вверх)
        sortedByLayer.forEach { (layer, layerElements) ->
            layerElements.forEach { element ->
                drawElement(canvas, element, converter)
            }
        }
    }
    
    /**
     * Рисует один элемент
     */
    private fun drawElement(
        canvas: Canvas,
        element: BackgroundElement,
        converter: CoordinateConverter
    ) {
        when (val geom = element.geometry) {
            is BackgroundGeometry.LineString -> drawLineString(canvas, geom, element, converter)
            is BackgroundGeometry.Polygon -> drawPolygon(canvas, geom, element, converter)
        }
    }
    
    /**
     * Рисует линию (дороги, реки-линии)
     */
    private fun drawLineString(
        canvas: Canvas,
        geometry: BackgroundGeometry.LineString,
        element: BackgroundElement,
        converter: CoordinateConverter
    ) {
        if (geometry.points.size < 2) return
        
        roadPaint.color = element.color
        
        val path = Path()
        geometry.points.forEachIndexed { index, latLng ->
            val (x, y) = converter.gpsToScreen(latLng)
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        
        canvas.drawPath(path, roadPaint)
    }
    
    /**
     * Рисует полигон (парки, озера, здания) с пятнистой текстурой
     */
    private fun drawPolygon(
        canvas: Canvas,
        geometry: BackgroundGeometry.Polygon,
        element: BackgroundElement,
        converter: CoordinateConverter
    ) {
        geometry.rings.forEach { ring ->
            if (ring.size < 3) return@forEach // Минимум 3 точки для полигона
            
            val path = Path()
            ring.forEachIndexed { index, latLng ->
                val (x, y) = converter.gpsToScreen(latLng)
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()
            
            // 1. Основная заливка (десатурированная)
            fillPaint.color = element.color
            canvas.drawPath(path, fillPaint)
            
            // 2. Добавляем пятнистую текстуру (органичность)
            addSpottyTexture(canvas, path, element)
            
            // 3. Очень тонкая обводка (едва заметная)
            strokePaint.color = darkenColor(element.color, 0.08f)
            canvas.drawPath(path, strokePaint)
        }
    }
    
    /**
     * Добавляет пятнистую текстуру к полигону (игровой эффект)
     */
    private fun addSpottyTexture(
        canvas: Canvas,
        path: Path,
        element: BackgroundElement
    ) {
        // Создаем псевдослучайные пятна на основе ID элемента
        // (чтобы текстура была стабильной при перерисовке)
        val elementSeed = element.id.hashCode()
        val spotCount = when (element.layer) {
            BackgroundLayer.WATER -> 5 // Меньше пятен для воды
            BackgroundLayer.GREENERY -> 8 // Больше для зелени
            BackgroundLayer.BUILDINGS -> 3 // Минимум для зданий
            else -> 5
        }
        
        // Сохраняем состояние canvas
        canvas.save()
        // Ограничиваем рисование внутри полигона
        canvas.clipPath(path)
        
        // Рисуем несколько полупрозрачных пятен
        repeat(spotCount) { i ->
            val spotRandom = Random(elementSeed + i)
            
            // Случайная позиция внутри полигона (упрощенно)
            val pathBounds = android.graphics.RectF()
            path.computeBounds(pathBounds, true)
            
            if (pathBounds.width() > 0 && pathBounds.height() > 0) {
                val spotX = pathBounds.left + spotRandom.nextFloat() * pathBounds.width()
                val spotY = pathBounds.top + spotRandom.nextFloat() * pathBounds.height()
                val spotRadius = spotRandom.nextFloat() * 20f + 10f // 10-30px
                
                // Цвет пятна - чуть темнее или светлее основного
                val spotColor = if (spotRandom.nextBoolean()) {
                    darkenColor(element.color, 0.05f)
                } else {
                    lightenColor(element.color, 0.05f)
                }
                
                texturePaint.color = spotColor
                canvas.drawCircle(spotX, spotY, spotRadius, texturePaint)
            }
        }
        
        canvas.restore()
    }
    
    /**
     * Делает цвет темнее для обводки
     * @param color Исходный ARGB цвет
     * @param factor Коэффициент затемнения (0.0-1.0)
     */
    private fun darkenColor(color: Int, factor: Float): Int {
        val a = (color shr 24) and 0xFF
        val r = ((color shr 16) and 0xFF)
        val g = ((color shr 8) and 0xFF)
        val b = (color and 0xFF)
        
        val newR = (r * (1 - factor)).toInt().coerceIn(0, 255)
        val newG = (g * (1 - factor)).toInt().coerceIn(0, 255)
        val newB = (b * (1 - factor)).toInt().coerceIn(0, 255)
        
        return (a shl 24) or (newR shl 16) or (newG shl 8) or newB
    }
    
    /**
     * Делает цвет светлее для текстуры
     * @param color Исходный ARGB цвет
     * @param factor Коэффициент осветления (0.0-1.0)
     */
    private fun lightenColor(color: Int, factor: Float): Int {
        val a = (color shr 24) and 0xFF
        val r = ((color shr 16) and 0xFF)
        val g = ((color shr 8) and 0xFF)
        val b = (color and 0xFF)
        
        val newR = (r + (255 - r) * factor).toInt().coerceIn(0, 255)
        val newG = (g + (255 - g) * factor).toInt().coerceIn(0, 255)
        val newB = (b + (255 - b) * factor).toInt().coerceIn(0, 255)
        
        return (a shl 24) or (newR shl 16) or (newG shl 8) or newB
    }
}

