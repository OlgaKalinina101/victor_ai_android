package com.example.victor_ai.ui.places

import com.google.gson.annotations.SerializedName

/**
 * 📍 Модели данных для интеграции Unity карты с Android
 *
 * Эти классы используются для:
 * 1. Передачи OSM данных из Android в Unity
 * 2. Получения событий из Unity в Android
 * 3. Синхронизации состояния между двумя системами
 */

// ════════════════════════════════════════════════════════════
// 📥 ДАННЫЕ ИЗ ANDROID → UNITY
// ════════════════════════════════════════════════════════════

/**
 * Главная структура данных карты
 * Отправляется из Android в Unity при загрузке/обновлении карты
 */
data class MapData(
    @SerializedName("bounds")
    val bounds: MapBounds,

    @SerializedName("pois")
    val pois: List<POI>,

    @SerializedName("roads")
    val roads: List<Road> = emptyList(), // Опционально

    @SerializedName("userLocation")
    val userLocation: LatLng? = null,

    @SerializedName("visitedPlaces")
    val visitedPlaces: Set<String> = emptySet() // ID посещенных мест
)

/**
 * Границы карты (GPS координаты)
 */
data class MapBounds(
    @SerializedName("minLat")
    val minLat: Double,

    @SerializedName("maxLat")
    val maxLat: Double,

    @SerializedName("minLon")
    val minLon: Double,

    @SerializedName("maxLon")
    val maxLon: Double
) {
    companion object {
        /**
         * Создает границы из центральной точки и радиуса
         * @param center Центральная точка
         * @param radiusMeters Радиус в метрах (например, 10000 = 10 км)
         */
        fun fromCenterAndRadius(center: LatLng, radiusMeters: Int): MapBounds {
            // Примерное преобразование метров в градусы
            // 1 градус ≈ 111 км
            val deltaLat = radiusMeters / 111000.0
            val deltaLon = radiusMeters / (111000.0 * kotlin.math.cos(Math.toRadians(center.lat)))

            return MapBounds(
                minLat = center.lat - deltaLat,
                maxLat = center.lat + deltaLat,
                minLon = center.lon - deltaLon,
                maxLon = center.lon + deltaLon
            )
        }
    }
}

/**
 * GPS координаты
 */
data class LatLng(
    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double
)

/**
 * Точка интереса (POI - Point Of Interest)
 */
data class POI(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: POIType,

    @SerializedName("location")
    val location: LatLng,

    @SerializedName("isVisited")
    var isVisited: Boolean = false,

    @SerializedName("impression")
    var impression: String? = null,

    @SerializedName("visitDate")
    var visitDate: Long? = null // Timestamp
)

/**
 * Типы точек интереса
 * Unity будет использовать это для выбора эмодзи
 */
enum class POIType(val osmTag: String, val emoji: String) {
    CAFE("cafe", "☕"),
    RESTAURANT("restaurant", "🍽️"),
    FAST_FOOD("fast_food", "🍔"),
    BAR("bar", "🍺"),
    PUB("pub", "🍺"),
    PARK("park", "🌳"),
    GARDEN("garden", "🌳"),
    MUSEUM("museum", "🖼️"),
    GALLERY("gallery", "🖼️"),
    CINEMA("cinema", "🎭"),
    THEATRE("theatre", "🎭"),
    SHOP("shop", "🛍️"),
    SUPERMARKET("supermarket", "🛒"),
    HOTEL("hotel", "🏨"),
    PHARMACY("pharmacy", "💊"),
    HOSPITAL("hospital", "🏥"),
    SCHOOL("school", "🎓"),
    UNIVERSITY("university", "🎓"),
    LIBRARY("library", "📚"),
    BANK("bank", "🏦"),
    ATM("atm", "🏦"),
    GYM("gym", "💪"),
    FITNESS("fitness_centre", "💪"),
    OTHER("other", "📍");

