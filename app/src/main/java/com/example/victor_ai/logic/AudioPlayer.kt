package com.example.victor_ai.logic

// AudioPlayer.kt
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

class AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var currentTempFile: File? = null
    private var onCompletionCallback: (() -> Unit)? = null  // 🔥 Callback для окончания трека

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
            stop()

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener {
                    Log.d("AudioPlayer", "MediaPlayer prepared, starting...")
                    start()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "MediaPlayer error: what=$what, extra=$extra")
                    true
                }
                setOnCompletionListener {
                    Log.d("AudioPlayer", "Playback completed")
                    onCompletionCallback?.invoke()  // 🔥 Вызываем callback
                }
                prepareAsync()  // ← стримит и готовит в фоне
            }

            Log.d("AudioPlayer", "Started streaming from $url")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error playing from URL", e)
        }
    }

    fun pause() {
        try {
            mediaPlayer?.pause()
            Log.d("AudioPlayer", "Paused")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error pausing", e)
        }
    }

    fun resume() {
        try {
            mediaPlayer?.start()
            Log.d("AudioPlayer", "Resumed")
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error resuming", e)
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