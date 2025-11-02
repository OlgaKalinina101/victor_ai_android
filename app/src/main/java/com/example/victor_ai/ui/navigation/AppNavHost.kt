package com.example.victor_ai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.State
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.screens.ChatScreen
import com.example.victor_ai.ui.screens.MainScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    playlistViewModel: PlaylistViewModel,  // 🔥 Получаем извне
    placesViewModel: PlacesViewModel,
    reminderManager: ReminderManager,
    chatMessages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onInitHistory: (List<ChatMessage>) -> Unit,
    onStartVoiceRecognition: () -> Unit,
    onRequestMicrophone: () -> Unit,
    isListeningState: State<Boolean>,
    isTypingState: State<Boolean>,
    permissionManager: PermissionManager,
    onStopListening: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                navController = navController,
                playlistViewModel = playlistViewModel,  // 🔥 Передаём
                placesViewModel = placesViewModel,
                reminderManager = reminderManager,
                onStartVoiceRecognition = onStartVoiceRecognition,
                onRequestMicrophone = onRequestMicrophone,
                isListeningState = isListeningState,
                permissionManager = permissionManager,
                onStopListening = onStopListening
            )
        }

        composable("chat") {
            ChatScreen(
                messages = chatMessages,
                onSendMessage = onSendMessage,
                onEditMessage = onEditMessage,
                onInitHistory = onInitHistory,
                onClose = { navController.popBackStack() },
                permissionManager = permissionManager,
                isListeningState = isListeningState,
                isTypingState = isTypingState,
                onStopListening = onStopListening
            )
        }

        // reminder убрали — он теперь в MainActivity
    }
}