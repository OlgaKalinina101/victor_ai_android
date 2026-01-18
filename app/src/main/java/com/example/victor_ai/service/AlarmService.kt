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

package com.example.victor_ai.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.example.victor_ai.alarm.AlarmConstants
import com.example.victor_ai.alarm.AlarmNotificationBuilder
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.MusicApi
import com.example.victor_ai.data.network.getTracksPaged
import com.example.victor_ai.data.repository.TrackCacheRepository
import com.example.victor_ai.logic.AudioPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * Foreground Service для проигрывания музыки будильника
 * Устанавливает максимальную громкость и воспроизводит трек
 */
@AndroidEntryPoint
class AlarmService : Service() {

    @Inject
    lateinit var trackCacheRepository: TrackCacheRepository

    @Inject
    lateinit var alarmNotificationBuilder: AlarmNotificationBuilder
    
    @Inject
    lateinit var musicApi: MusicApi
    
    @Inject
    @Named("baseUrl")
    lateinit var baseUrl: String

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var audioPlayer: AudioPlayer
    private lateinit var audioManager: AudioManager
    
    private var originalVolume: Int = 0
    private var isVolumeRestored = false
    
    // 🔥 Автостоп будильника через 10 минут
    private val handler = Handler(Looper.getMainLooper())
    private val stopRunnable = Runnable {
        Log.w(TAG, "⏰ Timeout достигнут (10 минут), останавливаем будильник")
        stopSelf()
    }

    companion object {
        private const val TAG = "AlarmService"
        private const val ALARM_TIMEOUT_MS = 10 * 60 * 1000L // 10 минут

        const val ACTION_START = "com.example.victor_ai.action.ALARM_START"
        const val ACTION_STOP = "com.example.victor_ai.action.ALARM_STOP"
    }

    override fun onCreate() {
        super.onCreate()
        audioPlayer = AudioPlayer(applicationContext)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        
        // 🔥 Останавливаем MusicPlaybackService если он запущен
        try {
            val stopMusicIntent = Intent(this, com.example.victor_ai.logic.MusicPlaybackService::class.java).apply {
                action = com.example.victor_ai.logic.MusicPlaybackService.ACTION_STOP
            }
            startService(stopMusicIntent)
            Log.d(TAG, "🛑 MusicPlaybackService остановлен при создании AlarmService")
        } catch (e: Exception) {
            Log.e(TAG, "⚠️ Не удалось остановить MusicPlaybackService", e)
        }
        
        // 🔊 Сохраняем текущую громкость и ставим на максимум
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        
        Log.d(TAG, "Сохранена громкость: $originalVolume, ставим максимум: $maxVolume")
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
        
        // 🔥 Запрашиваем аудио-фокус для будильника (высший приоритет)
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioFocusRequest = android.media.AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    android.media.AudioAttributes.Builder()
                        .setUsage(android.media.AudioAttributes.USAGE_ALARM)
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .build()
            audioManager.requestAudioFocus(audioFocusRequest)
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                null,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
        
        Log.d(TAG, "🎧 Audio focus запрошен: результат=$result")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val alarmIdRaw = intent?.getIntExtra(AlarmConstants.EXTRA_ALARM_ID, 0) ?: 0
        val alarmId = if (alarmIdRaw != 0) alarmIdRaw else 9999

        if (action == ACTION_STOP) {
            Log.d(TAG, "🛑 AlarmService stop requested: alarmId=$alarmId")
            stopSelf()
            return START_NOT_STICKY
        }

        val trackId = intent?.getIntExtra(AlarmConstants.EXTRA_TRACK_ID, -1)
            ?.takeIf { it > 0 }
            ?: intent?.getStringExtra("track_id")?.toIntOrNull()

        val alarmTime = intent?.getStringExtra(AlarmConstants.EXTRA_ALARM_TIME)
        val label = intent?.getStringExtra(AlarmConstants.EXTRA_ALARM_LABEL)

        Log.d(TAG, "AlarmService start: alarmId=$alarmId trackId=$trackId time=$alarmTime label=$label")

        // Создаем уведомление для Foreground Service
        val notification = alarmNotificationBuilder.build(
            alarmId = alarmId,
            alarmTime = alarmTime,
            label = label
        )
        startForeground(
            alarmId,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
        
        // 🔥 Запускаем таймер автостопа (10 минут)
        handler.postDelayed(stopRunnable, ALARM_TIMEOUT_MS)
        Log.d(TAG, "⏰ Таймер автостопа запущен (10 минут)")

        if (trackId != null) {
            playAlarmTrack(trackId)
        } else {
            Log.e(TAG, "trackId не указан!")
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun playAlarmTrack(trackId: Int) {
        serviceScope.launch {
            try {
                // 🔥 Проверяем, не отменена ли корутина
                ensureActive()

                val accountId = UserProvider.getCurrentUserId()

                // 🔥 Сначала пробуем воспроизвести трек из кеша
                val cachedPath = try {
                    trackCacheRepository.getCachedTrackPath(trackId)
                } catch (e: Exception) {
                    Log.e(TAG, "⚠️ Не удалось получить путь к кешированному треку", e)
                    null
                }

                if (!cachedPath.isNullOrEmpty()) {
                    Log.d(TAG, "🎵 Воспроизводим трек будильника из кеша: $cachedPath")
                    audioPlayer.playFromFile(cachedPath)
                    return@launch
                }

                // Если кеша нет — воспроизводим по сети
                val streamUrl = "${baseUrl.trimEnd('/')}/tracks/stream/$trackId?account_id=$accountId"

                Log.d(TAG, "Начинаем проигрывание по сети: $streamUrl")

                // 🔥 Проверяем перед сетевым запросом
                ensureActive()

                // Пытаемся загрузить метаданные трека (если сеть доступна)
                try {
                    val tracks = musicApi.getTracksPaged(accountId)
                    val track = tracks.firstOrNull { it.id == trackId }

                    if (track != null) {
                        audioPlayer.updateTrackMetadata(
                            title = track.title,
                            artist = track.artist,
                            duration = (track.duration * 1000).toLong()
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "⚠️ Не удалось загрузить метаданные трека, продолжаем без них", e)
                }

                // 🔥 Проверяем перед воспроизведением
                ensureActive()

                // Запускаем воспроизведение по сети
                audioPlayer.playFromUrl(streamUrl)
                Log.d(TAG, "Трек запущен успешно!")
                
            } catch (e: CancellationException) {
                // 🔥 Корутина отменена - это нормально, пробрасываем дальше
                Log.w(TAG, "Playback cancelled")
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка воспроизведения трека: $e", e)
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "AlarmService останавливается")
        
        // 🔥 Отменяем таймер автостопа
        handler.removeCallbacks(stopRunnable)
        
        try {
            // Останавливаем музыку
            audioPlayer.pause()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при остановке аудио", e)
        } finally {
            // 🔥 Гарантированно восстанавливаем громкость и отменяем корутины
            restoreVolume()
            serviceScope.cancel()
        }
    }
    
    /**
     * 🔥 Безопасное восстановление громкости
     */
    private fun restoreVolume() {
        if (!isVolumeRestored) {
            try {
                Log.d(TAG, "Восстанавливаем громкость: $originalVolume")
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0)
                isVolumeRestored = true
            } catch (e: Exception) {
                Log.e(TAG, "Не удалось восстановить громкость", e)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

