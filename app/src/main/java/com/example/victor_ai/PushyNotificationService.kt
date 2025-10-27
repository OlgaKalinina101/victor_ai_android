package com.example.victor_ai

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.pushy.sdk.receivers.PushyPushReceiver


class PushyNotificationService : PushyPushReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun onMessageReceived(data: Map<String, String>, context: Context) {
        Log.d("Pushy", "Получено сообщение: $data")

        val reminderId = data["reminder_id"]
        val title = "Напоминалка 🕊"
        val body = data["text"] ?: ""

        if (MyApp.isForeground) {
            // Приложение на переднем плане - показываем через Activity
            val intent = Intent(context, MainActivity::class.java).apply {
                action = "com.example.victor_ai.SHOW_REMINDER"
                putExtra("reminder_id", reminderId)
                putExtra("title", title)
                putExtra("text", body)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } else {
            // Приложение в фоне - показываем системную нотификацию
            showReminderNotification(context, title, body, reminderId)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showReminderNotification(context: Context, title: String, body: String, reminderId: String?) {
        ensureChannel(context)
        val channelId = context.getString(R.string.reminders_channel_id)

        // Открыть приложение по клику
        val openIntent = Intent(context, MainActivity::class.java).apply {
            action = "OPEN_REMINDER"
            putExtra("reminder_id", reminderId)
            putExtra("title", title)
            putExtra("text", body)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val openPending = PendingIntent.getActivity(
            context, 1001, openIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Экшены
        val okIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = "REMINDER_OK"
            putExtra("reminder_id", reminderId)
        }
        val delayIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = "REMINDER_DELAY"
            putExtra("reminder_id", reminderId)
        }

        val okPending = PendingIntent.getBroadcast(context, 2001, okIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val delayPending = PendingIntent.getBroadcast(context, 2002, delayIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = context.getString(R.string.reminders_channel_id)
            val name = context.getString(R.string.reminders_channel_name)
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (nm.getNotificationChannel(id) == null) {
                nm.createNotificationChannel(
                    NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH).apply {
                        description = "Системные напоминания Victor AI"
                        setShowBadge(true)
                    }
                )
            }
        }
    }
}