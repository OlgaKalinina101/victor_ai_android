package com.example.victor_ai.ui.chat

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.foundation.interaction.MutableInteractionSource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp


import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.victor_ai.data.network.sendToDiaryEntry
import com.example.victor_ai.logic.fetchChatHistory
import com.example.victor_ai.domain.model.ChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.victor_ai.ui.common.LongClickableText

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onInitHistory: (List<ChatMessage>) -> Unit,
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
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var currentMode by remember { mutableStateOf("production") }
    var showSearchOverlay by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val history = fetchChatHistory()
            onInitHistory(history)
        } catch (e: Exception) {
            Log.e("Chat", "Ошибка загрузки истории", e)
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
            // ┌─────────────────────────────┐
            // │ Header: меню, заголовок, поиск
            // └─────────────────────────────┘
            ChatHeader(
                onMenuClick = { showMenu = true },
                onSearchClick = { showSearchOverlay = true },
                showMenu = showMenu,
                currentMode = currentMode,
                onModeChange = { mode ->
                    currentMode = mode
                    showMenu = false
                },
                onDismissMenu = { showMenu = false }
            )

            HorizontalDivider(thickness = 1.dp, color = Color(0xFF333333))

            // ┌─────────────────────────────┐
            // │ Сообщения
            // └─────────────────────────────┘
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                reverseLayout = true
            ) {
                // Индикатор печати
                if (isTyping) {
                    item {
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
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }

                // Сообщения
                items(messages.reversed()) { message ->
                    val actualIndex = messages.size - 1 - messages.reversed().indexOf(message)
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
            }

            HorizontalDivider(thickness = 1.dp, color = Color(0xFF333333))

            // ┌─────────────────────────────┐
            // │ Input панель
            // └─────────────────────────────┘
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

// ═══════════════════════════════════════════════
// Вспомогательные компоненты
// ═══════════════════════════════════════════════

/**
 * Header с меню, заголовком и поиском
 */
@Composable
fun ChatHeader(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    showMenu: Boolean,
    currentMode: String,
    onModeChange: (String) -> Unit,
    onDismissMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF2B2929))
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // [☰] Меню
            Box {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Меню",
                        tint = Color(0xFFE0E0E0)
                    )
                }

                // Кастомное меню режимов
                if (showMenu) {
                    Box(
                        modifier = Modifier
                            .padding(top = 48.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onDismissMenu()
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .background(Color(0xFF3A3A3C), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "mode: $currentMode",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            ModeMenuItem(
                                text = "production",
                                isSelected = currentMode == "production",
                                onClick = { onModeChange("production") }
                            )

                            ModeMenuItem(
                                text = "edit mode",
                                isSelected = currentMode == "edit mode",
                                onClick = { onModeChange("edit mode") }
                            )
                        }
                    }
                }
            }

            // Victor AI
            Text(
                text = "Victor AI",
                fontSize = 18.sp,
                color = Color(0xFFE0E0E0),
                fontWeight = FontWeight.Medium
            )

            // [🔍] Поиск
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = Color(0xFFE0E0E0)
                )
            }
        }
    }
}

/**
 * Элемент сообщения
 */
@Composable
fun MessageItem(
    message: ChatMessage,
    isEditing: Boolean,
    editingText: String,
    currentMode: String,
    onEditingTextChange: (String) -> Unit,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onCopy: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (isEditing) {
            // Режим редактирования
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2C2C2E), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFFBB86FC),
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color(0xFFBB86FC)
                    ),
                    textStyle = TextStyle(fontSize = 15.sp),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancelEdit) {
                        Text("Отмена", color = Color.Gray, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = onSaveEdit) {
                        Text("✓", color = Color(0xFFBB86FC), fontSize = 18.sp)
                    }
                }
            }
        } else {
            // Обычный режим
            Column(modifier = Modifier.fillMaxWidth()) {
                // Текст сообщения
                if (currentMode == "edit mode") {
                    // В edit mode включаем долгий тап для редактирования
                    LongClickableText(
                        text = parseMarkdown(message.text),
                        onLongClick = onStartEdit,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color(0xFFE0E0E0)
                        )
                    )
                } else {
                    // В production mode просто отображаем текст
                    Text(
                        text = parseMarkdown(message.text),
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color(0xFFE0E0E0)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Timestamp + кнопка копирования
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTimestamp(message.timestamp),
                        fontSize = 12.sp,
                        color = Color(0xFF888888)
                    )

                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = "Копировать",
                        modifier = Modifier
                            .size(18.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onCopy()
                            },
                        tint = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

/**
 * Панель ввода
 */
@Composable
fun ChatInputPanel(
    userInput: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onAttachClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2B2929))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [📎] Прикрепить
        IconButton(
            onClick = onAttachClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Прикрепить",
                tint = Color(0xFFE0E0E0)
            )
        }

        // Поле ввода
        OutlinedTextField(
            value = userInput,
            onValueChange = onInputChange,
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFFBB86FC)
            ),
            shape = RoundedCornerShape(20.dp),
            placeholder = {
                Text("текст...", color = Color.Gray, fontSize = 14.sp)
            }
        )

        // [▶] Отправить
        IconButton(
            onClick = onSend,
            modifier = Modifier.size(40.dp)
        ) {
            Text("▶", fontSize = 20.sp, color = Color(0xFFE0E0E0))
        }
    }
}

