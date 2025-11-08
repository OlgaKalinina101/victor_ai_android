package com.example.victor_ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val date: String?,             // ISO-строка
    val repeatWeekly: Boolean,
    val dayOfWeek: String? = null
)
