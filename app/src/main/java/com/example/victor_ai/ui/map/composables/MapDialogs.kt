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

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.victor_ai.data.location.LocationProvider
import com.example.victor_ai.data.network.UnlockedAchievement
import com.example.victor_ai.data.repository.HomeWiFiRepository
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.places.AddressInputDialog
import com.example.victor_ai.ui.places.LocationAnomalyDialog

/**
 * 🗨️ Все диалоги карты в одном месте
 */
@Composable
fun MapDialogs(
    context: Context,
    showLocationAnomalyDialog: Boolean,
    showAddressInputDialog: Boolean,
    unlockedAchievements: List<UnlockedAchievement>,
    homeWiFiRepository: HomeWiFiRepository,
    locationProvider: LocationProvider,
    onDismissLocationAnomaly: () -> Unit,
    onDismissAddressInput: () -> Unit,
    onShowAddressInput: () -> Unit,
    onLocationUpdated: (LatLng) -> Unit,
    onDismissAchievements: () -> Unit
) {
    // Диалог для обработки аномалии геолокации
    if (showLocationAnomalyDialog) {
        val hasHomeLocation = homeWiFiRepository.isHomeWiFiSet()
        
        LocationAnomalyDialog(
            onDismiss = onDismissLocationAnomaly,
            onUseHomeLocation = {
                // Используем домашнюю локацию
                val homeCoords = homeWiFiRepository.getHomeCoordinates()
                if (homeCoords != null) {
                    locationProvider.setManualLocation(
                        lat = homeCoords.first,
                        lon = homeCoords.second,
                        source = "home"
                    )
                    onDismissLocationAnomaly()
                    Toast.makeText(context, "Установлена домашняя локация", Toast.LENGTH_SHORT).show()
                    
                    // Обновляем локацию на карте
                    onLocationUpdated(LatLng(homeCoords.first, homeCoords.second))
                }
            },
            onEnterManually = {
                // Показываем диалог ввода адреса
                onDismissLocationAnomaly()
                onShowAddressInput()
            },
            hasHomeLocation = hasHomeLocation
        )
    }
    
    // Диалог для ввода адреса вручную
    if (showAddressInputDialog) {
        AddressInputDialog(
            context = context,
            onDismiss = onDismissAddressInput,
            onAddressConfirmed = { latitude, longitude, address ->
                // Сохраняем локацию из введённого адреса
                locationProvider.setManualLocation(
                    lat = latitude,
                    lon = longitude,
                    source = "address_manual"
                )
                onDismissAddressInput()
                
                Toast.makeText(
                    context,
                    "Установлена локация: $address",
                    Toast.LENGTH_SHORT
                ).show()
                
                // Обновляем локацию на карте
                onLocationUpdated(LatLng(latitude, longitude))
            }
        )
    }
    
    // Диалог с достижениями
    if (unlockedAchievements.isNotEmpty()) {
        AchievementDialog(
            achievements = unlockedAchievements,
            onDismiss = onDismissAchievements
        )
    }
}

/**
 * 🏆 Диалог с разблокированными достижениями
 */
@Composable
fun AchievementDialog(
    achievements: List<UnlockedAchievement>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Заголовок
                Text(
                    text = "🏆 ${if (achievements.size > 1) "Достижения разблокированы!" else "Достижение разблокировано!"}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B2929),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Список достижений
                achievements.forEach { achievement ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        // Название
                        Text(
                            text = achievement.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFFD700),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Описание
                        Text(
                            text = achievement.description,
                            fontSize = 14.sp,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // Тип
                        Text(
                            text = "Тип: ${achievement.type}",
                            fontSize = 12.sp,
                            color = Color(0xFF999999),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Кнопка закрытия
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color(0xFF2B2929)
                    )
                ) {
                    Text(
                        text = "Ок",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

