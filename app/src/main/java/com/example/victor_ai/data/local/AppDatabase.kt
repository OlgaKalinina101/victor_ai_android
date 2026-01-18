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

package com.example.victor_ai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.victor_ai.data.local.converter.MetadataConverter
import com.example.victor_ai.data.local.dao.AlarmDao
import com.example.victor_ai.data.local.dao.CareBankDao
import com.example.victor_ai.data.local.dao.ChatMessageDao
import com.example.victor_ai.data.local.dao.MemoryDao
import com.example.victor_ai.data.local.dao.ReminderDao
import com.example.victor_ai.data.local.dao.TrackCacheDao
import com.example.victor_ai.data.local.entity.AlarmEntity
import com.example.victor_ai.data.local.entity.AlarmSelectedTrackEntity
import com.example.victor_ai.data.local.entity.CareBankEntity
import com.example.victor_ai.data.local.entity.ChatMessageEntity
import com.example.victor_ai.data.local.entity.MemoryEntity
import com.example.victor_ai.data.local.entity.ReminderEntity
import com.example.victor_ai.data.local.entity.TrackCacheEntity

@Database(
    entities = [
        ReminderEntity::class,
        ChatMessageEntity::class,
        MemoryEntity::class,
        CareBankEntity::class,
        TrackCacheEntity::class,
        AlarmEntity::class,
        AlarmSelectedTrackEntity::class
    ],
    version = 9,  // Добавлены AlarmEntity и AlarmSelectedTrackEntity
    exportSchema = false
)
@TypeConverters(MetadataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun memoryDao(): MemoryDao
    abstract fun careBankDao(): CareBankDao
    abstract fun trackCacheDao(): TrackCacheDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "victor_ai_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
