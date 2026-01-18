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

package com.example.victor_ai.ui.places

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import com.example.victor_ai.R
import com.example.victor_ai.data.location.LocationProvider
import com.example.victor_ai.data.repository.HomeWiFiRepository
import com.example.victor_ai.ui.map.MapActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 🗺️ Экран Places с нативной Android картой
 *
 * Отображает статистику мест и кнопку для открытия карты
 */
@Composable
fun PlacesMenu(
    onBack: () -> Unit,
    viewModel: PlacesViewModel
) {
    val context = LocalContext.current

    val didactGothic = FontFamily(
        Font(R.font.didact_gothic, FontWeight.Normal),
        // Android сам "надует" жирность из обычного файла
        Font(R.font.didact_gothic, FontWeight.Bold)
    )


    // Получаем LocationProvider из ViewModel
    val locationProvider = viewModel.locationProvider

    // Репозиторий домашней WiFi локации
    val homeWiFiRepository = remember { HomeWiFiRepository(context) }

    // Состояние для диалогов
    var showLocationAnomalyDialog by remember { mutableStateOf(false) }
    var showAddressInputDialog by remember { mutableStateOf(false) }
    val loading by viewModel.loading
    val error by viewModel.error
    val stats by viewModel.stats
    val lastJournalEntry by viewModel.lastJournalEntry
    val statsLoading by viewModel.statsLoading
    val lastAchievement by viewModel.lastAchievement

    // Загружаем статистику при первом открытии
    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }

    // ✅ Обновляем статистику при возврате на экран (после посещения карты!)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadStats() // Перезагружаем при возврате
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Нельзя smart-cast делегированные State'ы — используем локальные снимки.
    val achievement = lastAchievement
    val journalEntry = lastJournalEntry

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // воздух от статус-бара
        Spacer(modifier = Modifier.height(32.dp))

        TitleWithAchievement(
            title = "WeWanderMoments",
            achievementText = if (achievement != null) {
                "🏆 Последняя ачивка: ${achievement.name}"
            } else {
                "🏆 Последняя ачивка: пока нет…"
            },
            fontFamily = didactGothic,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            loading || statsLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = Color(0xFF2B2929)
                )
                Text(
                    text = "Загрузка...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFE0E0E0),
                    fontFamily = didactGothic,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            error != null -> {
                Text(
                    text = "Ошибка: $error",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                val todayDistance = stats?.todayDistance ?: 0f
                val todaySteps = stats?.todaySteps ?: 0
                val streak = stats?.streak ?: 0
                val weeklyChart = stats?.weeklyChart ?: List(7) { 0f }

                DashboardCard(
                    todayDistance = todayDistance,
                    todaySteps = todaySteps,
                    streak = streak,
                    weeklyData = weeklyChart,
                    journalText = journalEntry?.text,
                    enabled = !(loading || statsLoading),
                    fontFamily = didactGothic,
                    onOpenMap = {
                        // Проверяем историю локаций на аномальные скачки
                        val history = locationProvider?.getLocationHistory() ?: emptyList()
                        val hasAnomaly = if (history.size >= 2) {
                            // Берём последние 2 локации
                            val last = history.last()
                            val previous = history[history.lastIndex - 1]

                            // Вычисляем расстояние и время
                            val timeDiffMinutes = (last.timestamp - previous.timestamp) / 60000.0

                            // Haversine formula
                            fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
                                val earthRadiusKm = 6371.0
                                val dLat = Math.toRadians(lat2 - lat1)
                                val dLon = Math.toRadians(lon2 - lon1)
                                val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                                        kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                                        kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
                                val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
                                return earthRadiusKm * c
                            }

                            val distance = calculateDistance(previous.lat, previous.lon, last.lat, last.lon)

                            // Проверяем: больше 20 км за 10 минут?
                            distance > 20.0 && timeDiffMinutes <= 10.0
                        } else {
                            false
                        }

                        if (hasAnomaly) {
                            showLocationAnomalyDialog = true
                        } else {
                            MapActivity.start(context)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Диалог для обработки аномалии геолокации
    if (showLocationAnomalyDialog) {
        val hasHomeLocation = homeWiFiRepository.isHomeWiFiSet()

        LocationAnomalyDialog(
            onDismiss = {
                showLocationAnomalyDialog = false
            },
            onUseHomeLocation = {
                // Используем домашнюю локацию
                val homeCoords = homeWiFiRepository.getHomeCoordinates()
                if (homeCoords != null && locationProvider != null) {
                    locationProvider.setManualLocation(
                        lat = homeCoords.first,
                        lon = homeCoords.second,
                        source = "home"
                    )
                    showLocationAnomalyDialog = false
                    MapActivity.start(context)
                }
            },
            onEnterManually = {
                // Показываем диалог ввода адреса
                showLocationAnomalyDialog = false
                showAddressInputDialog = true
            },
            hasHomeLocation = hasHomeLocation
        )
    }

    // Диалог для ввода адреса вручную
    if (showAddressInputDialog) {
        AddressInputDialog(
            context = context,
            onDismiss = {
                showAddressInputDialog = false
            },
            onAddressConfirmed = { latitude, longitude, address ->
                // Сохраняем локацию из введённого адреса
                if (locationProvider != null) {
                    locationProvider.setManualLocation(
                        lat = latitude,
                        lon = longitude,
                        source = "address_manual"
                    )
                    showAddressInputDialog = false

                    android.widget.Toast.makeText(
                        context,
                        "Установлена локация: $address",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()

                    MapActivity.start(context)
                }
            }
        )
    }
}

@Composable
private fun TitleWithAchievement(
    title: String,
    achievementText: String,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFFE0E0E0),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp)) // маленький отступ, чтобы не "рассыпалось"
        Text(
            text = achievementText,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            fontFamily = fontFamily,
            color = Color(0xFFE0E0E0).copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DashboardCard(
    todayDistance: Float,
    todaySteps: Int,
    streak: Int,
    weeklyData: List<Float>,
    journalText: String?,
    enabled: Boolean,
    fontFamily: FontFamily,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Сегодня
            Text(
                text = "Сегодня",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B2929),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Пройдено: ${formatDistance(todayDistance)} / $todaySteps шагов",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = fontFamily,
                color = Color(0xFF2B2929)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "🔥 Стрик: $streak ${getDaysText(streak)} подряд",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                color = if (streak > 0) Color(0xFFFF9800) else Color(0xFF2B2929).copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF2B2929).copy(alpha = 0.12f))
            Spacer(modifier = Modifier.height(12.dp))

            // Неделя
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "📈 Неделя:",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamily,
                    fontSize = 12.sp,
                    color = Color(0xFF2B2929).copy(alpha = 0.85f)
                )
                WeekChart(weeklyData = weeklyData)
                Text(
                    text = " (${weeklyData.count { it > 0 }} / 7)",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamily,
                    fontSize = 12.sp,
                    color = Color(0xFF2B2929).copy(alpha = 0.85f)
                )
            }

            // Момент/цитата
            val moment = journalText?.take(60)
            if (!moment.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📒 \"${moment}${if ((journalText?.length ?: 0) > 60) "..." else ""}\"",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = fontFamily,
                    fontSize = 12.sp,
                    color = Color(0xFF2B2929).copy(alpha = 0.8f)
                )
            }

            // Воздух перед кнопкой + чуть больше нижнего отступа, чтобы кнопка "заземляла" карточку
            Spacer(modifier = Modifier.height(18.dp))

            // Кнопка
            OutlinedButton(
                enabled = enabled,
                onClick = onOpenMap,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                border = BorderStroke(1.dp, Color(0xFF2B2929).copy(alpha = 0.35f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFF7F7F7),
                    contentColor = Color(0xFF2B2929),
                    disabledContainerColor = Color(0xFFF7F7F7).copy(alpha = 0.65f),
                    disabledContentColor = Color(0xFF2B2929).copy(alpha = 0.45f)
                ),
                // Кнопка-акцент слева внутри карточки (как раньше)
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.Start)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Открыть уровень",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.2.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

