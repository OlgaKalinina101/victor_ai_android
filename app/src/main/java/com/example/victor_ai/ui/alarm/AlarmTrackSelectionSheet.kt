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

package com.example.victor_ai.ui.alarm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.data.network.Track
import com.example.victor_ai.ui.playlist.components.TrackItemWithCheckbox

/**
 * Sheet для выбора трека будильника - упрощенная версия PlaylistSheet
 * Без фильтров, без редактирования метаданных, без текущего проигрываемого трека вверху
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmTrackSelectionSheet(
    tracks: List<Track>,
    loading: Boolean,
    selectedTrackId: Int?,
    currentPlayingTrackId: Int?,
    isPlaying: Boolean,
    onPlayPause: (Int?) -> Unit,
    onSelectTrack: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val grayText = Color(0xFFE0E0E0)
    val barEmpty = Color(0xFF555555)
    val barFilled = Color(0xFFCCCCCC)

    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .nestedScroll(rememberNestedScrollInteropConnection()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "## ТРЕК:",
            fontSize = 20.sp,
            color = grayText,
            fontFamily = didactGothic,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Список треков
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
                strokeWidth = 2.dp,
                color = barFilled
            )
        } else if (tracks.isEmpty()) {
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
                items(tracks, key = { it.id }) { track ->
                    TrackItemWithCheckbox(
                        track = track,
                        isPlaying = currentPlayingTrackId == track.id && isPlaying,
                        isSelected = selectedTrackId == track.id,
                        onPlayPause = { onPlayPause(track.id) },
                        onSelectTrack = { onSelectTrack(track.id) },
                        grayText = grayText
                    )
                }
            }
        }
    }
}

