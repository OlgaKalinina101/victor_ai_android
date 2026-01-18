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

package com.example.victor_ai.ui.map.canvas.controllers

import android.util.Log
import com.example.victor_ai.ui.map.models.MapBounds

/**
 * 🔍 Контроллер для управления режимом поиска/навигации
 */
class SearchModeController(
    private val onAnimationFrame: () -> Unit
) {
    companion object {
        private const val TAG = "SearchModeController"
        private const val ANIMATION_FPS = 20 // 20 кадров в секунду
    }

    var isSearching: Boolean = false
        private set

    var animationTime: Long = 0L
        private set

    // Сохранение состояния карты перед поиском
    private var savedZoom: Float? = null
    private var savedMapBounds: MapBounds? = null

    private val animationRunnable = object : Runnable {
        override fun run() {
            if (isSearching) {
                animationTime = System.currentTimeMillis()
                onAnimationFrame()
                // Планируем следующий кадр через 50ms (20 FPS)
                scheduleNextFrame(this)
            }
        }
    }

    // Функции для управления анимацией (должны быть установлены извне)
    var postCallback: ((Runnable) -> Unit)? = null
    var removeCallback: ((Runnable) -> Unit)? = null

    /**
     * Включает режим поиска с анимацией
     * 
     * @param currentZoom Текущий зум для сохранения
     * @param currentBounds Текущие границы для сохранения
     */
    fun startSearchMode(currentZoom: Float, currentBounds: MapBounds?) {
        Log.d(TAG, "🚀 startSearchMode() вызван")

        // Сохраняем текущее состояние карты перед поиском
        savedZoom = currentZoom
        savedMapBounds = currentBounds
        Log.d(TAG, "💾 Сохранено состояние: zoom=$savedZoom, bounds=$savedMapBounds")

        isSearching = true
        animationTime = System.currentTimeMillis()
        
        // Запускаем анимацию
        removeCallback?.invoke(animationRunnable)
        postCallback?.invoke(animationRunnable)
        
        Log.d(TAG, "✅ startSearchMode() завершен. isSearching=$isSearching")
    }

    /**
     * Выключает режим поиска
     * 
     * @return Пара (сохраненный зум, сохраненные границы) для восстановления
     */
    fun stopSearchMode(): Pair<Float?, MapBounds?> {
        Log.d(TAG, "🛑 stopSearchMode() вызван. isSearching=$isSearching")
        
        isSearching = false
        removeCallback?.invoke(animationRunnable)

        val result = Pair(savedZoom, savedMapBounds)
        
        // Очищаем сохраненное состояние
        savedZoom = null
        savedMapBounds = null

        Log.d(TAG, "✅ stopSearchMode() завершен. isSearching=$isSearching")
        return result
    }

    /**
     * Планирует следующий кадр анимации
     */
    private fun scheduleNextFrame(runnable: Runnable) {
        postCallback?.invoke(runnable)
    }

    /**
     * Очищает ресурсы контроллера
     */
    fun cleanup() {
        removeCallback?.invoke(animationRunnable)
    }
}

