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

package com.example.victor_ai

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import com.example.victor_ai.R
import com.example.victor_ai.MainActivity
import com.example.victor_ai.ReminderActionReceiver
import com.example.victor_ai.MyApp
import com.example.victor_ai.alarm.AlarmScheduler
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyPushReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        // 🔍 Логируем ВСЕ данные, которые пришли в Intent
        Log.d("MyPushReceiver", "🔔 Получен пуш!")
        Log.d("MyPushReceiver", "Intent action: ${intent.action}")
        Log.d("MyPushReceiver", "Intent extras: ${intent.extras?.keySet()?.joinToString()}")
        
        // Выводим все extras
        intent.extras?.let { bundle ->
            for (key in bundle.keySet()) {
                Log.d("MyPushReceiver", "Extra: $key = ${bundle.get(key)}")
            }
        }
        
        // Проверяем тип пуша по наличию специфичных полей
        val trackId = intent.getStringExtra("track_id")
        val alarmTime = intent.getStringExtra("alarm_time")
        
        // Если есть track_id и alarm_time - это будильник
        if (trackId != null || alarmTime != null) {
            Log.d("MyPushReceiver", "🔔 Это БУДИЛЬНИК! trackId=$trackId, alarmTime=$alarmTime")
            
            // Синхронизируем данные с бэкенда перед запуском будильника
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            scope.launch {
                try {
                    Log.d("MyPushReceiver", "🔄 Синхронизация трека с бэкенда...")
                    alarmRepository.fetchAlarmsFromBackend()
                    Log.d("MyPushReceiver", "✅ Трек синхронизирован")
                } catch (e: Exception) {
                    Log.e("MyPushReceiver", "⚠️ Ошибка синхронизации трека: ${e.message}")
                } finally {
                    // Запускаем будильник в любом случае
                    handleAlarmRing(context, trackId, alarmTime)
                }
            }
        } else {
            // Иначе - это напоминание
            Log.d("MyPushReceiver", "📝 Это НАПОМИНАНИЕ")
            val reminderId = intent.getStringExtra("reminder_id")
            val title = intent.getStringExtra("title") ?: "Напоминалка 🕊"
            val body = intent.getStringExtra("text") ?: ""
            val repeatWeekly = intent.getBooleanExtra("repeat_weekly", false)

            if (MyApp.isForeground) {
                val openIntent = Intent(context, MainActivity::class.java).apply {
                    action = "com.example.victor_ai.SHOW_REMINDER"
                    putExtra("reminder_id", reminderId)
                    putExtra("title", title)
                    putExtra("text", body)
                    putExtra("repeat_weekly", repeatWeekly)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(openIntent)
            } else {
                showReminderNotification(context, title, body, reminderId, repeatWeekly)
            }
        }
    }
    
    private fun handleAlarmRing(context: Context, trackId: String?, alarmTime: String?) {
        Log.d("AlarmPush", "🔔🔔🔔 Будильник сработал!")
        Log.d("AlarmPush", "trackId=$trackId")
        Log.d("AlarmPush", "alarmTime=$alarmTime")

        // ✅ Новая архитектура: планируем/триггерим через AlarmManager → AlarmReceiver
        // (чтобы был "системный" flow: exact alarm → ALARM notification(fullScreen) → activity/service)
        val alarmId = 9999
        val trackIdInt = trackId?.toIntOrNull()
        alarmScheduler.fireNowViaAlarmManager(
            alarmId = alarmId,
            alarmTime = alarmTime,
            alarmLabel = "Один раз",
            trackId = trackIdInt
        )
    }

    @SuppressLint("SupportAnnotationUsage")
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showReminderNotification(
        context: Context,
        title: String,
        body: String,
        reminderId: String?,
        repeatWeekly: Boolean = false
    ) {
        ensureChannel(context)
        val channelId = context.getString(R.string.reminders_channel_id)
        val notifyId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

        val openIntent = Intent(context, MainActivity::class.java).apply {
            action = "OPEN_REMINDER"
            putExtra("reminder_id", reminderId)
            putExtra("title", title)
            putExtra("text", body)
            putExtra("repeat_weekly", repeatWeekly)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val openPending = PendingIntent.getActivity(
            context, 1001, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val accountId = UserProvider.getCurrentUserId()

        val okIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = "REMINDER_OK"
            putExtra("reminder_id", reminderId)
            putExtra("repeat_weekly", repeatWeekly)
            putExtra("account_id", accountId)
            putExtra("notification_id", notifyId)
        }
        val delayIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = "REMINDER_DELAY"
            putExtra("reminder_id", reminderId)
            putExtra("repeat_weekly", repeatWeekly)
            putExtra("account_id", accountId)
            putExtra("notification_id", notifyId)
        }

        val okPending = PendingIntent.getBroadcast(
            context, 2001, okIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val delayPending = PendingIntent.getBroadcast(
            context, 2002, delayIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setFullScreenIntent(openPending, true)
            .setContentIntent(openPending)
            .addAction(0, "Ок", okPending)
            .addAction(0, "Перенести на час", delayPending)
            .build()

        NotificationManagerCompat.from(context).notify(notifyId, notification)
    }

    private fun ensureChannel(context: Context) {
        val id = context.getString(R.string.reminders_channel_id)
        val name = context.getString(R.string.reminders_channel_name)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (nm.getNotificationChannel(id) == null) {
            val channel = NotificationChannel(
                id,
                name,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Системные напоминания Victor AI"
                setShowBadge(true)
            }
            nm.createNotificationChannel(channel)
        }
    }
}

