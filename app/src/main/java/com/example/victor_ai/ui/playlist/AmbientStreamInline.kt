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

package com.example.victor_ai.ui.playlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.victor_ai.ui.components.EyeState
import com.example.victor_ai.ui.components.VictorEyes

@Composable
fun AmbientThinkingRow(
    show: Boolean,
    typedText: String,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    // 🎯 Базовый стиль текста
    val textStyle = TextStyle(fontSize = 18.sp, fontFamily = fontFamily)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    ) {
        AnimatedVisibility(
            visible = show,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                // 🔥 ВАЖНО: при многострочном тексте выравниваем по верху, чтобы текст мог уходить вниз
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                VictorEyes(
                    state = EyeState.IDLE,
                    showTime = false,
                    alignCenter = true,
                    modifier = Modifier.size(48.dp)
                )

                // небольшой отступ, чтобы текст не "упирался" в глазки
                Spacer(modifier = Modifier.width(8.dp))

                // 🎯 Текст занимает всю оставшуюся ширину и ПЕРЕНОСИТСЯ ВНИЗ
                if (typedText.isBlank()) {
                    LoadingDots(
                        color = Color(0xFFE0E0E0),
                        textStyle = textStyle,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = typedText,
                        color = Color(0xFF666666),
                        style = textStyle,
                        // 🔥 Главное: разрешаем перенос строк вниз по ширине
                        softWrap = true,
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * ✨ Простой лоадер в виде бегущих точек "..."
 * Показывается до появления первых символов streaming-лога.
 */
@Composable
private fun LoadingDots(
    color: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_dots")

    val animated by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "loading_dots_value"
    )

    val dotCount = animated.toInt().coerceIn(0, 3)
    val text = if (dotCount == 0) "" else ".".repeat(dotCount)

    Text(
        text = text,
        color = color,
        style = textStyle,
        maxLines = 1,
        overflow = TextOverflow.Clip,
        modifier = modifier
    )
}

/**
 * 🎯 Бегущая строка (marquee effect)
 * Плавно прокручивает длинный текст слева направо
 */
@Composable
fun MarqueeText(
    text: String,
    textStyle: TextStyle,
    color: Color,
    maxWidth: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val textWidth = remember(text) {
        with(textMeasurer.measure(text, style = textStyle)) {
            size.width
        }
    }
    
    val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
    val scrollDistance = textWidth - maxWidthPx
    
    // 🎯 Анимация: текст уезжает влево, возвращается, пауза
    val infiniteTransition = rememberInfiniteTransition(label = "marquee")
    
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -scrollDistance,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (text.length * 80).coerceIn(3000, 8000),  // Зависит от длины
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "marquee_offset"
    )
    
    Box(
        modifier = modifier
            .width(maxWidth)
            .clipToBounds()  // Обрезаем текст по границам
    ) {
        Text(
            text = text,
            color = color,
            style = textStyle,
            maxLines = 1,
            modifier = Modifier.offset(x = with(LocalDensity.current) { offsetX.toDp() })
        )
    }
}



