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

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.victor_ai.data.network.LocationListItem

/**
 * 🎨 Верхняя панель карты
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTopBar(
    visitedCount: Int,
    totalCount: Int,
    isGPSMode: Boolean,
    currentLocationName: String?,
    currentLocationId: Int?,
    availableLocations: List<LocationListItem>,
    onBackPressed: () -> Unit,
    onLocationMenuItemClick: (locationId: Int?) -> Unit, // null = GPS mode
    onDeleteLocation: (locationId: Int) -> Unit,
    onRefreshLocation: () -> Unit
) {
    var showLocationMenu by remember { mutableStateOf(false) }
    
    TopAppBar(
        title = { 
            Text(
                if (totalCount > 0) {
                    "Points - открыто $visitedCount из $totalCount"
                } else {
                    "Points"
                }
            ) 
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
        },
        actions = {
            // 📍 Кнопка выбора локации
            LocationMenuButton(
                isGPSMode = isGPSMode,
                currentLocationName = currentLocationName,
                currentLocationId = currentLocationId,
                availableLocations = availableLocations,
                showMenu = showLocationMenu,
                onShowMenuChange = { showLocationMenu = it },
                onLocationMenuItemClick = onLocationMenuItemClick,
                onDeleteLocation = onDeleteLocation
            )
            
            // 🔄 Кнопка сброса локации
            IconButton(
                onClick = onRefreshLocation,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFF555555)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Сбросить локацию",
                    tint = Color.White
                )
            }
        }
    )
}

/**
 * 📍 Кнопка с меню локаций
 */
@Composable
private fun LocationMenuButton(
    isGPSMode: Boolean,
    currentLocationName: String?,
    currentLocationId: Int?,
    availableLocations: List<LocationListItem>,
    showMenu: Boolean,
    onShowMenuChange: (Boolean) -> Unit,
    onLocationMenuItemClick: (locationId: Int?) -> Unit,
    onDeleteLocation: (locationId: Int) -> Unit
) {
    Box {
        IconButton(
            onClick = { onShowMenuChange(true) },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(0xFF555555)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Локация",
                    tint = Color.White
                )
                Text(
                    text = when {
                        isGPSMode -> "GPS"
                        currentLocationName != null && currentLocationId != null -> 
                            "$currentLocationName (#$currentLocationId)"
                        else -> "?"
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Выпадающее меню
        LocationDropdownMenu(
            expanded = showMenu,
            onDismissRequest = { onShowMenuChange(false) },
            isGPSMode = isGPSMode,
            currentLocationId = currentLocationId,
            availableLocations = availableLocations,
            onLocationSelected = { locationId ->
                onShowMenuChange(false)
                onLocationMenuItemClick(locationId)
            },
            onDeleteLocation = { locationId ->
                onShowMenuChange(false)
                onDeleteLocation(locationId)
            }
        )
    }
}

