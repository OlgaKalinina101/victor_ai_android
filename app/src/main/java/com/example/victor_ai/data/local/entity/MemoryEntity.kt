package com.example.victor_ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.victor_ai.data.local.converter.MetadataConverter

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    @TypeConverters(MetadataConverter::class)
    val metadata: String  // JSON строка для хранения Map<String, Any>
)
