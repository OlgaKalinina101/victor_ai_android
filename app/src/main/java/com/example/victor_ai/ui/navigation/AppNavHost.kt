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

package com.example.victor_ai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.State
import com.example.victor_ai.auth.UserProvider
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
import com.example.victor_ai.ui.screens.HomeScreen
import com.example.victor_ai.ui.screens.system.SystemMenuScreen
import com.example.victor_ai.logic.carebank.CareBankCommandHandler
import com.example.victor_ai.utils.ImageUtils

@Composable
fun AppNavHost(
    navController: NavHostController,
    accountId: String,  // 🔐 Текущий accountId для reinitialize экранов
    playlistViewModel: PlaylistViewModel,  // 🔥 Получаем извне
    placesViewModel: PlacesViewModel,
    reminderManager: ReminderManager,
    chatMessages: List<ChatMessage>,
    onSendMessage: (String, List<ImageUtils.ImageAttachment>, Int?) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onPaginationInfo: (oldestId: Int?, hasMore: Boolean) -> Unit,
    onLoadMoreHistory: suspend (Int) -> Result<Triple<Boolean, Int?, Boolean>>,
    onSearch: (String) -> Unit,
    onSearchNext: () -> Unit,
    onClearSearch: () -> Unit,
    searchMatchedMessageId: Int?,
    onStartVoiceRecognition: () -> Unit,
    onRequestMicrophone: () -> Unit,
    isListeningState: State<Boolean>,
    isTypingState: State<Boolean>,
    isLoadingMoreState: State<Boolean>, // 🔥 Новый параметр из VM
    hasMoreHistoryState: State<Boolean>, // 🔥 Новый параметр из VM
    oldestIdState: State<Int?>, // 🔥 Новый параметр из VM для пагинации
    permissionManager: PermissionManager,
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
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            HomeScreen(
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
                onPaginationInfo = onPaginationInfo,
                onLoadMoreHistory = onLoadMoreHistory,
                onSearch = onSearch,
                onSearchNext = onSearchNext,
                onClearSearch = onClearSearch,
                searchMatchedMessageId = searchMatchedMessageId,
                onClose = { navController.popBackStack() },
                permissionManager = permissionManager,
                isListeningState = isListeningState,
                isTypingState = isTypingState,
                isLoadingMoreState = isLoadingMoreState, // 🔥 Передаём из VM
                hasMoreHistoryState = hasMoreHistoryState, // 🔥 Передаём из VM
                oldestIdState = oldestIdState, // 🔥 Передаём из VM для пагинации
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
            CalendarScreenWithReminders(
                accountId = accountId
            )
        }

        composable("system") {
            SystemMenuScreen(
                modifier = Modifier,
                accountId = accountId
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