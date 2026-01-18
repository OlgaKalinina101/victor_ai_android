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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R

/**
 * Header чата с меню, заголовком и поиском
 * Может переключаться между обычным видом и режимом поиска
 */
@Composable
fun ChatHeader(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onResetClick: () -> Unit,
    currentMode: String,
    isSearchMode: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onNextSearchResult: () -> Unit = {}
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF2B2929))
            .padding(horizontal = 12.dp)
            .pointerInput(Unit) {
                // Блокируем жесты ChatBox на области header
                detectTapGestures(
                    onTap = { /* consume */ },
                    onLongPress = { /* consume */ },
                    onPress = { /* consume */ }
                )
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // [☰] Меню
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Меню",
                    tint = Color(0xFFE0E0E0)
                )
            }

            // Центральная часть: либо "Victor AI", либо поле поиска
            if (isSearchMode) {
                // Режим поиска - показываем TextField
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF3A3A3C),
                        unfocusedContainerColor = Color(0xFF3A3A3C),
                        focusedIndicatorColor = Color(0xFFBB86FC),
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color(0xFFBB86FC)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = {
                        Text(
                            "Поиск...",
                            color = Color.Gray,
                            fontFamily = didactGothicFont,
                            fontSize = 14.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = didactGothicFont,
                        fontSize = 14.sp
                    ),
                    singleLine = true
                )
            } else {
                // Обычный режим - показываем "Victor AI"
                Text(
                    text = "Victor AI",
                    fontSize = 18.sp,
                    color = Color(0xFFE0E0E0),
                    fontWeight = FontWeight.Medium,
                    fontFamily = didactGothicFont
                )
            }

            // [🔍] Поиск / [⬆] Следующий результат
            IconButton(
                onClick = {
                    if (isSearchMode && searchQuery.isNotBlank()) {
                        // Если поиск активен и есть текст - показываем следующий результат
                        onNextSearchResult()
                    } else {
                        // Иначе - тогглим режим поиска
                        onSearchClick()
                    }
                }
            ) {
                Icon(
                    imageVector = if (isSearchMode && searchQuery.isNotBlank()) {
                        Icons.Default.KeyboardArrowUp  // Стрелка вверх при активном поиске
                    } else {
                        Icons.Default.Search  // Лупа в обычном режиме
                    },
                    contentDescription = when {
                        isSearchMode && searchQuery.isNotBlank() -> "Следующий результат"
                        isSearchMode -> "Закрыть поиск"
                        else -> "Поиск"
                    },
                    tint = when {
                        isSearchMode && searchQuery.isNotBlank() -> Color.Gray  // Серая стрелка
                        isSearchMode -> Color(0xFFBB86FC)  // Фиолетовая лупа
                        else -> Color(0xFFE0E0E0)  // Обычная лупа
                    }
                )
            }

            // [✕] Сбросить чат и вернуть к концу переписки (только в режиме поиска)
            if (isSearchMode) {
                IconButton(
                    onClick = onResetClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Сбросить чат",
                        tint = Color(0xFFE0E0E0)
                    )
                }
            }
        }
    }
}
