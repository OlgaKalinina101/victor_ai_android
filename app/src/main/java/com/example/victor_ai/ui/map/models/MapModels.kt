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

import com.example.victor_ai.data.network.dto.GeoLocation

/**
 * 📍 Модели данных для Android карты
 *
 * Эти классы используются для:
 * 1. Хранения данных карты, полученных из Places API
 * 2. Управления состоянием POI (точек интереса)
 * 3. Отслеживания посещений и прогресса пользователя
 */

// ════════════════════════════════════════════════════════════
// 📥 ОСНОВНЫЕ МОДЕЛИ КАРТЫ
// ════════════════════════════════════════════════════════════

/**
 * Главная структура данных карты
 * Используется для инициализации и обновления карты
 */
data class MapData(
    val bounds: MapBounds,

    val pois: List<POI>,

    val roads: List<Road> = emptyList(), // Опционально

    val userLocation: LatLng? = null,

    val visitedPlaces: Set<String> = emptySet(), // ID посещенных мест
    
    val backgroundElements: List<BackgroundElement> = emptyList() // 🎨 Фоновые слои
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

    var visitDate: Long? = null, // Timestamp

    /**
     * Исходные данные из Places API (PlaceElement).
     * Нужны для "комикс-облачка" и потенциальной логики/аналитики.
     */
    val elementType: String? = null,
    val tags: Map<String, String> = emptyMap()
)

/**
 * Типы точек интереса
 * Используется для отображения эмодзи на карте
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
// 📤 СОБЫТИЯ И ВЗАИМОДЕЙСТВИЯ
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
 * Конвертирует данные из Places API в модели карты
 */
object MapDataConverter {

    fun fromBackendResponse(
        response: PlacesResponse,
        bounds: MapBounds,
        visitedPlaceIds: Set<String> = emptySet()
    ): MapData {
        // 📍 Парсим POI (точки с amenity/shop/leisure/tourism)
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
                    isVisited = visitedPlaceIds.contains(item.id.toString()),
                    elementType = item.type,
                    tags = tags
                )
            }

        // 🎨 Парсим фоновые элементы (way/relation с landuse/natural/highway/building)
        val wayAndRelationItems = response.items.filter { it.type in listOf("way", "relation") }
        android.util.Log.d("MapDataConverter", "📊 Всего элементов в response: ${response.items.size}")
        android.util.Log.d("MapDataConverter", "   - nodes: ${response.items.count { it.type == "node" }}")
        android.util.Log.d("MapDataConverter", "   - ways: ${response.items.count { it.type == "way" }}")
        android.util.Log.d("MapDataConverter", "   - relations: ${response.items.count { it.type == "relation" }}")
        
        val backgroundElements = wayAndRelationItems.mapNotNull { parseBackgroundElement(it) }
        android.util.Log.d("MapDataConverter", "🎨 Фоновых элементов после парсинга: ${backgroundElements.size}")
        
        if (wayAndRelationItems.isNotEmpty() && backgroundElements.isEmpty()) {
            android.util.Log.w("MapDataConverter", "⚠️ Есть way/relation, но ни один не распарсился в фон!")
            wayAndRelationItems.take(5).forEach { item ->
                android.util.Log.d("MapDataConverter", "   Пример: type=${item.type}, tags=${item.toTagsMap()}")
            }
        }

        return MapData(
            bounds = bounds,
            pois = pois,
            backgroundElements = backgroundElements
        )
    }
    
    /**
     * Парсит фоновый элемент карты
     */
    private fun parseBackgroundElement(item: PlaceElement): BackgroundElement? {
        val tags = item.toTagsMap()
        
        android.util.Log.d("MapDataConverter", "🔍 parseBackgroundElement: id=${item.id}, type=${item.type}, tags=$tags")
        
        // Определяем слой и цвет по тегам
        val (layer, color) = when {
            // ВОДА
            tags["natural"] == "water" || tags["waterway"] == "riverbank" ->
                BackgroundLayer.WATER to MapColors.WATER
            
            // ЗЕЛЕНЬ
            tags["leisure"] == "park" || tags["leisure"] == "garden" ->
                BackgroundLayer.GREENERY to MapColors.PARK
            tags["landuse"] == "forest" ->
                BackgroundLayer.GREENERY to MapColors.FOREST
            tags["landuse"] == "meadow" || tags["landuse"] == "grass" ->
                BackgroundLayer.GREENERY to MapColors.MEADOW
            
            // ДОРОГИ
            tags["highway"] in listOf("motorway", "trunk", "primary", "secondary") ->
                BackgroundLayer.ROADS to MapColors.ROAD
            tags["bridge"] == "yes" ->
                BackgroundLayer.ROADS to MapColors.BRIDGE
            
            // ЗДАНИЯ
            tags["building"] in listOf("commercial", "retail") || tags["building"] != null ->
                BackgroundLayer.BUILDINGS to MapColors.BUILDING
            
            else -> {
                android.util.Log.d("MapDataConverter", "   ❌ Не подходит под фоновые категории")
                return null // Пропускаем неизвестные типы
            }
        }
        
        android.util.Log.d("MapDataConverter", "   ✅ Подходит: layer=$layer")
        
        // Конвертируем геометрию
        val geometry = when (item.type) {
            "way" -> {
                if (item.points == null) {
                    android.util.Log.w("MapDataConverter", "   ❌ item.points == null для way id=${item.id}")
                    null
                } else if (item.points.isEmpty()) {
                    android.util.Log.w("MapDataConverter", "   ❌ item.points.isEmpty() для way id=${item.id}")
                    null
                } else {
                    // Линия или замкнутый полигон
                    val latLngs = item.points.map { LatLng(lat = it[1], lon = it[0]) }
                    
                    android.util.Log.d("MapDataConverter", "   ✅ way имеет ${item.points.size} точек")
                    
                    // Если первая точка == последняя, это полигон
                    if (latLngs.size > 2 && latLngs.first() == latLngs.last()) {
                        BackgroundGeometry.Polygon(listOf(latLngs))
                    } else {
                        BackgroundGeometry.LineString(latLngs)
                    }
                }
            }
            "relation" -> {
                if (item.rings == null) {
                    android.util.Log.w("MapDataConverter", "   ❌ item.rings == null для relation id=${item.id}")
                    null
                } else if (item.rings.isEmpty()) {
                    android.util.Log.w("MapDataConverter", "   ❌ item.rings.isEmpty() для relation id=${item.id}")
                    null
                } else {
                    // Мультиполигон
                    android.util.Log.d("MapDataConverter", "   ✅ relation имеет ${item.rings.size} колец")
                    BackgroundGeometry.Polygon(
                        item.rings.map { ring ->
                            ring.map { LatLng(lat = it[1], lon = it[0]) }
                        }
                    )
                }
            }
            else -> null
        }
        
        if (geometry == null) {
            android.util.Log.w("MapDataConverter", "   ⚠️ Не удалось конвертировать геометрию для type=${item.type}")
            return null
        }
        
        android.util.Log.d("MapDataConverter", "   ✅ Создан BackgroundElement")
        
        return BackgroundElement(
            id = item.id,
            geometry = geometry,
            layer = layer,
            color = color
        )
    }
}

