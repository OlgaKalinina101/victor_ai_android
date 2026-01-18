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

import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.dto.DeleteRequest
import com.example.victor_ai.data.network.dto.DeleteResponse
import com.example.victor_ai.data.network.dto.MemoryResponse
import com.example.victor_ai.data.network.dto.UpdateMemoryRequest
import com.example.victor_ai.data.network.dto.UpdateMemoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * API для работы с памятью ассистента
 */
interface MemoriesApi {

    /**
     * Получить все записи памяти пользователя
     * 
     * @param accountId ID пользователя (по умолчанию текущий)
     * @return Список записей памяти
     */
    @GET("assistant/memories")
    suspend fun getMemories(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): List<MemoryResponse>

    /**
     * Удалить записи памяти
     * 
     * @param accountId ID пользователя (по умолчанию текущий)
     * @param request Запрос с ID записей для удаления
     * @return Результат удаления
     */
    @POST("assistant/memories/delete")
    suspend fun deleteMemories(
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Body request: DeleteRequest
    ): DeleteResponse

    /**
     * Обновить запись памяти
     * 
     * @param recordId ID записи для обновления
     * @param accountId ID пользователя (по умолчанию текущий)
     * @param request Данные для обновления
     * @return Результат обновления
     */
    @PUT("assistant/memories/update")
    suspend fun updateMemory(
        @Query("record_id") recordId: String,
        @Query("account_id") accountId: String = UserProvider.getCurrentUserId(),
        @Body request: UpdateMemoryRequest
    ): UpdateMemoryResponse
}

