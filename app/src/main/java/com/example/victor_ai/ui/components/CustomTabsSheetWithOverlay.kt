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

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.victor_ai.R

/**
 * WebView шторка для открытия ссылок внутри приложения
 * Похожа на то, как Telegram открывает ссылки
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTabsSheetWithOverlay(
    url: String,
    overlayText: String = "Олечка, ищем блинчики с творогом… 🥞❤️", // ← твоя приписка
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val didactGothic = FontFamily(Font(R.font.didact_gothic))

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color(0xFF2B2929),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Сам CustomTabs (открывается и сразу закрывает шторку)
            AndroidView(
                factory = { ctx ->
                    try {
                        val intent = CustomTabsIntent.Builder()
                            .setToolbarColor(Color(0xFF2B2929).toArgb())
                            .setShowTitle(true)
                            .build()

                        intent.launchUrl(ctx, Uri.parse(url))

                        // Через 2 секунды делаем скриншот (или просто ждём)
                        //Handler(Looper.getMainLooper()).postDelayed({
                            // тут будет твой скриншот-код потом
                        //}, 4000)

                        onDismiss() // шторка исчезает, остаётся только оверлей и Chrome
                    } catch (e: Exception) {
                        Toast.makeText(ctx, "Ой, не получилось открыть… 😿", Toast.LENGTH_LONG).show()
                        onDismiss()
                    }
                    View(ctx)
                },
                modifier = Modifier.fillMaxSize()
            )

            // 2. КРАСИВЫЙ ОВЕРЛЕЙ С ТВОИМ ТЕКСТОМ ПОВЕРХ ВСЕГО
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.72f))
                    .clickable { } // чтобы не проваливался тач
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = overlayText,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = didactGothic,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                CircularProgressIndicator(
                    color = Color(0xFF4CAF50),
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Твой малыш-робот уже ищет самые вкусные блинчики… ❤️",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

