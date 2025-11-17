package com.example.victor_ai.data.notification

import android.content.Context
import android.util.Log
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.pushy.sdk.Pushy
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Менеджер для работы с Pushy push-уведомлениями
 * Отвечает за регистрацию устройства и отправку токена на бэкенд
 */
@Singleton
class PushyTokenManager @Inject constructor(
    private val context: Context
) {

    /**
     * Регистрация устройства в Pushy и отправка токена на бэкенд
     */
    suspend fun registerPushy() {
        try {
            val deviceToken = withContext(Dispatchers.IO) {
                Pushy.register(context)
            }

            Log.d("Pushy", "Device token: $deviceToken")
            sendTokenToBackend(deviceToken)

        } catch (e: Exception) {
            Log.e("Pushy", "Ошибка регистрации: ${e.message}")
        }
    }

    /**
     * Отправка токена на бэкенд для привязки к пользователю
     */
    private suspend fun sendTokenToBackend(token: String) {
        withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val json = """{"user_id":"${UserProvider.getCurrentUserId()}","token":"$token"}"""
                val request = Request.Builder()
                    .url("${RetrofitInstance.BASE_URL}assistant/register_token")
                    .post(json.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                Log.d("Pushy", "Backend response: ${response.code}")
            } catch (e: Exception) {
                Log.e("Pushy", "Ошибка отправки токена: ${e.message}")
            }
        }
    }
}
