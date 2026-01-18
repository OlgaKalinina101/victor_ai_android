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

package com.example.victor_ai.data.repository

import android.util.Log
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.dto.ChatMetaResponse
import com.example.victor_ai.data.network.dto.WebDemoRegisterRequest
import com.example.victor_ai.data.network.dto.WebDemoRegisterResponse
import com.example.victor_ai.data.network.dto.WebDemoResolveRequest
import com.example.victor_ai.data.network.dto.WebDemoResolveResponse
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Repository для авторизации и работы с user meta данными.
 * Заменяет прямые вызовы RetrofitInstance в UserProvider.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    @Named("baseUrl") val baseUrl: String
) {
    companion object {
        private const val TAG = "AuthRepository"
    }

    /**
     * POST /auth/resolve — проверка demo_key и получение account_id
     */
    suspend fun resolveDemo(demoKey: String): Result<WebDemoResolveResponse> {
        return try {
            Log.d(TAG, "📡 resolveDemo: demo_key=${demoKey.take(6)}...")
            val response = apiService.resolveDemo(WebDemoResolveRequest(demo_key = demoKey))
            
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "✅ resolveDemo success: status=${response.body()?.status}")
                Result.success(response.body()!!)
            } else {
                val msg = "resolve failed: HTTP ${response.code()}"
                Log.e(TAG, "❌ $msg")
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ resolveDemo exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * POST /auth/register — регистрация нового пользователя
     */
    suspend fun registerDemo(
        demoKey: String,
        accountId: String,
        gender: String
    ): Result<WebDemoRegisterResponse> {
        return try {
            Log.d(TAG, "📡 registerDemo: account_id=$accountId, gender=$gender")
            val response = apiService.registerDemo(
                WebDemoRegisterRequest(
                    demo_key = demoKey,
                    account_id = accountId.trim(),
                    gender = gender
                )
            )
            
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "✅ registerDemo success: account_id=${response.body()?.account_id}")
                Result.success(response.body()!!)
            } else {
                val msg = "registration failed: HTTP ${response.code()} ${response.errorBody()?.string()}"
                Log.e(TAG, "❌ $msg")
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ registerDemo exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * GET /chat_meta/{account_id} — получение мета-данных пользователя
     */
    suspend fun getChatMeta(accountId: String): Result<ChatMetaResponse> {
        return try {
            Log.d(TAG, "📡 getChatMeta: account_id=$accountId")
            val response = apiService.getChatMeta(accountId)
            
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "✅ getChatMeta success")
                Result.success(response.body()!!)
            } else {
                val msg = "getChatMeta failed: HTTP ${response.code()}"
                Log.e(TAG, "❌ $msg")
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ getChatMeta exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}
