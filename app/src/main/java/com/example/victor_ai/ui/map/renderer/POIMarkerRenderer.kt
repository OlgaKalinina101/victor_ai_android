package com.example.victor_ai.ui.map.renderer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.victor_ai.ui.map.utils.CoordinateConverter
import com.example.victor_ai.ui.map.utils.EmojiMapper
import com.example.victor_ai.ui.map.models.POI

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
     * Отрисовывает все POI маркеры с умным размещением (без наложения)
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
        // Список уже размещенных маркеров (экранные координаты)
        val placedMarkers = mutableListOf<Pair<Float, Float>>()
        val minDistance = 50f // Минимальное расстояние между маркерами в пикселях

        pois.forEach { poi ->
            // Исходные экранные координаты из GPS
            val (baseX, baseY) = converter.gpsToScreen(poi.location)

            // Проверяем границы
            if (!converter.isInBounds(poi.location)) {
                return@forEach
            }

            // Ищем свободное место для маркера
            var finalX = baseX
            var finalY = baseY
            var attemptAngle = 0f
            var attemptRadius = 0f

            // Если маркер перекрывается с уже размещенными - смещаем по спирали
            while (hasOverlap(finalX, finalY, placedMarkers, minDistance) && attemptRadius < 120f) {
                attemptAngle += 45f // Шаг 45 градусов (8 точек вокруг)
                if (attemptAngle >= 360f) {
                    // Новый виток спирали
                    attemptAngle = 0f
                    attemptRadius += 15f
                }

                finalX = baseX + attemptRadius * kotlin.math.cos(Math.toRadians(attemptAngle.toDouble())).toFloat()
                finalY = baseY + attemptRadius * kotlin.math.sin(Math.toRadians(attemptAngle.toDouble())).toFloat()
            }

            // Сохраняем итоговую позицию
            placedMarkers.add(Pair(finalX, finalY))

            // Рисуем маркер со смещенными координатами
            drawMarkerAt(canvas, poi, finalX, finalY)
        }
    }

    /**
     * Проверяет, перекрывается ли маркер с уже размещенными
     */
    private fun hasOverlap(
        x: Float,
        y: Float,
        placed: List<Pair<Float, Float>>,
        minDist: Float
    ): Boolean {
        return placed.any { (px, py) ->
            val dx = x - px
            val dy = y - py
            val dist = kotlin.math.sqrt(dx * dx + dy * dy)
            dist < minDist
        }
    }

    /**
     * Отрисовывает маркер на заданных экранных координатах
     */
    private fun drawMarkerAt(
        canvas: Canvas,
        poi: POI,
        x: Float,
        y: Float
    ) {
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
     * Вычисляет итоговую позицию маркера с учетом смещения от перекрытий
     * (используется и при рисовании, и при проверке кликов)
     */
    private fun calculateMarkerPosition(
        poi: POI,
        pois: List<POI>,
        converter: CoordinateConverter
    ): Pair<Float, Float> {
        val placedMarkers = mutableListOf<Pair<Float, Float>>()
        val minDistance = 50f

        // Вычисляем позиции всех POI до текущего
        val index = pois.indexOf(poi)
        for (i in 0 until index) {
            val prevPOI = pois[i]
            if (!converter.isInBounds(prevPOI.location)) continue

            val (baseX, baseY) = converter.gpsToScreen(prevPOI.location)
            var finalX = baseX
            var finalY = baseY
            var attemptAngle = 0f
            var attemptRadius = 0f

            while (hasOverlap(finalX, finalY, placedMarkers, minDistance) && attemptRadius < 120f) {
                attemptAngle += 45f
                if (attemptAngle >= 360f) {
                    attemptAngle = 0f
                    attemptRadius += 15f
                }
                finalX = baseX + attemptRadius * kotlin.math.cos(Math.toRadians(attemptAngle.toDouble())).toFloat()
                finalY = baseY + attemptRadius * kotlin.math.sin(Math.toRadians(attemptAngle.toDouble())).toFloat()
            }
            placedMarkers.add(Pair(finalX, finalY))
        }

        // Вычисляем позицию текущего POI
        val (baseX, baseY) = converter.gpsToScreen(poi.location)
        var finalX = baseX
        var finalY = baseY
        var attemptAngle = 0f
        var attemptRadius = 0f

        while (hasOverlap(finalX, finalY, placedMarkers, minDistance) && attemptRadius < 120f) {
            attemptAngle += 45f
            if (attemptAngle >= 360f) {
                attemptAngle = 0f
                attemptRadius += 15f
            }
            finalX = baseX + attemptRadius * kotlin.math.cos(Math.toRadians(attemptAngle.toDouble())).toFloat()
            finalY = baseY + attemptRadius * kotlin.math.sin(Math.toRadians(attemptAngle.toDouble())).toFloat()
        }

        return Pair(finalX, finalY)
    }

    /**
     * Проверяет, попал ли клик на маркер (с учетом смещения от перекрытий)
     *
     * @param poi POI для проверки
     * @param pois Все POI (для вычисления смещений)
     * @param clickX X координата клика
     * @param clickY Y координата клика
     * @param converter Конвертер координат
     * @return true если клик попал на маркер
     */
    fun isMarkerClicked(
        poi: POI,
        pois: List<POI>,
        clickX: Float,
        clickY: Float,
        converter: CoordinateConverter
    ): Boolean {
        val (markerX, markerY) = calculateMarkerPosition(poi, pois, converter)

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
            isMarkerClicked(poi, pois, clickX, clickY, converter)
        }
    }
}
