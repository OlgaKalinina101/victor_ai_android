package com.example.victor_ai.logic

// AudioPlayer.kt - переведён на ExoPlayer для стабильности
import android.content.Context
import android.os.PowerManager
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy
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

            // 🎵 Настройка LoadControl для больших буферов
            val loadControl: LoadControl = DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    15000,  // min buffer: 15 секунд
                    50000,  // max buffer: 50 секунд
                    2500,   // buffer for playback: 2.5 секунды
                    5000    // buffer for playback after rebuffer: 5 секунд
                )
                .build()

            // 🎵 Настройка политики обработки ошибок (автоматический retry)
            val loadErrorHandlingPolicy = DefaultLoadErrorHandlingPolicy(
                5  // 5 попыток переподключения
            )

            // 🎵 MediaSourceFactory с retry policy
            val mediaSourceFactory = DefaultMediaSourceFactory(context)
                .setLoadErrorHandlingPolicy(loadErrorHandlingPolicy)

            // 🎵 Создаём ExoPlayer с retry и буферизацией
            exoPlayer = ExoPlayer.Builder(context)
                .setLoadControl(loadControl)
                .setMediaSourceFactory(mediaSourceFactory)  // 🔥 Подключаем retry policy
                .build().apply {
                    Log.d("AudioPlayer", "✅ ExoPlayer created with retry policy (5 attempts) and wake mode")
                    // Настройка wake lock через setWakeMode
                    setWakeMode(PowerManager.PARTIAL_WAKE_LOCK)

                // Добавляем listener для событий
                var hadError = false
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_IDLE -> {
                                Log.d("AudioPlayer", "📱 State: IDLE")
                                // IDLE после ошибки означает что retry не помог
                                if (hadError) {
                                    Log.e("AudioPlayer", "❌ Retry не помог, воспроизведение остановлено")
                                    releaseWakeLock()
                                    hadError = false
                                }
                            }
                            Player.STATE_BUFFERING -> {
                                if (hadError) {
                                    Log.d("AudioPlayer", "⏳ State: BUFFERING (пытаемся переподключиться...)")
                                } else {
                                    Log.d("AudioPlayer", "⏳ State: BUFFERING")
                                }
                            }
                            Player.STATE_READY -> {
                                if (hadError) {
                                    Log.d("AudioPlayer", "✅ State: READY (успешно переподключились! 🎉)")
                                    hadError = false
                                } else {
                                    Log.d("AudioPlayer", "✅ State: READY")
                                }
                            }
                            Player.STATE_ENDED -> {
                                Log.d("AudioPlayer", "✅ Playback completed normally")
                                releaseWakeLock()
                                hadError = false
                                onCompletionCallback?.invoke()
                            }
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        hadError = true  // 🔥 Отмечаем что была ошибка

                        Log.e("AudioPlayer", "❌ ExoPlayer error: ${error.message}")
                        Log.e("AudioPlayer", "   URL was: $url")
                        Log.e("AudioPlayer", "   Error code: ${error.errorCode}")
                        Log.e("AudioPlayer", "   Cause: ${error.cause}")

                        // Декодируем ошибки ExoPlayer
                        val errorType = when (error.errorCode) {
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "NETWORK_CONNECTION_FAILED (будет retry)"
                            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> "NETWORK_TIMEOUT (будет retry)"
                            PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE -> "INVALID_HTTP_CONTENT_TYPE"
                            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS -> "BAD_HTTP_STATUS"
                            PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED -> "MALFORMED_CONTAINER"
                            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED -> "MALFORMED_MANIFEST"
                            else -> "UNKNOWN (${error.errorCode})"
                        }
                        Log.e("AudioPlayer", "   Error type: $errorType")
                        Log.w("AudioPlayer", "⚠️ ExoPlayer попытается переподключиться автоматически (до 5 раз)")

                        // Не отпускаем Wake Lock сразу - даём шанс на retry
                        // releaseWakeLock() будет вызван только если retry не помогли
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