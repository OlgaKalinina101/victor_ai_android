package com.example.victor_ai.ui.playlist

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.domain.model.Track
import com.example.victor_ai.ui.playlist.components.CurrentTrackPlayer
import com.example.victor_ai.ui.playlist.components.TrackItemCompact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistSheet(
    tracks: List<Track>,
    loading: Boolean,
    currentPlayingTrackId: Int?,
    isPlaying: Boolean,
    currentPosition: Float,
    onPlayPause: (Int?) -> Unit,
    onSeek: (Float) -> Unit,
    onEnergyChange: (String, String?) -> Unit,
    onTemperatureChange: (String, String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel,
    onEditTrack: (Track?) -> Unit  // 🔥 Колбэк для открытия редактирования
) {
    val grayText = Color(0xFFE0E0E0)
    val barEmpty = Color(0xFF555555)
    val barFilled = Color(0xFFCCCCCC)

    // Фильтры (как в MemoriesSheet)
    var energyFilter by remember { mutableStateOf<String?>(null) }
    var temperatureFilter by remember { mutableStateOf<String?>(null) }
    var showEnergyDropdown by remember { mutableStateOf(false) }
    var showTempDropdown by remember { mutableStateOf(false) }

    // Сортировка
    var sortBy by remember { mutableStateOf("recent") }
    var showSortDropdown by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    // Уникальные значения для фильтров
    val energyOptions = tracks.mapNotNull { it.energyDescription }.distinct().sorted()
    val tempOptions = tracks.mapNotNull { it.temperatureDescription }.distinct().sorted()

    // Фильтрация и сортировка
    val filteredTracks = tracks
        .filter { track ->
            (energyFilter == null || track.energyDescription == energyFilter) &&
                    (temperatureFilter == null || track.temperatureDescription == temperatureFilter)
        }
        .let { list ->
            when (sortBy) {
                "title" -> list.sortedBy { it.title }
                "artist" -> list.sortedBy { it.artist }
                "duration" -> list.sortedByDescending { it.duration }
                else -> list.sortedByDescending { it.id } // recent
            }
        }

    // ← ДОБАВЛЕНО: текущий трек
    val currentTrack = tracks.firstOrNull { it.id == currentPlayingTrackId }

    // 🔥 Логирование для отладки
    LaunchedEffect(currentPlayingTrackId, isPlaying, tracks.size) {
        println("🎵 PlaylistSheet: currentPlayingTrackId=$currentPlayingTrackId, isPlaying=$isPlaying, tracksCount=${tracks.size}")
        println("🎵 PlaylistSheet: currentTrack=${currentTrack?.title ?: "null"}")
    }

    LaunchedEffect(sortBy) {
        if (filteredTracks.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    // 🔥 НОВОЕ: Синхронизируем фильтры с ViewModel для автовоспроизведения
    LaunchedEffect(energyFilter, temperatureFilter, sortBy) {
        viewModel.setFilters(energyFilter, temperatureFilter, sortBy)
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
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // ← ДОБАВЛЕНО: Текущий трек
        CurrentTrackPlayer(
            track = currentTrack,
            isPlaying = isPlaying,
            currentPosition = currentPosition,
            onPlayPause = {
                if (currentTrack == null && filteredTracks.isNotEmpty()) {
                    // Рандомный трек из отфильтрованных
                    val randomTrack = filteredTracks.random()
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

        // Фильтры — две строки
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
                            text = energyFilter ?: "Энергия",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    DropdownMenu(
                        expanded = showEnergyDropdown,
                        onDismissRequest = { showEnergyDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все", fontSize = 14.sp) },
                            onClick = {
                                energyFilter = null
                                showEnergyDropdown = false
                            }
                        )
                        energyOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, fontSize = 14.sp) },
                                onClick = {
                                    energyFilter = option
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
                            text = temperatureFilter ?: "Температура",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    DropdownMenu(
                        expanded = showTempDropdown,
                        onDismissRequest = { showTempDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все", fontSize = 14.sp) },
                            onClick = {
                                temperatureFilter = null
                                showTempDropdown = false
                            }
                        )
                        tempOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, fontSize = 14.sp) },
                                onClick = {
                                    temperatureFilter = option
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
                        text = when (sortBy) {
                            "title" -> "Сортировка: По названию"
                            "artist" -> "Сортировка: По исполнителю"
                            "duration" -> "Сортировка: По длине"
                            else -> "Сортировка: Недавние"
                        },
                        fontSize = 14.sp
                    )
                }
                DropdownMenu(
                    expanded = showSortDropdown,
                    onDismissRequest = { showSortDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Недавние", fontSize = 14.sp) },
                        onClick = { sortBy = "recent"; showSortDropdown = false }
                    )
                    DropdownMenuItem(
                        text = { Text("По названию", fontSize = 14.sp) },
                        onClick = { sortBy = "title"; showSortDropdown = false }
                    )
                    DropdownMenuItem(
                        text = { Text("По исполнителю", fontSize = 14.sp) },
                        onClick = { sortBy = "artist"; showSortDropdown = false }
                    )
                    DropdownMenuItem(
                        text = { Text("По длине", fontSize = 14.sp) },
                        onClick = { sortBy = "duration"; showSortDropdown = false }
                    )
                }
            }
        }

        // Список треков
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
                strokeWidth = 2.dp,
                color = barFilled
            )
        } else if (filteredTracks.isEmpty()) {
            Text(
                text = "Нет треков",
                fontSize = 18.sp,
                color = barEmpty,
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
            ){
                items(filteredTracks, key = { it.id }) { track ->
                    TrackItemCompact(
                        track = track,
                        isPlaying = currentPlayingTrackId == track.id && isPlaying,
                        onPlayPause = { onPlayPause(track.id) },
                        onClick = { onEditTrack(track) },  // 🔥 Используем колбэк из родителя
                        grayText = grayText
                    )
                }
            }
        }
    }
}
