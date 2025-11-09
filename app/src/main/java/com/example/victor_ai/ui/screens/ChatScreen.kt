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
    onEditMessage: (Int, String) -> Unit, // 👈 добавляем новый параметр
    onInitHistory: (List<ChatMessage>) -> Unit,
    onClose: () -> Unit,
    permissionManager: PermissionManager,
    isListeningState: State<Boolean>,
    isTypingState: State<Boolean>,
    onStopListening: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2E))  // 🔹 Непрозрачный фон
    ) {
        // 🔹 Чат
        ChatBox(
            messages = messages,
            onSendMessage = onSendMessage,
            onEditMessage = onEditMessage, // 👈 пробрасываем в ChatBox
            onInitHistory = onInitHistory,
            visible = true,
            isTyping = isTypingState.value
        )

        // 🔹 Прозрачная полоса сверху — тап/долгий тап
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.TopCenter)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            println("❌ TAP -> закрываем чат")
                            onClose()
                        },
                        onLongPress = {
                            println("🎤 LONG TAP -> микрофон")
                            permissionManager.requestMicrophonePermission()
                        },
                        onPress = {
                            tryAwaitRelease()
                            if (isListeningState.value) {
                                onStopListening()
                            }
                        }
                    )
                }
        )
    }
}