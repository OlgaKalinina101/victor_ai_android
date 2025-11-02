package com.example.victor_ai.ui.places

import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.victor_ai.data.network.dto.GeoLocation
import com.unity3d.player.UnityPlayer

/**
 * 🗺️ Экран Places с Unity картой
 *
 * Отображает статистику мест сверху и Unity карту снизу
 */
@Composable
fun PlacesMenu(
    onBack: () -> Unit,
    viewModel: PlacesViewModel,
    unityPlayer: UnityPlayer? = null
) {
    val places by viewModel.places
    val loading by viewModel.loading
    val error by viewModel.error
    var showFullMap by remember { mutableStateOf(false) }
    var latestGeo by remember { mutableStateOf<GeoLocation?>(null) }

    // Загружаем места при первом открытии
    LaunchedEffect(Unit) {
        latestGeo = GeoLocation(lat = 55.8445, lon = 37.3581)
        viewModel.loadPlacesAround(
            latitude = latestGeo!!.lat,
            longitude = latestGeo!!.lon,
            radiusMeters = 1000
        )
    }

    // Настраиваем обработчики Unity событий
    DisposableEffect(Unit) {
        setupUnityHandlers(viewModel)
        onDispose {
            UnityBridge.cleanup()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // === Верх: Статистика ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .background(Color.Transparent)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    loading -> CircularProgressIndicator()
                    error != null -> Text("Ошибка: $error", color = Color.Red)
                    places.isNotEmpty() -> Text(
                        text = "Найдено мест: ${places.size}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFE0E0E0)
                    )
                    else -> Text("Нет данных", color = Color.Gray)
                }
            }

            // === Низ: Unity карта (мини) ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showFullMap = true }
            ) {
                UnityMapView(
                    places = places,
                    userLocation = latestGeo,
                    unityPlayer = unityPlayer,
                    isFullScreen = false,
                    modifier = Modifier.fillMaxSize()
                )

                if (!loading && places.isNotEmpty()) {
                    Text(
                        "Нажми, чтобы открыть",
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // === Полноэкранная карта ===
        if (showFullMap) {
            Dialog(onDismissRequest = { showFullMap = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.9f))
                ) {
                    UnityMapView(
                        places = places,
                        userLocation = latestGeo,
                        unityPlayer = unityPlayer,
                        isFullScreen = true,
                        modifier = Modifier.fillMaxSize()
                    )

                    IconButton(
                        onClick = { showFullMap = false },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Компонент Unity карты
 */
@Composable
fun UnityMapView(
    places: List<PlaceElement>,
    userLocation: GeoLocation?,
    unityPlayer: UnityPlayer?,
    isFullScreen: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Отправляем данные в Unity при изменении мест или геолокации
    LaunchedEffect(places, userLocation) {
        if (unityPlayer != null && places.isNotEmpty() && userLocation != null) {
            sendDataToUnity(places, userLocation)
        }
    }

    if (unityPlayer == null) {
        // Fallback если Unity не доступен
        Box(
            modifier = modifier.background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Unity карта не загружена",
                color = Color.Gray
            )
        }
        return
    }

    // Встраиваем Unity View
    AndroidView(
        factory = {
            unityPlayer.view as? FrameLayout ?: FrameLayout(context).apply {
                setBackgroundColor(android.graphics.Color.parseColor("#1E1E1E"))
            }
        },
        modifier = modifier
    )
}

/**
 * Настраивает обработчики событий из Unity
 */
private fun setupUnityHandlers(viewModel: PlacesViewModel) {
    // Клик на POI
    UnityBridge.onPOIClicked = { poiId, name, type ->
        android.util.Log.d("PlacesMenu", "POI clicked: $name ($type)")
        // Здесь можно открыть детали места
        // или показать Toast
    }

    // Место посещено
    UnityBridge.onPlaceVisited = { poiId, impression, timestamp ->
        android.util.Log.d("PlacesMenu", "Place visited: $poiId, impression: $impression")
        // Сохранить в БД или отправить на бэкенд
    }

    // Впечатление обновлено
    UnityBridge.onImpressionUpdated = { poiId, impression ->
        android.util.Log.d("PlacesMenu", "Impression updated: $poiId -> $impression")
        // Обновить в БД
    }

    // Карта готова
    UnityBridge.onMapReady = {
        android.util.Log.d("PlacesMenu", "Unity map is ready!")
    }
}

/**
 * Отправляет данные о местах в Unity
 */
private fun sendDataToUnity(places: List<PlaceElement>, userLocation: GeoLocation) {
    try {
        // Конвертируем PlacesResponse → MapData для Unity
        val center = LatLng(userLocation.lat, userLocation.lon)
        val bounds = MapBounds.fromCenterAndRadius(center, 1000)

        // Преобразуем PlaceElement → POI
        val pois = places.mapNotNull { element ->
            convertOSMElementToPOI(element)
        }

        val mapData = MapData(
            bounds = bounds,
            pois = pois,
            userLocation = center,
            visitedPlaces = emptySet() // TODO: загружать из БД
        )

        // Отправляем через UnityBridge
        UnityBridge.sendMapData(mapData)
        UnityBridge.updateUserLocation(center)

        android.util.Log.d("PlacesMenu", "Sent ${pois.size} POIs to Unity")

    } catch (e: Exception) {
        android.util.Log.e("PlacesMenu", "Error sending data to Unity", e)
    }
}

/**
 * Конвертирует PlaceElement в POI для Unity
 */
private fun convertOSMElementToPOI(element: PlaceElement): POI? {
    // Получаем координаты
    val (lat, lon) = when {
        element.point != null -> element.point[1] to element.point[0] // [lon, lat] → (lat, lon)
        element.points != null && element.points.isNotEmpty() -> {
            val firstPoint = element.points.first()
            firstPoint[1] to firstPoint[0]
        }
        else -> return null // Нет координат
    }

    // Собираем теги из всех возможных полей PlaceElement
    val tags = buildMap<String, String> {
        element.name?.let { put("name", it) }
        element.amenity?.let { put("amenity", it) }
        element.shop?.let { put("shop", it) }
        element.leisure?.let { put("leisure", it) }
    }

    // Определяем тип POI
    val poiType = POIType.fromOsmTags(tags)

    // Получаем имя
    val name = element.name ?: element.amenity ?: element.shop ?: poiType.osmTag

    return POI(
        id = element.id.toString(),
        name = name,
        type = poiType,
        location = LatLng(lat, lon),
        isVisited = false
    )
}
