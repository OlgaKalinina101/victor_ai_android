package com.example.victor_ai.logic

import android.util.Log
import com.example.victor_ai.data.local.entity.ReminderEntity
import com.example.victor_ai.data.network.ReminderDto
import com.example.victor_ai.data.repository.ReminderRepository
import kotlinx.coroutines.flow.first

/**
 * Helper объект для работы с напоминалками через локальный репозиторий
 * Инициализируется в Application
 */
object ReminderHelper {
    lateinit var repository: ReminderRepository

    fun initialize(reminderRepository: ReminderRepository) {
        repository = reminderRepository
    }
}

/**
 * Загружает напоминалки из локальной БД (с предварительной синхронизацией с бэкендом)
 * Возвращает в формате Map<String, List<ReminderDto>> для совместимости с существующим кодом
 */
suspend fun getRemindersFromRepository(accountId: String): Map<String, List<ReminderDto>> {
    return try {
        // Синхронизируем с бэкендом
        ReminderHelper.repository.syncWithBackend(accountId)
            .onFailure { e ->
                Log.w("ReminderHelper", "⚠️ Синхронизация не удалась, используем локальные данные: ${e.message}")
            }

        // Получаем данные из локальной БД
        val entities = ReminderHelper.repository.getReminders().first()

        // Группируем по дате для совместимости с существующим форматом
        groupRemindersByDate(entities)
    } catch (e: Exception) {
        Log.e("ReminderHelper", "❌ Ошибка загрузки напоминалок", e)
        emptyMap()
    }
}

private fun groupRemindersByDate(entities: List<ReminderEntity>): Map<String, List<ReminderDto>> {
    val result = mutableMapOf<String, MutableList<ReminderDto>>()

    entities.forEach { entity ->
        val dto = entity.toDto()
        val dateKey = entity.date ?: "no_date"
        result.getOrPut(dateKey) { mutableListOf() }.add(dto)
    }

    return result
}

private fun ReminderEntity.toDto() = ReminderDto(
    id = id,
    text = text,
    date = date,
    repeatWeekly = repeatWeekly,
    dayOfWeek = dayOfWeek
)
