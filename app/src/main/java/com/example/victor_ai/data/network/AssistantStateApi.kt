package com.example.victor_ai.data.network

import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class AssistantState(
    val state: String
)

@JsonClass(generateAdapter = true)
data class AssistantMind(
    val mind: String,
    val type: String // "anchor" или "focus"
)

interface AssistantStateApi {
    @GET("assistant/assistant-state")
    suspend fun getAssistantState(
        @Query("account_id") accountId: String
    ): List<AssistantState>

    @GET("assistant/assistant-mind")
    suspend fun getAssistantMind(
        @Query("account_id") accountId: String
    ): List<AssistantMind>
}