    companion object {
        /**
         * Определяет тип POI из OSM тегов
         */
        fun fromOsmTags(tags: Map<String, String>): POIType {
            val amenity = tags["amenity"]
            val shop = tags["shop"]
            val leisure = tags["leisure"]
            val tourism = tags["tourism"]

            val tag = amenity ?: shop ?: leisure ?: tourism ?: "other"

            return values().find { it.osmTag == tag } ?: OTHER
        }
    }
}

/**
 * Дорога (опционально, если хочешь отображать)
 */
data class Road(
    @SerializedName("points")
    val points: List<LatLng>,

    @SerializedName("type")
    val type: RoadType = RoadType.STREET
)

enum class RoadType {
    HIGHWAY,
    STREET,
    PATH
}

// ════════════════════════════════════════════════════════════
// 📤 СОБЫТИЯ ИЗ UNITY → ANDROID
// ════════════════════════════════════════════════════════════

/**
 * Событие: Пользователь кликнул на POI
 */
data class POIClickedEvent(
    @SerializedName("poiId")
    val poiId: String,

    @SerializedName("poiName")
    val poiName: String,

    @SerializedName("poiType")
    val poiType: String
)

/**
 * Событие: Пользователь отметил место как посещенное
 */
data class PlaceVisitedEvent(
    @SerializedName("poiId")
    val poiId: String,

    @SerializedName("impression")
    val impression: String,

    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Событие: Пользователь обновил впечатление
 */
data class ImpressionUpdatedEvent(
    @SerializedName("poiId")
    val poiId: String,

    @SerializedName("impression")
    val impression: String
)

/**
 * Событие: Карта готова к использованию
 */
data class MapReadyEvent(
    @SerializedName("isReady")
    val isReady: Boolean = true
)

// ════════════════════════════════════════════════════════════
// 🔄 ВСПОМОГАТЕЛЬНЫЕ МОДЕЛИ
// ════════════════════════════════════════════════════════════

/**
 * Ответ от Overpass API (OpenStreetMap)
 */
data class OverpassResponse(
    @SerializedName("version")
    val version: Double,

    @SerializedName("elements")
    val elements: List<OsmElement>
)

/**
 * Элемент OSM
 */
data class OsmElement(
    @SerializedName("type")
    val type: String, // "node", "way", "relation"

    @SerializedName("id")
    val id: Long,

    @SerializedName("lat")
    val lat: Double?,

    @SerializedName("lon")
    val lon: Double?,

    @SerializedName("tags")
    val tags: Map<String, String>?,

    @SerializedName("nodes")
    val nodes: List<Long>? = null,

    @SerializedName("geometry")
    val geometry: List<GeometryPoint>? = null
)

data class GeometryPoint(
    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double
)

// ════════════════════════════════════════════════════════════
// 🛠️ КОНВЕРТЕРЫ
// ════════════════════════════════════════════════════════════

/**
 * Конвертирует OSM данные в формат для Unity
 */
object MapDataConverter {

    /**
     * Преобразует Overpass ответ в MapData
     */
    fun fromOverpassResponse(
        response: PlacesResponse,
        bounds: MapBounds,
        visitedPlaceIds: Set<String> = emptySet()
    ): MapData {
        val pois = response.elements
            .filter { it.type == "node" && it.tags != null }
            .filter { element ->
                element.tags!!.containsKey("amenity") ||
                        element.tags.containsKey("shop") ||
                        element.tags.containsKey("leisure") ||
                        element.tags.containsKey("tourism")
            }
            .map { element ->
                POI(
                    id = element.id.toString(),
                    name = element.tags!!["name"] ?: element.tags["amenity"] ?: "Unknown",
                    type = POIType.fromOsmTags(element.tags),
                    location = LatLng(element.lat!!, element.lon!!),
                    isVisited = visitedPlaceIds.contains(element.id.toString())
                )
            }

        return MapData(
            bounds = bounds,
            pois = pois
        )
    }
}