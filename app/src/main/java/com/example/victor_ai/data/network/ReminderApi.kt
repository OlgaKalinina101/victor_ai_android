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
import com.example.victor_ai.data.network.dto.ReminderResponse
import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// ========================
// DTOs
// ========================

data class ReminderDto(
    val id: String,
    val text: String,

    @Json(name = "datetime")
    val date: String?,             // ISO-строка, парсим в LocalDate

    @Json(name = "repeat_weekly")
    val repeatWeekly: Boolean,

    @Json(name = "dayOfWeek")
    val dayOfWeek: String? = null
)

/**
 * Запрос на пометку напоминания как выполненного
 * Соответствует бэкенду: class ReminderRequest(BaseModel)
 */
data class ReminderRequest(
    val reminder_id: String
)

/**
 * Запрос на откладывание напоминания
 * Соответствует бэкенду: class ReminderDelayRequest(BaseModel)
 */
data class ReminderDelayRequest(
    val reminder_id: String,
    val value: Int = 1,
    val unit: String = "hour"  // "minute", "hour", "day"
)

/**
 * Запрос на включение/выключение еженедельного повторения
 * Соответствует бэкенду: class ReminderRepeatWeeklyRequest(BaseModel)
 */
data class ReminderRepeatWeeklyRequest(
    val reminder_id: String,
    val repeat_weekly: Boolean = false
)

// ========================
// API Interface
// ========================

interface ReminderApi {

    /**
     * Получает все напоминания для указанного пользователя.
     *
     * Возвращает сгруппированный список всех активных напоминаний
     * (разовых и периодических), принадлежащих пользователю с заданным [accountId].
     *
     * Ключи map:
     * - ISO-дата (например, "2025-12-04") для разовых напоминаний
     * - Дни недели в верхнем регистре (например, "FRIDAY") для повторяющихся
     *
     * @param accountId Идентификатор пользователя (по умолчанию текущий пользователь).
     * @return Map, где ключи — категории напоминаний, значения — списки [ReminderDto].
     *
     * Возможные HTTP-коды от сервера:
     * - 200 — успех
     * - 400 — если [accountId] пустой или невалидный
     */
    @GET("reminders")
    suspend fun getReminders(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): Map<String, List<ReminderDto>>

    /**
     * Откладывает выполнение напоминания.
     *
     * На сервер уходит [ReminderDelayRequest] c:
     * - [ReminderDelayRequest.reminderId] — ID напоминания
     * - [ReminderDelayRequest.unit] — "minute", "hour" или "day"
     * - [ReminderDelayRequest.value] — положительное число > 0
     *
     * Сервер сам пересчитает новое время с помощью [unit] и [value].
     *
     * @param accountId Идентификатор пользователя (по умолчанию текущий пользователь).
     * @param body Объект запроса с данными для откладывания напоминания.
     * @return [Response] с [ReminderResponse] и HTTP-кодом:
     * - 200 — напоминание успешно отложено
     * - 400 — невалидный unit/value
     * - 404 — напоминание не найдено или недоступно
     */
    @POST("reminders/delay")
    suspend fun delayReminder(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Body body: ReminderDelayRequest
    ): Response<ReminderResponse>

    /**
     * Помечает напоминание как выполненное.
     *
     * Для разовых напоминаний — архивирует их.
     * Для периодических — обновляет время следующего выполнения.
     *
     * @param accountId Идентификатор пользователя (по умолчанию текущий пользователь).
     * @param body Объект запроса с обязательным полем [ReminderRequest.reminderId].
     * @return [Response] с [ReminderResponse] и HTTP-кодом:
     * - 200 — успех
     * - 400 — если `reminderId` не передан
     * - 404 — если напоминание не найдено или недоступно
     */
    @POST("reminders/done")
    suspend fun markReminderAsDone(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Body body: ReminderRequest
    ): Response<ReminderResponse>

    /**
     * Выключает еженедельное повторение для напоминания.
     *
     * На сервер уходит [ReminderRepeatWeeklyRequest] с:
     * - [ReminderRepeatWeeklyRequest.reminderId] — ID напоминания
     * - [ReminderRepeatWeeklyRequest.repeatWeekly] — флаг повторения
     *
     * @param accountId Идентификатор пользователя (по умолчанию текущий пользователь).
     * @param body Объект запроса с данными для настройки повторения.
     * @return [Response] с [ReminderResponse] и HTTP-кодом:
     * - 200 — успех
     * - 404 — напоминание не найдено или недоступно
     */
    @POST("reminders/repeat-weekly")
    suspend fun setReminderRepeatWeekly(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Body body: ReminderRepeatWeeklyRequest
    ): Response<ReminderResponse>
}
