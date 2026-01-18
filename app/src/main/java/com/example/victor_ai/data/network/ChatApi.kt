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
import com.example.victor_ai.domain.model.ChatMessage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Query

// ========================
// DTOs
// ========================

@JsonClass(generateAdapter = true)
data class ChatHistoryResponse(
    val messages: List<ChatMessage>,
    @Json(name = "has_more") val hasMore: Boolean,
    @Json(name = "oldest_id") val oldestId: Int?,
    @Json(name = "newest_id") val newestId: Int?
)

@JsonClass(generateAdapter = true)
data class SearchResult(
    val messages: List<ChatMessage>,
    @Json(name = "matched_message_id") val matchedMessageId: Int?,
    @Json(name = "total_matches") val totalMatches: Int,
    @Json(name = "current_match_index") val currentMatchIndex: Int,
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "has_prev") val hasPrev: Boolean
)

data class UpdateHistoryResponse(
    val success: Boolean,
    val message: String
)

@JsonClass(generateAdapter = true)
data class UpdateHistoryRequest(
    val messages: List<ChatMessage>,
    @Json(name = "edited_message_id") val editedMessageId: Int? = null,
    @Json(name = "edited_message_text") val editedMessageText: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdateEmojiRequest(
    @Json(name = "account_id") val accountId: String,
    @Json(name = "backend_id") val backendId: Int,
    val emoji: String?
)

@JsonClass(generateAdapter = true)
data class UpdateEmojiResponse(
    val success: Boolean,
    val message: String,
    @Json(name = "message_id") val messageId: Int,
    val emoji: String?
)

// ========================
// API Interface
// ========================

/**
 * API для работы с историей чата
 */
interface ChatApi {
    /**
     * Получает историю диалога с поддержкой пагинации из БД на бэкенде.
     *
     * Алгоритм загрузки истории:
     * 1. При первом запросе (beforeId=null):
     *    - Загружает последние N сообщений из базы данных
     *    - Возвращает их в порядке от новых к старым
     *
     * 2. При последующих запросах (с beforeId):
     *    - Загружает сообщения старше указанного ID
     *    - Поддерживает бесконечный скролл вверх
     *
     * Каждое сообщение содержит:
     * - text, is_user, timestamp, id
     * - image_count (количество прикрепленных изображений)
     * - vision_context (контекст изображения, если было отправлено)
     * - emoji (эмодзи-реакция пользователя, если установлена)
     *
     * @param accountId Идентификатор пользователя. По умолчанию "test_user".
     * @param limit Количество сообщений для загрузки за один запрос.
     *              Минимум 1, максимум 100. По умолчанию 25.
     * @param beforeId ID сообщения, до которого загружать историю.
     *                 Используется для пагинации при скролле вверх.
     *                 Если null - загружаются последние сообщения.
     * @return [ChatHistoryResponse] с историей сообщений и мета-информацией.
     * @throws BadRequestException Если параметры запроса невалидны.
     * @throws ServerErrorException При ошибках сервера.
     */
    @GET("chat/get_history")
    suspend fun getChatHistory(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("limit") limit: Int = 25,
        @Query("before_id") beforeId: Int? = null
    ): ChatHistoryResponse

    /**
     * Обновляет отредактированное сообщение в истории чата.
     *
     * Используется для редактирования сообщений (user или assistant).
     * При вызове отправляется:
     * - ID отредактированного сообщения (edited_message_id)
     * - Новый текст отредактированного сообщения (edited_message_text)
     * - Последние 3 пары сообщений (6 сообщений), от старых к новым
     *
     * Сообщения могут быть как синхронизированные (с ID из БД),
     * так и несинхронизированные (временные).
     *
     * @param request Объект [UpdateHistoryRequest] содержащий:
     *                - messages: последние 3 пары сообщений
     *                - editedMessageId: ID сообщения которое было отредактировано
     *                - editedMessageText: новый текст сообщения
     * @param accountId Идентификатор пользователя. По умолчанию "test_user".
     * @return [UpdateHistoryResponse] с результатом операции.
     * @throws BadRequestException Если запрос содержит невалидные данные.
     * @throws ServerErrorException При ошибках записи.
     */
    @PUT("chat/update_history")
    @Headers("Content-Type: application/json")
    suspend fun updateChatHistory(
        @Body request: UpdateHistoryRequest,
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): UpdateHistoryResponse

    /**
     * Поиск сообщений в истории диалога с возвращением контекста вокруг найденных совпадений.
     *
     * Реализует полнотекстовый поиск по истории сообщений пользователя с поддержкой:
     * - Пагинации по результатам поиска
     * - Загрузки контекста вокруг найденного сообщения
     * - Навигации "вперед/назад" по результатам
     *
     * @param accountId Идентификатор пользователя. По умолчанию "test_user".
     * @param query Поисковый запрос (минимум 1 символ).
     * @param offset Смещение по результатам поиска.
     *               0 = самый новый результат, 1 = следующий по старшинству.
     * @param contextBefore Сколько сообщений загрузить ДО найденного сообщения.
     *                      От 0 до 50, по умолчанию 10.
     * @param contextAfter Сколько сообщений загрузить ПОСЛЕ найденного сообщения.
     *                     От 0 до 50, по умолчанию 10.
     * @return [SearchResult] с контекстом найденного сообщения и мета-информацией.
     * @throws BadRequestException Если query пустой или параметры вне диапазона.
     * @throws ServerErrorException При ошибках поиска.
     */
    @GET("chat/history/search")
    suspend fun searchChatHistory(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("query") query: String,
        @Query("offset") offset: Int = 0,
        @Query("context_before") contextBefore: Int = 10,
        @Query("context_after") contextAfter: Int = 10
    ): SearchResult

    /**
     * Обновляет эмодзи-реакцию на сообщение ассистента.
     *
     * @param request Объект [UpdateEmojiRequest] содержащий account_id, backend_id и emoji.
     * @return [UpdateEmojiResponse] с результатом операции.
     * @throws BadRequestException Если запрос содержит невалидные данные.
     * @throws ServerErrorException При ошибках обновления.
     */
    @retrofit2.http.PATCH("chat/update_emoji")
    @Headers("Content-Type: application/json")
    suspend fun updateEmoji(
        @Body request: UpdateEmojiRequest
    ): UpdateEmojiResponse
}

