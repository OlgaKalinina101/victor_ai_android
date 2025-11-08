package com.example.victor_ai.data.repository

import android.util.Log
import com.example.victor_ai.data.local.dao.MemoryDao
import com.example.victor_ai.data.local.entity.MemoryEntity
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.dto.DeleteRequest
import com.example.victor_ai.data.network.dto.MemoryResponse
import com.example.victor_ai.data.network.dto.UpdateMemoryRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepository @Inject constructor(
    private val memoryDao: MemoryDao,
    private val apiService: ApiService
) {
    companion object {
        private const val TAG = "MemoryRepository"
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
            val memories = apiService.getMemories(accountId)

            // Конвертируем в Entity
            val entities = memories.map { it.toEntity() }

            // Очищаем старые и сохраняем новые
            memoryDao.clearAll()
            memoryDao.insertMemories(entities)

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
            apiService.deleteMemories(accountId, DeleteRequest(ids))

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
            apiService.updateMemory(
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
