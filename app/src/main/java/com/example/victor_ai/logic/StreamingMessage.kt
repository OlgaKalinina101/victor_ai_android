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

package com.example.victor_ai.logic

import android.util.Base64
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.dto.GeoLocation
import com.example.victor_ai.utils.ImageUtils
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

/**
 * Отправляет сообщение ассистенту через streaming API.
 * 
 * @param streamingApi ApiService с поддержкой длительных таймаутов для SSE
 * @param sessionId ID сессии/аккаунта
 * @param text Текст сообщения
 * @param geo Геолокация (опционально)
 * @param images Прикрепленные изображения (опционально)
 * @param swipeMessageId ID сообщения для свайпа (опционально)
 * @param systemEvent Системное событие (опционально)
 * @param onChunkReceived Callback для каждого чанка текста
 * @param onMetadataReceived Callback для метаданных
 */
suspend fun processStreamingMessage(
    streamingApi: ApiService,
    sessionId: String,
    text: String,
    geo: GeoLocation? = null,
    images: List<ImageUtils.ImageAttachment> = emptyList(),
    swipeMessageId: Int? = null,
    systemEvent: String? = null,
    onChunkReceived: suspend (String) -> Unit,
    onMetadataReceived: suspend (Map<String, Any>) -> Unit = {}
): Result<String> = withContext(Dispatchers.IO) {
    try {
        // Создаем RequestBody для обязательных параметров
        val sessionIdBody = sessionId.toRequestBody("text/plain".toMediaTypeOrNull())
        val textBody = text.toRequestBody("text/plain".toMediaTypeOrNull())
        
        // Создаем RequestBody для geo (если есть)
        val geoBody = geo?.let {
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(GeoLocation::class.java)
            val geoJson = adapter.toJson(it)
            geoJson.toRequestBody("application/json".toMediaTypeOrNull())
        }
        
        // Создаем MultipartBody.Part для изображений
        val imageParts = images.mapIndexed { index, attachment ->
            val imageBytes = Base64.decode(attachment.base64, Base64.DEFAULT)
            val imageRequestBody = imageBytes.toRequestBody("image/png".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(
                "images",
                "image_$index.png",
                imageRequestBody
            )
        }.takeIf { it.isNotEmpty() }
        
        // Создаем RequestBody для systemEvent (если есть)
        val systemEventBody = systemEvent?.toRequestBody("text/plain".toMediaTypeOrNull())

        // Создаем RequestBody для swipeMessageId (если есть)
        val swipeMessageIdBody = swipeMessageId?.toString()
            ?.toRequestBody("text/plain".toMediaTypeOrNull())
        
        // 🔥 Используем streamingApi с длительными таймаутами (до 5 минут)
        val call = streamingApi.sendAssistantRequestStream(
            sessionId = sessionIdBody,
            text = textBody,
            geo = geoBody,
            images = imageParts,
            swipeMessageId = swipeMessageIdBody,
            systemEvent = systemEventBody
        )
        val response = call.execute()

        if (!response.isSuccessful) {
            return@withContext Result.failure(
                Exception("HTTP ${response.code()}: ${response.message()}")
            )
        }

        val fullResponse = StringBuilder()
        val reader = response.body()?.byteStream()?.bufferedReader()

        reader?.use { bufferedReader ->
            // 🔥 КРИТИЧНО: Проверяем isActive на каждой итерации, чтобы остановиться при отмене
            while (isActive) {
                val line = bufferedReader.readLine() ?: break
                
                try {
                    val trimmed = line.trim()

                    // SSE часто присылает служебные строки (event:, id:, пустые строки, keepalive ":")
                    if (trimmed.isEmpty()) continue
                    if (trimmed.startsWith("event:") || trimmed.startsWith("id:") || trimmed.startsWith(":")) continue

                    // ✅ Поддержка SSE формата: "data: {...}"
                    val payload = if (trimmed.startsWith("data:")) {
                        trimmed.removePrefix("data:").trim()
                    } else {
                        trimmed
                    }

                    // Иногда серверы шлют data: [DONE]
                    if (payload == "[DONE]") break

                    val json = JSONObject(payload)

                    when {
                        json.has("chunk") -> {
                            val chunk = json.getString("chunk")
                            fullResponse.append(chunk)

                            withContext(Dispatchers.Main) {
                                onChunkReceived(chunk)
                            }
                        }

                        json.has("metadata") -> {
                            android.util.Log.d("StreamingMessage", "📥 Получен metadata chunk: $payload")
                            val metadata = json.getJSONObject("metadata")
                            val map = metadata.keys().asSequence().associateWith { key ->
                                val value = metadata.get(key)
                                android.util.Log.d("StreamingMessage", "🔍 Key: $key, Value type: ${value.javaClass}, Value: $value")
                                value
                            }
                            
                            android.util.Log.d("StreamingMessage", "📦 Готовая map для callback: $map")

                            withContext(Dispatchers.Main) {
                                android.util.Log.d("StreamingMessage", "🚀 Вызов onMetadataReceived с map: $map")
                                onMetadataReceived(map)
                                android.util.Log.d("StreamingMessage", "✅ onMetadataReceived завершён")
                            }
                        }

                        json.has("done") -> {
                            break
                        }

                        json.has("error") -> {
                            val error = json.getString("error")
                            return@withContext Result.failure(Exception(error))
                        }
                    }
                } catch (e: JSONException) {
                    continue
                }
            }
        }
        
        // Если стрим был отменён, возвращаем специальное сообщение
        if (!isActive) {
            android.util.Log.d("StreamingMessage", "🛑 Стрим был отменён")
            return@withContext Result.failure(Exception("Stream cancelled"))
        }

        Result.success(fullResponse.toString())

    } catch (e: Exception) {
        Result.failure(e)
    }
}
