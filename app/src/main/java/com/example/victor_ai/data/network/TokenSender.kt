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

package com.example.victor_ai.data.network

import android.util.Log
import com.example.victor_ai.BuildConfig
import com.example.victor_ai.auth.UserProvider
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object TokenSender {
    fun send(token: String) {
        // 🔥 ИСПРАВЛЕНО: Создаем OkHttpClient с правильными настройками (HTTP/1.1 only)
        val client = OkHttpClient.Builder()
            .protocols(listOf(okhttp3.Protocol.HTTP_1_1))
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        // Не отправляем токен, пока accountId реально не известен (не фоллбечимся на "test_user")
        val accountId = UserProvider.getResolvedAccountIdOrNull() ?: run {
            Log.w("FCM", "Skip token send: accountId not resolved yet")
            return
        }

        val json = """{"user_id":"$accountId","token":"$token"}"""
        val request = Request.Builder()
            .url("${BuildConfig.BASE_URL}assistant/register_token")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "Ошибка отправки токена: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                // 🔥 ИСПРАВЛЕНО: Закрываем response чтобы избежать connection leak
                response.use { resp ->
                    Log.d("FCM", "Код ответа: ${resp.code}")
                    resp.body?.string()?.let {
                        Log.d("FCM", "Ответ сервера: $it")
                    }
                }
            }
        })
    }
}


