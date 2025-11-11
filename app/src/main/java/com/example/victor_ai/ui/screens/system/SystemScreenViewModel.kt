package com.example.victor_ai.ui.screens.system

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.AssistantMind
import com.example.victor_ai.data.network.AssistantState
import com.example.victor_ai.data.network.ModelUsage
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.network.dto.ChatMetaUpdateRequest
import com.example.victor_ai.logic.UsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для SystemScreen
 * Управляет состоянием и логикой загрузки данных
 */
@HiltViewModel
class SystemScreenViewModel @Inject constructor(
    private val usageRepository: UsageRepository
) : ViewModel() {

    private val apiService = RetrofitInstance.apiService
    private val assistantApi = RetrofitInstance.assistantApi

    // Единый state для всего экрана
    private val _state = MutableStateFlow(SystemScreenState())
    val state: StateFlow<SystemScreenState> = _state.asStateFlow()

    // Вспомогательное поле для списка состояний (для вычисления emotionalShift)
    private val _assistantStateList = MutableStateFlow<List<AssistantState>>(emptyList())

    init {
        loadAllData()
    }

    /**
     * Загружает все данные для экрана
     */
    private fun loadAllData() {
        viewModelScope.launch {
            Log.d(TAG, "▶️ Начинаем загрузку данных")

            // Проверка связи
            checkConnection()

            // Загрузка ChatMeta
            loadChatMeta()

            // Загрузка usage данных
            loadModelUsage()

            // Загрузка состояния и фокусов
            loadAssistantData()
        }
    }

    /**
     * Проверяет связь с сервером
     */
    private suspend fun checkConnection() {
        _state.value = _state.value.copy(isChecking = true)
        Log.d(TAG, "🌐 Проверяем связь...")

        val isOnline = try {
            val response = apiService.checkConnection()
            Log.d(TAG, "🌐 Связь проверена: ${response.isSuccessful}")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "🌐 Ошибка проверки связи", e)
            false
        }

        _state.value = _state.value.copy(isOnline = isOnline, isChecking = false)
    }

    /**
     * Загружает ChatMeta для получения trust_level и модели
     */
    private suspend fun loadChatMeta() {
        Log.d(TAG, "🔐 Переходим к загрузке ChatMeta...")
        try {
            Log.d(TAG, "🔄 Начинаем загрузку ChatMeta...")
            val result = UserProvider.loadUserData()
            Log.d(TAG, "🔄 UserProvider.loadUserData() вызван, обрабатываем результат...")

            result
                .onSuccess { meta ->
                    _state.value = _state.value.copy(
                        trustLevel = meta.trust_level,
                        currentModel = meta.model
                    )
                    Log.d(TAG, "✅ ChatMeta загружена успешно!")
                    Log.d(TAG, "   account_id: ${meta.account_id}")
                    Log.d(TAG, "   trust_level: ${meta.trust_level}")
                    Log.d(TAG, "   model: ${meta.model}")
                    Log.d(TAG, "   Значение trustLevel в state: ${_state.value.trustLevel}")
                    Log.d(TAG, "   Значение currentModel в state: ${_state.value.currentModel}")
                }
                .onFailure { e ->
                    Log.e(TAG, "❌ Ошибка загрузки ChatMeta: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Исключение при загрузке ChatMeta", e)
        }
        Log.d(TAG, "🔐 Загрузка ChatMeta завершена")
    }

    /**
     * Загружает данные об использовании моделей
     */
    private suspend fun loadModelUsage() {
        val modelUsage = usageRepository.getModelUsage(UserProvider.getCurrentUserId())
        _state.value = _state.value.copy(modelUsageList = modelUsage)
    }

    /**
     * Загружает состояние ассистента и его фокусы
     */
    private suspend fun loadAssistantData() {
        try {
            val stateResponse = assistantApi.getAssistantState(UserProvider.getCurrentUserId())
            _assistantStateList.value = stateResponse

            val mind = assistantApi.getAssistantMind(UserProvider.getCurrentUserId())
                .filter { it.type == "focus" || it.type == "anchor" }

            _state.value = _state.value.copy(
                assistantState = stateResponse.lastOrNull()?.state,
                assistantMind = mind,
                emotionalShift = calculateEmotionalShift(stateResponse)
            )

            Log.d(TAG, "Получен список состояний: $stateResponse")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка запроса состояния или mind: ${e.message}")
        }
    }

    /**
     * Обновляет текущую модель (и провайдера)
     */
    fun updateModel(newModel: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "🔄 Обновляем модель на: $newModel")

                val response = apiService.updateChatMeta(
                    accountId = UserProvider.getCurrentUserId(),
                    body = ChatMetaUpdateRequest(model = newModel)
                )

                if (response.isSuccessful) {
                    _state.value = _state.value.copy(currentModel = newModel)
                    Log.d(TAG, "✅ Модель успешно обновлена на $newModel")
                } else {
                    Log.e(TAG, "❌ Ошибка обновления модели: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Исключение при обновлении модели", e)
            }
        }
    }

    /**
     * Вычисляет эмоциональный сдвиг из списка состояний
     */
    private fun calculateEmotionalShift(stateList: List<AssistantState>): String? {
        return if (stateList.isNotEmpty()) {
            val uniqueStates = stateList
                .takeLast(10)
                .distinctBy { it.state }
                .takeLast(2)

            if (uniqueStates.size >= 2) {
                uniqueStates.joinToString(" → ") { it.state }
            } else {
                "Эмоциональный сдвиг: Null"
            }
        } else {
            null
        }
    }

    companion object {
        private const val TAG = "SystemScreenViewModel"
    }
}
