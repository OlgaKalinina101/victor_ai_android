package com.example.victor_ai.ui.map.canvas

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.victor_ai.ui.map.renderer.POIMarkerRenderer
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import com.example.victor_ai.ui.map.utils.LocationUtils
import com.example.victor_ai.ui.places.LatLng
import com.example.victor_ai.ui.places.MapBounds
import com.example.victor_ai.ui.places.POI
import com.example.victor_ai.ui.places.POIType

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
    private var previousLocation: LatLng? = null

    private val trailPath: MutableList<LatLng> = mutableListOf()
    private val trailPaint = Paint().apply {
        color = Color.argb(120, 33, 150, 243)
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }

    private var pulseScale: Float = 1f
    private var pulseAnimator: ValueAnimator? = null

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
    private var lastVibrationBucket: Int? = null
    private var lastVibrationTimestamp: Long = 0L

    // Утилиты
    private var coordinateConverter: CoordinateConverter? = null
    private val markerRenderer = POIMarkerRenderer(context)

    // Gesture detectors
    private val gestureDetector = GestureDetector(context, GestureListener())
    private val scaleDetector = ScaleGestureDetector(context, ScaleListener())

    // Callback для кликов на POI
    var onPOIClicked: ((POI) -> Unit)? = null

    // Paint объекты
    private val backgroundPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 1f
        isAntiAlias = true
    }

    private val arrowPaint = Paint().apply {
        color = Color.BLUE
        alpha = 200
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val arrowStrokePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
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
        this.previousLocation = userLocation

        this.initialLatRange = bounds.maxLat - bounds.minLat
        this.initialLonRange = bounds.maxLon - bounds.minLon

        trailPath.clear()
        userLocation?.let { trailPath.add(it) }

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

        val lastPoint = trailPath.lastOrNull()
        if (lastPoint == null || LocationUtils.calculateDistance(lastPoint, location) > 1.0) {
            trailPath.add(location)
            if (trailPath.size > 2000) {
                trailPath.removeAt(0)
            }
        }

        updatePulseAndVibration(location)
        previousLocation = location
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
        if (poi == null) {
            stopPulseAnimation()
            lastVibrationBucket = null
        }
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

        // 2.1. След пользователя
        drawTrail(canvas)

        // 3. Рисуем POI маркеры
        val converter = coordinateConverter
        if (converter != null && pois.isNotEmpty()) {
            markerRenderer.drawMarkers(canvas, pois, converter)
        }

        // 4. Рисуем маркер пользователя
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
     * Рисует след пользователя на карте
     */
    private fun drawTrail(canvas: Canvas) {
        val converter = coordinateConverter ?: return
        if (trailPath.size < 2) return

        val path = Path()
        var started = false
        trailPath.forEach { latLng ->
            if (converter.isInBounds(latLng)) {
                val (x, y) = converter.gpsToScreen(latLng)
                if (!started) {
                    path.moveTo(x, y)
                    started = true
                } else {
                    path.lineTo(x, y)
                }
            } else {
                started = false
            }
        }

        canvas.drawPath(path, trailPaint)
    }

    /**
     * Рисует маркер текущей позиции пользователя в виде стрелки
     */
    private fun drawUserMarker(canvas: Canvas) {
        val location = userLocation ?: return
        val converter = coordinateConverter ?: return

        if (!converter.isInBounds(location)) return

        val (x, y) = converter.gpsToScreen(location)

        // Вычисляем угол направления к выбранному POI
        val bearing = selectedPOI?.let { poi ->
            LocationUtils.calculateBearing(location, poi.location)
        } ?: 0f // Если POI не выбран, стрелка направлена на север

        // Сохраняем состояние canvas
        canvas.save()

        // Перемещаемся в точку пользователя и поворачиваем
        canvas.translate(x, y)
        canvas.rotate(bearing)
        canvas.scale(pulseScale, pulseScale)

        // Создаем путь для стрелки (треугольник)
        val arrowPath = Path().apply {
            // Верхняя точка (направление стрелки)
            moveTo(0f, -ARROW_SIZE)
            // Правая нижняя точка
            lineTo(ARROW_SIZE * 0.6f, ARROW_SIZE * 0.4f)
            // Левая нижняя точка
            lineTo(-ARROW_SIZE * 0.6f, ARROW_SIZE * 0.4f)
            // Замыкаем путь
            close()
        }

        // Рисуем белую обводку
        canvas.drawPath(arrowPath, arrowStrokePaint)
        // Рисуем синюю заливку
        canvas.drawPath(arrowPath, arrowPaint)

        // Восстанавливаем состояние canvas
        canvas.restore()
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

    private fun updatePulseAndVibration(location: LatLng) {
        val target = selectedPOI ?: run {
            stopPulseAnimation()
            lastVibrationBucket = null
            return
        }

        val targetLocation = target.location
        val distance = LocationUtils.calculateDistance(location, targetLocation).toFloat()
        val previous = previousLocation
        val previousDistance = previous?.let { LocationUtils.calculateDistance(it, targetLocation) }

        val heading = if (previous != null) {
            LocationUtils.calculateBearing(previous, location)
        } else null
        val bearingToTarget = LocationUtils.calculateBearing(location, targetLocation)

        val onCourse = heading?.let {
            val diff = angularDifference(it, bearingToTarget)
            val gettingCloser = previousDistance == null || previousDistance > distance
            diff < 25f && gettingCloser
        } ?: false

        if (onCourse) {
            startPulseAnimation(distance)
        } else {
            stopPulseAnimation()
        }

        handleVibration(distance)
    }

    private fun angularDifference(a: Float, b: Float): Float {
        var diff = Math.abs(a - b) % 360f
        if (diff > 180f) diff = 360f - diff
        return diff
    }

    private fun startPulseAnimation(distance: Float) {
        val duration = (distance.coerceIn(0f, 300f) / 300f * 600f + 400f).toLong()
        val animator = pulseAnimator
        if (animator == null) {
            pulseAnimator = ValueAnimator.ofFloat(1f, 1.25f).apply {
                this.duration = duration
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener {
                    pulseScale = it.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            if (animator.duration != duration) {
                animator.duration = duration
            }
            if (!animator.isStarted) {
                animator.start()
            }
        }
    }

    private fun stopPulseAnimation() {
        pulseAnimator?.cancel()
        pulseAnimator = null
        pulseScale = 1f
    }

    private fun handleVibration(distance: Float) {
        if (distance >= 50f) {
            lastVibrationBucket = null
            return
        }

        val step = if (distance < 10f) 2 else 10
        val bucket = (distance / step).toInt()
        val now = SystemClock.elapsedRealtime()

        if (lastVibrationBucket != bucket || now - lastVibrationTimestamp > 1500L) {
            vibrator?.let { vib ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val duration = if (distance < 10f) 100L else 50L
                    val amplitude = if (distance < 10f) 200 else 120
                    vib.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                } else {
                    @Suppress("DEPRECATION")
                    vib.vibrate(if (distance < 10f) 100L else 50L)
                }
            }
            lastVibrationBucket = bucket
            lastVibrationTimestamp = now
        }
    }

    /**
     * Очистка ресурсов
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Очищаем кэш эмодзи при удалении View
        // (можно вызвать EmojiMapper.clearCache(), но лучше сделать это в Activity)
        stopPulseAnimation()
    }
}
