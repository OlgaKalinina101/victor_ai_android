package com.example.victor_ai.logic

import android.util.Log
import com.example.victor_ai.data.local.entity.ChatMessageEntity
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.data.repository.ChatRepository

/**
 * Helper объект для работы с историей чата через локальный репозиторий
 * Инициализируется в Application
 */
object ChatHistoryHelper {
    private var _repository: ChatRepository? = null
    val repository: ChatRepository
        get() = _repository ?: throw IllegalStateException("ChatHistoryHelper не инициализирован! Вызовите initialize() в Application")

    fun initialize(chatRepository: ChatRepository) {
        _repository = chatRepository
    }
}

/**
 * Загружает историю чата из локальной БД (с предварительной синхронизацией с бэкендом)
 */
suspend fun fetchChatHistory(): List<ChatMessage> {
    return try {
        // Синхронизируем с бэкендом
        ChatHistoryHelper.repository.syncWithBackend()
            .onFailure { e ->
                Log.w("ChatHistory", "⚠️ Синхронизация не удалась, используем локальные данные: ${e.message}")
            }

        // Возвращаем данные из локальной БД
        val entities = ChatHistoryHelper.repository.getChatHistoryOnce()
        entities.map { it.toChatMessage() }
    } catch (e: Exception) {
        Log.e("ChatHistory", "❌ Ошибка загрузки истории", e)
        // Возвращаем пустой список в случае ошибки
        emptyList()
    }
}

/**
 * Обновляет историю чата в локальной БД и на бэкенде
 */
suspend fun updateChatHistory(messages: List<ChatMessage>): Boolean {
    return try {
        // Сохраняем локально
        val entities = messages.map { it.toEntity() }
        ChatHistoryHelper.repository.clearHistory()
        ChatHistoryHelper.repository.addMessages(entities)

        // Отправляем на бэкенд
        val result = ChatHistoryHelper.repository.updateBackendHistory()
        result.isSuccess.also { success ->
            if (success) {
                Log.d("ChatHistory", "✅ История обновлена на бэкенде")
            } else {
                Log.w("ChatHistory", "⚠️ Не удалось обновить на бэкенде, но сохранено локально")
            }
        }
    } catch (e: Exception) {
        Log.e("ChatHistory", "❌ Ошибка обновления истории", e)
        false
    }
}

// Мапперы
private fun ChatMessageEntity.toChatMessage() = ChatMessage(
    text = text,
    isUser = isUser,
    timestamp = timestamp
)

private fun ChatMessage.toEntity() = ChatMessageEntity(
    text = text,
    isUser = isUser,
    timestamp = timestamp
)
