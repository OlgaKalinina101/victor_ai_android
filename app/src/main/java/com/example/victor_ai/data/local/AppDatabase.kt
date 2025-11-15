package com.example.victor_ai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.victor_ai.data.local.converter.MetadataConverter
import com.example.victor_ai.data.local.dao.ChatMessageDao
import com.example.victor_ai.data.local.dao.MemoryDao
import com.example.victor_ai.data.local.dao.ReminderDao
import com.example.victor_ai.data.local.entity.ChatMessageEntity
import com.example.victor_ai.data.local.entity.MemoryEntity
import com.example.victor_ai.data.local.entity.ReminderEntity

@Database(
    entities = [
        ReminderEntity::class,
        ChatMessageEntity::class,
        MemoryEntity::class
    ],
    version = 2,  // Увеличена версия для добавления backendId в ChatMessageEntity
    exportSchema = false
)
@TypeConverters(MetadataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun memoryDao(): MemoryDao

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
