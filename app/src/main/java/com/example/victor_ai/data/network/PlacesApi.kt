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

package com.example.victor_ai.data.network

import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.ui.map.models.PlacesResponse
import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// ========================
// DTOs
// ========================

// ---------- WALK SESSIONS ----------
data class StepPoint(
    val lat: Double,
    val lon: Double,
    val timestamp: String
)

data class POIVisit(
    val account_id: String,  // ← добавили
    val poi_id: String,
    val poi_name: String,
    val distance_from_start: Float,
    val found_at: String,
    val emotion_emoji: String,
    val emotion_label: String,
    val emotion_color: String
)

data class WalkSessionCreate(
    val account_id: String,
    val start_time: String,
    val end_time: String,
    val distance_m: Float,
    val steps: Int,
    val mode: String,
    val notes: String?,
    val poi_visits: List<POIVisit>,
    val step_points: List<StepPoint>
)

data class UnlockedAchievement(
    val name: String,
    val type: String,
    val description: String
)

data class WalkSessionResponse(
    val status: String,
    val session_id: Int,
    val unlocked_achievements: List<UnlockedAchievement>
)

// ---------- JOURNAL ----------
data class JournalEntry(
    val id: Int,
    val date: String,
    val text: String,
    val photo_path: String?,
    val poi_name: String?,
    val session_id: Int?
)

data class JournalEntryIn(
    val date: String,
    val text: String,
    val photo_path: String?,
    val poi_name: String?,
    val session_id: Int?,
    val account_id: String
)

// ---------- ACHIEVEMENTS ----------
data class Achievement(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val icon: String?,
    val unlocked_at: String?
)

// ---------- STATS ----------
data class StatsResponse(
    val today_distance: Float,
    val today_steps: Int,
    val weekly_chart: List<Float>,
    val streak: Int,
    val achievements: List<String>
)

// ---------- LOCATIONS ----------
/**
 * Краткая информация о локации (для списка)
 */
data class LocationListItem(
    val id: Int,
    val name: String,
    val description: String?,
    val is_active: Boolean,
    val difficulty: String?,
    val location_type: String?
)

/**
 * Ответ на удаление локации
 */
data class LocationDeleteResponse(
    val detail: String,
    val location_id: Int,
    val name: String
)

/**
 * Полная информация о локации (с bbox)
 */
data class LocationDetail(
    val id: Int,
    val account_id: String,
    val name: String,
    val description: String?,
    val bbox_south: Double,
    val bbox_west: Double,
    val bbox_north: Double,
    val bbox_east: Double,
    val is_active: Boolean,
    val difficulty: String?,
    val location_type: String?
)

// ---------- PLACE CAPTION ----------
data class PlaceCaptionRequest(
    @Json(name = "account_id")
    val accountId: String,
    @Json(name = "poi_osm_id")
    val poiOsmId: Long,
    @Json(name = "poi_osm_type")
    val poiOsmType: String, // "node" | "way" | "relation"
    val tags: Map<String, Any>? = null
)

data class PlaceCaptionResponse(
    val caption: String
)

// ========================
// API Interface
// ========================

/**
 * API для работы с местами, локациями и прогулками
 */
interface PlacesApi {

    // ========================
    // 📍 ЛОКАЦИИ
    // ========================

    /**
     * Получает OSM (OpenStreetMap) объекты для заданных географических координат.
     *
     * Интеллектуальный поиск игровой локации по точке на карте с возможностью
     * автоматического создания новой локации при необходимости.
     *
     * @param accountId Идентификатор пользователя.
     * @param latitude Географическая широта в градусах (-90 до 90).
     * @param longitude Географическая долгота в градусах (-180 до 180).
     * @param radiusKm Радиус поиска объектов в километрах. По умолчанию 2.0.
     * @param limit Максимальное количество возвращаемых объектов. По умолчанию 15000.
     * @param offset Смещение для пагинации. По умолчанию 0.
     * @return [PlacesResponse] с OSM объектами и информацией о локации.
     * @throws BadRequestException При достижении лимита локаций (MAX_GAME_LOCATIONS_REACHED).
     * @throws ServerErrorException При внутренней ошибке сервера.
     */
    @GET("places")
    suspend fun getPlaces(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius_km") radiusKm: Double = 2.0,
        @Query("limit") limit: Int = 15000,
        @Query("offset") offset: Int = 0
    ): PlacesResponse

