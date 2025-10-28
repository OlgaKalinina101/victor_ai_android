package com.example.victor_ai.data.models

import com.example.victor_ai.domain.model.ChatMessage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoLocation(
    val lat: Double,
    val lon: Double
)

@JsonClass(generateAdapter = true)
data class AssistantRequest(
    @Json(name = "session_id")
    val sessionId: String,
    val text: String,
    val geo: GeoLocation? = null
)

data class UpdateHistoryRequest(
    val messages: List<ChatMessage>
)

data class MemoryResponse(
    val id: String,
    val text: String,
    val metadata: Map<String, Any>
)

data class UpdateMemoryRequest(
    val text: String,
    val metadata: Map<String, Any>? = null
)

data class DeleteRequest(
    val record_ids: List<String>
)

