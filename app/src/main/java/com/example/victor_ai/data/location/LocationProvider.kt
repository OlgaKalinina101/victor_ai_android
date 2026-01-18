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

package com.example.victor_ai.data.location

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.victor_ai.data.network.dto.GeoLocation
import com.example.victor_ai.data.repository.LocationHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Провайдер для работы с геолокацией пользователя
 * Использует нативный LocationManager для прямого контроля над источниками локации
 * Требует разрешения ACCESS_FINE_LOCATION или ACCESS_COARSE_LOCATION
 */
@Singleton
class LocationProvider @Inject constructor(
    private val context: Context,
    private val locationHistoryRepository: LocationHistoryRepository
) {

    private val _currentLocation = MutableStateFlow<GeoLocation?>(null)
    val currentLocation: StateFlow<GeoLocation?> = _currentLocation

    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val handler = Handler(Looper.getMainLooper())
    private var locationListener: LocationListener? = null

    // Настройки фильтрации
    private val MAX_ACCEPTABLE_ACCURACY_METERS = 100f
    private val MAX_LOCATION_AGE_MS = 5 * 60 * 1000L // 5 минут
    private val LOCATION_UPDATE_TIMEOUT_MS = 10_000L // 10 секунд ждём обновления

    /**
     * Запрашиваем свежую локацию.
     * Стратегия:
     * 1. Проверяем последнюю известную локацию от Network (WiFi/сотовые вышки - работает в помещении)
     * 2. Если она свежая и точная - используем её
     * 3. Если нет - подписываемся на обновления и ждём свежую локацию (с таймаутом)
     * 4. Дополнительно пробуем GPS, если доступен
     */
    fun startFetchingLocation() {
        try {
            Log.d("Geo", "=== Starting location fetch ===")
            
            // Проверяем доступные провайдеры
            val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            val hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            
            Log.d("Geo", "Available providers: Network=$hasNetwork, GPS=$hasGPS")

            // Сначала пробуем получить последнюю известную локацию
            val bestLastLocation = getBestLastKnownLocation()
            if (bestLastLocation != null) {
                Log.d("Geo", "Using cached location (fresh enough)")
                updateLocation(bestLastLocation)
            }

            // Затем запрашиваем свежие обновления
            requestLocationUpdates()

        } catch (e: SecurityException) {
            Log.e("Geo", "Location permission missing", e)
        }
    }

    /**
     * Получаем лучшую из последних известных локаций
     */
    private fun getBestLastKnownLocation(): Location? {
        try {
            val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            Log.d("Geo", "Network last: ${formatLocation(networkLocation)}")
            Log.d("Geo", "GPS last: ${formatLocation(gpsLocation)}")

            // Выбираем лучшую локацию
            val candidates = listOfNotNull(networkLocation, gpsLocation)
                .filter { isLocationValid(it) }
                .sortedWith(compareByDescending<Location> { isFresh(it) }
                    .thenBy { it.accuracy })

            return candidates.firstOrNull()
        } catch (e: SecurityException) {
            Log.e("Geo", "No permission for last known location", e)
            return null
        }
    }

    /**
     * Подписываемся на обновления локации от доступных провайдеров
     */
    private fun requestLocationUpdates() {
        try {
            // Отписываемся от предыдущих обновлений
            stopLocationUpdates()

            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.d("Geo", "New location from ${location.provider}: ${formatLocation(location)}")
                    
                    if (isLocationValid(location) && isBetterThanCurrent(location)) {
                        updateLocation(location)
                        // Если получили точную локацию - можем остановить обновления
                        if (location.accuracy < 50f) {
                            Log.d("Geo", "Got accurate location, stopping updates")
                            stopLocationUpdates()
                        }
                    }
                }

                override fun onProviderEnabled(provider: String) {
                    Log.d("Geo", "Provider enabled: $provider")
                }

                override fun onProviderDisabled(provider: String) {
                    Log.d("Geo", "Provider disabled: $provider")
                }

                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    // Не используется в современных версиях Android
                }
            }

            // Запрашиваем обновления от Network провайдера (приоритет - работает в помещении!)
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.d("Geo", "Requesting updates from NETWORK provider")
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L, // минимальное время между обновлениями
                    0f, // минимальная дистанция
                    locationListener!!,
                    Looper.getMainLooper()
                )
            }

            // Дополнительно запрашиваем от GPS (если доступен)
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("Geo", "Requesting updates from GPS provider")
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    locationListener!!,
                    Looper.getMainLooper()
                )
            }

            // Таймаут: если за 10 секунд не получили локацию - останавливаем обновления
            handler.postDelayed({
                Log.d("Geo", "Location update timeout, stopping")
                stopLocationUpdates()
            }, LOCATION_UPDATE_TIMEOUT_MS)

        } catch (e: SecurityException) {
            Log.e("Geo", "No permission for location updates", e)
        }
    }

    /**
     * Останавливаем обновления локации
     */
    private fun stopLocationUpdates() {
        locationListener?.let {
            try {
                locationManager.removeUpdates(it)
                Log.d("Geo", "Location updates stopped")
            } catch (e: Exception) {
                Log.e("Geo", "Error stopping location updates", e)
            }
        }
        locationListener = null
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * Проверяем, валидна ли локация
     */
    private fun isLocationValid(location: Location): Boolean {
        val acc = location.accuracy
        val isFresh = isFresh(location)
        val isAccurate = acc in 1f..MAX_ACCEPTABLE_ACCURACY_METERS

        if (!isFresh) {
            Log.d("Geo", "Location too old: age=${getLocationAge(location)}ms")
        }
        if (!isAccurate) {
            Log.d("Geo", "Location inaccurate: acc=$acc")
        }

        return isFresh && isAccurate
    }

    /**
     * Проверяем, свежая ли локация (не старше 5 минут)
     */
    private fun isFresh(location: Location): Boolean {
        return getLocationAge(location) < MAX_LOCATION_AGE_MS
    }

    /**
     * Возвращает возраст локации в миллисекундах
     */
    private fun getLocationAge(location: Location): Long {
        return System.currentTimeMillis() - location.time
    }

    /**
     * Проверяем, лучше ли новая локация текущей
     */
    private fun isBetterThanCurrent(newLocation: Location): Boolean {
        val current = _currentLocation.value ?: return true

        // Если новая локация точнее - берём её
        return newLocation.accuracy < MAX_ACCEPTABLE_ACCURACY_METERS
    }

    /**
     * Обновляем текущую локацию с защитой от аномальных скачков
     */
    private fun updateLocation(location: Location) {
        val timestamp = System.currentTimeMillis()
        val age = getLocationAge(location) / 1000 // в секундах
        
        // Валидация и сохранение через репозиторий
        val validatedLocation = locationHistoryRepository.validateAndSave(
            lat = location.latitude,
            lon = location.longitude,
            timestamp = timestamp,
            accuracy = location.accuracy,
            source = location.provider ?: "unknown",
            isManual = false
        )
        
        // Обновляем текущую локацию
        _currentLocation.value = GeoLocation(lat = validatedLocation.lat, lon = validatedLocation.lon)
        
        Log.d("Geo", "✓ Location updated: ${validatedLocation.lat}, ${validatedLocation.lon} (acc=${location.accuracy}m, age=${age}s)")
    }

    /**
     * Ручное добавление локации (например, "дом" или "по адресу")
     */
    fun setManualLocation(lat: Double, lon: Double, source: String = "manual") {
        // Сохраняем через репозиторий с флагом isManual
        val trackedLocation = locationHistoryRepository.validateAndSave(
            lat = lat,
            lon = lon,
            accuracy = null,
            source = source,
            isManual = true
        )
        
        _currentLocation.value = GeoLocation(lat = trackedLocation.lat, lon = trackedLocation.lon)
        
        Log.d("Geo", "✓ Manual location set: $lat, $lon (source=$source)")
    }

    /**
     * Форматирование локации для логов
     */
    private fun formatLocation(location: Location?): String {
        if (location == null) return "null"
        val age = getLocationAge(location) / 1000
        return "lat=${location.latitude}, lon=${location.longitude}, acc=${location.accuracy}m, age=${age}s"
    }

    fun getCurrentGeo(): GeoLocation? = _currentLocation.value

    /**
     * Получить историю локаций через репозиторий
     */
    fun getLocationHistory() = locationHistoryRepository.getHistory()

    /**
     * Получить статистику по истории локаций
     */
    fun getLocationStatistics() = locationHistoryRepository.getStatistics()

    /**
     * Очистка ресурсов
     */
    fun cleanup() {
        stopLocationUpdates()
        locationHistoryRepository.clear()
        Log.d("Geo", "Location provider cleaned up")
    }
}

