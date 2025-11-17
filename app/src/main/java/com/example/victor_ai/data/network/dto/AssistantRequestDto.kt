package com.example.victor_ai.data.network.dto

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

@JsonClass(generateAdapter = true)
data class UpdateHistoryRequest(
    val messages: List<ChatMessage>
)

@JsonClass(generateAdapter = true)
data class MemoryResponse(
    val id: String,
    val text: String,
    val metadata: Map<String, Any>
)

@JsonClass(generateAdapter = true)
data class UpdateMemoryRequest(
    val text: String,
    val metadata: Map<String, Any>? = null
)

@JsonClass(generateAdapter = true)
data class DeleteRequest(
    val record_ids: List<String>
)

@JsonClass(generateAdapter = true)
data class ChatHistoryResponse(
    val messages: List<ChatMessage>,
    @Json(name = "has_more") val hasMore: Boolean,
    @Json(name = "oldest_id") val oldestId: Int?,
    @Json(name = "newest_id") val newestId: Int?
)

@JsonClass(generateAdapter = true)
data class SearchResult(
    val messages: List<ChatMessage>,
    @Json(name = "matched_message_id") val matchedMessageId: Int?,
    @Json(name = "total_matches") val totalMatches: Int,
    @Json(name = "current_match_index") val currentMatchIndex: Int,
    @Json(name = "has_next") val hasNext: Boolean,
    @Json(name = "has_prev") val hasPrev: Boolean
)

