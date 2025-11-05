package com.example.victor_ai.logic

// AudioPlayer.kt - переведён на ExoPlayer для стабильности
import android.content.Context
import android.os.PowerManager
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

class AudioPlayer(private val context: Context? = null) {
    private var exoPlayer: ExoPlayer? = null
    private var currentTempFile: File? = null
    private var onCompletionCallback: (() -> Unit)? = null  // 🔥 Callback для окончания трека
    private var wakeLock: PowerManager.WakeLock? = null  // 🔥 Wake Lock для работы при блокировке экрана

    fun setOnCompletionListener(callback: () -> Unit) {
        onCompletionCallback = callback
    }

    fun getCurrentPosition(): Int {
        return exoPlayer?.currentPosition?.toInt() ?: 0
    }

    fun seekTo(position: Int) {
        exoPlayer?.seekTo(position.toLong())
    }

    fun playFromUrl(url: String) {
        try {
            Log.d("AudioPlayer", "🎵 [ExoPlayer] playFromUrl called with URL: $url")
            stop()

            if (context == null) {
                Log.e("AudioPlayer", "❌ Context is null, cannot create ExoPlayer")
                return
            }

            // 🔥 Создаём Wake Lock для работы при блокировке экрана
            acquireWakeLock()

            // 🎵 Создаём ExoPlayer
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                // Настройка wake lock через setWakeMode
                setWakeMode(PowerManager.PARTIAL_WAKE_LOCK)
                Log.d("AudioPlayer", "✅ ExoPlayer created with wake mode")

                // Добавляем listener для событий
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_IDLE -> Log.d("AudioPlayer", "📱 State: IDLE")
                            Player.STATE_BUFFERING -> Log.d("AudioPlayer", "⏳ State: BUFFERING")
                            Player.STATE_READY -> Log.d("AudioPlayer", "✅ State: READY")
                            Player.STATE_ENDED -> {
                                Log.d("AudioPlayer", "✅ Playback completed normally")
                                releaseWakeLock()
                                onCompletionCallback?.invoke()
                            }
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        Log.e("AudioPlayer", "❌ ExoPlayer error: ${error.message}")
                        Log.e("AudioPlayer", "   URL was: $url")
                        Log.e("AudioPlayer", "   Error code: ${error.errorCode}")
                        Log.e("AudioPlayer", "   Cause: ${error.cause}")

                        // Декодируем ошибки ExoPlayer
                        val errorType = when (error.errorCode) {
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "NETWORK_CONNECTION_FAILED"
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> "NETWORK_TIMEOUT"
                            PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE -> "INVALID_HTTP_CONTENT_TYPE"
                            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> "BAD_HTTP_STATUS"
                            PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED -> "MALFORMED_CONTAINER"
                            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> "MALFORMED_MANIFEST"
                            else -> "UNKNOWN (${error.errorCode})"
                        }
                        Log.e("AudioPlayer", "   Error type: $errorType")

                        releaseWakeLock()
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        Log.d("AudioPlayer", "🎶 Playing state changed: $isPlaying")
                    }
                })

                // Создаём MediaItem из URL
                val mediaItem = MediaItem.fromUri(url)
                Log.d("AudioPlayer", "📡 Setting media item: $url")

                // Устанавливаем media item
                setMediaItem(mediaItem)

                // Подготавливаем плеер
                Log.d("AudioPlayer", "⏳ Preparing ExoPlayer...")
                prepare()

                // Начинаем воспроизведение
                Log.d("AudioPlayer", "▶️ Starting playback...")
                play()
            }

            Log.d("AudioPlayer", "✅ ExoPlayer configured and started")
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
            exoPlayer?.pause()
            releaseWakeLock()  // 🔥 Отпускаем Wake Lock при паузе
            Log.d("AudioPlayer", "⏸️ Paused")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "❌ Error pausing", e)
        }
    }

    fun resume() {
        try {
            acquireWakeLock()  // 🔥 Захватываем Wake Lock при возобновлении
            exoPlayer?.play()
            Log.d("AudioPlayer", "▶️ Resumed")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "❌ Error resuming", e)
            releaseWakeLock()
        }
    }

    fun stop() {
        try {
            exoPlayer?.apply {
                stop()
                release()
            }
            exoPlayer = null

            // Удаляем временный файл
            currentTempFile?.delete()
            currentTempFile = null

            releaseWakeLock()  // 🔥 Отпускаем Wake Lock при остановке

            Log.d("AudioPlayer", "🛑 Stopped and released")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "❌ Error stopping", e)
        }
    }

    fun isPlaying(): Boolean {
        return try {
            exoPlayer?.isPlaying ?: false
        } catch (e: Exception) {
            false
        }
    }
}