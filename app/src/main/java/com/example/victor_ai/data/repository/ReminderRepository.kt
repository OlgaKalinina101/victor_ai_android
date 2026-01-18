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
            Log.d(TAG, "Синхронизация напоминалок с бэкендом для accountId=$accountId...")
            val response = reminderApi.getReminders(accountId)

            // Конвертируем DTO в Entity
            val entities = response.values.flatten().map { dto ->
                dto.toEntity()
            }

            // 🔥 Очищаем старые данные перед вставкой новых
            reminderDao.clearAll()
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
