package com.example.victor_ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import com.example.victor_ai.ui.menu.MenuState

@Composable
fun PointerInputOverlay(
    enabled: Boolean,
    menuState: MutableState<MenuState>,
    showAssistantMenu: Boolean,
    showInfoBox: () -> Unit,
    onLongPress: () -> Unit,
    onStopListening: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Контент внутри
        content()

        // Прозрачный захватчик жестов поверх
        if (enabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .pointerInput(enabled, showAssistantMenu) {
                        detectTapGestures(
                            onTap = {
                                println("menuState = ${menuState.value}, showAssistantMenu = $showAssistantMenu")
                                if (!showAssistantMenu) showInfoBox()
                            },
                            onLongPress = {
                                if (!showAssistantMenu) onLongPress()
                            },
                            onPress = {
                                tryAwaitRelease()
                                onStopListening()
                            }
                        )
                    }
                    .zIndex(1000f) // самый верхний слой
            )
        }
    }
}
