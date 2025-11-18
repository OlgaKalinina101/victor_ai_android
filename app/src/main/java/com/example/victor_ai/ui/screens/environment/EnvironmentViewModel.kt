package com.example.victor_ai.ui.screens.environment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.repository.HomeWiFiRepository
import com.example.victor_ai.logic.WiFiNetworkManager
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * ViewModel для экрана "Среда"
 * Управляет WiFi, определением местоположения "дома"
 */
@HiltViewModel
class EnvironmentViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "EnvironmentViewModel"
    }

    private val homeWiFiRepository = HomeWiFiRepository(application)
    private val wifiManager = WiFiNetworkManager(application)
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _state = MutableStateFlow(EnvironmentState())
    val state: StateFlow<EnvironmentState> = _state.asStateFlow()

    init {
        loadData()
    }

    /**
     * Загрузить все данные
     */
    fun loadData() {
        viewModelScope.launch {
            // Загрузить текущий WiFi
            val currentWiFi = wifiManager.getCurrentWiFi()

            // Загрузить домашний WiFi
            val homeSSID = homeWiFiRepository.getHomeSSID()
            val homeBSSID = homeWiFiRepository.getHomeBSSID()
            val homeCoords = homeWiFiRepository.getHomeCoordinates()

            // СНАЧАЛА обновляем state
            _state.value = _state.value.copy(
                currentWiFi = currentWiFi?.first,
                currentBSSID = currentWiFi?.second,
                homeWiFi = homeSSID,
                homeBSSID = homeBSSID,
                homeCoordinates = homeCoords
            )

            // ПОТОМ вызываем updateHomeStatus
            updateHomeStatus()
        }
    }

    /**
     * Сканировать доступные WiFi сети
     */
    fun scanWiFiNetworks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isScanning = true, showNetworkDropdown = true)

            wifiManager.startScan()
            // Небольшая задержка для завершения сканирования
            kotlinx.coroutines.delay(1500)

            val networks = wifiManager.getAvailableNetworks()
            _state.value = _state.value.copy(
                availableNetworks = networks,
                isScanning = false
            )
        }
    }

    /**
     * Переключить видимость выпадающего списка сетей
     */
    fun toggleNetworkDropdown() {
        if (!_state.value.showNetworkDropdown) {
            // Открываем - сканируем сети
            scanWiFiNetworks()
        } else {
            // Закрываем
            _state.value = _state.value.copy(
                showNetworkDropdown = false,
                currentPage = 0
            )
        }
    }

    /**
     * Установить домашний WiFi
     */
    fun setHomeWiFi(ssid: String, bssid: String) {
        viewModelScope.launch {
            // Получить текущие GPS координаты
            val location = getCurrentLocation()
            if (location != null) {
                homeWiFiRepository.saveHomeWiFi(
                    ssid = ssid,
                    bssid = bssid,
                    latitude = location.latitude,
                    longitude = location.longitude
                )

                // Обновить текущий WiFi сразу
                val currentWiFi = wifiManager.getCurrentWiFi()

                // Проверяем, подключены ли мы к только что выбранной сети
                val isAtHome = currentWiFi?.first == ssid && currentWiFi?.second == bssid

                _state.value = _state.value.copy(
                    homeWiFi = ssid,
                    homeBSSID = bssid,
                    homeCoordinates = Pair(location.latitude, location.longitude),
                    currentWiFi = currentWiFi?.first,
                    currentBSSID = currentWiFi?.second,
                    isAtHome = isAtHome,
                    distanceToHome = if (isAtHome) null else 0
                )
            }
        }
    }

    /**
     * Обновить статус "дома"
     */
    private suspend fun updateHomeStatus() {
        val homeSSID = _state.value.homeWiFi
        val homeBSSID = _state.value.homeBSSID
        val currentWiFi = _state.value.currentWiFi
        val currentBSSID = _state.value.currentBSSID

        // ДЕБАГ
        Log.d(TAG, "=== updateHomeStatus ===")
        Log.d(TAG, "homeSSID: $homeSSID")
        Log.d(TAG, "homeBSSID: $homeBSSID")
        Log.d(TAG, "currentWiFi: $currentWiFi")
        Log.d(TAG, "currentBSSID: $currentBSSID")

        if (homeSSID != null && homeBSSID != null) {
            // Проверяем подключение к домашнему WiFi
            val isAtHome = currentWiFi == homeSSID && currentBSSID == homeBSSID

            Log.d(TAG, "isAtHome: $isAtHome")

            if (isAtHome) {
                _state.value = _state.value.copy(
                    isAtHome = true,
                    distanceToHome = null
                )
            } else {
                // Вычисляем расстояние до дома
                val homeCoords = _state.value.homeCoordinates
                if (homeCoords != null) {
                    val currentLocation = getCurrentLocation()
                    if (currentLocation != null) {
                        val distance = calculateDistance(
                            currentLocation.latitude,
                            currentLocation.longitude,
                            homeCoords.first,
                            homeCoords.second
                        )
                        println("distance: $distance") // ДЕБАГ
                        _state.value = _state.value.copy(
                            isAtHome = false,
                            distanceToHome = distance.toInt()
                        )
                    }
                }
            }
        }
        println("=== END DEBUG ===")
    }

    /**
     * Получить текущее местоположение
     */
    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) {
            return null
        }

        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Вычислить расстояние между двумя точками (в метрах)
     */
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0 // метры
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    /**
     * Проверить разрешение на геолокацию
     */
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Перейти на следующую страницу
     */
    fun nextPage() {
        val totalPages = (_state.value.availableNetworks.size + 4) / 5 // округление вверх
        val currentPage = _state.value.currentPage
        if (currentPage < totalPages - 1) {
            _state.value = _state.value.copy(currentPage = currentPage + 1)
        }
    }

    /**
     * Перейти на предыдущую страницу
     */
    fun previousPage() {
        val currentPage = _state.value.currentPage
        if (currentPage > 0) {
            _state.value = _state.value.copy(currentPage = currentPage - 1)
        }
    }

    /**
     * Удалить домашний WiFi
     */
    fun clearHomeWiFi() {
        homeWiFiRepository.clearHomeWiFi()
        _state.value = _state.value.copy(
            homeWiFi = null,
            homeBSSID = null,
            homeCoordinates = null,
            isAtHome = false,
            distanceToHome = null
        )
    }
}

/**
 * Состояние экрана "Среда"
 */
data class EnvironmentState(
    val currentWiFi: String? = null,
    val currentBSSID: String? = null,
    val homeWiFi: String? = null,
    val homeBSSID: String? = null,
    val homeCoordinates: Pair<Double, Double>? = null,
    val availableNetworks: List<Pair<String, String>> = emptyList(),
    val isAtHome: Boolean = false,
    val distanceToHome: Int? = null, // в метрах
    val isScanning: Boolean = false,
    val showNetworkDropdown: Boolean = false,
    val currentPage: Int = 0 // текущая страница для пагинации
)
