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
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.platform.LocalDensity
import com.example.victor_ai.R
import com.example.victor_ai.logic.carebank.CareBankCommandHandler
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.ui.chat.components.*
import com.example.victor_ai.utils.ImageUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.snapshotFlow
import com.example.victor_ai.ui.components.carebank.ui.WebViewSheet
import com.example.victor_ai.ui.components.carebank.ui.SearchScenario
import androidx.compose.ui.zIndex

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
    onSendMessage: (String, List<ImageUtils.ImageAttachment>, Int?) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onPaginationInfo: (oldestId: Int?, hasMore: Boolean) -> Unit = { _, _ -> },
    onLoadMoreHistory: suspend (Int) -> Result<Triple<Boolean, Int?, Boolean>> = { Result.failure(Exception("Not implemented")) },
    onSearch: (String) -> Unit = {},
    onSearchNext: () -> Unit = {},
    onClearSearch: () -> Unit = {},
    searchMatchedMessageId: Int? = null,
    visible: Boolean,
    isTyping: Boolean = false,
    isLoadingMore: Boolean = false, // 🔥 Теперь приходит из VM
    hasMoreHistory: Boolean = true, // 🔥 Теперь приходит из VM
    oldestIdState: State<Int?>, // 🔥 State для реактивного обновления!
    onClose: () -> Unit = {},
    onStartVoiceRecognition: () -> Unit = {},
    isListeningState: Boolean = false,
    onStopListening: () -> Unit = {},
    careBankCommandHandler: CareBankCommandHandler? = null,
    careBankWebViewUrl: String? = null, // URL для Care Bank WebView от бэкенда
    careBankAutomationData: Map<String, String> = emptyMap(), // Данные автоматизации от бэкенда
    onCloseCareBankWebView: () -> Unit = {}, // Callback для закрытия WebView
    careBankRepository: com.example.victor_ai.data.repository.CareBankRepository? = null, // Repository для автоматизации
    careBankApi: com.example.victor_ai.data.network.CareBankApi? = null, // API для Care Bank автоматизации
    onAddChatMessage: (String) -> Unit = {}, // Callback для добавления сообщения в чат
    onSendSystemEvent: (String) -> Unit = {}, // Callback для отправки системного события
    onUpdateEmoji: (Int, String?) -> Unit = { _, _ -> }, // Callback для обновления эмодзи
    onHandleCareBankCommand: (String) -> Unit = {} // 🔥 Callback для обработки команд Care Bank (теперь в VM)
) {
    // Логируем сколько сообщений получает ChatBox
    Log.d("ChatBox", "🔵 ChatBox recompose: получено ${messages.size} сообщений, isTyping=$isTyping")
    if (messages.isNotEmpty()) {
        Log.d("ChatBox", "📝 Первые 3 сообщения: ${messages.take(3).map { "id=${it.id}, isUser=${it.isUser}, text=${it.text.take(20)}" }}")
    }
    var userInput by remember { mutableStateOf("") }
    // 🔥 ОПТИМИЗАЦИЯ: Храним уникальный ключ вместо индекса (избегаем indexOf)
    var editingMessageKey by remember { mutableStateOf<String?>(null) }
    var editingText by remember { mutableStateOf("") }
    var attachedImages by remember { mutableStateOf<List<ImageUtils.ImageAttachment>>(emptyList()) }
    var swipeMessageId by remember { mutableStateOf<Int?>(null) }
    var swipeMessagePreview by remember { mutableStateOf<String?>(null) }
    var jumpHighlightId by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope() // 🔥 Для UI операций (скролл, анимации)
    val clipboardManager = LocalClipboardManager.current
    var showMenu by remember { mutableStateOf(false) }
    var currentMode by remember { mutableStateOf("production") }
    var showSearchOverlay by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    // 🔥 oldestId теперь приходит из VM как параметр (было локальное состояние)
    
    // 🔥 context удалён - больше не нужен (Care Bank команды теперь в VM)
    var showWebView by remember { mutableStateOf(false) }
    var webViewUrl by remember { mutableStateOf("") }
    var webViewSearchQuery by remember { mutableStateOf("") } // Запрос для автоматизации

    // 🔥 ОПТИМИЗАЦИЯ: Загрузка истории теперь в VM init блоке (было в LaunchedEffect)
    
    // 📜 Автоскролл вниз при отправке нового сообщения
    // 🔥 ВАЖНО: Отслеживаем количество НЕСИНХРОНИЗИРОВАННЫХ сообщений (только новые!)
    val unsyncedCount = remember(messages) { messages.count { !it.isSynced } }
    
    LaunchedEffect(unsyncedCount) {
        // Скроллим только если:
        // 1. Есть несинхронизированные сообщения (новые от пользователя/ассистента)
        // 2. Не в режиме поиска (searchMatchedMessageId == null)
        if (unsyncedCount > 0 && searchMatchedMessageId == null) {
            // Даем время на рендеринг нового сообщения
            delay(100)
            
            // Скроллим к индексу 0 (самое новое сообщение, так как reverseLayout = true)
            listState.animateScrollToItem(0)
            Log.d("ChatBox", "📜 Автоскролл к новому сообщению (unsyncedCount=$unsyncedCount)")
        }
    }
    
    // 🎯 Кнопка "вернуться вниз" - показываем когда скроллим вверх по истории
    val showScrollToBottom by remember {
        derivedStateOf {
            // Показываем если скролл не внизу (firstVisibleItemIndex > 5)
            // reverseLayout: index 0 = самое новое сообщение (внизу)
            listState.firstVisibleItemIndex > 5
        }
    }

    // 🔍 Автоскролл к найденному сообщению при поиске
    LaunchedEffect(searchMatchedMessageId) {
        searchMatchedMessageId?.let { matchedId ->
            Log.d("ChatBox", "🎯 Автоскролл к сообщению: matched_id=$matchedId")

            // Логируем все сообщения ДО сортировки
            Log.d("ChatBox", "📋 Сообщения ДО сортировки: ${messages.filter { it.isSynced }.map { "id=${it.id}" }}")

            // Находим индекс сообщения в списке ПОСЛЕ сортировки (как в рендеринге)
            val syncedMessages = messages.filter { it.isSynced }.sortedByDescending { it.id }

            // Логируем все сообщения ПОСЛЕ сортировки
            Log.d("ChatBox", "📋 Сообщения ПОСЛЕ сортировки: ${syncedMessages.map { "id=${it.id}" }}")

            val messageIndex = syncedMessages.indexOfFirst { it.id == matchedId }

            if (messageIndex != -1) {
                // Учитываем несинхронизированные сообщения и индикатор печати
                val unsyncedCount = messages.count { !it.isSynced }
                val typingIndicatorCount = if (isTyping) 1 else 0
                val actualIndex = typingIndicatorCount + unsyncedCount + messageIndex

                Log.d("ChatBox", "📍 Найдено: messageIndex в synced=$messageIndex, actualIndex в LazyColumn=$actualIndex")
                Log.d("ChatBox", "📊 Breakdown: typing=$typingIndicatorCount, unsynced=$unsyncedCount, messageIndex=$messageIndex")
                Log.d("ChatBox", "🔍 Сообщение на позиции $messageIndex: id=${syncedMessages[messageIndex].id}, text=${syncedMessages[messageIndex].text.take(50)}")

                // Даем время на рендеринг списка
                kotlinx.coroutines.delay(100)

                // Вычисляем offset для центрирования
                // Получаем высоту viewport и центрируем элемент
                val viewportHeight = listState.layoutInfo.viewportSize.height
                val centerOffset = -(viewportHeight / 2)

                Log.d("ChatBox", "📐 Viewport height: $viewportHeight, center offset: $centerOffset")

                // Скроллим к элементу с центрированием
                listState.animateScrollToItem(actualIndex, scrollOffset = centerOffset)

                Log.d("ChatBox", "✅ Скролл выполнен к индексу $actualIndex с центрированием")
            } else {
                Log.w("ChatBox", "⚠️ Сообщение с id=$matchedId НЕ НАЙДЕНО в списке!")
                Log.w("ChatBox", "⚠️ Доступные ID: ${syncedMessages.map { it.id }}")
            }
        }
    }

    // Отслеживание скролла для загрузки истории
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == null || isLoadingMore || !hasMoreHistory) {
                    // Логируем только если НЕ из-за null
                    if (lastVisibleIndex != null && (isLoadingMore || !hasMoreHistory)) {
                        Log.d("ChatBox", "⏸️ Пагинация остановлена: isLoadingMore=$isLoadingMore, hasMoreHistory=$hasMoreHistory")
                    }
                    return@collect
                }

                val totalItems = listState.layoutInfo.totalItemsCount

                // Если прокрутили близко к концу списка (который в reverse = начало истории)
                if (totalItems > 0 && lastVisibleIndex >= totalItems - 3) {
                    // 🔥 ВАЖНО: Получаем текущее значение из State!
                    val currentOldestId = oldestIdState.value
                    if (currentOldestId == null) {
                        Log.w("ChatBox", "⚠️ oldestId == null, загрузка невозможна")
                        return@collect
                    }

                    Log.d("ChatBox", "📜 Триггер загрузки: lastVisible=$lastVisibleIndex, total=$totalItems, oldestId=$currentOldestId")

                    // 🔥 isLoadingMore теперь управляется в VM
                    try {
                        Log.d("ChatBox", "📥 Начало загрузки истории: oldestId=$currentOldestId")
                        val result = onLoadMoreHistory(currentOldestId)

                        result.onSuccess { (stillHasMore, newOldestId, _) ->
                            // Успешная загрузка - oldestId обновляется в VM автоматически
                            Log.d("ChatBox", "✅ Загрузка завершена: newOldestId=$newOldestId, hasMore=$stillHasMore")
                        }.onFailure { error ->
                            // Ошибка сети - НЕ останавливаем пагинацию!
                            // Пользователь может попробовать снова при следующем скролле
                            Log.w("ChatBox", "⚠️ Временная ошибка загрузки: ${error.message}. Пагинация доступна при следующем скролле")
                        }
                    } catch (e: Exception) {
                        Log.e("ChatBox", "❌ Неожиданная ошибка при пагинации", e)
                    }
                }
            }
    }

    // Поиск с debounce
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            // Debounce: ждём 500ms перед поиском
            delay(500)
            Log.d("ChatBox", "🔍 Запуск поиска: query='$searchQuery'")
            onSearch(searchQuery)
        } else if (showSearchOverlay) {
            // Если поле очищено - сбрасываем поиск
            Log.d("ChatBox", "🔄 Сброс поиска (пустой запрос)")
            onClearSearch()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
                .then(
                    // Поднимаем поле ввода над клавиатурой ТОЛЬКО когда WebView закрыт
                    // Когда WebView открыт - не поднимаем, чтобы он мог полностью открыться
                    if (!showWebView) {
                        Modifier.windowInsetsPadding(WindowInsets.ime)
                    } else {
                        Modifier
                    }
                )
                .then(
                    // Жесты работают только в production mode
                    if (currentMode == "production") {
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    Log.d("ChatBox", "🎤 LONG TAP -> микрофон")
                                    onStartVoiceRecognition()
                                },
                                onPress = {
                                    tryAwaitRelease()
                                    if (isListeningState) {
                                        onStopListening()
                                    }
                                }
                            )
                        }
                    } else {
                        Modifier
                    }
                )
        ) {
            // "Ручка" как у шторки: свайп вниз закрывает чат (в production режиме)
            if (currentMode == "production" && !showWebView) {
                val closeThresholdPx = with(LocalDensity.current) { 96.dp.toPx() }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .pointerInput(Unit) {
                            var dragSum = 0f
                            detectVerticalDragGestures(
                                onVerticalDrag = { _, dragAmount ->
                                    // учитываем только свайп вниз
                                    if (dragAmount > 0) dragSum += dragAmount
                                },
                                onDragEnd = {
                                    if (dragSum > closeThresholdPx) {
                                        Log.d("ChatBox", "⬇️ Swipe down -> close chat")
                                        onClose()
                                    }
                                    dragSum = 0f
                                },
                                onDragCancel = { dragSum = 0f }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(4.dp)
                            .background(Color(0xFF444444), RoundedCornerShape(2.dp))
                    )
                }
            }

            // Header - с встроенным поиском
            ChatHeader(
                onMenuClick = { showMenu = true },
                onSearchClick = {
                    showSearchOverlay = !showSearchOverlay
                    if (!showSearchOverlay) {
                        searchQuery = "" // Очищаем при закрытии
                        onClearSearch() // Сбрасываем поиск и возвращаем обычную историю
                    }
                },
                onResetClick = {
                    showMenu = false
                    showSearchOverlay = false
                    searchQuery = ""
                    onClearSearch()
                    editingMessageKey = null
                    editingText = ""
                    swipeMessageId = null
                    swipeMessagePreview = null
                    jumpHighlightId = null
                    coroutineScope.launch {
                        listState.animateScrollToItem(0) // reverseLayout: 0 = внизу
                        Log.d("ChatBox", "🔄 Сброс чата -> скролл к концу переписки")
                    }
                },
                currentMode = currentMode,
                isSearchMode = showSearchOverlay,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onNextSearchResult = {
                    Log.d("ChatBox", "➡️ Клик на стрелку - следующий результат")
                    onSearchNext()
                }
            )

            HorizontalDivider(thickness = 1.dp, color = Color(0xFF333333))

    // 🔥 ОПТИМИЗАЦИЯ: Функция для получения уникального ключа сообщения
    // Важно: ключ должен меняться при изменении emoji, чтобы Compose рекомпозил item
    val getMessageKey = remember {
        { message: ChatMessage ->
            if (message.isSynced && message.id != null) {
                "synced_${message.id}_${message.emoji.hashCode()}"
            } else {
                "unsynced_${message.timestamp}_${if (message.isUser) "user" else "assistant"}_${message.emoji.hashCode()}"
            }
        }
    }
    
    // 🔥 ОПТИМИЗАЦИЯ: Кэшируем фильтрацию и сортировку, чтобы не выполнять при каждой рекомпозиции
    val unsyncedMessages = remember(messages) {
        messages
            .filter { !it.isSynced }
            .sortedWith(
                compareByDescending<ChatMessage> { it.timestamp }
                    .thenBy { if (it.isUser) 1 else 0 }
            )
    }

    val syncedMessages = remember(messages) {
        messages
            .filter { it.isSynced }
            .sortedByDescending { it.id }
    }

            // Чтобы корутины (клик по превью) видели актуальные значения
            val latestMessages by rememberUpdatedState(messages)
            val latestHasMoreHistory by rememberUpdatedState(hasMoreHistory)
            val latestIsTyping by rememberUpdatedState(isTyping)
            val jumpScrollTopPaddingPx = with(LocalDensity.current) { 12.dp.toPx().toInt() }

            suspend fun tryScrollToBackendMessageId(targetId: Int): Boolean {
                val currentMessages = latestMessages
                val synced = currentMessages
                    .filter { it.isSynced && it.id != null }
                    .sortedByDescending { it.id }

                val messageIndex = synced.indexOfFirst { it.id == targetId }
                if (messageIndex == -1) return false

                val unsyncedCountNow = currentMessages.count { !it.isSynced }
                val typingIndicatorCount = if (latestIsTyping) 1 else 0
                val actualIndex = typingIndicatorCount + unsyncedCountNow + messageIndex

                // даём время на рендер (если список только что обновился)
                delay(120)

                // ⚠️ Важно: не центрируем. Для длинных сообщений центр/отрицательный offset
                // визуально выглядит как "прыжок в конец". Якорим на НАЧАЛО элемента.
                listState.animateScrollToItem(actualIndex, scrollOffset = jumpScrollTopPaddingPx)

                return true
            }

            fun onSwipedPreviewClick(targetId: Int) {
                coroutineScope.launch {
                    // 1) Пытаемся проскроллить по уже загруженным данным
                    if (tryScrollToBackendMessageId(targetId)) {
                        jumpHighlightId = targetId
                        delay(1500)
                        if (jumpHighlightId == targetId) jumpHighlightId = null
                        return@launch
                    }

                    // 2) Если сообщения ещё нет, пробуем догрузить историю
                    // Ограничение по попыткам — чтобы не зациклиться
                    repeat(12) { _ ->
                        val oldestIdNow = oldestIdState.value ?: return@launch
                        val hasMoreNow = latestHasMoreHistory

                        // Если сообщение НЕ старше oldestId — оно не появится при загрузке "ещё старее"
                        if (!hasMoreNow || targetId >= oldestIdNow) return@launch

                        onLoadMoreHistory(oldestIdNow)
                            .onFailure { return@launch }

                        // ждём обновления StateFlow -> recomposition
                        delay(200)

                        if (tryScrollToBackendMessageId(targetId)) {
                            jumpHighlightId = targetId
                            delay(1500)
                            if (jumpHighlightId == targetId) jumpHighlightId = null
                            return@launch
                        }
                    }
                }
            }

            val swipeThresholdPx = with(androidx.compose.ui.platform.LocalDensity.current) { 72.dp.toPx() }

            // Сообщения
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                reverseLayout = true
            ) {
                // 🔥 Индикатор печати - ПЕРВЫЙ в списке = в самом конце чата (внизу)
                if (isTyping) {
                    item {
                        val didactGothicFont = FontFamily(Font(R.font.didact_gothic))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                "~ набирает ответ ~",
                                fontSize = 14.sp,
                                color = Color(0xFF888888),
                                fontStyle = FontStyle.Italic,
                                fontFamily = didactGothicFont
                            )
                        }
                    }
                }

                // 🔥 НЕСИНХРОНИЗИРОВАННЫЕ сообщения - показываются ВТОРЫМИ (внизу с reverseLayout)
                Log.d("ChatBox", "🔍 Несинхронизированных найдено: ${unsyncedMessages.size}")
                unsyncedMessages.forEach { Log.d("ChatBox", "  id=${it.id}, ts=${it.timestamp}, isUser=${it.isUser}, isSynced=${it.isSynced}, text=${it.text.take(20)}") }

                items(
                    items = unsyncedMessages,
                    key = { message -> getMessageKey(message) }
                ) { message ->
                    // 🔥 ОПТИМИЗАЦИЯ: Используем ключ вместо indexOf (O(1) вместо O(n))
                    val messageKey = getMessageKey(message)
                    val isEditing = editingMessageKey == messageKey

                    Log.d("ChatBox", "🎨 Рендерим НЕСИНХРОНИЗИРОВАННОЕ сообщение: id=${message.id}, isUser=${message.isUser}, text=${message.text.take(30)}")

                    Box(
                        modifier = Modifier.pointerInput(message.id) {
                            var dragSum = 0f
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { _, dragAmount ->
                                    dragSum += dragAmount
                                },
                                onDragEnd = {
                                    val canSwipe = message.isSynced && message.id != null
                                    if (canSwipe && kotlin.math.abs(dragSum) > swipeThresholdPx) {
                                        swipeMessageId = message.id
                                        swipeMessagePreview = message.text.take(120)
                                        Log.d("ChatBox", "👆 Swipe selected messageId=${message.id}")
                                    }
                                    dragSum = 0f
                                },
                                onDragCancel = { dragSum = 0f }
                            )
                        }
                    ) {
                        MessageItem(
                            message = message,
                            isEditing = isEditing,
                            editingText = editingText,
                            currentMode = currentMode,
                            onEditingTextChange = { editingText = it },
                            onStartEdit = {
                                editingMessageKey = messageKey
                                editingText = message.text
                            },
                            onCancelEdit = {
                                editingMessageKey = null
                                editingText = ""
                            },
                            onSaveEdit = {
                                if (editingText.isNotBlank()) {
                                    // Находим индекс в исходном списке только при сохранении
                                    val actualIndex = messages.indexOf(message)
                                    onEditMessage(actualIndex, editingText)
                                    editingMessageKey = null
                                    editingText = ""
                                }
                            },
                            onCopy = {
                                clipboardManager.setText(AnnotatedString(message.text))
                            },
                            onEmojiSelected = { emoji ->
                                message.id?.let { messageId ->
                                    onUpdateEmoji(messageId, emoji)
                                }
                            },
                            onSwipedMessageClick = { targetId ->
                                onSwipedPreviewClick(targetId)
                            },
                            searchQuery = searchQuery,
                            isHighlighted = message.id == searchMatchedMessageId || message.id == jumpHighlightId
                        )
                    }
                }

                // 🔥 СИНХРОНИЗИРОВАННЫЕ сообщения с бэкенда - показываются ПОСЛЕ (вверху с reverseLayout)
                items(
                    items = syncedMessages,
                    key = { message -> getMessageKey(message) }
                ) { message ->
                    // 🔥 ОПТИМИЗАЦИЯ: Используем ключ вместо indexOf (O(1) вместо O(n))
                    val messageKey = getMessageKey(message)
                    val isEditing = editingMessageKey == messageKey

                    Log.d("ChatBox", "🎨 Рендерим сообщение: id=${message.id}, isUser=${message.isUser}, text=${message.text.take(30)}")

                    Box(
                        modifier = Modifier.pointerInput(message.id) {
                            var dragSum = 0f
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { _, dragAmount ->
                                    dragSum += dragAmount
                                },
                                onDragEnd = {
                                    val canSwipe = message.isSynced && message.id != null
                                    if (canSwipe && kotlin.math.abs(dragSum) > swipeThresholdPx) {
                                        swipeMessageId = message.id
                                        swipeMessagePreview = message.text.take(120)
                                        Log.d("ChatBox", "👆 Swipe selected messageId=${message.id}")
                                    }
                                    dragSum = 0f
                                },
                                onDragCancel = { dragSum = 0f }
                            )
                        }
                    ) {
                        MessageItem(
                            message = message,
                            isEditing = isEditing,
                            editingText = editingText,
                            currentMode = currentMode,
                            onEditingTextChange = { editingText = it },
                            onStartEdit = {
                                editingMessageKey = messageKey
                                editingText = message.text
                            },
                            onCancelEdit = {
                                editingMessageKey = null
                                editingText = ""
                            },
                            onSaveEdit = {
                                if (editingText.isNotBlank()) {
                                    // Находим индекс в исходном списке только при сохранении
                                    val actualIndex = messages.indexOf(message)
                                    onEditMessage(actualIndex, editingText)
                                    editingMessageKey = null
                                    editingText = ""
                                }
                            },
                            onCopy = {
                                clipboardManager.setText(AnnotatedString(message.text))
                            },
                            onEmojiSelected = { emoji ->
                                message.id?.let { messageId ->
                                    onUpdateEmoji(messageId, emoji)
                                }
                            },
                            onSwipedMessageClick = { targetId ->
                                onSwipedPreviewClick(targetId)
                            },
                            searchQuery = searchQuery,
                            isHighlighted = message.id == searchMatchedMessageId || message.id == jumpHighlightId
                        )
                    }
                }

                // Индикатор загрузки истории
                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFFBB86FC),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = Color(0xFF333333))

            // Input панель
            if (swipeMessageId != null) {
                Surface(
                    color = Color(0xFF2C2C2E),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Свайп: #$swipeMessageId",
                            color = Color(0xFFBB86FC),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = swipeMessagePreview.orEmpty(),
                            color = Color(0xFFCCCCCC),
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                swipeMessageId = null
                                swipeMessagePreview = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Очистить swipe",
                                tint = Color(0xFF888888)
                            )
                        }
                    }
                }
            }
            ChatInputPanel(
                userInput = userInput,
                onInputChange = { userInput = it },
                onSend = {
                    // 🔒 Блокировка сообщений без текста (текст обязателен всегда)
                    if (userInput.isBlank()) {
                        Log.d("ChatBox", "⚠️ Попытка отправить сообщение без текста - игнорируем")
                        return@ChatInputPanel
                    }
                    
                    // Проверяем команды банка заботы (начинаются с /)
                    Log.d("ChatBox", "🔵 onSend вызван, userInput='$userInput'")
                    if (careBankCommandHandler != null && 
                        careBankCommandHandler.isCareBankCommand(userInput)) {
                        Log.d("ChatBox", "✅ Это команда банка заботы, обрабатываем...")
                        // Сохраняем значение перед очисткой!
                        val commandText = userInput
                        val searchQuery = userInput.trim().substring(1) // Убираем "/"
                        userInput = ""
                        attachedImages = emptyList()
                        swipeMessageId = null
                        swipeMessagePreview = null
                        
                        // 🔥 ОПТИМИЗАЦИЯ: Обработка теперь в VM вместо rememberCoroutineScope
                        onHandleCareBankCommand(commandText)
                        webViewSearchQuery = searchQuery // Сохраняем запрос для локального WebView
                        showWebView = true
                    } else {
                        // Обычная отправка сообщения (с текстом и/или изображениями)
                        onSendMessage(userInput, attachedImages, swipeMessageId)
                        userInput = ""
                        attachedImages = emptyList()
                        swipeMessageId = null
                        swipeMessagePreview = null
                    }
                },
                onAttachClick = { /* Обрабатывается внутри ChatInputPanel */ },
                attachedImages = attachedImages,
                onImagesAttached = { newImages ->
                    attachedImages = newImages.take(1)
                    Log.d("ChatBox", "📎 Прикреплено ${attachedImages.size} изображений")
                },
                onImageRemoved = { imageToRemove ->
                    attachedImages = attachedImages.filter { it != imageToRemove }
                    Log.d("ChatBox", "🗑️ Удалено изображение, осталось ${attachedImages.size}")
                },
                onLongPressSend = {
                    if (currentMode == "production") {
                        Log.d("ChatBox", "🎤 LONG TAP на кнопке отправки -> микрофон")
                        onStartVoiceRecognition()
                    }
                }
            )
        }
        
        // 🎯 Кнопка "вернуться к последнему сообщению" (scroll to bottom)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            ScrollToBottomButton(
                visible = showScrollToBottom,
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0) // reverseLayout: 0 = внизу
                        Log.d("ChatBox", "⬇️ Скролл к последнему сообщению")
                    }
                },
                modifier = Modifier.zIndex(1f) // Поверх сообщений
            )
        }

        // Меню режимов
        if (showMenu) {
            val didactGothicFont = FontFamily(Font(R.font.didact_gothic))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { showMenu = false }
                        )
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 72.dp)
                        .width(200.dp)
                        .background(Color(0xFF3A3A3C), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .pointerInput(Unit) {
                            // Блокируем все события
                            detectTapGestures(
                                onTap = { /* consume */ },
                                onLongPress = { /* consume */ },
                                onPress = { /* consume */ }
                            )
                        }
                ) {
                    Text(
                        text = "mode: $currentMode",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontFamily = didactGothicFont
                    )

                    ModeMenuItem(
                        text = "production",
                        isSelected = currentMode == "production",
                        onClick = {
                            currentMode = "production"
                            showMenu = false
                        }
                    )

                    ModeMenuItem(
                        text = "edit mode",
                        isSelected = currentMode == "edit mode",
                        onClick = {
                            currentMode = "edit mode"
                            showMenu = false
                        }
                    )
                }
            }
        }
        
        // WebView шторка для команд банка заботы (ручные команды с /)
        if (showWebView && webViewUrl.isNotEmpty()) {
            WebViewSheet(
                url = webViewUrl,
                onDismiss = {
                    showWebView = false
                    webViewUrl = ""
                    webViewSearchQuery = ""
                },
                enableAutomation = true, // Включаем автоматизацию для команд банка заботы
                automationScenario = SearchScenario(
                    name = "Care Bank Search: $webViewSearchQuery",
                    tapSearchYdp = 138, // Координата для тапа (может настраиваться с бэкенда)
                    tapAddYdp = 80, // Координата для добавления в корзину
                    searchText = webViewSearchQuery
                ),
                careBankApi = careBankApi
            )
        }
        
        // WebView шторка для Care Bank от бэкенда (автоматическое открытие по metadata)
        if (careBankWebViewUrl != null && careBankWebViewUrl.isNotEmpty()) {
            Log.d("ChatBox", "🌐 Открываем Care Bank WebView: url=$careBankWebViewUrl, data=$careBankAutomationData")
            
            WebViewSheet(
                url = careBankWebViewUrl,
                onDismiss = {
                    Log.d("ChatBox", "❌ Закрываем Care Bank WebView")
                    onCloseCareBankWebView()
                },
                enableAutomation = true,
                setupMode = false,
                emoji = "☕", // TODO: передавать правильный emoji из ChatViewModel
                repository = careBankRepository,
                careBankApi = careBankApi,
                onAddChatMessage = onAddChatMessage,
                onSendSystemEvent = { eventName ->
                    Log.d("ChatBox", "📤 Передача системного события: $eventName")
                    // Пробрасываем через callback который придет из верхних слоев
                    onSendSystemEvent(eventName)
                },
                automationData = careBankAutomationData
            )
        }
        
        } // Закрываем Box
    }
}
