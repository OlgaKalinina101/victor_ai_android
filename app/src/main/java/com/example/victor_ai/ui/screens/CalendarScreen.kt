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

package com.example.victor_ai.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.victor_ai.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.victor_ai.data.network.ReminderDto
import com.example.victor_ai.BuildConfig
import com.example.victor_ai.logic.AudioPlayer
import com.example.victor_ai.ui.alarm.AlarmTrackSelectionSheet
import com.example.victor_ai.ui.screens.alarm.AlarmPickerSection
import com.example.victor_ai.ui.screens.alarm.TrackSection
import com.example.victor_ai.ui.screens.calendar.*
import com.example.victor_ai.ui.screens.reminders.ReminderList
import com.example.victor_ai.ui.screens.Reminder
import java.time.LocalDate

/**
 * Главный экран календаря с будильниками и напоминаниями
 */
@Composable
fun CalendarScreenWithReminders(
    accountId: String = "",  // 🔐 Для reinitialize при смене аккаунта
    viewModel: CalendarViewModel = hiltViewModel()
) {
    var reminders by remember { mutableStateOf<Map<LocalDate, List<CalendarReminder>>>(emptyMap()) }
    var recurringReminders by remember { mutableStateOf<List<Reminder>>(emptyList()) }
    var isLoaded by remember { mutableStateOf(false) }

    // 🔐 Перезагрузка данных при смене аккаунта
    LaunchedEffect(accountId) {
        if (accountId.isNotEmpty()) {
            Log.d("CalendarScreen", "🔄 reload reminders for accountId=$accountId")
            viewModel.reinitialize()
            // Сбрасываем старые данные
            reminders = emptyMap()
            recurringReminders = emptyList()
            isLoaded = false
            // Загружаем новые
            try {
                val apiData = viewModel.loadReminders(accountId)
                reminders = groupCalendarReminders(apiData)
                recurringReminders = extractRecurringReminders(apiData)
                isLoaded = true
            } catch (e: Exception) {
                Log.e("CalendarScreen", "Error loading reminders for $accountId", e)
                isLoaded = true  // показываем UI даже при ошибке
            }
        }
    }

    // Первичная загрузка (если accountId ещё пустой при старте)
    LaunchedEffect(Unit) {
        if (!isLoaded && accountId.isEmpty()) {
            try {
                val currentAccountId = com.example.victor_ai.auth.UserProvider.getCurrentUserId()
                val apiData = viewModel.loadReminders(currentAccountId)
                reminders = groupCalendarReminders(apiData)
                recurringReminders = extractRecurringReminders(apiData)
                isLoaded = true
            } catch (e: Exception) {
                Log.e("CalendarScreen", "Error loading reminders", e)
                isLoaded = true
            }
        }
    }

    if (isLoaded) {
        CalendarScreen(
            reminders = reminders,
            recurringReminders = recurringReminders,
            viewModel = viewModel
        )
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFA6A6A6))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    reminders: Map<LocalDate, List<CalendarReminder>>,
    recurringReminders: List<Reminder> = emptyList(),
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val alarmData by viewModel.alarmData.collectAsState()
    val selectedTrackId by viewModel.selectedTrackId.collectAsState()

    // 🔥 AudioPlayer для прослушивания треков
    val audioPlayer = remember { AudioPlayer(context.applicationContext) }

    // 🔥 Очистка AudioPlayer при уничтожении Composable
    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
            Log.d("CalendarScreen", "AudioPlayer released")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2929))
            .padding(horizontal = 16.dp)
    ) {
        // -------------------- ВЕРХ: КАЛЕНДАРЬ --------------------
        CalendarHeader(
            currentMonth = uiState.currentMonth,
            onMonthChange = { viewModel.changeMonth(it) }
        )

        Spacer(Modifier.height(16.dp))

        CalendarGrid(
            year = uiState.currentMonth.year,
            month = uiState.currentMonth.monthValue,
            reminders = reminders,
            onDateClick = { date -> viewModel.selectDate(date) }
        )

        Spacer(Modifier.height(30.dp))

        // -------------------- SWIPEABLE: TODO ↔ БУДИЛЬНИКИ --------------------
        var showTodoMode by remember { mutableStateOf(true) }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        // Свайп влево → будильники, свайп вправо → TODO
                        if (dragAmount < -50 && showTodoMode) {
                            showTodoMode = false
                        } else if (dragAmount > 50 && !showTodoMode) {
                            showTodoMode = true
                        }
                    }
                }
        ) {
            if (showTodoMode) {
                // -------------------- РЕЖИМ: TODO --------------------
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        uiState.selectedDate?.let { date ->
                            ReminderList(
                                reminders = reminders[date].orEmpty(),
                                selectedDate = date,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    RecurringRemindersSection(
                        onShowSheet = { viewModel.showRecurringRemindersSheet() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                // -------------------- РЕЖИМ: БУДИЛЬНИКИ --------------------
                Column {
                    AlarmPickerSection(
                        alarms = alarmData.alarms,
                        uiStates = uiState.alarms,
                        onTimeClick = { index -> viewModel.toggleTimeDropdown(index) },
                        onRepeatClick = { index -> viewModel.toggleRepeatDropdown(index) },
                        onTimeSelected = { index, time ->
                            viewModel.closeTimeDropdown(index)
                            viewModel.updateAlarm(index, time, alarmData.alarms[index].repeatMode)
                        },
                        onRepeatSelected = { index, mode ->
                            viewModel.closeRepeatDropdown(index)
                            viewModel.updateAlarm(index, alarmData.alarms[index].time, mode)
                        },
                        onTimeDismiss = { index -> viewModel.closeTimeDropdown(index) },
                        onRepeatDismiss = { index -> viewModel.closeRepeatDropdown(index) }
                    )
                    
                    Spacer(Modifier.height(8.dp))
                    
                    // -------------------- ВЫБОР ТРЕКА --------------------
                    TrackSection(
                        onSelectTrackManually = { viewModel.showTrackSelectionSheet() },
                        onSelectTrackAutomatically = { viewModel.selectTrackAutomatically() },
                        showThinking = uiState.showThinking,
                        typedText = uiState.typedText
                    )
                }
            }
        }
    }

    // ==================== MODAL BOTTOM SHEET ====================
    if (uiState.showTrackSelectionSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        ModalBottomSheet(
            onDismissRequest = { viewModel.hideTrackSelectionSheet() },
            sheetState = sheetState,
            containerColor = Color(0xFF2B2929),
            modifier = Modifier.heightIn(max = screenHeight * 7 / 8)
        ) {
            AlarmTrackSelectionSheet(
                tracks = uiState.tracks,
                loading = uiState.isLoadingTracks,
                selectedTrackId = selectedTrackId,
                currentPlayingTrackId = uiState.currentPlayingTrackId,
                isPlaying = uiState.isPlaying,
                onPlayPause = { trackId ->
                    if (trackId == null) return@AlarmTrackSelectionSheet

                    if (uiState.currentPlayingTrackId == trackId) {
                        if (uiState.isPlaying) {
                            audioPlayer.pause()
                            viewModel.setIsPlaying(false)
                        } else {
                            audioPlayer.resume()
                            viewModel.setIsPlaying(true)
                        }
                    } else {
                        val track = uiState.tracks.firstOrNull { it.id == trackId }
                        if (track != null) {
                            val streamUrl = "${BuildConfig.BASE_URL.trimEnd('/')}/tracks/$trackId?account_id=${com.example.victor_ai.auth.UserProvider.getCurrentUserId()}"
                            audioPlayer.updateTrackMetadata(
                                title = track.title,
                                artist = track.artist,
                                duration = (track.duration * 1000).toLong()
                            )
                            audioPlayer.playFromUrl(streamUrl)
                            viewModel.setCurrentPlayingTrack(trackId)
                            viewModel.setIsPlaying(true)
                        }
                    }
                },
                onSelectTrack = { trackId ->
                    viewModel.selectTrack(trackId)
                }
            )
        }
    }

    // ==================== MODAL BOTTOM SHEET: RECURRING REMINDERS ====================
    if (uiState.showRecurringRemindersSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        ModalBottomSheet(
            onDismissRequest = { viewModel.hideRecurringRemindersSheet() },
            sheetState = sheetState,
            containerColor = Color(0xFF2B2929),
            modifier = Modifier.heightIn(max = screenHeight * 7 / 8)
        ) {
            RecurringRemindersSheet(
                reminders = recurringReminders,
                onDisableRepeat = { reminderId ->
                    viewModel.disableReminderRepeat(reminderId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }
}

/**
 * Секция с постоянными (еженедельными) напоминаниями
 */
@Composable
private fun RecurringRemindersSection(
    onShowSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        RecurringRemindersButton(onClick = onShowSheet)
    }
}

/**
 * Кнопка "Все постоянные напоминалки"
 */
@Composable
private fun RecurringRemindersButton(onClick: () -> Unit) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    
    Text(
        text = "[Все постоянные напоминалки]",
        color = Color(0xFFA6A6A6),
        fontFamily = didactGothic,
        fontSize = 16.sp,
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = null,  // убираем ripple эффект
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * Содержимое модальной шторки с постоянными напоминаниями
 */
@Composable
private fun RecurringRemindersSheet(
    reminders: List<Reminder>,
    onDisableRepeat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    
    Column(modifier = modifier) {
        // Заголовок
        Text(
            text = "/* Постоянные напоминалки */",
            color = Color(0xFFFFD700),
            fontFamily = didactGothic,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Список или пустое состояние
        if (reminders.isEmpty()) {
            Text(
                text = "- Нет постоянных напоминалок",
                color = Color(0xFFA6A6A6),
                fontFamily = didactGothic,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            // Группируем по дням недели
            val groupedByDay = reminders.groupBy { it.dayOfWeek }
                .toSortedMap(compareBy { it?.value ?: 8 })
            
            groupedByDay.forEach { (dayOfWeek, dayReminders) ->
                RecurringReminderGroup(
                    dayOfWeek = dayOfWeek,
                    reminders = dayReminders,
                    onDisableRepeat = onDisableRepeat
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

/**
 * Группа напоминаний для одного дня недели
 */
@Composable
private fun RecurringReminderGroup(
    dayOfWeek: java.time.DayOfWeek?,
    reminders: List<Reminder>,
    onDisableRepeat: (String) -> Unit
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    
    val dayText = dayOfWeek?.let {
        when (it) {
            java.time.DayOfWeek.MONDAY -> "MONDAY"
            java.time.DayOfWeek.TUESDAY -> "TUESDAY"
            java.time.DayOfWeek.WEDNESDAY -> "WEDNESDAY"
            java.time.DayOfWeek.THURSDAY -> "THURSDAY"
            java.time.DayOfWeek.FRIDAY -> "FRIDAY"
            java.time.DayOfWeek.SATURDAY -> "SATURDAY"
            java.time.DayOfWeek.SUNDAY -> "SUNDAY"
        }
    } ?: "WEEKLY"
    
    Column {
        // Заголовок дня недели
        Text(
            text = "$dayText:",
            color = Color(0xFFA6A6A6),
            fontFamily = didactGothic,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        // Список напоминаний для этого дня
        reminders.forEach { reminder ->
            RecurringReminderItem(
                reminder = reminder,
                onDisableRepeat = { onDisableRepeat(reminder.id) }
            )
        }
    }
}

/**
 * Элемент списка постоянного напоминания
 */
@Composable
private fun RecurringReminderItem(
    reminder: Reminder,
    onDisableRepeat: () -> Unit
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    
    val timeText = reminder.time?.let {
        String.format("%02d:%02d", it.hour, it.minute)
    } ?: "??:??"
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "  ${reminder.text} ($timeText)",
            color = Color(0xFFA6A6A6),
            fontFamily = didactGothic,
            fontSize = 16.sp,
            lineHeight = 26.sp,
            modifier = Modifier.weight(1f)
        )
        
        // Крестик для отключения повтора
        Text(
            text = "×",
            color = Color(0xFFA6A6A6),
            fontFamily = didactGothic,
            fontSize = 24.sp,
            modifier = Modifier
                .clickable(
                    onClick = onDisableRepeat,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(horizontal = 8.dp)
        )
    }
}

/**
 * Группирует напоминания по датам для календаря
 * (приватная функция, используется только в CalendarScreen)
 */
private fun groupCalendarReminders(apiReminders: Map<String, List<ReminderDto>>): Map<LocalDate, List<CalendarReminder>> {
    return apiReminders.entries.associate { (dateStr, reminderDtos) ->
        val date = try {
            LocalDate.parse(dateStr)
        } catch (e: Exception) {
            Log.e("CalendarScreen", "Invalid date format: $dateStr", e)
            return@associate null to emptyList<CalendarReminder>()
        }

        val reminders = reminderDtos.map { dto ->
            // Извлекаем время из поля date (которое содержит дату и время в ISO-формате с таймзоной)
            val timeStr = try {
                dto.date?.let { 
                    val dateTime = try {
                        java.time.OffsetDateTime.parse(it).toLocalTime()
                    } catch (e: Exception) {
                        // Fallback на LocalDateTime если без таймзоны
                        java.time.LocalDateTime.parse(it).toLocalTime()
                    }
                    String.format("%02d:%02d", dateTime.hour, dateTime.minute)
                } ?: "??:??"
            } catch (e: Exception) {
                Log.e("CalendarScreen", "Error parsing time from: ${dto.date}", e)
                "??:??"
            }
            
            CalendarReminder(
                text = dto.text,
                time = timeStr
            )
        }

        date to reminders
    }.mapNotNull { (key, value) -> 
        key?.let { it to value } 
    }.toMap()
}

/**
 * Извлекает постоянные (еженедельные) напоминания из API данных
 */
private fun extractRecurringReminders(apiReminders: Map<String, List<ReminderDto>>): List<Reminder> {
    val recurringList = mutableListOf<Reminder>()
    
    apiReminders.values.flatten().forEach { dto ->
        if (dto.repeatWeekly) {
            val parsedDateTime = dto.date?.let {
                try {
                    // Пробуем парсить с таймзоной
                    try {
                        java.time.OffsetDateTime.parse(it).toLocalDateTime()
                    } catch (e: Exception) {
                        // Fallback на LocalDateTime если без таймзоны
                        java.time.LocalDateTime.parse(it)
                    }
                } catch (e: Exception) {
                    Log.e("CalendarScreen", "Error parsing datetime: $it", e)
                    null
                }
            }
            
            recurringList.add(
                Reminder(
                    id = dto.id,
                    text = dto.text,
                    date = parsedDateTime?.toLocalDate(),
                    time = parsedDateTime?.toLocalTime(),
                    repeatWeekly = dto.repeatWeekly,
                    dayOfWeek = dto.dayOfWeek?.let { java.time.DayOfWeek.valueOf(it) }
                )
            )
        }
    }
    
    return recurringList.sortedWith(compareBy({ it.dayOfWeek?.value ?: 8 }, { it.time }))
}

