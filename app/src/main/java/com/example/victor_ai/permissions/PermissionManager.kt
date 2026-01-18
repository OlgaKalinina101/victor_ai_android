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

package com.example.victor_ai.permissions

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class PermissionManager(
    private val activity: ComponentActivity,
    private val onAudioGranted: () -> Unit,
    private val onLocationGranted: () -> Unit,
) {
    lateinit var requestAudio: ActivityResultLauncher<String>
    lateinit var requestNotifications: ActivityResultLauncher<String>
    lateinit var requestLocation: ActivityResultLauncher<String>

    fun register() {
        // 🔹 Аудио
        requestAudio = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) onAudioGranted()
            else Toast.makeText(activity, "Разрешение на микрофон отклонено", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Уведомления
        requestNotifications = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                Toast.makeText(
                    activity,
                    "Уведомления отключены — напоминания могут быть не видны",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // 🔹 Геолокация
        requestLocation = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) onLocationGranted()
            else Toast.makeText(
                activity,
                "Геолокация отключена — рекомендации поблизости могут не работать",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun requestMicrophonePermission() {
        requestAudio.launch(Manifest.permission.RECORD_AUDIO)
    }

    fun requestLocationPermission() {
        val granted = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            onLocationGranted() // уже было разрешено
        }
    }

    /**
     * Проверяет и запрашивает разрешение на Full-Screen Intent для будильников
     * Требуется для Android 14+ (API 34+)
     */
    fun checkAndRequestFullScreenIntentPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34
            val notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            if (!notificationManager.canUseFullScreenIntent()) {
                Toast.makeText(
                    activity,
                    "🔔 КРИТИЧНО: Включите 'Уведомления на весь экран' в настройках → Уведомления → Victor AI",
                    Toast.LENGTH_LONG
                ).show()
                
                // Открываем настройки приложения
                try {
                    // Пытаемся открыть специальный экран для Full Screen Intent
                    val intent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                        data = Uri.parse("package:${activity.packageName}")
                    }
                    activity.startActivity(intent)
                } catch (e: Exception) {
                    // Fallback - открываем настройки уведомлений приложения
                    try {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                        }
                        activity.startActivity(intent)
                    } catch (e2: Exception) {
                        // Последний fallback - общие настройки приложения
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:${activity.packageName}")
                        }
                        activity.startActivity(intent)
                    }
                }
            }
        }
    }

    /**
     * Проверяет и запрашивает отключение оптимизации батареи для будильников
     * Это критично важно, чтобы будильник не пропускался системой
     */
    fun checkAndRequestBatteryOptimizationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = activity.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            val packageName = activity.packageName
            
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                Toast.makeText(
                    activity,
                    "Для надежной работы будильника отключите оптимизацию батареи",
                    Toast.LENGTH_LONG
                ).show()
                
                try {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    activity.startActivity(intent)
                } catch (e: Exception) {
                    // Fallback - открываем общие настройки оптимизации батареи
                    try {
                        val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                        activity.startActivity(intent)
                    } catch (e2: Exception) {
                        // Последний fallback - общие настройки приложения
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:$packageName")
                        }
                        activity.startActivity(intent)
                    }
                }
            }
        }
    }

    /**
     * Проверяет и запрашивает разрешение на показ поверх других окон
     * Может помочь на некоторых устройствах (особенно Samsung, Xiaomi)
     */
    fun checkAndRequestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                Toast.makeText(
                    activity,
                    "Для показа будильника поверх экрана блокировки может потребоваться это разрешение",
                    Toast.LENGTH_LONG
                ).show()
                
                try {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${activity.packageName}")
                    )
                    activity.startActivity(intent)
                } catch (e: Exception) {
                    // Fallback на общие настройки приложения
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${activity.packageName}")
                    }
                    activity.startActivity(intent)
                }
            }
        }
    }

    /**
     * Проверяет все необходимые разрешения для будильника
     * @return true если все разрешения предоставлены, false если нужно что-то запросить
     */
    fun checkAlarmPermissions(): Boolean {
        var allGranted = true
        
        // 1. Full Screen Intent (Android 14+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.canUseFullScreenIntent()) {
                allGranted = false
            }
        }
        
        // 2. Оптимизация батареи (Android 6+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = activity.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(activity.packageName)) {
                allGranted = false
            }
        }
        
        // 3. Показ поверх других окон (опционально, но может помочь)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                allGranted = false
            }
        }
        
        return allGranted
    }
}
