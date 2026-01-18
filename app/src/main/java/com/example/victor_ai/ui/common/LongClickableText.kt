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
                            val url = annotation.item
                            val intent = if (url.contains("openstreetmap.org")) {
                                // Извлекаем координаты из OpenStreetMap URL
                                val latRegex = """mlat=([-\d.]+)""".toRegex()
                                val lonRegex = """mlon=([-\d.]+)""".toRegex()

                                val lat = latRegex.find(url)?.groupValues?.get(1)
                                val lon = lonRegex.find(url)?.groupValues?.get(1)

                                if (lat != null && lon != null) {
                                    // Открываем Google Maps с координатами
                                    Intent(Intent.ACTION_VIEW, "geo:$lat,$lon?q=$lat,$lon".toUri())
                                } else {
                                    // Если не смогли извлечь координаты, открываем как обычную ссылку
                                    Intent(Intent.ACTION_VIEW, url.toUri())
                                }
                            } else {
                                Intent(Intent.ACTION_VIEW, url.toUri())
                            }
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