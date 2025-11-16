package com.example.victor_ai.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R

/**
 * Header чата с меню, заголовком и поиском
 */
@Composable
fun ChatHeader(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    currentMode: String
) {
    val didactGothicFont = FontFamily(Font(R.font.didact_gothic))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF2B2929))
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // [☰] Меню
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Меню",
                    tint = Color(0xFFE0E0E0)
                )
            }

            // Victor AI
            Text(
                text = "Victor AI",
                fontSize = 18.sp,
                color = Color(0xFFE0E0E0),
                fontWeight = FontWeight.Medium,
                fontFamily = didactGothicFont
            )

            // [🔍] Поиск
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = Color(0xFFE0E0E0)
                )
            }
        }
    }
}
