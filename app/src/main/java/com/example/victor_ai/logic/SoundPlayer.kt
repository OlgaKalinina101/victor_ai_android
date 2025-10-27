package com.example.victor_ai.logic

import android.content.Context
import android.media.SoundPool
import com.example.victor_ai.R
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlin.random.Random

class SoundPlayer(private val context: Context) {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(3)
        .build()

    private val soundId: Int = soundPool.load(context, R.raw.keypress, 1)

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private var lastPlayTime = 0L
    private var currentMinInterval = 5L

    fun playKeypress() {
        val now = System.currentTimeMillis()
        if (now - lastPlayTime < currentMinInterval) return
        lastPlayTime = now

        // Рандомный интервал: 2-6 звуков/сек = 167-500ms между звуками
        // Но мы проверяем при каждом вызове, поэтому делаем короче
        currentMinInterval = Random.nextLong(2L, 8L)  // 2-8ms вариативность

        // Рандомная громкость (0.2-0.4)
        val volume = Random.nextFloat() * 0.2f + 0.2f
        soundPool.play(soundId, volume, volume, 1, 0, 1.0f)

        // Вибрация с вариацией
        if (vibrator.hasVibrator()) {
            val vibrationDuration = Random.nextLong(12L, 20L)  // 12-20ms
            val vibrationStrength = Random.nextInt(40, 70)     // 40-70 интенсивность

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(vibrationDuration, vibrationStrength)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(vibrationDuration)
            }
        }
    }

    fun release() {
        soundPool.release()
        vibrator.cancel()
    }
}