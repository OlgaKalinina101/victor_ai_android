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

package com.example.victor_ai.ui.places

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * ⚠️ Диалог для обработки аномального скачка геолокации
 * 
 * Предлагает пользователю варианты:
 * - Использовать домашнюю локацию
 * - Ввести адрес вручную
 * - Отменить и попробовать позже
 */
@Composable
fun LocationAnomalyDialog(
    onDismiss: () -> Unit,
    onUseHomeLocation: () -> Unit,
    onEnterManually: () -> Unit,
    hasHomeLocation: Boolean
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF2B2929),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Иконка предупреждения
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Заголовок
                Text(
                    text = "Проблемы с геолокацией",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Описание
                Text(
                    text = "Обнаружен аномальный скачок координат (>20 км за 10 минут). Возможно GPS работает некорректно.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE0E0E0),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка: Использовать домашнюю локацию
                if (hasHomeLocation) {
                    Button(
                        onClick = onUseHomeLocation,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Использовать домашнюю локацию")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Кнопка: Ввести адрес вручную
                OutlinedButton(
                    onClick = onEnterManually,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ввести адрес вручную")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Кнопка: Отмена
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Отмена",
                        color = Color(0xFFE0E0E0)
                    )
                }
            }
        }
    }
}