// ════════════════════════════════════════════════════════════
// 🎨 ФОНОВЫЕ ЭЛЕМЕНТЫ КАРТЫ
// ════════════════════════════════════════════════════════════

/**
 * Фоновый элемент карты (вода, зелень, дороги, здания)
 */
data class BackgroundElement(
    val id: Long,
    val geometry: BackgroundGeometry,
    val layer: BackgroundLayer,
    val color: Int // ARGB color
)

/**
 * Геометрия фонового элемента
 */
sealed class BackgroundGeometry {
    /**
     * Линия (для дорог, рек)
     */
    data class LineString(val points: List<LatLng>) : BackgroundGeometry()
    
    /**
     * Полигон (для парков, озер, зданий)
     */
    data class Polygon(val rings: List<List<LatLng>>) : BackgroundGeometry()
}

/**
 * Слой отрисовки (порядок снизу вверх)
 */
enum class BackgroundLayer(val zIndex: Int) {
    WATER(0),      // Самый нижний
    GREENERY(1),   // Парки, леса
    ROADS(2),      // Дороги, мосты
    BUILDINGS(3);  // Здания (под POI)
    
    companion object {
        fun comparator() = compareBy<BackgroundLayer> { it.zIndex }
    }
}

/**
 * 🎨 Игровая десатурированная палитра цветов для карты
 * Мягкие пастельные тона, как в стилизованных играх
 */
object MapColors {
    // Еще более мягкие, десатурированные цвета для игрового вида
    const val WATER = 0xFFD8E8F0.toInt()        // Очень бледный голубовато-серый 💧
    const val PARK = 0xFFE3EBE0.toInt()         // Бледный зеленовато-серый 🌳
    const val FOREST = 0xFFD5E0D0.toInt()       // Чуть насыщеннее зеленовато-серый 🌲
    const val MEADOW = 0xFFEBEDE0.toInt()       // Очень бледный желто-зеленый 🌾
    const val ROAD = 0xFFF0F0F0.toInt()         // Почти белый 🛣️
    const val BUILDING = 0xFFEAEAEA.toInt()     // Светло-серый 🏢
    const val BRIDGE = 0xFFE0E5E8.toInt()       // Серовато-голубой 🌉
}