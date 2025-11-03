package com.example.victor_ai.ui.map.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.victor_ai.ui.map.renderer.POIMarkerRenderer
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import com.example.victor_ai.ui.places.LatLng
import com.example.victor_ai.ui.places.MapBounds
import com.example.victor_ai.ui.places.POI

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
        private const val GRID_CELL_SIZE = 100f // Размер ячейки сетки в пикселях
        private const val USER_MARKER_RADIUS = 20f // Радиус маркера пользователя
    }

    // Данные карты
    private var mapBounds: MapBounds? = null
    private var pois: List<POI> = emptyList()
    private var userLocation: LatLng? = null

    // Утилиты
    private var coordinateConverter: CoordinateConverter? = null
    private val markerRenderer = POIMarkerRenderer(context)

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

    private val userMarkerPaint = Paint().apply {
        color = Color.BLUE
        alpha = 200
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val userMarkerStrokePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3f
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
        this.pois = pois
        this.userLocation = userLocation

        // Пересоздаем конвертер с новыми размерами
        if (width > 0 && height > 0) {
            coordinateConverter = CoordinateConverter(
                bounds = bounds,
                viewWidth = width.toFloat(),
                viewHeight = height.toFloat()
            )
        }

        invalidate() // Перерисовываем View
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
        this.pois = newPOIs
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
     * Рисует маркер текущей позиции пользователя
     */
    private fun drawUserMarker(canvas: Canvas) {
        val location = userLocation ?: return
        val converter = coordinateConverter ?: return

        if (!converter.isInBounds(location)) return

        val (x, y) = converter.gpsToScreen(location)

        // Синий круг с белой границей
        canvas.drawCircle(x, y, USER_MARKER_RADIUS, userMarkerPaint)
        canvas.drawCircle(x, y, USER_MARKER_RADIUS, userMarkerStrokePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            handleClick(event.x, event.y)
            return true
        }
        return super.onTouchEvent(event)
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

    /**
     * Очистка ресурсов
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Очищаем кэш эмодзи при удалении View
        // (можно вызвать EmojiMapper.clearCache(), но лучше сделать это в Activity)
    }
}
