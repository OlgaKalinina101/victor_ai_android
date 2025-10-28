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
