/**
Victor AI - Personal AI Companion for Android
Copyright (C) 2025-2026 Olga Kalinina

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.
 */

package com.example.victor_ai.logic

import android.content.Context
import android.media.SoundPool
import com.example.victor_ai.R
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.victor_ai.data.settings.SoundPlayerSettingsRepository
import kotlin.random.Random
import kotlin.math.roundToInt

class SoundPlayer(
    private val context: Context,
    private val settingsRepository: SoundPlayerSettingsRepository,
) {
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
        val soundScale = settingsRepository.settingsState.value.soundScale.coerceAtLeast(0f)
        val scaledVolume = (volume * soundScale).coerceIn(0f, 1f)
        if (scaledVolume > 0f) {
            soundPool.play(soundId, scaledVolume, scaledVolume, 1, 0, 1.0f)
        }

        // Вибрация с вариацией
        if (vibrator.hasVibrator()) {
            val vibrationDuration = Random.nextLong(12L, 20L)  // 12-20ms
            val vibrationStrength = Random.nextInt(40, 70)     // 40-70 интенсивность
            val vibrationScale = settingsRepository.settingsState.value.vibrationScale.coerceAtLeast(0f)
            val scaledStrength = (vibrationStrength * vibrationScale)
                .roundToInt()
                .coerceIn(0, 255)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (scaledStrength > 0) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(vibrationDuration, scaledStrength)
                    )
                }
            } else {
                @Suppress("DEPRECATION")
                if (vibrationScale > 0f) {
                    vibrator.vibrate(vibrationDuration)
                }
            }
        }
    }

    fun release() {
        soundPool.release()
        vibrator.cancel()
    }
}