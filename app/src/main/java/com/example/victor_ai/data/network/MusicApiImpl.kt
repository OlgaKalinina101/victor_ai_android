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

package com.example.victor_ai.data.network

import com.example.victor_ai.auth.UserProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * 🎵 Обёртка для MusicApi с дополнительным streaming методом
 * Делегирует обычные методы retrofit-версии, но добавляет streaming
 */
class MusicApiImpl(
    private val retrofitApi: MusicApi,
    private val baseUrl: String,
    streamingClient: OkHttpClient? = null
) {
    // Делегируем все методы из MusicApi к retrofitApi
    private val delegate = retrofitApi

    // ═══════════════════════════════════════════════════════════
    // Делегирование методов из MusicApi
    // ═══════════════════════════════════════════════════════════

    suspend fun getTracks(
        accountId: String = UserProvider.getCurrentUserId(),
        limit: Int? = null,
        offset: Int? = null
    ): List<Track> = delegate.getTracks(accountId, limit, offset)

    suspend fun getTrackStats(
        accountId: String = UserProvider.getCurrentUserId(),
        period: String = "week"
    ): TrackStats = delegate.getTrackStats(accountId, period)

    suspend fun getPlaylistMoments(
        accountId: String = UserProvider.getCurrentUserId(),
        limit: Int = 20
    ): List<PlaylistMomentOut> = delegate.getPlaylistMoments(accountId, limit)

    suspend fun updateTrackDescription(
        accountId: String = UserProvider.getCurrentUserId(),
        update: TrackDescriptionUpdate
    ): Map<String, String> = delegate.updateTrackDescription(accountId, update)

    suspend fun runPlaylistChain(
        accountId: String = UserProvider.getCurrentUserId(),
        extraContext: String? = null
    ): Map<String, Any> = delegate.runPlaylistChain(accountId, extraContext)

    suspend fun runPlaylistWave(
        accountId: String = UserProvider.getCurrentUserId(),
        energy: String? = null,
        temperature: String? = null
    ): WaveResponse = delegate.runPlaylistWave(accountId, energy, temperature)

    // ═══════════════════════════════════════════════════════════
    // Кастомный streaming метод (не в Retrofit interface)
    // ═══════════════════════════════════════════════════════════

    // 🔥 Клиент для streaming запросов
    // 🔥 ИСПРАВЛЕНО: Добавлен Protocol.HTTP_1_1 для fallback клиента
    private val client = streamingClient ?: OkHttpClient.Builder()
        .protocols(listOf(okhttp3.Protocol.HTTP_1_1))  // 🔥 ТОЛЬКО HTTP/1.1
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)      // Без таймаута на чтение
        .writeTimeout(30, TimeUnit.SECONDS)
        .callTimeout(5, TimeUnit.MINUTES)      // Максимум 5 минут
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * 🎵 STREAMING версия: Подбирает трек на основе контекста с логами в реальном времени.
     * 
     * Возвращает streaming response где каждая строка — это JSON событие:
     * - {"log": "🎵 анализирую твоё настроение..."}
     * - {"track": {"track_id": 123, "track": "Song", "artist": "Artist"}}
     * - {"done": true}
     * - {"error": "Error message"}
     * 
     * @param accountId Идентификатор пользователя
     * @param extraContext Дополнительный контекст ("manual" или "auto")
     * @param onEvent Callback для обработки каждого события из stream
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun runPlaylistChainStreaming(
        accountId: String,
        extraContext: String?,
        onEvent: suspend (Map<String, Any>) -> Unit
    ): Unit = withContext(Dispatchers.IO) {
        // Строим URL с query параметрами
        val urlBuilder = StringBuilder("${baseUrl.trimEnd('/')}/tracks/choose_for_me/stream")
        urlBuilder.append("?account_id=$accountId")
        if (extraContext != null) {
            urlBuilder.append("&extra_context=$extraContext")
        }

        val url = urlBuilder.toString()
        android.util.Log.d("MusicApiImpl", "🎵 Starting stream request to: $url")

        val request = Request.Builder()
            .url(url)
            .post(okhttp3.RequestBody.create(null, ByteArray(0))) // Пустое тело
            .build()

        try {
            client.newCall(request).execute().use { response ->
                android.util.Log.d("MusicApiImpl", "📡 Response received: code=${response.code}, message=${response.message}")
                
                if (!response.isSuccessful) {
                    val errorMsg = "HTTP ${response.code}: ${response.message}"
                    android.util.Log.e("MusicApiImpl", "❌ $errorMsg")
                    withContext(Dispatchers.Main) {
                        onEvent(mapOf("error" to errorMsg))
                    }
                    throw Exception(errorMsg)
                }

                val contentType = response.header("Content-Type")
                android.util.Log.d("MusicApiImpl", "📄 Content-Type: $contentType")

                response.body?.use { body ->
                    android.util.Log.d("MusicApiImpl", "📦 Body received, starting to read lines...")
                    val source = body.source()
                    var lineCount = 0

                    try {
                        // Читаем построчно
                        while (!source.exhausted()) {
                            val line = source.readUtf8Line()
                            
                            if (line == null) {
                                android.util.Log.d("MusicApiImpl", "⏹️ End of stream (null line)")
                                break
                            }
                            
                            lineCount++
                            android.util.Log.d("MusicApiImpl", "📝 Line $lineCount: $line")

                            if (line.isBlank()) {
                                android.util.Log.d("MusicApiImpl", "⏭️ Skipping blank line")
                                continue
                            }

                            try {
                                // Парсим JSON строку
                                val adapter = moshi.adapter(Map::class.java)
                                val event = adapter.fromJson(line) as? Map<String, Any>

                                if (event != null) {
                                    android.util.Log.d("MusicApiImpl", "✅ Parsed event: $event")
                                    
                                    // Вызываем callback для обработки события
                                    withContext(Dispatchers.Main) {
                                        onEvent(event)
                                    }
                                } else {
                                    android.util.Log.w("MusicApiImpl", "⚠️ Event is null after parsing")
                                }
                            } catch (e: Exception) {
                                // Ошибка парсинга отдельной строки — логируем, но продолжаем
                                android.util.Log.e("MusicApiImpl", "❌ Failed to parse line: $line", e)
                            }
                        }
                        
                        android.util.Log.d("MusicApiImpl", "✅ Stream reading completed. Total lines: $lineCount")
                        
                    } catch (e: Exception) {
                        android.util.Log.e("MusicApiImpl", "❌ Error reading stream", e)
                        // Общая ошибка чтения stream
                        withContext(Dispatchers.Main) {
                            onEvent(mapOf("error" to (e.message ?: "Stream error")))
                        }
                        throw e
                    }
                } ?: run {
                    android.util.Log.e("MusicApiImpl", "❌ Response body is null!")
                    withContext(Dispatchers.Main) {
                        onEvent(mapOf("error" to "Empty response body"))
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MusicApiImpl", "❌ Exception in streaming request", e)
            withContext(Dispatchers.Main) {
                onEvent(mapOf("error" to (e.message ?: "Network error")))
            }
            throw e
        }
    }
}

