package com.example.victor_ai.ui.chat

import android.content.Intent
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp


import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.victor_ai.data.network.sendToDiaryEntry
import com.example.victor_ai.logic.fetchChatHistory
import com.example.victor_ai.model.ChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.example.victor_ai.data.models.UpdateHistoryRequest
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.ui.components.LongClickableText

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onInitHistory: (List<ChatMessage>) -> Unit,
    visible: Boolean,
    isTyping: Boolean = false
) {
    var userInput by remember { mutableStateOf("") }
    var editingMessageIndex by remember { mutableStateOf<Int?>(null) } // 👈 индекс редактируемого
    var editingText by remember { mutableStateOf("") } // 👈 текст в поле редактирования
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var copiedMessageText by remember { mutableStateOf<String?>(null) }

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            // 🔹 Список сообщений
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp),
                reverseLayout = true
            ) {

                // 🔹 Индикатор печати (если включён)
                if (isTyping) {
                    item {
                        TypingIndicator()
                    }
                }

                items(messages.reversed()) { message ->
                    val actualIndex = messages.size - 1 - messages.reversed().indexOf(message)
                    val bgColor = if (message.isUser) Color(0xFF3A3A3C) else Color(0xFF2C2C2E)
                    val alignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
                    val isEditing = editingMessageIndex == actualIndex

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = alignment
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(bgColor, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                if (isEditing) {
                                    // 🔹 РЕЖИМ РЕДАКТИРОВАНИЯ
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()  // ← растягиваем на всю ширину родителя
                                            .padding(horizontal = 8.dp)  // отступы от краёв (опционально)
                                    ) {
                                        OutlinedTextField(
                                            value = editingText,
                                            onValueChange = { editingText = it },
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
                                            TextButton(
                                                onClick = {
                                                    editingMessageIndex = null
                                                    editingText = ""
                                                }
                                            ) {
                                                Text("Отмена", color = Color.Gray, fontSize = 14.sp)
                                            }

                                            Spacer(modifier = Modifier.width(4.dp))

                                            TextButton(
                                                onClick = {
                                                    if (editingText.isNotBlank()) {
                                                        Log.d("ChatBox", "Сохраняем: index=$actualIndex, text=$editingText")
                                                        onEditMessage(actualIndex, editingText)
                                                        editingMessageIndex = null
                                                        editingText = ""
                                                    }
                                                }
                                            ) {
                                                Text("✓", color = Color(0xFFBB86FC), fontSize = 18.sp)
                                            }
                                        }
                                    }
                                } else {
                                    // 🔹 ОБЫЧНЫЙ РЕЖИМ - используем наш кастомный компонент
                                    LongClickableText(
                                        text = parseMarkdown(message.text),
                                        onLongClick = {
                                            Log.d("ChatBox", "🔥 Долгое нажатие на сообщение! index=$actualIndex")
                                            editingMessageIndex = actualIndex
                                            editingText = message.text
                                        },
                                        style = TextStyle(
                                            fontSize = 15.sp,
                                            color = Color(0xFFE0E0E0)
                                        )
                                    )
                                }
                            }

                            // 🔹 Кнопка копирования
                            if (!isEditing) {
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    contentDescription = "Копировать",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable {
                                            clipboardManager.setText(AnnotatedString(message.text))
                                            copiedMessageText = message.text
                                            coroutineScope.launch {
                                                delay(1000)
                                                copiedMessageText = null
                                            }
                                        }
                                        .padding(4.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // 🔹 Поле ввода
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .pointerInput(Unit) {
                        // Блокируем тапы по панели ввода
                        detectTapGestures(onTap = { /* ничего */ })
                    },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
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
                        Text("Напишите сообщение...", color = Color.Gray)
                    }
                )
                Button(
                    onClick = {
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
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2B2929),
                        contentColor = Color.White
                    )
                ) {
                    Text("➤")
                }
            }
        }
    }
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

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF2C2C2E), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    TypingDot(delay = index * 200)
                }
            }
        }
    }
}

@Composable
fun TypingDot(delay: Int) {
    var alpha by remember { mutableStateOf(0.3f) }
    var scale by remember { mutableStateOf(0.8f) }

    // Анимация прозрачности
    LaunchedEffect(Unit) {
        while (true) {
            animate(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = tween(500, delayMillis = delay)
            ) { value, _ ->
                alpha = value
            }
            animate(
                initialValue = 1f,
                targetValue = 0.3f,
                animationSpec = tween(500)
            ) { value, _ ->
                alpha = value
            }
        }
    }

    // Анимация масштаба
    LaunchedEffect(Unit) {
        while (true) {
            animate(
                initialValue = 0.8f,
                targetValue = 1.2f,
                animationSpec = tween(500, delayMillis = delay)
            ) { value, _ ->
                scale = value
            }
            animate(
                initialValue = 1.2f,
                targetValue = 0.8f,
                animationSpec = tween(500)
            ) { value, _ ->
                scale = value
            }
        }
    }

    Box(
        modifier = Modifier
            .size((8 * scale).dp)
            .background(
                Color.White.copy(alpha = alpha),
                CircleShape
            )
    )
}




