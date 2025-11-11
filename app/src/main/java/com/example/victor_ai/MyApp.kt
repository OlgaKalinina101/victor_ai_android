package com.example.victor_ai

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.repository.ChatRepository
import com.example.victor_ai.data.repository.MemoryRepository
import com.example.victor_ai.data.repository.ReminderRepository
import com.example.victor_ai.logic.ChatHistoryHelper
import com.example.victor_ai.logic.ReminderHelper
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), DefaultLifecycleObserver {

    @Inject
    lateinit var chatRepository: ChatRepository

    @Inject
    lateinit var memoryRepository: MemoryRepository

    @Inject
    lateinit var reminderRepository: ReminderRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    companion object {
        @Volatile var isForeground: Boolean = false
        private const val TAG = "MyApp"
    }

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        // Инициализируем helper'ы
        ChatHistoryHelper.initialize(chatRepository)
        ReminderHelper.initialize(reminderRepository)

        // Синхронизация данных при старте приложения
        applicationScope.launch {
            syncDataOnStartup()
        }
    }

    private suspend fun syncDataOnStartup() {
        Log.d(TAG, "🔄 Начало синхронизации данных при старте...")

        // Синхронизация истории чата
        chatRepository.syncWithBackend()
            .onSuccess { Log.d(TAG, "✅ История чата синхронизирована") }
            .onFailure { e -> Log.w(TAG, "⚠️ Ошибка синхронизации чата: ${e.message}") }

        // Синхронизация воспоминаний
        memoryRepository.syncWithBackend(UserProvider.getCurrentUserId())
            .onSuccess { Log.d(TAG, "✅ Воспоминания синхронизированы") }
            .onFailure { e -> Log.w(TAG, "⚠️ Ошибка синхронизации воспоминаний: ${e.message}") }

        // Синхронизация напоминалок
        reminderRepository.syncWithBackend(UserProvider.getCurrentUserId())
            .onSuccess { Log.d(TAG, "✅ Напоминалки синхронизированы") }
            .onFailure { e -> Log.w(TAG, "⚠️ Ошибка синхронизации напоминалок: ${e.message}") }

        Log.d(TAG, "✅ Синхронизация данных завершена")
    }

    override fun onStart(owner: androidx.lifecycle.LifecycleOwner) { isForeground = true }
    override fun onStop(owner: androidx.lifecycle.LifecycleOwner) { isForeground = false }
}