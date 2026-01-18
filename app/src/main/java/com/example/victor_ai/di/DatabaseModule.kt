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

package com.example.victor_ai.di

import android.content.Context
import com.example.victor_ai.data.local.AppDatabase
import com.example.victor_ai.data.local.dao.AlarmDao
import com.example.victor_ai.data.local.dao.CareBankDao
import com.example.victor_ai.data.local.dao.ChatMessageDao
import com.example.victor_ai.data.local.dao.MemoryDao
import com.example.victor_ai.data.local.dao.ReminderDao
import com.example.victor_ai.data.local.dao.TrackCacheDao
import com.example.victor_ai.data.repository.TrackCacheRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideReminderDao(database: AppDatabase): ReminderDao {
        return database.reminderDao()
    }

    @Provides
    fun provideChatMessageDao(database: AppDatabase): ChatMessageDao {
        return database.chatMessageDao()
    }

    @Provides
    fun provideMemoryDao(database: AppDatabase): MemoryDao {
        return database.memoryDao()
    }

    @Provides
    fun provideCareBankDao(database: AppDatabase): CareBankDao {
        return database.careBankDao()
    }
    
    @Provides
    fun provideTrackCacheDao(database: AppDatabase): TrackCacheDao {
        return database.trackCacheDao()
    }
    
    @Provides
    fun provideAlarmDao(database: AppDatabase): AlarmDao {
        return database.alarmDao()
    }
    
    @Provides
    @Singleton
    fun provideTrackCacheRepository(
        trackCacheDao: TrackCacheDao,
        @ApplicationContext context: Context
    ): TrackCacheRepository {
        return TrackCacheRepository(trackCacheDao, context)
    }
}
