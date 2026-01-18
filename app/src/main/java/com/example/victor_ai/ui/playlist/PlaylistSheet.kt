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

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.ui.playlist.components.CurrentTrackPlayer
import com.example.victor_ai.ui.playlist.components.TrackCacheState
import com.example.victor_ai.ui.playlist.components.TrackItemCompact

private const val TAG = "PlaylistSheet"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistSheet(
    uiState: PlaylistUiState,
    onPlayPause: (Int?) -> Unit,
    onSeek: (Float) -> Unit,
    onEnergyFilterChange: (String?) -> Unit,
    onTemperatureFilterChange: (String?) -> Unit,
    onSortByChange: (String) -> Unit,
    onUpdateDescription: (String, String?, String?) -> Unit,
    onCacheTrack: (TrackUiModel) -> Unit,
    onRemoveCachedTrack: (Int) -> Unit,
    onEditTrack: (TrackUiModel?) -> Unit,
    modifier: Modifier = Modifier
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val grayText = Color(0xFFE0E0E0)
    val barEmpty = Color(0xFF555555)
    val barFilled = Color(0xFFCCCCCC)

    // 🔥 UI-локальные состояния для выпадающих меню (не влияют на бизнес-логику)
    var showEnergyDropdown by remember { mutableStateOf(false) }
    var showTempDropdown by remember { mutableStateOf(false) }
    var showSortDropdown by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // ← Текущий трек
    val currentTrack = remember(uiState.currentPlayingTrackId, uiState.tracks) {
        uiState.tracks.firstOrNull { it.id == uiState.currentPlayingTrackId }
    }

    // 🔥 Логирование для отладки
    LaunchedEffect(uiState.currentPlayingTrackId, uiState.isPlaying, uiState.tracks.size) {
        Log.d(TAG, "currentPlayingTrackId=${uiState.currentPlayingTrackId}, isPlaying=${uiState.isPlaying}, tracksCount=${uiState.tracks.size}")
        Log.d(TAG, "currentTrack=${currentTrack?.title ?: "null"}")
    }

    // 🔥 Скролл к началу при смене сортировки
    LaunchedEffect(uiState.sortBy) {
        if (uiState.tracks.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    // 🔥 Всегда показываем список треков
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .nestedScroll(rememberNestedScrollInteropConnection()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Плейлист",
            fontSize = 20.sp,
            color = grayText,
            fontFamily = didactGothic,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // ← Текущий трек с плеером
        CurrentTrackPlayer(
            track = currentTrack,
            isPlaying = uiState.isPlaying,
            currentPosition = uiState.currentPosition,
            onPlayPause = {
                if (currentTrack == null && uiState.tracks.isNotEmpty()) {
                    // Рандомный трек из отфильтрованных
                    val randomTrack = uiState.tracks.random()
                    onPlayPause(randomTrack.id)
                } else {
                    onPlayPause(currentTrack?.id)
                }
            },
            onSeek = onSeek,
            grayText = grayText,
            barEmpty = barEmpty,
            barFilled = barFilled
        )

        // ═══════════════════════════════════════════════════════
        // 🔥 ФИЛЬТРЫ — две строки
        // ═══════════════════════════════════════════════════════
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Первая строка: энергия + температура
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Энергия
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { showEnergyDropdown = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = grayText
                        ),
                        border = BorderStroke(1.dp, barEmpty),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(
                            text = uiState.energyFilter ?: "Энергия",
                            fontSize = 14.sp,
                            fontFamily = didactGothic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    DropdownMenu(
                        expanded = showEnergyDropdown,
                        onDismissRequest = { showEnergyDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все", fontSize = 14.sp, fontFamily = didactGothic) },
                            onClick = {
                                onEnergyFilterChange(null)
                                showEnergyDropdown = false
                            }
                        )
                        uiState.energyOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, fontSize = 14.sp, fontFamily = didactGothic) },
                                onClick = {
                                    onEnergyFilterChange(option)
                                    showEnergyDropdown = false
                                }
                            )
                        }
                    }
                }

                // Температура
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { showTempDropdown = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = grayText
                        ),
                        border = BorderStroke(1.dp, barEmpty),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(
                            text = uiState.temperatureFilter ?: "Температура",
                            fontSize = 14.sp,
                            fontFamily = didactGothic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    DropdownMenu(
                        expanded = showTempDropdown,
                        onDismissRequest = { showTempDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все", fontSize = 14.sp, fontFamily = didactGothic) },
                            onClick = {
                                onTemperatureFilterChange(null)
                                showTempDropdown = false
                            }
                        )
                        uiState.temperatureOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, fontSize = 14.sp, fontFamily = didactGothic) },
                                onClick = {
                                    onTemperatureFilterChange(option)
                                    showTempDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Вторая строка: сортировка
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { showSortDropdown = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = grayText
                    ),
                    border = BorderStroke(1.dp, barEmpty),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = when (uiState.sortBy) {
                            "title" -> "Сортировка: По названию"
                            "artist" -> "Сортировка: По исполнителю"
                            "duration" -> "Сортировка: По длине"
                            else -> "Сортировка: Недавние"
                        },
                        fontSize = 14.sp,
                        fontFamily = didactGothic
                    )
                }
                DropdownMenu(
                    expanded = showSortDropdown,
                    onDismissRequest = { showSortDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Недавние", fontSize = 14.sp, fontFamily = didactGothic) },
                        onClick = { onSortByChange("recent"); showSortDropdown = false }
                    )
                    DropdownMenuItem(
                        text = { Text("По названию", fontSize = 14.sp, fontFamily = didactGothic) },
                        onClick = { onSortByChange("title"); showSortDropdown = false }
                    )
                    DropdownMenuItem(
                        text = { Text("По исполнителю", fontSize = 14.sp, fontFamily = didactGothic) },
                        onClick = { onSortByChange("artist"); showSortDropdown = false }
                    )
                    DropdownMenuItem(
                        text = { Text("По длине", fontSize = 14.sp, fontFamily = didactGothic) },
                        onClick = { onSortByChange("duration"); showSortDropdown = false }
                    )
                }
            }
        }

        // ═══════════════════════════════════════════════════════
        // 🔥 СПИСОК ТРЕКОВ
        // ═══════════════════════════════════════════════════════
        
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
                strokeWidth = 2.dp,
                color = barFilled
            )
        } else if (uiState.tracks.isEmpty()) {
            Text(
                text = "Нет треков",
                fontSize = 18.sp,
                color = barEmpty,
                fontFamily = didactGothic,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.tracks, key = { it.id }) { track ->
                    TrackItemCompact(
                        track = track,
                        isPlaying = uiState.currentPlayingTrackId == track.id && uiState.isPlaying,
                        onPlayPause = { onPlayPause(track.id) },
                        onClick = { onEditTrack(track) },
                        grayText = grayText,
                        cacheState = track.cacheState,
                        onCacheClick = {
                            // 🔥 Переключаем состояние кеша
                            when (track.cacheState) {
                                TrackCacheState.NOT_CACHED -> onCacheTrack(track)
                                TrackCacheState.CACHED -> onRemoveCachedTrack(track.id)
                                TrackCacheState.DOWNLOADING -> { /* Ничего не делаем */ }
                            }
                        }
                    )
                }
            }
        }
    }
}
