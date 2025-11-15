package com.example.victor_ai.ui.playlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.victor_ai.ui.components.EyeState
import com.example.victor_ai.ui.components.VictorEyes

@Composable
fun AmbientThinkingRow(
    show: Boolean,
    typedText: String,
    fontFamily: FontFamily
) {
    // фиксированная высота — чтобы кнопка не прыгала
    val rowHeight = 48.dp

    Box(
        modifier = Modifier
            .height(rowHeight)
            .padding(start = 8.dp)
            .wrapContentWidth()
    ) {
        androidx.compose.animation.AnimatedVisibility(
            visible = show,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                VictorEyes(
                    state = EyeState.IDLE,
                    showTime = false,
                    alignCenter = true,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = typedText,
                    color = Color(0xFF666666),
                    fontSize = 18.sp,
                    fontFamily = fontFamily,
                    modifier = Modifier.offset(x = (0).dp) // расстояние между глазками и текстом
                )
            }
        }
    }
}



