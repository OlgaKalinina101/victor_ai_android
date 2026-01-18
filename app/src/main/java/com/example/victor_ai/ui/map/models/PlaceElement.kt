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

package com.example.victor_ai.ui.map.models

import com.squareup.moshi.Json

/**
 * 🗺️ Ответ от Places API
 */
data class PlacesResponse(
    val location: String? = null,  // Название локации (если запрос был для сохранённой локации)
    val items: List<PlaceElement>,
    val count: Int,
    val limit: Int,
    val offset: Int
)

/**
 * 📍 Элемент места из Places API
 */
data class PlaceElement(
    val id: Long,
    val type: String,
    val point: List<Double>? = null,
    val points: List<List<Double>>? = null,
    val rings: List<List<List<Double>>>? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "amenity") val amenity: String? = null,
    @Json(name = "shop") val shop: String? = null,
    @Json(name = "leisure") val leisure: String? = null,
    @Json(name = "tourism") val tourism: String? = null,
    
    // 🎨 Теги для фоновых слоев карты
    @Json(name = "landuse") val landuse: String? = null,
    @Json(name = "natural") val natural: String? = null,
    @Json(name = "waterway") val waterway: String? = null,
    @Json(name = "highway") val highway: String? = null,
    @Json(name = "building") val building: String? = null,
    @Json(name = "bridge") val bridge: String? = null,
    
    val tags: Map<String, String>? = null
) {
    /**
     * Собирает теги в Map для POIType.fromOsmTags
     */
    fun toTagsMap(): Map<String, String> = buildMap {
        amenity?.let { put("amenity", it) }
        name?.let { put("name", it) }
        shop?.let { put("shop", it) }
        leisure?.let { put("leisure", it) }
        tourism?.let { put("tourism", it) }
        
        // Фоновые теги
        landuse?.let { put("landuse", it) }
        natural?.let { put("natural", it) }
        waterway?.let { put("waterway", it) }
        highway?.let { put("highway", it) }
        building?.let { put("building", it) }
        bridge?.let { put("bridge", it) }

        // Добавляем дополнительные теги, если есть
        tags?.let { putAll(it) }
    }
}