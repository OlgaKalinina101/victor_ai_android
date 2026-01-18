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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.victor_ai.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

/**
 * Анимированные глаза Виктора с временем
 */
@Composable
fun VictorEyes(
    state: EyeState = EyeState.IDLE,
    trailingText: String? = null,
    showTime: Boolean = true,
    alignCenter: Boolean = false, // 👈 новый параметр
    modifier: Modifier = Modifier
) {
    val animatable = remember { Animatable(0f) }
    var timeText by remember { mutableStateOf("") }
    var typedText by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    // ⏰ обновление времени (реальное время)
    LaunchedEffect(Unit) {
        if (showTime && trailingText == null) {
            while (isActive) {
                timeText = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                delay(1000)
            }
        }
    }

    // 👁️ моргание
    LaunchedEffect(state) {
        when (state) {
            EyeState.IDLE -> {
                while (isActive) {
                    delay((3000..5000).random().toLong())
                    animatable.animateTo(1f, tween(200))
                    animatable.animateTo(0f, tween(200))
                }
            }
            EyeState.THINKING -> {
                while (isActive) {
                    animatable.animateTo(1f, tween(2000))
                    animatable.snapTo(0f)
                }
            }
            EyeState.SLEEPING -> animatable.snapTo(1f)
            EyeState.HAPPY -> animatable.snapTo(0f)
        }
    }

    val animationPhase = animatable.value
    val fullText = trailingText ?: if (showTime) "... $timeText." else ""

    // ✨ эффект "печатающегося текста"
    LaunchedEffect(fullText) {
        isVisible = true
        typedText = ""
        delay(300) // короткая пауза перед "началом печати"
        for (i in fullText.indices) {
            typedText = fullText.substring(0, i + 1)
            delay(45) // скорость совпадает с TypingText
        }
    }

    if (isVisible) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.alpha(1f)
        ) {
            // глазки
            Canvas(
                modifier = Modifier
                    .size(62.dp)
                    .then(
                        if (!alignCenter) Modifier.offset(x = (-18).dp) else Modifier // 👈 смещение только если не центр
                    )
            ) {
                drawEyes(animationPhase, state)
            }

            // печатающийся текст
            Text(
                text = typedText,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.didact_gothic)),
                    color = Color(0xFFA6A6A6),
                    fontSize = 26.sp
                ),
                modifier = Modifier.offset(x = (-16).dp)
            )
        }
    }
}




/**
 * Состояния глаз Виктора
 */
enum class EyeState {
    IDLE,       // спокойно моргает
    THINKING,   // зрачки двигаются
    SLEEPING,   // закрыты
    HAPPY       // улыбаются
}

/**
 * Рисует глаза с учётом фазы анимации
 */
