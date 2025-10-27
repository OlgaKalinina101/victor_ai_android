package com.example.victor_ai.logic

import android.util.Log
import com.example.victor_ai.data.models.UpdateHistoryRequest
import com.example.victor_ai.model.ChatMessage
import com.example.victor_ai.data.network.ChatApi
import com.example.victor_ai.data.network.RetrofitInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

suspend fun fetchChatHistory(): List<ChatMessage> {
    val retrofit = Retrofit.Builder()
        .baseUrl("${RetrofitInstance.BASE_URL}assistant/") // IP адрес для эмулятора
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ChatApi::class.java)
    return service.getChatHistory()
}

suspend fun updateChatHistory(messages: List<ChatMessage>): Boolean {
    return try {
        val retrofit = Retrofit.Builder()
            .baseUrl("${RetrofitInstance.BASE_URL}assistant/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ChatApi::class.java)
        val response = service.updateChatHistory(
            request = UpdateHistoryRequest(messages = messages),
            accountId = "test_user"
        )

        Log.d("ChatHistory", "✅ История обновлена: ${response.message}")
        response.success

    } catch (e: Exception) {
        Log.e("ChatHistory", "❌ Ошибка обновления истории", e)
        false
    }
}
