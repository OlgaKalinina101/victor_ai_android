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
import com.example.victor_ai.R
import com.example.victor_ai.ui.menu.MenuState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background

@Composable
fun HorizontalScrollMenu(
    visible: Boolean,
    onMenuItemClick: (MenuState) -> Unit,
    modifier: Modifier = Modifier
) {
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

            // Браузер
            MenuText(
                text = "браузер",
                onClick = { /* TODO: Браузер в разработке */ }
            )

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

            // Дневник
            MenuText(
                text = "дневник",
                onClick = { /* TODO: Дневник в разработке */ }
            )
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
