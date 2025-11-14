package com.example.victor_ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R

@Composable
fun BrowserScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2B2929)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "/* TODO: Браузер в разработке */",
            color = Color(0xFFFFD700), // желтый
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.didact_gothic))
        )
    }
}
