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

package com.example.victor_ai.ui.menu.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.BuildConfig
import com.example.victor_ai.R
import com.example.victor_ai.ui.menu.MenuState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background

@Composable
fun HorizontalScrollMenu(
    visible: Boolean,
    accountId: String,
    onMenuItemClick: (MenuState) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCreator = accountId == BuildConfig.TEST_USER_ID

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 300)
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .background(Color.Transparent)
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Места
            MenuText(
                text = "места",
                onClick = { onMenuItemClick(MenuState.PLACES) }
            )

            if (isCreator) {
                // Браузер
                MenuText(
                    text = "браузер",
                    onClick = { onMenuItemClick(MenuState.BROWSER) }
                )
            }

            // Системное
            MenuText(
                text = "системное",
                onClick = { onMenuItemClick(MenuState.SYSTEM) }
            )

            // Расписание
            MenuText(
                text = "расписание",
                onClick = { onMenuItemClick(MenuState.CALENDAR) }
            )

            // Плейлист
            MenuText(
                text = "плейлист",
                onClick = { onMenuItemClick(MenuState.PLAYLIST) }
            )

            if (isCreator) {
                // Среда
                MenuText(
                    text = "среда",
                    onClick = { onMenuItemClick(MenuState.ENVIRONMENT) }
                )
            }
        }
    }
}

@Composable
private fun MenuText(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily(Font(R.font.didact_gothic)),
            color = Color(0xFFA6A6A6),
            fontSize = 28.sp
        ),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
