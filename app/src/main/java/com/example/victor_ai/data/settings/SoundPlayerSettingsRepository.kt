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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

// ⚠️ top-level extension, НЕ внутри object / class
private const val SOUND_PLAYER_DATASTORE_NAME = "sound_player_prefs"
private val Context.soundPlayerDataStore: DataStore<Preferences> by preferencesDataStore(
    name = SOUND_PLAYER_DATASTORE_NAME
)

@Singleton
class SoundPlayerSettingsRepository @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val dataStore = context.soundPlayerDataStore

    private object Keys {
        val VIBRATION_SCALE = floatPreferencesKey("sound_player_vibration_scale")
        val SOUND_SCALE = floatPreferencesKey("sound_player_sound_scale")
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val settingsFlow: Flow<SoundPlayerSettings> = dataStore.data.map { prefs ->
        SoundPlayerSettings(
            vibrationScale = prefs[Keys.VIBRATION_SCALE] ?: 1f,
            soundScale = prefs[Keys.SOUND_SCALE] ?: 1f,
        )
    }

    val settingsState = settingsFlow.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = SoundPlayerSettings(),
    )

    suspend fun setVibrationScale(value: Float) {
        dataStore.edit { prefs ->
            prefs[Keys.VIBRATION_SCALE] = value
        }
    }

    suspend fun setSoundScale(value: Float) {
        dataStore.edit { prefs ->
            prefs[Keys.SOUND_SCALE] = value
        }
    }
}

