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

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.location.LocationProvider
import com.example.victor_ai.data.network.Achievement
import com.example.victor_ai.data.network.JournalEntry
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.repository.StatsRepository
import com.example.victor_ai.ui.map.models.PlaceElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placesApi: PlacesApi,
    private val statsRepository: StatsRepository,
    val locationProvider: LocationProvider
) : ViewModel() {

    private val _places = mutableStateOf<List<PlaceElement>>(emptyList())
    val places: State<List<PlaceElement>> = _places

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // Статистика
    private val _stats = mutableStateOf<StatsRepository.LocalStats?>(null)
    val stats: State<StatsRepository.LocalStats?> = _stats

    private val _lastJournalEntry = mutableStateOf<JournalEntry?>(null)
    val lastJournalEntry: State<JournalEntry?> = _lastJournalEntry

    private val _statsLoading = mutableStateOf(false)
    val statsLoading: State<Boolean> = _statsLoading

    // Последнее достижение
    private val _lastAchievement = mutableStateOf<Achievement?>(null)
    val lastAchievement: State<Achievement?> = _lastAchievement

    /**
     * Загружает места вокруг координаты
     */
    fun loadPlacesAround(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int = 2000,
        limit: Int = 1000
    ) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                // Конвертируем метры в километры
                val radiusKm = radiusMeters / 1000.0

                // Запрос к API
                val response = placesApi.getPlaces(
                    latitude = latitude,
                    longitude = longitude,
                    radiusKm = radiusKm,
                    limit = limit,
                    offset = 0
                )

                _places.value = response.items

                Log.d("PlacesVM", "Загружено мест: ${response.count}")

            } catch (e: Exception) {
                Log.e("PlacesVM", "Ошибка загрузки мест", e)
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Загружает все места (с большим радиусом)
     */
    fun loadAllPlaces(latitude: Double, longitude: Double, limit: Int = 1000) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val response = placesApi.getPlaces(
                    latitude = latitude,
                    longitude = longitude,
                    radiusKm = 2.0, // Стандартный радиус 2 км
                    limit = limit,
                    offset = 0
                )

                _places.value = response.items

            } catch (e: Exception) {
                Log.e("PlacesVM", "Ошибка загрузки мест", e)
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Загружает статистику пользователя
     */
    fun loadStats() {
        Log.d("PlacesVM", "🔍 loadStats() вызван")
        
        viewModelScope.launch {
            _statsLoading.value = true
            Log.d("PlacesVM", "🔍 statsLoading = true")

            try {
                // Сначала загружаем локальные данные
                Log.d("PlacesVM", "📂 Загружаем локальные данные...")
                
                val localStats = statsRepository.getLocalStats()
                _stats.value = localStats
                _lastJournalEntry.value = statsRepository.getLastJournalEntry()
                
                Log.d("PlacesVM", "✅ Локальные данные: dist=${localStats.todayDistance}, steps=${localStats.todaySteps}, lastUpdate=${localStats.lastUpdate}")

                // 🔥 КРИТИЧНО: Принудительная синхронизация при первом запуске (если lastUpdate == 0)
                val shouldSync = statsRepository.shouldSync() || localStats.lastUpdate == 0L
                Log.d("PlacesVM", "🔍 shouldSync=$shouldSync (lastUpdate=${localStats.lastUpdate})")
                
                if (shouldSync) {
                    Log.d("PlacesVM", "🌐 Начинаем синхронизацию с API...")
                    
                    val result = statsRepository.syncWithAPI()
                    result.onSuccess { updatedStats ->
                        _stats.value = updatedStats
                        _lastJournalEntry.value = statsRepository.getLastJournalEntry()
                        Log.d("PlacesVM", "✅ Синхронизировано! dist=${updatedStats.todayDistance}")
                    }.onFailure { e ->
                        Log.e("PlacesVM", "❌ Ошибка: ${e.message}", e)
                    }
                } else {
                    Log.d("PlacesVM", "⏭️ Пропуск синхронизации (данные свежие)")
                }

                // Загружаем последнее достижение
                Log.d("PlacesVM", "🏆 Загружаем последнее достижение...")
                loadLastAchievement()
            } catch (e: Exception) {
                Log.e("PlacesVM", "❌ Exception: ${e.message}", e)
            } finally {
                _statsLoading.value = false
                Log.d("PlacesVM", "🏁 loadStats() завершен, statsLoading = false")
            }
        }
    }

    /**
     * Загружает последнее разблокированное достижение
     */
    private fun loadLastAchievement() {
        viewModelScope.launch {
            try {
                Log.d("PlacesVM", "🌐 Запрашиваем достижения с API...")
                val response = placesApi.getAchievements()
                Log.d("PlacesVM", "📡 Ответ получен: isSuccessful=${response.isSuccessful}, code=${response.code()}")
                
                if (response.isSuccessful) {
                    val achievements = response.body() ?: emptyList()
                    // Берём последнее разблокированное достижение
                    _lastAchievement.value = achievements
                        .filter { it.unlocked_at != null }
                        .maxByOrNull { it.unlocked_at ?: "" }
                    Log.d("PlacesVM", "✅ Загружено достижений: ${achievements.size}, разблокировано: ${achievements.count { it.unlocked_at != null }}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PlacesVM", "❌ Ошибка загрузки достижений: ${response.code()}")
                    Log.e("PlacesVM", "   Тело ошибки: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("PlacesVM", "❌ Исключение при загрузке достижений: ${e.message}", e)
            }
        }
    }
}