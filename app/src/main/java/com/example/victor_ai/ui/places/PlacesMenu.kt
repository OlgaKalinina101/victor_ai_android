package com.example.victor_ai.ui.places

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.victor_ai.data.network.dto.PlaceDto
import android.graphics.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.victor_ai.data.network.dto.GeoLocation
import com.unity3d.player.UnityPlayer

@Composable
fun PlacesMenu(
    onBack: () -> Unit,
    viewModel: PlacesViewModel,
    unityPlayer: UnityPlayer? = null // ← добавь параметр
) {
    val places by viewModel.places
    val loading by viewModel.loading
    val error by viewModel.error
    var showFullMap by remember { mutableStateOf(false) }
    var latestGeo by remember { mutableStateOf<GeoLocation?>(null) }

    // Загружаем места
    LaunchedEffect(Unit) {
        latestGeo = GeoLocation(lat = 55.8445, lon = 37.3581)
        viewModel.loadPlacesAround(
            latitude = latestGeo!!.lat,
            longitude = latestGeo!!.lon,
            radiusMeters = 1000
        )
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
                        style = MaterialTheme.typography.headlineMedium
                    )
                    else -> Text("Нет данных", color = Color.Gray)
                }
            }

            // === Низ: Unity карта ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showFullMap = true }
            ) {
                MiniMap(
                    places = places,
                    centerLat = latestGeo?.lat,
                    centerLon = latestGeo?.lon,
                    latestGeo = latestGeo,
                    isFullScreen = false,
                    unityPlayer = unityPlayer,
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
                    MiniMap(
                        places = places,
                        centerLat = latestGeo?.lat,
                        centerLon = latestGeo?.lon,
                        latestGeo = latestGeo,
                        isFullScreen = true,
                        unityPlayer = unityPlayer,
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

@Composable
fun MiniMap(
    places: List<OSMElement>,
    centerLat: Double? = 55.751244,
    centerLon: Double? = 37.618423,
    latestGeo: GeoLocation? = null,
    isFullScreen: Boolean,
    modifier: Modifier = Modifier,
    unityPlayer: UnityPlayer? = null // ← передай Unity плеер
) {
    if (places.isEmpty()) {
        Box(
            modifier = modifier.background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Text("Нет данных для отображения", color = Color.Gray)
        }
        return
    }

    // Отправляем данные в Unity
    LaunchedEffect(places, latestGeo) {
        val json = convertToUnityFormat(places, latestGeo)
        unityPlayer.UnitySendMessage("MapController", "LoadOSMData", json)
    }

    // Контейнер для Unity View
    AndroidView(
        factory = { context ->
            // Unity View уже создан где-то в активити/фрагменте
            // Возвращаем контейнер-заглушку или сам UnityPlayer.view
            unityPlayer?.view ?: FrameLayout(context).apply {
                setBackgroundColor(android.graphics.Color.parseColor("#E3F2FD"))
            }
        },
        modifier = modifier
    )
}

/**
 * Конвертирует OSMElement + геолокация → JSON для Unity
 */
private fun convertToUnityFormat(
    elements: List<OSMElement>,
    userLocation: GeoLocation?
): String {
    val data = buildMap {
        // Места
        put("places", elements.map { element ->
            buildMap {
                put("id", element.id)
                put("type", element.type)

                // Геометрия
                when {
                    element.point != null -> put("point", element.point)
                    element.points != null -> put("points", element.points)
                    element.rings != null -> put("rings", element.rings)
                }

                // Теги (все)
                element.tags?.let { tags ->
                    put("tags", tags)
                }
            }
        })

        // Геолокация пользователя
        userLocation?.let { geo ->
            put("userLocation", mapOf(
                "lat" to geo.lat,
                "lon" to geo.lon
            ))
        }
    }

    return Json.encodeToString(data)
}

