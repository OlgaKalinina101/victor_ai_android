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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// ========================
// DTOs
// ========================

data class AlarmDto(
    val account_id: String?,
    val alarms: List<AlarmItemDto>
)

data class AlarmItemDto(
    val time: String?,              // "08:00" или null, если выключен
    val repeatMode: String?,        // "Один раз" / "Будни" / "Выходные" / "Всегда" / null
    val enabled: Boolean = true     // потом пригодится, если захотим выключать
)

// Request для эндпоинта /alarms/select-track
data class SelectTrackRequest(
    val account_id: String,
    val track_id: Int?              // null означает "сбросить"
)

// Payload для эндпоинта /alarms/select-track-for-yourself (в body)
data class PayloadData(
    val extra_context: String? = null
)

// Response для обоих эндпоинтов select-track
data class SelectTrackResponse(
    val status: String,
    val selected_track_id: Int?,
    val message: String? = null     // только для select-track-for-yourself
)

// Response для GET /alarms/{account_id}
data class GetAlarmsResponse(
    val alarms: List<AlarmItemDto>,
    val selected_track_id: Int?
)

// ========================
// API Interface
// ========================

/**
 * API для работы с будильниками
 */
interface AlarmsApi {
    /**
     * Сохраняет или обновляет список будильников для пользователя.
     *
     * Принимает полный список будильников и использует операцию merge
     * для обновления существующей записи или создания новой.
     *
     * @param body Объект [AlarmUpdateDto] с данными будильников:
     *             - accountId: Идентификатор пользователя
     *             - alarms: Полный список объектов будильников
     * @return [SimpleResponse] с результатом операции.
     * @throws BadRequestException Если данные будильников невалидны.
     * @throws ServerErrorException При ошибке сохранения.
     *
     * @note Этот метод полностью заменяет существующий список будильников.
     */
    @POST("alarms")
    suspend fun updateAlarm(@Body alarm: AlarmDto): Response<Unit>

    /**
     * Получает полную конфигурацию будильников пользователя.
     *
     * Возвращает список настроенных будильников и выбранный трек для воспроизведения.
     *
     * @param accountId Идентификатор пользователя.
     * @return [AlarmsResponse] с конфигурацией будильников.
     * @throws ServerErrorException При ошибке чтения из базы данных.
     */
    @GET("alarms/{account_id}")
    suspend fun getAlarms(
        @Path("account_id") accountId: String
    ): Response<GetAlarmsResponse>

    /**
     * Устанавливает или сбрасывает выбранный музыкальный трек для всех будильников пользователя.
     *
     * Позволяет зафиксировать конкретный трек для воспроизведения
     * или включить режим автоматического выбора.
     *
     * @param accountId Идентификатор пользователя.
     * @param trackId ID музыкального трека для установки.
     *                Если null - включается режим автоматического выбора.
     * @return [SelectTrackResponse] с результатом операции.
     * @throws BadRequestException Если передан невалидный trackId.
     * @throws NotFoundException Если трек с указанным ID не существует.
     * @throws ServerErrorException При ошибке сохранения.
     */
    @POST("alarms/select_track")
    suspend fun selectTrack(
        @Body request: SelectTrackRequest
    ): Response<SelectTrackResponse>

    /**
     * Кнопка "Выбери сам"
     *
     * @param accountId Идентификатор пользователя.
     * @param body Данные для экстра контекста (опционально).
     * @return [SelectTrackResponse] с результатом операции.
     * @throws ServerErrorException При ошибке алгоритма подбора.
     *
     * @see runPlaylistChain
     */
    @POST("alarms/select_track_for_yourself")
    suspend fun selectTrackForYourself(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Body payload: PayloadData
    ): Response<SelectTrackResponse>
}

