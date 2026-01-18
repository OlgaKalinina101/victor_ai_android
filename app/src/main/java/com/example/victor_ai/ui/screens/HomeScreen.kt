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

package com.example.victor_ai.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.victor_ai.R
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.components.EyeState
import com.example.victor_ai.ui.components.VictorEyes
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.LocalTime
import kotlin.random.Random

@Composable
fun HomeScreen(
    navController: NavController,
    reminderManager: ReminderManager,
    onStartVoiceRecognition: () -> Unit,
    onRequestMicrophone: () -> Unit,
    isListeningState: State<Boolean>,
    permissionManager: PermissionManager,
    onStopListening: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // ↩️ Стрелка выхода (правый верхний угол)
        IconButton(
            onClick = {
                UserProvider.logout()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Выход",
                tint = Color(0xFFA6A6A6)
            )
        }
    }
}

@Composable
fun PresencePlaceholder(
    modifier: Modifier = Modifier,
    customLines: List<String>? = null
) {
    var greetingText by remember { mutableStateOf("") }
    var showFirstLine by remember { mutableStateOf(false) }
    var showSecondLine by remember { mutableStateOf(false) }
    var showThirdLine by remember { mutableStateOf(false) }

    // плавное дыхание
    val alpha by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val didactStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.didact_gothic)),
        color = Color(0xFFA6A6A6),
        fontSize = 26.sp,
        textAlign = TextAlign.Start
    )

    // обновление приветствия и последовательная анимация появления
    LaunchedEffect(customLines) {
        while (isActive) {
            val now = LocalTime.now()
            greetingText = when (now.hour) {
                in 6..11 -> "Доброе утро."
                in 12..17 -> "Хорошего дня."
                in 18..22 -> "Тёплого вечера."
                else -> "Иди спать."
            }

            // Последовательное появление строк
            showFirstLine = false
            showSecondLine = false
            showThirdLine = false

            delay(600)
            showFirstLine = true

            delay(1000 + Random.nextLong(200, 700))
            showSecondLine = true

            delay(800 + Random.nextLong(300, 600))
            showThirdLine = true

            delay(58_000)
        }
    }

    // Используем кастомные строки, если переданы
    val lines = customLines ?: listOf("Я здесь.", greetingText)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
    ) {
        // 👀 Глазки с временем (первая строка)
        if (showFirstLine) {
            VictorEyes(
                state = EyeState.IDLE,
                showTime = true
            )
        }

        Spacer(Modifier.height(6.dp))

        // Вторая строка
        if (showSecondLine && lines.isNotEmpty()) {
            TypingText(text = lines[0], style = didactStyle, speed = 45L)
        }

        Spacer(Modifier.height(14.dp))

        // Третья строка
        if (showThirdLine && lines.size > 1) {
            TypingText(text = lines[1], style = didactStyle, speed = 50L)
        }
    }
}

@Composable
fun TypingText(text: String, style: TextStyle, speed: Long = 35L) {
    var displayed by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        displayed = ""
        text.forEachIndexed { i, _ ->
            displayed = text.take(i + 1)
            delay(speed)
        }
    }

    Text(text = displayed, style = style)
}


