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
import com.example.victor_ai.data.network.dto.AssistantRequest
import com.example.victor_ai.data.network.dto.AssistantResponse
import com.example.victor_ai.data.network.dto.WebDemoResolveRequest
import com.example.victor_ai.data.network.dto.WebDemoResolveResponse
import com.example.victor_ai.data.network.dto.WebDemoRegisterRequest
import com.example.victor_ai.data.network.dto.WebDemoRegisterResponse
import com.example.victor_ai.data.network.dto.ChatMetaResponse
import com.example.victor_ai.data.network.dto.ChatMetaUpdateRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

/**
 * Главный API интерфейс для работы с основными функциями приложения
 * 
 * Для специализированных API см.:
 * - [ReminderApi] - напоминания
 * - [ChatApi] - история чата
 * - [PlacesApi] - места, локации, прогулки
 * - [AlarmsApi] - будильники
 * - [MemoriesApi] - память ассистента
 * - [CareBankApi] - банк заботы
 * - [AssistantStateApi] - состояние ассистента
 * - [MusicApi] - музыка и треки
 */
interface ApiService {
    
    // ========================
    // 🤖 Ассистент
    // ========================
    
    /**
     * Отправка сообщения ассистенту (без стриминга)
     * Использует JSON для запроса без изображений
     */
    @POST("assistant/message")
    suspend fun sendAssistantRequest(
        @Body request: AssistantRequest
    ): AssistantResponse

    /**
     * Отправка сообщения ассистенту со стримингом (multipart/form-data)
     * 
     * @param sessionId ID сессии
     * @param text Текст сообщения
     * @param geo JSON строка с геолокацией (опционально)
     * @param images Список изображений как файлы (опционально)
     * @param systemEvent Системное событие (опционально)
     */
    @Streaming
    @Multipart
    @POST("assistant/message/stream")
    fun sendAssistantRequestStream(
        @Part("session_id") sessionId: RequestBody,
        @Part("text") text: RequestBody,
        @Part("geo") geo: RequestBody? = null,
        @Part images: List<MultipartBody.Part>? = null,
        @Part("swipe_message_id") swipeMessageId: RequestBody? = null,
        @Part("system_event") systemEvent: RequestBody? = null
    ): Call<ResponseBody>

    // ========================
    // 💬 Мета-данные чата
    // ========================

    @GET("chat_meta/{account_id}")
    suspend fun getChatMeta(
        @Path("account_id") accountId: String
    ): Response<ChatMetaResponse>

    @PATCH("chat_meta/{account_id}")
    suspend fun updateChatMeta(
        @Path("account_id") accountId: String,
        @Body body: ChatMetaUpdateRequest
    ): Response<ChatMetaResponse>

    // ========================
    // 🔐 Auth
    // ========================

    @POST("auth/resolve")
    suspend fun resolveDemo(
        @Body body: WebDemoResolveRequest
    ): Response<WebDemoResolveResponse>

    @POST("auth/register")
    suspend fun registerDemo(
        @Body body: WebDemoRegisterRequest
    ): Response<WebDemoRegisterResponse>

    // ========================
    // 📊 Использование и статистика
    // ========================


    @GET("assistant/usage")
    suspend fun getModelUsage(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): List<ModelUsage>

    // ========================
    // 🔌 Утилиты
    // ========================

    @GET("/")
    suspend fun checkConnection(): Response<Unit>
}

