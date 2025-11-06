package com.example.victor_ai.logic

// AudioPlayer.kt - переведён на ExoPlayer для стабильности
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
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
    private var wifiLock: WifiManager.WifiLock? = null  // 🔥 WiFi Lock для стабильного стриминга

    // 🔥 Audio Focus управление
    private var audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private var hasAudioFocus = false

    fun setOnCompletionListener(callback: () -> Unit) {
        onCompletionCallback = callback
    }

    fun getCurrentPosition(): Int {
        return exoPlayer?.currentPosition?.toInt() ?: 0
    }

    fun seekTo(position: Int) {
        exoPlayer?.seekTo(position.toLong())
    }

    @OptIn(UnstableApi::class)
    fun playFromUrl(url: String) {
        try {
            Log.d("AudioPlayer", "🎵 [ExoPlayer] playFromUrl called with URL: $url")
            stop()

            if (context == null) {
                Log.e("AudioPlayer", "❌ Context is null, cannot create ExoPlayer")
                return
            }

            // 🔥 Создаём Wake Lock и WiFi Lock для работы при блокировке экрана
            acquireWakeLock()
            acquireWifiLock()

            // 🔥 Запрашиваем Audio Focus
            if (!requestAudioFocus()) {
                Log.w("AudioPlayer", "⚠️ Failed to acquire audio focus, but will try to play anyway")
            }

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
                                    releaseWifiLock()
                                    abandonAudioFocus()
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
                                releaseWifiLock()
                                abandonAudioFocus()
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
            releaseWifiLock()
            abandonAudioFocus()
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

    /**
     * 🔥 Захватываем WiFi Lock для стабильного стриминга при блокировке экрана
     */
    private fun acquireWifiLock() {
        if (context == null) {
            Log.w("AudioPlayer", "⚠️ Context is null, cannot acquire WiFi Lock")
            return
        }

        if (wifiLock == null) {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiLock = wifiManager.createWifiLock(
                WifiManager.WIFI_MODE_FULL_HIGH_PERF,
                "VictorAI:MusicStreaming"
            )
        }

        if (wifiLock?.isHeld == false) {
            wifiLock?.acquire()
            Log.d("AudioPlayer", "📶 WiFi Lock acquired (high performance mode)")
        }
    }

    /**
     * 🔥 Отпускаем WiFi Lock когда музыка не играет
     */
    private fun releaseWifiLock() {
        if (wifiLock?.isHeld == true) {
            wifiLock?.release()
            Log.d("AudioPlayer", "📵 WiFi Lock released")
        }
    }

    /**
     * 🔥 Запрашиваем Audio Focus для воспроизведения музыки
     */
    private fun requestAudioFocus(): Boolean {
        if (context == null) {
            Log.w("AudioPlayer", "⚠️ Context is null, cannot request audio focus")
            return false
        }

        if (audioManager == null) {
            audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }

        val audioManager = audioManager ?: return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8+ использует AudioFocusRequest
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(audioFocusChangeListener)
                .build()

            val result = audioManager.requestAudioFocus(audioFocusRequest!!)
            hasAudioFocus = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            if (hasAudioFocus) {
                Log.d("AudioPlayer", "🔊 Audio focus acquired")
            } else {
                Log.w("AudioPlayer", "⚠️ Audio focus request denied")
            }
            hasAudioFocus
        } else {
            // Android 7 и ниже
            @Suppress("DEPRECATION")
            val result = audioManager.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
            hasAudioFocus = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            if (hasAudioFocus) {
                Log.d("AudioPlayer", "🔊 Audio focus acquired (legacy API)")
            } else {
                Log.w("AudioPlayer", "⚠️ Audio focus request denied (legacy API)")
            }
            hasAudioFocus
        }
    }

    /**
     * 🔥 Отпускаем Audio Focus когда музыка не играет
     */
    private fun abandonAudioFocus() {
        if (!hasAudioFocus || audioManager == null) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let {
                audioManager?.abandonAudioFocusRequest(it)
            }
        } else {
            @Suppress("DEPRECATION")
            audioManager?.abandonAudioFocus(audioFocusChangeListener)
        }

        hasAudioFocus = false
        Log.d("AudioPlayer", "🔇 Audio focus released")
    }

    /**
     * 🔥 Обработчик изменения Audio Focus
     */
    private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                // Получили фокус обратно - возобновляем воспроизведение
                Log.d("AudioPlayer", "🔊 Audio focus GAIN - resuming playback")
                if (exoPlayer?.playWhenReady == false && exoPlayer?.playbackState == Player.STATE_READY) {
                    exoPlayer?.play()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                // Потеряли фокус навсегда (звонок, другое приложение) - останавливаемся
                Log.d("AudioPlayer", "🔇 Audio focus LOSS - pausing playback")
                exoPlayer?.pause()
                abandonAudioFocus()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Временная потеря фокуса (уведомление) - пауза
                Log.d("AudioPlayer", "⏸️ Audio focus LOSS_TRANSIENT - pausing temporarily")
                exoPlayer?.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // Можно продолжать играть тише (уведомление)
                Log.d("AudioPlayer", "🔉 Audio focus LOSS_TRANSIENT_CAN_DUCK - lowering volume")
                // ExoPlayer автоматически снижает громкость, ничего не делаем
            }
        }
    }

    fun pause() {
        try {
            exoPlayer?.pause()
            releaseWakeLock()  // 🔥 Отпускаем Wake Lock при паузе
            releaseWifiLock()  // 🔥 Отпускаем WiFi Lock при паузе
            abandonAudioFocus()  // 🔥 Отпускаем Audio Focus при паузе
            Log.d("AudioPlayer", "⏸️ Paused")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "❌ Error pausing", e)
        }
    }

    fun resume() {
        try {
            acquireWakeLock()  // 🔥 Захватываем Wake Lock при возобновлении
            acquireWifiLock()  // 🔥 Захватываем WiFi Lock при возобновлении
            requestAudioFocus()  // 🔥 Запрашиваем Audio Focus при возобновлении
            exoPlayer?.play()
            Log.d("AudioPlayer", "▶️ Resumed")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "❌ Error resuming", e)
            releaseWakeLock()
            releaseWifiLock()
            abandonAudioFocus()
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
            releaseWifiLock()  // 🔥 Отпускаем WiFi Lock при остановке
            abandonAudioFocus()  // 🔥 Отпускаем Audio Focus при остановке

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