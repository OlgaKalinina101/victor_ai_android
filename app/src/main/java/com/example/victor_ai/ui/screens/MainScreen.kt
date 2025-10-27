package com.example.victor_ai.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.components.AssistantButtonArea
import com.example.victor_ai.ui.theme.MenuState

@Composable
fun MainScreen(
    navController: NavController,
    reminderManager: ReminderManager,
    onStartVoiceRecognition: () -> Unit,
    onRequestMicrophone: () -> Unit,
    isListeningState: State<Boolean>,
    permissionManager: PermissionManager,
    onStopListening: () -> Unit
) {
    var showAssistantMenu by remember { mutableStateOf(false) }
    val menuState = remember { mutableStateOf(MenuState.ROOT) }
    val popup by reminderManager.reminderPopup.collectAsState()

    // 🔥 Проверка текущего экрана
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        val allowGestures = currentRoute == "main"
                && menuState.value == MenuState.ROOT
                && !showAssistantMenu
                && popup == null

        // 🔸 Слой жестов — только на главном экране
        if (allowGestures) {
            Box(
                modifier = Modifier
                    .fillMaxSize()  // можешь оставить, т.к. allowGestures теперь контролирует
                    .padding(start = 220.dp, top = 360.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                println("✅ TAP -> открываем чат")
                                navController.navigate("chat")
                            },
                            onLongPress = {
                                println("🎤 LONG TAP -> микрофон")
                                permissionManager.requestMicrophonePermission()
                            },
                            onPress = {
                                println("🛑 PRESS -> остановить прослушку")
                                tryAwaitRelease()
                                if (isListeningState.value) {
                                    onStopListening()
                                }
                            }
                        )
                    }
            )
        }

        // 🔹 Кнопка ассистента
        AssistantButtonArea(
            modifier = Modifier.align(Alignment.BottomEnd),
            reminderManager = reminderManager,
            onStartVoiceRecognition = onStartVoiceRecognition,
            onRequestMicrophone = onRequestMicrophone,
            onOpenChat = { navController.navigate("chat") }
        )
    }
}