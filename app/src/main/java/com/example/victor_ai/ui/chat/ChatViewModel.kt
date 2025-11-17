package com.example.victor_ai.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.dto.AssistantRequest
import com.example.victor_ai.data.network.dto.GeoLocation
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.domain.model.toEntity
import com.example.victor_ai.logic.ChatHistoryHelper
import com.example.victor_ai.logic.SoundPlayer
import com.example.victor_ai.logic.processStreamingMessage
import com.example.victor_ai.logic.updateChatHistory
import com.example.victor_ai.ui.main.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val soundPlayer: SoundPlayer
) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    // Пагинация чата
    private var oldestMessageId: Int? = null

    // Геолокация (будет устанавливаться извне)
    private var latestGeo: GeoLocation? = null

    // MainViewModel для управления плейлистом (будет устанавливаться извне)
    private var mainViewModel: MainViewModel? = null

    // SessionId (будет устанавливаться извне)
    private var sessionId: String = ""

    // 🔍 Поиск по истории
    private val _searchResults = MutableStateFlow<List<ChatMessage>>(emptyList())
    val searchResults: StateFlow<List<ChatMessage>> = _searchResults

    private val _searchMatchedMessageId = MutableStateFlow<Int?>(null)
    val searchMatchedMessageId: StateFlow<Int?> = _searchMatchedMessageId

    private val _searchTotalMatches = MutableStateFlow(0)
    val searchTotalMatches: StateFlow<Int> = _searchTotalMatches

    private val _searchCurrentIndex = MutableStateFlow(0)
    val searchCurrentIndex: StateFlow<Int> = _searchCurrentIndex

    private val _searchHasNext = MutableStateFlow(false)
    val searchHasNext: StateFlow<Boolean> = _searchHasNext

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private var currentSearchQuery: String = ""

    /**
     * Установка sessionId для запросов к ассистенту
     */
    fun setSessionId(id: String) {
        sessionId = id
    }

    /**
     * Установка текущей геолокации
     */
    fun setLocation(geo: GeoLocation?) {
        latestGeo = geo
    }

    /**
     * Установка MainViewModel для управления треками
     */
    fun setMainViewModel(viewModel: MainViewModel) {
        mainViewModel = viewModel
    }

    /**
     * Добавление нового сообщения от пользователя
     */
    fun addUserMessage(text: String) {
        val timestamp = System.currentTimeMillis() / 1000
        val newMessage = ChatMessage(
            text = text,
            isUser = true,
            timestamp = timestamp,
            id = Int.MAX_VALUE - timestamp.toInt(),
            isSynced = false
        )
        _chatMessages.value += newMessage
        Log.d("Chat", "➕ Добавлено user сообщение: ВРЕМЕННЫЙ id=${newMessage.id}, isSynced=false, text=${newMessage.text.take(50)}")
        Log.d("Chat", "📊 Всего сообщений: ${_chatMessages.value.size}")
    }

    /**
     * Редактирование сообщения
     */
    fun editMessage(index: Int, newText: String) {
        _chatMessages.value = _chatMessages.value.toMutableList().apply {
            this[index] = this[index].copy(text = newText)
        }

        // Отправляем на бэкенд
        viewModelScope.launch {
            val success = updateChatHistory(_chatMessages.value)
            if (success) {
                _snackbarMessage.value = "✓ Сообщение обновлено"
            } else {
                _snackbarMessage.value = "⚠ Ошибка обновления"
            }
        }
    }

    /**
     * Инициализация истории чата с бэкенда
     */
    fun initHistory(history: List<ChatMessage>) {
        Log.d("Chat", "🔄 onInitHistory вызван: получено ${history.size} сообщений с бэкенда")

        if (history.isNotEmpty()) {
            Log.d("Chat", "📋 Первые 3 из истории: ${history.take(3).map { "id=${it.id}, ts=${it.timestamp}, isUser=${it.isUser}" }}")
        }

        val currentMessages = _chatMessages.value
        Log.d("Chat", "📊 Текущих сообщений: ${currentMessages.size}")

        // 🔥 Объединяем и удаляем дубликаты по уникальному ключу
        val allMessages = (currentMessages + history).distinctBy { message ->
            // Уникальный ключ: для синхронизированных - по ID, для несинхронизированных - по timestamp+isUser
            if (message.isSynced && message.id != null) {
                "synced_${message.id}"
            } else {
                "unsynced_${message.timestamp}_${message.isUser}"
            }
        }

        Log.d("Chat", "🔍 ДО фильтрации: ${currentMessages.size + history.size}, ПОСЛЕ фильтрации: ${allMessages.size}")

        _chatMessages.value = allMessages

        Log.d("Chat", "✅ ИТОГО: ${allMessages.size} сообщений")
        val unsynced = allMessages.filter { !it.isSynced }
        val synced = allMessages.filter { it.isSynced }
        Log.d("Chat", "📊 Несинхронизированных: ${unsynced.size}, синхронизированных: ${synced.size}")

        // Логируем все ID синхронизированных для отладки
        if (synced.isNotEmpty()) {
            Log.d("Chat", "📊 Синхронизированные IDs: ${synced.map { it.id }}")
        }

        if (unsynced.isNotEmpty()) {
            Log.d("Chat", "🔥 Несинхронизированные: ${unsynced.map { "id=${it.id}, ts=${it.timestamp}, isUser=${it.isUser}, isSynced=${it.isSynced}" }}")
        }
    }

    /**
     * Обновление информации о пагинации
     */
    fun updatePaginationInfo(oldestId: Int?, hasMore: Boolean) {
        oldestMessageId = oldestId
        Log.d("Chat", "📋 Пагинация: oldestId=$oldestId, hasMore=$hasMore")
    }

    /**
     * Загрузка дополнительной истории чата (пагинация)
     * Возвращает Result с Triple(hasMore, oldestId, isError)
     */
    suspend fun loadMoreHistory(beforeId: Int): Result<Triple<Boolean, Int?, Boolean>> {
        return withContext(Dispatchers.Main) {
            try {
                Log.d("Chat", "📥 Загрузка истории: beforeId=$beforeId")

                val result = withContext(Dispatchers.IO) {
                    ChatHistoryHelper.repository.loadMoreHistory(beforeId)
                }

                result.onSuccess { response ->
                    Log.d("Chat", "✅ Загружено ${response.messages.size} сообщений, has_more=${response.hasMore}, newOldestId=${response.oldestId}")

                    if (response.messages.isNotEmpty()) {
                        Log.d("Chat", "📋 Загруженные IDs: ${response.messages.map { it.id }}")

                        val currentMessages = _chatMessages.value
                        Log.d("Chat", "📊 Текущих сообщений ДО добавления: ${currentMessages.size}")

                        // 🔥 Объединяем и удаляем дубликаты по уникальному ключу
                        val allMessages = (currentMessages + response.messages).distinctBy { message ->
                            // Уникальный ключ: для синхронизированных - по ID, для несинхронизированных - по timestamp+isUser
                            if (message.isSynced && message.id != null) {
                                "synced_${message.id}"
                            } else {
                                "unsynced_${message.timestamp}_${message.isUser}"
                            }
                        }

                        Log.d("Chat", "🔍 ДО фильтрации: ${currentMessages.size + response.messages.size}, ПОСЛЕ фильтрации: ${allMessages.size}")

                        // Проверяем, были ли дубликаты
                        val duplicatesCount = (currentMessages.size + response.messages.size) - allMessages.size
                        if (duplicatesCount > 0) {
                            Log.w("Chat", "⚠️ Удалено дубликатов: $duplicatesCount")
                        }

                        _chatMessages.value = allMessages

                        Log.d("Chat", "📦 Обновлено: всего ${allMessages.size} сообщений")
                        Log.d("Chat", "📊 Все синхронизированные IDs: ${allMessages.filter { it.isSynced }.map { it.id }}")
                    }

                    // Успех: hasMore, oldestId, isError = false
                    return@withContext Result.success(Triple(response.hasMore, response.oldestId, false))
                }.onFailure { error ->
                    Log.e("Chat", "❌ Ошибка загрузки истории: ${error.message}")
                    // Ошибка: сохраняем текущие значения, isError = true
                    return@withContext Result.failure(error)
                }

                // Не должно дойти сюда
                Result.failure(Exception("Unexpected state"))
            } catch (e: Exception) {
                Log.e("Chat", "❌ Ошибка загрузки истории", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Отправка текста ассистенту
     */
    fun sendTextToAssistant(text: String) {
        viewModelScope.launch {
            try {
                _isTyping.value = true

                val request = AssistantRequest(
                    sessionId = sessionId,
                    text = text,
                    geo = latestGeo
                )

                // 🔥 Сохраняем user сообщение в локальную БД
                val userMessage = _chatMessages.value.last() // последнее сообщение - это user message
                ChatHistoryHelper.repository.addMessage(userMessage.toEntity())

                val timestamp = System.currentTimeMillis() / 1000
                val assistantMessage = ChatMessage(
                    text = "",
                    isUser = false,
                    timestamp = timestamp,
                    id = Int.MAX_VALUE - timestamp.toInt(),
                    isSynced = false
                )

                val currentMessages = _chatMessages.value.toMutableList()
                currentMessages.add(assistantMessage)
                val messageIndex = currentMessages.size - 1
                _chatMessages.value = currentMessages

                Log.d("Chat", "➕ Добавлено assistant сообщение (пустое): ВРЕМЕННЫЙ id=${assistantMessage.id}, isSynced=false")
                Log.d("Chat", "📊 Всего сообщений: ${_chatMessages.value.size}")

                val charQueue = Channel<Char>(Channel.UNLIMITED)

                // Корутина для печати
                val typingJob = launch {
                    var charCount = 0
                    for (char in charQueue) {
                        val messages = _chatMessages.value.toMutableList()
                        messages[messageIndex] = messages[messageIndex].copy(
                            text = messages[messageIndex].text + char
                        )
                        _chatMessages.value = messages

                        soundPlayer.playKeypress()

                        val progress = (charCount.toFloat() / 15f).coerceAtMost(1f)
                        val delayTime = (48 - (48 - 16) * progress).toLong()

                        delay(delayTime)
                        charCount++
                    }
                    Log.d("Typing", "✅ Печать завершена")
                }

                val streamJob = launch(Dispatchers.IO) {
                    val result = processStreamingMessage(
                        request = request,
                        onChunkReceived = { chunk ->
                            for (char in chunk) {
                                charQueue.send(char)
                            }
                        },
                        onMetadataReceived = { metadata ->
                            val trackId = metadata["track_id"] as? Int
                            if (trackId != null) {
                                Log.d("Assistant", "🎵 Получен track_id: $trackId")
                                // Запускаем воспроизведение трека
                                launch {
                                    mainViewModel?.playTrack(trackId)
                                }
                            }
                        }
                    )

                    result.onFailure { error ->
                        Log.e("Assistant", "❌ Ошибка стрима: ${error.message}")
                    }
                }

                streamJob.join()
                charQueue.close()
                typingJob.join()

                _isTyping.value = false

                // 🔥 Сохраняем assistant сообщение в локальную БД
                val finalAssistantMessage = _chatMessages.value[messageIndex]
                ChatHistoryHelper.repository.addMessage(finalAssistantMessage.toEntity())
                Log.d("Assistant", "✅ Сообщения сохранены в локальную БД")
                Log.d("Chat", "📊 Итого сообщений: ${_chatMessages.value.size}")
                Log.d("Chat", "💬 Последние 3 сообщения: ${_chatMessages.value.takeLast(3).map { "id=${it.id}, isUser=${it.isUser}, text=${it.text.take(20)}" }}")

            } catch (e: Exception) {
                Log.e("Assistant", "❌ Ошибка отправки: ${e.message}")
                _isTyping.value = false
            }
        }
    }

    /**
     * Поиск по истории чата
     */
    fun searchInHistory(query: String, offset: Int = 0) {
        if (query.isBlank()) {
            // Сброс поиска при пустом запросе
            clearSearch()
            return
        }

        currentSearchQuery = query

        viewModelScope.launch {
            _isSearching.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    ChatHistoryHelper.repository.searchHistory(query, offset)
                }

                result.onSuccess { searchResult ->
                    Log.d("ChatSearch", "🔍 Найдено: total=${searchResult.totalMatches}, index=$offset, has_next=${searchResult.hasNext}")

                    _searchResults.value = searchResult.messages
                    _searchMatchedMessageId.value = searchResult.matchedMessageId
                    _searchTotalMatches.value = searchResult.totalMatches
                    _searchCurrentIndex.value = searchResult.currentMatchIndex
                    _searchHasNext.value = searchResult.hasNext

                    // Если есть результаты - заменяем текущие сообщения на контекст поиска
                    if (searchResult.messages.isNotEmpty()) {
                        _chatMessages.value = searchResult.messages
                    }
                }.onFailure { error ->
                    Log.e("ChatSearch", "❌ Ошибка поиска: ${error.message}")
                    _snackbarMessage.value = "Ошибка поиска"
                }
            } finally {
                _isSearching.value = false
            }
        }
    }

    /**
     * Переход к следующему результату поиска
     */
    fun searchNext() {
        if (currentSearchQuery.isBlank()) return

        // Проверяем, есть ли еще результаты
        if (!_searchHasNext.value) {
            Log.d("ChatSearch", "⚠️ Больше результатов не найдено")
            _snackbarMessage.value = "Больше результатов не найдено"
            return
        }

        val nextOffset = _searchCurrentIndex.value + 1
        Log.d("ChatSearch", "➡️ Переход к следующему результату: offset=$nextOffset")

        // Повторный поиск со следующим offset
        searchInHistory(currentSearchQuery, nextOffset)
    }

    /**
     * Сброс поиска и возврат к обычной истории
     */
    fun clearSearch() {
        Log.d("ChatSearch", "🔄 Сброс поиска")
        currentSearchQuery = ""
        _searchResults.value = emptyList()
        _searchMatchedMessageId.value = null
        _searchTotalMatches.value = 0
        _searchCurrentIndex.value = 0
        _searchHasNext.value = false
        _isSearching.value = false

        // Перезагружаем обычную историю (последние 25 сообщений)
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    ChatHistoryHelper.repository.syncWithBackendPaginated()
                }

                result.onSuccess { response ->
                    Log.d("ChatSearch", "✅ Перезагружена обычная история: ${response.messages.size} сообщений")
                    _chatMessages.value = response.messages
                    oldestMessageId = response.oldestId
                }.onFailure { error ->
                    Log.e("ChatSearch", "❌ Ошибка перезагрузки истории: ${error.message}")
                }
            } catch (e: Exception) {
                Log.e("ChatSearch", "❌ Ошибка при сбросе поиска: ${e.message}")
            }
        }
    }

    /**
     * Очистка snackbar сообщения
     */
    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
