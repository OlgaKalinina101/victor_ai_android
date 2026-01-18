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

package com.example.victor_ai.ui.map.composables

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.PlaceCaptionRequest
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.ui.map.canvas.MapCanvasView
import com.example.victor_ai.ui.map.models.*
import com.example.victor_ai.ui.map.renderer.Canvas2DMapRenderer
import com.example.victor_ai.ui.map.renderer.MapRenderer
import com.example.victor_ai.ui.map.POIOverlay
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime

/**
 * 🗺️ Основной контент карты
 */
@Composable
fun MapContent(
    isLoading: Boolean,
    error: String?,
    selectedPOI: POI?,
    userLocation: LatLng?,
    searching: Boolean,
    elapsedSec: Long,
    walkedMeters: Double,
    nearby: List<POI>,
    visitedPOIs: Map<String, VisitEmotion>,
    pois: List<POI>,
    mapView: MapCanvasView?,
    onMapViewCreated: (MapCanvasView) -> Unit,
    onMapRendererCreated: (MapRenderer) -> Unit,
    onPOIClicked: (POI) -> Unit,
    onToggleSearch: () -> Unit,
    onDismissOverlay: () -> Unit,
    onSelectNearby: (POI) -> Unit,
    onMarkVisited: (VisitEmotion?) -> Unit,
    onMarkFound: (POI) -> Unit,
    placesApi: PlacesApi
) {
    val searchingState by rememberUpdatedState(searching)
    val selectedIdState by rememberUpdatedState(selectedPOI?.id)

    Box(modifier = Modifier.fillMaxSize()) {
        // Карта
        AndroidView(
            factory = { ctx ->
                MapCanvasView(ctx).apply {
                    val view = this
                    onMapViewCreated(view)
                    onMapRendererCreated(Canvas2DMapRenderer(view))
                    this.onPOIClicked = { poi -> onPOIClicked(poi) }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 💔 Экран ошибки для любых ошибок (с кастомными сообщениями)
        if (error != null) {
            MapLoadErrorScreen(
                errorMessage = error,
                modifier = Modifier.fillMaxSize()
            )
        }
        // 🎨 Кастомный прелоадер вместо стандартного
        else if (isLoading) {
            CustomMapLoader(modifier = Modifier.fillMaxSize())
        }

        // 🧩 Оверлей с деталями POI
        selectedPOI?.let { poi ->
            // Синхронизируем выбранный POI с MapView
            LaunchedEffect(poi) { 
                mapView?.setSelectedPOI(poi) 
            }

            // 💬 Комикс-облачко рядом с POI (печать посимвольно)
            LaunchedEffect(poi.id) {
                // При выборе нового POI — начинаем с чистого бабла
                mapView?.setSpeechBubbleText(null)

                // Если уже в режиме поиска — бабл не показываем
                if (searchingState) return@LaunchedEffect

                val visitEmotion = poi.impression?.let { impression ->
                    VISIT_EMOTIONS.find { it.name == impression }
                } ?: visitedPOIs[poi.name]

                val text = resolveBubbleTextOrCaption(
                    poi = poi,
                    visitEmotion = visitEmotion,
                    searching = { searchingState },
                    selectedId = { selectedIdState },
                    onProgressText = { t -> mapView?.setSpeechBubbleText(t) },
                    placesApi = placesApi
                )

                if (text.isBlank()) return@LaunchedEffect

                // Финальная печать посимвольно (или короткое сообщение)
                typewriterPrint(
                    fullText = text,
                    searching = { searchingState },
                    selectedId = { selectedIdState },
                    poiId = poi.id,
                    onUpdate = { t -> mapView?.setSpeechBubbleText(t) }
                )
            }

            POIOverlay(
                poi = poi,
                userLocation = userLocation,
                searching = searching,
                elapsedSec = elapsedSec,
                walkedMeters = walkedMeters,
                nearby = nearby,
                isVisited = poi.isVisited,
                visitEmotion = poi.impression?.let { impression ->
                    VISIT_EMOTIONS.find { it.name == impression }
                } ?: visitedPOIs[poi.name],
                onToggleSearch = onToggleSearch,
                onDismiss = onDismissOverlay,
                onSelectNearby = onSelectNearby,
                onMarkVisited = onMarkVisited,
                onMarkFound = onMarkFound,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    // Scaffold уже сдвигает контент под TopAppBar, поэтому лишний верхний отступ не нужен:
                    // оверлей должен визуально "приклеиваться" к header.
                    .padding(top = 0.dp)
            )
        }

        // Если стартовал поиск — бабл должен исчезнуть
        LaunchedEffect(searching) {
            if (searching) mapView?.setSpeechBubbleText(null)
        }

        // Если сняли выделение POI — прячем бабл и цель
        LaunchedEffect(selectedPOI?.id) {
            if (selectedPOI == null) {
                mapView?.setSelectedPOI(null)
                mapView?.setSpeechBubbleText(null)
            }
        }
    }
}

private fun buildPlaceElementBubbleText(poi: POI, visitEmotion: VisitEmotion?): String {
    if (poi.isVisited) {
        return if (visitEmotion != null) {
            "Тут ${visitEmotion.name.lowercase()} ${visitEmotion.emoji}"
        } else {
            "Тут понравилось 😊"
        }
    }

    // "закрыто" — вычисляем по opening_hours
    val openingHours = poi.tags["opening_hours"]?.toString()?.trim().takeUnless { it.isNullOrEmpty() }
    val openNow: Boolean? = openingHours?.let { parseIsOpenNow(it, LocalDateTime.now()) }
    if (openNow == false) return "Сейчас закрыто 😔"

    // Иначе (открыто/не смогли распарсить) — тут уже будет LLM caption
    return ""
}

private suspend fun resolveBubbleTextOrCaption(
    poi: POI,
    visitEmotion: VisitEmotion?,
    searching: () -> Boolean,
    selectedId: () -> String?,
    onProgressText: (String) -> Unit,
    placesApi: PlacesApi
): String {
    // visited/closed handled here
    val quick = buildPlaceElementBubbleText(poi, visitEmotion)
    if (quick.isNotBlank()) return quick

    // Только для НЕ посещённых и НЕ закрытых: дергаем caption endpoint
    val osmId = poi.id.toLongOrNull() ?: return "" // без id не можем идентифицировать
    val osmType = when (poi.elementType?.lowercase()) {
        "node", "way", "relation" -> poi.elementType!!.lowercase()
        else -> "node"
    }

    val req = PlaceCaptionRequest(
        accountId = UserProvider.getCurrentUserId(),
        poiOsmId = osmId,
        poiOsmType = osmType,
        tags = buildCaptionTags(poi.tags)
    )

    // Пока ждём — анимируем "...", посимвольно, параллельно с сетевым запросом
    return coroutineScope {
        val dotsJob = launch {
            var dots = ""
            while (isActive) {
                if (searching() || selectedId() != poi.id) return@launch
                dots = if (dots.length >= 3) "" else dots + "."
                onProgressText(dots)
                delay(180L)
            }
        }

        try {
            val resp = placesApi.generatePlaceCaption(req)
            dotsJob.cancel()
            resp.caption.trim()
        } catch (_: Throwable) {
            dotsJob.cancel()
            "" // тихо гасим — bubble просто исчезнет
        }
    }
}

private suspend fun typewriterPrint(
    fullText: String,
    searching: () -> Boolean,
    selectedId: () -> String?,
    poiId: String,
    onUpdate: (String) -> Unit
) {
    for (i in 1..fullText.length) {
        if (searching() || selectedId() != poiId) {
            onUpdate("")
            return
        }
        onUpdate(fullText.substring(0, i))
        delay(18L)
    }
}

private fun buildCaptionTags(tags: Map<String, String>): Map<String, Any> {
    // Отправляем "те теги, что печатали бы в облачке", но без контактов/ссылок/служебки.
    // Сохраняем открытие/тип/диеты/адресность и т.п.
    val out = linkedMapOf<String, Any>()

    fun putIfPresent(k: String) {
        val v = tags[k]?.trim().takeUnless { it.isNullOrEmpty() } ?: return
        out[k] = v
    }

    // Базовое
    putIfPresent("name")
    putIfPresent("amenity")
    putIfPresent("shop")
    putIfPresent("leisure")
    putIfPresent("tourism")
    putIfPresent("cuisine")
    putIfPresent("opening_hours")
    putIfPresent("addr:floor")
    putIfPresent("level")
    putIfPresent("diet:vegetarian")

    // Остальное полезное
    tags.entries
        .asSequence()
        .filter { (k, v) -> v.isNotBlank() && isUsefulPlaceTagKey(k) }
        .sortedBy { it.key }
        .forEach { (k, v) ->
            if (k !in out) out[k] = v.trim()
        }

    return out.ifEmpty { emptyMap() }
}

/**
 * Отбрасываем контакты/ссылки/служебные теги и оставляем только то, что помогает понять место.
 */
private fun isUsefulPlaceTagKey(keyRaw: String): Boolean {
    val key = keyRaw.trim()
    val k = key.lowercase()

    // Контакты и ссылки
    if (k.startsWith("contact:")) return false
    if (k.startsWith("social:")) return false
    if (k.contains("phone")) return false
    if (k.contains("website")) return false
    if (k.contains("url")) return false
    if (k.contains("email")) return false
    if (k.contains("instagram") || k.contains("facebook") || k.contains("vk") || k.contains("telegram") || k.contains("whatsapp") || k.contains("twitter") || k.contains("tiktok") || k.contains("youtube")) {
        return false
    }

    // Служебка/метаданные
    if (k == "check_date" || k.startsWith("check_date:")) return false
    if (k == "source" || k.startsWith("source:")) return false
    if (k == "fixme" || k == "note" || k == "created_by") return false

    // Адрес: оставляем, но некоторые поля часто не нужны/шумят
    if (k.startsWith("addr:")) {
        if (k == "addr:postcode" || k == "addr:country") return false
        return true
    }

    // Полезные категории
    val allowPrefixes = listOf(
        "diet:",
        "payment:",
        "toilets",
        "wheelchair",
        "takeaway",
        "delivery",
        "outdoor_seating",
        "indoor_seating",
        "smoking",
        "internet_access",
        "cuisine",
        "brand",
        "operator",
        "level"
    )
    if (allowPrefixes.any { k.startsWith(it) || k == it }) return true

    // По умолчанию — не показываем (чтобы не тащить весь мусор)
    return false
}

/**
 * Best-effort парсер `opening_hours` для самых частых кейсов:
 * - "24/7"
 * - "Mo-Su 10:00-22:00"
 * - "Mo-Fr 10:00-20:00; Sa-Su 11:00-18:00"
 * - несколько интервалов через запятую.
 *
 * @return true/false если уверены, null если не смогли распарсить.
 */
private fun parseIsOpenNow(openingHours: String, now: LocalDateTime): Boolean? {
    val s = openingHours.trim()
    if (s.isEmpty()) return null
    if (s.equals("24/7", ignoreCase = true)) return true

    val today = now.dayOfWeek
    val nowMin = now.hour * 60 + now.minute

    var hadRuleForToday = false

    val rules = s.split(';').map { it.trim() }.filter { it.isNotEmpty() }
    for (rule in rules) {
        val lower = rule.lowercase()
        if (lower.contains("off") || lower.contains("closed")) continue

        val m = Regex("^([A-Za-z,\\-\\s]+)\\s+(.+)$").find(rule)
        val (daysPart, timesPart) = if (m != null) {
            m.groupValues[1].trim() to m.groupValues[2].trim()
        } else {
            // если дней нет — считаем, что на все дни
            "" to rule.trim()
        }

        val days = if (daysPart.isBlank()) allDays() else parseDays(daysPart) ?: return null
        if (today !in days) continue
        hadRuleForToday = true

        val intervals = parseTimeIntervals(timesPart) ?: return null
        if (intervals.any { (start, end) -> isTimeWithin(nowMin, start, end) }) return true
    }

    return if (hadRuleForToday) false else null
}

private fun allDays(): Set<DayOfWeek> = setOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY
)

private fun parseDays(daysPart: String): Set<DayOfWeek>? {
    val tokens = daysPart
        .split(',')
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    val result = mutableSetOf<DayOfWeek>()
    for (t in tokens) {
        val range = t.split('-').map { it.trim() }
        if (range.size == 1) {
            val d = parseDayToken(range[0]) ?: return null
            result += d
        } else if (range.size == 2) {
            val start = parseDayToken(range[0]) ?: return null
            val end = parseDayToken(range[1]) ?: return null
            result += expandDayRange(start, end)
        } else {
            return null
        }
    }
    return result
}

private fun parseDayToken(token: String): DayOfWeek? = when (token.trim().lowercase()) {
    "mo" -> DayOfWeek.MONDAY
    "tu" -> DayOfWeek.TUESDAY
    "we" -> DayOfWeek.WEDNESDAY
    "th" -> DayOfWeek.THURSDAY
    "fr" -> DayOfWeek.FRIDAY
    "sa" -> DayOfWeek.SATURDAY
    "su" -> DayOfWeek.SUNDAY
    else -> null
}

private fun expandDayRange(start: DayOfWeek, end: DayOfWeek): Set<DayOfWeek> {
    // opening_hours использует Mo..Su. Диапазон может быть "Fr-Mo".
    val order = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )
    val s = order.indexOf(start)
    val e = order.indexOf(end)
    if (s == -1 || e == -1) return emptySet()
    return if (s <= e) {
        order.subList(s, e + 1).toSet()
    } else {
        (order.subList(s, order.size) + order.subList(0, e + 1)).toSet()
    }
}

private fun parseTimeIntervals(timesPart: String): List<Pair<Int, Int>>? {
    val part = timesPart.trim()
    if (part.isEmpty()) return null

    val chunks = part.split(',').map { it.trim() }.filter { it.isNotEmpty() }
    val result = mutableListOf<Pair<Int, Int>>()

    for (c in chunks) {
        val m = Regex("^(\\d{1,2}:\\d{2})\\s*-\\s*(\\d{1,2}:\\d{2})$").find(c) ?: return null
        val start = parseTimeMinutes(m.groupValues[1]) ?: return null
        val end = parseTimeMinutes(m.groupValues[2]) ?: return null
        result += start to end
    }
    return result
}

/**
 * Парсит время в минутах от полуночи.
 * Поддерживает "24:00" как 1440 (конец дня).
 */
private fun parseTimeMinutes(token: String): Int? {
    return try {
        val t = token.trim()
        val parts = t.split(':')
        if (parts.size != 2) return null
        val h = parts[0].toInt()
        val m = parts[1].toInt()
        if (h == 24 && m == 0) return 24 * 60
        if (h !in 0..23) return null
        if (m !in 0..59) return null
        h * 60 + m
    } catch (_: Throwable) {
        null
    }
}

private fun isTimeWithin(nowMin: Int, startMin: Int, endMin: Int): Boolean {
    return if (endMin >= startMin) {
        // Обычный интервал; end=1440 означает "до конца дня"
        if (endMin == 24 * 60) nowMin >= startMin else nowMin in startMin until endMin
    } else {
        // через полночь (например 22:00-02:00)
        nowMin >= startMin || nowMin < endMin
    }
}

