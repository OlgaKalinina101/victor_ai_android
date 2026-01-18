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
import com.example.victor_ai.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.ui.playlist.components.EditTrackMetadataSheet
import com.example.victor_ai.data.network.PlaylistMomentOut


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    onBackClick: () -> Unit
) {
    // 🎯 ИСПРАВЛЕНО: Единое состояние UI (вместо 8 отдельных StateFlow)
    val uiState by viewModel.uiState.collectAsState()
    
    // 🔥 UI-локальные состояния (не влияют на бизнес-логику)
    var showPlaylistSheet by rememberSaveable { mutableStateOf(false) }
    var editingTrackId by rememberSaveable { mutableStateOf<Int?>(null) }
    var showAmbientStream by rememberSaveable { mutableStateOf(false) }

    // 🔥 Получаем сам трек из списка по ID с remember для оптимизации
    val editingTrack = remember(editingTrackId, uiState.tracks) {
        editingTrackId?.let { id -> uiState.tracks.firstOrNull { it.id == id } }
    }

    // 🔥 Автоматически защищаем плейлист когда открыто редактирование
    val keepPlaylistOpen = editingTrackId != null

    val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // 🔥 Обновляем статистику при открытии экрана
    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }

    // 🎵 Состояние для анимации печати из streaming логов
    var typedText by remember { mutableStateOf("") }

    // 🧾 История логов для шторки (накапливаем строки, чтобы был именно "стрим логов")
    val streamHistory = remember { mutableStateListOf<String>() }
    var previousStreamingLog by remember { mutableStateOf<String?>(null) }

    // Запоминаем предыдущий ID трека для отслеживания запуска
    var previousTrackId by remember { mutableStateOf<Int?>(null) }

    // 🔥 Синхронизируем состояние плеера при открытии плейлиста
    LaunchedEffect(showPlaylistSheet) {
        if (showPlaylistSheet) {
            Log.d("PlaylistScreen", "🔄 showPlaylistSheet=true, calling syncPlayerState()")
            Log.d("PlaylistScreen", "🔄 currentPlayingTrackId=${uiState.currentPlayingTrackId}, isPlaying=${uiState.isPlaying}")
            viewModel.syncPlayerState()
        }
    }

    // 🎵 НОВОЕ: Анимация печати streaming логов
    LaunchedEffect(uiState.streamingLog) {
        val newLog = uiState.streamingLog

        // Перед началом печати нового лога — кладём предыдущий в историю
        if (!previousStreamingLog.isNullOrBlank() && previousStreamingLog != newLog) {
            streamHistory.add(previousStreamingLog!!)
        }

        if (newLog.isNotEmpty()) {
            // Показываем анимацию
            showAmbientStream = true
            typedText = ""
            
            // Печатаем посимвольно
            newLog.forEachIndexed { index, _ ->
                kotlinx.coroutines.delay(50) // 50мс между символами
                typedText = newLog.take(index + 1)
            }
        } else {
            // Лог пустой: фиксируем последний лог в историю, но не гасим глазки,
            // если волна ещё грузится (глазки+... должны оставаться как индикатор).
            if (!previousStreamingLog.isNullOrBlank() && streamHistory.lastOrNull() != previousStreamingLog) {
                streamHistory.add(previousStreamingLog!!)
            }
            if (!uiState.isWaveLoading) {
                showAmbientStream = false
            }
            typedText = ""
        }

        previousStreamingLog = newLog
    }

    // Отслеживание запуска трека для скрытия анимации
    LaunchedEffect(uiState.currentPlayingTrackId) {
        if (previousTrackId != uiState.currentPlayingTrackId && uiState.currentPlayingTrackId != null) {
            // Трек изменился.
            // ВАЖНО: если это происходит во время streaming-wave, не сбрасываем typedText,
            // иначе последний stage (часто stage_3) "мигает": исчезает на старте трека и потом возвращается.
            if (!uiState.isWaveLoading) {
                showAmbientStream = false
                typedText = ""
            }
        }
        previousTrackId = uiState.currentPlayingTrackId
    }

    // 🎯 Синхронизация анимации с загрузкой wave
    LaunchedEffect(uiState.isWaveLoading) {
        if (uiState.isWaveLoading) {
            // Начинается загрузка - показываем анимацию
            showAmbientStream = true
            // Сброс истории/состояния под новую волну
            streamHistory.clear()
            previousStreamingLog = null
            typedText = ""
        } else if (showAmbientStream) {
            // Загрузка завершена - скрываем анимацию
            showAmbientStream = false
            typedText = ""
        }
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
            // 🔥 НОВОЕ: Показываем ошибки пользователю
            uiState.error?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x33FF5252)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚠️ ",
                            fontSize = 18.sp
                        )
                        Text(
                            text = when (error) {
                                is ErrorState.LoadingTracksError -> "Ошибка загрузки: ${error.message}"
                                is ErrorState.PlaybackError -> "Ошибка воспроизведения: ${error.message}"
                                is ErrorState.NetworkError -> "Сетевая ошибка: ${error.message}"
                                is ErrorState.WaveError -> "Ошибка волны: ${error.message}"
                            },
                            color = Color(0xFFFFCDD2),
                            fontSize = 14.sp,
                            fontFamily = didactGothic,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "✕",
                            color = Color(0xFFFFCDD2),
                            fontSize = 18.sp,
                            modifier = Modifier.clickable {
                                viewModel.clearError()
                            }
                        )
                    }
                }
            }
            
            if (uiState.stats == null) {
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
                    fontSize = 28.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // ## Трек недели (markdown h2)
                Text(
                    text = "## Трек недели",
                    color = Color(0xFFB0B0B0),
                    fontSize = 22.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(10.dp))

                // > Название трека (markdown цитата, кликабельная)
                val topTrack = uiState.stats?.top_tracks?.firstOrNull()
                if (topTrack != null) {
                    Text(
                        text = "> ${topTrack.title}",
                        color = Color(0xFFE0E0E0),
                        fontSize = 20.sp,
                        fontFamily = didactGothic,
                        modifier = Modifier.clickable {
                            showPlaylistSheet = true
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "  ${topTrack.plays} прослушиваний",
                        color = Color(0xFF888888),
                        fontSize = 14.sp,
                        fontFamily = didactGothic
                    )
                } else {
                    // Если трека нет - показываем "Null" и даём открыть плейлист
                    Text(
                        text = "> Null",
                        color = Color(0xFF888888),
                        fontSize = 20.sp,
                        fontFamily = didactGothic,
                        modifier = Modifier.clickable {
                            showPlaylistSheet = true
                        }
                    )
                }

                Spacer(Modifier.height(24.dp))

                // --- (markdown разделитель)
                Text(
                    text = "---",
                    color = Color(0xFF606060),
                    fontSize = 18.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // Энергия: [значение]
                Text(
                    text = "Энергия: [${uiState.stats?.top_energy ?: "—"}]",
                    color = Color(0xFFB0B0B0),
                    fontSize = 18.sp,
                    fontFamily = didactGothic
                )
                Spacer(Modifier.height(8.dp))

                // Температура: [значение]
                Text(
                    text = "Температура: [${uiState.stats?.top_temperature ?: "—"}]",
                    color = Color(0xFFB0B0B0),
                    fontSize = 18.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // --- (markdown разделитель)
                Text(
                    text = "---",
                    color = Color(0xFF606060),
                    fontSize = 18.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // [ Запустить волну ] (markdown кнопка)
                Text(
                    text = "[ Запустить волну ]",
                    color = Color(0xFFCCCCCC),
                    fontSize = 18.sp,
                    fontFamily = didactGothic,
                    modifier = Modifier.clickable {
                        if (!uiState.isWaveLoading) {
                            // Запускаем только если не идет загрузка
                            viewModel.runWave(
                                energy = uiState.stats?.top_energy,
                                temperature = uiState.stats?.top_temperature
                            )
                        }
                    }
                )

                Spacer(Modifier.height(20.dp))

                // [ Выбери сам... ] + стрим логов на одном уровне
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    // 🔥 При многострочном streaming-тексте выравниваем по верху,
                    // чтобы текст мог уходить вниз и не "врезался" в глазки.
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "[ Выбери сам... ]",
                        color = Color(0xFFCCCCCC),
                        fontSize = 18.sp,
                        fontFamily = didactGothic,
                        modifier = Modifier.clickable {
                            if (!uiState.isWaveLoading) {
                                // 🎵 Запускаем STREAMING версию
                                viewModel.runAssistantWaveStreaming(manual = true)
                            }
                        }
                    )

                    // отступ между кнопкой и глазками: ставь 0.dp если хочешь вообще вплотную
                    Spacer(modifier = Modifier.width(20.dp))

                    AmbientThinkingRow(
                        show = showAmbientStream,
                        // 🔥 На экране оставляем только глазки + "..." (без стрима текста)
                        typedText = "",
                        fontFamily = didactGothic,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                enabled = uiState.isWaveLoading || typedText.isNotEmpty() || streamHistory.isNotEmpty(),
                                onClick = { viewModel.showStreamingLogSheet() }
                            )
                    )
                }

                Spacer(Modifier.height(24.dp))

                // --- (markdown разделитель)
                Text(
                    text = "---",
                    color = Color(0xFF606060),
                    fontSize = 18.sp,
                    fontFamily = didactGothic
                )

                Spacer(Modifier.height(24.dp))

                // (Посмотреть архив записей с "Выбери сам")
                Icon(
                    imageVector = Icons.Filled.Book,
                    contentDescription = "Архив записей",
                    tint = Color(0xFF606060),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { viewModel.showPlaylistMomentsSheet() }
                )
            }
        }
    }

    // ==================== MODAL BOTTOM SHEET: STREAMING LOGS ====================
    if (uiState.showStreamingLogSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { viewModel.hideStreamingLogSheet() },
            sheetState = sheetState,
            containerColor = Color(0xFF2B2929),
            modifier = Modifier.heightIn(max = screenHeight * 7 / 8)
        ) {
            StreamingLogSheet(
                history = streamHistory.toList(),
                currentTypedText = typedText,
                isWaveLoading = uiState.isWaveLoading,
                fontFamily = didactGothic,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }

    // ==================== MODAL BOTTOM SHEET: PLAYLIST MOMENTS (HISTORY) ====================
    if (uiState.showPlaylistMomentsSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = { viewModel.hidePlaylistMomentsSheet() },
            sheetState = sheetState,
            containerColor = Color(0xFF2B2929),
            modifier = Modifier.heightIn(max = screenHeight * 7 / 8)
        ) {
            PlaylistMomentsSheet(
                moments = uiState.playlistMoments,
                isLoading = uiState.isPlaylistMomentsLoading,
                error = uiState.playlistMomentsError,
                fontFamily = didactGothic,
                onPlayTrack = { trackId -> viewModel.playTrack(trackId) },
                onReload = { viewModel.loadPlaylistMoments(limit = 20) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }

    // Плейлист
    if (showPlaylistSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                Log.d("PlaylistScreen", "🔥 DISMISS REQUEST: keepPlaylistOpen=$keepPlaylistOpen, editingTrackId=$editingTrackId")
                if (!keepPlaylistOpen) {
                    showPlaylistSheet = false
                    Log.d("PlaylistScreen", "🔥 PLAYLIST CLOSED")
                } else {
                    Log.d("PlaylistScreen", "🔥 PLAYLIST DISMISS BLOCKED")
                }
            },
            sheetState = playlistSheetState,
            containerColor = Color(0xFF2B2929),
            modifier = Modifier.heightIn(max = screenHeight * 7 / 8)
        ) {
            // 🔥 Box для overlay редактирования поверх плейлиста
            Box(modifier = Modifier.fillMaxSize()) {
                PlaylistSheet(
                    uiState = uiState,
                    onPlayPause = { trackId ->
                        if (trackId == null) return@PlaylistSheet
                        if (uiState.currentPlayingTrackId == trackId) {
                            if (uiState.isPlaying) viewModel.pauseTrack() else viewModel.resumeTrack()
                        } else {
                            viewModel.playTrack(trackId)
                        }
                    },
                    onSeek = { position -> viewModel.seekTo(position) },
                    onEnergyFilterChange = { energy -> viewModel.updateEnergyFilter(energy) },
                    onTemperatureFilterChange = { temp -> viewModel.updateTemperatureFilter(temp) },
                    onSortByChange = { sortBy -> viewModel.updateSortBy(sortBy) },
                    onUpdateDescription = { trackId, energy, temp ->
                        viewModel.updateDescription(trackId, energy, temp)
                    },
                    onCacheTrack = { track -> viewModel.cacheTrack(track) },
                    onRemoveCachedTrack = { trackId -> viewModel.removeCachedTrack(trackId) },
                    onEditTrack = { track ->
                        Log.d("PlaylistScreen", "🔥 EDIT TRACK: track=$track")
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
                            onUpdateDescription = { trackId, energy, temp ->
                                viewModel.updateDescription(trackId, energy, temp)
                            },
                            onDismiss = {
                                Log.d("PlaylistScreen", "🔥 EDIT SHEET ON DISMISS CALLED")
                                editingTrackId = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StreamingLogSheet(
    history: List<String>,
    currentTypedText: String,
    isWaveLoading: Boolean,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "/* Thinking */",
            color = Color(0xFFA6A6A6),
            fontFamily = fontFamily,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val lines = buildList {
            addAll(history.filter { it.isNotBlank() })
            if (currentTypedText.isNotBlank()) add(currentTypedText)
        }

        if (lines.isEmpty()) {
            Text(
                text = if (isWaveLoading) "- ...\n- (ищу важное)" else "- (пока пусто)",
                color = Color(0xFFA6A6A6),
                fontFamily = fontFamily,
                fontSize = 16.sp
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                lines.forEach { line ->
                    Text(
                        text = line,
                        color = Color(0xFFB0B0B0),
                        fontFamily = fontFamily,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaylistMomentsSheet(
    moments: List<PlaylistMomentOut>,
    isLoading: Boolean,
    error: String?,
    fontFamily: FontFamily,
    onPlayTrack: (Int?) -> Unit,
    onReload: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "## МОМЕНТЫ",
            color = Color(0xFFE0E0E0),
            fontFamily = fontFamily,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFA6A6A6),
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Загружаю...",
                    color = Color(0xFFA6A6A6),
                    fontFamily = fontFamily,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        error?.let { msg ->
            Text(
                text = "Ошибка: $msg",
                color = Color(0xFFFF8A80),
                fontFamily = fontFamily,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "[ обновить ]",
                color = Color(0xFFCCCCCC),
                fontFamily = fontFamily,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { onReload() }
                    .padding(bottom = 16.dp)
            )
        }

        if (!isLoading && error == null && moments.isEmpty()) {
            Text(
                text = "- (пока пусто)",
                color = Color(0xFFA6A6A6),
                fontFamily = fontFamily,
                fontSize = 16.sp
            )
            return
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            moments.forEach { moment ->
                PlaylistMomentCard(
                    moment = moment,
                    fontFamily = fontFamily,
                    onPlayTrack = onPlayTrack
                )
                Spacer(Modifier.height(14.dp))
            }
        }
    }
}

@Composable
private fun PlaylistMomentCard(
    moment: PlaylistMomentOut,
    fontFamily: FontFamily,
    onPlayTrack: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF242323)),
        border = BorderStroke(1.dp, Color(0xFF3A3939)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            val stageText = buildString {
                fun addStage(label: String, value: String?) {
                    val v = value?.trim().orEmpty()
                    if (v.isNotEmpty()) {
                        append("- ")
                        append(v)
                    } else {
                        append("- ")
                        append("($label: пусто)")
                    }
                }

                addStage("stage_1", moment.stage1Text)
                append("\n\n")
                addStage("stage_2", moment.stage2Text)
                append("\n\n")
                addStage("stage_3", moment.stage3Text)
            }

            Text(
                text = stageText,
                color = Color(0xFFB0B0B0),
                fontFamily = fontFamily,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(12.dp))

            val trackId = moment.trackId ?: moment.track?.id
            val trackTitle = moment.track?.title ?: "Track #${trackId ?: "—"}"
            val trackArtist = moment.track?.artist

            Column(horizontalAlignment = Alignment.End) {
                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = "Запустить трек",
                    tint = Color(0xFFA6A6A6),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(enabled = trackId != null) { onPlayTrack(trackId) }
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = trackTitle,
                    color = Color(0xFFCCCCCC),
                    fontFamily = fontFamily,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .width(140.dp)
                        .clickable(enabled = trackId != null) { onPlayTrack(trackId) }
                )
                if (!trackArtist.isNullOrBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = trackArtist,
                        color = Color(0xFF808080),
                        fontFamily = fontFamily,
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(140.dp)
                    )
                }
            }
        }
    }
}

