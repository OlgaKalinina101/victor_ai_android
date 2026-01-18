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

package com.example.victor_ai.domain.model

data class CareBankEntry(
    val emoji: String,
    val accountId: String,
    val value: String,   // то, что мы ввели руками
    val timestamp: Long = System.currentTimeMillis(),
    // Новые поля для автоматизации
    val searchUrl: String? = null,
    val searchField: String? = null,
    val addToCart1Coords: String? = null, // формат: "x,y"
    val addToCart2Coords: String? = null,
    val addToCart3Coords: String? = null,
    val addToCart4Coords: String? = null,
    val addToCart5Coords: String? = null,
    val openCartCoords: String? = null,
    val placeOrderCoords: String? = null
)