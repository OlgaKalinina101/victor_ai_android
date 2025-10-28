package com.example.victor_ai.ui.menu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PlacesMenu(onBack: () -> Unit) {
    Column {
        Text("Карта мест")
        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}