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

package com.example.victor_ai.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.victor_ai.data.location.LocationProvider
import com.example.victor_ai.data.repository.HomeWiFiRepository
import com.example.victor_ai.ui.map.canvas.MapCanvasView
import com.example.victor_ai.ui.map.renderer.MapRenderer
import com.example.victor_ai.ui.map.models.*
import com.google.android.gms.location.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.os.Looper
import dagger.hilt.android.AndroidEntryPoint

/**
 * 🗺️ MapActivity - Activity для отображения карты с POI
 *
 * Функциональность:
 * - Отображение карты с маркерами
 * - Загрузка данных из Places API
 * - Обработка кликов на маркеры
 * - Сохранение посещенных мест
 */
@AndroidEntryPoint
class MapActivity : ComponentActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MapActivity::class.java))
        }
    }

    // 📍 LocationProvider через Hilt
    @javax.inject.Inject
    lateinit var locationProvider: LocationProvider

    // 🌐 PlacesApi через Hilt (для MapContent caption generation)
    @javax.inject.Inject
    lateinit var placesApi: com.example.victor_ai.data.network.PlacesApi
    
    // 📍 Геолокация через единый FusedLocationClient
    private val fusedLocationClient by lazy { 
        LocationServices.getFusedLocationProviderClient(this)
    }
    private var locationCallback: LocationCallback? = null

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                // Загрузка данных происходит в LaunchedEffect в MapScreen
            }
            else -> {
                Toast.makeText(this, "Разрешение на геолокацию не предоставлено", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MapScreen()
            }
        }

        // Запрашиваем разрешение на геолокацию
        requestLocationPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Останавливаем отслеживание позиции при уничтожении Activity
        stopLocationUpdates()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MapScreen() {
        // ✅ Используем HiltViewModel для сохранения state при пересоздании Activity
        val viewModel: MapViewModel = hiltViewModel()

        // Подписываемся на state из ViewModel
        val mapBounds by viewModel.mapBounds.collectAsState()
        val pois by viewModel.pois.collectAsState()
        val backgroundElements by viewModel.backgroundElements.collectAsState()
        val userLocation by viewModel.userLocation.collectAsState()
        val selectedPOI by viewModel.selectedPOI.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val searching by viewModel.searching.collectAsState()
        val searchStart by viewModel.searchStart.collectAsState()
        val elapsedSec by viewModel.elapsedSec.collectAsState()
        val walkedMeters by viewModel.walkedMeters.collectAsState()
        val path by viewModel.path.collectAsState()
        val nearby by viewModel.nearby.collectAsState()
        val visitedPOIs by viewModel.visitedPOIs.collectAsState()
        val unlockedAchievements by viewModel.unlockedAchievements.collectAsState()

        val context = LocalContext.current
        var mapView: MapCanvasView? by remember { mutableStateOf(null) }
        var mapRenderer: MapRenderer? by remember { mutableStateOf(null) }
        var isLocationUpdatesStarted by remember { mutableStateOf(false) }
        var hasInitialCentered by remember { mutableStateOf(false) }
        var lastLoadedCenter by remember { mutableStateOf<LatLng?>(null) }
        
        // Состояния для диалогов сброса локации
        var showLocationAnomalyDialog by remember { mutableStateOf(false) }
        var showAddressInputDialog by remember { mutableStateOf(false) }
        val homeWiFiRepository = remember { HomeWiFiRepository(context) }

        // Подсчет статистики для заголовка
        val visitedCount = pois.count { it.isVisited }
        val totalCount = pois.size
        
        // 📍 Локации
        val availableLocations by viewModel.availableLocations.collectAsState()
        val currentLocationName by viewModel.currentLocationName.collectAsState()
        val currentLocationId by viewModel.currentLocationId.collectAsState()
        val isGPSMode by viewModel.isGPSMode.collectAsState()
        
        // Ошибки
        val error by viewModel.error.collectAsState()
        
        // 🎨 Используем новые композитные компоненты для UI
        Scaffold(
            topBar = {
                com.example.victor_ai.ui.map.composables.MapTopBar(
                    visitedCount = visitedCount,
                    totalCount = totalCount,
                    isGPSMode = isGPSMode,
                    currentLocationName = currentLocationName,
                    currentLocationId = currentLocationId,
                    availableLocations = availableLocations,
                    onBackPressed = { (context as? ComponentActivity)?.finish() },
                    onLocationMenuItemClick = { locationId ->
                        if (locationId == null) {
                            // Возврат к GPS режиму
                            Log.d("MapActivity", "🔙 Возврат к GPS режиму")
                            mapView?.resetZoom()
                            viewModel.returnToGPSMode()
                        } else {
                            // Переключение на сохранённую локацию
                            Log.d("MapActivity", "🗺️ Переключаемся на локацию #$locationId")
                            mapView?.resetZoom()
                            viewModel.loadMapForLocation(locationId)
                        }
                    },
                    onDeleteLocation = { locationId ->
                        Log.d("MapActivity", "🗑️ Удаляем локацию #$locationId")
                        viewModel.deleteLocation(
                            locationId = locationId,
                            onSuccess = { message ->
                                Toast.makeText(context, "✅ $message", Toast.LENGTH_SHORT).show()
                            },
                            onError = { error ->
                                Toast.makeText(context, "❌ Ошибка: $error", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    onRefreshLocation = { showLocationAnomalyDialog = true }
                )
            }
        ) { paddingValues ->
            // 🗺️ Основной контент карты
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                com.example.victor_ai.ui.map.composables.MapContent(
                    isLoading = isLoading,
                    error = error,
                    selectedPOI = selectedPOI,
                    userLocation = userLocation,
                    searching = searching,
                    elapsedSec = elapsedSec,
                    walkedMeters = walkedMeters,
                    nearby = nearby,
                    visitedPOIs = visitedPOIs,
                    pois = pois,
                    mapView = mapView,
                    onMapViewCreated = { mapView = it },
                    onMapRendererCreated = { mapRenderer = it },
                    onPOIClicked = { poi -> viewModel.setSelectedPOI(poi) },
                    onToggleSearch = {
                        val currentPOI = selectedPOI
                        if (!searching && currentPOI != null) {
                            // Старт поиска
                            Log.d("MapActivity", "🚀 СТАРТ поиска для ${currentPOI.name}")
                            // 💬 При старте поиска "комикс-облачко" должно исчезнуть
                            mapView?.setSpeechBubbleText(null)
                            viewModel.startSearch(currentPOI, pois, radiusM = 400, limit = 6)
                            mapView?.setSelectedPOI(currentPOI)
                            mapView?.startSearchMode()
                            userLocation?.let { loc ->
                                mapView?.zoomToIncludeBoth(loc, currentPOI.location, paddingFactor = 0.4f)
                            }
                        } else {
                            // Стоп поиска
                            Log.d("MapActivity", "🛑 СТОП поиска")
                            viewModel.stopSearch()
                            mapView?.setTrail(emptyList())
                            mapView?.stopSearchMode()
                        }
                    },
                    onDismissOverlay = {
                        viewModel.setSelectedPOI(null)
                        mapView?.setSelectedPOI(null)
                        if (searching) {
                            viewModel.stopSearch()
                            mapView?.setTrail(emptyList())
                            mapView?.stopSearchMode()
                        }
                    },
                    onSelectNearby = { nearbyPOI ->
                        viewModel.setSelectedPOI(nearbyPOI)
                        if (searching) {
                            viewModel.startSearch(nearbyPOI, pois, 200, 6)
                            mapView?.updatePOIs(listOf(nearbyPOI) + nearby)
                        }
                    },
                    onMarkVisited = { emotion ->
                        val currentPOI = selectedPOI
                        currentPOI?.let { poi ->
                            viewModel.markPOIAsVisited(poi, emotion)
                        }
                    },
                    onMarkFound = { poi ->
                        viewModel.markPOIAsFound(poi)
                    },
                    placesApi = placesApi
                )
            }
        }

        // ⏱️ Все эффекты вынесены в отдельные композиты
        com.example.victor_ai.ui.map.composables.SearchTimerEffect(
            searching = searching,
            searchStart = searchStart,
            onElapsedUpdate = { viewModel.updateElapsedTime(it) }
        )
        
        com.example.victor_ai.ui.map.composables.LoadLocationsEffect(viewModel)
        
        com.example.victor_ai.ui.map.composables.ShowErrorEffect(context, error)
        
        com.example.victor_ai.ui.map.composables.LoadMapDataEffect(
            getCurrentLocation = { getCurrentLocation() },
            onDataLoaded = { location ->
                viewModel.loadMapData(location, radiusMeters = 2000)
                lastLoadedCenter = location
            }
        )
        
        com.example.victor_ai.ui.map.composables.AutoReloadEffect(
            userLocation = userLocation,
            lastLoadedCenter = lastLoadedCenter,
            searching = searching,
            pois = pois,
            onReload = { location ->
                viewModel.loadMapData(location, radiusMeters = 2000)
                lastLoadedCenter = location
            }
        )
        
        com.example.victor_ai.ui.map.composables.UpdatePOIsEffect(
            searching = searching,
            selectedPOI = selectedPOI,
            nearby = nearby,
            pois = pois,
            mapView = mapView
        )
        
        com.example.victor_ai.ui.map.composables.InitMapEffect(
            context = this,
            mapBounds = mapBounds,
            pois = pois,
            backgroundElements = backgroundElements,
            userLocation = userLocation,
            isLocationUpdatesStarted = isLocationUpdatesStarted,
            mapView = mapView,
            mapRenderer = mapRenderer,
            onStartLocationUpdates = {
                startLocationUpdates { newLocation, accuracy ->
                    val accepted = viewModel.updateUserLocation(newLocation, accuracy)
                    if (accepted) {
                        mapRenderer?.updateUserLocation(newLocation)
                    }
                }
                isLocationUpdatesStarted = true
            }
        )
        
        com.example.victor_ai.ui.map.composables.UpdateUserLocationEffect(
            userLocation = userLocation,
            searching = searching,
            mapRenderer = mapRenderer,
            mapBounds = mapBounds,
            hasInitialCentered = hasInitialCentered,
            onInitialCentered = { hasInitialCentered = true }
        )
        
        com.example.victor_ai.ui.map.composables.UpdateTrailEffect(
            searching = searching,
            path = path,
            mapView = mapView
        )
        
        // 🗨️ Диалоги
        com.example.victor_ai.ui.map.composables.MapDialogs(
            context = context,
            showLocationAnomalyDialog = showLocationAnomalyDialog,
            showAddressInputDialog = showAddressInputDialog,
            unlockedAchievements = unlockedAchievements,
            homeWiFiRepository = homeWiFiRepository,
            locationProvider = locationProvider,
            onDismissLocationAnomaly = { showLocationAnomalyDialog = false },
            onDismissAddressInput = { showAddressInputDialog = false },
            onShowAddressInput = { showAddressInputDialog = true },
            onLocationUpdated = { viewModel.updateUserLocation(it) },
            onDismissAchievements = { viewModel.clearUnlockedAchievements() }
        )
    }


    /**
     * Запрашивает разрешение на геолокацию
     */
    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // ✅ Методы loadMapData, calcNearby, loadMapDataCoroutine перенесены в MapViewModel

    /**
     * Получает текущую геолокацию
     */
    private suspend fun getCurrentLocation(): LatLng = suspendCoroutine { continuation ->
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("MapActivity", "⚠️ Разрешения на геолокацию нет. Используем fallback.")
            Toast.makeText(this, "⚠️ Разрешите доступ к геолокации", Toast.LENGTH_LONG).show()
            continuation.resume(LatLng(lat = 55.7558, lon = 37.6173))
            return@suspendCoroutine
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("MapActivity", "✅ Получена lastLocation: ${location.latitude}, ${location.longitude}")
                    continuation.resume(LatLng(location.latitude, location.longitude))
                } else {
                    Log.w("MapActivity", "⚠️ lastLocation = null. Используем fallback.")
                    Toast.makeText(this, "⚠️ Не удалось получить локацию. Проверьте GPS.", Toast.LENGTH_LONG).show()
                    continuation.resume(LatLng(lat = 55.7558, lon = 37.6173))
                }
            }
            .addOnFailureListener { e ->
                Log.e("MapActivity", "❌ Ошибка получения геолокации", e)
                Toast.makeText(this, "❌ Ошибка GPS: ${e.message}", Toast.LENGTH_LONG).show()
                continuation.resume(LatLng(lat = 55.7558, lon = 37.6173))
            }
    }

    // ✅ Методы loadPlacesData, calculateBoundingBox перенесены в MapViewModel

    /**
     * Запускает отслеживание позиции пользователя в real-time
     *
     * @param onLocationUpdate Callback, который вызывается при обновлении позиции (location, accuracy)
     */
    private fun startLocationUpdates(onLocationUpdate: (LatLng, Float) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("MapActivity", "⚠️ Разрешения на геолокацию нет для location updates")
            return
        }
        
        Log.d("MapActivity", "🚀 Запускаем location updates")

        // 🔥 Улучшенный LocationRequest для стабильного GPS
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3000 // Обновление каждые 3 секунды (чаще для лучшего отклика)
        ).apply {
            setMinUpdateIntervalMillis(1000) // Минимальный интервал 1 секунда
            setWaitForAccurateLocation(true) // ✅ ЖДЕМ точных координат!
            setMaxUpdateDelayMillis(5000) // Максимальная задержка batch обновлений
            setMinUpdateDistanceMeters(2f) // Минимальное смещение 2 метра
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d("MapActivity", "📍 Location update: lat=${location.latitude}, lon=${location.longitude}, accuracy=${location.accuracy}m")
                    // ✅ Передаём accuracy для фильтрации
                    onLocationUpdate(
                        LatLng(location.latitude, location.longitude),
                        location.accuracy
                    )
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    /**
     * Останавливает отслеживание позиции пользователя
     */
    private fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }
}
