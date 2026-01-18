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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.ui.playlist.AmbientThinkingRow

private val didactGothic = FontFamily(Font(R.font.didact_gothic))

/**
 * Секция выбора трека для будильника
 */
@Composable
fun TrackSection(
    onSelectTrackManually: () -> Unit,
    onSelectTrackAutomatically: () -> Unit,
    showThinking: Boolean,
    typedText: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.offset(x = (-2).dp)) {
            MarkdownButton(
                text = "Поставить самой",
                onClick = onSelectTrackManually
            )
            Spacer(modifier = Modifier.width(16.dp))
            MarkdownButton(
                text = "Разбуди меня сам...",
                onClick = onSelectTrackAutomatically
            )
        }

        // 🔥 Анимация думанья под кнопками
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-10).dp),  // поднимет глазки на 10dp вверх
            contentAlignment = Alignment.CenterEnd
        ) {
            AmbientThinkingRow(
                show = showThinking,
                typedText = typedText,
                fontFamily = didactGothic
            )
        }
    }
}

/**
 * Кнопка в стиле markdown
 */
@Composable
private fun MarkdownButton(text: String, onClick: () -> Unit) {
    Text(
        text = "[$text]",
        color = Color(0xFFA6A6A6),
        fontFamily = didactGothic,
        fontSize = 16.sp,
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(8.dp)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

