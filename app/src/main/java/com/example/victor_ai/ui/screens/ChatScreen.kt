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
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.chat.ChatBox

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onInitHistory: (List<ChatMessage>) -> Unit,
    onPaginationInfo: (oldestId: Int?, hasMore: Boolean) -> Unit,
    onLoadMoreHistory: suspend (Int) -> Boolean,
    onClose: () -> Unit,
    permissionManager: PermissionManager,
    isListeningState: State<Boolean>,
    isTypingState: State<Boolean>,
    onStopListening: () -> Unit
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
            onInitHistory = onInitHistory,
            onPaginationInfo = onPaginationInfo,
            onLoadMoreHistory = onLoadMoreHistory,
            visible = true,
            isTyping = isTypingState.value,
            onClose = onClose,
            onStartVoiceRecognition = {
                permissionManager.requestMicrophonePermission()
            },
            isListeningState = isListeningState.value,
            onStopListening = onStopListening
        )
    }
}