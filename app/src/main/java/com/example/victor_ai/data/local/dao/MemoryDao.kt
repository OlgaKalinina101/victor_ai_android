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

package com.example.victor_ai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.victor_ai.data.local.entity.MemoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories")
    fun getAllMemories(): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories")
    suspend fun getAllMemoriesOnce(): List<MemoryEntity>

    @Query("SELECT * FROM memories WHERE id = :id")
    suspend fun getMemoryById(id: String): MemoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemories(memories: List<MemoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: MemoryEntity)

    @Update
    suspend fun updateMemory(memory: MemoryEntity)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteMemory(id: String)

    @Query("DELETE FROM memories WHERE id IN (:ids)")
    suspend fun deleteMemories(ids: List<String>)

    @Query("DELETE FROM memories")
    suspend fun clearAll()
}
