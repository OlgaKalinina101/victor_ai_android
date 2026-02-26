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

package com.example.victor_ai.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.dto.GeoLocation
import com.example.victor_ai.data.repository.CareBankRepository
import com.example.victor_ai.data.repository.ChatRepository
import com.example.victor_ai.di.StreamingApi
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.domain.model.toChatMessage
import com.example.victor_ai.domain.playback.PlaybackController
import com.example.victor_ai.logic.SoundPlayer
import com.example.victor_ai.logic.processStreamingMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val soundPlayer: SoundPlayer,
    private val chatRepository: ChatRepository,
    val careBankRepository: CareBankRepository,
    @StreamingApi private val streamingApi: ApiService
) : ViewModel() {

    companion object {
        private const val TAG = "Chat"
    }

    // Временные сообщения (во время стриминга, до синхронизации с бэкендом)
    private val _temporaryMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    
    // Флаг режима поиска - когда true, показываем результаты поиска вместо Room
    private val _isInSearchMode = MutableStateFlow(false)
    private val _searchResultMessages = MutableStateFlow<List<ChatMessage>>(emptyList())

    /**
     * Основной источник данных для UI - комбинация Room + временные сообщения.
     * Room автоматически уведомляет при изменениях в БД.
     * 
     * ДЕДУПЛИКАЦИЯ: При синхронизации сообщение может существовать и как временное,
     * и как синхронизированное. Временное сообщение удаляется, если есть соответствующее
     * синхронизированное с тем же текстом, ролью и близким timestamp (±2 минуты).
     */
    val chatMessages: StateFlow<List<ChatMessage>> = combine(
        chatRepository.getChatHistory().map { entities -> 
            entities.map { it.toChatMessage() } 
        },
        _temporaryMessages,
        _isInSearchMode,
        _searchResultMessages
    ) { roomMessages, tempMessages, isSearchMode, searchMessages ->
        if (isSearchMode) {
            // В режиме поиска показываем результаты поиска
            searchMessages
        } else {
            // Фильтруем временные сообщения: удаляем те, для которых есть синхронизированный дубликат
            val filteredTempMessages = tempMessages.filter { temp ->
                // Ищем синхронизированное сообщение с тем же текстом, ролью и близким timestamp
                val hasSyncedDuplicate = roomMessages.any { synced ->
                    synced.isUser == temp.isUser &&
                    synced.text == temp.text &&
                    kotlin.math.abs(synced.timestamp - temp.timestamp) < 120 // 2 минуты
                }
                !hasSyncedDuplicate
            }
            
            // Объединяем: синхронизированные + отфильтрованные временные
            val combined = roomMessages + filteredTempMessages
            combined.sortedBy { it.timestamp }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    // Пагинация чата
    private var oldestMessageId: Int? = null
    
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore
    
    private val _hasMoreHistory = MutableStateFlow(true)
    val hasMoreHistory: StateFlow<Boolean> = _hasMoreHistory
    
    // Публичный StateFlow для oldestId (нужен для UI пагинации)
    private val _oldestId = MutableStateFlow<Int?>(null)
    val oldestId: StateFlow<Int?> = _oldestId

    // Геолокация (будет устанавливаться извне)
    private var latestGeo: GeoLocation? = null

    // PlaybackController для управления плейлистом (интерфейс вместо прямой VM зависимости)
    private var playbackController: PlaybackController? = null

    private fun parseIntLike(value: Any?): Int? {
        return when (value) {
            is Int -> value
            is Long -> value.toInt()
            is Double -> value.toInt()
            is Float -> value.toInt()
            is Number -> value.toInt()
            is String -> value.toIntOrNull()
            else -> null
        }
    }

    private fun extractTrackId(metadata: Map<String, Any>): Int? {
        // ✅ Базовый формат (ожидаемый): {"metadata": {"track_id": 40}}
        parseIntLike(metadata["track_id"])?.let { return it }

        // 🔁 Частые варианты ключей
        parseIntLike(metadata["trackId"])?.let { return it }
        parseIntLike(metadata["track-id"])?.let { return it }

        // ✅ Вложенный формат: {"metadata": {"track": {"track_id": 40, ...}}}
        val trackObj = metadata["track"]
        when (trackObj) {
            is Map<*, *> -> return parseIntLike(trackObj["track_id"] ?: trackObj["trackId"])
            is JSONObject -> return parseIntLike(trackObj.opt("track_id") ?: trackObj.opt("trackId"))
        }

        return null
    }

    // SessionId (будет устанавливаться извне)
    private var sessionId: String = ""
    
    // StateFlow для управления WebView с Care Bank
    private val _careBankWebViewUrl = MutableStateFlow<String?>(null)
    val careBankWebViewUrl: StateFlow<String?> = _careBankWebViewUrl
    
    private val _careBankAutomationData = MutableStateFlow<Map<String, String>>(emptyMap())
    val careBankAutomationData: StateFlow<Map<String, String>> = _careBankAutomationData

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
    
    // Job для текущего стрима (для отмены при новом сообщении)
    private var currentStreamJob: Job? = null
    
    // Флаг для предотвращения повторной загрузки истории
    private var isHistoryInitialized = false

    init {
        // Данные автоматически загрузятся из Room через Flow
        // MyApp синхронизирует данные при старте приложения
        Log.d(TAG, "🚀 ChatViewModel init - подписан на Room Flow")
    }
    
    /**
     * Очистка ресурсов при уничтожении ViewModel
     */
    override fun onCleared() {
        super.onCleared()
        currentStreamJob?.cancel()
        Log.d(TAG, "🧹 ViewModel.onCleared(): все активные стримы отменены")
    }

    /**
     * Установка sessionId для запросов к ассистенту
     */
    fun setSessionId(id: String) {
        sessionId = id
    }

    /**
     * Перезагрузка чата для указанного аккаунта.
     * Вызывается после успешной авторизации (когда accountId уже известен).
     * Очищает Room и запускает синхронизацию с бэкендом.
     * UI автоматически обновится через Flow.
     */
    fun reloadForAccount(accountId: String) {
        Log.d(TAG, "🔄 reloadForAccount($accountId)")
        sessionId = accountId
        _temporaryMessages.value = emptyList()
        _isInSearchMode.value = false
        oldestMessageId = null
        _oldestId.value = null
        _hasMoreHistory.value = true
        
        // Синхронизация выполняется в MyApp при смене аккаунта.
        // Здесь только сбрасываем локальные флаги и ждём обновлений из Room.
        Log.d(TAG, "⏳ Синхронизация чата выполняется в MyApp, ждём Room updates")
    }

    /**
     * Установка текущей геолокации
     */
    fun setLocation(geo: GeoLocation?) {
        latestGeo = geo
    }

    /**
     * Установка контроллера воспроизведения (через интерфейс, не прямая VM зависимость)
     * @param controller Реализация PlaybackController (обычно MainViewModel)
     */
    fun setPlaybackController(controller: PlaybackController) {
        playbackController = controller
    }
    
    /**
     * Закрыть WebView с Care Bank
     */
    fun closeCareBankWebView() {
        _careBankWebViewUrl.value = null
        _careBankAutomationData.value = emptyMap()
    }
    
    /**
     * Обработка команды Care Bank (перенесено из UI для правильной архитектуры)
     * @param command Команда начинающаяся с "/" (например "/кофе")
     * @param context Android Context для обработки команды
     * @param careBankCommandHandler Handler для обработки команд
     */
    fun handleCareBankCommand(
        command: String,
        context: android.content.Context,
        careBankCommandHandler: com.example.victor_ai.logic.carebank.CareBankCommandHandler
    ) {
        viewModelScope.launch {
            try {
                Log.d("Chat", "🔵 Обработка команды Care Bank: '$command'")
                val url = careBankCommandHandler.handleCommand(command, context)
                
                if (url != null) {
                    Log.d("Chat", "✅ Получен URL для WebView: $url")
                    val searchQuery = command.trim().substring(1) // Убираем "/"
                    _careBankWebViewUrl.value = url
                    // Для ручных команд через / пока не используем automation data
                    _careBankAutomationData.value = emptyMap()
                } else {
                    Log.e("Chat", "❌ Не удалось обработать команду: $command")
                }
            } catch (e: Exception) {
                Log.e("Chat", "❌ Ошибка обработки команды Care Bank: ${e.message}")
            }
        }
    }
    
    /**
     * Удалить последнее assistant сообщение из временных сообщений.
     * Используется для очистки временного сообщения перед новым стримом.
     */
    fun removeLastAssistantMessage() {
        val tempMessages = _temporaryMessages.value.toMutableList()
        val lastAssistantIndex = tempMessages.indexOfLast { !it.isUser }
        
        if (lastAssistantIndex != -1) {
            val removedMessage = tempMessages[lastAssistantIndex]
            tempMessages.removeAt(lastAssistantIndex)
            _temporaryMessages.value = tempMessages
            Log.d(TAG, "🗑️ Удалено временное assistant сообщение: text='${removedMessage.text.take(30)}'")
        } else {
            Log.w(TAG, "⚠️ Не найдено временное assistant сообщение для удаления")
        }
    }
    
    /**
     * Маппер типов metadata -> emoji
     */
    private fun getEmojiForMetadataType(type: String): String? {
        return when (type) {
            "food" -> "☕"
            // В дальнейшем можно добавить другие типы
            else -> null
        }
    }

    /**
     * Добавление нового сообщения от пользователя.
     * Сообщение добавляется во временный список и будет синхронизировано с бэкендом после стриминга.
     */
    fun addUserMessage(text: String, imageCount: Int = 0) {
        if (text.isBlank() && imageCount == 0) {
            Log.d(TAG, "⚠️ Попытка добавить пустое сообщение без изображений - игнорируем")
            return
        }

        val timestamp = System.currentTimeMillis() / 1000
        val newMessage = ChatMessage(
            text = text,
            isUser = true,
            timestamp = timestamp,
            id = null,  // Будет присвоен бэкендом после синхронизации
            isSynced = false,
            imageCount = imageCount
        )
        _temporaryMessages.value += newMessage
        Log.d(TAG, "➕ Добавлено временное user сообщение: text='${text.take(50)}', imageCount=$imageCount")
    }

    /**
     * Входящее сообщение от рефлексии Victor'а (push reflection_message).
     * Бэкенд уже сохранил его в dialogue_history — оно появится при следующей синхронизации,
     * но для мгновенного UX добавляем как временное.
     */
    fun addReflectionMessage(text: String) {
        if (text.isBlank()) return
        val timestamp = System.currentTimeMillis() / 1000
        val message = ChatMessage(
            text = text,
            isUser = false,
            timestamp = timestamp,
            id = null,
            isSynced = false
        )
        _temporaryMessages.value += message
        Log.d(TAG, "💬 Добавлено reflection-сообщение: text='${text.take(80)}'")
    }

    /**
     * Редактирование сообщения.
     * Обновляет на бэкенде и синхронизирует Room.
     */
    fun editMessage(index: Int, newText: String) {
        val messages = chatMessages.value
        if (index >= messages.size) {
            Log.e(TAG, "❌ Индекс $index вне диапазона (${messages.size} сообщений)")
            return
        }
        
        val editedMessage = messages[index]
        
        viewModelScope.launch {
            try {
                val editedMessageId = editedMessage.id
                val lastMessages = messages.sortedBy { it.timestamp }.takeLast(6)
                
                Log.d(TAG, "📝 Редактирование сообщения: id=$editedMessageId, text='${newText.take(30)}'")
                
                val result = withContext(Dispatchers.IO) {
                    chatRepository.updateBackendHistory(
                        messages = lastMessages,
                        editedMessageId = editedMessageId,
                        editedMessageText = newText
                    )
                }
                
                if (result.isSuccess) {
                    // Синхронизируем с бэкендом чтобы получить обновлённые данные
                    withContext(Dispatchers.IO) {
                        chatRepository.syncWithBackend(sessionId)
                    }
                    _snackbarMessage.value = "✓ Сообщение обновлено"
                    Log.d(TAG, "✅ Сообщение успешно обновлено")
                } else {
                    _snackbarMessage.value = "⚠ Ошибка обновления"
                    Log.e(TAG, "❌ Ошибка обновления на бэкенде")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при обновлении: ${e.message}")
                _snackbarMessage.value = "⚠ Ошибка обновления"
            }
        }
    }

    /**
     * Обновление информации о пагинации
     */
    fun updatePaginationInfo(oldestId: Int?, hasMore: Boolean) {
        oldestMessageId = oldestId
        _oldestId.value = oldestId
        _hasMoreHistory.value = hasMore
        Log.d(TAG, "📋 Пагинация: oldestId=$oldestId, hasMore=$hasMore")
    }

    /**
     * Загрузка дополнительной истории чата (пагинация).
     * Данные сохраняются в Room, UI обновляется автоматически через Flow.
     * Возвращает Result с Triple(hasMore, oldestId, isError)
     */
    suspend fun loadMoreHistory(beforeId: Int): Result<Triple<Boolean, Int?, Boolean>> {
        return withContext(Dispatchers.Main) {
            _isLoadingMore.value = true
            try {
                Log.d(TAG, "📥 Загрузка истории: beforeId=$beforeId")

                val result = withContext(Dispatchers.IO) {
                    chatRepository.loadMoreHistory(beforeId)
                }

                result.onSuccess { response ->
                    Log.d(TAG, "✅ Загружено ${response.messages.size} сообщений, has_more=${response.hasMore}")
                    
                    // Обновляем пагинацию - данные уже сохранены в Room через chatRepository.loadMoreHistory()
                    oldestMessageId = response.oldestId
                    _oldestId.value = response.oldestId
                    
                    return@withContext Result.success(Triple(response.hasMore, response.oldestId, false))
                }.onFailure { error ->
                    Log.e(TAG, "❌ Ошибка загрузки истории: ${error.message}")
                    return@withContext Result.failure(error)
                }

                Result.failure(Exception("Unexpected state"))
            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка загрузки истории", e)
                Result.failure(e)
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    /**
     * Отправка текста ассистенту.
     * Создаёт временные сообщения во время стриминга, затем синхронизирует с бэкендом.
     */
    fun sendTextToAssistant(
        text: String,
        attachedImages: List<com.example.victor_ai.utils.ImageUtils.ImageAttachment> = emptyList(),
        swipeMessageId: Int? = null
    ) {
        currentStreamJob?.cancel()
        Log.d(TAG, "🛑 Предыдущий стрим отменён (если был)")
        
        currentStreamJob = viewModelScope.launch {
            try {
                _isTyping.value = true

                Log.d(TAG, "📤 Отправка сообщения: text='${text.take(50)}', изображений=${attachedImages.size}")

                // Создаём временное assistant сообщение для стриминга
                val timestamp = System.currentTimeMillis() / 1000
                val assistantMessage = ChatMessage(
                    text = "",
                    isUser = false,
                    timestamp = timestamp,
                    id = null,
                    isSynced = false
                )

                // Добавляем во временный список
                val tempMessages = _temporaryMessages.value.toMutableList()
                tempMessages.add(assistantMessage)
                val messageIndex = tempMessages.size - 1
                _temporaryMessages.value = tempMessages

                Log.d(TAG, "➕ Добавлено временное assistant сообщение")

                val charQueue = Channel<Char>(Channel.UNLIMITED)

                // Корутина для печати - обновляет временное сообщение
                val typingJob = launch {
                    var charCount = 0
                    for (char in charQueue) {
                        val messages = _temporaryMessages.value.toMutableList()
                        if (messageIndex < messages.size) {
                            messages[messageIndex] = messages[messageIndex].copy(
                                text = messages[messageIndex].text + char
                            )
                            _temporaryMessages.value = messages
                        }

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
                        streamingApi = streamingApi,
                        sessionId = sessionId,
                        text = text,
                        geo = latestGeo,
                        images = attachedImages,
                        swipeMessageId = swipeMessageId,
                        systemEvent = null,
                        onChunkReceived = { chunk ->
                            for (char in chunk) {
                                charQueue.send(char)
                            }
                        },
                        onMetadataReceived = { metadata ->
                            Log.d("Assistant", "🎯 onMetadataReceived вызван! metadata=$metadata")
                            Log.d("Assistant", "🔑 Keys в metadata: ${metadata.keys}")
                            
                            // Обработка track_id (музыка)
                            val trackId = extractTrackId(metadata)
                            if (trackId != null) {
                                Log.d("Assistant", "🎵 Получен track_id: $trackId")
                                // 🔥 Запускаем воспроизведение через интерфейс (не прямая VM зависимость)
                                viewModelScope.launch {
                                    playbackController?.playTrack(trackId)
                                }
                            } else if (metadata.containsKey("track_id") || metadata.containsKey("track") || metadata.containsKey("trackId")) {
                                Log.w("Assistant", "⚠️ track_id присутствует, но не распарсился. track_id=${metadata["track_id"]} (type=${metadata["track_id"]?.javaClass}), track=${metadata["track"]} (type=${metadata["track"]?.javaClass})")
                            }
                            
                            // Обработка food (Care Bank)
                            Log.d("Assistant", "🔍 Проверяем наличие 'food' в metadata...")
                            val foodData = metadata["food"]
                            Log.d("Assistant", "📊 foodData: $foodData (type: ${foodData?.javaClass})")
                            if (foodData != null) {
                                Log.d("Assistant", "☕ Получены данные food: $foodData")
                                
                                // Дожидаемся окончания печати
                                Log.d("Assistant", "🚀 Запускаем корутину для обработки food...")
                                viewModelScope.launch {
                                    Log.d("Assistant", "✨ Корутина запущена! Ждём окончания печати...")
                                    // Ждем пока закончится печать сообщения
                                    typingJob.join()
                                    Log.d("Assistant", "⌨️ Печать завершена! Парсим данные...")
                                    
                                    // Парсим данные из metadata
                                    val automationData = when (foodData) {
                                        is org.json.JSONObject -> {
                                            // Конвертируем JSONObject в Map<String, String>
                                            foodData.keys().asSequence().associateWith { key ->
                                                foodData.getString(key)
                                            }
                                        }
                                        is Map<*, *> -> {
                                            // Если уже Map, конвертируем в Map<String, String>
                                            foodData.mapKeys { it.key.toString() }
                                                .mapValues { it.value.toString() }
                                        }
                                        else -> {
                                            Log.e("Assistant", "❌ Неожиданный тип данных food: ${foodData.javaClass}")
                                            emptyMap()
                                        }
                                    }
                                    
                                    if (automationData.isEmpty()) {
                                        Log.e("Assistant", "❌ Не удалось распарсить данные food")
                                        return@launch
                                    }
                                    
                                    Log.d("Assistant", "📦 Распарсенные данные food: $automationData")
                                    
                                    // Получаем emoji для типа "food"
                                    val emoji = getEmojiForMetadataType("food")
                                    Log.d("Assistant", "🔍 getEmojiForMetadataType('food') вернул: $emoji")
                                    if (emoji == null) {
                                        Log.e("Assistant", "❌ Не найден emoji для типа 'food'")
                                        return@launch
                                    }
                                    
                                    // Загружаем запись из CareBankRepository
                                    Log.d("Assistant", "📡 Загружаем CareBankEntry для emoji: $emoji")
                                    val careBankEntry = withContext(Dispatchers.IO) {
                                        careBankRepository.getEntryByEmoji(emoji)
                                    }
                                    Log.d("Assistant", "📥 Результат getEntryByEmoji: $careBankEntry")
                                    if (careBankEntry == null) {
                                        Log.e("Assistant", "❌ Не найдена запись Care Bank для emoji: $emoji")
                                        return@launch
                                    }
                                    
                                    Log.d("Assistant", "✅ Найдена запись Care Bank:")
                                    Log.d("Assistant", "   - emoji: ${careBankEntry.emoji}")
                                    Log.d("Assistant", "   - value: ${careBankEntry.value}")
                                    Log.d("Assistant", "   - searchUrl: ${careBankEntry.searchUrl}")
                                    Log.d("Assistant", "   - searchField: ${careBankEntry.searchField}")
                                    Log.d("Assistant", "   - addToCart1: ${careBankEntry.addToCart1Coords}")
                                    Log.d("Assistant", "   - openCart: ${careBankEntry.openCartCoords}")
                                    Log.d("Assistant", "   - placeOrder: ${careBankEntry.placeOrderCoords}")
                                    
                                    // Открываем WebView через StateFlow
                                    withContext(Dispatchers.Main) {
                                        _careBankWebViewUrl.value = careBankEntry.value
                                        _careBankAutomationData.value = automationData
                                    }
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

                // Temp-сообщения остаются в UI — дедупликация уберёт их,
                // когда Room получит синхронизированные копии с бэкенда.
                Log.d(TAG, "🔄 syncLatestPage после стриминга...")
                try {
                    val result = withContext(Dispatchers.IO) {
                        chatRepository.syncLatestPage(sessionId)
                    }
                    
                    result.onSuccess { response ->
                        Log.d(TAG, "✅ syncLatestPage: ${response.messages.size} сообщений")
                        oldestMessageId = response.oldestId
                        _oldestId.value = response.oldestId
                    }.onFailure { error ->
                        Log.e(TAG, "❌ Ошибка syncLatestPage: ${error.message}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Исключение при syncLatestPage: ${e.message}")
                }

                // Финальная очистка temp (дубли уже убраны дедупликацией)
                _temporaryMessages.value = emptyList()
                _isTyping.value = false

            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка отправки: ${e.message}")
                _isTyping.value = false
            } finally {
                if (currentStreamJob?.isActive == false) {
                    currentStreamJob = null
                }
            }
        }
    }

    /**
     * Поиск по истории чата.
     * Включает режим поиска и показывает результаты вместо обычной истории.
     */
    fun searchInHistory(query: String, offset: Int = 0) {
        if (query.isBlank()) {
            clearSearch()
            return
        }

        currentSearchQuery = query

        viewModelScope.launch {
            _isSearching.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    chatRepository.searchHistory(query, offset)
                }

                result.onSuccess { searchResult ->
                    Log.d(TAG, "🔍 Найдено: total=${searchResult.totalMatches}, index=$offset")

                    _searchResults.value = searchResult.messages
                    _searchMatchedMessageId.value = searchResult.matchedMessageId
                    _searchTotalMatches.value = searchResult.totalMatches
                    _searchCurrentIndex.value = searchResult.currentMatchIndex
                    _searchHasNext.value = searchResult.hasNext

                    // Включаем режим поиска - UI будет показывать результаты поиска
                    if (searchResult.messages.isNotEmpty()) {
                        _searchResultMessages.value = searchResult.messages
                        _isInSearchMode.value = true
                    }
                }.onFailure { error ->
                    Log.e(TAG, "❌ Ошибка поиска: ${error.message}")
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

        if (!_searchHasNext.value) {
            Log.d(TAG, "⚠️ Больше результатов не найдено")
            _snackbarMessage.value = "Больше результатов не найдено"
            return
        }

        val nextOffset = _searchCurrentIndex.value + 1
        Log.d(TAG, "➡️ Переход к следующему результату: offset=$nextOffset")
        searchInHistory(currentSearchQuery, nextOffset)
    }

    /**
     * Сброс поиска и возврат к обычной истории.
     * Данные из Room автоматически восстановятся через Flow.
     */
    fun clearSearch() {
        Log.d(TAG, "🔄 Сброс поиска")
        currentSearchQuery = ""
        _searchResults.value = emptyList()
        _searchResultMessages.value = emptyList()
        _searchMatchedMessageId.value = null
        _searchTotalMatches.value = 0
        _searchCurrentIndex.value = 0
        _searchHasNext.value = false
        _isSearching.value = false
        _isInSearchMode.value = false
        // Данные из Room автоматически восстановятся через chatMessages Flow
    }

    /**
     * Очистка snackbar сообщения
     */
    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
    
    /**
     * Отправка системного события на бэкенд.
     * @param eventName Название события (например "food_flow_completed")
     */
    fun sendSystemEvent(eventName: String) {
        removeLastAssistantMessage()
        
        currentStreamJob?.cancel()
        Log.d(TAG, "🛑 Предыдущий стрим отменён (если был)")
        
        currentStreamJob = viewModelScope.launch {
            try {
                _isTyping.value = true
                
                Log.d(TAG, "📤 Отправка системного события: $eventName")
                
                // Создаём временное assistant сообщение
                val timestamp = System.currentTimeMillis() / 1000
                val assistantMessage = ChatMessage(
                    text = "",
                    isUser = false,
                    timestamp = timestamp,
                    id = null,
                    isSynced = false
                )
                
                val tempMessages = _temporaryMessages.value.toMutableList()
                tempMessages.add(assistantMessage)
                val messageIndex = tempMessages.size - 1
                _temporaryMessages.value = tempMessages
                
                Log.d(TAG, "➕ Добавлено временное assistant сообщение для события")
                
                val charQueue = Channel<Char>(Channel.UNLIMITED)
                
                val typingJob = launch {
                    var charCount = 0
                    for (char in charQueue) {
                        val messages = _temporaryMessages.value.toMutableList()
                        if (messageIndex < messages.size) {
                            messages[messageIndex] = messages[messageIndex].copy(
                                text = messages[messageIndex].text + char
                            )
                            _temporaryMessages.value = messages
                        }
                        
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
                        streamingApi = streamingApi,
                        sessionId = sessionId,
                        text = "",
                        geo = latestGeo,
                        images = emptyList(),
                        swipeMessageId = null,
                        systemEvent = eventName,
                        onChunkReceived = { chunk ->
                            for (char in chunk) {
                                charQueue.send(char)
                            }
                        },
                        onMetadataReceived = { _ -> }
                    )
                    
                    result.onFailure { error ->
                        Log.e(TAG, "❌ Ошибка стрима события: ${error.message}")
                    }
                }
                
                streamJob.join()
                charQueue.close()
                typingJob.join()
                
                Log.d(TAG, "🔄 syncLatestPage после системного события...")
                try {
                    val result = withContext(Dispatchers.IO) {
                        chatRepository.syncLatestPage(sessionId)
                    }
                    
                    result.onSuccess { response ->
                        Log.d(TAG, "✅ syncLatestPage: ${response.messages.size} сообщений")
                        oldestMessageId = response.oldestId
                        _oldestId.value = response.oldestId
                    }.onFailure { error ->
                        Log.e(TAG, "❌ Ошибка syncLatestPage: ${error.message}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Исключение при syncLatestPage: ${e.message}")
                }

                _temporaryMessages.value = emptyList()
                _isTyping.value = false
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Ошибка отправки системного события: ${e.message}")
                _isTyping.value = false
            } finally {
                if (currentStreamJob?.isActive == false) {
                    currentStreamJob = null
                }
            }
        }
    }
    
    /**
     * Обновление эмодзи-реакции на сообщение.
     * Отправляет на бэкенд и синхронизирует Room.
     * 
     * @param messageId ID сообщения в БД бэкенда
     * @param emoji Эмодзи для установки (null для удаления)
     */
    fun updateMessageEmoji(messageId: Int, emoji: String?) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "📤 Обновление эмодзи для сообщения $messageId: emoji=$emoji")

                val previousEmoji = withContext(Dispatchers.IO) {
                    chatRepository.getMessageByBackendId(messageId)?.emoji
                }

                // Локально обновляем сразу, чтобы UI не дергался
                withContext(Dispatchers.IO) {
                    chatRepository.updateEmojiLocal(messageId, emoji)
                }

                // Отправляем на бэкенд
                val result = withContext(Dispatchers.IO) {
                    try {
                        val request = com.example.victor_ai.data.network.UpdateEmojiRequest(
                            accountId = com.example.victor_ai.auth.UserProvider.getCurrentUserId(),
                            backendId = messageId,
                            emoji = emoji
                        )
                        val response = chatRepository.chatApi.updateEmoji(request)
                        Result.success(response)
                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                }

                result.onSuccess { response ->
                    Log.d(TAG, "✅ Эмодзи обновлено на бэкенде: ${response.message}")
                }.onFailure { error ->
                    Log.e(TAG, "❌ Ошибка обновления эмодзи: ${error.message}")
                    _snackbarMessage.value = "Ошибка обновления реакции"
                    // Откат локального изменения при ошибке
                    withContext(Dispatchers.IO) {
                        chatRepository.updateEmojiLocal(messageId, previousEmoji)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при обновлении эмодзи: ${e.message}")
                _snackbarMessage.value = "Ошибка обновления реакции"
            }
        }
    }
}
