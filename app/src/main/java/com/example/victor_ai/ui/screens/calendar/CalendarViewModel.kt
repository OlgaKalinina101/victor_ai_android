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

package com.example.victor_ai.ui.screens.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.alarm.AlarmScheduler
import com.example.victor_ai.alarm.AlarmTimeCalculator
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.MusicApi
import com.example.victor_ai.data.network.getTracksPaged
import com.example.victor_ai.data.network.ReminderApi
import com.example.victor_ai.data.network.Track
import com.example.victor_ai.data.repository.AlarmModelData
import com.example.victor_ai.data.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel для экрана календаря с будильниками
 */
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    private val musicApi: MusicApi,
    private val reminderApi: ReminderApi,
    private val reminderRepository: com.example.victor_ai.data.repository.ReminderRepository
) : ViewModel() {

    // ==================== State ====================
    
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    // Данные будильников из репозитория
    val alarmData: StateFlow<AlarmModelData> = alarmRepository.alarmFlow
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = AlarmModelData(
                alarms = listOf(
                    com.example.victor_ai.data.repository.AlarmItem(
                        time = "Null",
                        repeatMode = "Один раз"
                    ),
                    com.example.victor_ai.data.repository.AlarmItem(
                        time = "Null",
                        repeatMode = "Будни"
                    ),
                    com.example.victor_ai.data.repository.AlarmItem(
                        time = "Null",
                        repeatMode = "Выходные"
                    )
                )
            )
        )

    // Выбранный трек для будильника
    val selectedTrackId: StateFlow<Int?> = alarmRepository.selectedTrackIdFlow
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        Log.d(TAG, "CalendarViewModel initialized")
    }

    /**
     * 🔐 Переинициализация для нового аккаунта.
     * Сбрасывает кешированные треки и перезагружает данные.
     */
    fun reinitialize() {
        Log.d(TAG, "🔄 reinitialize: сбрасываем треки и состояние")
        _uiState.value = CalendarUiState()
        // Данные будильников будут обновлены через alarmRepository.alarmFlow автоматически
    }

    // ==================== Date Selection ====================

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        closeAllDropdowns()
    }

    fun changeMonth(newMonth: LocalDate) {
        _uiState.update { it.copy(currentMonth = newMonth) }
    }

    // ==================== Dropdowns ====================

    fun toggleTimeDropdown(alarmIndex: Int) {
        _uiState.update { state ->
            val newAlarms = state.alarms.mapIndexed { index, alarm ->
                if (index == alarmIndex) {
                    alarm.copy(
                        timeExpanded = !alarm.timeExpanded,
                        repeatExpanded = false // закрываем другой dropdown
                    )
                } else {
                    alarm.copy(timeExpanded = false)
                }
            }
            state.copy(alarms = newAlarms)
        }
    }

    fun toggleRepeatDropdown(alarmIndex: Int) {
        _uiState.update { state ->
            val newAlarms = state.alarms.mapIndexed { index, alarm ->
                if (index == alarmIndex) {
                    alarm.copy(
                        repeatExpanded = !alarm.repeatExpanded,
                        timeExpanded = false // закрываем другой dropdown
                    )
                } else {
                    alarm.copy(repeatExpanded = false)
                }
            }
            state.copy(alarms = newAlarms)
        }
    }

    fun closeAllDropdowns() {
        _uiState.update { state ->
            val newAlarms = state.alarms.map { alarm ->
                alarm.copy(timeExpanded = false, repeatExpanded = false)
            }
            state.copy(alarms = newAlarms)
        }
    }

    fun closeTimeDropdown(alarmIndex: Int) {
        _uiState.update { state ->
            val newAlarms = state.alarms.mapIndexed { index, alarm ->
                if (index == alarmIndex) alarm.copy(timeExpanded = false) else alarm
            }
            state.copy(alarms = newAlarms)
        }
    }

    fun closeRepeatDropdown(alarmIndex: Int) {
        _uiState.update { state ->
            val newAlarms = state.alarms.mapIndexed { index, alarm ->
                if (index == alarmIndex) alarm.copy(repeatExpanded = false) else alarm
            }
            state.copy(alarms = newAlarms)
        }
    }

    // ==================== Alarm Updates ====================

    fun updateAlarm(alarmIndex: Int, time: String, repeatMode: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Updating alarm $alarmIndex: time=$time, repeatMode=$repeatMode")
                alarmRepository.updateAlarm(alarmIndex, time, repeatMode)

                // 🔔 Plan alarm using AlarmManager (system-like exact alarm)
                val alarmId = alarmIndex + 1
                if (time == "Null") {
                    alarmScheduler.cancel(alarmId)
                } else {
                    val localTime = AlarmTimeCalculator.parseTimeOrNull(time)
                    if (localTime != null) {
                        val triggerAt = AlarmTimeCalculator.computeNextTriggerMillis(localTime, repeatMode)
                        val trackId = selectedTrackId.value
                        alarmScheduler.scheduleAlarmClock(
                            alarmId = alarmId,
                            triggerAtMillis = triggerAt,
                            alarmTime = time,
                            alarmLabel = repeatMode,
                            trackId = trackId
                        )
                    } else {
                        Log.e(TAG, "Invalid alarm time format: $time")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating alarm", e)
                _uiState.update { it.copy(errorMessage = "Не удалось обновить будильник") }
            }
        }
    }

    // ==================== Track Selection ====================

    fun showTrackSelectionSheet() {
        _uiState.update { it.copy(showTrackSelectionSheet = true) }
        if (_uiState.value.tracks.isEmpty()) {
            loadTracks()
        }
    }

    fun hideTrackSelectionSheet() {
        _uiState.update { it.copy(showTrackSelectionSheet = false) }
    }

    // ==================== Recurring Reminders Sheet ====================

    fun showRecurringRemindersSheet() {
        _uiState.update { it.copy(showRecurringRemindersSheet = true) }
    }

    fun hideRecurringRemindersSheet() {
        _uiState.update { it.copy(showRecurringRemindersSheet = false) }
    }

    fun disableReminderRepeat(reminderId: String) {
        viewModelScope.launch {
            try {
                val response = reminderApi.setReminderRepeatWeekly(
                    body = com.example.victor_ai.data.network.ReminderRepeatWeeklyRequest(
                        reminder_id = reminderId,
                        repeat_weekly = false
                    )
                )
                if (response.isSuccessful) {
                    Log.d(TAG, "Reminder repeat disabled: $reminderId")
                } else {
                    Log.e(TAG, "Error disabling reminder repeat: ${response.code()}")
                    _uiState.update { it.copy(errorMessage = "Не удалось отключить повтор напоминания") }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error disabling reminder repeat", e)
                _uiState.update { it.copy(errorMessage = "Ошибка при отключении повтора напоминания") }
            }
        }
    }

    // ==================== Load Data ====================

    private fun loadTracks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingTracks = true, errorMessage = null) }
            try {
                val accountId = UserProvider.getCurrentUserId()
                val tracks = musicApi.getTracksPaged(accountId)
                _uiState.update { it.copy(tracks = tracks, isLoadingTracks = false) }
                Log.d(TAG, "Loaded ${tracks.size} tracks")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading tracks", e)
                _uiState.update { 
                    it.copy(
                        isLoadingTracks = false,
                        errorMessage = "Не удалось загрузить треки. Проверьте подключение к интернету."
                    ) 
                }
            }
        }
    }

    fun selectTrack(trackId: Int?) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Selecting track: $trackId")
                alarmRepository.selectTrack(trackId)
            } catch (e: Exception) {
                Log.e(TAG, "Error selecting track", e)
                _uiState.update { it.copy(errorMessage = "Не удалось выбрать трек") }
            }
        }
    }

    fun selectTrackAutomatically() {
        _uiState.update { it.copy(showThinking = true, typedText = "", errorMessage = null) }
        viewModelScope.launch {
            try {
                Log.d(TAG, "Auto-selecting track...")
                // Запускаем анимацию и запрос параллельно
                val animationJob = launch { animateTyping() }
                alarmRepository.selectTrackForYourself()
                // Дожидаемся конца анимации для лучшего UX
                animationJob.join()
            } catch (e: Exception) {
                Log.e(TAG, "Error auto-selecting track", e)
                _uiState.update { it.copy(errorMessage = "Не удалось подобрать трек автоматически") }
            } finally {
                _uiState.update { it.copy(showThinking = false, typedText = "") }
            }
        }
    }

    private suspend fun animateTyping() {
        val fullText = "> думаю..."
        fullText.forEachIndexed { index, _ ->
            kotlinx.coroutines.delay(50)
            _uiState.update { it.copy(typedText = fullText.take(index + 1)) }
        }
    }

    // ==================== Audio Player State ====================

    fun setCurrentPlayingTrack(trackId: Int?) {
        _uiState.update { it.copy(currentPlayingTrackId = trackId) }
    }

    fun setIsPlaying(playing: Boolean) {
        _uiState.update { it.copy(isPlaying = playing) }
    }
    
    // ==================== Error Handling ====================
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    // ==================== Reminders ====================
    
    /**
     * Загружает напоминания из репозитория для текущего аккаунта
     * Возвращает в формате Map<String, List<ReminderDto>> для совместимости
     */
    suspend fun loadReminders(accountId: String): Map<String, List<com.example.victor_ai.data.network.ReminderDto>> {
        try {
            // Синхронизируем с бэкендом
            reminderRepository.syncWithBackend(accountId)
                .onFailure { e ->
                    Log.w(TAG, "⚠️ Синхронизация не удалась, используем локальные данные: ${e.message}")
                }

            // Получаем данные из локальной БД
            val entities = reminderRepository.getReminders().stateIn(viewModelScope).value

            // Группируем по дате для совместимости с существующим форматом
            val result = mutableMapOf<String, MutableList<com.example.victor_ai.data.network.ReminderDto>>()
            entities.forEach { entity ->
                val dto = com.example.victor_ai.data.network.ReminderDto(
                    id = entity.id,
                    text = entity.text,
                    date = entity.date,
                    repeatWeekly = entity.repeatWeekly,
                    dayOfWeek = entity.dayOfWeek
                )
                // Извлекаем только дату из timestamp
                val dateKey = entity.date?.substringBefore("T") ?: "no_date"
                result.getOrPut(dateKey) { mutableListOf() }.add(dto)
            }
            return result
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка загрузки напоминалок", e)
            return emptyMap()
        }
    }

    companion object {
        private const val TAG = "CalendarViewModel"
    }
}

/**
 * UI State для экрана календаря
 */
data class CalendarUiState(
    // Calendar
    val currentMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val selectedDate: LocalDate? = null,

    // Alarms
    val alarms: List<AlarmUiState> = listOf(
        AlarmUiState(),
        AlarmUiState(),
        AlarmUiState()
    ),

    // Track Selection
    val showTrackSelectionSheet: Boolean = false,
    val tracks: List<Track> = emptyList(),
    val isLoadingTracks: Boolean = false,
    val currentPlayingTrackId: Int? = null,
    val isPlaying: Boolean = false,

    // Recurring Reminders Sheet
    val showRecurringRemindersSheet: Boolean = false,

    // Thinking animation
    val showThinking: Boolean = false,
    val typedText: String = "",
    
    // Error handling
    val errorMessage: String? = null
)

/**
 * UI State для одного будильника
 */
data class AlarmUiState(
    val timeExpanded: Boolean = false,
    val repeatExpanded: Boolean = false
)

