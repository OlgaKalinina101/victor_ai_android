package com.example.victor_ai.ui.places

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.victor_ai.data.network.dto.GeoLocation
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
    val places by viewModel.places
    val loading by viewModel.loading
    val error by viewModel.error
    val stats by viewModel.stats
    val lastJournalEntry by viewModel.lastJournalEntry
    val statsLoading by viewModel.statsLoading

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // === Статистика ===
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
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                error != null -> {
                    Text(
                        text = "Ошибка: $error",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    // Иконка карты
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Карта",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(bottom = 16.dp),
                        tint = Color(0xFFE0E0E0)
                    )

                    // Название
                    Text(
                        text = "WeWanderMoments",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Статистика (если есть)
                    if (stats != null) {
                        StatsDisplay(
                            stats = stats!!,
                            lastEntry = lastJournalEntry,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    } else {
                        Text(
                            text = "Пока нет прогулок 🚶",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE0E0E0).copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }

                    // Кнопка открытия карты
                    Button(
                        onClick = { MapActivity.start(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE0E0E0),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "Открыть уровень",
                            color = Color(0xFF2B2929),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Компонент для отображения статистики
 */
@Composable
fun StatsDisplay(
    stats: com.example.victor_ai.data.repository.StatsRepository.LocalStats,
    lastEntry: com.example.victor_ai.data.network.dto.JournalEntry?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Сегодня
        Text(
            text = "Пройдено сегодня: ${formatDistance(stats.todayDistance)} / ${stats.todaySteps} шагов",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            color = Color(0xFFE0E0E0),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Стрик
        Text(
            text = "🔥 Стрик: ${stats.streak} ${getDaysText(stats.streak)} подряд",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (stats.streak > 0) Color(0xFFFF9800) else Color(0xFFE0E0E0).copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Достижения
        if (stats.achievements.isNotEmpty()) {
            Text(
                text = "🏆 Достижения: ${stats.achievements.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color(0xFFE0E0E0),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // График недели
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "📈 Неделя: ",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color(0xFFE0E0E0)
            )
            WeekChart(weeklyData = stats.weeklyChart)
            Text(
                text = " (${stats.weeklyChart.count { it > 0 }} из 7)",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = Color(0xFFE0E0E0)
            )
        }

        // Последняя запись из дневника
        if (lastEntry != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "📔: \"${lastEntry.text.take(40)}${if (lastEntry.text.length > 40) "..." else ""}\"",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                color = Color(0xFFE0E0E0).copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
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
            val symbol = if (value > 0) "▓" else "░"
            Text(
                text = symbol,
                fontSize = 14.sp,
                color = if (value > 0) Color(0xFF4CAF50) else Color(0xFFE0E0E0).copy(alpha = 0.3f)
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
