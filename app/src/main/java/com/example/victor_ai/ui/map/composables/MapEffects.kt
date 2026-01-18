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
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.victor_ai.ui.map.MapViewModel
import com.example.victor_ai.ui.map.canvas.MapCanvasView
import com.example.victor_ai.ui.map.models.LatLng
import com.example.victor_ai.ui.map.models.MapBounds
import com.example.victor_ai.ui.map.models.POI
import com.example.victor_ai.ui.map.models.BackgroundElement
import com.example.victor_ai.ui.map.renderer.MapRenderer
import com.example.victor_ai.ui.map.utils.LocationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ⏱️ Эффект для обновления счётчика времени поиска
 */
@Composable
fun SearchTimerEffect(
    searching: Boolean,
    searchStart: Long?,
    onElapsedUpdate: (Long) -> Unit
) {
    LaunchedEffect(searching, searchStart) {
        while (searching) {
            kotlinx.coroutines.delay(1000)
            val elapsed = ((System.currentTimeMillis() - (searchStart ?: System.currentTimeMillis())) / 1000)
            onElapsedUpdate(elapsed)
        }
    }
}

/**
 * 📍 Эффект для загрузки локаций при старте
 */
@Composable
fun LoadLocationsEffect(
    viewModel: MapViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadLocations()
    }
}

/**
 * 🗺️ Эффект для загрузки данных карты при старте
 */
@Composable
fun LoadMapDataEffect(
    getCurrentLocation: suspend () -> LatLng,
    onDataLoaded: (LatLng) -> Unit
) {
    LaunchedEffect(Unit) {
        val location = getCurrentLocation()
        onDataLoaded(location)
    }
}

/**
 * 🔄 Эффект для автоматической перезагрузки мест при смещении GPS
 */
@Composable
fun AutoReloadEffect(
    userLocation: LatLng?,
    lastLoadedCenter: LatLng?,
    searching: Boolean,
    pois: List<POI>,
    onReload: (LatLng) -> Unit
) {
    LaunchedEffect(userLocation) {
        userLocation?.let { currentLoc ->
            val lastCenter = lastLoadedCenter

            // Пропускаем если нет предыдущей загрузки, идёт поиск или места уже загружены
            if (lastCenter == null || searching || pois.isNotEmpty()) {
                return@LaunchedEffect
            }

            // Проверяем расстояние от последней загрузки
            val distance = LocationUtils.calculateDistance(lastCenter, currentLoc)

            // Если сместились больше чем на 500м и места пустые - перезагружаем
            if (distance > 500) {
                Log.d("MapEffects", "🔄 GPS улучшился, перезагружаем места (смещение ${distance.toInt()}м)")
                onReload(currentLoc)
            }
        }
    }
}

/**
 * 🎯 Эффект для обновления POI в режиме поиска
 */
@Composable
fun UpdatePOIsEffect(
    searching: Boolean,
    selectedPOI: POI?,
    nearby: List<POI>,
    pois: List<POI>,
    mapView: MapCanvasView?
) {
    LaunchedEffect(searching, selectedPOI, nearby) {
        Log.d("MapEffects", "🔄 UpdatePOIsEffect: searching=$searching, selectedPOI=${selectedPOI?.name}, nearby.size=${nearby.size}")
        if (searching && selectedPOI != null) {
            Log.d("MapEffects", "  ➡️ Режим поиска: обновляем POI -> selectedPOI + nearby = ${(listOf(selectedPOI) + nearby).size}")
            mapView?.updatePOIs((listOf(selectedPOI) + nearby) as List<POI>)
        } else if (!searching) {
            Log.d("MapEffects", "  ➡️ Обычный режим: обновляем POI -> все pois = ${pois.size}")
            mapView?.updatePOIs(pois)
        }
    }
}

/**
 * 🗺️ Эффект для инициализации карты при изменении данных
 */
@Composable
fun InitMapEffect(
    context: Context,
    mapBounds: MapBounds?,
    pois: List<POI>,
    backgroundElements: List<BackgroundElement>,
    userLocation: LatLng?,
    isLocationUpdatesStarted: Boolean,
    mapView: MapCanvasView?,
    mapRenderer: MapRenderer?,
    onStartLocationUpdates: () -> Unit
) {
    LaunchedEffect(mapBounds, pois, backgroundElements) {
        if (mapBounds != null) {
            mapView?.setMapData(mapBounds, pois, userLocation, backgroundElements)
            mapRenderer?.renderPOIs(pois)

            // Показываем Toast если POI не найдены
            if (pois.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "⚠️ Проблемы с геолокацией. Пытаемся загрузить до победного.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            // Запускаем location updates только один раз
            if (!isLocationUpdatesStarted) {
                onStartLocationUpdates()
            }
        }
    }
}

/**
 * 📍 Эффект для обновления позиции пользователя на карте
 */
@Composable
fun UpdateUserLocationEffect(
    userLocation: LatLng?,
    searching: Boolean,
    mapRenderer: MapRenderer?,
    mapBounds: MapBounds?,
    hasInitialCentered: Boolean,
    onInitialCentered: () -> Unit
) {
    LaunchedEffect(userLocation, searching) {
        userLocation?.let { loc ->
            mapRenderer?.updateUserLocation(loc)

            // Центрируем только при первой загрузке (не в режиме поиска!)
            if (!searching && mapRenderer != null && mapBounds != null && !hasInitialCentered) {
                mapRenderer.centerOnPoint(loc, 5f)
                onInitialCentered()
            }
        }
    }
}

/**
 * 🛤️ Эффект для обновления trail при изменении пути
 */
@Composable
fun UpdateTrailEffect(
    searching: Boolean,
    path: List<LatLng>,
    mapView: MapCanvasView?
) {
    LaunchedEffect(path) {
        if (searching) {
            mapView?.setTrail(path)
        }
    }
}

/**
 * ❌ Эффект для отображения ошибок через Toast
 * (теперь все ошибки показываются через кастомный экран, поэтому Toast не нужен)
 */
@Composable
fun ShowErrorEffect(
    context: Context,
    error: String?
) {
    // Больше не показываем Toast - все ошибки отображаются через MapLoadErrorScreen
    // LaunchedEffect оставляем для совместимости, но ничего не делаем
}

