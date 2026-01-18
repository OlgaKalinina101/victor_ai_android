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
import com.example.victor_ai.service.AlarmService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmDismissReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "AlarmDismissReceiver"
    }

    @Inject
    lateinit var alarmNotificationBuilder: AlarmNotificationBuilder

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != AlarmConstants.ACTION_ALARM_DISMISS) return

        val alarmId = intent.getIntExtra(AlarmConstants.EXTRA_ALARM_ID, 0)
        Log.d(TAG, "🛑 Dismiss requested: alarmId=$alarmId")

        // Stop sound
        context.stopService(Intent(context, AlarmService::class.java).apply {
            action = AlarmService.ACTION_STOP
            putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId)
        })

        // Remove notification
        alarmNotificationBuilder.cancel(alarmId)
    }
}


