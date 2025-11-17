package com.example.victor_ai.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.domain.model.ReminderPopup
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.common.AnimatedBackgroundBox
import com.example.victor_ai.ui.components.AssistantButtonArea
import com.example.victor_ai.ui.components.ReminderOverlay
import com.example.victor_ai.ui.navigation.AppNavHost
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.screens.PresencePlaceholder

/**
 * Главный экран приложения
 * Содержит навигацию, snackbar, фоновую анимацию и overlay компоненты
 */
@Composable
fun MainScreen(
    playlistViewModel: PlaylistViewModel,
    placesViewModel: PlacesViewModel,
    reminderManager: ReminderManager,
    chatMessages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onInitHistory: (List<ChatMessage>) -> Unit,
    onPaginationInfo: (Int?, Boolean) -> Unit,
    onLoadMoreHistory: suspend (Int) -> Result<Triple<Boolean, Int?, Boolean>>,
    onStartVoiceRecognition: () -> Unit,
    onRequestMicrophone: () -> Unit,
    isListeningState: State<Boolean>,
    isTypingState: State<Boolean>,
    permissionManager: PermissionManager,
    onStopListening: () -> Unit,
    snackbarMessage: String?,
    onClearSnackbar: () -> Unit,
    reminderPopup: ReminderPopup?
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    // Показываем snackbar когда приходит сообщение
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            onClearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        containerColor = Color(0xFF1C1C1E),
                        contentColor = Color.White
                    ) {
                        Text(text = data.visuals.message)
                    }
                }
            )
        }
    ) { innerPadding ->
        AnimatedBackgroundBox {
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavHost(
                    navController = navController,
                    playlistViewModel = playlistViewModel,
                    placesViewModel = placesViewModel,
                    reminderManager = reminderManager,
                    chatMessages = chatMessages,
                    onSendMessage = onSendMessage,
                    onEditMessage = onEditMessage,
                    onInitHistory = onInitHistory,
                    onPaginationInfo = onPaginationInfo,
                    onLoadMoreHistory = onLoadMoreHistory,
                    onStartVoiceRecognition = onStartVoiceRecognition,
                    onRequestMicrophone = onRequestMicrophone,
                    isListeningState = isListeningState,
                    isTypingState = isTypingState,
                    permissionManager = permissionManager,
                    onStopListening = onStopListening
                )

                // 🔹 PresencePlaceholder — только на главном экране
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                if (currentRoute == "main" || currentRoute == null) {
                    PresencePlaceholder(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .padding(top = 180.dp)
                            .offset(x = (50).dp) // смещаем влево от края
                    )
                }

                // TODO: Добавить PresencePlaceholder на другие экраны после определения правильного расположения
                // - playlist: "Уснуть под музыку хорошая идея. Ты уже в кровати?"
                // - places: "Ты же не идёшь гулять, да?"
                // - calendar: "Просматриваешь планы? Не забудь про отдых."
                // - system: "Настраиваешь систему? Я помогу, если нужно."

                // 🔹 AssistantButtonArea — доступна со всех экранов кроме чата
                if (currentRoute != "chat") {
                    AssistantButtonArea(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        playlistViewModel = playlistViewModel,
                        placesViewModel = placesViewModel,
                        reminderManager = reminderManager,
                        navController = navController,
                        onStartVoiceRecognition = onStartVoiceRecognition,
                        onRequestMicrophone = onRequestMicrophone,
                        onOpenChat = { navController.navigate("chat") }
                    )
                }

                // 🔹 Overlay напоминаний
                reminderPopup?.let {
                    ReminderOverlay(
                        popup = it,
                        onOk = {
                            reminderManager.sendReminderActionCoroutine("done", it.id)
                            reminderManager.clearPopup()
                        },
                        onDelay = {
                            reminderManager.sendReminderActionCoroutine("delay", it.id)
                            reminderManager.clearPopup()
                        },
                        onDismiss = {
                            reminderManager.clearPopup()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
