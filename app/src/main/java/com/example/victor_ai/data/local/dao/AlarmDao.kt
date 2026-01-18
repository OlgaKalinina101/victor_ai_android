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
import com.example.victor_ai.data.local.entity.AlarmEntity
import com.example.victor_ai.data.local.entity.AlarmSelectedTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    
    // ═══════════════════════════════════════════════════════
    // 🔔 БУДИЛЬНИКИ
    // ═══════════════════════════════════════════════════════
    
    @Query("SELECT * FROM alarms ORDER BY id ASC")
    fun getAllAlarms(): Flow<List<AlarmEntity>>
    
    @Query("SELECT * FROM alarms ORDER BY id ASC")
    suspend fun getAllAlarmsOnce(): List<AlarmEntity>
    
    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: Int): AlarmEntity?
    
    @Query("SELECT * FROM alarms WHERE is_enabled = 1")
    fun getEnabledAlarms(): Flow<List<AlarmEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarms(alarms: List<AlarmEntity>)
    
    @Update
    suspend fun updateAlarm(alarm: AlarmEntity)
    
    @Query("UPDATE alarms SET is_enabled = :enabled WHERE id = :id")
    suspend fun setAlarmEnabled(id: Int, enabled: Boolean)
    
    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteAlarm(id: Int)
    
    @Query("DELETE FROM alarms")
    suspend fun clearAllAlarms()
    
    // ═══════════════════════════════════════════════════════
    // 🎵 ВЫБРАННЫЙ ТРЕК
    // ═══════════════════════════════════════════════════════
    
    @Query("SELECT * FROM alarm_selected_track WHERE id = 1")
    fun getSelectedTrack(): Flow<AlarmSelectedTrackEntity?>
    
    @Query("SELECT * FROM alarm_selected_track WHERE id = 1")
    suspend fun getSelectedTrackOnce(): AlarmSelectedTrackEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedTrack(track: AlarmSelectedTrackEntity)
    
    @Query("UPDATE alarm_selected_track SET is_cached = :cached WHERE id = 1")
    suspend fun updateTrackCachedStatus(cached: Boolean)
    
    @Query("DELETE FROM alarm_selected_track")
    suspend fun clearSelectedTrack()
}

