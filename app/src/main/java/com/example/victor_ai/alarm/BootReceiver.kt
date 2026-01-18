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
import android.util.Log
import com.example.victor_ai.data.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "BootReceiver"
    }

    @Inject lateinit var alarmRepository: AlarmRepository
    @Inject lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "🔄 Restoring alarms after boot...")
                val enabled = alarmRepository.getEnabledAlarmsOnce()
                val selectedTrackId = alarmRepository.getSelectedTrackIdOnce()

                enabled.forEach { alarm ->
                    val time = alarm.time ?: return@forEach
                    val localTime = AlarmTimeCalculator.parseTimeOrNull(time) ?: return@forEach
                    val triggerAt = AlarmTimeCalculator.computeNextTriggerMillis(localTime, alarm.repeatMode)
                    val trackId = alarm.trackId ?: selectedTrackId

                    alarmScheduler.scheduleAlarmClock(
                        alarmId = alarm.id,
                        triggerAtMillis = triggerAt,
                        alarmTime = time,
                        alarmLabel = alarm.repeatMode,
                        trackId = trackId
                    )
                }

                Log.d(TAG, "✅ Restored ${enabled.size} alarms")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Failed to restore alarms", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}


