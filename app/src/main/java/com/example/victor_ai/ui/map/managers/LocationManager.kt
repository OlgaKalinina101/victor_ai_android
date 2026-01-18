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

package com.example.victor_ai.ui.map.managers

import android.util.Log
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.network.LocationListItem
import com.example.victor_ai.ui.map.models.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * 📍 Менеджер для управления локациями
 * 
 * Ответственность:
 * - Управление GPS режимом и сохраненными локациями
 * - Загрузка списка доступных локаций
 * - Удаление локаций
 * - Переключение между GPS и сохраненной локацией
 */
class LocationManager(
    private val placesApi: PlacesApi
) {
    companion object {
        private const val TAG = "LocationManager"
    }

    // 📍 Локации
    private val _availableLocations = MutableStateFlow<List<LocationListItem>>(emptyList())
    val availableLocations: StateFlow<List<LocationListItem>> = _availableLocations.asStateFlow()
    
    private val _currentLocationName = MutableStateFlow<String?>(null)
    val currentLocationName: StateFlow<String?> = _currentLocationName.asStateFlow()
    
    private val _currentLocationId = MutableStateFlow<Int?>(null)
    val currentLocationId: StateFlow<Int?> = _currentLocationId.asStateFlow()
    
    // Режим: true = GPS, false = сохранённая локация
    private val _isGPSMode = MutableStateFlow(true)
    val isGPSMode: StateFlow<Boolean> = _isGPSMode.asStateFlow()
    
    // Сохранённая GPS позиция для возврата
    private var savedGPSLocation: LatLng? = null

    /**
     * Устанавливает GPS режим
     */
    fun setGPSMode(location: LatLng) {
        _isGPSMode.value = true
        _currentLocationName.value = null
        _currentLocationId.value = null
        savedGPSLocation = location
        
        Log.d(TAG, "📍 Режим: GPS, позиция сохранена")
        Log.d(TAG, "   isGPSMode: ${_isGPSMode.value}")
        Log.d(TAG, "   savedGPSLocation: $savedGPSLocation")
    }

    /**
     * Устанавливает режим сохраненной локации
     */
    fun setSavedLocationMode(locationId: Int, locationName: String) {
        _isGPSMode.value = false
        _currentLocationName.value = locationName
        _currentLocationId.value = locationId
        
        Log.d(TAG, "📍 Режим: Сохранённая локация '$locationName' (ID=$locationId)")
        Log.d(TAG, "   isGPSMode: ${_isGPSMode.value}")
    }

    /**
     * Возвращает сохраненную GPS позицию (для возврата к GPS режиму)
     */
    fun getSavedGPSLocation(): LatLng? = savedGPSLocation

    /**
     * 📍 Загружает список доступных локаций пользователя
     */
    suspend fun loadLocations() {
        withContext(Dispatchers.IO) {
            try {
                val response = placesApi.getLocations(UserProvider.getCurrentUserId())
                if (response.isSuccessful) {
                    val locations = response.body() ?: emptyList()
                    _availableLocations.value = locations
                    Log.d(TAG, "📍 Загружено локаций: ${locations.size}")
                } else {
                    Log.e(TAG, "❌ Ошибка загрузки локаций: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при загрузке локаций", e)
            }
        }
    }

    /**
     * 🗑️ Удаляет локацию (soft delete)
     */
    suspend fun deleteLocation(
        locationId: Int
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = placesApi.deleteLocation(locationId, UserProvider.getCurrentUserId())
            if (response.isSuccessful) {
                val result = response.body()
                Log.d(TAG, "🗑️ Локация удалена: ${result?.name}")
                
                // Обновляем список локаций
                _availableLocations.value = _availableLocations.value.filter { it.id != locationId }
                
                Result.success(result?.detail ?: "Локация успешно удалена")
            } else {
                val errorMsg = when (response.code()) {
                    404 -> "Локация не найдена"
                    400 -> "Локация уже удалена"
                    else -> "Ошибка ${response.code()}"
                }
                Log.e(TAG, "❌ Ошибка удаления локации: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Исключение при удалении локации", e)
            Result.failure(e)
        }
    }

    /**
     * Проверяет, является ли указанная локация текущей
     */
    fun isCurrentLocation(locationId: Int): Boolean {
        return _currentLocationId.value == locationId
    }
}

