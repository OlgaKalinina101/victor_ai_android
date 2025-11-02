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
    val type: String, // "node", "way", "relation"

    // Геометрия (одно из трёх)
    val point: List<Double>? = null,      // [lon, lat]
    val points: List<List<Double>>? = null, // [[lon, lat], ...]
    val rings: List<List<List<Double>>>? = null, // [[[lon, lat], ...]]

    // Все остальные поля из tags (динамически)
    @Json(name = "name") val name: String? = null,
    @Json(name = "amenity") val amenity: String? = null,
    @Json(name = "shop") val shop: String? = null,
    @Json(name = "leisure") val leisure: String? = null,
    @Json(name = "tourism") val tourism: String? = null,

    // Map для дополнительных тегов
    val tags: Map<String, String>? = null
)
