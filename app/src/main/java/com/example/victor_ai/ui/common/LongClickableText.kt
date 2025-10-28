package com.example.victor_ai.ui.common

import android.content.Intent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@Composable
fun LongClickableText(
    text: AnnotatedString,
    onLongClick: () -> Unit,
    style: TextStyle = TextStyle(fontSize = 15.sp, color = Color.White),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    BasicText(
        text = text,
        style = style,
        onTextLayout = { layoutResult.value = it },
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { offset ->
                    layoutResult.value?.let { layout ->
                        val position = layout.getOffsetForPosition(offset)
                        val annotations = text.getStringAnnotations(
                            tag = "URL",
                            start = position,
                            end = position
                        )

                        annotations.firstOrNull()?.let { annotation ->
                            // Клик по ссылке
                            val intent = Intent(Intent.ACTION_VIEW, annotation.item.toUri())
                            context.startActivity(intent)
                        }
                    }
                },
                onLongPress = {
                    // Долгое нажатие
                    onLongClick()
                }
            )
        }
    )
}