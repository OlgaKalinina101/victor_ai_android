package com.example.victor_ai.ui.places

import com.example.victor_ai.data.network.dto.GeoLocation

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
    val bounds: MapBounds,

    val pois: List<POI>,

    val roads: List<Road> = emptyList(), // Опционально

    val userLocation: LatLng? = null,

    val visitedPlaces: Set<String> = emptySet() // ID посещенных мест
)

/**
 * Границы карты (GPS координаты)
 */
data class MapBounds(
    val minLat: Double,

    val maxLat: Double,

    val minLon: Double,

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
 * Используем GeoLocation из DTO для унификации с Moshi
 */
typealias LatLng = GeoLocation

/**
 * Точка интереса (POI - Point Of Interest)
 */
data class POI(
    val id: String,

    val name: String,

    val type: POIType,

    val location: LatLng,

    var isVisited: Boolean = false,

    var impression: String? = null,

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
    val points: List<LatLng>,

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
    val poiId: String,

    val poiName: String,

    val poiType: String
)

/**
 * Событие: Пользователь отметил место как посещенное
 */
data class PlaceVisitedEvent(
    val poiId: String,

    val impression: String,

    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Событие: Пользователь обновил впечатление
 */
data class ImpressionUpdatedEvent(
    val poiId: String,

    val impression: String
)

/**
 * Событие: Карта готова к использованию
 */
/**
 * Ответ от собственного бэкенда
 */
/**
 * Ответ от собственного бэкенда
 */

data class PlaceItem(
    val id: Long,

    val type: String, // "node", "way", "relation"

    // Точка (для type = "node")
    val point: List<Double>?, // [lon, lat]

    // Линия (для type = "way")
    val points: List<List<Double>>?, // [[lon, lat], ...]

    // Полигон (для type = "relation")
    val rings: List<List<List<Double>>>?, // [[[lon, lat], ...]]

    // OSM теги (amenity, name, shop и т.д.)
    // Они уже распакованы в корень объекта
    val amenity: String? = null,

    val name: String? = null,

    val shop: String? = null,

    val leisure: String? = null,

    val tourism: String? = null
) {
    /**
     * Собирает теги обратно в Map для совместимости с POIType.fromOsmTags
     */
    fun toTagsMap(): Map<String, String> = buildMap {
        amenity?.let { put("amenity", it) }
        name?.let { put("name", it) }
        shop?.let { put("shop", it) }
        leisure?.let { put("leisure", it) }
        tourism?.let { put("tourism", it) }
    }
}

/**
 * Элемент OSM - тоже используем GeoLocation
 */
typealias GeometryPoint = GeoLocation

// ════════════════════════════════════════════════════════════
// 🛠️ КОНВЕРТЕРЫ
// ════════════════════════════════════════════════════════════

/**
 * Конвертирует OSM данные в формат для Unity
 */
object MapDataConverter {

    fun fromBackendResponse(
        response: PlacesResponse,
        bounds: MapBounds,
        visitedPlaceIds: Set<String> = emptySet()
    ): MapData {
        val pois = response.items
            .filter { it.type == "node" && it.point != null }
            .filter { item ->
                item.amenity != null ||
                        item.shop != null ||
                        item.leisure != null ||
                        item.tourism != null
            }
            .map { item ->
                val tags = item.toTagsMap()

                POI(
                    id = item.id.toString(),
                    name = (tags["name"] ?: tags["amenity"] ?: "Unknown") as String,
                    type = POIType.fromOsmTags(tags),
                    location = LatLng(
                        lat = item.point!![1], // point = [lon, lat]
                        lon = item.point[0]
                    ),
                    isVisited = visitedPlaceIds.contains(item.id.toString())
                )
            }

        return MapData(
            bounds = bounds,
            pois = pois
        )
    }
}