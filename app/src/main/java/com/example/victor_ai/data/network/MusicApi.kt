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
import com.squareup.moshi.Json
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// ========================
// DTOs
// ========================

/**
 * Запрос на обновление описания трека
 * Соответствует бэкенду: только track_id и описания, без account_id
 */
data class TrackDescriptionUpdate(
    val track_id: String,
    val energy_description: String?,
    val temperature_description: String?
)

data class TrackStats(
    val period: String,
    val from: String,
    val to: String,
    val total_plays: Int,
    val top_tracks: List<TopTrack>,
    val top_energy: String?,
    val top_temperature: String?,
    val average_duration: Float
)

data class Track(
    val id: Int,
    val filename: String,
    @Json(name = "file_path") val filePath: String,
    val title: String,
    val artist: String,
    val album: String?,
    val year: Int?,
    val genre: String?,
    val duration: Float,
    @Json(name = "track_number") val trackNumber: Int?,
    val bitrate: Int,
    @Json(name = "file_size") val fileSize: Long,
    @Json(name = "energy_description") val energyDescription: String?,
    @Json(name = "temperature_description") val temperatureDescription: String?
)

/**
 * История моментов "выбора трека" (playlist moments)
 * Бэкенд: GET /tracks/playlist_moments
 */
data class PlaylistMomentOut(
    val id: Int,
    @Json(name = "account_id") val accountId: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "track_id") val trackId: Int?,
    @Json(name = "stage1_text") val stage1Text: String?,
    @Json(name = "stage2_text") val stage2Text: String?,
    @Json(name = "stage3_text") val stage3Text: String?,
    val track: Track?
)

data class TopTrack(
    val title: String,
    val artist: String,
    val plays: Int
)

data class WaveTrack(
    val id: Int,
    val title: String,
    val artist: String?,
    val duration: Float,
    @Json(name = "energy_description") val energyDescription: String?,
    @Json(name = "temperature_description") val temperatureDescription: String?,
    @Json(name = "stream_url") val streamUrl: String
)

data class WaveResponse(
    val tracks: List<WaveTrack>,
    val energy: String?,
    val temperature: String?
)

// ========================
// API Interface
// ========================

interface MusicApi {

    /**
     * Возвращает все треки пользователя с их персонализированными описаниями.
     *
     * Получает полный список музыкальных треков, доступных пользователю,
     * вместе с описаниями (энергия, температура), которые пользователь
     * ранее назначил каждому треку.
     *
     * @param accountId Идентификатор пользователя (обязательный параметр).
     * @return Список объектов [Track], содержащих метаданные трека
     *         и пользовательские описания.
     * @throws BadRequestException Если [accountId] не указан или пуст.
     * @throws NotFoundException Если для пользователя не найдено ни одного трека.
     * @throws ServerErrorException При внутренней ошибке сервера.
     */
    @GET("tracks")
    suspend fun getTracks(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<Track>

    /**
     * Возвращает агрегированную статистику по прослушиваниям пользователя за указанный период.
     *
     * @param accountId Идентификатор пользователя (обязательный параметр).
     * @param period Период для анализа статистики.
     *               Допустимые значения: "week" (последние 7 дней) или "month" (последние 30 дней).
     *               По умолчанию "week".
     * @return Объект [TrackStats] с агрегированными данными.
     * @throws BadRequestException Если указан неверный период или отсутствует [accountId].
     * @throws ServerErrorException При внутренней ошибке сервера.
     */
    @GET("tracks/stats")
    suspend fun getTrackStats(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("period") period: String = "week"
    ): TrackStats

    /**
     * Возвращает историю моментов выбора трека (PlaylistMoment) для пользователя.
     *
     * Бэкенд: GET /tracks/playlist_moments
     */
    @GET("tracks/playlist_moments")
    suspend fun getPlaylistMoments(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("limit") limit: Int = 20
    ): List<PlaylistMomentOut>

    /**
     * Обновляет или создаёт персонализированное описание трека для пользователя.
     *
     * Позволяет пользователю аннотировать треки характеристиками "энергия" и "температура".
     *
     * @param accountId Идентификатор пользователя (по умолчанию текущий пользователь).
     * @param body Объект [TrackDescriptionUpdate] с данными для обновления:
     *             - trackId: Идентификатор трека
     *             - energyDescription: Уровень энергии ("low", "medium", "high" или null)
     *             - temperatureDescription: Температурная характеристика ("cold", "neutral", "warm" или null)
     * @return Объект [MessageResponse] с сообщением об успешном выполнении.
     * @throws BadRequestException Если данные запроса невалидны.
     * @throws NotFoundException Если указанный трек не существует.
     * @throws ServerErrorException При ошибке сохранения.
     */
    @POST("tracks/update_track_description")
    suspend fun updateTrackDescription(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Body update: TrackDescriptionUpdate
    ): Map<String, String>

    /**
     * Запускает кнопку "выбери сам".
     *
     * @param accountId Идентификатор пользователя (обязательный параметр).
     * @param extraContext Дополнительный текстовый контекст для уточнения подбора.
     * @return Объект [PlaylistChainResponse] с подобранным треком и объяснением выбора.
     * @throws BadRequestException Если [accountId] не указан.
     * @throws NotFoundException Если не удалось подобрать подходящий трек.
     * @throws ServerErrorException При ошибке алгоритма подбора.
     */
    @POST("tracks/choose_for_me")
    suspend fun runPlaylistChain(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("extra_context") extraContext: String? = null
    ): Map<String, Any>

    // 🔥 NOTE: runPlaylistChainStreaming moved to MusicApiImpl
    // Retrofit не поддерживает streaming SSE из коробки, поэтому
    // этот метод реализован вручную в MusicApiImpl через OkHttpClient

    /**
     * Генерирует персонализированную "волну" треков на основе:
     * - Текущее желаемое состояние (энергия/температура)
     *
     * @param accountId Идентификатор пользователя (обязательный параметр).
     * @param energy Желаемый уровень энергии для подбора треков.
     * @param temperature Желаемая температурная характеристика.
     * @param limit Максимальное количество треков в возвращаемой "волне".
     *              По умолчанию 20.
     * @return Объект [PlaylistWaveResponse] с подобранной "волной" треков.
     * @throws BadRequestException Если [accountId] не указан или параметры невалидны.
     * @throws ServerErrorException При ошибке подбора треков.
     */
    @POST("tracks/run_playlist_wave")
    suspend fun runPlaylistWave(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("energy") energy: String? = null,
        @Query("temperature") temperature: String? = null
    ): WaveResponse
}