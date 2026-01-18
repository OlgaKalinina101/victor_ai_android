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

package com.example.victor_ai.ui.playlist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import com.example.victor_ai.data.network.Track

/**
 * Компактный элемент списка треков с галочкой для выбора будильника
 */
@Composable
fun TrackItemWithCheckbox(
    track: Track,
    isPlaying: Boolean,
    isSelected: Boolean,
    onPlayPause: () -> Unit,
    onSelectTrack: () -> Unit,
    grayText: Color
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val barEmpty = Color(0xFF555555)
    val checkColor = Color(0xFFCCCCCC)

    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    fontFamily = didactGothic,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.artist,
                    color = grayText.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    fontFamily = didactGothic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Длительность
            Text(
                text = formatDuration(track.duration),
                color = grayText.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontFamily = didactGothic
            )

            // Галочка/пустой квадратик
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        border = BorderStroke(2.dp, if (isSelected) checkColor else barEmpty),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable(onClick = onSelectTrack),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = checkColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

