package com.example.victor_ai.ui.places

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.victor_ai.data.network.dto.GeoLocation
import com.example.victor_ai.ui.map.MapActivity

/**
 * 🗺️ Экран Places с нативной Android картой
 *
 * Отображает статистику мест и кнопку для открытия карты
 */
@Composable
fun PlacesMenu(
    onBack: () -> Unit,
    viewModel: PlacesViewModel
) {
    val context = LocalContext.current
    val places by viewModel.places
    val loading by viewModel.loading
    val error by viewModel.error

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // === Статистика ===
            when {
                loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Загрузка мест...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                error != null -> {
                    Text(
                        text = "Ошибка: $error",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    // Иконка карты
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = "Карта",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 24.dp),
                        tint = Color(0xFFE0E0E0)
                    )

                    // Статистика
                    Text(
                        text = "Карта интересных мест",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Исследуйте места вокруг вас",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFB0B0B0),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Кнопка открытия карты
                    Button(
                        onClick = {
                            MapActivity.start(context)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "Открыть карту",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
