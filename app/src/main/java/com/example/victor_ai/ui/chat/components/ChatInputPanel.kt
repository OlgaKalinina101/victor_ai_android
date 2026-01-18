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

package com.example.victor_ai.ui.chat.components

import android.util.Log
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.utils.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Панель ввода сообщений с поддержкой изображений
 */
@Composable
fun ChatInputPanel(
    userInput: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    onAttachClick: () -> Unit,
    attachedImages: List<ImageUtils.ImageAttachment> = emptyList(),
    onImagesAttached: (List<ImageUtils.ImageAttachment>) -> Unit = {},
    onImageRemoved: (ImageUtils.ImageAttachment) -> Unit = {},
    onLongPressSend: () -> Unit = {}
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isProcessing by remember { mutableStateOf(false) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult

        // Ограничение: максимум 1 изображение
        if (attachedImages.isNotEmpty()) {
            Log.w("ChatInputPanel", "Уже прикреплено 1 изображение")
            return@rememberLauncherForActivityResult
        }

        // Обработка в фоне
        scope.launch {
            isProcessing = true
            val newAttachments = withContext(Dispatchers.IO) {
                listOfNotNull(ImageUtils.createImageAttachment(context, uri))
            }

            if (newAttachments.isNotEmpty()) {
                onImagesAttached(newAttachments) // максимум 1
                Log.d("ChatInputPanel", "Прикреплено ${newAttachments.size} изображений")
            }
            isProcessing = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2B2929))
            .pointerInput(Unit) {
                // Блокируем жесты ChatBox на области ввода
                detectTapGestures(
                    onTap = { /* consume */ },
                    onLongPress = { /* consume */ },
                    onPress = { /* consume */ }
                )
            }
    ) {
        // Превью прикрепленных изображений
        if (attachedImages.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(attachedImages) { attachment ->
                    ImagePreviewItem(
                        attachment = attachment,
                        onRemove = { onImageRemoved(attachment) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // [📎] Прикрепить
            IconButton(
                onClick = {
                    if (attachedImages.isEmpty() && !isProcessing) {
                        imagePickerLauncher.launch("image/*")
                    }
                },
                modifier = Modifier.size(40.dp),
                enabled = attachedImages.isEmpty() && !isProcessing
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFFBB86FC),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Прикрепить изображение",
                        tint = if (attachedImages.isEmpty()) Color(0xFFE0E0E0) else Color(0xFF666666)
                    )
                }
            }

            // Поле ввода
            OutlinedTextField(
                value = userInput,
                onValueChange = onInputChange,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFFBB86FC)
                ),
                shape = RoundedCornerShape(20.dp),
                placeholder = {
                    Text("текст...", color = Color.Gray, fontSize = 14.sp, fontFamily = didactGothicFont)
                },
                textStyle = TextStyle(fontFamily = didactGothicFont)
            )

            // [▶] Отправить (с поддержкой долгого тапа для микрофона)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { onSend() },
                            onLongPress = { onLongPressSend() }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("▶", fontSize = 20.sp, color = Color(0xFFE0E0E0), fontFamily = didactGothicFont)
            }
        }
    }
}

/**
 * Превью изображения с кнопкой удаления
 */
@Composable
private fun ImagePreviewItem(
    attachment: ImageUtils.ImageAttachment,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier.size(80.dp)
    ) {
        // Превью изображения
        attachment.thumbnail?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Прикрепленное изображение",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF444444), RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Кнопка удаления
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .offset(x = 4.dp, y = (-4).dp)
                .background(Color.Black.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Удалить",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
