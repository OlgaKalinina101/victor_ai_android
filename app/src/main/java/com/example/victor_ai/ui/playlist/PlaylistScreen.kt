package com.example.victor_ai.ui.playlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import com.example.victor_ai.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.domain.model.Track
import com.example.victor_ai.ui.playlist.components.CurrentTrackPlayer
import com.example.victor_ai.ui.playlist.components.EditTrackMetadataSheet
import com.example.victor_ai.ui.playlist.components.TrackItemCompact


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    onBackClick: () -> Unit
) {
    val tracks by viewModel.tracks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentPlayingTrackId by viewModel.currentPlayingTrackId.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    var showPlaylistSheet by rememberSaveable { mutableStateOf(false) }  // 🔥 Используем rememberSaveable для защиты от recomposition

    // 🔥 Состояние редактирования трека - храним только ID для защиты от recomposition
    var editingTrackId by rememberSaveable { mutableStateOf<Int?>(null) }

    // 🔥 Получаем сам трек из списка по ID
    val editingTrack = editingTrackId?.let { id -> tracks.firstOrNull { it.id == id } }

    // 🔥 Автоматически защищаем плейлист когда открыто редактирование
    val keepPlaylistOpen = editingTrackId != null

    val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val stats by viewModel.stats.collectAsState()
    var showAmbientStream by rememberSaveable { mutableStateOf(false) }

    // 🔥 Обновляем статистику при открытии экрана
    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }

    // Состояние для анимации печати
    var typedText by remember { mutableStateOf("") }
    val fullText = "👀 > думаю о музыке..."

    // Запоминаем предыдущий ID трека для отслеживания запуска
    var previousTrackId by remember { mutableStateOf<Int?>(null) }

    // 🔥 Синхронизируем состояние плеера при открытии плейлиста
    LaunchedEffect(showPlaylistSheet) {
        if (showPlaylistSheet) {
            println("🔄 PlaylistScreen: showPlaylistSheet=true, calling syncPlayerState()")
            println("🔄 PlaylistScreen: currentPlayingTrackId=$currentPlayingTrackId, isPlaying=$isPlaying")
            viewModel.syncPlayerState()
        }
    }

    // Анимация печати текста
    LaunchedEffect(showAmbientStream) {
        if (showAmbientStream) {
            typedText = ""
            fullText.forEachIndexed { index, _ ->
                kotlinx.coroutines.delay(50) // 50мс между символами
                typedText = fullText.take(index + 1)
            }
        }
    }

    // Отслеживание запуска трека для скрытия анимации
    LaunchedEffect(currentPlayingTrackId) {
        if (previousTrackId != currentPlayingTrackId && currentPlayingTrackId != null) {
            // Трек изменился - скрываем анимацию
            showAmbientStream = false
            typedText = ""
        }
        previousTrackId = currentPlayingTrackId
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 Основной контент — колонка по центру
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (stats == null) {
                Text(
                    text = "Загружается статистика...",
                    color = Color(0xFFE0E0E0),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            } else {
                // Заголовок
                Text(
                    text = "СТАТИСТИКА НЕДЕЛИ",
                    color = Color(0xFF888888),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 1.5.sp
                )

                Spacer(Modifier.height(24.dp))

                // Трек недели
                stats?.top_tracks?.firstOrNull()?.let { t ->
                    Text(
                        text = "Трек недели",
                        color = Color(0xFF999999),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = t.title,
                        color = Color(0xFFE0E0E0),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${t.plays} прослушиваний",
                        color = Color(0xFF777777),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                Spacer(Modifier.height(32.dp))
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color(0xFF404040),
                    modifier = Modifier.padding(horizontal = 48.dp)
                )
                Spacer(Modifier.height(32.dp))

                // Характеристики
                Text(
                    text = "Энергия: ${stats?.top_energy ?: "—"}",
                    color = Color(0xFFB0B0B0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Температура: ${stats?.top_temperature ?: "—"}",
                    color = Color(0xFFB0B0B0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(Modifier.height(40.dp))
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color(0xFF404040),
                    modifier = Modifier.padding(horizontal = 48.dp)
                )
                Spacer(Modifier.height(40.dp))

                // Действия
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: запуск волны по треку недели */ }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Запустить волну",
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showAmbientStream = !showAmbientStream
                            if (showAmbientStream) viewModel.runPlaylistWave(manual = true)
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Выбери сам...",
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }

                // Индикатор процесса с анимацией печати
                AnimatedVisibility(
                    visible = showAmbientStream,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = typedText,
                            color = Color(0xFF555555),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // 🔹 Верхний бар (кнопка "Плейлист") - размещаем ПОСЛЕ Column чтобы был поверх
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                showPlaylistSheet = true
                println("PlaylistSheet opened")
            }) {
                Icon(
                    Icons.Default.List,
                    contentDescription = "Плейлист",
                    tint = Color(0xFFE0E0E0)
                )
            }
        }
    }

    // Плейлист
    if (showPlaylistSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                println("🔥 DISMISS REQUEST: keepPlaylistOpen=$keepPlaylistOpen, editingTrackId=$editingTrackId")
                if (!keepPlaylistOpen) {
                    showPlaylistSheet = false
                    println("🔥 PLAYLIST CLOSED")
                } else {
                    println("🔥 PLAYLIST DISMISS BLOCKED")
                }
            },
            sheetState = playlistSheetState,
            containerColor = Color(0xFF2B2929),
            modifier = Modifier.heightIn(max = screenHeight * 7 / 8)
        ) {
            // 🔥 Box для overlay редактирования поверх плейлиста
            Box(modifier = Modifier.fillMaxSize()) {
                PlaylistSheet(
                    tracks = tracks,
                    loading = isLoading,
                    currentPlayingTrackId = currentPlayingTrackId,
                    isPlaying = isPlaying,
                    currentPosition = currentPosition,
                    onPlayPause = { trackId ->
                        if (trackId == null) return@PlaylistSheet
                        if (currentPlayingTrackId == trackId) {
                            if (isPlaying) viewModel.pauseTrack() else viewModel.resumeTrack()
                        } else {
                            viewModel.playTrack(trackId)
                        }
                    },
                    onSeek = { position -> viewModel.seekTo(position) },
                    onEnergyChange = { trackId, energy ->
                        viewModel.updateDescription(trackId, energy, null)
                    },
                    onTemperatureChange = { trackId, temp ->
                        viewModel.updateDescription(trackId, null, temp)
                    },
                    viewModel = viewModel,
                    onEditTrack = { track ->
                        println("🔥 EDIT TRACK: track=$track")
                        editingTrackId = track?.id
                    }
                )

                // 🔥 НОВОЕ: EditSheet как overlay поверх списка
                if (editingTrack != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2B2929))
                            .pointerInput(Unit) {
                                // Блокируем все клики чтобы они не проваливались к плейлисту
                                detectTapGestures { }
                            }
                    ) {
                        EditTrackMetadataSheet(
                            track = editingTrack,
                            viewModel = viewModel,
                            onDismiss = {
                                println("🔥 EDIT SHEET ON DISMISS CALLED")
                                editingTrackId = null
                            }
                        )
                    }
                }
            }
        }
    }
}

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
