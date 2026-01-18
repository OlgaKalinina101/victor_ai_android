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

package com.example.victor_ai.ui.permissions

/**
 * Модель разрешения для отображения в UI
 */
data class PermissionItem(
    val type: PermissionType,
    val icon: String,
    val title: String,
    val description: String,
    val isGranted: Boolean = false
)

/**
 * Типы разрешений
 */
enum class PermissionType {
    MICROPHONE,
    LOCATION,
    NOTIFICATIONS,
    FULL_SCREEN_INTENT,
    EXACT_ALARM,
    BATTERY_OPTIMIZATION,
    PHOTOS
}

