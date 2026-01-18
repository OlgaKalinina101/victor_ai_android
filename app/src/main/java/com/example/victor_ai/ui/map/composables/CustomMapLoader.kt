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

package com.example.victor_ai.ui.map.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 🎨 Кастомный загрузчик карты с меняющимися фразами
 */
@Composable
fun CustomMapLoader(
    modifier: Modifier = Modifier
) {
    // Список фраз, которые будут сменяться каждые 5 секунд
    val loadingPhrases = listOf(
        "Ищу нашу точку на карте...",
        "Смотрю в OpenStreetMap...",
        "Качаю локацию...",
        "Рисую дорожки для прогулки...",
        "Маркирую интересные места...",
        "Финальные штрихи..."
    )

    var currentPhraseIndex by remember { mutableStateOf(0) }

    // Меняем фразу каждые 5 секунд
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentPhraseIndex = (currentPhraseIndex + 1) % loadingPhrases.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = Color(0xFF2B2929),
                strokeWidth = 4.dp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = loadingPhrases[currentPhraseIndex],
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                color = Color(0xFF2B2929),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

/**
 * 💔 Экран ошибки с кастомными сообщениями для разных типов ошибок
 */
@Composable
fun MapLoadErrorScreen(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    val errorData = getErrorData(errorMessage)
    
    // Анимация появления эмодзи (масштабирование)
    val scale by rememberInfiniteTransition(label = "emoji_scale").animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emoji_scale_animation"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text(
                text = errorData.emoji,
                style = MaterialTheme.typography.displayLarge,
                fontSize = (72 * scale).sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = errorData.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                color = Color(0xFF2B2929),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = errorData.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF2B2929).copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Данные для отображения ошибки
 */
private data class ErrorDisplayData(
    val emoji: String,
    val title: String,
    val subtitle: String
)

/**
 * Возвращает данные для отображения в зависимости от типа ошибки
 */
private fun getErrorData(errorMessage: String): ErrorDisplayData {
    return when {
        // Ошибка 500 - таймаут на бэкенде
        errorMessage.contains("500") -> ErrorDisplayData(
            emoji = "😔",
            title = "Не хватило времени для загрузки 😔",
            subtitle = "Давай увеличим timeout на бэкенде?"
        )
        
        // Ошибка 503 - сервер недоступен
        errorMessage.contains("503") -> ErrorDisplayData(
            emoji = "🔧",
            title = "Сервер на техобслуживании 🛠️",
            subtitle = "Может, ngrok туннель упал? Или бэкенд перезапускается?"
        )
        
        // Ошибка 404 - не найдено
        errorMessage.contains("404") -> ErrorDisplayData(
            emoji = "🗺️",
            title = "Локация потерялась 🧭",
            subtitle = "Такой точки на карте не нашлось... Может, она удалена?"
        )
        
        // Ошибка 403 - нет доступа
        errorMessage.contains("403") -> ErrorDisplayData(
            emoji = "🔒",
            title = "Доступ закрыт 🚫",
            subtitle = "Похоже, у нас нет прав на эту территорию..."
        )
        
        // Таймаут соединения
        errorMessage.contains("таймаут", ignoreCase = true) || 
        errorMessage.contains("timeout", ignoreCase = true) -> ErrorDisplayData(
            emoji = "⏰",
            title = "Время вышло ⌛",
            subtitle = "Сервер долго думал... Может, интернет тормозит?"
        )
        
        // Нет интернета
        errorMessage.contains("интернет", ignoreCase = true) ||
        errorMessage.contains("resolve host", ignoreCase = true) -> ErrorDisplayData(
            emoji = "📡",
            title = "Потеряли связь со спутником 🛰️",
            subtitle = "Проверь, включен ли интернет?"
        )
        
        // Неизвестная ошибка - дефолтное сообщение
        else -> ErrorDisplayData(
            emoji = "🤔",
            title = "Что-то пошло не так...",
            subtitle = "Попробуй ещё раз? Или расскажи разрабам про эту ошибку 🐛"
        )
    }
}
