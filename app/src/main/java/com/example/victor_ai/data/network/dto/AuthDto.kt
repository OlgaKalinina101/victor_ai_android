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

data class WebDemoResolveRequest(
    val demo_key: String,
    val account_id: String? = null,
    val gender: String? = null
)

data class WebDemoResolveResponse(
    val status: String,
    val message: String? = null,
    val required_fields: List<String>? = null,
    val gender_options: List<String>? = null,
    val access_token: String? = null,
    val account_id: String? = null,
    // NOTE: структура может быть сложной; оставляем как Map (Moshi reflection справится)
    val initial_state: Map<String, Any?>? = null
)

data class WebDemoRegisterRequest(
    val demo_key: String,
    val account_id: String,
    val gender: String  // MALE или FEMALE
)

data class WebDemoRegisterResponse(
    val access_token: String,
    val token_type: String = "bearer",
    val account_id: String,
    val initial_state: Map<String, Any?>? = null
)