private fun DrawScope.drawEyes(phase: Float, state: EyeState) {
    val eyeOutlineColor = Color(0xFF202022)
    val eyeWhiteColor = Color(0xFFFFFFFF)
    val pupilColor = Color(0xFF3F4650)
    val highlightColor = Color(0x99FFFFFF)

    val leftEyeCenter = Offset(size.width * 0.40f, size.height * 0.5f) //расстояние между глазками = разница между 0.38f и 0.63f = 0.25 (25% ширины Canvas)
    val rightEyeCenter = Offset(size.width * 0.60f, size.height * 0.5f) //расстояние между глазками = разница между 0.38f и 0.63f = 0.25 (25% ширины Canvas)

    val eyeWidth = size.width * 0.10f
    val eyeHeight = size.width * 0.16f

    // 🎯 НАСТРОЙКИ ЗРАЧКОВ
    val pupilRadius = eyeHeight * 0.35f
    val pupilOffsetX = -eyeWidth * 0.4f       // горизонтальное смещение
    val pupilOffsetY = 0f                     // вертикальное смещение

    val highlightRadius = pupilRadius * 0.25f

    when (state) {
        EyeState.IDLE, EyeState.THINKING -> {
            if (phase > 0f && state == EyeState.IDLE) {
                // Моргание
                drawBlinkingEye(leftEyeCenter, eyeWidth, eyeHeight, phase, eyeOutlineColor)
                drawBlinkingEye(rightEyeCenter, eyeWidth, eyeHeight, phase, eyeOutlineColor)
            } else {
                // Левый глаз
                drawEmojiEye(
                    center = leftEyeCenter,
                    width = eyeWidth,
                    height = eyeHeight,
                    pupilRadius = pupilRadius,
                    highlightRadius = highlightRadius,
                    pupilOffset = if (state == EyeState.THINKING) {
                        val angle = phase * 2 * PI.toFloat()
                        Offset(
                            cos(angle) * eyeWidth * 0.2f,
                            sin(angle) * eyeHeight * 0.2f
                        )
                    } else {
                        Offset(pupilOffsetX, pupilOffsetY)  // ← ИСПОЛЬЗУЕМ ПЕРЕМЕННЫЕ
                    },
                    eyeWhiteColor = eyeWhiteColor,
                    eyeOutlineColor = eyeOutlineColor,
                    pupilColor = pupilColor,
                    highlightColor = highlightColor
                )

                // Правый глаз
                drawEmojiEye(
                    center = rightEyeCenter,
                    width = eyeWidth,
                    height = eyeHeight,
                    pupilRadius = pupilRadius,
                    highlightRadius = highlightRadius,
                    pupilOffset = if (state == EyeState.THINKING) {
                        val angle = phase * 2 * PI.toFloat()
                        Offset(
                            cos(angle) * eyeWidth * 0.2f,
                            sin(angle) * eyeHeight * 0.2f
                        )
                    } else {
                        Offset(pupilOffsetX, pupilOffsetY)  // ← ИСПОЛЬЗУЕМ ПЕРЕМЕННЫЕ
                    },
                    eyeWhiteColor = eyeWhiteColor,
                    eyeOutlineColor = eyeOutlineColor,
                    pupilColor = pupilColor,
                    highlightColor = highlightColor
                )
            }
        }

        EyeState.SLEEPING -> {
            // Закрытые глаза
            drawLine(
                eyeOutlineColor,
                start = Offset(leftEyeCenter.x - eyeWidth, leftEyeCenter.y),
                end = Offset(leftEyeCenter.x + eyeWidth, leftEyeCenter.y),
                strokeWidth = 3f
            )
            drawLine(
                eyeOutlineColor,
                start = Offset(rightEyeCenter.x - eyeWidth, rightEyeCenter.y),
                end = Offset(rightEyeCenter.x + eyeWidth, rightEyeCenter.y),
                strokeWidth = 3f
            )
        }

        EyeState.HAPPY -> {
            // Улыбающиеся глаза (дуги)
            val path = Path().apply {
                moveTo(leftEyeCenter.x - eyeWidth, leftEyeCenter.y)
                quadraticBezierTo(
                    leftEyeCenter.x, leftEyeCenter.y - eyeHeight * 0.8f,
                    leftEyeCenter.x + eyeWidth, leftEyeCenter.y
                )
            }
            drawPath(path, eyeOutlineColor, style = Stroke(width = 3f))

            val pathRight = Path().apply {
                moveTo(rightEyeCenter.x - eyeWidth, rightEyeCenter.y)
                quadraticBezierTo(
                    rightEyeCenter.x, rightEyeCenter.y - eyeHeight * 0.8f,
                    rightEyeCenter.x + eyeWidth, rightEyeCenter.y
                )
            }
            drawPath(pathRight, eyeOutlineColor, style = Stroke(width = 3f))
        }
    }
}

/**
 * Рисует один глаз в стиле эмодзи 👀
 */
private fun DrawScope.drawEmojiEye(
    center: Offset,
    width: Float,
    height: Float,
    pupilRadius: Float,
    highlightRadius: Float,
    pupilOffset: Offset,
    eyeWhiteColor: Color,
    eyeOutlineColor: Color,
    pupilColor: Color,
    highlightColor: Color
) {
    // Белок глаза
    drawOval(
        color = eyeWhiteColor,
        topLeft = Offset(center.x - width, center.y - height),
        size = androidx.compose.ui.geometry.Size(width * 2, height * 2)
    )

    // Контур глаза
    drawOval(
        color = eyeOutlineColor,
        topLeft = Offset(center.x - width, center.y - height),
        size = androidx.compose.ui.geometry.Size(width * 2, height * 2),
        style = Stroke(width = 2.5f)
    )

    // Зрачок
    val pupilCenter = center + pupilOffset
    drawCircle(
        color = pupilColor,
        radius = pupilRadius,
        center = pupilCenter
    )

    // Блик
    drawCircle(
        color = highlightColor,
        radius = highlightRadius,
        center = pupilCenter + Offset(-pupilRadius * 0.3f, -pupilRadius * 0.3f)
    )
}

/**
 * Рисует моргающий глаз
 */
private fun DrawScope.drawBlinkingEye(
    center: Offset,
    width: Float,
    height: Float,
    phase: Float,
    color: Color
) {
    val topLidY = center.y - height + (height * 2 * phase)
    val bottomLidY = center.y + height - (height * 2 * phase)

    if (topLidY < bottomLidY) {
        val clipPath = Path().apply {
            moveTo(center.x - width, topLidY)
            quadraticBezierTo(
                center.x, center.y - height,
                center.x + width, topLidY
            )
            lineTo(center.x + width, bottomLidY)
            quadraticBezierTo(
                center.x, center.y + height,
                center.x - width, bottomLidY
            )
            close()
        }

        drawPath(clipPath, Color.White)
        drawPath(clipPath, color, style = Stroke(width = 2.5f))
    } else {
        drawLine(
            color,
            start = Offset(center.x - width, center.y),
            end = Offset(center.x + width, center.y),
            strokeWidth = 3f
        )
    }
}
