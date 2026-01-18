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

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * 📍 Диалог для ввода адреса вручную с использованием Geocoder
 * 
 * Позволяет ввести адрес и получить координаты через Android Geocoder API
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressInputDialog(
    context: Context,
    onDismiss: () -> Unit,
    onAddressConfirmed: (latitude: Double, longitude: Double, address: String) -> Unit
) {
    var addressText by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var foundAddresses by remember { mutableStateOf<List<android.location.Address>>(emptyList()) }
    
    val scope = rememberCoroutineScope()
    val geocoder = remember { Geocoder(context) }

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
                // Иконка
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Заголовок
                Text(
                    text = "Введите адрес",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Описание
                Text(
                    text = "Укажите адрес, чтобы найти его координаты",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE0E0E0),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Поле ввода адреса
                OutlinedTextField(
                    value = addressText,
                    onValueChange = { 
                        addressText = it
                        errorMessage = null
                        foundAddresses = emptyList()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Адрес") },
                    placeholder = { Text("Например: Москва, Красная площадь") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF2196F3)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFF555555),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    singleLine = false,
                    maxLines = 3,
                    enabled = !isSearching
                )

                // Сообщение об ошибке
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFFF5252),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }

                // Найденные адреса
                if (foundAddresses.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Найденные адреса:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    foundAddresses.forEachIndexed { index, address ->
                        OutlinedButton(
                            onClick = {
                                val lat = address.latitude
                                val lon = address.longitude
                                val fullAddress = address.getAddressLine(0) ?: addressText
                                onAddressConfirmed(lat, lon, fullAddress)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = address.getAddressLine(0) ?: "Адрес $index",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                                Text(
                                    text = "📍 ${String.format("%.6f", address.latitude)}, ${String.format("%.6f", address.longitude)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFBBBBBB)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка: Найти координаты
                Button(
                    onClick = {
                        if (addressText.isBlank()) {
                            errorMessage = "Введите адрес"
                            return@Button
                        }

                        scope.launch {
                            isSearching = true
                            errorMessage = null
                            foundAddresses = emptyList()

                            try {
                                // Для Android 13+ (API 33+) используем новый API с callback
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val addresses = mutableListOf<Address>()
                                    
                                    withContext(Dispatchers.IO) {
                                        geocoder.getFromLocationName(addressText, 5) { results ->
                                            addresses.addAll(results)
                                        }
                                        // Даем время на обработку callback
                                        kotlinx.coroutines.delay(1000)
                                    }
                                    
                                    if (addresses.isEmpty()) {
                                        errorMessage = "Адрес не найден. Попробуйте другой запрос."
                                    } else {
                                        foundAddresses = addresses
                                        Log.d("AddressInput", "Найдено адресов: ${addresses.size}")
                                    }
                                } else {
                                    // Для старых версий Android используем устаревший метод
                                    @Suppress("DEPRECATION")
                                    val addresses = withContext(Dispatchers.IO) {
                                        geocoder.getFromLocationName(addressText, 5)
                                    }

                                    if (addresses.isNullOrEmpty()) {
                                        errorMessage = "Адрес не найден. Попробуйте другой запрос."
                                    } else {
                                        foundAddresses = addresses
                                        Log.d("AddressInput", "Найдено адресов: ${addresses.size}")
                                    }
                                }
                            } catch (e: IOException) {
                                Log.e("AddressInput", "Ошибка Geocoder", e)
                                errorMessage = "Ошибка сети. Проверьте подключение к интернету."
                            } catch (e: Exception) {
                                Log.e("AddressInput", "Неизвестная ошибка", e)
                                errorMessage = "Произошла ошибка: ${e.message}"
                            } finally {
                                isSearching = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isSearching && addressText.isNotBlank()
                ) {
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Поиск...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Найти координаты")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Кнопка: Отмена
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSearching
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

