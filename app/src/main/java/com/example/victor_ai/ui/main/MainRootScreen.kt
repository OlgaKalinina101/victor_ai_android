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

package com.example.victor_ai.ui.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.domain.model.ReminderPopup
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.logic.carebank.CareBankCommandHandler
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.common.AnimatedBackgroundBox
import com.example.victor_ai.ui.components.AssistantButtonArea
import com.example.victor_ai.ui.components.ReminderOverlay
import com.example.victor_ai.ui.navigation.AppNavHost
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.screens.PresencePlaceholder
import com.example.victor_ai.utils.ImageUtils

/**
 * Корневой экран приложения (shell)
 * Содержит навигацию, snackbar, фоновую анимацию и overlay компоненты
 */
@Composable
fun MainRootScreen(
    accountId: String,  // 🔐 Для reinitialize экранов при смене аккаунта
    playlistViewModel: PlaylistViewModel,
    placesViewModel: PlacesViewModel,
    reminderManager: ReminderManager,
    chatMessages: List<ChatMessage>,
    onSendMessage: (String, List<ImageUtils.ImageAttachment>, Int?) -> Unit,
    onEditMessage: (Int, String) -> Unit,
    onPaginationInfo: (Int?, Boolean) -> Unit,
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
    snackbarMessage: String?,
    onClearSnackbar: () -> Unit,
    reminderPopup: ReminderPopup?,
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
                    accountId = accountId,
                    playlistViewModel = playlistViewModel,
                    placesViewModel = placesViewModel,
                    reminderManager = reminderManager,
                    chatMessages = chatMessages,
                    onSendMessage = onSendMessage,
                    onEditMessage = onEditMessage,
                    onPaginationInfo = onPaginationInfo,
                    onLoadMoreHistory = onLoadMoreHistory,
                    onSearch = onSearch,
                    onSearchNext = onSearchNext,
                    onClearSearch = onClearSearch,
                    searchMatchedMessageId = searchMatchedMessageId,
                    onStartVoiceRecognition = onStartVoiceRecognition,
                    onRequestMicrophone = onRequestMicrophone,
                    isListeningState = isListeningState,
                    isTypingState = isTypingState,
                    isLoadingMoreState = isLoadingMoreState, // 🔥 Передаём из VM
                    hasMoreHistoryState = hasMoreHistoryState, // 🔥 Передаём из VM
                    oldestIdState = oldestIdState, // 🔥 Передаём из VM для пагинации
                    permissionManager = permissionManager,
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

                // 🔹 PresencePlaceholder — только на главном экране
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                val allowTapToChat = (currentRoute == "main" || currentRoute == null) && reminderPopup == null

                if (currentRoute == "main" || currentRoute == null) {
                    PresencePlaceholder(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .padding(top = 180.dp)
                            .offset(x = (50).dp) // смещаем влево от края
                            .then(
                                if (allowTapToChat) {
                                    Modifier.pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = { navController.navigate("chat") }
                                        )
                                    }
                                } else {
                                    Modifier
                                }
                            )
                    )
                }

                // 🔹 AssistantButtonArea — доступна со всех экранов кроме чата
                if (currentRoute != "chat") {
                    AssistantButtonArea(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        accountId = accountId,
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
                            reminderManager.sendReminderActionCoroutine("done", it.id, it.repeatWeekly)
                            reminderManager.clearPopup()
                        },
                        onDelay = {
                            reminderManager.sendReminderActionCoroutine("delay", it.id, it.repeatWeekly)
                            reminderManager.clearPopup()
                        },
                        onDisableRepeat = {
                            reminderManager.disableReminderRepeat(it.id)
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


