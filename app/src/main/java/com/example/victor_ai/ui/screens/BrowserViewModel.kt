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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.CareBankApi
import com.example.victor_ai.data.repository.CareBankRepository
import com.example.victor_ai.domain.model.CareBankEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для BrowserScreen
 * Управляет банком заботы
 */
@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val careBankRepository: CareBankRepository,
    private val careBankApi: CareBankApi
) : ViewModel() {

    companion object {
        private const val TAG = "BrowserViewModel"
    }

    // Список записей банка заботы
    private val _careBankEntries = MutableStateFlow<List<CareBankEntry>>(emptyList())
    val careBankEntries: StateFlow<List<CareBankEntry>> = _careBankEntries.asStateFlow()

    // Состояние загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Сообщение об ошибке
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadEntries()
    }

    /**
     * Загружает записи из локального репозитория
     */
    private fun loadEntries() {
        viewModelScope.launch {
            careBankRepository.getEntries().collect { entries ->
                _careBankEntries.value = entries
                Log.d(TAG, "✅ Загружено записей: ${entries.size}")
            }
        }
    }

    /**
     * Синхронизация с бэкендом
     * Вызывается при открытии шторки
     */
    fun syncWithBackend() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = careBankRepository.syncWithBackend()
            _isLoading.value = false

            result.onSuccess {
                Log.d(TAG, "✅ Синхронизация с бэкендом завершена")
            }.onFailure { error ->
                Log.e(TAG, "❌ Ошибка синхронизации", error)
                _errorMessage.value = "Ошибка синхронизации: ${error.message}"
            }
        }
    }

    /**
     * Сохраняет запись в банк заботы
     * Если запись с таким эмодзи уже есть - она обновится
     */
    fun saveEntry(emoji: String, value: String) {
        if (value.isBlank()) {
            _errorMessage.value = "Текст не может быть пустым"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val result = careBankRepository.saveEntry(emoji, value)
            _isLoading.value = false

            result.onSuccess {
                Log.d(TAG, "✅ Запись сохранена: $emoji $value")
                _errorMessage.value = null
            }.onFailure { error ->
                Log.e(TAG, "❌ Ошибка сохранения записи", error)
                _errorMessage.value = "Ошибка сохранения: ${error.message}"
            }
        }
    }

    /**
     * Удаляет запись по эмодзи
     */
    fun deleteEntry(emoji: String) {
        viewModelScope.launch {
            val result = careBankRepository.deleteEntry(emoji)
            result.onFailure { error ->
                Log.e(TAG, "❌ Ошибка удаления записи", error)
                _errorMessage.value = "Ошибка удаления: ${error.message}"
            }
        }
    }

    /**
     * Получить репозиторий для настройки автоматизации
     */
    fun getRepository(): CareBankRepository {
        return careBankRepository
    }

    /**
     * Получить API для автоматизации Care Bank
     */
    fun getCareBankApi(): CareBankApi {
        return careBankApi
    }

    /**
     * Очищает сообщение об ошибке
     */
    fun clearError() {
        _errorMessage.value = null
    }
}

