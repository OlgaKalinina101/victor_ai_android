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
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.victor_ai.MainActivity
import com.example.victor_ai.R

/**
 * 🎵 Foreground Service для медиа-контроллера на экране блокировки и в уведомлениях
 *
 * Этот сервис создает MediaStyle уведомление с кнопками управления воспроизведением.
 * Команды передаются через MediaSession в AudioPlayer.
 */
class MusicPlaybackService : Service() {

    companion object {
        const val ACTION_START = "com.example.victor_ai.action.START_FOREGROUND"
        const val ACTION_STOP = "com.example.victor_ai.action.STOP_FOREGROUND"
        const val ACTION_UPDATE = "com.example.victor_ai.action.UPDATE_NOTIFICATION"

        // Actions для кнопок в уведомлении
        const val ACTION_PLAY = "com.example.victor_ai.action.PLAY"
        const val ACTION_PAUSE = "com.example.victor_ai.action.PAUSE"
        const val ACTION_NEXT = "com.example.victor_ai.action.NEXT"
        const val ACTION_PREVIOUS = "com.example.victor_ai.action.PREVIOUS"

        // Extras
        const val EXTRA_TRACK_TITLE = "track_title"
        const val EXTRA_TRACK_ARTIST = "track_artist"
        const val EXTRA_IS_PLAYING = "is_playing"
        const val EXTRA_SESSION_TOKEN = "session_token"

        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "music_playback_channel"
        private const val CHANNEL_NAME = "Воспроизведение музыки"

        /**
         * Запустить foreground service с медиа-уведомлением
         */
        fun startPlayback(
            context: Context,
            trackTitle: String,
            trackArtist: String,
            isPlaying: Boolean,
            sessionToken: MediaSessionCompat.Token?
        ) {
            val intent = Intent(context, MusicPlaybackService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_TRACK_TITLE, trackTitle)
                putExtra(EXTRA_TRACK_ARTIST, trackArtist)
                putExtra(EXTRA_IS_PLAYING, isPlaying)
                putExtra(EXTRA_SESSION_TOKEN, sessionToken)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        /**
         * Обновить уведомление (например, при изменении состояния play/pause)
         */
        fun updateNotification(
            context: Context,
            trackTitle: String,
            trackArtist: String,
            isPlaying: Boolean,
            sessionToken: MediaSessionCompat.Token?
        ) {
            val intent = Intent(context, MusicPlaybackService::class.java).apply {
                action = ACTION_UPDATE
                putExtra(EXTRA_TRACK_TITLE, trackTitle)
                putExtra(EXTRA_TRACK_ARTIST, trackArtist)
                putExtra(EXTRA_IS_PLAYING, isPlaying)
                putExtra(EXTRA_SESSION_TOKEN, sessionToken)
            }
            context.startService(intent)
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

    private var currentTrackTitle = "Неизвестный трек"
    private var currentTrackArtist = "Victor AI"
    private var isPlaying = false
    private var mediaSessionToken: MediaSessionCompat.Token? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicService", "🎵 Service onCreate")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MusicService", "📡 onStartCommand: action=${intent?.action}")

        when (intent?.action) {
            ACTION_START -> {
                currentTrackTitle = intent.getStringExtra(EXTRA_TRACK_TITLE) ?: "Неизвестный трек"
                currentTrackArtist = intent.getStringExtra(EXTRA_TRACK_ARTIST) ?: "Victor AI"
                isPlaying = intent.getBooleanExtra(EXTRA_IS_PLAYING, false)
                mediaSessionToken = intent.getParcelableExtra(EXTRA_SESSION_TOKEN)

                Log.d("MusicService", "▶️ Starting foreground service: $currentTrackTitle - $currentTrackArtist")
                startForegroundService()
            }
            ACTION_UPDATE -> {
                currentTrackTitle = intent.getStringExtra(EXTRA_TRACK_TITLE) ?: currentTrackTitle
                currentTrackArtist = intent.getStringExtra(EXTRA_TRACK_ARTIST) ?: currentTrackArtist
                isPlaying = intent.getBooleanExtra(EXTRA_IS_PLAYING, isPlaying)
                mediaSessionToken = intent.getParcelableExtra(EXTRA_SESSION_TOKEN) ?: mediaSessionToken

                Log.d("MusicService", "🔄 Updating notification: $currentTrackTitle (playing=$isPlaying)")
                updateNotificationInternal()
            }
            ACTION_STOP -> {
                Log.d("MusicService", "🛑 Stopping foreground service")
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            // Обработка команд из уведомления - передаем broadcast
            ACTION_PLAY -> {
                Log.d("MusicService", "▶️ Play command from notification")
                sendBroadcast(Intent(ACTION_PLAY))
            }
            ACTION_PAUSE -> {
                Log.d("MusicService", "⏸️ Pause command from notification")
                sendBroadcast(Intent(ACTION_PAUSE))
            }
            ACTION_NEXT -> {
                Log.d("MusicService", "⏭️ Next command from notification")
                sendBroadcast(Intent(ACTION_NEXT))
            }
            ACTION_PREVIOUS -> {
                Log.d("MusicService", "⏮️ Previous command from notification")
                sendBroadcast(Intent(ACTION_PREVIOUS))
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MusicService", "🔴 Service onDestroy")
    }

    /**
     * Запускаем foreground service с MediaStyle уведомлением
     */
    private fun startForegroundService() {
        val notification = createMediaNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        Log.d("MusicService", "✅ Foreground service started with media notification")
    }

    /**
     * Обновляем уведомление без перезапуска сервиса
     */
    private fun updateNotificationInternal() {
        val notification = createMediaNotification()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
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
                description = "Показывает текущий трек и кнопки управления"
                setShowBadge(false)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("MusicService", "✅ Notification channel created")
        }
    }

    /**
     * Создаём MediaStyle уведомление с кнопками управления
     */
    private fun createMediaNotification(): Notification {
        // Intent для открытия приложения при клике на уведомление
        val contentIntent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // PendingIntents для кнопок управления
        val previousIntent = createActionIntent(ACTION_PREVIOUS)
        val playPauseIntent = if (isPlaying) {
            createActionIntent(ACTION_PAUSE)
        } else {
            createActionIntent(ACTION_PLAY)
        }
        val nextIntent = createActionIntent(ACTION_NEXT)

        // Создаем MediaStyle notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentTrackTitle)
            .setContentText(currentTrackArtist)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: заменить на иконку музыки
            .setContentIntent(contentPendingIntent)
            .setOngoing(true) // Нельзя смахнуть
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Показывать на экране блокировки
            .setPriority(NotificationCompat.PRIORITY_LOW)

            // Добавляем кнопки
            .addAction(
                android.R.drawable.ic_media_previous,
                "Previous",
                previousIntent
            )
            .addAction(
                if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (isPlaying) "Pause" else "Play",
                playPauseIntent
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "Next",
                nextIntent
            )

        // MediaStyle для красивого отображения на экране блокировки
        if (mediaSessionToken != null) {
            builder.setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionToken)
                    .setShowActionsInCompactView(0, 1, 2) // Показывать все 3 кнопки
            )
        }

        return builder.build()
    }

    /**
     * Создаем PendingIntent для кнопок управления
     */
    private fun createActionIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicPlaybackService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(), // уникальный requestCode для каждой кнопки
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
