package com.example.victor_ai.ui.playlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import com.example.victor_ai.domain.model.Track

/**
 * Компактный элемент списка треков
 */
@Composable
fun TrackItemCompact(
    track: Track,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onClick: () -> Unit,
    grayText: Color
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))

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
        }
    }
}
