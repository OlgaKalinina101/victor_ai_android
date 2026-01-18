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
import com.example.victor_ai.data.network.LocationDetail
import com.example.victor_ai.data.network.LocationListItem
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.ui.map.models.PlacesResponse
import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 📍 Репозиторий для работы с сохранёнными локациями
 * 
 * Предоставляет методы для:
 * - Получения списка локаций пользователя
 * - Получения деталей конкретной локации
 * - Загрузки OSM данных для локации
 */
@Singleton
class LocationsRepository @Inject constructor(
    private val placesApi: PlacesApi
) {
    
    companion object {
        private const val TAG = "LocationsRepository"
        private const val MAX_RETRIES = 3
        private const val INITIAL_RETRY_DELAY_MS = 500L
    }

    private fun isRetriableCode(code: Int): Boolean {
        return code == 408 || code == 429 || code == 500 || code == 502 || code == 503 || code == 504
    }

    private suspend fun <T> requestWithRetry(
        operation: String,
        block: suspend () -> T
    ): T? {
        var delayMs = INITIAL_RETRY_DELAY_MS
        repeat(MAX_RETRIES) { attempt ->
            try {
                return block()
            } catch (e: HttpException) {
                val code = e.code()
                Log.e(TAG, "❌ $operation HTTP ${e.code()} (attempt ${attempt + 1}/$MAX_RETRIES)")
                if (!isRetriableCode(code) || attempt == MAX_RETRIES - 1) {
                    return null
                }
            } catch (e: IOException) {
                Log.e(TAG, "❌ $operation network error (attempt ${attempt + 1}/$MAX_RETRIES)", e)
                if (attempt == MAX_RETRIES - 1) return null
            } catch (e: Exception) {
                Log.e(TAG, "❌ $operation error (attempt ${attempt + 1}/$MAX_RETRIES)", e)
                if (attempt == MAX_RETRIES - 1) return null
            }

            delay(delayMs)
            delayMs = (delayMs * 2).coerceAtMost(2_000L)
        }
        return null
    }

    private suspend fun <T> responseWithRetry(
        operation: String,
        block: suspend () -> Response<T>
    ): Response<T>? {
        var delayMs = INITIAL_RETRY_DELAY_MS
        repeat(MAX_RETRIES) { attempt ->
            try {
                val response = block()
                if (response.isSuccessful) return response

                val code = response.code()
                Log.e(TAG, "❌ $operation HTTP $code (attempt ${attempt + 1}/$MAX_RETRIES)")
                if (!isRetriableCode(code) || attempt == MAX_RETRIES - 1) {
                    return response
                }
            } catch (e: IOException) {
                Log.e(TAG, "❌ $operation network error (attempt ${attempt + 1}/$MAX_RETRIES)", e)
                if (attempt == MAX_RETRIES - 1) return null
            } catch (e: Exception) {
                Log.e(TAG, "❌ $operation error (attempt ${attempt + 1}/$MAX_RETRIES)", e)
                if (attempt == MAX_RETRIES - 1) return null
            }

            delay(delayMs)
            delayMs = (delayMs * 2).coerceAtMost(2_000L)
        }
        return null
    }
    
    /**
     * Получает список всех сохранённых локаций пользователя
     * 
     * @return List<LocationListItem> или null при ошибке
     */
    suspend fun getLocations(): List<LocationListItem>? {
        val response = responseWithRetry("getLocations") {
            placesApi.getLocations(UserProvider.getCurrentUserId())
        } ?: return null

        return if (response.isSuccessful) {
            val locations = response.body() ?: emptyList()
            Log.d(TAG, "✅ Загружено локаций: ${locations.size}")
            locations
        } else {
            Log.e(TAG, "❌ Ошибка загрузки локаций: ${response.code()}")
            null
        }
    }
    
    /**
     * Получает только активные локации
     */
    suspend fun getActiveLocations(): List<LocationListItem>? {
        return getLocations()?.filter { it.is_active }
    }
    
    /**
     * Получает полную информацию о конкретной локации
     * 
     * @param locationId ID локации
     * @return LocationDetail или null при ошибке
     */
    suspend fun getLocationDetail(locationId: Int): LocationDetail? {
        val response = responseWithRetry("getLocationDetail($locationId)") {
            placesApi.getLocationDetail(
                locationId = locationId,
                accountId = UserProvider.getCurrentUserId()
            )
        } ?: return null

        return if (response.isSuccessful) {
            val location = response.body()
            Log.d(TAG, "✅ Загружена локация: ${location?.name}")
            location
        } else {
            when (response.code()) {
                404 -> Log.e(TAG, "❌ Локация не найдена: $locationId")
                403 -> Log.e(TAG, "❌ Доступ запрещён к локации: $locationId")
                else -> Log.e(TAG, "❌ Ошибка загрузки локации: ${response.code()}")
            }
            null
        }
    }
    
    /**
     * Получает OSM данные для сохранённой локации
     * Возвращает места в том же формате что и getPlaces
     * 
     * @param locationId ID локации
     * @param limit Максимум элементов (по умолчанию 15000)
     * @param offset Смещение для пагинации
     * @return PlacesResponse или null при ошибке
     */
    suspend fun getLocationPlaces(
        locationId: Int,
        limit: Int = 15000,
        offset: Int = 0
    ): PlacesResponse? {
        return requestWithRetry("getLocationPlaces($locationId)") {
            val places = placesApi.getLocationPlaces(
                locationId = locationId,
                accountId = UserProvider.getCurrentUserId(),
                limit = limit,
                offset = offset
            )
            Log.d(TAG, "✅ Загружено мест для локации '${places.location}': ${places.count} элементов")
            places
        }
    }
    
    /**
     * Проверяет существование локации
     */
    suspend fun locationExists(locationId: Int): Boolean {
        return getLocationDetail(locationId) != null
    }
    
    /**
     * Получает bbox локации в виде удобного объекта
     */
    suspend fun getLocationBounds(locationId: Int): LocationBounds? {
        val location = getLocationDetail(locationId) ?: return null
        return LocationBounds(
            south = location.bbox_south,
            west = location.bbox_west,
            north = location.bbox_north,
            east = location.bbox_east
        )
    }
}

/**
 * Вспомогательный класс для bbox локации
 */
data class LocationBounds(
    val south: Double,
    val west: Double,
    val north: Double,
    val east: Double
) {
    /**
     * Вычисляет центр bbox
     */
    fun center(): Pair<Double, Double> {
        val lat = (south + north) / 2.0
        val lon = (west + east) / 2.0
        return Pair(lat, lon)
    }
    
    /**
     * Вычисляет примерный радиус в метрах
     */
    fun approximateRadius(): Double {
        val latDiff = north - south
        val lonDiff = east - west
        val avgDiff = (latDiff + lonDiff) / 2.0
        return avgDiff * 111000.0 / 2.0 // 1 градус ≈ 111км
    }
}

