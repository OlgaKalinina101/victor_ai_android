package com.example.victor_ai.ui.places

import android.util.Log
import com.google.gson.Gson
import com.unity3d.player.UnityPlayer

/**
 * 🌉 Мост между Android (Kotlin) и Unity (C#)
 *
 * Этот класс отвечает за двустороннюю коммуникацию:
 * - Отправка данных из Android в Unity
 * - Получение событий из Unity в Android
 *
 * Unity вызывает методы этого класса через:
 * AndroidJavaClass("com.yourapp.map.unity.UnityBridge")
 */
object UnityBridge {

    private const val TAG = "UnityBridge"
    private const val UNITY_GAME_OBJECT = "MapUIManager" // Имя GameObject в Unity с MapUIManager
    private val gson = Gson()

    // ════════════════════════════════════════════════════════════
    // 📤 CALLBACK'И ДЛЯ ANDROID ПРИЛОЖЕНИЯ
    // ════════════════════════════════════════════════════════════

    /**
     * Вызывается когда пользователь кликает на POI в Unity
     * Используй это в Activity/Fragment:
     *
     * UnityBridge.onPOIClicked = { poiId, name, type ->
     *     // Открыть детальный экран
     *     openPOIDetails(poiId)
     * }
     */
    var onPOIClicked: ((poiId: String, name: String, type: String) -> Unit)? = null

    /**
     * Вызывается когда пользователь отмечает место как посещенное
     */
    var onPlaceVisited: ((poiId: String, impression: String, timestamp: Long) -> Unit)? = null

    /**
     * Вызывается когда пользователь обновляет впечатление
     */
    var onImpressionUpdated: ((poiId: String, impression: String) -> Unit)? = null

    /**
     * Вызывается когда Unity карта полностью загружена и готова
     */
    var onMapReady: (() -> Unit)? = null

    // ════════════════════════════════════════════════════════════
    // 📥 ANDROID → UNITY (отправка данных)
    // ════════════════════════════════════════════════════════════

    /**
     * Отправляет данные карты в Unity
     *
     * @param mapData Данные карты с POI, границами и т.д.
     *
     * Вызови это после загрузки OSM данных:
     * ```
     * val mapData = loadOsmData(latitude, longitude)
     * UnityBridge.sendMapData(mapData)
     * ```
     */
    fun sendMapData(mapData: MapData) {
        try {
            val json = gson.toJson(mapData)
            Log.d(TAG, "Sending map data to Unity: ${json.take(200)}...")

            sendMessageToUnity(
                gameObject = UNITY_GAME_OBJECT,
                method = "ReceiveMapDataFromAndroid",
                message = json
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error sending map data to Unity", e)
        }
    }

    /**
     * Обновляет текущую геолокацию пользователя на карте
     */
    fun updateUserLocation(location: LatLng) {
        try {
            val json = gson.toJson(location)
            sendMessageToUnity(
                gameObject = UNITY_GAME_OBJECT,
                method = "UpdateUserLocation",
                message = json
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user location", e)
        }
    }

    /**
     * Центрирует карту на определенной точке
     */
    fun centerOnPoint(location: LatLng) {
        try {
            val json = gson.toJson(location)
            sendMessageToUnity(
                gameObject = UNITY_GAME_OBJECT,
                method = "CenterOnPoint",
                message = json
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error centering on point", e)
        }
    }

    /**
     * Обновляет список посещенных мест
     * Используй это когда пользователь отмечает место из другой части приложения
     */
    fun updateVisitedPlaces(visitedIds: Set<String>) {
        try {
            val json = gson.toJson(visitedIds)
            sendMessageToUnity(
                gameObject = UNITY_GAME_OBJECT,
                method = "UpdateVisitedPlaces",
                message = json
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error updating visited places", e)
        }
    }

    /**
     * Фильтрует отображаемые POI по типу
     * @param types Список типов для отображения (пустой = показать все)
     */
    fun filterPOIsByType(types: List<POIType>) {
        try {
            val typeNames = types.map { it.name }
            val json = gson.toJson(typeNames)
            sendMessageToUnity(
                gameObject = UNITY_GAME_OBJECT,
                method = "FilterPOIsByType",
                message = json
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error filtering POIs", e)
        }
    }

    // ════════════════════════════════════════════════════════════
    // 📤 UNITY → ANDROID (получение событий)
    // ════════════════════════════════════════════════════════════

    /**
     * Вызывается Unity когда пользователь кликает на POI
     * НЕ вызывай это вручную! Unity вызывает через AndroidJavaClass
     *
     * @param jsonData JSON с POIClickedEvent
     */
    @JvmStatic
    fun OnPOIClickedFromUnity(jsonData: String) {
        try {
            Log.d(TAG, "OnPOIClickedFromUnity: $jsonData")
            val event = gson.fromJson(jsonData, POIClickedEvent::class.java)
            onPOIClicked?.invoke(event.poiId, event.poiName, event.poiType)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing POI clicked event", e)
        }
    }

    /**
     * Вызывается Unity когда место отмечено как посещенное
     */
    @JvmStatic
    fun OnPlaceVisitedFromUnity(jsonData: String) {
        try {
            Log.d(TAG, "OnPlaceVisitedFromUnity: $jsonData")
            val event = gson.fromJson(jsonData, PlaceVisitedEvent::class.java)
            onPlaceVisited?.invoke(event.poiId, event.impression, event.timestamp)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing place visited event", e)
        }
    }

    /**
     * Вызывается Unity когда пользователь обновляет впечатление
     */
    @JvmStatic
    fun OnImpressionUpdatedFromUnity(jsonData: String) {
        try {
            Log.d(TAG, "OnImpressionUpdatedFromUnity: $jsonData")
            val event = gson.fromJson(jsonData, ImpressionUpdatedEvent::class.java)
            onImpressionUpdated?.invoke(event.poiId, event.impression)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing impression updated event", e)
        }
    }

    /**
     * Вызывается Unity когда карта готова
     */
    @JvmStatic
    fun OnMapReadyFromUnity(jsonData: String) {
        try {
            Log.d(TAG, "OnMapReadyFromUnity")
            onMapReady?.invoke()
        } catch (e: Exception) {
            Log.e(TAG, "Error handling map ready event", e)
        }
    }

    // ════════════════════════════════════════════════════════════
    // 🔧 ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ════════════════════════════════════════════════════════════

    /**
     * Низкоуровневая отправка сообщения в Unity
     * Использует UnityPlayer.UnitySendMessage()
     */
    private fun sendMessageToUnity(
        gameObject: String,
        method: String,
        message: String
    ) {
        try {
            UnityPlayer.UnitySendMessage(gameObject, method, message)
            Log.d(TAG, "Sent to Unity: $gameObject.$method")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send message to Unity: $gameObject.$method", e)
        }
    }

    /**
     * Очистка callback'ов (вызови в onDestroy Activity)
     */
    fun cleanup() {
        onPOIClicked = null
        onPlaceVisited = null
        onImpressionUpdated = null
        onMapReady = null
    }
}