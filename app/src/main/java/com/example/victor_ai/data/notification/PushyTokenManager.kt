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

package com.example.victor_ai.data.notification

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.victor_ai.auth.UserProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.pushy.sdk.Pushy
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Менеджер для работы с Pushy push-уведомлениями
 * Отвечает за регистрацию устройства и отправку токена на бэкенд
 */
@Singleton
class PushyTokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("baseUrl") private val baseUrl: String
) {
    private val prefs: SharedPreferences by lazy {
        context.applicationContext.getSharedPreferences("victor_ai_pushy", Context.MODE_PRIVATE)
    }

    private companion object {
        private const val TAG = "Pushy"
        private const val KEY_DEVICE_TOKEN = "pushy_device_token"
    }

    /**
     * Регистрирует устройство в Pushy (если ещё не было) и кэширует device token локально.
     * Привязку к accountId делаем отдельно, когда accountId точно известен.
     */
    suspend fun registerPushy() {
        try {
            val token = ensureDeviceToken()
            if (token != null) {
                Log.d(TAG, "Device token ready: ${token.take(10)}***")
            } else {
                Log.w(TAG, "Device token is null, cannot cache/register")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Ошибка регистрации: ${e.message}")
        }
    }

    /**
     * Привязка push-токена к конкретному пользователю.
     * Важно: НЕ используем fallback "test_user".
     */
    suspend fun bindTokenToAccount(accountId: String) {
        if (accountId.isBlank()) return
        val token = ensureDeviceToken() ?: run {
            Log.w(TAG, "No device token yet, skip bind for accountId=$accountId")
            return
        }
        sendTokenToBackend(accountId = accountId, token = token)
    }

    private suspend fun ensureDeviceToken(): String? {
        val cached = prefs.getString(KEY_DEVICE_TOKEN, null)
        if (!cached.isNullOrBlank()) return cached

        val deviceToken = withContext(Dispatchers.IO) {
            Pushy.register(context)
        }
        prefs.edit().putString(KEY_DEVICE_TOKEN, deviceToken).apply()
        return deviceToken
    }

    private suspend fun sendTokenToBackend(accountId: String, token: String) {
        withContext(Dispatchers.IO) {
            try {
                // 🔥 ИСПРАВЛЕНО: Создаем OkHttpClient с правильными настройками (HTTP/1.1 only)
                val client = OkHttpClient.Builder()
                    .protocols(listOf(okhttp3.Protocol.HTTP_1_1))
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .build()
                    
                // Safety: never bind tokens to hardcoded fallback user_id
                val resolved = UserProvider.getResolvedAccountIdOrNull()
                if (resolved == null) {
                    Log.w(TAG, "Auth not resolved yet, skip token bind (accountId=$accountId)")
                    return@withContext
                }
                val json = """{"user_id":"$accountId","token":"$token"}"""
                val request = Request.Builder()
                    .url("${baseUrl}assistant/register_token")
                    .post(json.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                Log.d(TAG, "Backend response: ${response.code}")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка отправки токена: ${e.message}")
            }
        }
    }
}
