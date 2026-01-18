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

package com.example.victor_ai.ui.screens.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.settings.SoundPlayerSettings
import com.example.victor_ai.data.settings.SoundPlayerSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoundPlayerSettingsViewModel @Inject constructor(
    private val repository: SoundPlayerSettingsRepository,
) : ViewModel() {

    val settings: StateFlow<SoundPlayerSettings> = repository.settingsState

    fun setVibrationScale(value: Float) {
        viewModelScope.launch {
            repository.setVibrationScale(value)
        }
    }

    fun setSoundScale(value: Float) {
        viewModelScope.launch {
            repository.setSoundScale(value)
        }
    }
}

