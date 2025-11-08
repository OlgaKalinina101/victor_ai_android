package com.example.victor_ai.di

import android.content.Context
import com.example.victor_ai.data.local.AppDatabase
import com.example.victor_ai.data.local.dao.ChatMessageDao
import com.example.victor_ai.data.local.dao.MemoryDao
import com.example.victor_ai.data.local.dao.ReminderDao
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
}
