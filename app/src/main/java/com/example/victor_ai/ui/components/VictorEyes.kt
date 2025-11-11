package com.example.victor_ai.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

/**
 * Анимированные глаза Виктора
 * Поддерживают разные состояния: IDLE (моргание), THINKING (движение зрачков), SLEEPING (закрыты), HAPPY (улыбаются)
 */
@Composable
fun VictorEyes(
    state: EyeState = EyeState.IDLE,
    modifier: Modifier = Modifier
) {
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(state) {
        when (state) {
            EyeState.IDLE -> {
                // Медленное моргание каждые 3-5 сек
                while (true) {
                    delay((3000..5000).random().toLong())
                    animatable.animateTo(1f, animationSpec = tween(200))
                    animatable.animateTo(0f, animationSpec = tween(200))
                }
            }
            EyeState.THINKING -> {
                // Зрачки двигаются
                while (true) {
                    animatable.animateTo(1f, animationSpec = tween(1000))
                }
            }
            EyeState.SLEEPING -> {
                animatable.snapTo(1f) // закрыты мгновенно
            }
            EyeState.HAPPY -> {
                animatable.snapTo(0f) // открыты и улыбаются
            }
        }
    }

    val animationPhase = animatable.value

    Canvas(modifier = modifier.size(48.dp)) {
        drawEyes(animationPhase, state)
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
    val eyeColor = Color(0xFFA6A6A6)
    val pupilColor = Color(0xFF666666)

    val leftEyeCenter = Offset(size.width * 0.35f, size.height * 0.5f)
    val rightEyeCenter = Offset(size.width * 0.65f, size.height * 0.5f)
    val eyeRadius = size.width * 0.12f
    val pupilRadius = eyeRadius * 0.5f

    when (state) {
        EyeState.IDLE, EyeState.THINKING -> {
            // Моргание: рисуем закрывающиеся веки
            if (phase > 0f) {
                drawBlinkingEye(leftEyeCenter, eyeRadius, phase, eyeColor)
                drawBlinkingEye(rightEyeCenter, eyeRadius, phase, eyeColor)
            } else {
                // Открытые глаза
                drawCircle(eyeColor, eyeRadius, leftEyeCenter, style = Stroke(width = 2f))
                drawCircle(eyeColor, eyeRadius, rightEyeCenter, style = Stroke(width = 2f))

                // Зрачки
                val pupilOffset = if (state == EyeState.THINKING) {
                    // Движение зрачков по окружности
                    val angle = phase * 2 * PI.toFloat()
                    Offset(
                        cos(angle) * pupilRadius * 0.4f,
                        sin(angle) * pupilRadius * 0.4f
                    )
                } else {
                    Offset.Zero
                }

                drawCircle(pupilColor, pupilRadius, leftEyeCenter + pupilOffset)
                drawCircle(pupilColor, pupilRadius, rightEyeCenter + pupilOffset)
            }
        }

        EyeState.SLEEPING -> {
            // Закрытые глаза (горизонтальные линии)
            drawLine(
                eyeColor,
                start = Offset(leftEyeCenter.x - eyeRadius, leftEyeCenter.y),
                end = Offset(leftEyeCenter.x + eyeRadius, leftEyeCenter.y),
                strokeWidth = 2f
            )
            drawLine(
                eyeColor,
                start = Offset(rightEyeCenter.x - eyeRadius, rightEyeCenter.y),
                end = Offset(rightEyeCenter.x + eyeRadius, rightEyeCenter.y),
                strokeWidth = 2f
            )
        }

        EyeState.HAPPY -> {
            // Улыбающиеся глаза (дуги вверх)
            val path = Path().apply {
                moveTo(leftEyeCenter.x - eyeRadius, leftEyeCenter.y)
                quadraticBezierTo(
                    leftEyeCenter.x, leftEyeCenter.y - eyeRadius * 0.5f,
                    leftEyeCenter.x + eyeRadius, leftEyeCenter.y
                )
            }
            drawPath(path, eyeColor, style = Stroke(width = 2f))

            val pathRight = Path().apply {
                moveTo(rightEyeCenter.x - eyeRadius, rightEyeCenter.y)
                quadraticBezierTo(
                    rightEyeCenter.x, rightEyeCenter.y - eyeRadius * 0.5f,
                    rightEyeCenter.x + eyeRadius, rightEyeCenter.y
                )
            }
            drawPath(pathRight, eyeColor, style = Stroke(width = 2f))
        }
    }
}

/**
 * Рисует моргающий глаз
 */
private fun DrawScope.drawBlinkingEye(center: Offset, radius: Float, phase: Float, color: Color) {
    // Верхнее веко опускается вниз
    val topLidY = center.y - radius + (radius * 2 * phase)
    // Нижнее веко поднимается вверх
    val bottomLidY = center.y + radius - (radius * 2 * phase)

    if (topLidY < bottomLidY) {
        // Рисуем частично закрытый глаз
        val path = Path().apply {
            // Верхняя дуга (верхнее веко)
            moveTo(center.x - radius, topLidY)
            quadraticBezierTo(
                center.x, center.y - radius,
                center.x + radius, topLidY
            )

            // Нижняя дуга (нижнее веко)
            lineTo(center.x + radius, bottomLidY)
            quadraticBezierTo(
                center.x, center.y + radius,
                center.x - radius, bottomLidY
            )
            close()
        }
        drawPath(path, color, style = Stroke(width = 2f))
    } else {
        // Полностью закрыт - рисуем горизонтальную линию
        drawLine(
            color,
            start = Offset(center.x - radius, center.y),
            end = Offset(center.x + radius, center.y),
            strokeWidth = 2f
        )
    }
}
