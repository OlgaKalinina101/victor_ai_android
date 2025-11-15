package com.example.victor_ai.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ChatMessage(
    @Json(name = "text") val text: String,
    @Json(name = "is_user") val isUser: Boolean,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "id") val id: Int? = null  // ID из БД (null для SessionContext сообщений)
)