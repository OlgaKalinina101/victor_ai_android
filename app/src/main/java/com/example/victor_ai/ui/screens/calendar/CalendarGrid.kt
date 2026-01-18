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

package com.example.victor_ai.ui.screens.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DateTimeException
import java.time.LocalDate

/**
 * Сетка календаря с днями месяца
 */
@Composable
fun CalendarGrid(
    year: Int,
    month: Int,
    reminders: Map<LocalDate, List<CalendarReminder>>,
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
                    color = Color(0xFFA6A6A6)
                )
            }
        }

        for (week in 0 until totalCells / 7) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (dayIndex in 0..6) {
                    val dayOfMonth = week * 7 + dayIndex - firstDayOfWeek + 1
                    
                    // 🔥 Проверяем валидность даты без игнорирования исключений
                    val date = if (dayOfMonth in 1..daysInMonth) {
                        try {
                            LocalDate.of(year, month, dayOfMonth)
                        } catch (e: DateTimeException) {
                            // Это не должно происходить, но логируем на всякий случай
                            Log.e("CalendarGrid", "Invalid date: $year-$month-$dayOfMonth", e)
                            null
                        }
                    } else {
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
                            .clickable(
                                enabled = date != null,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (date != null) onDateClick(date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null && dayOfMonth in 1..daysInMonth) {
                            Text(
                                text = "$dayOfMonth",
                                fontSize = 16.sp,
                                color = Color(0xFFA6A6A6)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Data class для напоминаний в календаре
 * (упрощённая версия для отображения)
 */
data class CalendarReminder(
    val text: String,
    val time: String
)

