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

package com.example.victor_ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.logic.carebank.CareBankCommandHandler
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.chat.ChatBox
import com.example.victor_ai.utils.ImageUtils

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    onSendMessage: (String, List<ImageUtils.ImageAttachment>, Int?) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onPaginationInfo: (oldestId: Int?, hasMore: Boolean) -> Unit,
    onLoadMoreHistory: suspend (Int) -> Result<Triple<Boolean, Int?, Boolean>>,
    onSearch: (String) -> Unit,
    onSearchNext: () -> Unit,
    onClearSearch: () -> Unit,
    searchMatchedMessageId: Int?,
    onClose: () -> Unit,
    permissionManager: PermissionManager,
    isListeningState: State<Boolean>,
    isTypingState: State<Boolean>,
    isLoadingMoreState: State<Boolean>, // 🔥 Новый параметр из VM
    hasMoreHistoryState: State<Boolean>, // 🔥 Новый параметр из VM
    oldestIdState: State<Int?>, // 🔥 Новый параметр из VM для пагинации
    onStopListening: () -> Unit,
    careBankCommandHandler: CareBankCommandHandler? = null,
    careBankWebViewUrl: String? = null,
    careBankAutomationData: Map<String, String> = emptyMap(),
    onCloseCareBankWebView: () -> Unit = {},
    careBankRepository: com.example.victor_ai.data.repository.CareBankRepository? = null,
    careBankApi: com.example.victor_ai.data.network.CareBankApi? = null,
    onAddChatMessage: (String) -> Unit = {},
    onSendSystemEvent: (String) -> Unit = {},
    onUpdateEmoji: (Int, String?) -> Unit = { _, _ -> }
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2E))
    ) {
        // Чат с жестами на всей области
        ChatBox(
            messages = messages,
            onSendMessage = onSendMessage,
            onEditMessage = onEditMessage,
            onPaginationInfo = onPaginationInfo,
            onLoadMoreHistory = onLoadMoreHistory,
            onSearch = onSearch,
            onSearchNext = onSearchNext,
            onClearSearch = onClearSearch,
            searchMatchedMessageId = searchMatchedMessageId,
            visible = true,
            isTyping = isTypingState.value,
            isLoadingMore = isLoadingMoreState.value, // 🔥 Передаём из VM
            hasMoreHistory = hasMoreHistoryState.value, // 🔥 Передаём из VM
            oldestIdState = oldestIdState, // 🔥 Передаём State (не .value!)
            onClose = onClose,
            onStartVoiceRecognition = {
                permissionManager.requestMicrophonePermission()
            },
            isListeningState = isListeningState.value,
            onStopListening = onStopListening,
            careBankCommandHandler = careBankCommandHandler,
            careBankWebViewUrl = careBankWebViewUrl,
            careBankAutomationData = careBankAutomationData,
            onCloseCareBankWebView = onCloseCareBankWebView,
            careBankRepository = careBankRepository,
            careBankApi = careBankApi,
            onAddChatMessage = onAddChatMessage,
            onSendSystemEvent = onSendSystemEvent,
            onUpdateEmoji = onUpdateEmoji
        )
    }
}