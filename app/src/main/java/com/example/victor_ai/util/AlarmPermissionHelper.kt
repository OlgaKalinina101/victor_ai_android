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

package com.example.victor_ai.util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher

/**
 * Утилита для проверки и запроса разрешений будильника
 */
object AlarmPermissionHelper {
    
    private const val TAG = "AlarmPermissions"
    
    /**
     * Проверяет все необходимые разрешения для будильника
     */
    fun checkAllPermissions(context: Context): PermissionsStatus {
        val hasFullScreenIntent = checkFullScreenIntentPermission(context)
        val isBatteryOptimized = isBatteryOptimizationEnabled(context)
        val hasOverlayPermission = checkOverlayPermission(context)
        
        Log.d(TAG, "📊 Статус разрешений:")
        Log.d(TAG, "  Full Screen Intent: $hasFullScreenIntent")
        Log.d(TAG, "  Battery Optimization: ${if (isBatteryOptimized) "❌ Включена (плохо)" else "✅ Отключена (хорошо)"}")
        Log.d(TAG, "  Overlay Permission: $hasOverlayPermission")
        
        return PermissionsStatus(
            hasFullScreenIntent = hasFullScreenIntent,
            isBatteryOptimized = isBatteryOptimized,
            hasOverlayPermission = hasOverlayPermission
        )
    }
    
    /**
     * Проверяет разрешение USE_FULL_SCREEN_INTENT (Android 14+)
     */
    fun checkFullScreenIntentPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val canUse = notificationManager.canUseFullScreenIntent()
            Log.d(TAG, "Android 14+: Full Screen Intent = $canUse")
            canUse
        } else {
            // На Android < 14 разрешение не требуется
            Log.d(TAG, "Android < 14: Full Screen Intent не требует runtime разрешения")
            true
        }
    }
    
    /**
     * Проверяет, включена ли оптимизация батареи для приложения
     * (Если включена - может блокировать будильник)
     */
    fun isBatteryOptimizationEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.packageName)
            !isIgnoring // true = оптимизация включена (плохо)
        } else {
            false // На старых версиях нет оптимизации
        }
    }
    
    /**
     * Проверяет разрешение SYSTEM_ALERT_WINDOW (для показа поверх других приложений)
     */
    fun checkOverlayPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true // На старых версиях не требуется
        }
    }
    
    /**
     * Открывает настройки для предоставления разрешения Full Screen Intent (Android 14+)
     */
    fun requestFullScreenIntentPermission(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                    data = Uri.parse("package:${activity.packageName}")
                }
                activity.startActivity(intent)
                Log.d(TAG, "Открыты настройки Full Screen Intent")
            } catch (e: Exception) {
                Log.e(TAG, "Не удалось открыть настройки Full Screen Intent", e)
                // Fallback: открываем общие настройки приложения
                openAppSettings(activity)
            }
        }
    }
    
    /**
     * Открывает настройки для отключения оптимизации батареи
     */
    fun requestIgnoreBatteryOptimization(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:${activity.packageName}")
                }
                activity.startActivity(intent)
                Log.d(TAG, "Запрос отключения Battery Optimization")
            } catch (e: Exception) {
                Log.e(TAG, "Не удалось запросить отключение Battery Optimization", e)
            }
        }
    }
    
    /**
     * Открывает настройки для предоставления разрешения SYSTEM_ALERT_WINDOW
     */
    fun requestOverlayPermission(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    data = Uri.parse("package:${activity.packageName}")
                }
                activity.startActivity(intent)
                Log.d(TAG, "Запрос разрешения Overlay")
            } catch (e: Exception) {
                Log.e(TAG, "Не удалось запросить разрешение Overlay", e)
            }
        }
    }
    
    /**
     * Открывает общие настройки приложения
     */
    fun openAppSettings(activity: ComponentActivity) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${activity.packageName}")
            }
            activity.startActivity(intent)
            Log.d(TAG, "Открыты настройки приложения")
        } catch (e: Exception) {
            Log.e(TAG, "Не удалось открыть настройки приложения", e)
        }
    }
    
    /**
     * Запрашивает все недостающие разрешения
     */
    fun requestMissingPermissions(activity: ComponentActivity) {
        val status = checkAllPermissions(activity)
        
        if (!status.hasFullScreenIntent) {
            Log.w(TAG, "⚠️ Нет разрешения Full Screen Intent - запрашиваем")
            requestFullScreenIntentPermission(activity)
        }
        
        if (status.isBatteryOptimized) {
            Log.w(TAG, "⚠️ Battery Optimization включена - запрашиваем отключение")
            requestIgnoreBatteryOptimization(activity)
        }
        
        if (!status.hasOverlayPermission) {
            Log.w(TAG, "⚠️ Нет разрешения Overlay - запрашиваем")
            requestOverlayPermission(activity)
        }
    }
    
    /**
     * Возвращает человекочитаемое описание проблем с разрешениями
     */
    fun getPermissionIssuesDescription(status: PermissionsStatus): String? {
        val issues = mutableListOf<String>()
        
        if (!status.hasFullScreenIntent) {
            issues.add("Нет разрешения на полноэкранные уведомления")
        }
        if (status.isBatteryOptimized) {
            issues.add("Включена оптимизация батареи")
        }
        if (!status.hasOverlayPermission) {
            issues.add("Нет разрешения на показ поверх других окон")
        }
        
        return if (issues.isEmpty()) {
            null
        } else {
            "Для корректной работы будильника необходимо:\n" + issues.joinToString("\n") { "• $it" }
        }
    }
}

/**
 * Статус разрешений будильника
 */
data class PermissionsStatus(
    val hasFullScreenIntent: Boolean,
    val isBatteryOptimized: Boolean,
    val hasOverlayPermission: Boolean
) {
    val allGranted: Boolean
        get() = hasFullScreenIntent && !isBatteryOptimized && hasOverlayPermission
}

