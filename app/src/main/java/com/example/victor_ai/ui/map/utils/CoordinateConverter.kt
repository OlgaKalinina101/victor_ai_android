package com.example.victor_ai.ui.map.utils

import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.MapBounds

/**
 * 🗺️ Конвертер GPS координат в экранные координаты
 *
 * Преобразует географические координаты (широта/долгота) в пиксели на экране
 */
class CoordinateConverter(
    private val bounds: MapBounds,
    private val viewWidth: Float,
    private val viewHeight: Float
) {

    // Вычисляем размеры области в градусах
    private val latRange = bounds.maxLat - bounds.minLat
    private val lonRange = bounds.maxLon - bounds.minLon

    /**
     * Конвертирует GPS координаты в экранные координаты
     * @return Pair(x, y) в пикселях
     */
    fun gpsToScreen(latLng: LatLng): Pair<Float, Float> {
        // Нормализуем координаты (0.0 - 1.0)
        val normalizedX = (latLng.lon - bounds.minLon) / lonRange
        val normalizedY = (bounds.maxLat - latLng.lat) / latRange // Инвертируем Y (карта сверху вниз)

        // Преобразуем в пиксели
        val x = normalizedX.toFloat() * viewWidth
        val y = normalizedY.toFloat() * viewHeight

        return Pair(x, y)
    }

    /**
     * Конвертирует экранные координаты обратно в GPS
     */
    fun screenToGps(x: Float, y: Float): LatLng {
        val normalizedX = x / viewWidth
        val normalizedY = y / viewHeight

        val lon = bounds.minLon + (normalizedX * lonRange)
        val lat = bounds.maxLat - (normalizedY * latRange) // Инвертируем Y обратно

        return LatLng(lat, lon)
    }

    /**
     * Проверяет, попадает ли точка в границы карты
     */
    fun isInBounds(latLng: LatLng): Boolean {
        return latLng.lat in bounds.minLat..bounds.maxLat &&
               latLng.lon in bounds.minLon..bounds.maxLon
    }

    /**
     * Вычисляет масштаб карты (метров на пиксель)
     */
    fun getMetersPerPixel(): Float {
        // Примерное вычисление: 1 градус широты ≈ 111 км
        val heightInMeters = latRange * 111_000
        return (heightInMeters / viewHeight).toFloat()
    }
}
