package com.example.victor_ai.logic

import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.ModelUsage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getModelUsage(accountId: String): List<ModelUsage> {
        return try {
            val response = apiService.getModelUsage(accountId)
            response // просто возвращаем список
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // или кидай ошибку дальше
        }
    }
}
