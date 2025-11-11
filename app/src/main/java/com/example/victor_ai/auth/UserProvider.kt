package com.example.victor_ai.auth

import android.util.Log
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.network.dto.ChatMetaResponse

/**
 * Модуль авторизации
 * Предоставляет информацию о текущем пользователе
 * Использует getChatMeta для получения данных пользователя с бэкенда
 */
object UserProvider {

    private const val TAG = "UserProvider"
    private const val HARDCODED_USER_ID = "test_user" // Хардкод для запроса getChatMeta

    // Кэш данных пользователя из ChatMeta
    @Volatile
    private var chatMeta: ChatMetaResponse? = null

    /**
     * Загружает данные пользователя с бэкенда через getChatMeta
     * Использует хардкод "test_user" для запроса
     */
    suspend fun loadUserData(): Result<ChatMetaResponse> {
        return try {
            Log.d(TAG, "🔐 Загрузка данных пользователя для: $HARDCODED_USER_ID")
            Log.d(TAG, "📡 Вызываем apiService.getChatMeta($HARDCODED_USER_ID)...")
            val response = RetrofitInstance.apiService.getChatMeta(HARDCODED_USER_ID)
            Log.d(TAG, "📡 Получен ответ: isSuccessful=${response.isSuccessful}, code=${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                chatMeta = response.body()
                Log.d(TAG, "✅ Данные пользователя загружены успешно!")
                Log.d(TAG, "   account_id: ${chatMeta?.account_id}")
                Log.d(TAG, "   trust_level: ${chatMeta?.trust_level}")
                Log.d(TAG, "   model: ${chatMeta?.model}")
                Result.success(chatMeta!!)
            } else {
                Log.e(TAG, "❌ Ошибка загрузки данных: HTTP ${response.code()}, body=${response.body()}")
                Result.failure(Exception("Failed to load user data: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Исключение при загрузке данных пользователя: ${e.javaClass.simpleName}: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Возвращает ID текущего пользователя
     * Берет из загруженной ChatMeta, если данные есть
     * Иначе возвращает хардкод "test_user"
     */
    fun getCurrentUserId(): String {
        return chatMeta?.account_id ?: HARDCODED_USER_ID
    }

    /**
     * Возвращает полные данные пользователя из ChatMeta (если загружены)
     */
    fun getChatMeta(): ChatMetaResponse? {
        return chatMeta
    }
}
