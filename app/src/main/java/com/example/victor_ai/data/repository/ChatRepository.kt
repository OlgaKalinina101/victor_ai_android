package com.example.victor_ai.data.repository

import android.util.Log
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.local.dao.ChatMessageDao
import com.example.victor_ai.data.local.entity.ChatMessageEntity
import com.example.victor_ai.data.network.ChatApi
import com.example.victor_ai.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
    private val chatApi: ChatApi
) {
    companion object {
        private const val TAG = "ChatRepository"
    }

    // Локальный источник истины - все UI читает отсюда
    fun getChatHistory(): Flow<List<ChatMessageEntity>> {
        return chatMessageDao.getAllMessages()
    }

    // Получить все сообщения один раз (без Flow)
    suspend fun getChatHistoryOnce(): List<ChatMessageEntity> {
        return chatMessageDao.getAllMessagesOnce()
    }

    // Синхронизация с бэкендом (загрузка истории) - возвращает информацию о пагинации
    suspend fun syncWithBackendPaginated(accountId: String = UserProvider.getCurrentUserId()): Result<com.example.victor_ai.data.network.dto.ChatHistoryResponse> {
        return try {
            Log.d(TAG, "Синхронизация истории чата с бэкендом...")
            val response = chatApi.getChatHistory(accountId, limit = 25, beforeId = null)

            // Разделяем SessionContext и DB сообщения
            val sessionContextMessages = response.messages.filter { it.id == null }
            val dbMessages = response.messages.filter { it.id != null }

            Log.d(TAG, "📦 SessionContext: ${sessionContextMessages.size}, DB: ${dbMessages.size}")

            // Сохраняем ТОЛЬКО сообщения из БД (не SessionContext!)
            val entities = dbMessages.map { it.toEntity() }
            chatMessageDao.clearAll()
            chatMessageDao.insertMessages(entities)

            Log.d(TAG, "✅ Синхронизация завершена: ${response.messages.size} всего (${sessionContextMessages.size} SessionContext + ${dbMessages.size} DB), has_more=${response.hasMore}, oldest_id=${response.oldestId}")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка синхронизации", e)
            Result.failure(e)
        }
    }

    // Синхронизация с бэкендом (загрузка истории)
    suspend fun syncWithBackend(accountId: String = UserProvider.getCurrentUserId()): Result<Unit> {
        return try {
            Log.d(TAG, "Синхронизация истории чата с бэкендом...")
            val response = chatApi.getChatHistory(accountId, limit = 25, beforeId = null)

            // Конвертируем в Entity
            val entities = response.messages.map { it.toEntity() }

            // Очищаем старую историю и сохраняем новую
            chatMessageDao.clearAll()
            chatMessageDao.insertMessages(entities)

            Log.d(TAG, "✅ Синхронизация завершена: ${entities.size} сообщений, has_more=${response.hasMore}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка синхронизации", e)
            Result.failure(e)
        }
    }

    // Загрузка истории с пагинацией (для скролла вверх)
    suspend fun loadMoreHistory(
        beforeId: Int,
        limit: Int = 25,
        accountId: String = UserProvider.getCurrentUserId()
    ): Result<com.example.victor_ai.data.network.dto.ChatHistoryResponse> {
        return try {
            Log.d(TAG, "Загрузка истории: beforeId=$beforeId, limit=$limit")
            val response = chatApi.getChatHistory(accountId, limit, beforeId)

            // Конвертируем в Entity и добавляем к существующим
            val entities = response.messages.map { it.toEntity() }
            chatMessageDao.insertMessages(entities)

            Log.d(TAG, "✅ Загружено ${entities.size} сообщений, has_more=${response.hasMore}")
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
    suspend fun updateBackendHistory(accountId: String = UserProvider.getCurrentUserId()): Result<Unit> {
        return try {
            Log.d(TAG, "Отправка истории на бэкенд...")
            val localMessages = chatMessageDao.getAllMessagesOnce()
            val chatMessages = localMessages.map { it.toChatMessage() }

            chatApi.updateChatHistory(
                request = com.example.victor_ai.data.network.dto.UpdateHistoryRequest(chatMessages),
                accountId = accountId
            )

            Log.d(TAG, "✅ История отправлена на бэкенд")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка отправки истории", e)
            Result.failure(e)
        }
    }

    // Очистить историю
    suspend fun clearHistory() {
        chatMessageDao.clearAll()
    }
}

// Маппер ChatMessage -> Entity
private fun ChatMessage.toEntity() = ChatMessageEntity(
    text = text,
    isUser = isUser,
    timestamp = timestamp,
    backendId = id  // Сохраняем backend ID
)

// Маппер Entity -> ChatMessage
private fun ChatMessageEntity.toChatMessage() = ChatMessage(
    text = text,
    isUser = isUser,
    timestamp = timestamp,
    id = backendId  // Восстанавливаем backend ID
)
