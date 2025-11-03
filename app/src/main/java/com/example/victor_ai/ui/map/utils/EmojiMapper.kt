package com.example.victor_ai.ui.map.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import com.example.victor_ai.ui.places.POIType

/**
 * 🎨 Маппер для преобразования POI типов в эмодзи
 *
 * Генерирует Bitmap с эмодзи для отображения на карте
 */
object EmojiMapper {

    private const val EMOJI_SIZE = 64 // Размер эмодзи в пикселях

    /**
     * Получает эмодзи для типа POI
     */
    fun getEmoji(type: POIType): String {
        return type.emoji
    }

    /**
     * Создает Bitmap с эмодзи для отрисовки на Canvas
     *
     * @param context Android контекст
     * @param type Тип POI
     * @param size Размер эмодзи в пикселях (по умолчанию 64)
     * @return Bitmap с эмодзи
     */
    fun createEmojiBitmap(
        context: Context,
        type: POIType,
        size: Int = EMOJI_SIZE
    ): Bitmap {
        val emoji = getEmoji(type)

        // Создаем Bitmap
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Настраиваем Paint для текста
        val paint = Paint().apply {
            textSize = size * 0.7f // Эмодзи занимает 70% от размера
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.DEFAULT
        }

        // Рисуем эмодзи в центре
        val x = size / 2f
        val y = size / 2f - (paint.descent() + paint.ascent()) / 2f
        canvas.drawText(emoji, x, y, paint)

        return bitmap
    }

    /**
     * Кэш для Bitmap эмодзи, чтобы не создавать их каждый раз
     */
    private val emojiCache = mutableMapOf<Pair<POIType, Int>, Bitmap>()

    /**
     * Получает закэшированный Bitmap эмодзи или создает новый
     */
    fun getCachedEmojiBitmap(
        context: Context,
        type: POIType,
        size: Int = EMOJI_SIZE
    ): Bitmap {
        val key = Pair(type, size)
        return emojiCache.getOrPut(key) {
            createEmojiBitmap(context, type, size)
        }
    }

    /**
     * Очищает кэш эмодзи
     */
    fun clearCache() {
        emojiCache.values.forEach { it.recycle() }
        emojiCache.clear()
    }
}
