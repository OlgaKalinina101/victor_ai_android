package com.example.victor_ai.data.location

import android.content.Context
import android.util.Log
import com.example.victor_ai.data.network.dto.GeoLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Провайдер для работы с геолокацией пользователя
 * Использует Google Play Services для получения последней известной позиции
 */
@Singleton
class LocationProvider @Inject constructor(
    private val context: Context
) {

    private val _currentLocation = MutableStateFlow<GeoLocation?>(null)
    val currentLocation: StateFlow<GeoLocation?> = _currentLocation

    /**
     * Запуск получения геолокации
     * Требует разрешения ACCESS_FINE_LOCATION или ACCESS_COARSE_LOCATION
     */
    fun startFetchingLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        Log.d("Geo", "Location: $lat, $lon")

                        val geo = GeoLocation(lat = lat, lon = lon)
                        _currentLocation.value = geo
                    } else {
                        Log.w("Geo", "Location is null (disabled or no fix yet)")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Geo", "Failed to get location", e)
                }

        } catch (e: SecurityException) {
            Log.e("Geo", "Location permission missing", e)
        }
    }

    /**
     * Получение текущей геолокации (может быть null)
     */
    fun getCurrentGeo(): GeoLocation? = _currentLocation.value
}
