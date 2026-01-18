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
data class ImageContent(
    val type: String = "base64",
    @Json(name = "media_type")
    val mediaType: String = "image/png",
    val data: String // base64 encoded PNG
)

@JsonClass(generateAdapter = true)
data class AssistantRequest(
    @Json(name = "session_id")
    val sessionId: String,
    val text: String,
    val geo: GeoLocation? = null,
    val images: List<ImageContent>? = null,

    @Json(name = "system_event")
    val systemEvent: String? = null
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



