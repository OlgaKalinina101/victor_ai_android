package com.example.victor_ai.ui.places

import com.squareup.moshi.Json

/**
 * 🗺️ Ответ от Places API
 */
data class PlacesResponse(
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

        // Добавляем дополнительные теги, если есть
        tags?.let { putAll(it) }
    }
}