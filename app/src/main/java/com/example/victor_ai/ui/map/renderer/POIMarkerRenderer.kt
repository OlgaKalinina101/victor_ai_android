package com.example.victor_ai.ui.map.renderer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import com.example.victor_ai.ui.map.utils.EmojiMapper
import com.example.victor_ai.ui.places.POI

/**
 * 🎨 Рендерер для отрисовки POI маркеров на карте
 *
 * Рисует серые полупрозрачные круги с эмодзи внутри
 */
class POIMarkerRenderer(
    private val context: Context
) {

    companion object {
        private const val MARKER_RADIUS = 40f // Радиус круга маркера
        private const val MARKER_ALPHA = 128 // Прозрачность (0-255)
        private const val MARKER_COLOR = Color.GRAY

        private const val VISITED_MARKER_COLOR = 0xFF4CAF50.toInt() // Зеленый для посещенных
        private const val EMOJI_SIZE = 48 // Размер эмодзи
    }

    // Paint для серого круга
    private val circlePaint = Paint().apply {
        color = MARKER_COLOR
        alpha = MARKER_ALPHA
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // Paint для зеленого круга (посещенные места)
    private val visitedCirclePaint = Paint().apply {
        color = VISITED_MARKER_COLOR
        alpha = MARKER_ALPHA
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // Paint для границы круга
    private val strokePaint = Paint().apply {
        color = Color.WHITE
        alpha = 200
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    // Paint для отрисовки эмодзи
    private val bitmapPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
    }

    /**
     * Отрисовывает один POI маркер
     *
     * @param canvas Canvas для рисования
     * @param poi Точка интереса
     * @param converter Конвертер координат
     */
    fun drawMarker(
        canvas: Canvas,
        poi: POI,
        converter: CoordinateConverter
    ) {
        // Конвертируем GPS координаты в экранные
        val (x, y) = converter.gpsToScreen(poi.location)

        // Проверяем, что маркер в границах видимости
        if (!converter.isInBounds(poi.location)) {
            return
        }

        // Выбираем Paint в зависимости от статуса посещения
        val paint = if (poi.isVisited) visitedCirclePaint else circlePaint

        // Рисуем серый/зеленый круг
        canvas.drawCircle(x, y, MARKER_RADIUS, paint)

        // Рисуем белую границу
        canvas.drawCircle(x, y, MARKER_RADIUS, strokePaint)

        // Получаем эмодзи Bitmap
        val emojiBitmap = EmojiMapper.getCachedEmojiBitmap(
            context = context,
            type = poi.type,
            size = EMOJI_SIZE
        )

        // Рисуем эмодзи в центре круга
        val emojiLeft = x - EMOJI_SIZE / 2f
        val emojiTop = y - EMOJI_SIZE / 2f
        canvas.drawBitmap(emojiBitmap, emojiLeft, emojiTop, bitmapPaint)
    }

    /**
     * Отрисовывает все POI маркеры
     *
     * @param canvas Canvas для рисования
     * @param pois Список точек интереса
     * @param converter Конвертер координат
     */
    fun drawMarkers(
        canvas: Canvas,
        pois: List<POI>,
        converter: CoordinateConverter
    ) {
        pois.forEach { poi ->
            drawMarker(canvas, poi, converter)
        }
    }

    /**
     * Проверяет, попал ли клик на маркер
     *
     * @param poi POI для проверки
     * @param clickX X координата клика
     * @param clickY Y координата клика
     * @param converter Конвертер координат
     * @return true если клик попал на маркер
     */
    fun isMarkerClicked(
        poi: POI,
        clickX: Float,
        clickY: Float,
        converter: CoordinateConverter
    ): Boolean {
        val (markerX, markerY) = converter.gpsToScreen(poi.location)

        // Вычисляем расстояние от клика до центра маркера
        val dx = clickX - markerX
        val dy = clickY - markerY
        val distance = kotlin.math.sqrt(dx * dx + dy * dy)

        // Проверяем, что клик внутри радиуса маркера
        return distance <= MARKER_RADIUS
    }

    /**
     * Находит POI, на который кликнули
     *
     * @param pois Список POI
     * @param clickX X координата клика
     * @param clickY Y координата клика
     * @param converter Конвертер координат
     * @return POI если найден, иначе null
     */
    fun findClickedPOI(
        pois: List<POI>,
        clickX: Float,
        clickY: Float,
        converter: CoordinateConverter
    ): POI? {
        // Ищем с конца, чтобы найти маркер, который нарисован сверху
        return pois.lastOrNull { poi ->
            isMarkerClicked(poi, clickX, clickY, converter)
        }
    }
}
