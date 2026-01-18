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

package com.example.victor_ai.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 🎵 Сущность для хранения информации о кешированных треках
 * 
 * Хранит:
 * - ID трека
 * - Путь к кешированному файлу на устройстве
 * - Размер файла
 * - Дату кеширования
 */
@Entity(tableName = "track_cache")
data class TrackCacheEntity(
    @PrimaryKey
    val trackId: Int,
    
    @ColumnInfo(name = "local_path")
    val localPath: String,
    
    @ColumnInfo(name = "file_size")
    val fileSize: Long,
    
    @ColumnInfo(name = "cached_at")
    val cachedAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "artist")
    val artist: String
)

