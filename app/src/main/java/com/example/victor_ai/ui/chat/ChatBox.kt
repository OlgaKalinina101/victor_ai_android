package com.example.victor_ai.ui.chat

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import com.example.victor_ai.R
import com.example.victor_ai.data.network.sendToDiaryEntry
import com.example.victor_ai.logic.ChatHistoryHelper
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.ui.chat.components.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.snapshotFlow

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onInitHistory: (List<ChatMessage>) -> Unit,
    onPaginationInfo: (oldestId: Int?, hasMore: Boolean) -> Unit = { _, _ -> },
    onLoadMoreHistory: suspend (Int) -> Result<Triple<Boolean, Int?, Boolean>> = { Result.failure(Exception("Not implemented")) },
    onSearch: (String) -> Unit = {},
    onSearchNext: () -> Unit = {},
    onClearSearch: () -> Unit = {},
    searchMatchedMessageId: Int? = null,
    visible: Boolean,
    isTyping: Boolean = false,
    onClose: () -> Unit = {},
    onStartVoiceRecognition: () -> Unit = {},
    isListeningState: Boolean = false,
    onStopListening: () -> Unit = {}
) {
    // Логируем сколько сообщений получает ChatBox
    Log.d("ChatBox", "🔵 ChatBox recompose: получено ${messages.size} сообщений, isTyping=$isTyping")
    if (messages.isNotEmpty()) {
        Log.d("ChatBox", "📝 Первые 3 сообщения: ${messages.take(3).map { "id=${it.id}, isUser=${it.isUser}, text=${it.text.take(20)}" }}")
    }
    var userInput by remember { mutableStateOf("") }
    var editingMessageIndex by remember { mutableStateOf<Int?>(null) }
    var editingText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    var showMenu by remember { mutableStateOf(false) }
    var currentMode by remember { mutableStateOf("production") }
    var showSearchOverlay by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var isLoadingMore by remember { mutableStateOf(false) }
    var hasMoreHistory by remember { mutableStateOf(true) }
    var oldestId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        Log.d("ChatBox", "🚀 LaunchedEffect(Unit) - начало первичной загрузки истории")
        try {
            val result = ChatHistoryHelper.repository.syncWithBackendPaginated()
            result.onSuccess { response ->
                Log.d("ChatBox", "📥 Получено ${response.messages.size} сообщений с бэкенда")
                Log.d("ChatBox", "📋 IDs полученных: ${response.messages.map { it.id }}")

                onInitHistory(response.messages)
                onPaginationInfo(response.oldestId, response.hasMore)
                hasMoreHistory = response.hasMore
                oldestId = response.oldestId

                Log.d("ChatBox", "✅ Первичная загрузка завершена: oldestId=$oldestId, hasMore=$hasMoreHistory")
            }.onFailure { e ->
                Log.e("ChatBox", "❌ Ошибка загрузки истории", e)
            }
        } catch (e: Exception) {
            Log.e("ChatBox", "❌ Ошибка загрузки истории", e)
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

                // Скроллим к элементу
                // Используем небольшой offset чтобы элемент был ближе к центру экрана
                kotlinx.coroutines.delay(100) // Даем время на рендеринг
                listState.animateScrollToItem(actualIndex, scrollOffset = -200)

                Log.d("ChatBox", "✅ Скролл выполнен к индексу $actualIndex")
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
                    val currentOldestId = oldestId
                    if (currentOldestId == null) {
                        Log.w("ChatBox", "⚠️ oldestId == null, загрузка невозможна")
                        hasMoreHistory = false
                        return@collect
                    }

                    Log.d("ChatBox", "📜 Триггер загрузки: lastVisible=$lastVisibleIndex, total=$totalItems, oldestId=$currentOldestId")

                    isLoadingMore = true
                    try {
                        Log.d("ChatBox", "📥 Начало загрузки истории: oldestId=$currentOldestId")
                        val result = onLoadMoreHistory(currentOldestId)

                        result.onSuccess { (stillHasMore, newOldestId, _) ->
                            // Успешная загрузка - обновляем состояние
                            hasMoreHistory = stillHasMore
                            oldestId = newOldestId
                            Log.d("ChatBox", "✅ Загрузка завершена: newOldestId=$newOldestId, hasMore=$stillHasMore")
                        }.onFailure { error ->
                            // Ошибка сети - НЕ останавливаем пагинацию!
                            // Пользователь может попробовать снова при следующем скролле
                            Log.w("ChatBox", "⚠️ Временная ошибка загрузки: ${error.message}. Пагинация доступна при следующем скролле")
                            // hasMoreHistory и oldestId остаются без изменений
                        }
                    } catch (e: Exception) {
                        Log.e("ChatBox", "❌ Неожиданная ошибка при пагинации", e)
                        // При неожиданной ошибке тоже не останавливаем
                    } finally {
                        isLoadingMore = false
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
                .then(
                    // Жесты работают только в production mode
                    if (currentMode == "production") {
                        Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    Log.d("ChatBox", "❌ TAP -> закрываем чат")
                                    onClose()
                                },
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
                // Фильтруем по флагу isSynced вместо манипуляций с ID
                val unsyncedMessages = messages
                    .filter { !it.isSynced }  // 🔥 Используем флаг вместо ID
                    .sortedWith(
                        compareByDescending<ChatMessage> { it.timestamp }
                            .thenBy { if (it.isUser) 1 else 0 }  // При равных timestamp: assistant первым → внизу, user вторым → вверху
                    )

                Log.d("ChatBox", "🔍 Несинхронизированных найдено: ${unsyncedMessages.size}")
                unsyncedMessages.forEach { Log.d("ChatBox", "  id=${it.id}, ts=${it.timestamp}, isUser=${it.isUser}, isSynced=${it.isSynced}, text=${it.text.take(20)}") }

                items(unsyncedMessages) { message ->
                    val actualIndex = messages.indexOf(message)
                    val isEditing = editingMessageIndex == actualIndex

                    Log.d("ChatBox", "🎨 Рендерим НЕСИНХРОНИЗИРОВАННОЕ сообщение: id=${message.id}, isUser=${message.isUser}, text=${message.text.take(30)}")

                    MessageItem(
                        message = message,
                        isEditing = isEditing,
                        editingText = editingText,
                        currentMode = currentMode,
                        onEditingTextChange = { editingText = it },
                        onStartEdit = {
                            editingMessageIndex = actualIndex
                            editingText = message.text
                        },
                        onCancelEdit = {
                            editingMessageIndex = null
                            editingText = ""
                        },
                        onSaveEdit = {
                            if (editingText.isNotBlank()) {
                                onEditMessage(actualIndex, editingText)
                                editingMessageIndex = null
                                editingText = ""
                            }
                        },
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(message.text))
                        },
                        onTapOutsideLink = {
                            if (currentMode == "production") {
                                Log.d("ChatBox", "❌ TAP вне ссылки -> закрываем чат")
                                onClose()
                            }
                        },
                        onLongPressOutsideLink = {
                            if (currentMode == "production") {
                                Log.d("ChatBox", "🎤 LONG TAP -> микрофон")
                                onStartVoiceRecognition()
                            }
                        },
                        searchQuery = searchQuery,
                        isHighlighted = message.id == searchMatchedMessageId
                    )
                }

                // 🔥 СИНХРОНИЗИРОВАННЫЕ сообщения с бэкенда - показываются ПОСЛЕ (вверху с reverseLayout)
                // Фильтруем по флагу isSynced вместо манипуляций с ID
                val syncedMessages = messages
                    .filter { it.isSynced }  // 🔥 Используем флаг вместо ID
                    .sortedByDescending { it.id }  // По убыванию: новые первыми, старые в конце = вверху

                items(syncedMessages) { message ->
                    val actualIndex = messages.indexOf(message)
                    val isEditing = editingMessageIndex == actualIndex

                    Log.d("ChatBox", "🎨 Рендерим сообщение [$actualIndex]: id=${message.id}, isUser=${message.isUser}, text=${message.text.take(30)}")

                    MessageItem(
                        message = message,
                        isEditing = isEditing,
                        editingText = editingText,
                        currentMode = currentMode,
                        onEditingTextChange = { editingText = it },
                        onStartEdit = {
                            editingMessageIndex = actualIndex
                            editingText = message.text
                        },
                        onCancelEdit = {
                            editingMessageIndex = null
                            editingText = ""
                        },
                        onSaveEdit = {
                            if (editingText.isNotBlank()) {
                                onEditMessage(actualIndex, editingText)
                                editingMessageIndex = null
                                editingText = ""
                            }
                        },
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(message.text))
                        },
                        onTapOutsideLink = {
                            if (currentMode == "production") {
                                Log.d("ChatBox", "❌ TAP вне ссылки -> закрываем чат")
                                onClose()
                            }
                        },
                        onLongPressOutsideLink = {
                            if (currentMode == "production") {
                                Log.d("ChatBox", "🎤 LONG TAP -> микрофон")
                                onStartVoiceRecognition()
                            }
                        },
                        searchQuery = searchQuery,
                        isHighlighted = message.id == searchMatchedMessageId
                    )
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
            ChatInputPanel(
                userInput = userInput,
                onInputChange = { userInput = it },
                onSend = {
                    if (userInput.isNotBlank()) {
                        if (userInput.startsWith("#Дневник", ignoreCase = true)) {
                            coroutineScope.launch {
                                sendToDiaryEntry(userInput)
                            }
                        } else {
                            onSendMessage(userInput)
                        }
                        userInput = ""
                    }
                },
                onAttachClick = { /* TODO: заглушка */ }
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
    }
}
