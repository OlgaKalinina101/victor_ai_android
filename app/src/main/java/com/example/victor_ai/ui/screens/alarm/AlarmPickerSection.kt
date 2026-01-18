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

package com.example.victor_ai.ui.screens.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.data.repository.AlarmItem
import com.example.victor_ai.ui.screens.calendar.AlarmUiState

private val didactGothic = FontFamily(Font(R.font.didact_gothic))

/**
 * Секция с 3 будильниками - оформлена как Composable функция (пасхалка для своих 🤣)
 */
@Composable
fun AlarmPickerSection(
    alarms: List<AlarmItem>,
    uiStates: List<AlarmUiState>,
    onTimeClick: (Int) -> Unit,
    onRepeatClick: (Int) -> Unit,
    onTimeSelected: (Int, String) -> Unit,
    onRepeatSelected: (Int, String) -> Unit,
    onTimeDismiss: (Int) -> Unit,
    onRepeatDismiss: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFFCC7832))) { append("@Composable") }
                withStyle(SpanStyle(color = Color.White)) { append("\n") }
                withStyle(SpanStyle(color = Color(0xFFCC7832))) { append("fun ") }
                withStyle(SpanStyle(color = Color(0xFF56A8F5))) { append("WakeupSchedule") }
                withStyle(SpanStyle(color = Color.White)) { append("()") }
            },
            fontFamily = didactGothic,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(start = 18.dp, bottom = 0.dp)
        )

        // Блок будильников
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp), // ← именно здесь отступ 40.dp
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            val alarmNames = listOf("onetimeAlarm", "weekdaysAlarm", "weekendAlarm")

            repeat(3) { index ->
                AlarmPickerRow(
                    alarmIndex = index,
                    alarmName = alarmNames[index],
                    alarm = alarms.getOrNull(index) ?: AlarmItem("Null", "Один раз"),
                    uiState = uiStates.getOrNull(index) ?: AlarmUiState(),
                    onTimeClick = { onTimeClick(index) },
                    onRepeatClick = { onRepeatClick(index) },
                    onTimeSelected = { time -> onTimeSelected(index, time) },
                    onRepeatSelected = { mode -> onRepeatSelected(index, mode) },
                    onTimeDismiss = { onTimeDismiss(index) },
                    onRepeatDismiss = { onRepeatDismiss(index) }
                )
            }
        }
    }
}

/**
 * Одна строка с будильником в стиле: val onetimeAlarm = "08:00"
 */
@Composable
private fun AlarmPickerRow(
    alarmIndex: Int,
    alarmName: String,
    alarm: AlarmItem,
    uiState: AlarmUiState,
    onTimeClick: () -> Unit,
    onRepeatClick: () -> Unit,
    onTimeSelected: (String) -> Unit,
    onRepeatSelected: (String) -> Unit,
    onTimeDismiss: () -> Unit,
    onRepeatDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Текст: val alarmName = (с подсветкой синтаксиса Kotlin)
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFFCC7832))) { append("val ") }
                withStyle(SpanStyle(color = Color.White)) { append("$alarmName = ") }
            },
            fontFamily = didactGothic,
            fontSize = 16.sp
        )

        // Время (кликабельное)
        TimeDropdownButton(
            time = alarm.time,
            expanded = uiState.timeExpanded,
            onClick = onTimeClick,
            onTimeSelected = onTimeSelected,
            onDismiss = onTimeDismiss
        )

        Spacer(Modifier.width(8.dp))

        // Если Null - показываем комментарий, иначе - выпадающий список
        if (alarm.time == "Null") {
            Text(
                text = "// не баг",
                color = Color(0xFF808080),
                fontFamily = didactGothic,
                fontSize = 16.sp
            )
        } else {
            // Режим повторения (кликабельный)
            RepeatDropdownButton(
                repeatMode = alarm.repeatMode,
                expanded = uiState.repeatExpanded,
                onClick = onRepeatClick,
                onModeSelected = onRepeatSelected,
                onDismiss = onRepeatDismiss
            )
        }
    }
}

/**
 * Кнопка выбора времени с выпадающим списком
 */
@Composable
private fun TimeDropdownButton(
    time: String,
    expanded: Boolean,
    onClick: () -> Unit,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Box {
        // Отображаем время в кавычках: "08:00" (зеленым) или null (оранжевым)
        val displayText = if (time == "Null") "null" else "\"$time\""
        val textColor = if (time == "Null") Color(0xFFCC7832) else Color(0xFF6A8759)
        
        Text(
            text = displayText,
            color = textColor,
            fontFamily = didactGothic,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
                .padding(vertical = 2.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .background(Color(0xFF333333))
                .border(1.dp, Color(0xFF666666))
                .heightIn(max = 200.dp)
        ) {
            val times = listOf("Null") + generateTimes(startHour = 6, endHour = 23)

            times.forEach { timeOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = timeOption,
                            color = Color(0xFFA6A6A6),
                            fontFamily = didactGothic,
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        onTimeSelected(timeOption)
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(0xFFA6A6A6)
                    )
                )
            }
        }
    }
}

/**
 * Кнопка выбора режима повторения с выпадающим списком
 */
@Composable
private fun RepeatDropdownButton(
    repeatMode: String,
    expanded: Boolean,
    onClick: () -> Unit,
    onModeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Box {
        // Отображаем режим как комментарий: // Будни (серым)
        Text(
            text = "// $repeatMode",
            color = Color(0xFF808080),
            fontFamily = didactGothic,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClick() }
                .padding(vertical = 2.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = Modifier
                .background(Color(0xFF333333))
                .border(1.dp, Color(0xFF666666))
                .heightIn(max = 200.dp)
        ) {
            val modes = listOf("Null", "Один раз", "Будни", "Выходные", "Всегда")

            modes.forEach { mode ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = mode,
                            color = Color(0xFFA6A6A6),
                            fontFamily = didactGothic,
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        onModeSelected(mode)
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(0xFFA6A6A6)
                    )
                )
            }
        }
    }
}

/**
 * Генерирует список времени
 */
private fun generateTimes(
    startHour: Int = 0,
    endHour: Int = 23,
    stepMinutes: Int = 60
): List<String> {
    val times = mutableListOf<String>()
    var currentMinutes = startHour * 60

    while (currentMinutes <= endHour * 60) {
        val hour = currentMinutes / 60
        val minute = currentMinutes % 60
        times.add(String.format("%02d:%02d", hour, minute))
        currentMinutes += stepMinutes
    }
    return times
}

