package com.example.victor_ai.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
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
}
