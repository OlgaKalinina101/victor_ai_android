/**
Victor AI - Personal AI Companion for Android
Copyright (C) 2025-2026 Olga Kalinina

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.
 */

package com.example.victor_ai.ui.map

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material.icons.outlined.Timer
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.POI
import com.example.victor_ai.ui.map.models.VisitEmotion
import com.example.victor_ai.ui.map.models.VISIT_EMOTIONS
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
        val colors = MaterialTheme.colorScheme
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = colors.surfaceVariant.copy(alpha = 0.92f),
            border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.35f)),
            tonalElevation = 0.dp,
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.NearMe,
                    contentDescription = null,
                    tint = colors.primary
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Кажется, мы его нашли",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurfaceVariant
                    )
                    Text(
                        text = "Отметить '$poiName' как найденное и остановить поиск?",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant.copy(alpha = 0.85f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(contentColor = colors.onSurfaceVariant)
                    ) {
                        Text("Не сейчас", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Найдено", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun OverlayChip(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
    content: @Composable RowScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    Surface(
        modifier = modifier.then(clickableModifier),
        shape = RoundedCornerShape(999.dp),
        color = containerColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                modifier = Modifier.padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                content = content
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
    isVisited: Boolean = false,
    visitEmotion: VisitEmotion? = null,
    onToggleSearch: () -> Unit,
    onMarkVisited: (VisitEmotion?) -> Unit,
    onMarkFound: (POI) -> Unit,
    onDismiss: () -> Unit,
    onSelectNearby: (POI) -> Unit,
    modifier: Modifier = Modifier
) {
    val distance = userLocation?.let { LocationUtils.calculateDistance(it, poi.location) }
    val distanceText = distance?.let { LocationUtils.formatDistance(it) } ?: "?"
    val bearing = userLocation?.let { LocationUtils.calculateBearing(it, poi.location) } ?: 0f
    val colors = MaterialTheme.colorScheme
    val scroll = rememberScrollState()

    // Состояния для UI
    var showEmotionSelector by remember { mutableStateOf(false) }
    var showAutoVisitPrompt by remember { mutableStateOf(false) }

    // 🔥 Флаг для отслеживания, был ли показан автоматический промпт
    var autoPromptShown by remember(poi.id) { mutableStateOf(false) }

    // Автоматическое предложение посещения при расстоянии < 10м (ТОЛЬКО в режиме поиска!)
    LaunchedEffect(distance, searching, isVisited) {
        if (distance != null && distance < 10.0 && !isVisited && searching && !autoPromptShown) {
            showAutoVisitPrompt = true
            autoPromptShown = true // 🔥 Запоминаем, что показали промпт
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .widthIn(max = 420.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Автоматическое предложение посещения
        AutoVisitPrompt(
            visible = showAutoVisitPrompt,
            poiName = poi.name,
            onConfirm = {
                showAutoVisitPrompt = false
                onMarkFound(poi)
                // промпт показываем только в режиме поиска, но подстрахуемся
                if (searching) onToggleSearch()
            },
            onDismiss = {
                showAutoVisitPrompt = false
            }
        )

        // Основная панель (белая, компактная, "игровая")
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.96f),
            border = BorderStroke(2.dp, colors.outline.copy(alpha = 0.22f)),
            tonalElevation = 0.dp,
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Компактный белый subheader: ← Имя · дистанция [Искать/Стоп] ✕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color(0xFF1E1E1E)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = poi.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E1E1E),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "· $distanceText",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color(0xFF1E1E1E).copy(alpha = 0.72f),
                                maxLines = 1
                            )
                            MiniCompass(bearing = bearing)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = poi.type.name,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF1E1E1E).copy(alpha = 0.70f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            if (isVisited) {
                                OverlayChip(
                                    onClick = { showEmotionSelector = true },
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF1E1E1E),
                                    borderColor = (visitEmotion?.color ?: colors.primary).copy(alpha = 0.55f),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 5.dp)
                                ) {
                                    if (visitEmotion != null) {
                                        Text(visitEmotion.emoji, fontSize = 16.sp)
                                    } else {
                                        Icon(
                                            imageVector = Icons.Outlined.CheckCircle,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (searching) {
                        Button(
                            onClick = onToggleSearch,
                            modifier = Modifier.height(34.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.errorContainer,
                                contentColor = colors.onErrorContainer
                            )
                        ) {
                            Icon(imageVector = Icons.Outlined.StopCircle, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Стоп", maxLines = 1)
                        }
                    } else {
                        OutlinedButton(
                            onClick = onToggleSearch,
                            modifier = Modifier.height(34.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF1E1E1E)
                            )
                        ) {
                            Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Искать", maxLines = 1)
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Закрыть",
                            tint = Color(0xFF1E1E1E)
                        )
                    }
                }

                // Всё, что раньше было "под стрелочкой", теперь показываем всегда
                if (!searching) {
                    FilledTonalButton(
                        onClick = {
                            if (isVisited) onMarkVisited(null) else showEmotionSelector = true
                        }
                    ) {
                        Text(
                            if (isVisited) "Снять отметку" else "Посетили",
                            maxLines = 1
                        )
                    }
                }

                if (searching) {
                    val mm = (elapsedSec / 60).toString().padStart(2, '0')
                    val ss = (elapsedSec % 60).toString().padStart(2, '0')

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OverlayChip(
                            onClick = null,
                            containerColor = Color.White,
                            contentColor = Color(0xFF1E1E1E),
                            borderColor = colors.outline.copy(alpha = 0.22f)
                        ) {
                            Icon(Icons.Outlined.Timer, contentDescription = null)
                            Text(
                                text = "$mm:$ss",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        OverlayChip(
                            onClick = null,
                            containerColor = Color.White,
                            contentColor = Color(0xFF1E1E1E),
                            borderColor = colors.outline.copy(alpha = 0.22f)
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.DirectionsWalk, contentDescription = null)
                            Text(
                                text = LocationUtils.formatDistance(walkedMeters),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        OverlayChip(
                            onClick = null,
                            containerColor = Color.White,
                            contentColor = Color(0xFF1E1E1E),
                            borderColor = colors.outline.copy(alpha = 0.22f)
                        ) {
                            Icon(Icons.Outlined.Place, contentDescription = null)
                            Text(
                                text = distanceText,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                if (nearby.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Рядом:",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF1E1E1E).copy(alpha = 0.70f)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scroll),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            nearby.forEach { n ->
                                val dist = LocationUtils.calculateDistance(poi.location, n.location)
                                val distText = LocationUtils.formatDistance(dist)

                                OverlayChip(
                                    onClick = { onSelectNearby(n) },
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF1E1E1E),
                                    borderColor = colors.outline.copy(alpha = 0.22f)
                                ) {
                                    Text(
                                        text = n.name,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "· $distText",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF1E1E1E).copy(alpha = 0.72f)
                                    )
                                }
                            }
                        }
                    }
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