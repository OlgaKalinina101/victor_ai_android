package com.example.victor_ai.ui.map

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.victor_ai.ui.places.LatLng
import com.example.victor_ai.ui.places.POI
import com.example.victor_ai.ui.map.utils.LocationUtils

/**
 * 💬 POI Enhanced with Visit functionality
 *
 * Отображает информацию о месте и позволяет:
 * - Отметить место как посещенное
 * - Добавить впечатление с эмоциями
 * - Просмотреть детали
 * - Показывает расстояние до POI
 * - Автоматически предлагает отметить посещение при приближении
 */

// Данные для эмоций/впечатлений
data class VisitEmotion(
    val emoji: String,
    val name: String,
    val color: Color
)

val VISIT_EMOTIONS = listOf(
    VisitEmotion("😍", "Восхитительно", Color(0xFFE91E63)),
    VisitEmotion("😊", "Понравилось", Color(0xFF4CAF50)),
    VisitEmotion("🙂", "Неплохо", Color(0xFF2196F3)),
    VisitEmotion("😐", "Обычно", Color(0xFF9E9E9E)),
    VisitEmotion("😞", "Разочарование", Color(0xFFFF9800)),
    VisitEmotion("😤", "Ужасно", Color(0xFFF44336))
)

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
fun VisitEmotionSelector(
    visible: Boolean,
    onEmotionSelected: (VisitEmotion) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Как впечатления? 🤔",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VISIT_EMOTIONS.forEach { emotion ->
                        Surface(
                            modifier = Modifier
                                .clickable { onEmotionSelected(emotion) }
                                .padding(4.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = emotion.color.copy(alpha = 0.1f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                emotion.color.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = emotion.emoji,
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = emotion.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = emotion.color,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Отмена")
                }
            }
        }
    }
}

@Composable
fun AutoVisitPrompt(
    visible: Boolean,
    poiName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut()
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            color = Color(0xFF4CAF50),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "🎯",
                    fontSize = 24.sp
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Вы на месте!",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Отметить '$poiName' как посещенное?",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Нет", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Да!", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
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
    isVisited: Boolean = false,
    visitEmotion: VisitEmotion? = null,
    onToggleSearch: () -> Unit,
    onMarkVisited: (VisitEmotion?) -> Unit,
    onDismiss: () -> Unit,
    onSelectNearby: (POI) -> Unit,
    modifier: Modifier = Modifier
) {
    val distance = userLocation?.let { LocationUtils.calculateDistance(it, poi.location) }
    val distanceText = distance?.let { LocationUtils.formatDistance(it) } ?: "?"
    val bearing = userLocation?.let { LocationUtils.calculateBearing(it, poi.location) } ?: 0f

    // Состояния для UI
    var showEmotionSelector by remember { mutableStateOf(false) }
    var showAutoVisitPrompt by remember { mutableStateOf(false) }

    // Автоматическое предложение посещения при расстоянии < 10м
    LaunchedEffect(distance) {
        if (distance != null && distance < 10.0 && !isVisited && !searching) {
            showAutoVisitPrompt = true
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Автоматическое предложение посещения
        AutoVisitPrompt(
            visible = showAutoVisitPrompt,
            poiName = poi.name,
            onConfirm = {
                showAutoVisitPrompt = false
                showEmotionSelector = true
            },
            onDismiss = {
                showAutoVisitPrompt = false
            }
        )

        // Основная панель
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Заголовок и статус посещения
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            poi.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            poi.type.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Индикатор посещения
                    if (isVisited) {
                        Surface(
                            shape = CircleShape,
                            color = visitEmotion?.color?.copy(alpha = 0.2f) ?: Color(0xFF4CAF50).copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = visitEmotion?.emoji ?: "✅",
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "📍 Расстояние: $distanceText",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    MiniCompass(bearing = bearing)
                }

                // Панель действий
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Кнопка поиска
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

                    // Кнопка посещения
                    if (!searching) {
                        Button(
                            onClick = {
                                if (isVisited) {
                                    // Сброс посещения
                                    onMarkVisited(null)
                                } else {
                                    // Открыть селектор эмоций
                                    showEmotionSelector = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isVisited) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                if (isVisited) "Посещено ${visitEmotion?.emoji ?: "✅"}" else "Посетил",
                                maxLines = 1
                            )
                        }
                    }

                    if (searching) {
                        val mm = (elapsedSec / 60).toString().padStart(2, '0')
                        val ss = (elapsedSec % 60).toString().padStart(2, '0')
                        Text(
                            "⏱ $mm:$ss  ·  🚶 ${LocationUtils.formatDistance(walkedMeters)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
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
                                color = Color.White,
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
                                        text = "· $distText",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Закрыть")
                }
            }
        }

        // Селектор эмоций
        VisitEmotionSelector(
            visible = showEmotionSelector,
            onEmotionSelected = { emotion ->
                onMarkVisited(emotion)
                showEmotionSelector = false
            },
            onDismiss = {
                showEmotionSelector = false
            }
        )
    }
}