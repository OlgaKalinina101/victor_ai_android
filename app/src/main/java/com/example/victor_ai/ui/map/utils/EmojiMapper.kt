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

package com.example.victor_ai.ui.map.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import com.example.victor_ai.ui.map.models.POIType

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
     * Создает Bitmap с галочкой ✔️ для посещенных мест
     */
    fun createCheckmarkBitmap(
        context: Context,
        size: Int = EMOJI_SIZE
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            textSize = size * 0.7f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = Typeface.DEFAULT
        }

        // Рисуем зеленую галочку ✔️
        val x = size / 2f
        val y = size / 2f - (paint.descent() + paint.ascent()) / 2f
        canvas.drawText("✔️", x, y, paint)

        return bitmap
    }

    /**
     * Кэш для Bitmap эмодзи, чтобы не создавать их каждый раз
     */
    private val emojiCache = mutableMapOf<Pair<POIType, Int>, Bitmap>()
    
    /**
     * Кэш для галочки (по размеру)
     */
    private val checkmarkCache = mutableMapOf<Int, Bitmap>()

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
     * Получает закэшированный Bitmap галочки или создает новый
     */
    fun getCachedCheckmarkBitmap(
        context: Context,
        size: Int = EMOJI_SIZE
    ): Bitmap {
        return checkmarkCache.getOrPut(size) {
            createCheckmarkBitmap(context, size)
        }
    }

    /**
     * Очищает кэш эмодзи
     */
    fun clearCache() {
        emojiCache.values.forEach { it.recycle() }
        emojiCache.clear()
        checkmarkCache.values.forEach { it.recycle() }
        checkmarkCache.clear()
    }
}
