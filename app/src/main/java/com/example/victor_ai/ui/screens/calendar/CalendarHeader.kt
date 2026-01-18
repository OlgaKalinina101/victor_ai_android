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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// 🔥 Оптимизация: создаем один раз на уровне модуля
private val didactGothic = FontFamily(Font(R.font.didact_gothic))
private val russianLocale = Locale("ru")
private val monthYearFormatter = DateTimeFormatter
    .ofPattern("LLLL yyyy", russianLocale)
    .withLocale(russianLocale)

/**
 * Заголовок календаря с месяцем и стрелками навигации
 */
@Composable
fun CalendarHeader(
    currentMonth: LocalDate,
    onMonthChange: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Предыдущий месяц",
                tint = Color(0xFFA6A6A6)
            )
        }

        Text(
            text = currentMonth.format(monthYearFormatter)
                .replaceFirstChar { it.uppercase() }, // Ноябрь 2025 вместо ноябрь 2025
            color = Color(0xFFA6A6A6),
            fontSize = 20.sp,
            fontFamily = didactGothic,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Следующий месяц",
                tint = Color(0xFFA6A6A6)
            )
        }
    }
}

