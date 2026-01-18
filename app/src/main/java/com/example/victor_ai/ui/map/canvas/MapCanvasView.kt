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

package com.example.victor_ai.ui.map.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.victor_ai.ui.map.canvas.controllers.MapController
import com.example.victor_ai.ui.map.canvas.controllers.SearchModeController
import com.example.victor_ai.ui.map.canvas.gestures.MapGestureHandler
import com.example.victor_ai.ui.map.canvas.renderers.GridRenderer
import com.example.victor_ai.ui.map.canvas.renderers.SearchModeRenderer
import com.example.victor_ai.ui.map.canvas.renderers.TrailRenderer
import com.example.victor_ai.ui.map.canvas.renderers.UserMarkerRenderer
import com.example.victor_ai.ui.map.renderer.POIMarkerRenderer
import com.example.victor_ai.ui.map.renderer.BackgroundLayerRenderer
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.MapBounds
import com.example.victor_ai.ui.map.models.POI
import com.example.victor_ai.ui.map.models.POIType
import com.example.victor_ai.ui.map.models.BackgroundElement

/**
 * 🗺️ Custom View для отображения карты с POI маркерами
 * 
 * Выступает как координатор между:
 * - Рендерерами (отрисовка)
 * - Контроллерами (логика управления)
 * - Обработчиками жестов (взаимодействие)
 */
class MapCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "MapCanvasView"
        private const val BACKGROUND_COLOR = 0xFFF8F8F6.toInt() // Теплый светло-бежевый
    }

    // ============ Speech bubble (комикс-облачко) ============
    private var speechBubbleText: String? = null
    private var speechBubbleLines: List<String> = emptyList()
    private var speechBubbleMaxTextWidthPx: Float = 0f

    private val bubbleFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        style = Paint.Style.FILL
        // Легкая тень "мультяшности" (работает в software-рендеринге)
        setShadowLayer(10f, 0f, 4f, 0x33000000)
    }

    private val bubbleStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // Серый контур, мягче чем чисто чёрный
        color = 0xFFB0B0B0.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val bubbleTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF1E1E1E.toInt()
        // displayMetrics.scaledDensity deprecated на новых SDK; эквивалент: density * fontScale
        val density = resources.displayMetrics.density
        val fontScale = resources.configuration.fontScale
        textSize = density * fontScale * 14f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    }

    /**
     * Устанавливает текст "облачка". null/blank => скрыть.
     * Позиция облачка привязана к текущему `selectedPOI`.
     */
    fun setSpeechBubbleText(text: String?) {
        speechBubbleText = text?.takeIf { it.isNotBlank() }
        speechBubbleLines = emptyList()
        invalidate()
    }

    // ============ Контроллеры ============
    private val mapController = MapController(
        viewWidth = { width },
        viewHeight = { height },
        onStateChanged = { invalidate() }
    )

    private val searchModeController = SearchModeController(
        onAnimationFrame = { invalidate() }
    ).apply {
        postCallback = { runnable -> post(runnable) }
        removeCallback = { runnable -> removeCallbacks(runnable) }
    }

    // ============ Рендереры ============
    private val gridRenderer = GridRenderer()
    private val backgroundRenderer = BackgroundLayerRenderer()
    private val trailRenderer = TrailRenderer()
    private val userMarkerRenderer = UserMarkerRenderer()
    private val searchModeRenderer = SearchModeRenderer()
    private val poiMarkerRenderer = POIMarkerRenderer(context)

    // ============ Обработчик жестов ============
    private val gestureHandler = MapGestureHandler(
        context = context,
        mapController = mapController,
        markerRenderer = poiMarkerRenderer
    )

    // ============ Данные ============
    private var pois: List<POI> = emptyList()
    private var userLocation: LatLng? = null
    private var selectedPOI: POI? = null
    private var backgroundElements: List<BackgroundElement> = emptyList()
    private val trailPoints: MutableList<LatLng> = mutableListOf()

    // ============ Публичное API ============

    /**
     * Callback для кликов на POI
     */
    var onPOIClicked: ((POI) -> Unit)? = null
        set(value) {
            field = value
            gestureHandler.onPOIClicked = value
        }

    /**
     * Устанавливает данные карты
     */
    fun setMapData(
        bounds: MapBounds,
        pois: List<POI>,
        userLocation: LatLng? = null,
        backgroundElements: List<BackgroundElement> = emptyList()
    ) {
        Log.d(TAG, "📍 setMapData() - pois.size=${pois.size}, backgroundElements.size=${backgroundElements.size}")
        
        this.pois = pois.filter { isAllowedPOIType(it.type) }
        this.userLocation = userLocation
        this.backgroundElements = backgroundElements
        
        gestureHandler.pois = this.pois

        if (width > 0 && height > 0) {
            mapController.initialize(bounds)

            // В режиме поиска не трогаем зум/панорамирование
            if (!searchModeController.isSearching) {
                mapController.applyInitialZoomIfNeeded(userLocation)
            }
        }

        invalidate()
    }

    /**
     * Обновляет позицию пользователя
     */
    fun updateUserLocation(location: LatLng) {
        this.userLocation = location
        invalidate()
    }

    /**
     * Обновляет список POI
     */
    fun updatePOIs(newPOIs: List<POI>) {
        Log.d(TAG, "🔄 updatePOIs() - newPOIs.size=${newPOIs.size}")
        this.pois = newPOIs.filter { isAllowedPOIType(it.type) }
        gestureHandler.pois = this.pois
        invalidate()
    }

    /**
     * Устанавливает след пользователя
     */
    fun setTrail(points: List<LatLng>) {
        trailPoints.clear()
        trailPoints.addAll(points)
        invalidate()
    }

    /**
     * Устанавливает выбранный POI для направления стрелки
     */
    fun setSelectedPOI(poi: POI?) {
        Log.d(TAG, "🎯 setSelectedPOI() - poi=${poi?.name}")
        this.selectedPOI = poi
        if (poi == null) {
            // Если снимаем выбор — скрываем облачко
            speechBubbleText = null
            speechBubbleLines = emptyList()
        }
        invalidate()
    }

    /**
     * Включает режим поиска с анимацией
     */
    fun startSearchMode() {
        Log.d(TAG, "🚀 startSearchMode()")
        searchModeController.startSearchMode(
            mapController.currentZoom,
            mapController.mapBounds
        )
    }

    /**
     * Выключает режим поиска
     */
    fun stopSearchMode() {
        Log.d(TAG, "🛑 stopSearchMode()")
        val (savedZoom, savedBounds) = searchModeController.stopSearchMode()

        // Восстанавливаем сохраненное состояние карты
        if (savedZoom != null && savedBounds != null) {
            mapController.mapBounds = savedBounds
            mapController.currentZoom = savedZoom
            mapController.onSizeChanged()
        }

        invalidate()
    }

    /**
     * Сбрасывает зум к комфортному значению
     */
    fun resetZoom() {
        Log.d(TAG, "🔄 resetZoom()")
        mapController.resetZoom()
    }

    /**
     * Центрирует карту на указанной локации
     */
    fun panTo(location: LatLng) {
        mapController.panTo(location)
    }

    /**
     * Изменяет зум карты
     */
    fun zoomTo(zoom: Float) {
        mapController.zoomTo(zoom)
    }

    /**
     * Зумирует карту так, чтобы обе точки были видны
     */
    fun zoomToIncludeBoth(loc1: LatLng, loc2: LatLng, paddingFactor: Float = 0.3f) {
        mapController.zoomToIncludeBoth(loc1, loc2, paddingFactor)
    }

    // ============ View lifecycle ============

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mapController.onSizeChanged()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1. Рисуем мягкий бежевый фон
        canvas.drawColor(BACKGROUND_COLOR)

        val converter = mapController.coordinateConverter ?: return

        // 2. Рисуем фоновые слои (вода, зелень, дороги, здания)
        if (backgroundElements.isNotEmpty()) {
            backgroundRenderer.drawBackgroundLayers(canvas, backgroundElements, converter)
        }

        // 3. Рисуем серую сетку
        gridRenderer.draw(canvas, width.toFloat(), height.toFloat())

        // 4. Трек пользователя
        if (trailPoints.isNotEmpty()) {
            trailRenderer.draw(canvas, trailPoints, converter)
        }

        // 5. Пунктирная линия до цели (если режим поиска)
        if (searchModeController.isSearching) {
            val user = userLocation
            val target = selectedPOI
            if (user != null && target != null) {
                searchModeRenderer.drawDashedLineToTarget(canvas, user, target, converter)
            }
        }

        // 6. Рисуем POI маркеры
        if (pois.isNotEmpty()) {
            // В режиме поиска показываем только выбранный POI
            val poisToShow = if (searchModeController.isSearching && selectedPOI != null) {
                listOf(selectedPOI!!)
            } else {
                pois
            }
            poiMarkerRenderer.drawMarkers(canvas, poisToShow, converter)
        }

        // 6.5. Комикс-облачко рядом с выбранным POI (только вне режима поиска)
        if (!searchModeController.isSearching) {
            drawSpeechBubbleIfNeeded(canvas, converter)
        }

        // 7. Пульсирующая анимация на цели (если режим поиска)
        if (searchModeController.isSearching) {
            selectedPOI?.let { target ->
                searchModeRenderer.drawPulsingTarget(
                    canvas,
                    target,
                    converter,
                    searchModeController.animationTime
                )
            }
        }

        // 8. Рисуем маркер пользователя
        userLocation?.let { location ->
            userMarkerRenderer.draw(canvas, location, selectedPOI, converter)
        }
    }

    private fun drawSpeechBubbleIfNeeded(canvas: Canvas, converter: com.example.victor_ai.ui.map.utils.CoordinateConverter) {
        val poi = selectedPOI ?: return
        val text = speechBubbleText ?: return
        if (pois.isEmpty()) return
        if (!converter.isInBounds(poi.location)) return

        // Позиция маркера должна совпадать с drawMarkers()/кликами (учет смещений)
        val (markerX, markerY) = poiMarkerRenderer.getMarkerScreenPosition(poi, pois, converter)
        val markerRadius = poiMarkerRenderer.markerRadiusPx()

        // Лэйаут текста (кешируем, пока строка не меняется)
        val density = resources.displayMetrics.density
        val margin = 8f * density
        val paddingX = 12f * density
        val paddingY = 10f * density
        val radius = 14f * density
        val tailSize = 10f * density
        val lineGap = 4f * density

        val maxWidth = (width * 0.68f).coerceAtLeast(220f * density)
        if (speechBubbleLines.isEmpty() || speechBubbleMaxTextWidthPx != maxWidth) {
            speechBubbleMaxTextWidthPx = maxWidth
            speechBubbleLines = wrapTextToLines(text, bubbleTextPaint, maxWidth)
        }

        val lineHeight = (bubbleTextPaint.fontMetrics.descent - bubbleTextPaint.fontMetrics.ascent)
        val textWidth = speechBubbleLines.maxOfOrNull { bubbleTextPaint.measureText(it) } ?: 0f
        val bubbleW = (textWidth + paddingX * 2).coerceAtMost(maxWidth + paddingX * 2)
        val bubbleH = speechBubbleLines.size * lineHeight + (speechBubbleLines.size - 1).coerceAtLeast(0) * lineGap + paddingY * 2

        // Предпочитаем облачко сверху-справа от маркера, иначе снизу
        val preferAbove = markerY - markerRadius - tailSize - bubbleH - margin > 0f
        val bubbleLeftRaw = markerX + markerRadius * 0.55f
        // coerceIn() кидает исключение на "пустом диапазоне" (max < min) — например, если облачко шире доступного места.
        val maxBubbleLeft = width - bubbleW - margin
        val bubbleLeft = if (maxBubbleLeft <= margin) margin else bubbleLeftRaw.coerceIn(margin, maxBubbleLeft)

        val bubbleTop = if (preferAbove) {
            (markerY - markerRadius - tailSize - bubbleH - margin).coerceAtLeast(margin)
        } else {
            (markerY + markerRadius + tailSize + margin).coerceAtMost(height - bubbleH - margin)
        }
        val bubbleRect = RectF(bubbleLeft, bubbleTop, bubbleLeft + bubbleW, bubbleTop + bubbleH)

        // Не "квадрат со скруглениями", а облачко (волнистый контур)
        val cloudPath = buildCloudPath(bubbleRect, radius, bump = radius * 0.9f)

        // Рисуем тело облачка
        canvas.drawPath(cloudPath, bubbleFillPaint)
        canvas.drawPath(cloudPath, bubbleStrokePaint)

        // Хвост (треугольник) к маркеру
        val tailPath = Path()
        if (preferAbove) {
            // Хвост вниз
            val baseY = bubbleRect.bottom
            val minBaseX = bubbleRect.left + radius
            val maxBaseX = bubbleRect.right - radius
            val baseX = if (maxBaseX <= minBaseX) bubbleRect.centerX() else markerX.coerceIn(minBaseX, maxBaseX)
            tailPath.moveTo(baseX - tailSize, baseY)
            tailPath.lineTo(baseX + tailSize, baseY)
            tailPath.lineTo(markerX, markerY - markerRadius * 0.15f)
            tailPath.close()
        } else {
            // Хвост вверх
            val baseY = bubbleRect.top
            val minBaseX = bubbleRect.left + radius
            val maxBaseX = bubbleRect.right - radius
            val baseX = if (maxBaseX <= minBaseX) bubbleRect.centerX() else markerX.coerceIn(minBaseX, maxBaseX)
            tailPath.moveTo(baseX - tailSize, baseY)
            tailPath.lineTo(baseX + tailSize, baseY)
            tailPath.lineTo(markerX, markerY + markerRadius * 0.15f)
            tailPath.close()
        }
        canvas.drawPath(tailPath, bubbleFillPaint)
        canvas.drawPath(tailPath, bubbleStrokePaint)

        // Текст
        var y = bubbleRect.top + paddingY - bubbleTextPaint.fontMetrics.ascent
        val x = bubbleRect.left + paddingX
        speechBubbleLines.forEachIndexed { idx, line ->
            canvas.drawText(line, x, y, bubbleTextPaint)
            y += lineHeight + if (idx == speechBubbleLines.lastIndex) 0f else lineGap
        }
    }

    /**
     * Создаёт "комикс-облачко": волнистый контур вокруг прямоугольника.
     * Реализовано через последовательность quadTo (без булевых операций Path).
     */
    private fun buildCloudPath(rect: RectF, r: Float, bump: Float): Path {
        val left = rect.left
        val top = rect.top
        val right = rect.right
        val bottom = rect.bottom

        // Если облачко очень маленькое — деградируем в овал
        if (rect.width() < 2.2f * r || rect.height() < 2.2f * r) {
            return Path().apply { addOval(rect, Path.Direction.CW) }
        }

        val path = Path()

        // Разбиваем стороны на сегменты и делаем "пухлые" дуги наружу
        val w = right - left
        val h = bottom - top
        val topSeg = (w - 2 * r) / 3f
        val bottomSeg = topSeg
        val sideSeg = (h - 2 * r) / 2f

        // Start: верхняя левая после радиуса
        path.moveTo(left + r, top)

        // Top (3 bumps вверх)
        run {
            val y = top
            var x = left + r
            repeat(3) {
                val xMid = x + topSeg / 2f
                val xEnd = x + topSeg
                path.quadTo(xMid, y - bump, xEnd, y)
                x = xEnd
            }
        }

        // Right (2 bumps вправо)
        run {
            val x = right
            var y = top + r
            repeat(2) {
                val yMid = y + sideSeg / 2f
                val yEnd = y + sideSeg
                path.quadTo(x + bump, yMid, x, yEnd)
                y = yEnd
            }
        }

        // Bottom (3 bumps вниз)
        run {
            val y = bottom
            var x = right - r
            repeat(3) {
                val xMid = x - bottomSeg / 2f
                val xEnd = x - bottomSeg
                path.quadTo(xMid, y + bump, xEnd, y)
                x = xEnd
            }
        }

        // Left (2 bumps влево)
        run {
            val x = left
            var y = bottom - r
            repeat(2) {
                val yMid = y - sideSeg / 2f
                val yEnd = y - sideSeg
                path.quadTo(x - bump, yMid, x, yEnd)
                y = yEnd
            }
        }

        path.close()
        return path
    }

    private fun wrapTextToLines(text: String, paint: Paint, maxWidth: Float): List<String> {
        val result = mutableListOf<String>()
        val paragraphs = text.split('\n')

        paragraphs.forEach { paragraph ->
            if (paragraph.isBlank()) {
                result.add("")
                return@forEach
            }

            val words = paragraph.split(Regex("\\s+"))
            var current = ""
            words.forEach { w ->
                val candidate = if (current.isEmpty()) w else "$current $w"
                if (paint.measureText(candidate) <= maxWidth) {
                    current = candidate
                } else {
                    if (current.isNotEmpty()) result.add(current)
                    // если слово слишком длинное — режем посимвольно
                    if (paint.measureText(w) > maxWidth) {
                        var chunk = ""
                        w.forEach { ch ->
                            val c2 = chunk + ch
                            if (paint.measureText(c2) <= maxWidth) chunk = c2
                            else {
                                if (chunk.isNotEmpty()) result.add(chunk)
                                chunk = ch.toString()
                            }
                        }
                        current = chunk
                    } else {
                        current = w
                    }
                }
            }
            if (current.isNotEmpty()) result.add(current)
        }

        return result.take(10) // ограничим высоту облачка
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val handled = gestureHandler.handleTouchEvent(event)
        return handled || super.onTouchEvent(event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        searchModeController.cleanup()
    }

    // ============ Вспомогательные методы ============

    /**
     * Проверяет, является ли тип POI разрешенным
     */
    private fun isAllowedPOIType(poiType: POIType): Boolean {
        val type = poiType.osmTag.lowercase()
        if (type.isEmpty()) return false

        return type.contains("cafe") || type.contains("coffee") ||
               type.contains("restaurant") || type.contains("food") ||
               type.contains("bar") || type.contains("pub") || type.contains("nightclub") ||
               type.contains("hookah") || type.contains("shisha") || type.contains("кальян") ||
               type.contains("park") || type.contains("garden") || type.contains("playground")
    }
}
