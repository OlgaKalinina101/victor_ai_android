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

package com.example.victor_ai

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.ReminderApi
import com.example.victor_ai.data.network.ReminderDelayRequest
import com.example.victor_ai.data.network.ReminderRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * BroadcastReceiver для обработки действий из уведомлений напоминаний
 * (кнопки "Ок" и "Перенести на час" в шторке)
 */
@AndroidEntryPoint
class ReminderActionReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var reminderApi: ReminderApi
    
    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("reminder_id")
        val repeatWeekly = intent.getBooleanExtra("repeat_weekly", false)
        val action = intent.action
        val accountIdFromIntent = intent.getStringExtra("account_id")
        val notificationId = intent.getIntExtra("notification_id", -1)
        
        if (reminderId == null || action == null) {
            Log.e("ReminderAction", "❌ Некорректные данные: reminderId=$reminderId, action=$action")
            return
        }
        
        Log.d("ReminderAction", "🔔 Получено действие из уведомления:")
        Log.d("ReminderAction", "  action=$action")
        Log.d("ReminderAction", "  reminderId=$reminderId")
        Log.d("ReminderAction", "  repeatWeekly=$repeatWeekly")
        Log.d("ReminderAction", "  accountId(extra)=$accountIdFromIntent")
        Log.d("ReminderAction", "  notificationId=$notificationId")

        // Закрываем уведомление сразу, чтобы UI не "залипал" в шторке.
        if (notificationId != -1) {
            NotificationManagerCompat.from(context).cancel(notificationId)
        }

        // ✅ Используем goAsync() для BroadcastReceiver - продлеваем его жизнь до завершения корутины
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accountId = accountIdFromIntent ?: UserProvider.getCurrentUserId()
                Log.d("ReminderAction", "  accountId=$accountId")
                
                val response = when (action) {
                    "REMINDER_OK" -> {
                        Log.d("ReminderAction", "✅ Отправляем 'done' на бэкенд...")
                        reminderApi.markReminderAsDone(
                            accountId = accountId,
                            body = ReminderRequest(reminder_id = reminderId)
                        )
                    }
                    "REMINDER_DELAY" -> {
                        Log.d("ReminderAction", "⏰ Отправляем 'delay' на 1 час...")
                        reminderApi.delayReminder(
                            accountId = accountId,
                            body = ReminderDelayRequest(
                                reminder_id = reminderId,
                                value = 1,
                                unit = "hour"
                            )
                        )
                    }
                    else -> {
                        Log.e("ReminderAction", "❌ Неизвестное действие: $action")
                        return@launch
                    }
                }
                
                if (response.isSuccessful) {
                    Log.d("ReminderAction", "✅ Успешно обработано: $action")
                    if (repeatWeekly && action == "REMINDER_OK") {
                        Log.d("ReminderAction", "🔄 Постоянное напоминание - бэкенд сам пересоздаст через 7 дней")
                    }
                } else {
                    Log.e("ReminderAction", "❌ Ошибка от бэкенда: ${response.code()} ${response.message()}")
                    val errorBody = runCatching { response.errorBody()?.string() }.getOrNull()
                    if (!errorBody.isNullOrBlank()) {
                        Log.e("ReminderAction", "  errorBody=$errorBody")
                    }
                }
            } catch (e: Exception) {
                Log.e("ReminderAction", "❌ Исключение при обработке действия '$action'", e)
            } finally {
                // ✅ Обязательно вызываем finish() чтобы система знала что работа завершена
                pendingResult.finish()
            }
        }
    }
}
