package com.example.victor_ai.data.repository

import android.util.Log
import com.example.victor_ai.data.local.dao.ReminderDao
import com.example.victor_ai.data.local.entity.ReminderEntity
import com.example.victor_ai.data.network.ReminderApi
import com.example.victor_ai.data.network.ReminderDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao,
    private val reminderApi: ReminderApi
) {
    companion object {
        private const val TAG = "ReminderRepository"
    }

    // Локальный источник истины - все UI читает отсюда
    fun getReminders(): Flow<List<ReminderEntity>> {
        return reminderDao.getAllReminders()
    }

    // Синхронизация с бэкендом
    suspend fun syncWithBackend(accountId: String): Result<Unit> {
        return try {
            Log.d(TAG, "Синхронизация напоминалок с бэкендом...")
            val response = reminderApi.getReminders(accountId)

            // Конвертируем DTO в Entity
            val entities = response.values.flatten().map { dto ->
                dto.toEntity()
            }

            // Сохраняем в локальную БД
            reminderDao.insertReminders(entities)

            Log.d(TAG, "✅ Синхронизация завершена: ${entities.size} напоминалок")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка синхронизации", e)
            Result.failure(e)
        }
    }

    // Получить напоминалку по ID
    suspend fun getReminderById(id: String): ReminderEntity? {
        return reminderDao.getReminderById(id)
    }

    // Удалить напоминалку
    suspend fun deleteReminder(id: String) {
        reminderDao.deleteReminder(id)
    }

    // Добавить/обновить напоминалку локально
    suspend fun saveReminder(reminder: ReminderEntity) {
        reminderDao.insertReminder(reminder)
    }
}

// Маппер DTO -> Entity
private fun ReminderDto.toEntity() = ReminderEntity(
    id = id,
    text = text,
    date = date,
    repeatWeekly = repeatWeekly,
    dayOfWeek = dayOfWeek
)
