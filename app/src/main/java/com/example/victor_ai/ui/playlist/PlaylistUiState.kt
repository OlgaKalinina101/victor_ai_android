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

package com.example.victor_ai.ui.playlist

import androidx.compose.ui.graphics.Color
import com.example.victor_ai.data.network.Track
import com.example.victor_ai.data.network.TrackStats
import com.example.victor_ai.ui.playlist.components.TrackCacheState

/**
 * 🎯 Единое состояние UI для экрана плейлиста
 * Все state-переменные живут в одном месте
 */
data class PlaylistUiState(
    val tracks: List<TrackUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val currentPlayingTrackId: Int? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Float = 0f,
    val stats: TrackStats? = null,
    val isWaveLoading: Boolean = false,
    val error: ErrorState? = null,
    
    // 🔥 Фильтры (единая точка правды)
    val energyFilter: String? = null,
    val temperatureFilter: String? = null,
    val sortBy: String = "recent",
    
    // 🔥 Опции для фильтров (вычисляются один раз)
    val energyOptions: List<String> = emptyList(),
    val temperatureOptions: List<String> = emptyList(),
    
    // 🎵 Streaming логи от ассистента
    val streamingLog: String = "",  // Текущий лог для анимации печати

    // 🧾 Шторка для streaming-логов (аналогично календарной RecurringRemindersSheet)
    val showStreamingLogSheet: Boolean = false,

    // 📚 Архив "моментов выбора" (playlist moments)
    val showPlaylistMomentsSheet: Boolean = false,
    val playlistMoments: List<com.example.victor_ai.data.network.PlaylistMomentOut> = emptyList(),
    val isPlaylistMomentsLoading: Boolean = false,
    val playlistMomentsError: String? = null
)

/**
 * 🎯 UI модель трека с предобработанными данными
 * Вся логика форматирования вынесена из Composable
 */
data class TrackUiModel(
    val id: Int,
    val title: String,
    val artist: String,  // Никогда не null
    val album: String,
    val formattedDuration: String,  // "3:45"
    val duration: Float,  // Оригинальная длительность в секундах
    val energyDescription: String?,
    val energyColor: Color,
    val temperatureDescription: String?,
    val temperatureColor: Color,
    val cacheState: TrackCacheState,
    val genre: String,
    val year: String
)

/**
 * 🎯 Состояния ошибок с конкретными типами
 */
sealed class ErrorState {
    data class LoadingTracksError(val message: String) : ErrorState()
    data class PlaybackError(val message: String) : ErrorState()
    data class NetworkError(val message: String) : ErrorState()
    data class WaveError(val message: String) : ErrorState()
}

/**
 * 🎯 Маппер: Track (DTO) → TrackUiModel (UI)
 */
fun Track.toUiModel(cacheState: TrackCacheState = TrackCacheState.NOT_CACHED): TrackUiModel {
    return TrackUiModel(
        id = id,
        title = title,
        artist = artist ?: "Victor AI",
        album = album ?: "Неизвестный альбом",
        formattedDuration = formatDuration(duration),
        duration = duration,
        energyDescription = energyDescription,
        energyColor = getEnergyColor(energyDescription),
        temperatureDescription = temperatureDescription,
        temperatureColor = getTemperatureColor(temperatureDescription),
        cacheState = cacheState,
        genre = genre ?: "Неизвестный жанр",
        year = year?.toString() ?: "—"
    )
}

/**
 * 🎯 Форматирование длительности: 245.5 → "4:05"
 */
private fun formatDuration(seconds: Float): String {
    val totalSeconds = seconds.toInt()
    val minutes = totalSeconds / 60
    val secs = totalSeconds % 60
    return "%d:%02d".format(minutes, secs)
}

/**
 * 🎯 Цвет для энергии
 */
private fun getEnergyColor(energy: String?): Color {
    return when (energy?.lowercase()) {
        "высокая", "high" -> Color(0xFFFF5722)  // Яркий оранжево-красный
        "средняя", "medium" -> Color(0xFFFFC107)  // Янтарный
        "низкая", "low" -> Color(0xFF4CAF50)  // Зелёный
        "очень высокая", "very high" -> Color(0xFFD32F2F)  // Насыщенный красный
        "очень низкая", "very low" -> Color(0xFF2196F3)  // Синий
        else -> Color(0xFF9E9E9E)  // Серый для неизвестных
    }
}

/**
 * 🎯 Цвет для температуры
 */
private fun getTemperatureColor(temperature: String?): Color {
    return when (temperature?.lowercase()) {
        "тёплая", "warm", "тёплое", "тёплый" -> Color(0xFFFF9800)  // Оранжевый
        "холодная", "cold", "холодное", "холодный" -> Color(0xFF03A9F4)  // Голубой
        "нейтральная", "neutral", "нейтральное" -> Color(0xFF9E9E9E)  // Серый
        "горячая", "hot" -> Color(0xFFF44336)  // Красный
        "ледяная", "freezing" -> Color(0xFF00BCD4)  // Циан
        else -> Color(0xFF9E9E9E)  // Серый для неизвестных
    }
}

