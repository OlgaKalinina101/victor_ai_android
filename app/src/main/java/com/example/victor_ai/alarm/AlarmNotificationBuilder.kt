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

package com.example.victor_ai.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.victor_ai.R
import com.example.victor_ai.ui.alarm.AlarmRingActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmNotificationBuilder @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val nm = context.getSystemService(NotificationManager::class.java)
        if (nm.getNotificationChannel(AlarmConstants.ALARM_CHANNEL_ID) != null) return

        val channel = NotificationChannel(
            AlarmConstants.ALARM_CHANNEL_ID,
            "Будильники",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Уведомления будильника Victor AI"
            lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
        }

        nm.createNotificationChannel(channel)
    }

    fun build(
        alarmId: Int,
        alarmTime: String?,
        label: String?
    ): android.app.Notification {
        ensureChannel()

        val fullScreenIntent = Intent(context, AlarmRingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
            putExtra(AlarmConstants.EXTRA_ALARM_TIME, alarmTime)
            putExtra(AlarmConstants.EXTRA_ALARM_LABEL, label)
        }

        val fullScreenPending = PendingIntent.getActivity(
            context,
            alarmId,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(context, AlarmDismissReceiver::class.java).apply {
            action = AlarmConstants.ACTION_ALARM_DISMISS
            putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
        }
        val dismissPending = PendingIntent.getBroadcast(
            context,
            alarmId,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val content = buildString {
            val t = alarmTime?.takeIf { it.isNotBlank() && it != "Null" }
            if (t != null) append("Время: ").append(t) else append("Сработал будильник")
        }

        return NotificationCompat.Builder(context, AlarmConstants.ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Просыпайся ♡")
            .setContentText(content)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPending, true)
            .setContentIntent(fullScreenPending)
            .addAction(0, "Выключить", dismissPending)
            .setOngoing(true)
            .build()
    }

    fun cancel(alarmId: Int) {
        NotificationManagerCompat.from(context).cancel(alarmId)
    }
}


