package com.example.victor_ai.data.network

import android.util.Log
import com.example.victor_ai.auth.UserProvider
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object TokenSender {
    fun send(token: String) {
        val client = OkHttpClient()

        val json = """{"user_id":"${UserProvider.getCurrentUserId()}","token":"$token"}"""
        val request = Request.Builder()
            .url("${RetrofitInstance.BASE_URL}assistant/register_token")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Ошибка отправки токена: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("FCM", "Код ответа: ${response.code}")
                response.body?.string()?.let {
                    Log.d("FCM", "Ответ сервера: $it")
                }
            }
        })
    }
}


