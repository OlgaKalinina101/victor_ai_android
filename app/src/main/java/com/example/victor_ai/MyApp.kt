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

package com.example.victor_ai

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.repository.AlarmRepository
import com.example.victor_ai.data.repository.CareBankRepository
import com.example.victor_ai.data.repository.ChatRepository
import com.example.victor_ai.data.repository.MemoryRepository
import com.example.victor_ai.data.repository.ReminderRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), DefaultLifecycleObserver {


    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var chatRepository: ChatRepository

    @Inject
    lateinit var memoryRepository: MemoryRepository

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var careBankRepository: CareBankRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Volatile
    private var lastSyncedAccountId: String? = null
    
    // 🔥 Защита от параллельных синхронизаций (race condition fix)
    private val syncMutex = Mutex()

    companion object {
        @Volatile var isForeground: Boolean = false
        private const val TAG = "MyApp"
    }

    override fun onCreate() {
        super<Application>.onCreate()
        Log.e(TAG, "🚀🚀🚀 MyApp.onCreate() - BUILD v13 - allow HTTP/2")
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        // 🔐 Инициализация авторизации (prefs + demo_key)
        UserProvider.init(this)

        // Синхронизация данных при старте приложения
        applicationScope.launch {
            syncDataOnStartup()
        }

        // 🔄 Повторная синхронизация при смене аккаунта (например, при смене demo_key)
        applicationScope.launch {
            UserProvider.authState.collect { st ->
                if (st is UserProvider.AuthState.Ok) {
                    if (lastSyncedAccountId != st.accountId) {
                        Log.d(TAG, "🔄 Account changed -> resync for account_id=${st.accountId}")
                        lastSyncedAccountId = st.accountId
                        syncDataForAccount(st.accountId)
                    }
                }
            }
        }
    }

    private suspend fun syncDataOnStartup() {
        Log.d(TAG, "🔄 Начало синхронизации данных при старте...")

        // 🔐 Авторизация: /auth/resolve
        when (val st = UserProvider.resolveOnStartup()) {
            is UserProvider.AuthState.Ok -> {
                Log.d(TAG, "✅ resolve ok: account_id=${st.accountId}")
                lastSyncedAccountId = st.accountId
                syncDataForAccount(st.accountId)
                Log.d(TAG, "✅ Синхронизация данных завершена")
                return
            }
            is UserProvider.AuthState.NeedsDemoKey -> {
                Log.w(TAG, "🗝️ needs_demo_key -> пропускаем синхронизацию до ввода ключа")
                return
            }
            is UserProvider.AuthState.NeedsRegistration -> {
                Log.w(TAG, "📝 needs_registration -> пропускаем синхронизацию до регистрации")
                return
            }
            is UserProvider.AuthState.Error -> {
                Log.w(TAG, "⚠️ resolve error: ${st.message} -> пропускаем синхронизацию, чтобы не тянуть данные 'из кеша/по старому user_id'")
                return
            }
            else -> Unit
        }
    }

    private suspend fun syncDataForAccount(accountId: String) = withContext(Dispatchers.IO) {
        // 🔥 v9: Выполняем на IO dispatcher чтобы не блокировать Main thread!
        // Проблема была: Main thread занят UI, continuation не выполняется, body не читается -> timeout
        
        // 🔥 Защита от параллельных синхронизаций - критично для ngrok!
        // tryLock возвращает true если mutex свободен, false если занят
        if (!syncMutex.tryLock()) {
            Log.w(TAG, "⏭️ Sync already in progress, skipping duplicate call for account_id=$accountId")
            return@withContext
        }
        
        try {
            Log.d(TAG, "🔄 Sync data for account_id=$accountId (on IO thread)")

            // 🔥 КРИТИЧЕСКОЕ ИСПРАВЛЕНИЕ: Делаем синхронизацию ПОСЛЕДОВАТЕЛЬНОЙ, а не параллельной
            // Множественные параллельные запросы перегружают ngrok туннель
            
            // 1. Синхронизация истории чата (чистит локальную таблицу)
            try {
                Log.e(TAG, "🔄 [1/5] Начинаем синхронизацию чата для $accountId...")
                val startTime = System.currentTimeMillis()
                chatRepository.syncWithBackend(accountId = accountId)
                    .onSuccess { 
                        val duration = System.currentTimeMillis() - startTime
                        Log.e(TAG, "✅ [1/5] История чата синхронизирована за ${duration}ms") 
                    }
                    .onFailure { e -> 
                        val duration = System.currentTimeMillis() - startTime
                        Log.e(TAG, "⚠️ [1/5] Ошибка синхронизации чата за ${duration}ms: ${e.javaClass.simpleName}: ${e.message}") 
                    }
            } catch (e: Exception) {
                Log.e(TAG, "⚠️ [1/5] Exception при синхронизации чата: ${e.javaClass.simpleName}: ${e.message}")
            }

            // 2. Синхронизация воспоминаний
            try {
                Log.e(TAG, "🔄 [2/5] Начинаем синхронизацию воспоминаний...")
                val startTime2 = System.currentTimeMillis()
                memoryRepository.syncWithBackend(accountId)
                    .onSuccess { 
                        val duration = System.currentTimeMillis() - startTime2
                        Log.e(TAG, "✅ [2/5] Воспоминания синхронизированы за ${duration}ms") 
                    }
                    .onFailure { e -> 
                        val duration = System.currentTimeMillis() - startTime2
                        Log.e(TAG, "⚠️ [2/5] Ошибка синхронизации воспоминаний за ${duration}ms: ${e.javaClass.simpleName}: ${e.message}") 
                    }
            } catch (e: Exception) {
                Log.e(TAG, "⚠️ [2/5] Exception при синхронизации воспоминаний: ${e.javaClass.simpleName}: ${e.message}")
            }

            // 3. Синхронизация напоминалок
            try {
                Log.d(TAG, "🔄 [3/5] Начинаем синхронизацию напоминалок...")
                reminderRepository.syncWithBackend(accountId)
                    .onSuccess { Log.d(TAG, "✅ [3/5] Напоминалки синхронизированы") }
                    .onFailure { e -> Log.w(TAG, "⚠️ [3/5] Ошибка синхронизации напоминалок: ${e.javaClass.simpleName}: ${e.message}") }
            } catch (e: Exception) {
                Log.w(TAG, "⚠️ [3/5] Exception при синхронизации напоминалок: ${e.javaClass.simpleName}: ${e.message}")
            }

            // 4. Синхронизация будильников
            try {
                Log.d(TAG, "🔄 [4/5] Начинаем синхронизацию будильников...")
                alarmRepository.fetchAlarmsFromBackend()
                Log.d(TAG, "✅ [4/5] Будильники синхронизированы")
            } catch (e: java.net.SocketTimeoutException) {
                Log.w(TAG, "⏰ [4/5] Timeout при синхронизации будильников (бэкенд не ответил за 60 сек). Пропускаем.")
            } catch (e: java.net.UnknownHostException) {
                Log.w(TAG, "🌐 [4/5] Не удалось подключиться к серверу (проверьте ngrok URL). Пропускаем.")
            } catch (e: Exception) {
                Log.w(TAG, "⚠️ [4/5] Ошибка синхронизации будильников: ${e.javaClass.simpleName}: ${e.message}")
            }

            // 5. Синхронизация банка заботы
            try {
                Log.d(TAG, "🔄 [5/5] Начинаем синхронизацию банка заботы...")
                careBankRepository.syncWithBackend()
                    .onSuccess { Log.d(TAG, "✅ [5/5] Банк заботы синхронизирован") }
                    .onFailure { e -> Log.w(TAG, "⚠️ [5/5] Ошибка синхронизации банка заботы: ${e.javaClass.simpleName}: ${e.message}") }
            } catch (e: Exception) {
                Log.w(TAG, "⚠️ [5/5] Exception при синхронизации банка заботы: ${e.javaClass.simpleName}: ${e.message}")
            }
            
            Log.d(TAG, "🏁 Синхронизация завершена (все 5 шагов выполнены)")
        } finally {
            syncMutex.unlock()
        }
    }

    override fun onStart(owner: androidx.lifecycle.LifecycleOwner) { isForeground = true }
    override fun onStop(owner: androidx.lifecycle.LifecycleOwner) { isForeground = false }
}