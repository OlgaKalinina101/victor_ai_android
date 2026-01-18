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
import com.example.victor_ai.data.network.POIVisit
import com.example.victor_ai.data.network.StepPoint
import com.example.victor_ai.data.network.UnlockedAchievement
import com.example.victor_ai.data.network.WalkSessionCreate
import com.example.victor_ai.data.repository.StatsRepository
import com.example.victor_ai.ui.map.models.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

/**
 * 🚶 Менеджер для управления walk sessions
 * 
 * Ответственность:
 * - Сохранение walk session на бэкенд
 * - Конвертация path в StepPoint
 * - Обновление локальной статистики
 */
class WalkSessionManager(
    private val placesApi: PlacesApi,
    private val statsRepository: StatsRepository?
) {
    companion object {
        private const val TAG = "WalkSessionManager"
    }

    /**
     * Результат сохранения прогулки
     */
    data class SaveResult(
        val sessionId: Int,
        val unlockedAchievements: List<UnlockedAchievement>
    )

    /**
     * Сохраняет walk session на бэкенд
     * @return SaveResult с ID сессии и разблокированными достижениями, или null если ошибка
     */
    suspend fun saveWalkSession(
        startTime: Long,
        walkedMeters: Double,
        path: List<LatLng>,
        visits: List<POIVisit>
    ): SaveResult? {
        Log.d(TAG, "🔥 saveWalkSession() ВЫЗВАН с startTime=$startTime")

        return withContext(Dispatchers.IO) {
            Log.d(TAG, "🔥 saveWalkSession() корутина ЗАПУЩЕНА")

            try {
                val endTime = System.currentTimeMillis()

                Log.d(TAG, "📦 Подготовка walk session для отправки...")
                Log.d(TAG, "   - Дистанция: $walkedMeters м")
                Log.d(TAG, "   - Время: ${(endTime - startTime) / 1000} сек")
                Log.d(TAG, "   - Путь: ${path.size} точек")
                Log.d(TAG, "   - Посещения: ${visits.size}")

                // Конвертируем path в StepPoint
                val stepPoints = path.mapIndexed { index, latLng ->
                    StepPoint(
                        lat = latLng.lat,
                        lon = latLng.lon,
                        timestamp = Instant.ofEpochMilli(startTime + (index * 5000L)).toString()
                    )
                }

                // Примерный расчет шагов (1 шаг ≈ 0.75 метра)
                val steps = (walkedMeters / 0.75).toInt()

                val walkSession = WalkSessionCreate(
                    account_id = UserProvider.getCurrentUserId(),
                    start_time = Instant.ofEpochMilli(startTime).toString(),
                    end_time = Instant.ofEpochMilli(endTime).toString(),
                    distance_m = walkedMeters.toFloat(),
                    steps = steps,
                    mode = "search",
                    notes = "Прогулка с поиском точек интереса",
                    poi_visits = visits,
                    step_points = stepPoints
                )

                Log.d(TAG, "📡 Отправляем walk session на бэкенд:")
                Log.d(TAG, "   URL: POST /api/walk_sessions/")
                Log.d(TAG, "   account_id: ${walkSession.account_id}")
                Log.d(TAG, "   distance_m: ${walkSession.distance_m}")
                Log.d(TAG, "   steps: ${walkSession.steps}")
                Log.d(TAG, "   poi_visits: ${walkSession.poi_visits.size}")
                Log.d(TAG, "   step_points: ${walkSession.step_points.size}")

                val response = placesApi.createWalkSession(walkSession)

                Log.d(TAG, "📥 Ответ от бэкенда:")
                Log.d(TAG, "   HTTP код: ${response.code()}")
                Log.d(TAG, "   Успешно: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val body = response.body()
                    val sessionId = body?.session_id
                    val achievements = body?.unlocked_achievements ?: emptyList()
                    
                    Log.d(TAG, "✅ Walk session сохранена с ID: $sessionId")
                    
                    if (achievements.isNotEmpty()) {
                        Log.d(TAG, "🏆 Разблокировано достижений: ${achievements.size}")
                        achievements.forEach { achievement ->
                            Log.d(TAG, "   - ${achievement.name}: ${achievement.description}")
                        }
                    }

                    // Обновляем локальную статистику
                    statsRepository?.let {
                        Log.d(TAG, "💾 Обновляем локальную статистику...")
                        it.addTodayDistance(walkedMeters.toFloat())
                        it.addTodaySteps(steps)
                        Log.d(TAG, "✅ Локальная статистика обновлена: +${walkedMeters}м, +${steps} шагов")
                    } ?: Log.w(TAG, "⚠️ statsRepository == null, локальная статистика НЕ обновлена!")
                    
                    if (sessionId != null) {
                        SaveResult(sessionId, achievements)
                    } else {
                        null
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "❌ Ошибка сохранения walk session:")
                    Log.e(TAG, "   HTTP код: ${response.code()}")
                    Log.e(TAG, "   Тело ошибки: $errorBody")
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при сохранении walk session", e)
                Log.e(TAG, "   Exception: ${e.message}")
                Log.e(TAG, "   Тип: ${e.javaClass.simpleName}")
                null
            }
        }
    }
}

