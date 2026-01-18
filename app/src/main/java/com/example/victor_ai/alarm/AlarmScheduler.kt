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

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "AlarmScheduler"
    }

    private val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarmClock(
        alarmId: Int,
        triggerAtMillis: Long,
        alarmTime: String?,
        alarmLabel: String?,
        trackId: Int?
    ) {
        val operation = buildAlarmFirePendingIntent(alarmId, alarmTime, alarmLabel, trackId)
        val showIntent = buildAlarmShowPendingIntent(alarmId, alarmTime, alarmLabel, trackId)

        val info = AlarmManager.AlarmClockInfo(triggerAtMillis, showIntent)
        alarmManager.setAlarmClock(info, operation)

        Log.d(
            TAG,
            "✅ setAlarmClock(): alarmId=$alarmId triggerAt=${Instant.ofEpochMilli(triggerAtMillis)} time=$alarmTime repeat=$alarmLabel trackId=$trackId"
        )
    }

    fun cancel(alarmId: Int) {
        alarmManager.cancel(buildAlarmFirePendingIntent(alarmId, null, null, null))
        Log.d(TAG, "🛑 cancel(): alarmId=$alarmId")
    }

    fun fireNowViaAlarmManager(
        alarmId: Int,
        alarmTime: String?,
        alarmLabel: String?,
        trackId: Int?
    ) {
        val triggerAtMillis = System.currentTimeMillis() + 1000L
        scheduleAlarmClock(
            alarmId = alarmId,
            triggerAtMillis = triggerAtMillis,
            alarmTime = alarmTime,
            alarmLabel = alarmLabel,
            trackId = trackId
        )
    }

    private fun buildAlarmFirePendingIntent(
        alarmId: Int,
        alarmTime: String?,
        alarmLabel: String?,
        trackId: Int?
    ): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmConstants.ACTION_ALARM_FIRE
            putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
            putExtra(AlarmConstants.EXTRA_ALARM_TIME, alarmTime)
            putExtra(AlarmConstants.EXTRA_ALARM_LABEL, alarmLabel)
            if (trackId != null) putExtra(AlarmConstants.EXTRA_TRACK_ID, trackId)
        }

        return PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildAlarmShowPendingIntent(
        alarmId: Int,
        alarmTime: String?,
        alarmLabel: String?,
        trackId: Int?
    ): PendingIntent {
        val intent = Intent(context, com.example.victor_ai.ui.alarm.AlarmRingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
            putExtra(AlarmConstants.EXTRA_ALARM_TIME, alarmTime)
            putExtra(AlarmConstants.EXTRA_ALARM_LABEL, alarmLabel)
            if (trackId != null) putExtra(AlarmConstants.EXTRA_TRACK_ID, trackId)
        }

        return PendingIntent.getActivity(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}


