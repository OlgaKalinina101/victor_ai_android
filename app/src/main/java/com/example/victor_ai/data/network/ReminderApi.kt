package com.example.victor_ai.data.network

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

data class ReminderDto(
    val id: String,
    val text: String,

    @Json(name = "datetime")
    val date: String?,             // ISO-строка, парсим в LocalDate

    @Json(name = "repeat_weekly")
    val repeatWeekly: Boolean,

    @Json(name = "day_of_week")
    val dayOfWeek: String? = null
)

interface ReminderApi {
    @GET("assistant/reminders")
    suspend fun getReminders(
        @Query("account_id") accountId: String
    ): Map<String, List<ReminderDto>>
}
