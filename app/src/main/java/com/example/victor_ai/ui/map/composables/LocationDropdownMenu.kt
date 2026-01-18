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

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.victor_ai.data.network.LocationListItem

/**
 * 📍 Выпадающее меню для выбора локации
 */
@Composable
fun LocationDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    isGPSMode: Boolean,
    currentLocationId: Int?,
    availableLocations: List<LocationListItem>,
    onLocationSelected: (locationId: Int?) -> Unit, // null = GPS mode
    onDeleteLocation: (locationId: Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        // 📍 Пункт GPS (первым)
        GPSModeMenuItem(
            isSelected = isGPSMode,
            onClick = { onLocationSelected(null) }
        )
        
        // Разделитель
        HorizontalDivider()
        
        // 📍 Сохранённые локации
        availableLocations.forEach { location ->
            SavedLocationMenuItem(
                location = location,
                isSelected = !isGPSMode && currentLocationId == location.id,
                onLocationSelected = { onLocationSelected(location.id) },
                onDeleteLocation = { onDeleteLocation(location.id) }
            )
        }
    }
}

/**
 * 🌍 Пункт меню для GPS режима
 */
@Composable
private fun GPSModeMenuItem(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Маркер для GPS режима
                Text(
                    text = if (isSelected) ">" else " ",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Column {
                    Text(
                        text = "🌍 Текущая локация",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    Text(
                        text = "Points рядом",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        onClick = onClick
    )
}

/**
 * 📍 Пункт меню для сохранённой локации
 */
@Composable
private fun SavedLocationMenuItem(
    location: LocationListItem,
    isSelected: Boolean,
    onLocationSelected: () -> Unit,
    onDeleteLocation: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Маркер текущей локации
                    Text(
                        text = if (isSelected) ">" else " ",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Column {
                        Text(
                            text = "${location.name} (#${location.id})",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (location.description != null) {
                            Text(
                                text = location.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // 🗑️ Кнопка удаления
                IconButton(
                    onClick = onDeleteLocation,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Удалить локацию",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        onClick = onLocationSelected
    )
}

