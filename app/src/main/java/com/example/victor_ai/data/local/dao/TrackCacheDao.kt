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
import com.example.victor_ai.data.local.entity.TrackCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackCacheDao {
    
    @Query("SELECT * FROM track_cache")
    fun getAllCachedTracks(): Flow<List<TrackCacheEntity>>
    
    @Query("SELECT * FROM track_cache WHERE trackId = :trackId")
    suspend fun getCachedTrack(trackId: Int): TrackCacheEntity?
    
    @Query("SELECT * FROM track_cache WHERE trackId = :trackId")
    fun getCachedTrackFlow(trackId: Int): Flow<TrackCacheEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedTrack(track: TrackCacheEntity)
    
    @Query("DELETE FROM track_cache WHERE trackId = :trackId")
    suspend fun deleteCachedTrack(trackId: Int)
    
    @Query("DELETE FROM track_cache")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM track_cache")
    suspend fun getCachedCount(): Int
    
    @Query("SELECT SUM(file_size) FROM track_cache")
    suspend fun getTotalCacheSize(): Long?
}

