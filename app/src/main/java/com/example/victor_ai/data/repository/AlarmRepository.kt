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

package com.example.victor_ai.data.repository

import android.util.Log
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.local.dao.AlarmDao
import com.example.victor_ai.data.local.entity.AlarmEntity
import com.example.victor_ai.data.local.entity.AlarmSelectedTrackEntity
import com.example.victor_ai.data.network.AlarmDto
import com.example.victor_ai.data.network.AlarmItemDto
import com.example.victor_ai.data.network.AlarmsApi
import com.example.victor_ai.data.network.MusicApi
import com.example.victor_ai.data.network.getTracksPaged
import com.example.victor_ai.data.network.PayloadData
import com.example.victor_ai.data.network.SelectTrackRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * 🔔 Репозиторий для управления будильниками
 * 
 * Теперь использует Room вместо DataStore:
 * - Локальное хранение в БД (offline-first)
 * - Синхронизация с бэкендом
 * - Автоматическое кеширование треков для будильников
 */
@Singleton
class AlarmRepository @Inject constructor(
    private val alarmDao: AlarmDao,
    private val alarmsApi: AlarmsApi,
    private val musicApi: MusicApi,
    private val trackCacheRepository: TrackCacheRepository,
    @Named("baseUrl") private val baseUrl: String
) {
    companion object {
        private const val TAG = "AlarmRepository"
        
        // 🔥 Количество будильников (по умолчанию 3)
        private const val DEFAULT_ALARMS_COUNT = 3
    }

    // ═══════════════════════════════════════════════════════
    // 📖 ЧТЕНИЕ ДАННЫХ (Flow для реактивного UI)
    // ═══════════════════════════════════════════════════════
    
    /**
     * Получить все будильники (реактивно)
     */
    fun getAllAlarms(): Flow<List<AlarmEntity>> {
        return alarmDao.getAllAlarms()
    }
    
    /**
     * Получить только включенные будильники
     */
    fun getEnabledAlarms(): Flow<List<AlarmEntity>> {
        return alarmDao.getEnabledAlarms()
    }

    /**
     * Получить включенные будильники (one-shot, для BootReceiver и т.п.)
     */
    suspend fun getEnabledAlarmsOnce(): List<AlarmEntity> {
        return alarmDao.getAllAlarmsOnce().filter { it.isEnabled && it.time != null && it.time != "Null" }
    }

    /**
     * Получить будильник по id (one-shot)
     */
    suspend fun getAlarmById(alarmId: Int): AlarmEntity? {
        return alarmDao.getAlarmById(alarmId)
    }

    /**
     * Получить выбранный трек (one-shot)
     */
    suspend fun getSelectedTrackIdOnce(): Int? {
        return alarmDao.getSelectedTrackOnce()?.trackId
    }
    
    /**
     * Получить выбранный трек для будильника
     */
    fun getSelectedTrack(): Flow<AlarmSelectedTrackEntity?> {
        return alarmDao.getSelectedTrack()
    }
    
    /**
     * Получить ID выбранного трека (для совместимости со старым API)
     */
    val selectedTrackIdFlow: Flow<Int?> = alarmDao.getSelectedTrack().map { it?.trackId }
    
    /**
     * Получить данные для старого UI (для совместимости)
     */
    val alarmFlow: Flow<AlarmModelData> = alarmDao.getAllAlarms().map { alarms ->
        // Дополняем до 3 будильников, если меньше
        val paddedAlarms = alarms.take(DEFAULT_ALARMS_COUNT) + 
            List(maxOf(0, DEFAULT_ALARMS_COUNT - alarms.size)) { index ->
                AlarmEntity(
                    id = alarms.size + index + 1,
                    time = null,
                    repeatMode = when (index) {
                        0 -> "Один раз"
                        1 -> "Будни"
                        2 -> "Выходные"
                        else -> "Один раз"
                    },
                    isEnabled = false
                )
            }
        
        AlarmModelData(
            alarms = paddedAlarms.map { entity ->
                AlarmItem(
                    time = entity.time ?: "Null",
                    repeatMode = entity.repeatMode
                )
            }
        )
    }

    // ═══════════════════════════════════════════════════════
    // 💾 ЛОКАЛЬНОЕ СОХРАНЕНИЕ
    // ═══════════════════════════════════════════════════════
    
    /**
     * Сохранить будильник локально
     */
    suspend fun saveAlarmLocally(alarmIndex: Int, time: String, repeatMode: String) {
        Log.d(TAG, "💾 Сохраняем будильник #$alarmIndex: time=$time, repeatMode=$repeatMode")
        
        // Получаем текущие будильники
        val alarms = alarmDao.getAllAlarmsOnce()
        
        // Находим будильник по индексу или создаем новый
        val alarm = alarms.getOrNull(alarmIndex) ?: AlarmEntity(
            id = alarmIndex + 1,
            time = null,
            repeatMode = "Один раз",
            isEnabled = false
        )
        
        // Обновляем
        val updated = alarm.copy(
            time = if (time == "Null") null else time,
            repeatMode = repeatMode,
            isEnabled = time != "Null",
            updatedAt = System.currentTimeMillis()
        )
        
        alarmDao.insertAlarm(updated)
        Log.d(TAG, "✅ Будильник сохранен локально")
    }
    
    /**
     * Включить/выключить будильник
     */
    suspend fun setAlarmEnabled(alarmId: Int, enabled: Boolean) {
        alarmDao.setAlarmEnabled(alarmId, enabled)
        Log.d(TAG, "🔔 Будильник #$alarmId: enabled=$enabled")
        
        // Синхронизируем с бэкендом
        syncAlarmToBackend()
    }

    // ═══════════════════════════════════════════════════════
    // 🌐 СИНХРОНИЗАЦИЯ С БЭКЕНДОМ
    // ═══════════════════════════════════════════════════════
    
    /**
     * Загрузить будильники с бэкенда и сохранить локально
     */
    suspend fun fetchAlarmsFromBackend() {
        try {
            val accountId = UserProvider.getCurrentUserId()
            Log.d(TAG, "🔄 Синхронизация будильников с бэкенда для $accountId")
            
            val response = alarmsApi.getAlarms(accountId)
            
            if (response.isSuccessful) {
                val data = response.body()
                Log.d(TAG, "✅ Получены данные: alarms=${data?.alarms?.size}, trackId=${data?.selected_track_id}")
                
                // Сохраняем будильники в Room
                val entities = mutableListOf<AlarmEntity>()
                data?.alarms?.forEachIndexed { index, alarmDto ->
                    entities.add(
                        AlarmEntity(
                            id = index + 1,
                            time = alarmDto.time,
                            repeatMode = alarmDto.repeatMode ?: "Один раз",
                            isEnabled = alarmDto.time != null,
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                }
                
                // Дополняем до 3 будильников
                while (entities.size < DEFAULT_ALARMS_COUNT) {
                    entities.add(
                        AlarmEntity(
                            id = entities.size + 1,
                            time = null,
                            repeatMode = when (entities.size) {
                                0 -> "Один раз"
                                1 -> "Будни"
                                2 -> "Выходные"
                                else -> "Один раз"
                            },
                            isEnabled = false
                        )
                    )
                }
                
                // Очищаем и сохраняем
                alarmDao.clearAllAlarms()
                alarmDao.insertAlarms(entities)
                
                // Сохраняем выбранный трек
                val trackId = data?.selected_track_id
                if (trackId != null) {
                    val trackEntity = AlarmSelectedTrackEntity(
                        trackId = trackId,
                        isCached = false,
                        updatedAt = System.currentTimeMillis()
                    )
                    alarmDao.insertSelectedTrack(trackEntity)
                    Log.d(TAG, "  Трек установлен: $trackId")
                    
                    // 🔥 Автоматически кешируем трек
                    cacheAlarmTrackIfNeeded(trackId)
                } else {
                    alarmDao.clearSelectedTrack()
                    Log.d(TAG, "  Трек не установлен (null)")
                }
                
                Log.d(TAG, "✅ Данные будильников сохранены локально")
            } else {
                Log.e(TAG, "❌ Ошибка получения данных: code=${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Не удалось получить данные будильников с бэкенда", e)
        }
    }
    
    /**
     * Обновить будильник (локально + бэкенд)
     */
    suspend fun updateAlarm(alarmIndex: Int, time: String, repeatMode: String) {
        Log.d(TAG, "🔔 updateAlarm() called: alarmIndex=$alarmIndex time=$time repeatMode=$repeatMode")
        
        saveAlarmLocally(alarmIndex, time, repeatMode)
        Log.d(TAG, "✅ Сохранено локально")
        
        // Проверяем выбранный трек
        val selectedTrackId = selectedTrackIdFlow.first()
        Log.d(TAG, "📀 Выбранный трек для будильника: $selectedTrackId")
        
        syncAlarmToBackend()
        Log.d(TAG, "✅ Синхронизация с бэкендом завершена")
    }
    
    /**
     * Синхронизировать локальные данные с бэкендом
     */
    suspend fun syncAlarmToBackend() {
        val alarms = alarmDao.getAllAlarmsOnce()
        Log.d(TAG, "syncAlarmToBackend(): alarms count=${alarms.size}")

        val dto = AlarmDto(
            account_id = UserProvider.getCurrentUserId(),
            alarms = alarms.map { alarm ->
                AlarmItemDto(
                    time = alarm.time,
                    repeatMode = if (alarm.time == null) null else alarm.repeatMode
                )
            }
        )
        
        Log.d(TAG, "🔔 Отправляем на бэкенд:")
        Log.d(TAG, "  account_id: ${dto.account_id}")
        dto.alarms.forEachIndexed { index, alarm ->
            Log.d(TAG, "  Будильник #$index: time=${alarm.time}, repeatMode=${alarm.repeatMode}")
        }

        try {
            val response = alarmsApi.updateAlarm(dto)
            Log.d(TAG, "✅ updateAlarm response: code=${response.code()} success=${response.isSuccessful}")
            if (!response.isSuccessful) {
                Log.e(TAG, "❌ Ошибка от бэкенда: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Не удалось отправить будильник на бэкенд", e)
        }
    }

    // ═══════════════════════════════════════════════════════
    // 🎵 ВЫБОР ТРЕКА ДЛЯ БУДИЛЬНИКА
    // ═══════════════════════════════════════════════════════
    
    /**
     * Вручную выбрать трек для будильника (кнопка "Поставить самой")
     */
    suspend fun selectTrack(trackId: Int?): Boolean {
        val accountId = UserProvider.getCurrentUserId()
        Log.d(TAG, "selectTrack() called: accountId=$accountId trackId=$trackId")

        return try {
            val request = SelectTrackRequest(
                account_id = accountId,
                track_id = trackId
            )
            val response = alarmsApi.selectTrack(request)
            
            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "selectTrack response: ${body?.status}, selected_track_id=${body?.selected_track_id}")
                
                // Сохраняем локально
                if (trackId == null) {
                    alarmDao.clearSelectedTrack()
                } else {
                    val entity = AlarmSelectedTrackEntity(
                        trackId = trackId,
                        isCached = false,
                        updatedAt = System.currentTimeMillis()
                    )
                    alarmDao.insertSelectedTrack(entity)
                    
                    // 🔥 Автоматически кешируем трек
                    cacheAlarmTrackIfNeeded(trackId)
                }
                true
            } else {
                Log.e(TAG, "selectTrack failed: code=${response.code()} error=${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Не удалось выбрать трек", e)
            false
        }
    }

    /**
     * Автоматически выбрать трек (кнопка "Разбуди меня сам...")
     */
    suspend fun selectTrackForYourself(extraContext: String? = null): Boolean {
        val accountId = UserProvider.getCurrentUserId()
        Log.d(TAG, "selectTrackForYourself() called: accountId=$accountId")

        return try {
            val payload = PayloadData(extra_context = extraContext)
            val response = alarmsApi.selectTrackForYourself(
                accountId = accountId,
                payload = payload
            )
            
            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "selectTrackForYourself response: ${body?.status}, message=${body?.message}")
                
                // Сохраняем локально
                val trackId = body?.selected_track_id
                if (trackId == null) {
                    alarmDao.clearSelectedTrack()
                } else {
                    val entity = AlarmSelectedTrackEntity(
                        trackId = trackId,
                        isCached = false,
                        updatedAt = System.currentTimeMillis()
                    )
                    alarmDao.insertSelectedTrack(entity)
                    
                    // 🔥 Автоматически кешируем трек
                    cacheAlarmTrackIfNeeded(trackId)
                }
                true
            } else {
                Log.e(TAG, "selectTrackForYourself failed: code=${response.code()} error=${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Не удалось автоматически выбрать трек", e)
            false
        }
    }
    
    // ═══════════════════════════════════════════════════════
    // 🔥 АВТОМАТИЧЕСКОЕ КЕШИРОВАНИЕ ТРЕКОВ
    // ═══════════════════════════════════════════════════════
    
    /**
     * Закешировать трек для будильника, если он еще не закеширован
     */
    private suspend fun cacheAlarmTrackIfNeeded(trackId: Int) {
        try {
            Log.d(TAG, "🎵 Проверяем кеш для трека будильника: $trackId")
            
            // Проверяем, закеширован ли уже
            val isCached = trackCacheRepository.isCached(trackId)
            
            if (isCached) {
                Log.d(TAG, "✅ Трек уже закеширован: $trackId")
                alarmDao.updateTrackCachedStatus(true)
                return
            }
            
            Log.d(TAG, "📥 Начинаем кеширование трека будильника: $trackId")
            
            // Получаем информацию о треке
            val track = try {
                musicApi.getTracksPaged(UserProvider.getCurrentUserId())
                    .firstOrNull { it.id == trackId }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Не удалось получить информацию о треке $trackId", e)
                null
            }
            
            if (track == null) {
                Log.e(TAG, "❌ Трек $trackId не найден в плейлисте")
                return
            }
            
            // Кешируем трек
            val result = trackCacheRepository.cacheTrack(
                track = track,
                accountId = UserProvider.getCurrentUserId(),
                baseUrl = baseUrl
            )
            
            result.fold(
                onSuccess = { path ->
                    Log.d(TAG, "✅ Трек будильника закеширован: ${track.title}")
                    alarmDao.updateTrackCachedStatus(true)
                },
                onFailure = { error ->
                    Log.e(TAG, "❌ Ошибка кеширования трека будильника: ${error.message}")
                }
            )
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Не удалось закешировать трек будильника", e)
        }
    }
}

// ═══════════════════════════════════════════════════════
// 📦 DATA MODELS (для совместимости со старым API)
// ═══════════════════════════════════════════════════════

data class AlarmModelData(
    val alarms: List<AlarmItem>
)

data class AlarmItem(
    val time: String,
    val repeatMode: String
)
