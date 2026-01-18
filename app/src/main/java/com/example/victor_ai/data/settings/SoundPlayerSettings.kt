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

package com.example.victor_ai.data.settings

data class SoundPlayerSettings(
    /**
     * 0.0 = выключить, 1.0 = оригинальные значения, >1.0 = усилить
     */
    val vibrationScale: Float = 1f,
    /**
     * 0.0 = выключить, 1.0 = оригинальные значения, >1.0 = усилить
     */
    val soundScale: Float = 1f,
)

