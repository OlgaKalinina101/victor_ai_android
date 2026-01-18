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

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.domain.model.ChatMessage
import kotlinx.coroutines.delay

/**
 * Список сообщений чата с пагинацией
 */
@Composable
fun ChatMessagesList(
    messages: List<ChatMessage>,
    unsyncedMessages: List<ChatMessage>,
    syncedMessages: List<ChatMessage>,
    isTyping: Boolean,
    isLoadingMore: Boolean,
    listState: LazyListState,
    searchQuery: String,
    searchMatchedMessageId: Int?,
    editingMessageKey: String?,
    editingText: String,
    currentMode: String,
    getMessageKey: (ChatMessage) -> String,
    onEditingTextChange: (String) -> Unit,
    onStartEdit: (String, String) -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: (ChatMessage, String) -> Unit,
    onUpdateEmoji: (Int, String?) -> Unit,
    onClose: () -> Unit,
    onStartVoiceRecognition: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        reverseLayout = true
    ) {
        // 🔥 Индикатор печати - ПЕРВЫЙ в списке = в самом конце чата (внизу)
        if (isTyping) {
            item(key = "typing_indicator") {
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

        // 🔥 НЕСИНХРОНИЗИРОВАННЫЕ сообщения
        Log.d("ChatBox", "🔍 Несинхронизированных найдено: ${unsyncedMessages.size}")
        unsyncedMessages.forEach { 
            Log.d("ChatBox", "  id=${it.id}, ts=${it.timestamp}, isUser=${it.isUser}, isSynced=${it.isSynced}, text=${it.text.take(20)}") 
        }

        items(
            items = unsyncedMessages,
            key = { message -> getMessageKey(message) }
        ) { message ->
            val messageKey = getMessageKey(message)
            val isEditing = editingMessageKey == messageKey

            Log.d("ChatBox", "🎨 Рендерим НЕСИНХРОНИЗИРОВАННОЕ сообщение: id=${message.id}, isUser=${message.isUser}, text=${message.text.take(30)}")

            MessageItem(
                message = message,
                isEditing = isEditing,
                editingText = editingText,
                currentMode = currentMode,
                onEditingTextChange = onEditingTextChange,
                onStartEdit = {
                    onStartEdit(messageKey, message.text)
                },
                onCancelEdit = onCancelEdit,
                onSaveEdit = {
                    if (editingText.isNotBlank()) {
                        onSaveEdit(message, editingText)
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
                searchQuery = searchQuery,
                isHighlighted = message.id == searchMatchedMessageId
            )
        }

        // 🔥 СИНХРОНИЗИРОВАННЫЕ сообщения
        items(
            items = syncedMessages,
            key = { message -> getMessageKey(message) }
        ) { message ->
            val messageKey = getMessageKey(message)
            val isEditing = editingMessageKey == messageKey

            Log.d("ChatBox", "🎨 Рендерим сообщение: id=${message.id}, isUser=${message.isUser}, text=${message.text.take(30)}")

            MessageItem(
                message = message,
                isEditing = isEditing,
                editingText = editingText,
                currentMode = currentMode,
                onEditingTextChange = onEditingTextChange,
                onStartEdit = {
                    onStartEdit(messageKey, message.text)
                },
                onCancelEdit = onCancelEdit,
                onSaveEdit = {
                    if (editingText.isNotBlank()) {
                        onSaveEdit(message, editingText)
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
                searchQuery = searchQuery,
                isHighlighted = message.id == searchMatchedMessageId
            )
        }

        // Индикатор загрузки истории
        if (isLoadingMore) {
            item(key = "loading_indicator") {
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
}

