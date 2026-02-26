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

package com.example.victor_ai.ui.chat.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.ui.chat.utils.formatTimestamp
import com.example.victor_ai.ui.chat.utils.parseMarkdown
import com.example.victor_ai.ui.chat.utils.highlightSearchText
import com.example.victor_ai.ui.common.LongClickableText

/**
 * Элемент сообщения в чате
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
    onCopy: () -> Unit,
    onEmojiSelected: (String?) -> Unit = {},
    onSwipedMessageClick: (Int) -> Unit = {},
    searchQuery: String = "",
    isHighlighted: Boolean = false
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))
    val context = LocalContext.current
    // 🔥 Важно: используем ключ с message.id + message.emoji для правильного запоминания состояния
    var showEmojiPicker by remember(message.id, message.emoji) { mutableStateOf(false) }

    // User-сообщения справа и светлее фона
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isUser) Color(0xFF3A3838) else Color.Transparent

    // Применяем markdown и поисковую подсветку (с кэшированием)
    val annotatedText = remember(message.text, searchQuery) {
        parseMarkdown(message.text).let { parsed ->
            if (searchQuery.isNotBlank()) {
                highlightSearchText(parsed, searchQuery)
            } else {
                parsed
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = alignment
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
                    textStyle = TextStyle(fontSize = 15.sp, fontFamily = didactGothicFont),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancelEdit) {
                        Text("Отмена", color = Color.Gray, fontSize = 14.sp, fontFamily = didactGothicFont)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = onSaveEdit) {
                        Text("✓", color = Color(0xFFBB86FC), fontSize = 18.sp, fontFamily = didactGothicFont)
                    }
                }
            }
        } else {
            // Обычный режим
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(
                            max = if (message.isUser) 320.dp else 380.dp
                        )
                        .background(backgroundColor, RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    // 🔁 "Приклеенное" превью свайпа (если есть)
                    if (message.swipedMessageId != null) {
                        Surface(
                            color = Color(0xFF2C2C2E),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onSwipedMessageClick(message.swipedMessageId)
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Свайп к #${message.swipedMessageId}",
                                    fontSize = 12.sp,
                                    color = Color(0xFFBB86FC),
                                    fontFamily = didactGothicFont
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = message.swipedMessageText ?: "Открыть сообщение",
                                    fontSize = 13.sp,
                                    color = Color(0xFFCCCCCC),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontFamily = didactGothicFont
                                )
                            }
                        }
                    }

                    // Превью изображений (если есть)
                    if (message.imageCount > 0) {
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = "Прикрепленные изображения",
                                tint = Color(0xFFBB86FC),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${message.imageCount}",
                                fontSize = 12.sp,
                                color = Color(0xFFBB86FC),
                                fontFamily = didactGothicFont
                            )
                        }
                    }

                    // Метка vision context (если есть)
                    if (message.visionContext != null) {
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = "",
                                tint = Color(0xFFBB86FC),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "",
                                fontSize = 12.sp,
                                color = Color(0xFFBB86FC),
                                fontFamily = didactGothicFont
                            )
                        }
                    }

                    // Текст сообщения
                    if (currentMode == "edit mode") {
                        // В edit mode включаем долгий тап для редактирования
                        LongClickableText(
                            text = annotatedText,
                            onLongClick = onStartEdit,
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color(0xFFE0E0E0),
                                fontFamily = didactGothicFont
                            )
                        )
                    } else {
                        // В production mode: ссылки кликабельны + жесты ChatBox работают
                        val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

                        BasicText(
                            text = annotatedText,
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color(0xFFE0E0E0),
                                fontFamily = didactGothicFont
                            ),
                            onTextLayout = { layoutResult.value = it },
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { offset ->
                                        layoutResult.value?.let { layout ->
                                            val position = layout.getOffsetForPosition(offset)

                                            // Расширяем область поиска ссылки - проверяем ±8 символов вокруг клика
                                            val searchRange = 8
                                            val startPos = (position - searchRange).coerceAtLeast(0)
                                            val endPos = (position + searchRange).coerceAtMost(annotatedText.length)

                                            val annotations = annotatedText.getStringAnnotations(
                                                tag = "URL",
                                                start = startPos,
                                                end = endPos
                                            )

                                            if (annotations.isNotEmpty()) {
                                                // Клик на ссылку - открываем URL
                                                val url = annotations.first().item
                                                val intent = if (url.contains("openstreetmap.org")) {
                                                    val latRegex = """mlat=([-\d.]+)""".toRegex()
                                                    val lonRegex = """mlon=([-\d.]+)""".toRegex()
                                                    val lat = latRegex.find(url)?.groupValues?.get(1)
                                                    val lon = lonRegex.find(url)?.groupValues?.get(1)

                                                    if (lat != null && lon != null) {
                                                        Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lon?q=$lat,$lon"))
                                                    } else {
                                                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                    }
                                                } else {
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                }
                                                context.startActivity(intent)
                                            } else {
                                                // Клик вне ссылки - ничего не делаем
                                                // (закрытие чата теперь делается свайпом вниз в ChatBox)
                                            }
                                        }
                                    }
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Timestamp + кнопки (эмодзи + копирование)
                    // Показываем только если сообщение не пустое (стрим завершен)
                    if (message.text.isNotBlank()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Выбранное эмодзи (если есть)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                if (message.emoji != null) {
                                    Text(
                                        text = message.emoji,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                }
                                Text(
                                    text = formatTimestamp(message.timestamp),
                                    fontSize = 12.sp,
                                    color = Color(0xFF888888),
                                    fontFamily = didactGothicFont
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Кнопка выбора реакции (только для assistant сообщений с backendId)
                                if (!message.isUser && message.id != null) {
                                    Icon(
                                        imageVector = Icons.Outlined.AddReaction,
                                        contentDescription = "Добавить реакцию",
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                showEmojiPicker = true
                                            },
                                        tint = Color(0xFF666666)
                                    )
                                }

                                // Кнопка копирования
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
        }
    }

    // Диалог выбора эмодзи
    if (showEmojiPicker) {
        EmojiPickerDialog(
            currentEmoji = message.emoji,
            onEmojiSelected = { emoji ->
                onEmojiSelected(emoji)
            },
            onDismiss = { showEmojiPicker = false }
        )
    }
}
