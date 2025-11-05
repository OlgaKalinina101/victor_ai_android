package com.example.victor_ai.logic

// AudioPlayer.kt
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.PowerManager
import android.util.Log
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

class AudioPlayer(private val context: Context? = null) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentTempFile: File? = null
    private var onCompletionCallback: (() -> Unit)? = null  // 🔥 Callback для окончания трека
    private var wakeLock: PowerManager.WakeLock? = null  // 🔥 Wake Lock для работы при блокировке экрана

    fun setOnCompletionListener(callback: () -> Unit) {
        onCompletionCallback = callback
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun playFromUrl(url: String) {
        try {
            Log.d("AudioPlayer", "🎵 playFromUrl called with URL: $url")
            stop()

            // 🔥 Создаём Wake Lock для работы при блокировке экрана
            acquireWakeLock()

            mediaPlayer = MediaPlayer().apply {
                // 🔥 Устанавливаем Wake Mode для MediaPlayer
                context?.let { ctx ->
                    setWakeMode(ctx, PowerManager.PARTIAL_WAKE_LOCK)
                    Log.d("AudioPlayer", "✅ Wake mode set")
                }

                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )

                Log.d("AudioPlayer", "📡 Setting data source: $url")
                setDataSource(url)

                setOnPreparedListener {
                    Log.d("AudioPlayer", "✅ MediaPlayer prepared successfully, starting playback...")
                    try {
                        start()
                        Log.d("AudioPlayer", "🎶 Playback started")
                    } catch (e: Exception) {
                        Log.e("AudioPlayer", "❌ Error starting playback after prepare", e)
                        releaseWakeLock()
                    }
                }

                setOnErrorListener { mp, what, extra ->
                    Log.e("AudioPlayer", "❌ MediaPlayer error: what=$what, extra=$extra")
                    Log.e("AudioPlayer", "   URL was: $url")

                    // Декодируем ошибки
                    val whatStr = when(what) {
                        MediaPlayer.MEDIA_ERROR_UNKNOWN -> "MEDIA_ERROR_UNKNOWN (1)"
                        MediaPlayer.MEDIA_ERROR_SERVER_DIED -> "MEDIA_ERROR_SERVER_DIED (100)"
                        else -> "UNKNOWN ($what)"
                    }
                    val extraStr = when(extra) {
                        MediaPlayer.MEDIA_ERROR_IO -> "MEDIA_ERROR_IO (-1004) - network/file error"
                        MediaPlayer.MEDIA_ERROR_MALFORMED -> "MEDIA_ERROR_MALFORMED (-1007) - bitstream error"
                        MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> "MEDIA_ERROR_UNSUPPORTED (-1010) - format not supported"
                        MediaPlayer.MEDIA_ERROR_TIMED_OUT -> "MEDIA_ERROR_TIMED_OUT (-110) - operation timeout"
                        else -> "UNKNOWN ($extra)"
                    }

                    Log.e("AudioPlayer", "   what: $whatStr")
                    Log.e("AudioPlayer", "   extra: $extraStr")

                    releaseWakeLock()  // 🔥 Отпускаем Wake Lock при ошибке
                    true
                }

                setOnCompletionListener {
                    Log.d("AudioPlayer", "✅ Playback completed normally")
                    releaseWakeLock()  // 🔥 Отпускаем Wake Lock после завершения
                    onCompletionCallback?.invoke()  // 🔥 Вызываем callback
                }

                Log.d("AudioPlayer", "⏳ Preparing async...")
                prepareAsync()  // ← стримит и готовит в фоне
            }

            Log.d("AudioPlayer", "✅ MediaPlayer created, waiting for prepare...")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "❌ Exception in playFromUrl: ${e.message}", e)
            Log.e("AudioPlayer", "   URL was: $url")
            releaseWakeLock()
        }
    }

    /**
     * 🔥 Захватываем Wake Lock чтобы музыка играла при блокировке экрана
     */
    private fun acquireWakeLock() {
        if (context == null) {
            Log.w("AudioPlayer", "⚠️ Context is null, cannot acquire Wake Lock")
            return
        }

        if (wakeLock == null) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "VictorAI:MusicPlayback"
            )
        }

        if (wakeLock?.isHeld == false) {
            wakeLock?.acquire(10 * 60 * 1000L) // 10 минут макс
            Log.d("AudioPlayer", "🔓 Wake Lock acquired")
        }
    }

    /**
     * 🔥 Отпускаем Wake Lock когда музыка не играет
     */
    private fun releaseWakeLock() {
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
            Log.d("AudioPlayer", "🔒 Wake Lock released")
        }
    }

    fun pause() {
        try {
            mediaPlayer?.pause()
            releaseWakeLock()  // 🔥 Отпускаем Wake Lock при паузе
            Log.d("AudioPlayer", "Paused")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error pausing", e)
        }
    }

    fun resume() {
        try {
            acquireWakeLock()  // 🔥 Захватываем Wake Lock при возобновлении
            mediaPlayer?.start()
            Log.d("AudioPlayer", "Resumed")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error resuming", e)
            releaseWakeLock()
        }
    }

    fun stop() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
            mediaPlayer = null

            // Удаляем временный файл
            currentTempFile?.delete()
            currentTempFile = null

            releaseWakeLock()  // 🔥 Отпускаем Wake Lock при остановке

            Log.d("AudioPlayer", "Stopped and released")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error stopping", e)
        }
    }

    fun isPlaying(): Boolean {
        return try {
            mediaPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            false
        }
    }
}