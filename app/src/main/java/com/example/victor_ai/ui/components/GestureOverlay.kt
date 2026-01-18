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
