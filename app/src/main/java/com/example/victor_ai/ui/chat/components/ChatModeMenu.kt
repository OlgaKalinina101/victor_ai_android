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

package com.example.victor_ai.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R

/**
 * Меню выбора режима работы чата (production / edit mode)
 */
@Composable
fun ChatModeMenu(
    currentMode: String,
    onModeSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onDismiss() }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp, top = 72.dp)
                .width(200.dp)
                .background(Color(0xFF3A3A3C), RoundedCornerShape(12.dp))
                .padding(12.dp)
                .pointerInput(Unit) {
                    // Блокируем все события чтобы не закрывать меню при клике внутри
                    detectTapGestures(
                        onTap = { /* consume */ },
                        onLongPress = { /* consume */ },
                        onPress = { /* consume */ }
                    )
                }
        ) {
            Text(
                text = "mode: $currentMode",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp),
                fontFamily = didactGothicFont
            )

            ModeMenuItem(
                text = "production",
                isSelected = currentMode == "production",
                onClick = {
                    onModeSelected("production")
                }
            )

            ModeMenuItem(
                text = "edit mode",
                isSelected = currentMode == "edit mode",
                onClick = {
                    onModeSelected("edit mode")
                }
            )
        }
    }
}

