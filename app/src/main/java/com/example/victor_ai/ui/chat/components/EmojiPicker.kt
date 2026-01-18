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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * Список доступных эмодзи для реакций
 */
val AVAILABLE_EMOJIS = listOf(
    "🌸", "🙈", "❤️", "😂", "😍", "🥰", "😁", "🫠", "🤗", "🤔",
    "😏", "💔", "💯", "🫶", "🧐", "🫂", "😱", "😥", "🥹", "😎",
    "🥴", "😮‍💨", "😔", "😵‍💫", "🤯", "🤧", "😡", "😤", "😳", "😌",
    "😔", "👌", "🙌", "🤝"
)

/**
 * Диалог выбора эмодзи
 * 
 * @param currentEmoji Текущее выбранное эмодзи (если есть)
 * @param onEmojiSelected Callback при выборе эмодзи
 * @param onDismiss Callback при закрытии диалога
 */
@Composable
fun EmojiPickerDialog(
    currentEmoji: String?,
    onEmojiSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .background(Color(0xFF2C2C2E), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Выберите реакцию",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(AVAILABLE_EMOJIS) { emoji ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                if (emoji == currentEmoji) Color(0xFFBB86FC).copy(alpha = 0.3f)
                                else Color(0xFF3A3A3C),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                // Если выбран тот же эмодзи - снимаем его
                                if (emoji == currentEmoji) {
                                    onEmojiSelected(null)
                                } else {
                                    onEmojiSelected(emoji)
                                }
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 24.sp
                        )
                    }
                }
            }

            // Кнопка "Убрать реакцию" если эмодзи выбрано
            if (currentEmoji != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF3A3A3C), RoundedCornerShape(8.dp))
                        .clickable {
                            onEmojiSelected(null)
                            onDismiss()
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Убрать реакцию",
                        fontSize = 14.sp,
                        color = Color(0xFFBB86FC)
                    )
                }
            }
        }
    }
}

