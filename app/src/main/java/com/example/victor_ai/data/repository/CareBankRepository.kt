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
import com.example.victor_ai.data.local.dao.CareBankDao
import com.example.victor_ai.data.local.entity.CareBankEntity
import com.example.victor_ai.data.network.CareBankApi
import com.example.victor_ai.data.network.dto.CareBankEntryCreate
import com.example.victor_ai.data.network.dto.CareBankEntryDto
import com.example.victor_ai.data.network.dto.CareBankSettingsRead
import com.example.victor_ai.data.network.dto.CareBankSettingsUpdate
import com.example.victor_ai.domain.model.CareBankEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CareBankRepository @Inject constructor(
    private val careBankDao: CareBankDao,
    private val careBankApi: CareBankApi
) {
    companion object {
        private const val TAG = "CareBankRepository"
    }

    /**
     * Получить все записи для текущего пользователя
     */
    fun getEntries(): Flow<List<CareBankEntry>> {
        val accountId = UserProvider.getCurrentUserId()
        return careBankDao.getEntriesByAccount(accountId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    /**
     * Синхронизация с бэкендом
     * Загружает данные с сервера и сохраняет локально
     */
    suspend fun syncWithBackend(): Result<Unit> {
        return try {
            val accountId = UserProvider.getCurrentUserId()
            Log.d(TAG, "📡 Синхронизация с бэкендом для accountId=$accountId")
            
            val response = careBankApi.getCareBankEntries(accountId)
            
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                Log.d(TAG, "📥 Получено DTO записей: ${dtos.size}")
                dtos.forEach { dto ->
                    Log.d(TAG, "   DTO: emoji=${dto.emoji}, searchField=${dto.searchField}, searchUrl=${dto.searchUrl}")
                }
                
                val entries = dtos.map { it.toEntity() }
                
                // Сохраняем все записи локально (REPLACE стратегия обновит существующие)
                entries.forEach { entry ->
                    careBankDao.insertEntry(entry)
                }
                
                Log.d(TAG, "✅ Синхронизация завершена: ${entries.size} записей")
                Result.success(Unit)
            } else {
                Log.e(TAG, "❌ Ошибка синхронизации: HTTP ${response.code()}")
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка синхронизации", e)
            Result.failure(e)
        }
    }

    /**
     * Сохранить/обновить запись в банке заботы
     * Сохраняет локально И отправляет на бэкенд
     */
    suspend fun saveEntry(emoji: String, value: String): Result<Unit> {
        return saveEntry(
            emoji = emoji,
            value = value,
            searchUrl = null,
            searchField = null,
            addToCart1Coords = null,
            addToCart2Coords = null,
            addToCart3Coords = null,
            addToCart4Coords = null,
            addToCart5Coords = null,
            openCartCoords = null,
            placeOrderCoords = null
        )
    }

    /**
     * Сохранить/обновить запись в банке заботы с координатами автоматизации
     * Сохраняет локально И отправляет на бэкенд
     */
    suspend fun saveEntry(
        emoji: String,
        value: String,
        searchUrl: String? = null,
        searchField: String? = null,
        addToCart1Coords: String? = null,
        addToCart2Coords: String? = null,
        addToCart3Coords: String? = null,
        addToCart4Coords: String? = null,
        addToCart5Coords: String? = null,
        openCartCoords: String? = null,
        placeOrderCoords: String? = null
    ): Result<Unit> {
        return try {
            val accountId = UserProvider.getCurrentUserId()
            val timestampMs = System.currentTimeMillis()

            // 1. Сохраняем локально
            val entity = CareBankEntity(
                emoji = emoji,
                accountId = accountId,
                value = value,
                timestamp = timestampMs,
                searchUrl = searchUrl,
                searchField = searchField,
                addToCart1Coords = addToCart1Coords,
                addToCart2Coords = addToCart2Coords,
                addToCart3Coords = addToCart3Coords,
                addToCart4Coords = addToCart4Coords,
                addToCart5Coords = addToCart5Coords,
                openCartCoords = openCartCoords,
                placeOrderCoords = placeOrderCoords
            )
            careBankDao.insertEntry(entity)
            Log.d(TAG, "✅ Локально сохранено: emoji=$emoji, value=$value, searchUrl=$searchUrl")

            // 2. Отправляем на бэкенд
            val createDto = CareBankEntryCreate(
                accountId = accountId,
                emoji = emoji,
                value = value,
                timestampMs = timestampMs,
                searchUrl = searchUrl,
                searchField = searchField,
                addToCart1Coords = addToCart1Coords,
                addToCart2Coords = addToCart2Coords,
                addToCart3Coords = addToCart3Coords,
                addToCart4Coords = addToCart4Coords,
                addToCart5Coords = addToCart5Coords,
                openCartCoords = openCartCoords,
                placeOrderCoords = placeOrderCoords
            )
            
            val response = careBankApi.createCareBankEntry(createDto)
            
            if (response.isSuccessful) {
                Log.d(TAG, "✅ Отправлено на бэкенд: emoji=$emoji")
                Result.success(Unit)
            } else {
                Log.e(TAG, "⚠️ Локально сохранено, но не отправлено на бэкенд: HTTP ${response.code()}")
                // Все равно считаем успехом, т.к. локально сохранили
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка сохранения записи", e)
            Result.failure(e)
        }
    }

    /**
     * Получить запись по эмодзи
     */
    suspend fun getEntryByEmoji(emoji: String): CareBankEntry? {
        val accountId = UserProvider.getCurrentUserId()
        return careBankDao.getEntryByEmoji(emoji, accountId)?.toModel()
    }

    /**
     * Удалить запись по эмодзи
     */
    suspend fun deleteEntry(emoji: String): Result<Unit> {
        return try {
            val accountId = UserProvider.getCurrentUserId()
            careBankDao.deleteEntry(emoji, accountId)
            Log.d(TAG, "✅ Запись удалена: emoji=$emoji")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка удаления записи", e)
            Result.failure(e)
        }
    }

    /**
     * Очистить все записи текущего пользователя
     */
    suspend fun clearEntries(): Result<Unit> {
        return try {
            val accountId = UserProvider.getCurrentUserId()
            careBankDao.clearEntriesByAccount(accountId)
            Log.d(TAG, "✅ Записи очищены для пользователя: $accountId")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка очистки записей", e)
            Result.failure(e)
        }
    }

    /**
     * Получить настройки банка заботы
     */
    suspend fun getCareBankSettings(): Result<CareBankSettingsRead> {
        return try {
            val accountId = UserProvider.getCurrentUserId()
            val response = careBankApi.getCareBankSettings(accountId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Не удалось получить настройки: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении настроек банка заботы", e)
            Result.failure(e)
        }
    }

    /**
     * Обновить настройки банка заботы
     */
    suspend fun updateCareBankSettings(settings: CareBankSettingsUpdate): Result<CareBankSettingsRead> {
        return try {
            val response = careBankApi.upsertCareBankSettings(settings)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Не удалось обновить настройки: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при обновлении настроек банка заботы", e)
            Result.failure(e)
        }
    }
}

// Маппер Entity -> Model
private fun CareBankEntity.toModel(): CareBankEntry {
    Log.d("CareBankRepository", "🔄 Маппинг Entity -> Model: emoji=$emoji, searchField=$searchField, searchUrl=$searchUrl")
    return CareBankEntry(
        emoji = emoji,
        accountId = accountId,
        value = value,
        timestamp = timestamp,
        searchUrl = searchUrl,
        searchField = searchField,
        addToCart1Coords = addToCart1Coords,
        addToCart2Coords = addToCart2Coords,
        addToCart3Coords = addToCart3Coords,
        addToCart4Coords = addToCart4Coords,
        addToCart5Coords = addToCart5Coords,
        openCartCoords = openCartCoords,
        placeOrderCoords = placeOrderCoords
    )
}

// Маппер DTO -> Entity
private fun CareBankEntryDto.toEntity(): CareBankEntity {
    Log.d("CareBankRepository", "🔄 Маппинг DTO -> Entity: emoji=$emoji, searchField=$searchField, searchUrl=$searchUrl")
    return CareBankEntity(
        emoji = emoji,
        accountId = accountId,
        value = value,
        timestamp = timestampMs,
        searchUrl = searchUrl,
        searchField = searchField,
        addToCart1Coords = addToCart1Coords,
        addToCart2Coords = addToCart2Coords,
        addToCart3Coords = addToCart3Coords,
        addToCart4Coords = addToCart4Coords,
        addToCart5Coords = addToCart5Coords,
        openCartCoords = openCartCoords,
        placeOrderCoords = placeOrderCoords
    )
}

