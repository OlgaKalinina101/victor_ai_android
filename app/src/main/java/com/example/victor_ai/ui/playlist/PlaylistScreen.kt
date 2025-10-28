package com.example.victor_ai.ui.playlist

import com.example.victor_ai.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


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

    // 🔥 Синхронизируем состояние плеера при открытии плейлиста
    LaunchedEffect(showPlaylistSheet) {
        if (showPlaylistSheet) {
            println("🔄 PlaylistScreen: showPlaylistSheet=true, calling syncPlayerState()")
            println("🔄 PlaylistScreen: currentPlayingTrackId=$currentPlayingTrackId, isPlaying=$isPlaying")
            viewModel.syncPlayerState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // TopBar как обычный Row
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
                    "Плейлист",
                    tint = Color(0xFFE0E0E0)
                )
            }
        }

        // Основной контент
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Статистика появится здесь",
                color = Color(0xFFE0E0E0),
                fontSize = 16.sp
            )
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
                            track = editingTrack!!,
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


@Composable
fun TrackItemCompact(
    track: Track,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onClick: () -> Unit,
    grayText: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Play/Pause — отдельная кликабельная зона
            IconButton(
                onClick = onPlayPause,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause
                    else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color(0xFFCCCCCC)
                )
            }

            // Инфо о треке
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    color = grayText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.artist,
                    color = grayText.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Длительность
            Text(
                text = formatDuration(track.duration),
                color = grayText.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

fun formatDuration(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val secs = (seconds % 60).toInt()
    return "%d:%02d".format(minutes, secs)
}

@Composable
fun CurrentTrackPlayer(
    track: Track?,
    isPlaying: Boolean,
    currentPosition: Float,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    grayText: Color,
    barEmpty: Color,
    barFilled: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (track != null) {
            // Инфо о треке
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Play/Pause
                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause
                        else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Название и исполнитель
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.title,
                        color = grayText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = track.artist,
                        color = grayText.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Прогресс-бар с перемоткой
            Column {
                // Времена
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatDuration(currentPosition),
                        color = grayText.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = formatDuration(track.duration),
                        color = grayText.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }

                // Слайдер
                Slider(
                    value = currentPosition,
                    onValueChange = { onSeek(it) },
                    valueRange = 0f..track.duration,
                    colors = SliderDefaults.colors(
                        thumbColor = barFilled,
                        activeTrackColor = barFilled,
                        inactiveTrackColor = barEmpty
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            // Пустое состояние — только кнопка play
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play random",
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        // Разделитель
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = barEmpty
        )
    }
}

val CustomFont = FontFamily(
    Font(R.font.didact_gothic, FontWeight.Normal)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTrackMetadataSheet(
    track: Track,
    viewModel: PlaylistViewModel,
    onDismiss: () -> Unit
) {
    val energyOptions = listOf(
        "Светлая-ритмичная",
        "Тёплая-сердечная",
        "Тихая-заземляющая",
        "Отражающее-наблюдение",
        "Сложно-рефлексивные"
    )
    val temperatureOptions = listOf(
        "Тёплая",
        "Умеренная",
        "Горячая",
        "Холодная",
        "Ледяная"
    )

    var selectedEnergy by remember { mutableStateOf(track.energyDescription ?: "Светлая-ритмичная") }
    var selectedTemperature by remember { mutableStateOf(track.temperatureDescription ?: "Умеренная") }
    var energyExpanded by remember { mutableStateOf(false) }
    var temperatureExpanded by remember { mutableStateOf(false) }

    // 🔥 Убрали ModalBottomSheet — оставили только содержимое
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),  // 🔥 Добавили скролл
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 🔥 Добавили кнопку "Назад"
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Назад",
                tint = Color(0xFFE0E0E0)
            )
        }

        // Весь остальной код БЕЗ ИЗМЕНЕНИЙ
        Text(
            text = "Редактировать метаданные: ${track.title}",
            color = Color(0xFFE0E0E0),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = CustomFont
        )

            // Выпадающий список для Energy
        Column {
            Text(
                text = "Энергия",
                color = Color(0xFFE0E0E0),
                fontSize = 16.sp,
                fontFamily = CustomFont
            )

            Box {
                OutlinedButton(
                    onClick = { energyExpanded = !energyExpanded },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE0E0E0)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF555555))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedEnergy, fontSize = 16.sp)
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color(0xFFE0E0E0)
                        )
                    }
                }

                DropdownMenu(
                    expanded = energyExpanded,
                    onDismissRequest = { energyExpanded = false },
                    modifier = Modifier.background(Color(0xFF2B2929))
                ) {
                    energyOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    color = Color(0xFFE0E0E0),
                                    fontSize = 16.sp,
                                    fontFamily = CustomFont
                                )
                            },
                            onClick = {
                                selectedEnergy = option
                                energyExpanded = false
                            }
                        )
                    }
                }
            }
        }

            // Выпадающий список для Temperature
        Column {
            Text(
                text = "Температура",
                color = Color(0xFFE0E0E0),
                fontSize = 16.sp,
                fontFamily = CustomFont
            )

            Box {
                OutlinedButton(
                    onClick = { temperatureExpanded = !temperatureExpanded },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE0E0E0)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF555555))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedTemperature, fontSize = 16.sp)
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color(0xFFE0E0E0)
                        )
                    }
                }

                DropdownMenu(
                    expanded = temperatureExpanded,
                    onDismissRequest = { temperatureExpanded = false },
                    modifier = Modifier.background(Color(0xFF2B2929))
                ) {
                    temperatureOptions.forEach { option ->  // 🔥 temperatureOptions, не energy
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    color = Color(0xFFE0E0E0),
                                    fontSize = 16.sp,
                                    fontFamily = CustomFont
                                )
                            },
                            onClick = {
                                selectedTemperature = option
                                temperatureExpanded = false  // 🔥 temperatureExpanded, не selectedTemperature
                            }
                        )
                    }
                }
            }
        }

            // Кнопки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Отмена",
                        color = Color(0xFFE0E0E0),
                        fontSize = 16.sp,
                        fontFamily = CustomFont
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        println("🔥 SAVE BUTTON CLICKED")
                        viewModel.updateDescription(
                            trackId = track.id.toString(),
                            energy = selectedEnergy,
                            temperature = selectedTemperature
                        )
                        println("🔥 UPDATE DESCRIPTION CALLED")
                        onDismiss()  // 🔥 Теперь можно закрывать сразу - нет конфликта sheet'ов
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E0E0),
                        contentColor = Color(0xFF2B2929)
                    )
                ) {
                    Text(
                        text = "Сохранить",
                        fontSize = 16.sp,
                        fontFamily = CustomFont
                    )
                }
            }
        }
    }
