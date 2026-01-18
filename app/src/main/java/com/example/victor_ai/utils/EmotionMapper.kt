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

package com.example.victor_ai.utils

/**
 * Маппер эмоций на эмодзи
 * Преобразует название эмоции в соответствующий эмодзи
 */
object EmotionMapper {

    /**
     * Возвращает эмодзи для указанной эмоции
     * @param emotion название эмоции (например "JOY", "SADNESS")
     * @return эмодзи или "🤖" если эмоция не найдена
     */
    fun getEmoji(emotion: String?): String {
        if (emotion == null) return "🤖"

        return when (emotion.lowercase()) {
            "радость" -> "😊"
            "грусть" -> "😔"
            "злость" -> "😠"
            "страх" -> "😨"
            "удивление" -> "😮"
            "разочарование" -> "😞"
            "вдохновение" -> "🌟"
            "усталость" -> "🥱"
            "нежность" -> "💗"
            "неуверенность" -> "😟"
            "любопытство" -> "🧐"
            "растерянность" -> "😕"
            "смущение" -> "😳"
            "спокойствие" -> "🌿"
            "решимость" -> "💪"
            "восхищение" -> "🤩"
            "отчуждение" -> "🌫️"
            "облегчение" -> "😌"
            else -> "🤖"
        }
    }
}
