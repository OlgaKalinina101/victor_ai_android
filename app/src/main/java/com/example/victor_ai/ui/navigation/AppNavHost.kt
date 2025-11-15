package com.example.victor_ai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.State
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.logic.UsageRepository
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.places.PlacesMenu
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.playlist.PlaylistScreen
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.screens.BrowserScreen
import com.example.victor_ai.ui.screens.CalendarScreenWithReminders
import com.example.victor_ai.ui.screens.ChatScreen
import com.example.victor_ai.ui.screens.EnvironmentScreen
import com.example.victor_ai.ui.screens.MainScreen
import com.example.victor_ai.ui.screens.system.SystemMenuScreen

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
    onPaginationInfo: (oldestId: Int?, hasMore: Boolean) -> Unit,
    onLoadMoreHistory: suspend (Int) -> Boolean,
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
                onPaginationInfo = onPaginationInfo,
                onLoadMoreHistory = onLoadMoreHistory,
                onClose = { navController.popBackStack() },
                permissionManager = permissionManager,
                isListeningState = isListeningState,
                isTypingState = isTypingState,
                onStopListening = onStopListening
            )
        }

        composable("playlist") {
            PlaylistScreen(
                viewModel = playlistViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("places") {
            PlacesMenu(
                onBack = { navController.popBackStack() },
                viewModel = placesViewModel
            )
        }

        composable("calendar") {
            CalendarScreenWithReminders {
                com.example.victor_ai.logic.getRemindersFromRepository(UserProvider.getCurrentUserId())
            }
        }

        composable("system") {
            SystemMenuScreen(
                modifier = Modifier
            )
        }

        composable("environment") {
            EnvironmentScreen(
                modifier = Modifier
            )
        }

        composable("browser") {
            BrowserScreen(
                modifier = Modifier
            )
        }
    }
}