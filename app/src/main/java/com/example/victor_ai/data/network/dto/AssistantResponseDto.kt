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

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssistantResponse(
    val answer: String,
    val status: String
)

data class ReminderResponse(
    val status: String
)

data class DeleteResponse(
    val message: String
)

data class UpdateMemoryResponse(
    val message: String
)

data class ChatMetaResponse(
    val account_id: String,
    val model: String,
    val trust_level: Int,
    val raw_trust_score: Int?,
    val gender: String,
    val relationship_level: String?,
    val is_creator: Boolean,
    val trust_established: Boolean,
    val trust_test_completed: Boolean,
    val trust_test_timestamp: String?,
    val last_updated: String?
)

data class ChatMetaUpdateRequest(
    val model: String? = null,
    val trust_level: Int? = null,
    val raw_trust_score: Int? = null,
    val gender: String? = null,
    val relationship_level: String? = null,
    val is_creator: Boolean? = null,
    val trust_established: Boolean? = null,
    val trust_test_completed: Boolean? = null,
    val trust_test_timestamp: String? = null,
    val last_updated: String? = null
)