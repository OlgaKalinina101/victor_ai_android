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

package com.example.victor_ai.ui.map.models

import androidx.compose.ui.graphics.Color

/**
 * 💚 Модели для посещений и эмоций
 */

/**
 * Эмоция/впечатление от посещения POI
 */
data class VisitEmotion(
    val emoji: String,
    val name: String,
    val color: Color
)

/**
 * Список доступных эмоций для оценки посещений
 */
val VISIT_EMOTIONS = listOf(
    VisitEmotion("😍", "Восхитительно", Color(0xFFE91E63)),
    VisitEmotion("😊", "Понравилось", Color(0xFF4CAF50)),
    VisitEmotion("🙂", "Неплохо", Color(0xFF2196F3)),
    VisitEmotion("😐", "Обычно", Color(0xFF9E9E9E)),
    VisitEmotion("😞", "Разочарование", Color(0xFFFF9800)),
    VisitEmotion("😤", "Ужасно", Color(0xFFF44336))
)

