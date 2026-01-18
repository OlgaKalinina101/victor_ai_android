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
import com.example.victor_ai.data.local.dao.MemoryDao
import com.example.victor_ai.data.local.entity.MemoryEntity
import com.example.victor_ai.data.network.MemoriesApi
import com.example.victor_ai.data.network.dto.DeleteRequest
import com.example.victor_ai.data.network.dto.MemoryResponse
import com.example.victor_ai.data.network.dto.UpdateMemoryRequest
import com.google.gson.Gson
import com.example.victor_ai.data.network.getMemoriesPaged
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepository @Inject constructor(
    private val memoryDao: MemoryDao,
    private val memoriesApi: MemoriesApi
) {
    companion object {
        private const val TAG = "MemoryRepository"
        private const val MEMORY_BATCH_SIZE = 100
    }

    private val gson = Gson()

    // Локальный источник истины - все UI читает отсюда
    fun getMemories(): Flow<List<MemoryEntity>> {
        return memoryDao.getAllMemories()
    }

    // Получить все воспоминания один раз (без Flow)
    suspend fun getMemoriesOnce(): List<MemoryEntity> {
        return memoryDao.getAllMemoriesOnce()
    }

    // Синхронизация с бэкендом
    suspend fun syncWithBackend(accountId: String): Result<Unit> {
        return try {
            Log.d(TAG, "Синхронизация воспоминаний с бэкендом...")
            val memories = memoriesApi.getMemoriesPaged(accountId)

            // Конвертируем в Entity
            val entities = memories.map { it.toEntity() }

            // Очищаем старые и сохраняем новые батчами
            memoryDao.clearAll()
            var inserted = 0
            entities.chunked(MEMORY_BATCH_SIZE).forEach { batch ->
                memoryDao.insertMemories(batch)
                inserted += batch.size
                Log.d(TAG, "💾 Batch insert: $inserted/${entities.size}")
            }

            Log.d(TAG, "✅ Синхронизация завершена: ${entities.size} воспоминаний")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка синхронизации", e)
            Result.failure(e)
        }
    }

    // Получить воспоминание по ID
    suspend fun getMemoryById(id: String): MemoryEntity? {
        return memoryDao.getMemoryById(id)
    }

    // Удалить воспоминание локально
    suspend fun deleteMemoryLocally(id: String) {
        memoryDao.deleteMemory(id)
    }

    // Удалить воспоминания локально и на бэкенде
    suspend fun deleteMemories(accountId: String, ids: List<String>): Result<Unit> {
        return try {
            Log.d(TAG, "Удаление воспоминаний...")

            // Удаляем на бэкенде
            memoriesApi.deleteMemories(accountId, DeleteRequest(ids))

            // Удаляем локально
            memoryDao.deleteMemories(ids)

            Log.d(TAG, "✅ Воспоминания удалены: ${ids.size}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка удаления", e)
            Result.failure(e)
        }
    }

    // Обновить воспоминание локально и на бэкенде
    suspend fun updateMemory(
        id: String,
        accountId: String,
        newText: String,
        metadata: Map<String, Any>
    ): Result<Unit> {
        return try {
            Log.d(TAG, "Обновление воспоминания $id...")

            // Обновляем на бэкенде
            memoriesApi.updateMemory(
                recordId = id,
                accountId = accountId,
                request = UpdateMemoryRequest(text = newText, metadata = metadata)
            )

            // Обновляем локально
            val entity = MemoryEntity(
                id = id,
                text = newText,
                metadata = gson.toJson(metadata)
            )
            memoryDao.updateMemory(entity)

            Log.d(TAG, "✅ Воспоминание обновлено")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка обновления", e)
            Result.failure(e)
        }
    }

    // Добавить воспоминание локально
    suspend fun addMemory(memory: MemoryEntity) {
        memoryDao.insertMemory(memory)
    }
}

// Маппер MemoryResponse -> Entity
private fun MemoryResponse.toEntity(): MemoryEntity {
    val gson = Gson()
    return MemoryEntity(
        id = id,
        text = text,
        metadata = gson.toJson(metadata)
    )
}
