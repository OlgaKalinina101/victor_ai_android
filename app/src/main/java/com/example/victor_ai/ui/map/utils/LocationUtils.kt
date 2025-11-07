package com.example.victor_ai.ui.map.utils

import com.example.victor_ai.ui.map.models.LatLng
import kotlin.math.*

/**
 * 📍 Утилиты для работы с GPS координатами
 *
 * Функции:
 * - Расчет расстояния между точками (формула Haversine)
 * - Расчет угла направления между точками
 */
object LocationUtils {

    private const val EARTH_RADIUS_METERS = 6371000.0 // Радиус Земли в метрах

    /**
     * Вычисляет расстояние между двумя GPS точками
     *
     * Использует формулу Haversine для точного расчета расстояния
     * на сфере (Земле)
     *
     * @param from Начальная точка
     * @param to Конечная точка
     * @return Расстояние в метрах
     */
    fun calculateDistance(from: LatLng, to: LatLng): Double {
        val lat1 = Math.toRadians(from.lat)
        val lat2 = Math.toRadians(to.lat)
        val lon1 = Math.toRadians(from.lon)
        val lon2 = Math.toRadians(to.lon)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2) +
                cos(lat1) * cos(lat2) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_METERS * c
    }

    /**
     * Вычисляет угол направления от одной точки к другой
     *
     * @param from Начальная точка
     * @param to Конечная точка
     * @return Угол в градусах (0° = север, 90° = восток, 180° = юг, 270° = запад)
     */
    fun calculateBearing(from: LatLng, to: LatLng): Float {
        val lat1 = Math.toRadians(from.lat)
        val lat2 = Math.toRadians(to.lat)
        val lon1 = Math.toRadians(from.lon)
        val lon2 = Math.toRadians(to.lon)

        val dLon = lon2 - lon1

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) -
                sin(lat1) * cos(lat2) * cos(dLon)

        val bearing = atan2(y, x)

        // Конвертируем из радианов в градусы и нормализуем к диапазону 0-360
        return ((Math.toDegrees(bearing) + 360) % 360).toFloat()
    }

    /**
     * Форматирует расстояние в читаемый вид
     *
     * @param distanceMeters Расстояние в метрах
     * @return Строка вида "123 м" или "1.2 км"
     */
    fun formatDistance(distanceMeters: Double): String {
        return when {
            distanceMeters < 1000 -> {
                "${distanceMeters.roundToInt()} м"
            }
            else -> {
                val km = distanceMeters / 1000.0
                "%.1f км".format(km)
            }
        }
    }
}
