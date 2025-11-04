package com.example.victor_ai.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.victor_ai.data.network.RetrofitInstance.placesApi
import com.example.victor_ai.data.repository.VisitedPlacesRepository
import com.example.victor_ai.ui.map.canvas.MapCanvasView
import com.example.victor_ai.ui.map.renderer.Canvas2DMapRenderer
import com.example.victor_ai.ui.map.renderer.MapRenderer
import com.example.victor_ai.ui.places.*
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.cos
import android.os.Looper

/**
 * 🗺️ MapActivity - Activity для отображения карты с POI
 *
 * Функциональность:
 * - Отображение карты с маркерами
 * - Загрузка данных из Places API
 * - Обработка кликов на маркеры
 * - Сохранение посещенных мест
 */
class MapActivity : ComponentActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MapActivity::class.java))
        }
    }

    private lateinit var repository: VisitedPlacesRepository
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                loadMapData()
            }
            else -> {
                Toast.makeText(this, "Разрешение на геолокацию не предоставлено", Toast.LENGTH_SHORT).show()
                loadMapDataWithDefaultLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository = VisitedPlacesRepository(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
        var mapBounds by remember { mutableStateOf<MapBounds?>(null) }
        var pois by remember { mutableStateOf<List<POI>>(emptyList()) }
        var userLocation by remember { mutableStateOf<LatLng?>(null) }
        var selectedPOI by remember { mutableStateOf<POI?>(null) }
        var isLoading by remember { mutableStateOf(true) }

        val context = LocalContext.current
        var mapView: MapCanvasView? by remember { mutableStateOf(null) }
        var mapRenderer: MapRenderer? by remember { mutableStateOf(null) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Points") },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(Icons.Default.ArrowBack, "Назад")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Map Canvas View
                AndroidView(
                    factory = { ctx ->
                        MapCanvasView(ctx).apply {
                            mapView = this
                            mapRenderer = Canvas2DMapRenderer(this)
                            // Callback для кликов на POI
                            onPOIClicked = { poi ->
                                selectedPOI = poi
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Loading indicator
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        // Диалог с деталями POI
        selectedPOI?.let { poi ->
            // Устанавливаем выбранный POI в MapView для поворота стрелки
            LaunchedEffect(poi) {
                mapView?.setSelectedPOI(poi)
            }

            POIDetailDialog(
                poi = poi,
                userLocation = userLocation,
                onDismiss = {
                    selectedPOI = null
                    mapView?.setSelectedPOI(null) // Сбрасываем направление стрелки
                },
                onMarkAsVisited = { impression ->
                    // Сохраняем посещение
                    repository.markPlaceAsVisited(poi.id, impression)

                    // Обновляем POI в списке
                    poi.isVisited = true
                    poi.impression = impression

                    // Обновляем карту
                    mapRenderer?.renderPOIs(pois)
                }
            )
        }

        // Загружаем данные при старте
        LaunchedEffect(Unit) {
            val data = loadMapDataCoroutine()
            if (data != null) {
                mapBounds = data.bounds
                pois = data.pois
                userLocation = data.userLocation
                mapView?.setMapData(data.bounds, data.pois, data.userLocation)
                mapRenderer?.renderPOIs(data.pois)
                mapRenderer?.updateUserLocation(data.userLocation ?: LatLng(55.7558, 37.6173))
                mapRenderer?.centerOnPoint(data.userLocation ?: LatLng(55.7558, 37.6173), 5f)

                // Запускаем отслеживание позиции в real-time
                startLocationUpdates { newLocation ->
                    userLocation = newLocation
                    mapRenderer?.updateUserLocation(newLocation)
                }
            }
            isLoading = false
        }
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

    /**
     * Загружает данные карты
     */
    private fun loadMapData() {
        lifecycleScope.launch {
            try {
                val location = getCurrentLocation()
                val mapData = loadPlacesData(location, 10000)
                // Данные загружены, обновляем UI через Compose State
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MapActivity, "Ошибка загрузки данных: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Загружает данные с дефолтной локацией (Москва)
     */
    private fun loadMapDataWithDefaultLocation() {
        val defaultLocation = LatLng(55.7558, 37.6173) // Москва
        lifecycleScope.launch {
            try {
                val mapData = loadPlacesData(defaultLocation, 10000)
                // Данные загружены
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MapActivity, "Ошибка загрузки данных: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Корутина для загрузки данных карты
     */
    private suspend fun loadMapDataCoroutine(): MapData? = withContext(Dispatchers.IO) {
        try {
            val location = getCurrentLocation()
            loadPlacesData(location, 10000)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Получает текущую геолокацию
     */
    private suspend fun getCurrentLocation(): LatLng = suspendCoroutine { continuation ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Fallback на Москву
            continuation.resume(LatLng(lat = 55.7558, lon = 37.6173))
            return@suspendCoroutine
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                continuation.resume(LatLng(location.latitude, location.longitude))
            } else {
                continuation.resume(LatLng(lat = 55.7558, lon = 37.6173))
            }
        }.addOnFailureListener {
            continuation.resume(LatLng(lat = 55.7558, lon = 37.6173))
        }
    }

    /**
     * Загружает места из API
     */
    private suspend fun loadPlacesData(
        location: LatLng,
        radiusMeters: Int
    ): MapData = withContext(Dispatchers.IO) {
        val bbox = calculateBoundingBox(location.lat, location.lon, radiusMeters)

        val placesResponse = placesApi.getPlaces(
            limit = 15000,
            bbox = "${bbox.minLon},${bbox.minLat},${bbox.maxLon},${bbox.maxLat}"
        )

        val visitedPlaceIds = repository.getVisitedPlaceIds()
        val bounds = MapBounds.fromCenterAndRadius(location, radiusMeters)

        val mapData = MapDataConverter.fromBackendResponse(
            response = placesResponse,
            bounds = bounds,
            visitedPlaceIds = visitedPlaceIds
        )

        // Обновляем POI с впечатлениями
        mapData.pois.forEach { poi ->
            if (poi.isVisited) {
                poi.impression = repository.getImpression(poi.id)
                poi.visitDate = repository.getVisitDate(poi.id)
            }
        }

        mapData.copy(userLocation = location)
    }

    private data class BBox(
        val minLat: Double,
        val minLon: Double,
        val maxLat: Double,
        val maxLon: Double
    )

    private fun calculateBoundingBox(
        lat: Double,
        lon: Double,
        radiusMeters: Int
    ): BBox {
        val latDelta = radiusMeters / 111_000.0
        val lonDelta = radiusMeters / (111_000.0 * cos(Math.toRadians(lat)))

        return BBox(
            minLat = lat - latDelta,
            minLon = lon - lonDelta,
            maxLat = lat + latDelta,
            maxLon = lon + lonDelta
        )
    }

    /**
     * Запускает отслеживание позиции пользователя в real-time
     *
     * @param onLocationUpdate Callback, который вызывается при обновлении позиции
     */
    private fun startLocationUpdates(onLocationUpdate: (LatLng) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000 // Обновление каждые 5 секунд
        ).apply {
            setMinUpdateIntervalMillis(2000) // Минимальный интервал 2 секунды
            setWaitForAccurateLocation(false)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    onLocationUpdate(LatLng(location.latitude, location.longitude))
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
