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

package com.example.victor_ai.ui.alarm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.NotificationManagerCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.offset
import com.example.victor_ai.R
import com.example.victor_ai.alarm.AlarmConstants
import com.example.victor_ai.service.AlarmService
import com.example.victor_ai.ui.components.EyeState
import com.example.victor_ai.ui.components.VictorEyes
import dagger.hilt.android.AndroidEntryPoint

/**
 * Full-Screen Activity для будильника
 * Показывается поверх экрана блокировки
 */
@AndroidEntryPoint
class AlarmRingActivity : ComponentActivity() {

    companion object {
        private const val TAG = "AlarmRingActivity"
    }

    /**
     * Настраивает Activity для показа поверх экрана блокировки
     * Работает на всех версиях Android
     */
    private fun setupLockScreenDisplay() {
        // 🔥 Для Android 8.1+ (API 27+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            
            // 🔥 Для Android 8+ пытаемся разблокировать keyguard
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val keyguardManager = getSystemService(android.content.Context.KEYGUARD_SERVICE) as android.app.KeyguardManager
                keyguardManager.requestDismissKeyguard(this, null)
            }
            
            Log.d(TAG, "✅ Используем API 27+ для показа на экране блокировки")
        } else {
            // 🔥 Для Android 7.1 и ниже (API 26-)
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
            
            Log.d(TAG, "✅ Используем deprecated flags для показа на экране блокировки")
        }
        
        // 🔥 ДОПОЛНИТЕЛЬНЫЕ флаги для надежности на разных устройствах
        @Suppress("DEPRECATION")
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "🔔🔔🔔 AlarmRingActivity.onCreate() вызван!")
        Log.d(TAG, "  Android API: ${Build.VERSION.SDK_INT}")
        Log.d(TAG, "  Intent action: ${intent?.action}")
        
        // 🔥 КРИТИЧНО: Настраиваем Activity для показа поверх экрана блокировки
        try {
            setupLockScreenDisplay()
            Log.d(TAG, "✅ setupLockScreenDisplay() выполнен успешно")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка в setupLockScreenDisplay()", e)
        }

        val alarmId = intent?.getIntExtra(AlarmConstants.EXTRA_ALARM_ID, 9999) ?: 9999
        val trackId = intent?.getIntExtra(AlarmConstants.EXTRA_TRACK_ID, -1)?.takeIf { it > 0 }
            ?: intent?.getStringExtra("track_id")?.toIntOrNull()
        val alarmTime = intent?.getStringExtra(AlarmConstants.EXTRA_ALARM_TIME)
            ?: intent?.getStringExtra("alarm_time")
            ?: "??:??"

        Log.d(TAG, "📦 Данные из Intent:")
        Log.d(TAG, "  alarmId: $alarmId")
        Log.d(TAG, "  trackId: $trackId")
        Log.d(TAG, "  alarmTime: $alarmTime")
        
        // Safety: if Activity was opened directly, ensure alarm sound service is running.
        try {
            val serviceIntent = Intent(this, AlarmService::class.java).apply {
                action = AlarmService.ACTION_START
                putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
                if (trackId != null) putExtra(AlarmConstants.EXTRA_TRACK_ID, trackId)
                putExtra(AlarmConstants.EXTRA_ALARM_TIME, alarmTime)
            }
            startForegroundService(serviceIntent)
        } catch (e: Exception) {
            Log.e(TAG, "⚠️ Failed to ensure AlarmService is running", e)
        }

        setContent {
            AlarmRingScreen(
                alarmTime = alarmTime,
                onDismiss = {
                    // Stop sound
                    stopService(Intent(this, AlarmService::class.java).apply {
                        action = AlarmService.ACTION_STOP
                        putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
                    })

                    // Remove the ONE alarm notification
                    NotificationManagerCompat.from(this).cancel(alarmId)
                    finish()
                }
            )
        }
    }
}

@Composable
fun AlarmRingScreen(
    alarmTime: String,
    onDismiss: () -> Unit
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2929)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Глазки Victor
            VictorEyes(
                state = EyeState.IDLE,
                showTime = false,
                alignCenter = true,
                modifier = Modifier
                    .size(240.dp)
                    .offset(x = 90.dp)
            )

            // Время будильника
            Text(
                text = alarmTime,
                color = Color(0xFFE0E0E0),
                fontSize = 128.sp,
                fontFamily = didactGothic,
                textAlign = TextAlign.Center
            )

            // Сообщение
            Text(
                text = "Просыпайся ♡",
                color = Color(0xFFA6A6A6),
                fontSize = 24.sp,
                fontFamily = didactGothic,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка выключения
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF555555),
                    contentColor = Color(0xFFE0E0E0)
                ),
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            ) {
                Text(
                    text = "Выключить",
                    fontSize = 20.sp,
                    fontFamily = didactGothic
                )
            }
        }
    }
}

