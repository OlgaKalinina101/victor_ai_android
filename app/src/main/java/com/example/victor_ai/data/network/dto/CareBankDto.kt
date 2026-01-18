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

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CareBankEntryDto(
    val id: Int?,
    @Json(name = "account_id")
    val accountId: String,
    val emoji: String,
    val value: String,
    @Json(name = "timestamp_ms")
    val timestampMs: Long,
    // Новые поля для автоматизации
    @Json(name = "search_url")
    val searchUrl: String? = null,
    @Json(name = "search_field")
    val searchField: String? = null,
    @Json(name = "add_to_cart_1_coords")
    val addToCart1Coords: String? = null, // формат: "x,y"
    @Json(name = "add_to_cart_2_coords")
    val addToCart2Coords: String? = null,
    @Json(name = "add_to_cart_3_coords")
    val addToCart3Coords: String? = null,
    @Json(name = "add_to_cart_4_coords")
    val addToCart4Coords: String? = null,
    @Json(name = "add_to_cart_5_coords")
    val addToCart5Coords: String? = null,
    @Json(name = "open_cart_coords")
    val openCartCoords: String? = null,
    @Json(name = "place_order_coords")
    val placeOrderCoords: String? = null
)

@JsonClass(generateAdapter = true)
data class CareBankEntryCreate(
    @Json(name = "account_id")
    val accountId: String,
    val emoji: String,
    val value: String,
    @Json(name = "timestamp_ms")
    val timestampMs: Long? = null,
    // Новые поля для автоматизации
    @Json(name = "search_url")
    val searchUrl: String? = null,
    @Json(name = "search_field")
    val searchField: String? = null,
    @Json(name = "add_to_cart_1_coords")
    val addToCart1Coords: String? = null,
    @Json(name = "add_to_cart_2_coords")
    val addToCart2Coords: String? = null,
    @Json(name = "add_to_cart_3_coords")
    val addToCart3Coords: String? = null,
    @Json(name = "add_to_cart_4_coords")
    val addToCart4Coords: String? = null,
    @Json(name = "add_to_cart_5_coords")
    val addToCart5Coords: String? = null,
    @Json(name = "open_cart_coords")
    val openCartCoords: String? = null,
    @Json(name = "place_order_coords")
    val placeOrderCoords: String? = null
)

@JsonClass(generateAdapter = true)
data class CareBankEntriesResponse(
    val entries: List<CareBankEntryDto>
)

@JsonClass(generateAdapter = true)
data class CareBestResponse(
    val reason: String,
    val bestChoice: Boolean
)

/**
 * Ответ от бэкенда при анализе скриншота
 * Поддерживает оба формата: camelCase (приоритет) и snake_case (fallback)
 */
@JsonClass(generateAdapter = true)
data class ScreenshotAnalysisResponse(
    val id: String,                    // "1", "2", "3", "4", "5"
    @Json(name = "selectedItem")  // camelCase (от бэкенда)
    val selectedItem: String,          // "Блинчики с творогом"
    @Json(name = "matchType")  // camelCase (от бэкенда)
    val matchType: String,             // "exact" | "similar" | "none"
    @Json(name = "userMessage")  // camelCase (от бэкенда)
    val userMessage: String            // Сообщение для Jarvis
)

// Настройки банка заботы
enum class TaxiClass(val value: String) {
    @Json(name = "economy")
    ECONOMY("economy"),

    @Json(name = "comfort")
    COMFORT("comfort"),

    @Json(name = "comfort_plus")
    COMFORT_PLUS("comfort_plus"),

    @Json(name = "business")
    BUSINESS("business"),

    @Json(name = "minivan")
    MINIVAN("minivan")
}

@JsonClass(generateAdapter = true)
data class CareBankSettingsUpdate(
    @Json(name = "account_id")
    val accountId: String,
    @Json(name = "auto_approved")
    val autoApproved: Boolean? = null,
    @Json(name = "presence_address")
    val presenceAddress: String? = null,
    @Json(name = "max_order_cost")
    val maxOrderCost: Int? = null,
    @Json(name = "preferred_taxi_class")
    val preferredTaxiClass: TaxiClass? = null
)

@JsonClass(generateAdapter = true)
data class CareBankSettingsRead(
    val id: Int,
    @Json(name = "account_id")
    val accountId: String,
    @Json(name = "auto_approved")
    val autoApproved: Boolean,
    @Json(name = "presence_address")
    val presenceAddress: String? = null,
    @Json(name = "max_order_cost")
    val maxOrderCost: Int? = null,
    @Json(name = "preferred_taxi_class")
    val preferredTaxiClass: TaxiClass? = null
)

