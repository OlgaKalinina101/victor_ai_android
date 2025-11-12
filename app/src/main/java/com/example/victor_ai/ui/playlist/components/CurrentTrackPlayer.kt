package com.example.victor_ai.ui.playlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.victor_ai.ui.playlist.components.formatDuration

/**
 * Компонент для отображения текущего играющего трека с плеером
 */
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
    val didactGothic = FontFamily(Font(R.font.didact_gothic))

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
                        fontSize = 12.sp,
                        fontFamily = didactGothic
                    )
                    Text(
                        text = formatDuration(track.duration),
                        color = grayText.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontFamily = didactGothic
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
