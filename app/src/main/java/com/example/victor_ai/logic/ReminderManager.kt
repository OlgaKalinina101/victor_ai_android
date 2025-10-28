package com.example.victor_ai.logic

import android.annotation.SuppressLint
import com.example.victor_ai.domain.model.ReminderPopup
import android.content.*
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.ReminderRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderManager(
    private val activity: ComponentActivity,
    private val api: ApiService,
    private val onSnackbar: (String) -> Unit,
    private val onReminder: (ReminderPopup) -> Unit // ← теперь сохраняем в поле
) {
    private val _reminderPopup = MutableStateFlow<ReminderPopup?>(null)
    val reminderPopup: StateFlow<ReminderPopup?> = _reminderPopup

    private val reminderReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            handleReminderIntent(intent)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun registerReceiver() {
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
        activity.unregisterReceiver(reminderReceiver)
    }

    fun handleReminderIntent(intent: Intent?) {
        if (intent == null) return

        Log.d("ReminderManager", "[DEBUG] Получен интент в handleReminderIntent: action=${intent.action}, extras=${intent.extras}")

        if (intent.action == "com.example.victor_ai.SHOW_REMINDER" || intent.action == "OPEN_REMINDER") {
            val id = intent.getStringExtra("reminder_id") ?: return
            val title = intent.getStringExtra("title") ?: "Напоминание"
            val text = intent.getStringExtra("text") ?: ""

            Log.d("ReminderManager", "[DEBUG] Создаём popup: id=$id, title=$title, text=$text")

            _reminderPopup.value = ReminderPopup(id, title, text)
        } else {
            Log.d("ReminderManager", "[ERROR] Некорректное действие интента: ${intent.action}")
        }
    }


    fun clearPopup() {
        _reminderPopup.value = null
    }

    fun sendReminderActionCoroutine(action: String, reminderId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = when (action) {
                    "done" -> api.markReminderAsDone(ReminderRequest(reminderId))
                    "delay" -> api.delayReminder(ReminderRequest(reminderId))
                    else -> return@launch
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        onSnackbar(if (action == "done") "Напоминание выполнено" else "Перенесено на час")
                    } else {
                        onSnackbar("Ошибка: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object
}

