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
import com.example.victor_ai.data.network.JournalEntryIn
import com.example.victor_ai.data.network.POIVisit
import com.example.victor_ai.ui.map.models.POI
import com.example.victor_ai.ui.map.models.VisitEmotion
import com.example.victor_ai.ui.map.models.VISIT_EMOTIONS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.time.Instant

/**
 * 📝 Менеджер для управления посещениями и журналом
 * 
 * Ответственность:
 * - Управление посещенными POI с эмоциями
 * - Сохранение записей в журнал
 * - Загрузка посещенных мест из журнала
 * - Управление визитами текущей сессии
 */
class VisitManager(
    private val placesApi: PlacesApi
) {
    companion object {
        private const val TAG = "VisitManager"
    }

    // Посещенные POI с эмоциями (хранится только в текущей сессии)
    private val _visitedPOIs = MutableStateFlow<Map<String, VisitEmotion>>(emptyMap())
    val visitedPOIs: StateFlow<Map<String, VisitEmotion>> = _visitedPOIs.asStateFlow()

    // Список посещений для текущей walk session
    private val _currentSessionVisits = mutableListOf<POIVisit>()
    val currentSessionVisits: List<POIVisit> get() = _currentSessionVisits.toList()

    private var currentSessionId: Int? = null

    /**
     * Отмечает POI как посещенное с эмоцией
     */
    suspend fun markPOIAsVisited(
        poi: POI,
        emotion: VisitEmotion?,
        walkedMeters: Double,
        isSearching: Boolean,
        onPOIUpdated: (POI) -> Unit
    ) {
        Log.d(TAG, "🏷️ markPOIAsVisited вызван")
        Log.d(TAG, "   - POI: ${poi.name} (id=${poi.id})")
        Log.d(TAG, "   - Эмоция: ${emotion?.name} ${emotion?.emoji}")

        if (emotion != null) {
            val visitDate = System.currentTimeMillis()
            
            // 🔄 Обновляем POI объект сразу для реактивности UI
            poi.isVisited = true
            poi.impression = emotion.name
            poi.visitDate = visitDate
            
            onPOIUpdated(poi)
            
            // Добавляем в карту посещенных (для текущей сессии)
            val newMap = _visitedPOIs.value + (poi.name to emotion)
            _visitedPOIs.value = newMap

            Log.d(TAG, "✅ POI добавлен в посещенные")
            Log.d(TAG, "   - Текущая карта посещений: ${_visitedPOIs.value.keys}")
            Log.d(TAG, "   - Размер карты: ${_visitedPOIs.value.size}")

            // Если идет walk session, добавляем в список посещений
            if (isSearching) {
                val visit = POIVisit(
                    account_id = UserProvider.getCurrentUserId(),
                    poi_id = poi.id,
                    poi_name = poi.name,
                    distance_from_start = walkedMeters.toFloat(),
                    found_at = Instant.now().toString(),
                    emotion_emoji = emotion.emoji,
                    emotion_label = emotion.name,
                    emotion_color = String.format("#%06X", (0xFFFFFF and emotion.color.value.toInt()))
                )
                _currentSessionVisits.add(visit)
                Log.d(TAG, "   - Добавлен в session visits (всего: ${_currentSessionVisits.size})")
            }

            // 📖 Сохраняем в journal на бэкенде
            saveJournalEntry(poi, emotion)
        } else {
            // Убираем из посещенных (если эмоция null)
            _visitedPOIs.value = _visitedPOIs.value - poi.name
            Log.d(TAG, "❌ POI удален из посещенных: ${poi.name}")
        }
    }

    /**
     * Проверяет, посещен ли POI
     */
    fun isPOIVisited(poiName: String): Boolean {
        val isVisited = _visitedPOIs.value.containsKey(poiName)
        Log.d(TAG, "🔍 isPOIVisited('$poiName') = $isVisited")
        return isVisited
    }

    /**
     * Получает эмоцию для посещенного POI
     */
    fun getVisitEmotion(poiName: String): VisitEmotion? {
        return _visitedPOIs.value[poiName]
    }

    /**
     * Загружает посещенные места из journal
     */
    suspend fun loadVisitedPlacesFromJournal() {
        withContext(Dispatchers.IO) {
            try {
                val response = placesApi.getJournalEntries(UserProvider.getCurrentUserId())
                if (response.isSuccessful) {
                    val entries = response.body() ?: emptyList()
                    Log.d(TAG, "✅ Загружено ${entries.size} записей из дневника")

                    // Парсим эмоции из текста
                    val visitedMap = mutableMapOf<String, VisitEmotion>()

                    entries.forEach { entry ->
                        entry.poi_name?.let { poiName ->
                            val emotion = parseEmotionFromText(entry.text)
                            if (emotion != null) {
                                visitedMap[poiName] = emotion
                                Log.d(TAG, "📍 Восстановлено посещение: $poiName -> ${emotion.name} ${emotion.emoji}")
                            }
                        }
                    }

                    _visitedPOIs.value = visitedMap
                    Log.d(TAG, "✅ Восстановлено ${visitedMap.size} посещенных мест")
                } else {
                    Log.e(TAG, "❌ Ошибка загрузки journal: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при загрузке journal", e)
            }
        }
    }

    /**
     * Очищает список визитов текущей сессии
     */
    fun clearSessionVisits() {
        _currentSessionVisits.clear()
    }

    /**
     * Устанавливает ID текущей сессии
     */
    fun setCurrentSessionId(sessionId: Int?) {
        currentSessionId = sessionId
    }

    /**
     * Сохраняет запись в дневник о посещении POI
     */
    private suspend fun saveJournalEntry(poi: POI, emotion: VisitEmotion) {
        withContext(Dispatchers.IO) {
            try {
                val dateOnly = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    java.time.LocalDate.now().toString()
                } else {
                    java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                        .format(java.util.Date())
                }

                val entry = JournalEntryIn(
                    date = dateOnly,
                    text = "Сидели в ${poi.name}. ${emotion.name} ${emotion.emoji}",
                    photo_path = null,
                    poi_name = poi.name,
                    session_id = currentSessionId,
                    account_id = UserProvider.getCurrentUserId()
                )

                val response = placesApi.createJournalEntry(entry)
                if (response.isSuccessful) {
                    Log.d(TAG, "✅ Запись в дневник сохранена для ${poi.name}")
                } else {
                    Log.e(TAG, "❌ Ошибка сохранения в дневник: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при сохранении в дневник", e)
            }
        }
    }

    /**
     * Парсит эмоцию из текста журнала
     * Формат: "Посетил {poi}. {name} {emoji}"
     */
    private fun parseEmotionFromText(text: String): VisitEmotion? {
        val emojiRegex = "[\\p{So}\\p{Sk}]".toRegex()
        val matches = emojiRegex.findAll(text).toList()

        if (matches.isEmpty()) return null

        val emoji = matches.last().value
        return VISIT_EMOTIONS.find { it.emoji == emoji }
    }
}

