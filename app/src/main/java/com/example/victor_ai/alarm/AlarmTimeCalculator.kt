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

package com.example.victor_ai.alarm

import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object AlarmTimeCalculator {
    private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun parseTimeOrNull(time: String?): LocalTime? {
        val t = time?.trim()?.takeIf { it.isNotEmpty() && it != "Null" } ?: return null
        return runCatching { LocalTime.parse(t, TIME_FORMATTER) }.getOrNull()
    }

    fun computeNextTriggerMillis(
        time: LocalTime,
        repeatMode: String,
        now: ZonedDateTime = ZonedDateTime.now()
    ): Long {
        val mode = repeatMode.trim()

        fun candidateForDay(day: ZonedDateTime): ZonedDateTime {
            return day.withHour(time.hour).withMinute(time.minute).withSecond(0).withNano(0)
        }

        // Try today first, then up to +7 days.
        for (i in 0..7) {
            val day = now.plusDays(i.toLong())
            val dow = day.dayOfWeek

            val matches = when (mode) {
                "Будни" -> dow in setOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY
                )
                "Выходные" -> dow in setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
                "Всегда" -> true
                "Один раз" -> true
                else -> true
            }

            if (!matches) continue

            val candidate = candidateForDay(day)
            if (candidate.isAfter(now)) return candidate.toInstant().toEpochMilli()
        }

        // Fallback (should never happen)
        return candidateForDay(now.plusDays(1)).toInstant().toEpochMilli()
    }
}


