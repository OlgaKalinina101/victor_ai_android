package com.example.victor_ai.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SystemMenu(onBack: () -> Unit) {
    Column {
        Text("Системные настройки")
        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}