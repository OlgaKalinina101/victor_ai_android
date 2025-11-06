package com.example.victor_ai.logic

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.victor_ai.MainActivity
import com.example.victor_ai.R

/**
 * 🎵 Foreground Service для защиты воспроизведения музыки от Doze mode
 *
 * ВАЖНО: Этот сервис НЕ управляет воспроизведением напрямую!
 * Он просто держит foreground notification, чтобы Android не убил процесс.
 * Фактическое воспроизведение происходит через AudioPlayer в PlaylistViewModel.
 *
 * TODO: В будущем можно переместить AudioPlayer сюда для лучшей архитектуры.
 */
class MusicPlaybackService : Service() {

    companion object {
        const val ACTION_START = "com.example.victor_ai.action.START_FOREGROUND"
        const val ACTION_STOP = "com.example.victor_ai.action.STOP_FOREGROUND"
        const val EXTRA_URL = "url"

        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "music_playback_channel"
        private const val CHANNEL_NAME = "Воспроизведение музыки"

        /**
         * Запустить foreground service (защита от Doze mode)
         */
        fun startPlayback(context: Context, url: String) {
            val intent = Intent(context, MusicPlaybackService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_URL, url)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        /**
         * Остановить foreground service
         */
        fun stopPlayback(context: Context) {
            val intent = Intent(context, MusicPlaybackService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicService", "🎵 Service onCreate")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MusicService", "📡 onStartCommand: action=${intent?.action}")

        when (intent?.action) {
            ACTION_START -> {
                Log.d("MusicService", "▶️ Starting foreground service")
                startForegroundService()
            }
            ACTION_STOP -> {
                Log.d("MusicService", "🛑 Stopping foreground service")
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        // START_STICKY = перезапускаем сервис если система убила его
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Мы не используем bound service
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MusicService", "🔴 Service onDestroy")
    }

    /**
     * Запускаем foreground service с уведомлением
     */
    private fun startForegroundService() {
        val notification = createNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ требует указать тип foreground service
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        Log.d("MusicService", "✅ Foreground service started with notification")
    }

    /**
     * Создаём notification channel для Android 8+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW // LOW = не издаёт звук
            ).apply {
                description = "Показывает когда играет музыка"
                setShowBadge(false)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("MusicService", "✅ Notification channel created")
        }
    }

    /**
     * Создаём уведомление для foreground service
     */
    private fun createNotification(): Notification {
        // Intent для открытия приложения при клике на уведомление
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🎵 Воспроизведение музыки")
            .setContentText("Victor AI проигрывает музыку")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Нельзя смахнуть
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
