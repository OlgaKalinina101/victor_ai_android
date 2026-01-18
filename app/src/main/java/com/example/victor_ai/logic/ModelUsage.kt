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
            android.util.Log.e("UsageRepository", "❌ Ошибка получения usage: ${e.javaClass.simpleName}: ${e.message}", e)
            emptyList() // или кидай ошибку дальше
        }
    }
}
