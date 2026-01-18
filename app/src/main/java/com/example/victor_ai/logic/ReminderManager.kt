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

import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.victor_ai.data.network.ReminderApi
import com.example.victor_ai.data.network.ReminderDelayRequest
import com.example.victor_ai.data.network.ReminderRequest
import com.example.victor_ai.data.repository.ReminderRepository
import com.example.victor_ai.domain.model.ReminderPopup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class ReminderManager(
    activity: ComponentActivity,  // ✅ Не хранится напрямую
    private val reminderApi: ReminderApi,
    private val reminderRepository: ReminderRepository,
    private val onSnackbar: (String) -> Unit,
    private val onReminder: (ReminderPopup) -> Unit,
    private val coroutineScope: CoroutineScope  // ✅ Принимаем scope извне (lifecycleScope)
) {
    // ✅ Используем WeakReference чтобы не удерживать Activity при rotation
    private val activityRef = WeakReference(activity)
    private val _reminderPopup = MutableStateFlow<ReminderPopup?>(null)
    val reminderPopup: StateFlow<ReminderPopup?> = _reminderPopup

    private val reminderReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            handleReminderIntent(intent)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun registerReceiver() {
        val activity = activityRef.get() ?: run {
            Log.w("ReminderManager", "⚠️ Activity is null, cannot register receiver")
            return
        }

        val filter = IntentFilter().apply {
            addAction("com.example.victor_ai.OPEN_REMINDER")
            addAction("com.example.victor_ai.SHOW_REMINDER")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(
                reminderReceiver,
                filter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            @Suppress("DEPRECATION")
            activity.registerReceiver(
                reminderReceiver,
                filter,
                /* flags = */ Context.RECEIVER_NOT_EXPORTED // ← ⚠️ ВАЖНО: теперь обязательно
            )
        }
    }




    fun unregisterReceiver() {
        val activity = activityRef.get()
        if (activity == null) {
            Log.w("ReminderManager", "⚠️ Activity is null, cannot unregister receiver")
            return
        }
        try {
            activity.unregisterReceiver(reminderReceiver)
        } catch (e: IllegalArgumentException) {
            // Receiver already unregistered - это нормально
            Log.d("ReminderManager", "Receiver already unregistered")
        }
    }

    fun handleReminderIntent(intent: Intent?) {
        if (intent == null) return

        Log.d("ReminderManager", "[DEBUG] Получен интент в handleReminderIntent: action=${intent.action}, extras=${intent.extras}")

        if (intent.action == "com.example.victor_ai.SHOW_REMINDER" || intent.action == "OPEN_REMINDER") {
            val id = intent.getStringExtra("reminder_id") ?: return
            
            // 🔥 Загружаем напоминание из локальной БД вместо использования данных из пуша
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val reminderEntity = reminderRepository.getReminderById(id)
                    
                    withContext(Dispatchers.Main) {
                        if (reminderEntity != null) {
                            // ✅ Используем данные из БД (они полные и корректные)
                            Log.d("ReminderManager", "[DEBUG] Создаём popup из БД: id=$id, text=${reminderEntity.text}, repeatWeekly=${reminderEntity.repeatWeekly}")
                            _reminderPopup.value = ReminderPopup(
                                id = reminderEntity.id,
                                title = "Напоминалка ♡",
                                text = reminderEntity.text,
                                repeatWeekly = reminderEntity.repeatWeekly
                            )
                        } else {
                            // ⚠️ Fallback: если напоминание не найдено в БД - используем данные из пуша
                            val title = intent.getStringExtra("title") ?: "Напоминалка"
                            val text = intent.getStringExtra("text") ?: ""
                            val repeatWeekly = intent.getBooleanExtra("repeat_weekly", false)
                            
                            Log.w("ReminderManager", "[WARNING] Напоминание $id не найдено в БД, используем данные из пуша")
                            _reminderPopup.value = ReminderPopup(id, title, text, repeatWeekly)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ReminderManager", "[ERROR] Ошибка загрузки напоминания из БД", e)
                    
                    // Fallback на данные из пуша
                    withContext(Dispatchers.Main) {
                        val title = intent.getStringExtra("title") ?: "Напоминание"
                        val text = intent.getStringExtra("text") ?: ""
                        val repeatWeekly = intent.getBooleanExtra("repeat_weekly", false)
                        _reminderPopup.value = ReminderPopup(id, title, text, repeatWeekly)
                    }
                }
            }
        } else {
            Log.d("ReminderManager", "[ERROR] Некорректное действие интента: ${intent.action}")
        }
    }


    fun clearPopup() {
        _reminderPopup.value = null
    }

    fun disableReminderRepeat(reminderId: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val response = reminderApi.setReminderRepeatWeekly(
                    body = com.example.victor_ai.data.network.ReminderRepeatWeeklyRequest(
                        reminder_id = reminderId,
                        repeat_weekly = false
                    )
                )
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        onSnackbar("Повтор отключен")
                    } else {
                        onSnackbar("Ошибка: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    activityRef.get()?.let { activity ->
                        Toast.makeText(activity, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
                    } ?: Log.e("ReminderManager", "Cannot show error toast - activity is null")
                }
            }
        }
    }

    fun sendReminderActionCoroutine(action: String, reminderId: String, repeatWeekly: Boolean = false) {
        coroutineScope.launch(Dispatchers.IO) {  // ✅ Используем переданный scope - привязан к lifecycle
            try {
                val response = when (action) {
                    "done" -> {
                        Log.d("ReminderManager", "✅ Помечаем напоминание как выполненное: $reminderId (repeatWeekly=$repeatWeekly)")
                        reminderApi.markReminderAsDone(
                            body = ReminderRequest(reminder_id = reminderId)
                        )
                    }
                    "delay" -> {
                        Log.d("ReminderManager", "⏰ Откладываем напоминание на 1 час: $reminderId")
                        reminderApi.delayReminder(
                            body = ReminderDelayRequest(
                                reminder_id = reminderId,
                                value = 1,
                                unit = "hour"
                            )
                        )
                    }
                    else -> return@launch
                }

                // 🔥 Логика повтора для постоянных напоминаний обрабатывается на бэкенде!
                // Если repeat_weekly = true, бэкенд сам пересоздаст напоминание через 7 дней.
                // Клиент просто отправляет "done" или "delay", больше ничего делать не нужно.

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val message = when (action) {
                            "done" -> if (repeatWeekly) "Напоминание выполнено. Увидимся через неделю!" else "Напоминание выполнено"
                            "delay" -> "Перенесено на час"
                            else -> "Готово"
                        }
                        onSnackbar(message)
                        Log.d("ReminderManager", "✅ Успешно обработано действие '$action'")
                    } else {
                        onSnackbar("Ошибка: ${response.code()}")
                        Log.e("ReminderManager", "❌ Ошибка при выполнении '$action': ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ReminderManager", "❌ Исключение при выполнении '$action'", e)
                withContext(Dispatchers.Main) {
                    activityRef.get()?.let { activity ->
                        Toast.makeText(activity, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
                    } ?: Log.e("ReminderManager", "Cannot show error toast - activity is null")
                }
            }
        }
    }

    companion object
}

