package com.example.victor_ai.ui.playlist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.domain.model.Track
import com.example.victor_ai.ui.playlist.PlaylistViewModel

/**
 * Форма для редактирования метаданных трека (энергия и температура)
 */
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Кнопка "Назад"
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

        // Заголовок
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
                    temperatureOptions.forEach { option ->
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
                                temperatureExpanded = false
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
                    onDismiss()
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
