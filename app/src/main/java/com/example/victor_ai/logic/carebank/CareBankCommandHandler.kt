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

package com.example.victor_ai.logic.carebank

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.victor_ai.data.repository.CareBankRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Обработчик команд банка заботы
 * Перехватывает команды вида /запрос и открывает WebView с поиском
 */
@Singleton
class CareBankCommandHandler @Inject constructor(
    private val careBankRepository: CareBankRepository
) {
    companion object {
        private const val TAG = "CareBankCommandHandler"
        private const val COMMAND_PREFIX = "/"
        private const val DEFAULT_EMOJI = "☕" // Пока работаем с одним эмодзи
    }

    /**
     * Проверяет, является ли сообщение командой банка заботы
     */
    fun isCareBankCommand(message: String): Boolean {
        return message.trim().startsWith(COMMAND_PREFIX) && message.trim().length > 1
    }

    /**
     * Обрабатывает команду банка заботы
     * @param message - команда вида "/блинчики"
     * @param context - контекст для Toast сообщений
     * @return URL для открытия в WebView или null если ошибка
     */
    suspend fun handleCommand(message: String, context: Context): String? {
        Log.d(TAG, "🔵 handleCommand вызван с message='$message'")
        
        if (!isCareBankCommand(message)) {
            Log.d(TAG, "⚠️ Сообщение не является командой банка заботы")
            return null
        }

        return withContext(Dispatchers.IO) {
            try {
                // Извлекаем запрос (убираем "/" в начале)
                val query = message.trim().substring(1)
                Log.d(TAG, "🔍 Извлечен запрос: query='$query'")

                // Получаем value из репозитория (URL базового сайта)
                Log.d(TAG, "📡 Получаем запись из репозитория для эмодзи: $DEFAULT_EMOJI")
                val entry = careBankRepository.getEntryByEmoji(DEFAULT_EMOJI)
                
                if (entry == null) {
                    Log.e(TAG, "❌ Запись для эмодзи $DEFAULT_EMOJI не найдена в репозитории!")
                    Log.e(TAG, "💡 Убедитесь, что вы сохранили URL в Банке заботы через шторку браузера")
                    
                    // Показываем Toast пользователю
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Сначала сохраните URL в Банке заботы (настройки браузера)",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    
                    return@withContext null
                }

                val baseUrl = entry.value
                Log.d(TAG, "📍 Base URL получен: $baseUrl")

                // Формируем полный URL для поиска
                val searchUrl = buildSearchUrl(baseUrl, query)
                Log.d(TAG, "🌐 Сформирован Search URL: $searchUrl")
                Log.d(TAG, "✅ Команда обработана успешно, возвращаем URL")
                
                searchUrl
            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка обработки команды: ${e.javaClass.simpleName}: ${e.message}", e)
                null
            }
        }
    }

    /**
     * Формирует URL для поиска
     */
    private fun buildSearchUrl(baseUrl: String, query: String): String {
        // Убираем trailing slash из baseUrl если есть
        val cleanBaseUrl = baseUrl.trimEnd('/')
        
        // Формируем URL с параметром поиска
        return "$cleanBaseUrl/search"
    }

}

