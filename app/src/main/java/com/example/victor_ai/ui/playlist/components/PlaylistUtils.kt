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

package com.example.victor_ai.ui.playlist.components

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.victor_ai.R

/**
 * Форматирует длительность из секунд в формат MM:SS
 */
fun formatDuration(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val secs = (seconds % 60).toInt()
    return "%d:%02d".format(minutes, secs)
}

/**
 * Кастомный шрифт для плейлиста
 */
val CustomFont = FontFamily(
    Font(R.font.didact_gothic, FontWeight.Normal)
)
