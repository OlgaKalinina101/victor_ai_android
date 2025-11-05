package com.example.victor_ai.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.victor_ai.ui.map.utils.LocationUtils
import com.example.victor_ai.ui.places.LatLng
import com.example.victor_ai.ui.places.POI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 💬 Диалог с деталями POI
 *
 * Отображает информацию о месте и позволяет:
 * - Отметить место как посещенное
 * - Просмотреть детали
 * - Показывает расстояние до POI
 */
@Composable
fun POIDetailDialog(
    poi: POI,
    userLocation: LatLng?,
    onDismiss: () -> Unit,
    onMarkAsVisited: (String) -> Unit
) {
    val distance = userLocation?.let {
        LocationUtils.calculateDistance(it, poi.location)
    }
    val distanceText = distance?.let {
        LocationUtils.formatDistance(it)
    } ?: "Расстояние неизвестно"

    val bearing = userLocation?.let {
        LocationUtils.calculateBearing(it, poi.location)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, start = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.widthIn(max = 360.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = poi.type.emoji,
                            fontSize = 48.sp
                        )
                        Column {
                            Text(
                                text = poi.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = poi.type.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Divider()

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "📍", fontSize = 20.sp)
                            Text(
                                text = "Расстояние: $distanceText",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            MiniCompass(bearing = bearing)
                        }
                    }

                    if (poi.isVisited) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "✓", fontSize = 20.sp)
                                Text(
                                    text = "Посещено",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    if (poi.isVisited && poi.impression != null) {
                        OutlinedCard {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Впечатление:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = poi.impression!!,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Закрыть")
                        }

                        if (!poi.isVisited) {
                            Button(
                                onClick = {
                                    onMarkAsVisited("")
                                    onDismiss()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Посетил")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniCompass(
    bearing: Float?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(44.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val radius = size.minDimension / 2f * 0.9f
            val center = Offset(size.width / 2f, size.height / 2f)

            drawCircle(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                radius = radius,
                center = center
            )
            drawCircle(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                radius = radius,
                center = center,
                style = Stroke(width = 3f)
            )

            val angle = bearing?.let { Math.toRadians((it - 90f).toDouble()) }
            if (angle != null) {
                val lineLength = radius * 0.75f
                val endX = center.x + (cos(angle) * lineLength).toFloat()
                val endY = center.y + (sin(angle) * lineLength).toFloat()

                drawLine(
                    color = MaterialTheme.colorScheme.primary,
                    start = center,
                    end = Offset(endX, endY),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )

                drawCircle(
                    color = MaterialTheme.colorScheme.primary,
                    radius = radius * 0.15f,
                    center = Offset(endX, endY)
                )
            }
        }

        Text(
            text = "N",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

/**
 * Preview для диалога
 */
@Composable
private fun POIDetailDialogPreview() {
    val samplePOI = POI(
        id = "1",
        name = "Кафе Пушкин",
        type = com.example.victor_ai.ui.places.POIType.CAFE,
        location = com.example.victor_ai.ui.places.LatLng(55.7558, 37.6173),
        isVisited = false
    )

    POIDetailDialog(
        poi = samplePOI,
        userLocation = com.example.victor_ai.ui.places.LatLng(55.751244, 37.618423),
        onDismiss = {},
        onMarkAsVisited = {}
    )
}
