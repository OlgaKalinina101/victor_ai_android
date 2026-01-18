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

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PermissionsUiState(
    val permissions: List<PermissionItem> = emptyList(),
    val allGranted: Boolean = false
)

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(PermissionsUiState())
    val uiState: StateFlow<PermissionsUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "PermissionsViewModel"
    }

    init {
        initializePermissions()
    }

    private fun initializePermissions() {
        val permissions = listOf(
            PermissionItem(
                type = PermissionType.MICROPHONE,
                icon = "🎤",
                title = "Микрофон",
                description = "чтобы я мог слышать твой голос и отвечать на него"
            ),
            PermissionItem(
                type = PermissionType.LOCATION,
                icon = "📍",
                title = "Геолокация",
                description = "чтобы я мог смотреть погоду и гулять с тобой по ресторанам"
            ),
            PermissionItem(
                type = PermissionType.NOTIFICATIONS,
                icon = "🔔",
                title = "Уведомления",
                description = "чтобы напоминать о важных делах и событиях"
            ),
            PermissionItem(
                type = PermissionType.FULL_SCREEN_INTENT,
                icon = "📺",
                title = "Уведомления на весь экран",
                description = "чтобы будильник мог открываться поверх экрана блокировки (Android 14+)"
            ),
            PermissionItem(
                type = PermissionType.EXACT_ALARM,
                icon = "⏰",
                title = "Будильник",
                description = "чтобы мог будить тебя по утрам в точное время"
            ),
            PermissionItem(
                type = PermissionType.BATTERY_OPTIMIZATION,
                icon = "🔋",
                title = "Работа в фоне",
                description = "чтобы будильники и напоминания работали, даже когда экран выключен"
            ),
            PermissionItem(
                type = PermissionType.PHOTOS,
                icon = "📸",
                title = "Фотографии",
                description = "чтобы ты мог отправлять мне фотографии в чате"
            )
        )

        _uiState.update { it.copy(permissions = permissions) }
    }

    fun checkPermissions() {
        viewModelScope.launch {
            val updatedPermissions = _uiState.value.permissions.map { permission ->
                permission.copy(isGranted = isPermissionGranted(permission.type))
            }

            val allGranted = updatedPermissions.all { it.isGranted }

            _uiState.update {
                it.copy(
                    permissions = updatedPermissions,
                    allGranted = allGranted
                )
            }

            Log.d(TAG, "Permissions checked: ${updatedPermissions.count { it.isGranted }}/${updatedPermissions.size} granted")
        }
    }

    private fun isPermissionGranted(type: PermissionType): Boolean {
        return when (type) {
            PermissionType.MICROPHONE -> {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            }
            PermissionType.LOCATION -> {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            }
            PermissionType.NOTIFICATIONS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                } else {
                    // На Android 12 и ниже уведомления разрешены по умолчанию
                    true
                }
            }
            PermissionType.FULL_SCREEN_INTENT -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34
                    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    nm.canUseFullScreenIntent()
                } else {
                    true
                }
            }
            PermissionType.EXACT_ALARM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.canScheduleExactAlarms()
                } else {
                    true
                }
            }
            PermissionType.BATTERY_OPTIMIZATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                    powerManager.isIgnoringBatteryOptimizations(context.packageName)
                } else {
                    true
                }
            }
            PermissionType.PHOTOS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                } else {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                }
            }
        }
    }

    fun requestPermission(type: PermissionType) {
        Log.d(TAG, "Request permission: $type")
        // Здесь будет запрос через MainActivity
        // Пока просто логируем
    }

    fun requestAllPermissions() {
        Log.d(TAG, "Request all permissions")
        _uiState.value.permissions.forEach { permission ->
            if (!permission.isGranted) {
                requestPermission(permission.type)
            }
        }
    }

    /**
     * Получить Intent для открытия настроек разрешения
     */
    fun getPermissionSettingsIntent(type: PermissionType): Intent? {
        return when (type) {
            PermissionType.FULL_SCREEN_INTENT -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    // Android 14+: специальный экран для Full Screen Intent
                    Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                } else null
            }
            PermissionType.EXACT_ALARM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                } else null
            }
            PermissionType.BATTERY_OPTIMIZATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                } else null
            }
            else -> null
        }
    }

    /**
     * Получить список разрешений для ActivityResultContract
     */
    fun getPermissionsToRequest(): Array<String> {
        val permissions = mutableListOf<String>()

        _uiState.value.permissions.forEach { permission ->
            if (!permission.isGranted) {
                when (permission.type) {
                    PermissionType.MICROPHONE -> {
                        permissions.add(Manifest.permission.RECORD_AUDIO)
                    }
                    PermissionType.LOCATION -> {
                        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                    PermissionType.NOTIFICATIONS -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                    PermissionType.PHOTOS -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                    else -> {
                        // EXACT_ALARM, OVERLAY, BATTERY_OPTIMIZATION требуют Intent
                    }
                }
            }
        }

        return permissions.toTypedArray()
    }
}

