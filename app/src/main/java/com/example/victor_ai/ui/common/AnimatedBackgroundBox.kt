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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.victor_ai.R

@Composable
fun AnimatedBackgroundBox(content: @Composable BoxScope.() -> Unit) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.background)
                .build(),
            imageLoader = imageLoader,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        content() // 🔥 всё, что ты хочешь поверх гифки
    }
}