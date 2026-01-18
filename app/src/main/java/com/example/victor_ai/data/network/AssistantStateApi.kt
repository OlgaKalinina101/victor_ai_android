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
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

// ========================
// DTOs
// ========================

@JsonClass(generateAdapter = true)
data class AssistantState(
    val state: String
)

@JsonClass(generateAdapter = true)
data class AssistantMind(
    val mind: String,
    val type: String // "anchor" или "focus"
)

// ========================
// API Interface
// ========================

/**
 * API для работы с состоянием ассистента
 */
interface AssistantStateApi {
    /**
     * Получить состояние ассистента
     */
    @GET("assistant/assistant-state")
    suspend fun getAssistantState(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): List<AssistantState>

    /**
     * Получить "мысли" ассистента (anchor или focus)
     */
    @GET("assistant/assistant-mind")
    suspend fun getAssistantMind(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId()
    ): List<AssistantMind>
}

