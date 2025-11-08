package com.example.victor_ai.ui.screens

import com.example.victor_ai.data.network.ReminderDto
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

typealias ReminderMap = Map<String, List<Reminder>>
data class Reminder(
    val id: String,
    val text: String,
    val date: LocalDate?,         // если разовое
    val repeatWeekly: Boolean,    // если повтор
    val dayOfWeek: DayOfWeek?,    // для repeat
    val time: LocalTime?
)

// Карта с напоминаниями на конкретные даты
fun groupReminders(apiData: Map<String, List<ReminderDto>>): Map<LocalDate, List<Reminder>> {
    val result = mutableMapOf<LocalDate, MutableList<Reminder>>()

    apiData.forEach { (key, dtoList) ->
        val dateKey = key.takeIf { it.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) }
            ?.let { LocalDate.parse(it) }

        dtoList.forEach { dto ->
            val parsedDateTime = dto.date?.let {
                LocalDateTime.parse(it)
            }

            val reminder = Reminder(
                id = dto.id,
                text = dto.text,
                date = parsedDateTime?.toLocalDate(),
                time = parsedDateTime?.toLocalTime(), // ← вот это!
                repeatWeekly = dto.repeatWeekly,
                dayOfWeek = dto.dayOfWeek?.let { DayOfWeek.valueOf(it) }
            )


            // Если это обычная дата → добавляем по дате
            if (dateKey != null) {
                result.getOrPut(dateKey) { mutableListOf() }.add(reminder)
            }
            // Иначе можно обработать повторы отдельно
        }
    }

    return result
}

