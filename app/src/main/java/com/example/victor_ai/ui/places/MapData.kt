package com.example.victor_ai.ui.places

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class MapFeature(
    val type: FeatureType,
    val path: List<Offset>,
    val color: Color? = null,
    val strokeWidth: Float? = null
)

enum class FeatureType { BUILDING, ROAD, RIVER }

object MitinoMapData {
    private fun Float.percentX(width: Float) = this * width / 100f
    private fun Float.percentY(height: Float) = this * height / 100f

    fun getFeatures(width: Float, height: Float): List<MapFeature> = listOf(
        // РЕКА (как в макете!)
        MapFeature(
            type = FeatureType.RIVER,
            path = listOf(
                Offset(0f, 35f.percentY(height)),
                Offset(15f.percentX(width), 55f.percentY(height)),
                Offset(35f.percentX(width), 25f.percentY(height)),
                Offset(60f.percentX(width), 45f.percentY(height)),
                Offset(80f.percentX(width), 30f.percentY(height)),
                Offset(100f.percentX(width), 65f.percentY(height))
            ),
            color = Color(0xFF64B5F6),
            strokeWidth = 8f
        ),

        // ЗДАНИЯ (пример)
        MapFeature(
            type = FeatureType.BUILDING,
            path = listOf(
                Offset(10f.percentX(width), 20f.percentY(height)),
                Offset(25f.percentX(width), 20f.percentY(height)),
                Offset(25f.percentX(width), 35f.percentY(height)),
                Offset(10f.percentX(width), 35f.percentY(height))
            ),
            color = Color(0xFF757575)
        ),
        MapFeature(
            type = FeatureType.BUILDING,
            path = listOf(
                Offset(40f.percentX(width), 60f.percentY(height)),
                Offset(55f.percentX(width), 60f.percentY(height)),
                Offset(55f.percentX(width), 75f.percentY(height)),
                Offset(40f.percentX(width), 75f.percentY(height))
            ),
            color = Color(0xFF757575)
        ),

        // ДОРОГИ
        MapFeature(
            type = FeatureType.ROAD,
            path = listOf(
                Offset(5f.percentX(width), 40f.percentY(height)),
                Offset(95f.percentX(width), 40f.percentY(height))
            ),
            color = Color.LightGray,
            strokeWidth = 3f
        ),
        MapFeature(
            type = FeatureType.ROAD,
            path = listOf(
                Offset(30f.percentX(width), 10f.percentY(height)),
                Offset(30f.percentX(width), 90f.percentY(height))
            ),
            color = Color.LightGray,
            strokeWidth = 3f
        )
    )
}

fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371000 // радиус Земли в метрах
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return r * c
}