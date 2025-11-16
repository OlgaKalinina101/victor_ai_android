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
    onLoadMoreHistory: suspend (Int) -> Pair<Boolean, Int?> = { false to null },
    visible: Boolean,
    isTyping: Boolean = false,
    onClose: () -> Unit = {},
    onStartVoiceRecognition: () -> Unit = {},
    isListeningState: Boolean = false,
    onStopListening: () -> Unit = {}
) {
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
        try {
            val result = ChatHistoryHelper.repository.syncWithBackendPaginated()
            result.onSuccess { response ->
                onInitHistory(response.messages)
                onPaginationInfo(response.oldestId, response.hasMore)
                hasMoreHistory = response.hasMore
                oldestId = response.oldestId
            }.onFailure { e ->
                Log.e("Chat", "Ошибка загрузки истории", e)
            }
        } catch (e: Exception) {
            Log.e("Chat", "Ошибка загрузки истории", e)
        }
    }

    // Отслеживание скролла для загрузки истории
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == null || isLoadingMore || !hasMoreHistory) return@collect

                val totalItems = listState.layoutInfo.totalItemsCount

                // Если прокрутили близко к концу списка (который в reverse = начало истории)
                if (totalItems > 0 && lastVisibleIndex >= totalItems - 3) {
                    val currentOldestId = oldestId
                    if (currentOldestId == null) {
                        Log.w("Chat", "⚠️ oldestId == null, загрузка невозможна")
                        hasMoreHistory = false
                        return@collect
                    }

                    isLoadingMore = true
                    try {
                        Log.d("Chat", "📥 Триггер загрузки: oldestId=$currentOldestId")
                        val (stillHasMore, newOldestId) = onLoadMoreHistory(currentOldestId)
                        hasMoreHistory = stillHasMore
                        oldestId = newOldestId
                        Log.d("Chat", "✅ Обновлен oldestId: $newOldestId, hasMore=$stillHasMore")
                    } catch (e: Exception) {
                        Log.e("Chat", "Ошибка загрузки истории", e)
                        hasMoreHistory = false
                    } finally {
                        isLoadingMore = false
                    }
                }
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
            // Header
            ChatHeader(
                onMenuClick = { showMenu = true },
                onSearchClick = { showSearchOverlay = true },
                currentMode = currentMode
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
                // Индикатор печати
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

                // Сообщения
                items(messages) { message ->
                    val actualIndex = messages.indexOf(message)
                    val isEditing = editingMessageIndex == actualIndex

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
                        }
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

        // Оверлей поиска
        if (showSearchOverlay) {
            SearchOverlay(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                onClose = {
                    showSearchOverlay = false
                    searchQuery = ""
                }
            )
        }
    }
}
