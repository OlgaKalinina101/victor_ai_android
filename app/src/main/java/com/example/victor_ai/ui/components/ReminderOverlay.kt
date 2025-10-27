package com.example.victor_ai.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.victor_ai.model.ReminderPopup

@Composable
fun ReminderOverlay(
    popup: ReminderPopup,
    onOk: () -> Unit,
    onDelay: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier // ← добавили
)
 {
    Log.d("ReminderDebug", "Composable ReminderOverlay launched: $popup")

    // Центрируем маленький блок
     Box(
         modifier = modifier
             .fillMaxSize() // ← можно оставить, но лучше пусть `modifier` приходит снаружи
             .clickable(
                 indication = null,
                 interactionSource = remember { MutableInteractionSource() }
             ) { onDismiss() },
         contentAlignment = Alignment.Center
     ) {
        // Сам блок напоминания
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(0.92f),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C1C1E).copy(alpha = 0.92f) // ← тёмный полупрозрачный фон
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Заголовок
                Text(
                    "Напоминалка 🕊",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth() // 👈 Обязательно
                )

                Spacer(Modifier.height(8.dp))

                // Текст напоминания
                Text(
                    popup.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCCCCCC),
                    textAlign = TextAlign.Center, // 👈 Центрируем текст
                    modifier = Modifier.fillMaxWidth() // 👈 Центрируем контейнер
                )

                Spacer(Modifier.height(24.dp))

                // Две кнопки
                Row(
                    horizontalArrangement = Arrangement.Center, // 👈 Вместо spacedBy
                    modifier = Modifier.fillMaxWidth(),         // 👈 Центрируем строку
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Перенести
                    TextButton(
                        onClick = onDelay,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White // ← белый текст
                        )
                    ) {
                        Text("Перенести на час")
                    }

                    Spacer(modifier = Modifier.width(16.dp)) // 👈 Разделяем кнопки

                    // Выполнить
                    Button(
                        onClick = onOk,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2C2C2E), // ← тёмнее, чем фон карточки
                            contentColor = Color.White // ← белый текст
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Ок")
                    }
                }
            }
        }
    }
}