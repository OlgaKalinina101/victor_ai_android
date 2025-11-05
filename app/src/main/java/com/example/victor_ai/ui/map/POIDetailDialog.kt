package com.example.victor_ai.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.victor_ai.ui.places.LatLng
import com.example.victor_ai.ui.places.POI
import com.example.victor_ai.ui.map.utils.LocationUtils

/**
 * 💬 POI
 *
 * Отображает информацию о месте и позволяет:
 * - Отметить место как посещенное
 * - Добавить впечатление
 * - Просмотреть детали
 * - Показывает расстояние до POI
 */
@Composable
fun MiniCompass(bearing: Float, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val arrowColor = colors.primary
    val circleColor = colors.onSurface.copy(alpha = 0.4f)

    Canvas(modifier = modifier.size(20.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2.5f
        drawCircle(color = circleColor, radius = radius, style = Stroke(width = 1.5f))
        rotate(bearing) {
            drawLine(
                color = arrowColor,
                start = center,
                end = Offset(center.x, center.y - radius),
                strokeWidth = 2.5f
            )
        }
    }
}



@Composable
fun POIOverlay(
    poi: POI,
    userLocation: LatLng?,
    searching: Boolean,
    elapsedSec: Long,
    walkedMeters: Double,
    nearby: List<POI>,
    onToggleSearch: () -> Unit,
    onDismiss: () -> Unit,
    onSelectNearby: (POI) -> Unit,
    modifier: Modifier = Modifier
) {
    val distance = userLocation?.let { LocationUtils.calculateDistance(it, poi.location) }
    val distanceText = distance?.let { LocationUtils.formatDistance(it) } ?: "?"

    val bearing = userLocation?.let { LocationUtils.calculateBearing(it, poi.location) } ?: 0f

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(poi.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(poi.type.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("📍 Расстояние: $distanceText", style = MaterialTheme.typography.bodyMedium)
                MiniCompass(bearing = bearing)
            }

            // Панель поиска
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val buttonColor = if (searching) Color(0xFFD32F2F) else Color(0xFF2B2929)
                Button(
                    onClick = onToggleSearch,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(if (searching) "Стоп" else "Искать")
                }

                if (searching) {
                    val mm = (elapsedSec / 60).toString().padStart(2, '0')
                    val ss = (elapsedSec % 60).toString().padStart(2, '0')
                    Text("⏱ $mm:$ss  ·  🚶 ${LocationUtils.formatDistance(walkedMeters)}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Рядом (подсказки)
            if (nearby.isNotEmpty()) {
                Text(
                    "Рядом:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    nearby.forEach { n ->
                        val dist = LocationUtils.calculateDistance(poi.location, n.location)
                        val distText = LocationUtils.formatDistance(dist)

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White, // 👈 полностью непрозрачный белый фон
                            shadowElevation = 1.dp,
                            tonalElevation = 0.dp,
                            modifier = Modifier.clickable { onSelectNearby(n) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = n.name,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "· $distText", // 👈 расстояние до текущего POI
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }


            TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) { Text("Закрыть") }
        }
    }
}