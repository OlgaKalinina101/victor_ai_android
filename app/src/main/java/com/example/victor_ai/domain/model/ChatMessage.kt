package com.example.victor_ai.domain.model

import com.squareup.moshi.Json


data class ChatMessage(
    @Json(name = "text") val text: String,
    @Json(name = "is_user") val isUser: Boolean,
    @Json(name = "timestamp") val timestamp: Long
)