/**
 * График активности за неделю
 * Показывает активные дни подряд с начала (без пропусков)
 */
@Composable
fun WeekChart(weeklyData: List<Float>) {
    // 🔥 Подсчитываем количество активных дней
    val activeDaysCount = weeklyData.count { it > 0 }

    // 🔥 Создаем новый массив: первые N дней заполнены, остальные пусты
    val displayData = List(7) { index ->
        if (index < activeDaysCount) 1f else 0f
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        displayData.forEach { value ->
            val symbol = if (value > 0) "🟩" else "⬛"
            Text(
                text = symbol,
                fontSize = 14.sp,
                color = Color.Unspecified
            )
        }
    }
}

/**
 * Форматирует расстояние
 */
fun formatDistance(meters: Float): String {
    return if (meters >= 1000) {
        String.format(Locale.US, "%.1f км", meters / 1000)
    } else {
        String.format(Locale.US, "%.0f м", meters)
    }
}

/**
 * Склонение слова "день"
 */
fun getDaysText(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "день"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "дня"
        else -> "дней"
    }
}

/**
 * Форматирует дату
 */
fun formatDate(dateString: String): String {
    return try {
        // Пытаемся распарсить ISO 8601 формат
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        // Если не получилось, возвращаем как есть
        dateString.take(10)
    }
}