package com.example.victor_ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex

@Composable
fun GestureOverlay(
    enabled: Boolean,
    onTap: () -> Unit,
    onLongPress: () -> Unit,
    onStopListening: () -> Unit
) {
    if (!enabled) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x5500FF00))
            .zIndex(10f)
            .background(Color.Transparent)
            .pointerInput(enabled) {
                detectTapGestures(
                    onTap = {
                        println("🟢 TAP detected")
                        onTap()
                    },
                    onLongPress = {
                        println("🟡 LONG TAP detected")
                        onLongPress()
                    },
                    onPress = {
                        tryAwaitRelease()
                        println("🔴 RELEASE detected")
                        onStopListening()
                    }
                )
            }
    )
}
