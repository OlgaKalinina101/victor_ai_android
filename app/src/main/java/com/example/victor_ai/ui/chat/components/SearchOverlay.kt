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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R

/**
 * Оверлей поиска
 */
@Composable
fun SearchOverlay(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClose()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // Блокируем закрытие при клике на содержимое
                }
        ) {
            Text(
                text = "ПОИСК",
                fontSize = 20.sp,
                color = Color(0xFFE0E0E0),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                fontFamily = didactGothicFont
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF2C2C2E),
                    unfocusedContainerColor = Color(0xFF2C2C2E),
                    focusedIndicatorColor = Color(0xFFBB86FC),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFFBB86FC)
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text("Введите запрос...", color = Color.Gray, fontFamily = didactGothicFont)
                },
                textStyle = TextStyle(fontFamily = didactGothicFont)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "результаты... (в самом чате)",
                fontSize = 14.sp,
                color = Color(0xFF888888),
                fontStyle = FontStyle.Italic,
                fontFamily = didactGothicFont
            )
        }
    }
}
