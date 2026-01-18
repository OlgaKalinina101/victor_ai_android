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

package com.example.victor_ai.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 💾 Repository для управления посещенными местами
 *
 * Хранит данные в SharedPreferences:
 * - ID посещенных мест
 * - Впечатления о местах
 * - Даты посещений
 */
class VisitedPlacesRepository(context: Context) {

    companion object {
        private const val PREFS_NAME = "visited_places"
        private const val KEY_VISITED_IDS = "visited_ids"
        private const val KEY_IMPRESSIONS = "impressions"
        private const val KEY_VISIT_DATES = "visit_dates"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Модель посещенного места
     */
    data class VisitedPlace(
        val poiId: String,
        val impression: String,
        val visitDate: Long
    )

    /**
     * Получает все ID посещенных мест
     */
    fun getVisitedPlaceIds(): Set<String> {
        val json = prefs.getString(KEY_VISITED_IDS, null) ?: return emptySet()
        val type = object : TypeToken<Set<String>>() {}.type
        return gson.fromJson(json, type) ?: emptySet()
    }

    /**
     * Проверяет, было ли место посещено
     */
    fun isPlaceVisited(poiId: String): Boolean {
        return getVisitedPlaceIds().contains(poiId)
    }

    /**
     * Отмечает место как посещенное
     *
     * @param poiId ID места
     * @param impression Впечатление (например, "Понравилось", "Не понравилось")
     * @param visitDate Дата посещения (timestamp)
     */
    fun markPlaceAsVisited(
        poiId: String,
        impression: String,
        visitDate: Long = System.currentTimeMillis()
    ) {
        // Добавляем ID в список посещенных
        val visitedIds = getVisitedPlaceIds().toMutableSet()
        visitedIds.add(poiId)
        saveVisitedIds(visitedIds)

        // Сохраняем впечатление
        saveImpression(poiId, impression)

        // Сохраняем дату посещения
        saveVisitDate(poiId, visitDate)
    }

    /**
     * Отмечает место как найденное (без впечатления/даты).
     *
     * Используем это как "открыто" в игровом смысле.
     */
    fun markPlaceAsFound(poiId: String) {
        val visitedIds = getVisitedPlaceIds().toMutableSet()
        if (visitedIds.add(poiId)) {
            saveVisitedIds(visitedIds)
        }
    }

    /**
     * Получает впечатление о месте
     */
    fun getImpression(poiId: String): String? {
        val json = prefs.getString(KEY_IMPRESSIONS, null) ?: return null
        val type = object : TypeToken<Map<String, String>>() {}.type
        val impressions: Map<String, String> = gson.fromJson(json, type) ?: return null
        return impressions[poiId]
    }

    /**
     * Обновляет впечатление о месте
     */
    fun updateImpression(poiId: String, impression: String) {
        saveImpression(poiId, impression)
    }

    /**
     * Получает дату посещения места
     */
    fun getVisitDate(poiId: String): Long? {
        val json = prefs.getString(KEY_VISIT_DATES, null) ?: return null
        val type = object : TypeToken<Map<String, Long>>() {}.type
        val dates: Map<String, Long> = gson.fromJson(json, type) ?: return null
        return dates[poiId]
    }

    /**
     * Получает все посещенные места с полной информацией
     */
    fun getAllVisitedPlaces(): List<VisitedPlace> {
        val visitedIds = getVisitedPlaceIds()
        return visitedIds.mapNotNull { poiId ->
            val impression = getImpression(poiId) ?: return@mapNotNull null
            val visitDate = getVisitDate(poiId) ?: return@mapNotNull null
            VisitedPlace(poiId, impression, visitDate)
        }
    }

    /**
     * Удаляет место из посещенных
     */
    fun removeVisitedPlace(poiId: String) {
        // Удаляем из списка посещенных
        val visitedIds = getVisitedPlaceIds().toMutableSet()
        visitedIds.remove(poiId)
        saveVisitedIds(visitedIds)

        // Удаляем впечатление
        val impressions = getImpressionsMap().toMutableMap()
        impressions.remove(poiId)
        saveImpressionsMap(impressions)

        // Удаляем дату
        val dates = getVisitDatesMap().toMutableMap()
        dates.remove(poiId)
        saveVisitDatesMap(dates)
    }

    /**
     * Очищает все посещенные места
     */
    fun clearAll() {
        prefs.edit().clear().apply()
    }

    // ════════════════════════════════════════════════════════════
    // Приватные вспомогательные методы
    // ════════════════════════════════════════════════════════════

    private fun saveVisitedIds(ids: Set<String>) {
        val json = gson.toJson(ids)
        prefs.edit().putString(KEY_VISITED_IDS, json).apply()
    }

    private fun saveImpression(poiId: String, impression: String) {
        val impressions = getImpressionsMap().toMutableMap()
        impressions[poiId] = impression
        saveImpressionsMap(impressions)
    }

    private fun saveVisitDate(poiId: String, date: Long) {
        val dates = getVisitDatesMap().toMutableMap()
        dates[poiId] = date
        saveVisitDatesMap(dates)
    }

    private fun getImpressionsMap(): Map<String, String> {
        val json = prefs.getString(KEY_IMPRESSIONS, null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(json, type) ?: emptyMap()
    }

    private fun saveImpressionsMap(impressions: Map<String, String>) {
        val json = gson.toJson(impressions)
        prefs.edit().putString(KEY_IMPRESSIONS, json).apply()
    }

    private fun getVisitDatesMap(): Map<String, Long> {
        val json = prefs.getString(KEY_VISIT_DATES, null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, Long>>() {}.type
        return gson.fromJson(json, type) ?: emptyMap()
    }

    private fun saveVisitDatesMap(dates: Map<String, Long>) {
        val json = gson.toJson(dates)
        prefs.edit().putString(KEY_VISIT_DATES, json).apply()
    }
}