/**
 * Элемент меню режимов
 */
@Composable
fun ModeMenuItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isSelected) "> " else "  ",
            fontSize = 14.sp,
            color = Color(0xFFE0E0E0),
            modifier = Modifier.width(20.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFFE0E0E0)
        )
    }
}

/**
 * Оверлей поиска
 */
@Composable
fun SearchOverlay(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClose()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // Блокируем закрытие при клике на содержимое
                }
        ) {
            Text(
                text = "ПОИСК",
                fontSize = 20.sp,
                color = Color(0xFFE0E0E0),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF2C2C2E),
                    unfocusedContainerColor = Color(0xFF2C2C2E),
                    focusedIndicatorColor = Color(0xFFBB86FC),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFFBB86FC)
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text("Введите запрос...", color = Color.Gray)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "результаты... (в самом чате)",
                fontSize = 14.sp,
                color = Color(0xFF888888),
                fontStyle = FontStyle.Italic
            )
        }
    }
}

/**
 * Форматирование timestamp
 */
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        val lines = text.split("\\n")

        lines.forEachIndexed { lineIndex, line ->
            var lineIndex = 0

            // Регулярки для разных элементов markdown
            val boldRegex = """\*\*(.+?)\*\*""".toRegex()
            val italicRegex = """\*(.+?)\*""".toRegex()
            val linkRegex = """\[(.+?)\]\((.+?)\)""".toRegex()

            // Находим все совпадения
            val matches = mutableListOf<Pair<IntRange, MatchResult>>()
            boldRegex.findAll(line).forEach { matches.add(it.range to it) }
            italicRegex.findAll(line).forEach { matches.add(it.range to it) }
            linkRegex.findAll(line).forEach { matches.add(it.range to it) }

            // Сортируем по позиции
            matches.sortBy { it.first.first }

            var lastIndex = 0
            matches.forEach { (range, match) ->
                // Добавляем текст до совпадения
                if (lastIndex < range.first) {
                    withStyle(SpanStyle(color = Color(0xFFE0E0E0))) {
                        append(line.substring(lastIndex, range.first))
                    }
                }

                when {
                    // Жирный текст
                    match.value.startsWith("**") -> {
                        val innerText = match.groupValues[1]
                        withStyle(SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE0E0E0)
                        )) {
                            append(innerText)
                        }
                    }
                    // Курсив
                    match.value.startsWith("*") && !match.value.startsWith("**") -> {
                        val innerText = match.groupValues[1]
                        withStyle(SpanStyle(
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFA0A0A0)
                        )) {
                            append(innerText)
                        }
                    }
                    // Ссылки
                    match.value.startsWith("[") -> {
                        val label = match.groupValues[1]
                        val url = match.groupValues[2]
                        val start = length

                        withStyle(SpanStyle(
                            color = Color(0xFFBB86FC),
                            textDecoration = TextDecoration.Underline
                        )) {
                            append(label)
                        }

                        addStringAnnotation(
                            tag = "URL",
                            annotation = url,
                            start = start,
                            end = start + label.length
                        )
                    }
                }

                lastIndex = range.last + 1
            }

            // Остаток строки
            if (lastIndex < line.length) {
                withStyle(SpanStyle(color = Color(0xFFE0E0E0))) {
                    append(line.substring(lastIndex))
                }
            }

            // Перенос строки (кроме последней)
            if (lineIndex < lines.size - 1) {
                append("\n")
            }
        }
    }
}

// ═══════════════════════════════════════════════
// Конец файла
// ═══════════════════════════════════════════════