    /**
     * Возвращает список всех активных игровых локаций пользователя.
     *
     * Получает краткую информацию о сохранённых локациях для отображения
     * в интерфейсе выбора игровых зон.
     *
     * @param accountId Идентификатор пользователя.
     * @return Список [GameLocationListItem] с базовой информацией о локациях.
     * @throws ServerErrorException При внутренней ошибке сервера.
     */
    @GET("places/locations")
    suspend fun getLocations(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): Response<List<LocationListItem>>

    /**
     * Генерирует одну короткую "живую" подпись к месту по OSM-тегам.
     */
    @POST("places/caption")
    suspend fun generatePlaceCaption(
        @Body body: PlaceCaptionRequest
    ): PlaceCaptionResponse

    /**
     * Получает полную детальную информацию о конкретной игровой локации.
     *
     * Возвращает все метаданные локации с проверкой прав доступа.
     *
     * @param locationId ID локации.
     * @param accountId Идентификатор пользователя для проверки прав доступа.
     * @return [GameLocationResponse] с детальной информацией о локации.
     * @throws ForbiddenException Если локация принадлежит другому пользователю.
     * @throws NotFoundException Если локация с указанным ID не существует.
     * @throws ServerErrorException При внутренней ошибке сервера.
     */
    @GET("places/locations/{location_id}")
    suspend fun getLocationDetail(
        @Path("location_id") locationId: Int,
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): Response<LocationDetail>

    /**
     * Получает OSM объекты для существующей сохранённой локации.
     *
     * Быстрая загрузка объектов локации без геопоиска и запросов к Overpass API.
     *
     * @param locationId ID существующей локации.
     * @param accountId Идентификатор пользователя для проверки прав доступа.
     * @param limit Максимальное количество возвращаемых объектов. По умолчанию 15000.
     * @param offset Смещение для пагинации. По умолчанию 0.
     * @return [PlacesResponse] с OSM объектами в том же формате, что и getPlaces().
     * @throws ForbiddenException Если локация принадлежит другому пользователю.
     * @throws NotFoundException Если локация не существует.
     * @throws ServerErrorException При внутренней ошибке сервера.
     */
    @GET("places/locations/{location_id}/places")
    suspend fun getLocationPlaces(
        @Path("location_id") locationId: Int,
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("limit") limit: Int = 15000,
        @Query("offset") offset: Int = 0
    ): PlacesResponse

    /**
     * Выполняет мягкое удаление (soft delete) игровой локации.
     *
     * Устанавливает флаг isActive = false, делая локацию недоступной.
     * Только владелец локации может её удалить.
     *
     * @param locationId ID локации для удаления.
     * @param accountId Идентификатор пользователя (должен быть владельцем).
     * @return [GameLocationDeleteResponse] с подтверждением удаления.
     * @throws BadRequestException Если локация уже удалена.
     * @throws ForbiddenException Если локация принадлежит другому пользователю.
     * @throws NotFoundException Если локация не существует.
     * @throws ServerErrorException При внутренней ошибке сервера.
     */
    @DELETE("places/locations/{location_id}")
    suspend fun deleteLocation(
        @Path("location_id") locationId: Int,
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): Response<LocationDeleteResponse>

    // ========================
    // 🏃 Прогулки
    // ========================

    @POST("api/walk_sessions/")
    suspend fun createWalkSession(@Body body: WalkSessionCreate): Response<WalkSessionResponse>

    // ========================
    // 📔 Дневник
    // ========================

    @GET("api/journal/")
    suspend fun getJournalEntries(@Query("account_id") accountId: String = UserProvider.getCurrentUserId()): Response<List<JournalEntry>>

    @POST("api/journal/")
    suspend fun createJournalEntry(@Body entry: JournalEntryIn): Response<Map<String, Any>>

    // ========================
    // 🏆 Достижения
    // ========================

    @GET("api/achievements/")
    suspend fun getAchievements(@Query("account_id") accountId: String = UserProvider.getCurrentUserId()): Response<List<Achievement>>

    // ========================
    // 📊 Статистика
    // ========================

    @GET("api/stats/")
    suspend fun getStats(@Query("account_id") accountId: String = UserProvider.getCurrentUserId()): Response<StatsResponse>
}

