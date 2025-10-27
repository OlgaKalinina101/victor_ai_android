package com.example.victor_ai.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.data.network.Reminder
import com.example.victor_ai.data.network.ReminderDto
import com.example.victor_ai.data.network.groupReminders
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun CalendarScreenWithReminders(api: suspend () -> Map<String, List<ReminderDto>>) {
    var reminders by remember { mutableStateOf<Map<LocalDate, List<Reminder>>>(emptyMap()) }
    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val apiResponse = api()
            Log.d("CalendarAPI", "✅ Response received: ${apiResponse}") // ← добавим лог

            reminders = groupReminders(apiResponse)
            isLoaded = true
        } catch (e: Exception) {
            Log.e("CalendarAPI", "❌ Error fetching reminders", e)
        }
    }

    if (isLoaded) {
        CalendarScreen(reminders = reminders)
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CalendarScreen(reminders: Map<LocalDate, List<Reminder>>) {
    var currentDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Заголовок: месяц и год
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentDate = currentDate.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Предыдущий месяц", tint = Color.Gray)
            }
            Text(
                text = "${currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentDate.year}",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFCCCCCC) // ← серый текст
            )
            IconButton(onClick = { currentDate = currentDate.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Следующий месяц", tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Сам календарь
        CalendarGrid(
            year = currentDate.year,
            month = currentDate.monthValue,
            reminders = reminders,
            onDateClick = { date ->
                selectedDate = date
                showDialog = true
            }
        )
    }

    // Показываем диалог, если дата выбрана
    if (showDialog && selectedDate != null) {
        val remindersForDate = reminders[selectedDate].orEmpty()

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Напоминалки на ${selectedDate}") },
            text = {
                if (remindersForDate.isEmpty()) {
                    Text("Нет напоминалок")
                } else {
                    Column {
                        remindersForDate.sortedBy { it.time }.forEach {
                            val timeText = it.time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "??:??"
                            Text("⏰ $timeText — ${it.text}")
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }
}



@Composable
fun CalendarGrid(
    year: Int,
    month: Int,
    reminders: Map<LocalDate, List<Reminder>>,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = firstDayOfMonth.lengthOfMonth()
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7 // make Monday = 0

    val totalCells = ((firstDayOfWeek + daysInMonth + 6) / 7) * 7

    Column {
        // Header row
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС").forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    textAlign = TextAlign.Center,
                    color = Color(0xFFCCCCCC)
                )
            }
        }

        for (week in 0 until totalCells / 7) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (dayIndex in 0..6) {
                    val dayOfMonth = week * 7 + dayIndex - firstDayOfWeek + 1
                    val date = try {
                        LocalDate.of(year, month, dayOfMonth)
                    } catch (_: Exception) {
                        null
                    }

                    val hasReminder = date != null && reminders.containsKey(date)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .background(
                                if (hasReminder) Color(0xFFE0E0E0) else Color(0xFF333333),
                                shape = RectangleShape
                            )
                            .border(1.dp, Color(0xFF666666))
                            .clickable(enabled = date != null) {
                                if (date != null) onDateClick(date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null && dayOfMonth in 1..daysInMonth) {
                            Text(
                                text = "$dayOfMonth",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


