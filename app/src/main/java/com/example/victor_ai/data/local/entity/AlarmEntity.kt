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
 * 🔔 Сущность будильника в локальной БД
 * 
 * Хранит информацию о настроенных будильниках:
 * - Время срабатывания
 * - Режим повтора (разово, будни, выходные)
 * - Включен/выключен
 * - Выбранный трек для будильника (если есть)
 */
@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "time")
    val time: String?,  // Время в формате "HH:mm" (null = не установлен)
    
    @ColumnInfo(name = "repeat_mode")
    val repeatMode: String,  // "Один раз", "Будни", "Выходные", etc.
    
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = true,  // Включен или выключен будильник
    
    @ColumnInfo(name = "track_id")
    val trackId: Int? = null,  // ID трека для этого будильника (null = общий для всех)
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * 🎵 Сущность выбранного трека для будильника
 * 
 * Хранит общий трек для всех будильников (когда не указан индивидуальный)
 */
@Entity(tableName = "alarm_selected_track")
data class AlarmSelectedTrackEntity(
    @PrimaryKey
    val id: Int = 1,  // Всегда 1, так как запись одна
    
    @ColumnInfo(name = "track_id")
    val trackId: Int?,  // ID выбранного трека (null = не выбран)
    
    @ColumnInfo(name = "is_cached")
    val isCached: Boolean = false,  // Закеширован ли трек на устройстве
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

