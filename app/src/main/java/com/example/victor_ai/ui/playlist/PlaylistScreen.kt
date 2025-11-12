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
import androidx.compose.ui.text.font.Font
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

    // Шрифт Didact Gothic для всей страницы
    val didactGothic = FontFamily(Font(R.font.didact_gothic))

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 Основной контент — markdown страница с выравниванием по левому верхнему краю
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (stats == null) {
                Text(
                    text = "Загружается статистика...",
                    color = Color(0xFFE0E0E0),
                    fontSize = 14.sp,
                    fontFamily = didactGothic
                )
            } else {
                // # СТАТИСТИКА НЕДЕЛИ (markdown h1)
                Text(
                    text = "# СТАТИСТИКА НЕДЕЛИ",
                    color = Color(0xFFE0E0E0),
                    fontSize = 24.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // ## Трек недели (markdown h2)
                Text(
                    text = "## Трек недели",
                    color = Color(0xFFB0B0B0),
                    fontSize = 18.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(8.dp))

                // > Название трека (markdown цитата, кликабельная)
                stats?.top_tracks?.firstOrNull()?.let { t ->
                    Text(
                        text = "> ${t.title}",
                        color = Color(0xFFE0E0E0),
                        fontSize = 16.sp,
                        fontFamily = didactGothic,
                        modifier = Modifier.clickable {
                            showPlaylistSheet = true
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "  ${t.plays} прослушиваний",
                        color = Color(0xFF888888),
                        fontSize = 14.sp,
                        fontFamily = didactGothic
                    )
                }

                Spacer(Modifier.height(24.dp))

                // --- (markdown разделитель)
                Text(
                    text = "---",
                    color = Color(0xFF606060),
                    fontSize = 14.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // Энергия: [значение]
                Text(
                    text = "Энергия: [${stats?.top_energy ?: "—"}]",
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp,
                    fontFamily = didactGothic
                )
                Spacer(Modifier.height(8.dp))

                // Температура: [значение]
                Text(
                    text = "Температура: [${stats?.top_temperature ?: "—"}]",
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // --- (markdown разделитель)
                Text(
                    text = "---",
                    color = Color(0xFF606060),
                    fontSize = 14.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // [ Запустить волну ] (markdown кнопка)
                Text(
                    text = "[ Запустить волну ]",
                    color = Color(0xFFCCCCCC),
                    fontSize = 14.sp,
                    fontFamily = didactGothic,
                    modifier = Modifier.clickable {
                        /* TODO: запуск волны по треку недели */
                    }
                )

                Spacer(Modifier.height(16.dp))

                // [ Выбери сам... ] + стрим логов на одном уровне
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "[ Выбери сам... ]",
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp,
                        fontFamily = didactGothic,
                        modifier = Modifier.clickable {
                            showAmbientStream = !showAmbientStream
                            if (showAmbientStream) viewModel.runPlaylistWave(manual = true)
                        }
                    )

                    // Стрим логов рядом с кнопкой
                    AnimatedVisibility(
                        visible = showAmbientStream,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(
                            text = typedText,
                            color = Color(0xFF666666),
                            fontSize = 14.sp,
                            fontFamily = didactGothic,
                            modifier = Modifier.padding(start = 16.dp)
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

