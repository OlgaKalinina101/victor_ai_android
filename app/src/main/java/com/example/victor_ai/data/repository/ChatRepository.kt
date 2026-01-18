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

package com.example.victor_ai.data.repository

import android.util.Log
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.local.dao.ChatMessageDao
import com.example.victor_ai.data.local.entity.ChatMessageEntity
import com.example.victor_ai.data.network.ChatApi
import com.example.victor_ai.data.network.ChatHistoryResponse
import com.example.victor_ai.data.network.SearchResult
import com.example.victor_ai.data.network.UpdateHistoryRequest
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
    internal val chatApi: ChatApi  // internal для доступа из ChatViewModel
) {
    companion object {
        private const val TAG = "ChatRepository"
        private const val SYNC_PAGE_LIMIT = 10
        private const val MAX_SYNC_PAGES = 50
        private const val SEARCH_CONTEXT_BEFORE = 5
        private const val SEARCH_CONTEXT_AFTER = 5
    }
    
    // Защита от параллельных запросов к API
    private val apiMutex = Mutex()

    /**
     * Локальный источник истины - все UI читает отсюда через Flow.
     * Room автоматически уведомляет подписчиков при изменениях.
     */
    fun getChatHistory(): Flow<List<ChatMessageEntity>> {
        return chatMessageDao.getAllMessages()
    }

    /**
     * Получить все сообщения один раз (без Flow).
     * Используется для операций где не нужна реактивность.
     */
    suspend fun getChatHistoryOnce(): List<ChatMessageEntity> {
        return chatMessageDao.getAllMessagesOnce()
    }

    /**
     * Синхронизация с бэкендом - загружает историю и сохраняет в Room.
     * Возвращает ChatHistoryResponse для информации о пагинации.
     * 
     * @param accountId ID аккаунта пользователя
     * @return Result с ChatHistoryResponse (содержит hasMore, oldestId для пагинации)
     */
    suspend fun syncWithBackend(accountId: String = UserProvider.getCurrentUserId()): Result<ChatHistoryResponse> {
        Log.d(TAG, "🔄 syncWithBackend для $accountId")

        return apiMutex.withLock {
            try {
                Log.d(TAG, "🔒 Mutex получен, старт синка для $accountId")
                val startTime = System.currentTimeMillis()

                var nextBeforeId: Int? = null
                var page = 1
                var totalInserted = 0
                var lastResponse: ChatHistoryResponse? = null
                var isFirstPage = true

                while (page <= MAX_SYNC_PAGES) {
                    Log.d(TAG, "📦 Page $page: get_history limit=$SYNC_PAGE_LIMIT beforeId=$nextBeforeId")
                    val response = chatApi.getChatHistory(
                        accountId = accountId,
                        limit = SYNC_PAGE_LIMIT,
                        beforeId = nextBeforeId
                    )
                    lastResponse = response

                    Log.d(
                        TAG,
                        "📡 Page $page: messages=${response.messages.size}, has_more=${response.hasMore}, oldest_id=${response.oldestId}"
                    )

                    val entities = response.messages.map { it.toEntity() }
                    if (isFirstPage) {
                        chatMessageDao.clearAll()
                        isFirstPage = false
                    }
                    if (entities.isNotEmpty()) {
                        chatMessageDao.insertMessages(entities)
                        totalInserted += entities.size
                        Log.d(TAG, "💾 Page $page inserted=${entities.size} total=$totalInserted")
                    }

                    if (!response.hasMore || response.oldestId == null) {
                        break
                    }

                    nextBeforeId = response.oldestId
                    page += 1
                }

                val totalTime = System.currentTimeMillis() - startTime
                Log.d(TAG, "✅ Синхронизация завершена за ${totalTime}ms: total=$totalInserted pages=$page")

                if (lastResponse != null) {
                    Result.success(lastResponse)
                } else {
                    Result.failure(IllegalStateException("Empty response from chat history"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка синхронизации", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Загрузка дополнительной истории с пагинацией (для скролла вверх).
     * Добавляет загруженные сообщения в Room (append, не replace).
     * UI автоматически обновится через Flow.
     * 
     * @param beforeId ID сообщения, до которого загружать историю
     * @param limit Количество сообщений для загрузки
     * @param accountId ID аккаунта пользователя
     * @return Result с ChatHistoryResponse (содержит hasMore, oldestId для следующей пагинации)
     */
    suspend fun loadMoreHistory(
        beforeId: Int,
        limit: Int = 25,
        accountId: String = UserProvider.getCurrentUserId()
    ): Result<ChatHistoryResponse> {
        return try {
            Log.d(TAG, "📥 Загрузка истории: beforeId=$beforeId, limit=$limit")
            val response = chatApi.getChatHistory(accountId, limit, beforeId)

            Log.d(TAG, "✅ Загружено ${response.messages.size} сообщений, has_more=${response.hasMore}")

            // Конвертируем и добавляем в Room (append)
            if (response.messages.isNotEmpty()) {
                val entities = response.messages.map { it.toEntity() }
                chatMessageDao.insertMessages(entities)
                Log.d(TAG, "💾 Добавлено ${entities.size} сообщений в Room")
            }

            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка загрузки истории", e)
            Result.failure(e)
        }
    }

    // Добавить новое сообщение локально
    suspend fun addMessage(message: ChatMessageEntity) {
        chatMessageDao.insertMessage(message)
    }

    // Добавить список сообщений локально
    suspend fun addMessages(messages: List<ChatMessageEntity>) {
        chatMessageDao.insertMessages(messages)
    }

    // Обновить историю на бэкенде
    suspend fun updateBackendHistory(
        messages: List<ChatMessage>,
        editedMessageId: Int?,
        editedMessageText: String?,
        accountId: String = UserProvider.getCurrentUserId()
    ): Result<Unit> {
        return try {
            Log.d(TAG, "Отправка истории на бэкенд...")
            Log.d(TAG, "  - Сообщений: ${messages.size}")
            Log.d(TAG, "  - ID редактируемого: $editedMessageId")
            Log.d(TAG, "  - Новый текст: ${editedMessageText?.take(50)}")

            chatApi.updateChatHistory(
                request = UpdateHistoryRequest(
                    messages = messages,
                    editedMessageId = editedMessageId,
                    editedMessageText = editedMessageText
                ),
                accountId = accountId
            )

            Log.d(TAG, "✅ История отправлена на бэкенд")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка отправки истории", e)
            Result.failure(e)
        }
    }

    // Поиск по истории чата
    suspend fun searchHistory(
        query: String,
        offset: Int = 0,
        contextBefore: Int = SEARCH_CONTEXT_BEFORE,
        contextAfter: Int = SEARCH_CONTEXT_AFTER,
        accountId: String = UserProvider.getCurrentUserId()
    ): Result<SearchResult> {
        return try {
            Log.d(TAG, "Поиск в истории: query='$query', offset=$offset")
            val searchResult = chatApi.searchChatHistory(
                accountId = accountId,
                query = query,
                offset = offset,
                contextBefore = contextBefore,
                contextAfter = contextAfter
            )

            Log.d(TAG, "✅ Найдено: total=${searchResult.totalMatches}, matched_id=${searchResult.matchedMessageId}, has_next=${searchResult.hasNext}")
            Result.success(searchResult)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка поиска", e)
            if (contextBefore > 1 || contextAfter > 1) {
                Log.w(TAG, "🔁 Retry search with smaller context")
                return try {
                    val retryResult = chatApi.searchChatHistory(
                        accountId = accountId,
                        query = query,
                        offset = offset,
                        contextBefore = 1,
                        contextAfter = 1
                    )
                    Log.d(TAG, "✅ Retry search ok: total=${retryResult.totalMatches}")
                    Result.success(retryResult)
                } catch (retryError: Exception) {
                    Log.e(TAG, "❌ Retry search failed", retryError)
                    Result.failure(retryError)
                }
            }
            Result.failure(e)
        }
    }

    // Очистить историю
    suspend fun clearHistory() {
        chatMessageDao.clearAll()
    }

    /**
     * Удалить сообщение по backend ID
     */
    suspend fun deleteMessageById(backendId: Int) {
        chatMessageDao.deleteByBackendId(backendId)
        Log.d(TAG, "✅ Сообщение удалено из БД: backendId=$backendId")
    }

    /**
     * Обновить эмодзи локально (без полной синхронизации).
     */
    suspend fun updateEmojiLocal(backendId: Int, emoji: String?) {
        chatMessageDao.updateEmojiByBackendId(backendId, emoji)
        Log.d(TAG, "✅ Emoji обновлено локально: backendId=$backendId emoji=$emoji")
    }

    suspend fun getMessageByBackendId(backendId: Int): ChatMessageEntity? {
        return chatMessageDao.getByBackendId(backendId)
    }
}
