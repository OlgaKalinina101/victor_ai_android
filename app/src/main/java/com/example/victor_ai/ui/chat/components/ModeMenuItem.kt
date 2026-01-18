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

/**
 * Элемент меню режимов
 */
@Composable
fun ModeMenuItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isSelected) "> " else "  ",
            fontSize = 14.sp,
            color = Color(0xFFE0E0E0),
            modifier = Modifier.width(20.dp),
            fontFamily = didactGothicFont
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFFE0E0E0),
            fontFamily = didactGothicFont
        )
    }
}
