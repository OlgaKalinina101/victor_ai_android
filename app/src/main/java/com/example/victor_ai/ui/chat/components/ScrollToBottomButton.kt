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

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R

/**
 * Кнопка "вернуться к последнему сообщению" (scroll to bottom)
 * 
 * Появляется когда пользователь скроллит вверх по истории
 * и исчезает когда находится внизу (у последних сообщений)
 */
@Composable
fun ScrollToBottomButton(
    visible: Boolean,
    unreadCount: Int = 0, // Количество непрочитанных (опционально)
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(8.dp, CircleShape)
                .background(Color(0xFF3A3A3C), CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            // Если есть непрочитанные - показываем бейдж
            if (unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp)
                        .size(20.dp)
                        .background(Color(0xFFBB86FC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))
                    Text(
                        text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                        fontSize = 10.sp,
                        color = Color.White,
                        fontFamily = didactGothicFont
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Вернуться к последнему сообщению",
                tint = Color(0xFFBB86FC),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

