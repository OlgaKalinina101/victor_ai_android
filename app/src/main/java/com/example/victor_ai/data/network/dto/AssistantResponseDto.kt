package com.example.victor_ai.data.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssistantResponse(
    val answer: String,
    val status: String
)

data class UpdateHistoryResponse(
    val success: Boolean,
    val message: String
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
