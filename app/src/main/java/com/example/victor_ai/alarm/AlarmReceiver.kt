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

package com.example.victor_ai.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.victor_ai.data.repository.AlarmRepository
import com.example.victor_ai.service.AlarmService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AlarmReceiver"
    }

    @Inject lateinit var alarmRepository: AlarmRepository
    @Inject lateinit var alarmScheduler: AlarmScheduler
    @Inject lateinit var alarmNotificationBuilder: AlarmNotificationBuilder

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != AlarmConstants.ACTION_ALARM_FIRE) return

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val alarmId = intent.getIntExtra(AlarmConstants.EXTRA_ALARM_ID, 0)
                val alarm = alarmRepository.getAlarmById(alarmId)
                val selectedTrackId = alarmRepository.getSelectedTrackIdOnce()

                val alarmTime = alarm?.time ?: intent.getStringExtra(AlarmConstants.EXTRA_ALARM_TIME)
                val repeatMode = alarm?.repeatMode ?: (intent.getStringExtra(AlarmConstants.EXTRA_ALARM_LABEL) ?: "Один раз")
                val trackId = alarm?.trackId ?: selectedTrackId ?: intent.getIntExtra(AlarmConstants.EXTRA_TRACK_ID, 1)

                Log.d(TAG, "🔔 FIRE alarmId=$alarmId time=$alarmTime repeat=$repeatMode trackId=$trackId")

                // Stop playlist music before alarm.
                try {
                    val stopMusicIntent = Intent(context, com.example.victor_ai.logic.MusicPlaybackService::class.java).apply {
                        action = com.example.victor_ai.logic.MusicPlaybackService.ACTION_STOP
                    }
                    context.startService(stopMusicIntent)
                } catch (e: Exception) {
                    Log.e(TAG, "⚠️ Failed to stop MusicPlaybackService", e)
                }

                // Post ONE alarm notification (same one will be used by foreground service).
                val notification = alarmNotificationBuilder.build(
                    alarmId = alarmId,
                    alarmTime = alarmTime,
                    label = repeatMode
                )
                try {
                    androidx.core.app.NotificationManagerCompat.from(context).notify(alarmId, notification)
                } catch (e: SecurityException) {
                    Log.e(TAG, "❌ No POST_NOTIFICATIONS permission; alarm notification may not show", e)
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Failed to post alarm notification", e)
                }

                // Start sound as foreground service using the SAME notification ID.
                val serviceIntent = Intent(context, AlarmService::class.java).apply {
                    action = AlarmService.ACTION_START
                    putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
                    putExtra(AlarmConstants.EXTRA_TRACK_ID, trackId)
                    putExtra(AlarmConstants.EXTRA_ALARM_TIME, alarmTime)
                    putExtra(AlarmConstants.EXTRA_ALARM_LABEL, repeatMode)
                }

                withContext(Dispatchers.Main) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent)
                    } else {
                        context.startService(serviceIntent)
                    }
                }

                // Reschedule for repeating modes.
                if (alarmTime != null && alarmTime != "Null") {
                    val localTime = AlarmTimeCalculator.parseTimeOrNull(alarmTime)
                    if (localTime != null) {
                        if (repeatMode == "Один раз") {
                            // one-shot: do not reschedule
                            alarmScheduler.cancel(alarmId)
                        } else {
                            val next = AlarmTimeCalculator.computeNextTriggerMillis(localTime, repeatMode)
                            alarmScheduler.scheduleAlarmClock(
                                alarmId = alarmId,
                                triggerAtMillis = next,
                                alarmTime = alarmTime,
                                alarmLabel = repeatMode,
                                trackId = trackId
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ AlarmReceiver failed", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}


