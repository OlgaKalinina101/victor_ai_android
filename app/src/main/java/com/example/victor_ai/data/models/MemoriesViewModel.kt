package com.example.victor_ai.data.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.network.RetrofitInstance.apiService
import kotlinx.coroutines.launch

class MemoriesViewModel : ViewModel() {
    private val _memories = MutableLiveData<List<MemoryResponse>>()
    val memories: LiveData<List<MemoryResponse>> get() = _memories

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchMemories(accountId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                Log.d("MemoriesViewModel", "Загрузка воспоминаний для accountId=$accountId")
                val response = RetrofitInstance.apiService.getMemories(accountId)
                Log.d("MemoriesViewModel", "Получено ${response.size} воспоминаний")
                _memories.value = response
            } catch (e: Exception) {
                Log.e("MemoriesViewModel", "Ошибка загрузки: ${e.message}", e)
                _error.value = "Ошибка загрузки воспоминаний: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteMemories(accountId: String, recordIds: List<String>) {
        viewModelScope.launch {
            _loading.value = true
            try {
                Log.d("MemoriesViewModel", "Удаление: accountId=$accountId, recordIds=$recordIds")
                RetrofitInstance.apiService.deleteMemories(accountId, DeleteRequest(recordIds))
                Log.d("MemoriesViewModel", "Успешно удалены записи: $recordIds")
                fetchMemories(accountId) // Обновляем список
            } catch (e: Exception) {
                Log.e("MemoriesViewModel", "Ошибка удаления: ${e.message}", e)
                _error.value = "Ошибка удаления: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateMemory(recordId: String, accountId: String, text: String, metadata: Map<String, Any>?) {
        viewModelScope.launch {
            _loading.value = true
            try {
                Log.d("MemoriesViewModel", "Обновление: recordId=$recordId, accountId=$accountId, text=$text")
                RetrofitInstance.apiService.updateMemory(recordId, accountId, UpdateMemoryRequest(text, metadata))
                Log.d("MemoriesViewModel", "Успешно обновлена запись: $recordId")
                fetchMemories(accountId)
            } catch (e: Exception) {
                Log.e("MemoriesViewModel", "Ошибка обновления: ${e.message}", e)
                _error.value = "Ошибка обновления: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}