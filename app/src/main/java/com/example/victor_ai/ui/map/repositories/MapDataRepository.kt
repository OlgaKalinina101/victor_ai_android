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

package com.example.victor_ai.ui.map.repositories

import android.util.Log
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.ui.map.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 📦 Репозиторий для загрузки данных карты с API
 * 
 * Ответственность:
 * - Загрузка мест вокруг GPS точки
 * - Загрузка мест для сохраненной локации
 * - Загрузка журнала посещений
 * - Конвертация данных из DTO в модели UI
 */
class MapDataRepository(
    private val placesApi: PlacesApi
) {
    companion object {
        private const val TAG = "MapDataRepository"
    }

    /**
     * Загружает данные карты вокруг указанной GPS точки
     */
    suspend fun loadPlacesAroundLocation(
        location: LatLng,
        radiusMeters: Int,
        visitedPlaceIds: Set<String> = emptySet()
    ): MapData = withContext(Dispatchers.IO) {
        val radiusKm = radiusMeters / 1000.0

        Log.d(TAG, "📦 Запрашиваем данные для lat=${location.lat}, lon=${location.lon}, radius=${radiusKm}км")

        val placesResponse = placesApi.getPlaces(
            latitude = location.lat,
            longitude = location.lon,
            radiusKm = radiusKm,
            limit = 15000
        )

        Log.d(TAG, "📥 Ответ от бэкенда: count=${placesResponse.count}, items.size=${placesResponse.items.size}")

        // 📖 Загружаем журнал с бэкенда
        val journalMap = loadJournalMap()
        
        val bounds = MapBounds.fromCenterAndRadius(location, radiusMeters)

        val mapData = MapDataConverter.fromBackendResponse(
            response = placesResponse,
            bounds = bounds,
            visitedPlaceIds = visitedPlaceIds
        )

        Log.d(TAG, "🔄 После конвертации: pois.size=${mapData.pois.size}")

        // 🎯 Мэтчим POI с записями журнала
        matchPOIsWithJournal(mapData.pois, journalMap)

        mapData.copy(userLocation = location)
    }

    /**
     * Загружает данные карты для сохраненной локации
     */
    suspend fun loadPlacesForLocation(
        locationId: Int,
        visitedPlaceIds: Set<String> = emptySet()
    ): Pair<MapData, String?> = withContext(Dispatchers.IO) {
        Log.d(TAG, "🗺️ Загружаем карту для локации ID=$locationId")

        val placesResponse = placesApi.getLocationPlaces(
            locationId = locationId,
            accountId = UserProvider.getCurrentUserId()
        )

        Log.d(TAG, "📥 Ответ от бэкенда:")
        Log.d(TAG, "   location='${placesResponse.location}'")
        Log.d(TAG, "   count=${placesResponse.count}")
        Log.d(TAG, "   items.size=${placesResponse.items.size}")

        // Загружаем журнал для мэтчинга
        val journalMap = loadJournalMap()

        // Вычисляем bbox из данных
        val bounds = calculateBoundsFromResponse(placesResponse)

        // Конвертируем данные
        val mapData = MapDataConverter.fromBackendResponse(
            response = placesResponse,
            bounds = bounds,
            visitedPlaceIds = visitedPlaceIds
        )
        Log.d(TAG, "🔄 После конвертации:")
        Log.d(TAG, "   POIs: ${mapData.pois.size}")
        Log.d(TAG, "   Background elements: ${mapData.backgroundElements.size}")

        // Мэтчим с журналом
        matchPOIsWithJournal(mapData.pois, journalMap)

        Log.d(TAG, "✅ Карта для локации '${placesResponse.location}' загружена")

        Pair(mapData, placesResponse.location)
    }

    /**
     * Загружает журнал и возвращает map: poi_name -> (emotion, date)
     */
    private suspend fun loadJournalMap(): Map<String, Pair<String?, Long>> {
        return try {
            val journalResponse = placesApi.getJournalEntries(UserProvider.getCurrentUserId())
            if (journalResponse.isSuccessful) {
                val entries = journalResponse.body() ?: emptyList()
                Log.d(TAG, "📖 Загружено записей журнала: ${entries.size}")
                
                entries.mapNotNull { entry ->
                    val poiName = entry.poi_name ?: return@mapNotNull null
                    val emotion = parseEmotionFromJournalText(entry.text)
                    val date = parseDateToTimestamp(entry.date)
                    
                    Log.d(TAG, "   📝 '$poiName': emotion='$emotion', date=$date")
                    poiName to Pair(emotion, date)
                }.toMap()
            } else {
                Log.e(TAG, "❌ Ошибка загрузки журнала: ${journalResponse.code()}")
                emptyMap()
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Исключение при загрузке журнала", e)
            emptyMap()
        }
    }

    /**
     * Мэтчит POI с записями журнала
     */
    private fun matchPOIsWithJournal(
        pois: List<POI>,
        journalMap: Map<String, Pair<String?, Long>>
    ) {
        var matchedCount = 0
        pois.forEach { poi ->
            val journalData = journalMap[poi.name]
            if (journalData != null) {
                poi.isVisited = true
                poi.impression = journalData.first
                poi.visitDate = journalData.second
                matchedCount++
                Log.d(TAG, "   ✅ POI '${poi.name}': impression='${poi.impression}', visitDate=${poi.visitDate}")
            }
        }
        
        Log.d(TAG, "🎯 Смэтчено POI с журналом: $matchedCount из ${pois.size}")
    }

    /**
     * Вычисляет bounds из ответа API
     */
    private fun calculateBoundsFromResponse(response: PlacesResponse): MapBounds {
        var minLat = Double.MAX_VALUE
        var maxLat = Double.MIN_VALUE
        var minLon = Double.MAX_VALUE
        var maxLon = Double.MIN_VALUE
        
        var pointsCount = 0

        response.items.forEach { item ->
            // Для точек (nodes)
            item.point?.let { point ->
                val lon = point[0]
                val lat = point[1]
                minLat = minOf(minLat, lat)
                maxLat = maxOf(maxLat, lat)
                minLon = minOf(minLon, lon)
                maxLon = maxOf(maxLon, lon)
                pointsCount++
            }

            // Для линий (ways)
            item.points?.forEach { point ->
                val lon = point[0]
                val lat = point[1]
                minLat = minOf(minLat, lat)
                maxLat = maxOf(maxLat, lat)
                minLon = minOf(minLon, lon)
                maxLon = maxOf(maxLon, lon)
                pointsCount++
            }

            // Для полигонов (relations)
            item.rings?.forEach { ring ->
                ring.forEach { point ->
                    val lon = point[0]
                    val lat = point[1]
                    minLat = minOf(minLat, lat)
                    maxLat = maxOf(maxLat, lat)
                    minLon = minOf(minLon, lon)
                    maxLon = maxOf(maxLon, lon)
                    pointsCount++
                }
            }
        }
        
        // Проверка валидности bounds
        if (pointsCount == 0 || minLat == Double.MAX_VALUE) {
            Log.w(TAG, "⚠️ Не найдено ни одной точки для вычисления bounds! Используем дефолтные.")
            // Дефолтные bounds (Москва)
            return MapBounds(
                minLat = 55.7,
                maxLat = 55.8,
                minLon = 37.6,
                maxLon = 37.7
            )
        }
        
        Log.d(TAG, "📐 Вычислены bounds: lat=$minLat..$maxLat, lon=$minLon..$maxLon (точек: $pointsCount)")

        return MapBounds(
            minLat = minLat,
            maxLat = maxLat,
            minLon = minLon,
            maxLon = maxLon
        )
    }

    /**
     * Парсит название эмоции из текста журнала по смайлику
     * Пример: "Сидели в Тануки. Впечатление: Неплохо 🙂" -> находим 🙂 -> возвращаем "Неплохо"
     */
    private fun parseEmotionFromJournalText(text: String): String? {
        val emojiPattern = """[\p{So}\p{Sk}]""".toRegex()
        val emojis = emojiPattern.findAll(text).map { it.value }.toList()
        
        emojis.forEach { emoji ->
            val emotion = VISIT_EMOTIONS.find { it.emoji == emoji }
            if (emotion != null) {
                return emotion.name
            }
        }
        
        return null
    }
    
    /**
     * Парсит дату из строки ISO или простого формата
     */
    private fun parseDateToTimestamp(dateStr: String): Long {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val date = java.time.LocalDate.parse(dateStr)
                date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
            } else {
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                sdf.parse(dateStr)?.time ?: System.currentTimeMillis()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка парсинга даты: $dateStr", e)
            System.currentTimeMillis()
        }
    }
}

