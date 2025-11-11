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

    // State flows для UI
    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _isChecking = MutableStateFlow(true)
    val isChecking: StateFlow<Boolean> = _isChecking.asStateFlow()

    private val _modelUsageList = MutableStateFlow<List<ModelUsage>>(emptyList())
    val modelUsageList: StateFlow<List<ModelUsage>> = _modelUsageList.asStateFlow()

    private val _assistantStateList = MutableStateFlow<List<AssistantState>>(emptyList())
    val assistantStateList: StateFlow<List<AssistantState>> = _assistantStateList.asStateFlow()

    private val _assistantState = MutableStateFlow<String?>(null)
    val assistantState: StateFlow<String?> = _assistantState.asStateFlow()

    private val _assistantMind = MutableStateFlow<List<AssistantMind>>(emptyList())
    val assistantMind: StateFlow<List<AssistantMind>> = _assistantMind.asStateFlow()

    private val _trustLevel = MutableStateFlow(0)
    val trustLevel: StateFlow<Int> = _trustLevel.asStateFlow()

    private val _currentModel = MutableStateFlow<String?>(null)
    val currentModel: StateFlow<String?> = _currentModel.asStateFlow()

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
        _isChecking.value = true
        Log.d(TAG, "🌐 Проверяем связь...")

        _isOnline.value = try {
            val response = apiService.checkConnection()
            Log.d(TAG, "🌐 Связь проверена: ${response.isSuccessful}")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "🌐 Ошибка проверки связи", e)
            false
        }

        _isChecking.value = false
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
                    _trustLevel.value = meta.trust_level
                    _currentModel.value = meta.model
                    Log.d(TAG, "✅ ChatMeta загружена успешно!")
                    Log.d(TAG, "   account_id: ${meta.account_id}")
                    Log.d(TAG, "   trust_level: ${meta.trust_level}")
                    Log.d(TAG, "   model: ${meta.model}")
                    Log.d(TAG, "   Значение trustLevel в state: ${_trustLevel.value}")
                    Log.d(TAG, "   Значение currentModel в state: ${_currentModel.value}")
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
        _modelUsageList.value = usageRepository.getModelUsage(UserProvider.getCurrentUserId())
    }

    /**
     * Загружает состояние ассистента и его фокусы
     */
    private suspend fun loadAssistantData() {
        try {
            val stateResponse = assistantApi.getAssistantState(UserProvider.getCurrentUserId())
            _assistantStateList.value = stateResponse
            _assistantState.value = stateResponse.lastOrNull()?.state

            _assistantMind.value = assistantApi.getAssistantMind(UserProvider.getCurrentUserId())
                .filter { it.type == "focus" || it.type == "anchor" }

            Log.d(TAG, "Получен список состояний: ${_assistantStateList.value}")
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
                    _currentModel.value = newModel
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
    fun getEmotionalShift(): String? {
        val stateList = _assistantStateList.value

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
