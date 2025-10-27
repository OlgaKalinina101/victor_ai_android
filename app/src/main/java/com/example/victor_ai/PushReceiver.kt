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

class MyPushReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        // ← Pushy передаёт данные через Intent extras
        val reminderId = intent.getStringExtra("reminder_id")
        val title = intent.getStringExtra("title") ?: "Напоминалка 🕊"
        val body = intent.getStringExtra("text") ?: ""

        if (MyApp.isForeground) {
            val openIntent = Intent(context, MainActivity::class.java).apply {
                action = "com.example.victor_ai.SHOW_REMINDER"
                putExtra("reminder_id", reminderId)
                putExtra("title", title)
                putExtra("text", body)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(openIntent)
        } else {
            showReminderNotification(context, title, body, reminderId)
        }
    }

    @SuppressLint("SupportAnnotationUsage")
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showReminderNotification(
        context: Context,
        title: String,
        body: String,
        reminderId: String?
    ) {
        ensureChannel(context)
        val channelId = context.getString(R.string.reminders_channel_id)

        val openIntent = Intent(context, MainActivity::class.java).apply {
            action = "OPEN_REMINDER"
            putExtra("reminder_id", reminderId)
            putExtra("title", title)
            putExtra("text", body)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val openPending = PendingIntent.getActivity(
            context, 1001, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val okIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = "REMINDER_OK"
            putExtra("reminder_id", reminderId)
        }
        val delayIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = "REMINDER_DELAY"
            putExtra("reminder_id", reminderId)
        }

        val okPending = PendingIntent.getBroadcast(
            context, 2001, okIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val delayPending = PendingIntent.getBroadcast(
            context, 2002, delayIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notifyId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
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

