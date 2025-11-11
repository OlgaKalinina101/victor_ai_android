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

        return when (emotion.uppercase()) {
            "JOY" -> "😊"
            "SADNESS" -> "😔"
            "ANGER" -> "😠"
            "FEAR" -> "😨"
            "SURPRISE" -> "😮"
            "DISAPPOINTMENT" -> "😞"
            "INSPIRATION" -> "🌟"
            "FATIGUE" -> "🥱"
            "TENDERNESS" -> "💗"
            "INSECURITY" -> "😟"
            "CURIOSITY" -> "🧐"
            "CONFUSION" -> "😕"
            "EMBARRASSMENT" -> "😳"
            "SERENITY" -> "🌿"
            "DETERMINATION" -> "💪"
            "ADMIRATION" -> "🤩"
            "ALIENATION" -> "🌫️"
            "RELIEF" -> "😌"
            else -> "🤖"
        }
    }
}
