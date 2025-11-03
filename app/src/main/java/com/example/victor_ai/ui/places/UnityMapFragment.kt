package com.example.victor_ai.ui.places

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.victor_ai.data.network.RetrofitInstance.placesApi
import com.google.android.gms.location.LocationServices
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.cos

/**
 * 🗺️ Fragment с Unity картой
 *
 * Этот Fragment встраивает Unity карту в Android приложение
 * и управляет передачей данных между Android и Unity
 */
class UnityMapFragment : Fragment() {

    private var unityPlayer: UnityPlayer? = null
    private lateinit var unityContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Создаем контейнер для Unity
        unityContainer = FrameLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        return unityContainer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем Unity Player
        initializeUnity()

        // Настраиваем обработчики событий из Unity
        setupUnityEventHandlers()
        // Фоллбэк: если Unity не ответит за 3 секунды, загрузим принудительно
        lifecycleScope.launch {
            delay(3000)
            if (unityPlayer != null) {
                Log.d("UnityMap", "⏰ Таймаут ожидания Unity, загружаем данные принудительно")
                loadAndSendMapData()
            }
        }
    }

    /**
     * Инициализирует Unity Player
     */
    private fun initializeUnity() {
        try {
            // Создаем UnityPlayer
            unityPlayer = UnityPlayer(requireActivity()).apply {
                // Добавляем Unity view в контейнер
                unityContainer.addView(this)
            }

            // Запускаем Unity
            unityPlayer?.requestFocus()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Ошибка запуска Unity: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Настраивает обработчики событий из Unity
     */
    private fun setupUnityEventHandlers() {
        // Обработчик клика на POI
        UnityBridge.onPOIClicked = { poiId, name, type ->
            lifecycleScope.launch {
                // Можно открыть детальный экран или показать диалог
                Toast.makeText(context, "Clicked: $name ($type)", Toast.LENGTH_SHORT).show()

                // Пример: открыть детальный экран
                // findNavController().navigate(
                //     UnityMapFragmentDirections.actionToPoiDetails(poiId)
                // )
            }
        }

        // Обработчик посещения места
        UnityBridge.onPlaceVisited = { poiId, impression, timestamp ->
            lifecycleScope.launch {
                // Сохраняем в базу данных
                savePlaceVisitToDatabase(poiId, impression, timestamp)

                Toast.makeText(context, "Место отмечено как посещенное! ✓", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик обновления впечатления
        UnityBridge.onImpressionUpdated = { poiId, impression ->
            lifecycleScope.launch {
                // Обновляем в базе данных
                updateImpressionInDatabase(poiId, impression)
            }
        }

        // Обработчик готовности карты
        UnityBridge.onMapReady = {
            lifecycleScope.launch {
                // Карта готова, можно отправить данные
                loadAndSendMapData()
            }
        }
    }

    /**
     * Загружает OSM данные и отправляет в Unity
     */
    private fun loadAndSendMapData() {
        lifecycleScope.launch {
            try {
                Log.d("UnityMap", "🔄 Начало загрузки")

                val location = getCurrentLocation()
                Log.d("UnityMap", "📍 Локация: ${location.lat}, ${location.lon}")

                val osmData = loadPlacesFromBackend(
                    latitude = location.lat,
                    longitude = location.lon,
                    radiusMeters = 10000
                )
                Log.d("UnityMap", "✅ Загружено items: ${osmData.items.size}")

                val visitedPlaceIds = getVisitedPlacesFromDatabase()

                val bounds = MapBounds.fromCenterAndRadius(location, 10000)

                val mapData = MapDataConverter.fromBackendResponse(
                    response = osmData,
                    bounds = bounds,
                    visitedPlaceIds = visitedPlaceIds
                )
                Log.d("UnityMap", "✅ Создано POI: ${mapData.pois.size}")
                Log.d("UnityMap", "✅ Bounds: ${mapData.bounds}")

                // КРИТИЧНО
                UnityBridge.sendMapData(mapData)
                Log.d("UnityMap", "📤 Отправлено в Unity")

                UnityBridge.updateUserLocation(location)

            } catch (e: Exception) {
                Log.e("UnityMap", "❌ Ошибка загрузки", e)
                e.printStackTrace()
                Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    /**
     * Получает реальную геолокацию пользователя
     */
    private suspend fun getCurrentLocation(): LatLng = suspendCoroutine { continuation ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Проверка разрешений
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
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
                // Fallback если геолокация недоступна
                continuation.resume(LatLng(lat = 55.7558, lon = 37.6173))
            }
        }.addOnFailureListener {
            continuation.resume(LatLng(lat = 55.7558, lon = 37.6173))
        }
    }

    /**
     * Загружает места из собственного бэкенда
     */
    private suspend fun loadPlacesFromBackend(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int
    ): PlacesResponse = withContext(Dispatchers.IO) {
        // Вычисляем bbox вокруг точки
        val bbox = calculateBoundingBox(latitude, longitude, radiusMeters)

        // Запрос к бэкенду
        placesApi.getPlaces(
            limit = 15000,
            bbox = "${bbox.minLon},${bbox.minLat},${bbox.maxLon},${bbox.maxLat}"
        )
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
        // Примерное вычисление (1° ≈ 111км)
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
     * Получает ID посещенных мест из базы данных
     * TODO: Реализовать через Room Database
     */
    private suspend fun getVisitedPlacesFromDatabase(): Set<String> {
        // Заглушка
        return emptySet()

        /*
        // Реальная реализация:
        return database.visitedPlacesDao().getAllVisitedIds()
        */
    }

    /**
     * Сохраняет посещенное место в базу данных
     */
    private suspend fun savePlaceVisitToDatabase(poiId: String, impression: String, timestamp: Long) {
        // TODO: Реализовать сохранение в Room Database
        /*
        val visit = VisitedPlace(
            poiId = poiId,
            impression = impression,
            visitDate = timestamp
        )
        database.visitedPlacesDao().insert(visit)
        */
    }

    /**
     * Обновляет впечатление в базе данных
     */
    private suspend fun updateImpressionInDatabase(poiId: String, impression: String) {
        // TODO: Реализовать обновление в Room Database
        /*
        database.visitedPlacesDao().updateImpression(poiId, impression)
        */
    }

    // ════════════════════════════════════════════════════════════
    // 🔄 LIFECYCLE МЕТОДЫ
    // ════════════════════════════════════════════════════════════

    override fun onResume() {
        super.onResume()
        unityPlayer?.resume()
    }

    override fun onPause() {
        super.onPause()
        unityPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Очищаем Unity Player
        unityPlayer?.destroy()
        unityPlayer = null

        // Очищаем callback'и
        UnityBridge.cleanup()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        unityPlayer?.lowMemory()
    }
}