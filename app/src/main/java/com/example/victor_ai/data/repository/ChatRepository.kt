package com.example.victor_ai.data.repository

import android.util.Log
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

    // Синхронизация с бэкендом (загрузка истории)
    suspend fun syncWithBackend(accountId: String = "test_user"): Result<Unit> {
        return try {
            Log.d(TAG, "Синхронизация истории чата с бэкендом...")
            val messages = chatApi.getChatHistory(accountId)

            // Конвертируем в Entity
            val entities = messages.map { it.toEntity() }

            // Очищаем старую историю и сохраняем новую
            chatMessageDao.clearAll()
            chatMessageDao.insertMessages(entities)

            Log.d(TAG, "✅ Синхронизация завершена: ${entities.size} сообщений")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка синхронизации", e)
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
    suspend fun updateBackendHistory(accountId: String = "test_user"): Result<Unit> {
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
    timestamp = timestamp
)

// Маппер Entity -> ChatMessage
private fun ChatMessageEntity.toChatMessage() = ChatMessage(
    text = text,
    isUser = isUser,
    timestamp = timestamp
)
