package com.example.victor_ai.ui.map.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import android.view.View
import com.example.victor_ai.ui.map.renderer.POIMarkerRenderer
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import com.example.victor_ai.ui.map.utils.LocationUtils
import com.example.victor_ai.ui.places.LatLng
import com.example.victor_ai.ui.places.MapBounds
import com.example.victor_ai.ui.places.POI
import com.example.victor_ai.ui.places.POIType
import kotlin.math.sin
import androidx.core.graphics.toColorInt

/**
 * 🗺️ Custom View для отображения карты с POI маркерами
 *
 * Рисует:
 * - Белый фон
 * - Серую сетку
 * - POI маркеры с эмодзи
 * - Текущую позицию пользователя
 */
class MapCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

companion object {
    private const val GRID_CELL_SIZE = 200f // Размер ячейки сетки в пикселях
    private const val ARROW_SIZE = 40f // Размер стрелки пользователя

    private const val MAX_ZOOM = 12f // Зум на старте
}

/**
 * Проверяет, является ли тип POI разрешенным
 */
private fun isAllowedPOIType(poiType: POIType): Boolean {
    val type = poiType.osmTag.lowercase()
    if (type.isEmpty()) return false

    // Кофейни
    if (type.contains("cafe") || type.contains("coffee")) return true

    // Рестораны
    if (type.contains("restaurant") || type.contains("food")) return true

    // Бары
    if (type.contains("bar") || type.contains("pub") || type.contains("nightclub")) return true

    // Кальянные
    if (type.contains("hookah") || type.contains("shisha") || type.contains("кальян")) return true

    // Парки
    if (type.contains("park") || type.contains("garden") || type.contains("playground")) return true

    return false
}

    private var initialLatRange: Double = 0.0
    private var initialLonRange: Double = 0.0
    private var currentZoom: Float = 300f

    // Данные карты
    private var mapBounds: MapBounds? = null
    private var pois: List<POI> = emptyList()
    private var userLocation: LatLng? = null
    private var selectedPOI: POI? = null // Выбранный POI для направления стрелки
    private var isSearching: Boolean = false // Режим поиска/навигации

    // Утилиты
    private var coordinateConverter: CoordinateConverter? = null
    private val markerRenderer = POIMarkerRenderer(context)

    // Анимация
    private var animationTime: Long = 0
    private val animationRunnable = object : Runnable {
        override fun run() {
            if (isSearching) {
                animationTime = System.currentTimeMillis()
                invalidate()
                postDelayed(this, 50) // 20 FPS
            }
        }
    }

    // Gesture detectors
    private val gestureDetector = GestureDetector(context, GestureListener())
    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())

    // Callback для кликов на POI
    var onPOIClicked: ((POI) -> Unit)? = null

    private val gridPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 1f
        isAntiAlias = true
    }

    private val trailPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 6f
        alpha = 160
        isAntiAlias = true
    }

    // Paint для пунктирной линии до цели
    private val dashedLinePaint = Paint().apply {
        color = "#4A4A4A".toColorInt()
        style = Paint.Style.STROKE
        strokeWidth = 16f                // в 2 раза толще
        alpha = 220                      // чуть плотнее
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

    private val trailPoints: MutableList<LatLng> = mutableListOf()

    fun setTrail(points: List<LatLng>) {
        trailPoints.clear()
        trailPoints.addAll(points)
        invalidate()
    }

    /**
     * Включает режим поиска с анимацией
     */
    fun startSearchMode() {
        isSearching = true
        animationTime = System.currentTimeMillis()
        removeCallbacks(animationRunnable)
        post(animationRunnable)
    }

    /**
     * Выключает режим поиска
     */
    fun stopSearchMode() {
        isSearching = false
        removeCallbacks(animationRunnable)
        invalidate()
    }


    /**
     * Устанавливает данные карты
     */
    fun setMapData(
        bounds: MapBounds,
        pois: List<POI>,
        userLocation: LatLng? = null
    ) {
        this.mapBounds = bounds
        this.pois = pois.filter { isAllowedPOIType(it.type) }
        this.userLocation = userLocation

        this.initialLatRange = bounds.maxLat - bounds.minLat
        this.initialLonRange = bounds.maxLon - bounds.minLon

        if (width > 0 && height > 0) {
            coordinateConverter = CoordinateConverter(
                bounds = bounds,
                viewWidth = width.toFloat(),
                viewHeight = height.toFloat()
            )

            // ВАЖНО: СНАЧАЛА ЦЕНТРИРУЕМ НА ПОЛЬЗОВАТЕЛЕ
            userLocation?.let { panTo(it) }

            // ТОЛЬКО ПОТОМ — ЗУМИМ ДО МАКСИМУМА
            zoomTo(MAX_ZOOM)
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
        this.pois = newPOIs.filter { isAllowedPOIType(it.type) }
        invalidate()
    }

    /**
     * Устанавливает выбранный POI для направления стрелки
     */
    fun setSelectedPOI(poi: POI?) {
        this.selectedPOI = poi
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Пересоздаем конвертер с новыми размерами
        mapBounds?.let { bounds ->
            coordinateConverter = CoordinateConverter(
                bounds = bounds,
                viewWidth = w.toFloat(),
                viewHeight = h.toFloat()
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1. Рисуем белый фон
        canvas.drawColor(Color.WHITE)

        // 2. Рисуем серую сетку
        drawGrid(canvas)

        // 2.5. Трек пользователя
        drawTrail(canvas)

        // 3. Пунктирная линия до цели (если режим поиска)
        if (isSearching) {
            drawDashedLineToTarget(canvas)
        }

        // 4. Рисуем POI маркеры
        val converter = coordinateConverter
        if (converter != null && pois.isNotEmpty()) {
            markerRenderer.drawMarkers(canvas, pois, converter)
        }

        // 5. Пульсирующая анимация на цели (если режим поиска)
        if (isSearching) {
            drawPulsingTarget(canvas)
        }

        // 6. Рисуем маркер пользователя
        drawUserMarker(canvas)
    }

    /**
     * Рисует сетку на карте
     */
    private fun drawGrid(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        // Вертикальные линии
        var x = 0f
        while (x <= w) {
            canvas.drawLine(x, 0f, x, h, gridPaint)
            x += GRID_CELL_SIZE
        }

        // Горизонтальные линии
        var y = 0f
        while (y <= h) {
            canvas.drawLine(0f, y, w, y, gridPaint)
            y += GRID_CELL_SIZE
        }
    }

    /**
     * Рисует маркер текущей позиции пользователя в виде стрелки
     */
    private fun drawUserMarker(canvas: Canvas) {
        val location = userLocation ?: return
        val converter = coordinateConverter ?: return

        if (!converter.isInBounds(location)) return

        val (x, y) = converter.gpsToScreen(location)
        val bearing = selectedPOI?.let { poi ->
            LocationUtils.calculateBearing(location, poi.location)
        } ?: 0f

        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(bearing)

        // Размер стрелки
        val arrowSize = 32f

        // Немного закругленные углы
        val arrowPath = Path().apply {
            moveTo(0f, -arrowSize)                          // верх
            lineTo(arrowSize * 0.7f, arrowSize * 0.4f)     // нижний правый угол
            lineTo(arrowSize * 0.4f, arrowSize * 0.4f)     // вырез под основание
            lineTo(arrowSize * 0.4f, arrowSize)            // край основания
            lineTo(-arrowSize * 0.4f, arrowSize)           // противоположный край основания
            lineTo(-arrowSize * 0.4f, arrowSize * 0.4f)    // вырез под основание
            lineTo(-arrowSize * 0.7f, arrowSize * 0.4f)    // нижний левый угол
            close()
        }

        val arrowPaint = Paint().apply {
            color = Color.parseColor("#4A4A4A")
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val arrowStrokePaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
        }

        canvas.drawPath(arrowPath, arrowStrokePaint)
        canvas.drawPath(arrowPath, arrowPaint)

        canvas.restore()
    }


    /**
     * Рисует след на карте
     */
    private fun drawTrail(canvas: Canvas) {
        val converter = coordinateConverter ?: return
        if (trailPoints.size < 2) return

        val footprintPaint = Paint(trailPaint).apply {
            textSize = 48f
            alpha = 180 // полупрозрачные, как тень
            textAlign = Paint.Align.CENTER
        }

        var prevX = 0f
        var prevY = 0f
        var isFirst = true

        for ((i, point) in trailPoints.withIndex()) {
            val (screenX, screenY) = converter.gpsToScreen(point)

            if (isFirst) {
                isFirst = false
            } else {
                // Считаем направление от предыдущей точки
                val dx = screenX - prevX
                val dy = screenY - prevY
                val distance = kotlin.math.hypot(dx.toDouble(), dy.toDouble()).toFloat()

                // Шаг ~30–40 пикселей (настраивай под масштаб!)
                val stepDistance = 35f
                val steps = (distance / stepDistance).toInt()

                // Рисуем следы вдоль пути
                for (step in 0..steps) {
                    val ratio = step.toFloat() / steps.coerceAtLeast(1)
                    val x = prevX + dx * ratio
                    val y = prevY + dy * ratio

                    // Поворачиваем след по направлению движения
                    canvas.save()
                    canvas.translate(x, y)
                    val angle = kotlin.math.atan2(dy, dx) * 180 / kotlin.math.PI.toFloat()
                    canvas.rotate(angle)

                    // Чередуем левый/правый след (для реализма!)
                    val emoji = if (step % 2 == 0) "👞" else "👟" // или просто "👣"
                    canvas.drawText(emoji, 0f, 0f, footprintPaint)

                    canvas.restore()
                }
            }

            prevX = screenX
            prevY = screenY
        }
    }
    /**
     * Рисует пунктирную линию от пользователя до целевого POI
     */
    private fun drawDashedLineToTarget(canvas: Canvas) {
        val converter = coordinateConverter ?: return
        val target = selectedPOI ?: return
        val userLoc = userLocation ?: return

        if (!converter.isInBounds(userLoc) || !converter.isInBounds(target.location)) return

        val (userX, userY) = converter.gpsToScreen(userLoc)
        val (targetX, targetY) = converter.gpsToScreen(target.location)

        canvas.drawLine(userX, userY, targetX, targetY, dashedLinePaint)
    }

    /**
     * Рисует пульсирующую анимацию на целевой точке
     */
    private fun drawPulsingTarget(canvas: Canvas) {
        val converter = coordinateConverter ?: return
        val target = selectedPOI ?: return

        if (!converter.isInBounds(target.location)) return

        val (x, y) = converter.gpsToScreen(target.location)

        // Вычисляем радиус пульсации (от 50 до 80 пикселей)
        val time = animationTime % 1500 // Период 1.5 секунды
        val progress = time / 1500f
        val radius = 50f + 30f * sin(progress * Math.PI * 2).toFloat()
        val alpha = (255 * (1 - progress)).toInt().coerceIn(0, 255)

        pulseCirclePaint.alpha = alpha
        canvas.drawCircle(x, y, radius, pulseCirclePaint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        var handled = scaleDetector.onTouchEvent(event)
        handled = gestureDetector.onTouchEvent(event) || handled
        return handled || super.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val converter = coordinateConverter ?: return false

            val deltaLat = (distanceY / height) * (mapBounds!!.maxLat - mapBounds!!.minLat)
            val deltaLon = (distanceX / width) * (mapBounds!!.maxLon - mapBounds!!.minLon)

            mapBounds = MapBounds(
                minLat = mapBounds!!.minLat + deltaLat,
                maxLat = mapBounds!!.maxLat + deltaLat,
                minLon = mapBounds!!.minLon - deltaLon,
                maxLon = mapBounds!!.maxLon - deltaLon
            )

            updateConverter()
            invalidate()
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            handleClick(e.x, e.y)
            return true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            val newZoom = currentZoom * scaleFactor
            zoomTo(newZoom.coerceIn(0.5f, 10f)) // Ограничение зума
            return true
        }
    }

    /**
     * Обрабатывает клик на карте
     */
    private fun handleClick(x: Float, y: Float) {
        val converter = coordinateConverter ?: return

        // Находим POI, на который кликнули
        val clickedPOI = markerRenderer.findClickedPOI(pois, x, y, converter)

        if (clickedPOI != null) {
            onPOIClicked?.invoke(clickedPOI)
        }
    }

    private fun updateConverter() {
        if (width > 0 && height > 0 && mapBounds != null) {
            coordinateConverter = CoordinateConverter(
                mapBounds!!,
                width.toFloat(),
                height.toFloat()
            )
        }
    }

    private fun getCurrentCenter(): LatLng {
        val b = mapBounds ?: return LatLng(0.0, 0.0)
        return LatLng(
            (b.minLat + b.maxLat) / 2,
            (b.minLon + b.maxLon) / 2
        )
    }

    fun panTo(location: LatLng) {
        val currentLatRange = mapBounds?.let { it.maxLat - it.minLat } ?: return
        val currentLonRange = mapBounds?.let { it.maxLon - it.minLon } ?: return

        mapBounds = MapBounds(
            minLat = location.lat - currentLatRange / 2,
            maxLat = location.lat + currentLatRange / 2,
            minLon = location.lon - currentLonRange / 2,
            maxLon = location.lon + currentLonRange / 2
        )

        updateConverter()
        invalidate()
    }

    fun zoomTo(zoom: Float) {
        currentZoom = zoom.coerceIn(1f, MAX_ZOOM)  // ОК

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
        invalidate()
    }

    /**
     * Очистка ресурсов
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Очищаем кэш эмодзи при удалении View
        // (можно вызвать EmojiMapper.clearCache(), но лучше сделать это в Activity)
    }
}
