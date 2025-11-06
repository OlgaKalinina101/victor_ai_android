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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.victor_ai.data.network.RetrofitInstance.placesApi
import com.example.victor_ai.data.repository.VisitedPlacesRepository
import com.example.victor_ai.data.repository.StatsRepository
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
import com.example.victor_ai.ui.map.utils.LocationUtils

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
    private lateinit var statsRepository: StatsRepository
    private lateinit var fusedLocationClient: FusedLocationProviderClient
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

        repository = VisitedPlacesRepository(this)
        statsRepository = StatsRepository(this, placesApi)
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
        // ✅ Используем ViewModel для сохранения state при пересоздании Activity
        val viewModel: MapViewModel = viewModel(
            factory = MapViewModelFactory(placesApi, repository, statsRepository)
        )

        // Подписываемся на state из ViewModel
        val mapBounds by viewModel.mapBounds.collectAsState()
        val pois by viewModel.pois.collectAsState()
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

        val context = LocalContext.current
        var mapView: MapCanvasView? by remember { mutableStateOf(null) }
        var mapRenderer: MapRenderer? by remember { mutableStateOf(null) }
        var isLocationUpdatesStarted by remember { mutableStateOf(false) }
        var hasInitialCentered by remember { mutableStateOf(false) }

        LaunchedEffect(searching, searchStart) {
            while (searching) {
                kotlinx.coroutines.delay(1000)
                val elapsed = ((System.currentTimeMillis() - (searchStart ?: System.currentTimeMillis())) / 1000)
                viewModel.updateElapsedTime(elapsed)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Points") },
                    navigationIcon = {
                        IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Весь контент карты и оверлеев в одном Box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Карта
                AndroidView(
                    factory = { ctx ->
                        MapCanvasView(ctx).apply {
                            mapView = this
                            mapRenderer = Canvas2DMapRenderer(this)
                            onPOIClicked = { poi -> viewModel.setSelectedPOI(poi) }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Прелоадер
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // 🧩 Наш оверлей (внутри Box → без затемнения)
                selectedPOI?.let { poi ->
                    // чтобы стрелка пользователя крутилась на POI
                    LaunchedEffect(poi) { mapView?.setSelectedPOI(poi) }

                    POIOverlay(
                        poi = poi,
                        userLocation = userLocation,
                        searching = searching,
                        elapsedSec = elapsedSec,
                        walkedMeters = walkedMeters,
                        nearby = nearby,
                        // 🆕 Проверяем статус посещения из ViewModel
                        isVisited = viewModel.isPOIVisited(poi.name),
                        visitEmotion = viewModel.getVisitEmotion(poi.name),
                        onToggleSearch = {
                            if (!searching) {
                                // старт
                                viewModel.startSearch(poi, pois, radiusM = 400, limit = 6)
                                mapView?.updatePOIs(listOf(poi) + nearby)
                                mapView?.startSearchMode()
                                // Увеличиваем зум и центрируем на пользователе
                                userLocation?.let { loc ->
                                    mapView?.zoomTo(10f) // Большой зум для детализации
                                    mapView?.panTo(loc)
                                }
                                // trail обновится автоматически через LaunchedEffect(path)
                            } else {
                                // стоп
                                viewModel.stopSearch()
                                mapView?.stopSearchMode()
                                // вернуть все POI:
                                mapView?.updatePOIs(pois)
                                mapView?.setTrail(emptyList())
                            }
                        },
                        onDismiss = {
                            viewModel.setSelectedPOI(null)
                            mapView?.setSelectedPOI(null)
                            // при закрытии — можно тоже вернуть обычный режим
                            if (searching) {
                                viewModel.stopSearch()
                                mapView?.stopSearchMode()
                                mapView?.updatePOIs(pois)
                                mapView?.setTrail(emptyList())
                            }
                        },
                        onSelectNearby = { n ->
                            // выбрать другой POI из подсказок
                            viewModel.setSelectedPOI(n)
                            if (searching) {
                                // перезапустить поиск на новом POI
                                viewModel.startSearch(n, pois, 200, 6)
                                mapView?.updatePOIs(listOf(n) + nearby)
                                // trail обновится автоматически через LaunchedEffect(path)
                            }
                        },
                        onMarkVisited = { emotion ->
                            // Отмечаем посещение в ViewModel (который сохранит в API)
                            viewModel.markPOIAsVisited(poi, emotion)
                        },
                        modifier = Modifier
                            .align(Alignment.TopCenter)   // 👈 вот это!
                            .padding(top = 30.dp)        // 👈 и отступ вниз от верхнего края
                    )
                }
            }
        }

        // Загрузка данных при старте
        LaunchedEffect(Unit) {
            val location = getCurrentLocation()
            viewModel.loadMapData(location, radiusMeters = 10000)
        }

        // Обновление карты при изменении данных из ViewModel (БЕЗ userLocation!)
        LaunchedEffect(mapBounds, pois) {
            // ✅ ИСПРАВЛЕНО: Инициализируем карту даже если POI пустой!
            if (mapBounds != null) {
                mapView?.setMapData(mapBounds!!, pois, userLocation)
                mapRenderer?.renderPOIs(pois)

                // Показываем Toast если POI не найдены
                if (pois.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MapActivity,
                            "⚠️ Не найдены места рядом. Проверьте подключение к серверу",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                // Запускаем location updates только один раз
                if (!isLocationUpdatesStarted) {
                    startLocationUpdates { newLocation, accuracy ->
                        // ✅ Используем ViewModel с фильтрацией GPS
                        val accepted = viewModel.updateUserLocation(newLocation, accuracy)
                        if (accepted) {
                            mapRenderer?.updateUserLocation(newLocation)
                        }
                    }
                    isLocationUpdatesStarted = true
                }
            }
        }

        // Обновляем userLocation на карте (без полной перерисовки)
        LaunchedEffect(userLocation, searching) {
            userLocation?.let { loc ->
                mapRenderer?.updateUserLocation(loc)

                // Центрируем при первой загрузке ИЛИ во время поиска
                if (searching) {
                    // Во время поиска постоянно следуем за пользователем
                    mapView?.panTo(loc)
                } else if (mapRenderer != null && mapBounds != null && !hasInitialCentered) {
                    // Центрируем только один раз при первой загрузке
                    mapRenderer?.centerOnPoint(loc, 5f)
                    hasInitialCentered = true
                }
            }
        }

        // Обновляем trail при изменении path
        LaunchedEffect(path) {
            if (searching) {
                mapView?.setTrail(path)
            }
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

    // ✅ Методы loadMapData, calcNearby, loadMapDataCoroutine перенесены в MapViewModel

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
