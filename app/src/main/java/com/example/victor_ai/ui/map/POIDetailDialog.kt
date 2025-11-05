package com.example.victor_ai.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.victor_ai.ui.places.LatLng
import com.example.victor_ai.ui.places.POI
import com.example.victor_ai.ui.map.utils.LocationUtils

/**
 * 💬 Диалог с деталями POI
 *
 * Отображает информацию о месте и позволяет:
 * - Отметить место как посещенное
 * - Добавить впечатление
 * - Просмотреть детали
 * - Показывает расстояние до POI
 */
@Composable
fun POIDetailDialog(
    poi: POI,
    userLocation: LatLng?,
    onDismiss: () -> Unit,
    onMarkAsVisited: (String) -> Unit // Callback с впечатлением
) {
    var impression by remember { mutableStateOf(poi.impression ?: "") }
    var showImpressionInput by remember { mutableStateOf(!poi.isVisited) }

    // Вычисляем расстояние до POI
    val distance = userLocation?.let {
        LocationUtils.calculateDistance(it, poi.location)
    }
    val distanceText = distance?.let {
        LocationUtils.formatDistance(it)
    } ?: "Расстояние неизвестно"

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Эмодзи и название
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

                // Расстояние до POI
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
                    }
                }

                // Статус посещения
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

                // Впечатление
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

                // Поле для ввода впечатления
                if (showImpressionInput) {
                    OutlinedTextField(
                        value = impression,
                        onValueChange = { impression = it },
                        label = { Text("Запомним что-нибудь?") },
                        placeholder = { Text("Tags") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }

                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Кнопка "Закрыть"
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Закрыть")
                    }

                    // Кнопка "Отметить как посещенное"
                    if (!poi.isVisited) {
                        Button(
                            onClick = {
                                if (impression.isNotBlank()) {
                                    onMarkAsVisited(impression)
                                    onDismiss()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = impression.isNotBlank()
                        ) {
                            Text("Посетил")
                        }
                    } else {
                        // Кнопка для редактирования впечатления
                        Button(
                            onClick = {
                                if (!showImpressionInput) {
                                    impression = poi.impression ?: ""
                                    showImpressionInput = true
                                } else if (impression.isNotBlank()) {
                                    onMarkAsVisited(impression)
                                    showImpressionInput = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !showImpressionInput || impression.isNotBlank()
                        ) {
                            Text(if (showImpressionInput) "Сохранить" else "Изменить")
                        }
                    }
                }
            }
        }
    }
}

