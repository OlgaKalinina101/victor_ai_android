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

package com.example.victor_ai.ui.screens.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.ui.screens.calendar.CalendarReminder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val didactGothic = FontFamily(Font(R.font.didact_gothic))

/**
 * Список напоминаний (TODO) для выбранной даты
 */
@Composable
fun ReminderList(
    reminders: List<CalendarReminder>,
    selectedDate: LocalDate? = null,
    modifier: Modifier = Modifier
) {
    if (reminders.isEmpty()) {
        Text(
            text = "---",
            color = Color(0xFFA6A6A6),
            fontFamily = didactGothic,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
    } else {
        val sorted = reminders.sortedBy { it.time }

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Заголовок с датой
            item {
                ReminderHeader(selectedDate)
            }
            
            // Список напоминаний
            items(sorted) { reminder ->
                ReminderItem(reminder)
            }
        }
    }
}

/**
 * Заголовок блока TODO с датой
 */
@Composable
private fun ReminderHeader(date: LocalDate?) {
    val dateText = date?.let {
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"))
        it.format(formatter)
    } ?: "..."
    
    Text(
        text = "/* TODO на $dateText: */",
        color = Color(0xFFFFD700),
        fontFamily = didactGothic,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        modifier = Modifier.padding(start = 18.dp, bottom = 8.dp, top = 6.dp)
    )
}

/**
 * Элемент списка TODO
 */
@Composable
private fun ReminderItem(reminder: CalendarReminder) {
    Text(
        text = "- ${reminder.text} (${reminder.time})",
        color = Color(0xFFA6A6A6),
        fontFamily = didactGothic,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        modifier = Modifier.padding(start = 18.dp, bottom = 4.dp)
    )
}

