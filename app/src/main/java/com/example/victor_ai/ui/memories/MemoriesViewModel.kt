package com.example.victor_ai.ui.memories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.local.entity.MemoryEntity
import com.example.victor_ai.data.network.dto.MemoryResponse
import com.example.victor_ai.data.repository.MemoryRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoriesViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository
) : ViewModel() {

    private val gson = Gson()

    // Подписываемся на Flow из репозитория и конвертируем в LiveData
    val memories: LiveData<List<MemoryResponse>> = memoryRepository.getMemories()
        .map { entities -> entities.map { it.toResponse() } }
        .asLiveData()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // Синхронизация с бэкендом (загрузка данных в локальную БД)
    fun fetchMemories(accountId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                Log.d("MemoriesViewModel", "Синхронизация воспоминаний с бэкендом...")
                memoryRepository.syncWithBackend(accountId)
                    .onSuccess {
                        Log.d("MemoriesViewModel", "✅ Синхронизация завершена")
                    }
                    .onFailure { e ->
                        Log.e("MemoriesViewModel", "❌ Ошибка синхронизации: ${e.message}", e)
                        _error.value = "Ошибка загрузки: ${e.message}"
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteMemories(accountId: String, recordIds: List<String>) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                Log.d("MemoriesViewModel", "Удаление воспоминаний: $recordIds")
                memoryRepository.deleteMemories(accountId, recordIds)
                    .onSuccess {
                        Log.d("MemoriesViewModel", "✅ Воспоминания удалены")
                    }
                    .onFailure { e ->
                        Log.e("MemoriesViewModel", "❌ Ошибка удаления: ${e.message}", e)
                        _error.value = "Ошибка удаления: ${e.message}"
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateMemory(recordId: String, accountId: String, text: String, metadata: Map<String, Any>?) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                Log.d("MemoriesViewModel", "Обновление воспоминания: $recordId")
                memoryRepository.updateMemory(recordId, accountId, text, metadata ?: emptyMap())
                    .onSuccess {
                        Log.d("MemoriesViewModel", "✅ Воспоминание обновлено")
                    }
                    .onFailure { e ->
                        Log.e("MemoriesViewModel", "❌ Ошибка обновления: ${e.message}", e)
                        _error.value = "Ошибка обновления: ${e.message}"
                    }
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    // Маппер Entity -> MemoryResponse для обратной совместимости с UI
    private fun MemoryEntity.toResponse(): MemoryResponse {
        val metadataMap = try {
            gson.fromJson(metadata, Map::class.java) as? Map<String, Any> ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
        return MemoryResponse(
            id = id,
            text = text,
            metadata = metadataMap
        )
    }
}