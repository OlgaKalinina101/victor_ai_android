package com.example.victor_ai

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.victor_ai.data.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class ReminderActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("reminder_id") ?: return
        val action = intent.action ?: return
        Log.d("FCM", "Action: $action, id=$reminderId")

        val endpoint = when (action) {
            "REMINDER_OK" -> "reminders/done"
            "REMINDER_DELAY" -> "reminders/delay"
            else -> return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("${RetrofitInstance.BASE_URL}assistant/$endpoint")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                }
                val json = """{"reminder_id":"$reminderId"}"""
                conn.outputStream.use { it.write(json.toByteArray()) }
                conn.inputStream.close()
                Log.d("FCM", "Action sent to backend: $endpoint")
            } catch (e: Exception) {
                Log.e("FCM", "Failed to send action: ${e.message}")
            }
        }
    }
}
