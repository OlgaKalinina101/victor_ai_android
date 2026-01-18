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

package com.example.victor_ai.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.victor_ai.BuildConfig
import com.example.victor_ai.data.network.dto.ChatMetaResponse
import com.example.victor_ai.data.repository.AuthRepository
import com.example.victor_ai.di.AuthEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Модуль авторизации
 * Предоставляет информацию о текущем пользователе
 * Использует AuthRepository (через Hilt EntryPoint) для запросов к бэкенду
 */
object UserProvider {

    private const val TAG = "UserProvider"
    private val HARDCODED_USER_ID = BuildConfig.TEST_USER_ID // Fallback user ID из конфига

    private const val PREFS_NAME = "victor_ai_auth"
    private const val KEY_ACCOUNT_ID = "account_id"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_DEMO_KEY = "demo_key"
    private const val KEY_PERMISSIONS_COMPLETED = "permissions_completed"

    sealed class AuthState {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data class Ok(val accountId: String, val accessToken: String?) : AuthState()
        data class NeedsDemoKey(val message: String? = null) : AuthState()
        data class NeedsRegistration(
            val message: String?,
            val genderOptions: List<String> = emptyList()
        ) : AuthState()
        data class NeedsPermissions(val accountId: String, val accessToken: String?) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    // Кэш данных пользователя из ChatMeta
    @Volatile
    private var chatMeta: ChatMetaResponse? = null

    @Volatile
    private var currentAccountId: String? = null

    @Volatile
    private var accessToken: String? = null

    @Volatile
    private var demoKey: String = BuildConfig.DEMO_KEY

    private var prefs: SharedPreferences? = null
    
    // Context для доступа к Hilt EntryPoint
    private var appContext: Context? = null
    
    // Lazy доступ к AuthRepository через Hilt EntryPoint
    private val authRepository: AuthRepository
        get() = appContext?.let {
            EntryPointAccessors.fromApplication(it, AuthEntryPoint::class.java).authRepository()
        } ?: throw IllegalStateException("UserProvider не инициализирован! Вызовите init() в Application")

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /**
     * Должен быть вызван один раз на старте (например, в Application/Activity)
     */
    fun init(context: Context) {
        if (prefs != null) return
        appContext = context.applicationContext
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Загружаем сохранённые значения (если есть)
        currentAccountId = prefs?.getString(KEY_ACCOUNT_ID, null)
        accessToken = prefs?.getString(KEY_ACCESS_TOKEN, null)
        demoKey = prefs?.getString(KEY_DEMO_KEY, null) ?: BuildConfig.DEMO_KEY

        Log.d(TAG, "🔐 init: demoKey=${if (demoKey.length > 6) demoKey.take(6) + "***" else "***"}, accountId=$currentAccountId, token=${accessToken != null}")
    }

    fun updateDemoKey(newDemoKey: String) {
        demoKey = newDemoKey.trim()
        prefs?.edit()?.putString(KEY_DEMO_KEY, demoKey)?.apply()

        // Разрешаем повторный resolve (например, после ввода ключа)
        _authState.value = AuthState.Idle

        Log.i(TAG, "✅ demo_key updated: ${demoKey.take(6)}...")
    }

    fun getDemoKey(): String = demoKey

    /**
     * Загружает ChatMeta для текущего авторизованного аккаунта.
     * Использует currentAccountId (из /auth/resolve), а не хардкод.
     */
    suspend fun loadUserData(): Result<ChatMetaResponse> {
        val accountId = currentAccountId ?: HARDCODED_USER_ID
        Log.d(TAG, "🔐 Загрузка ChatMeta для: $accountId")
        
        return authRepository.getChatMeta(accountId).also { result ->
            result.onSuccess { meta ->
                chatMeta = meta
                Log.d(TAG, "✅ ChatMeta загружена успешно!")
                Log.d(TAG, "   account_id: ${meta.account_id}")
                Log.d(TAG, "   trust_level: ${meta.trust_level}")
                Log.d(TAG, "   model: ${meta.model}")
            }.onFailure { e ->
                Log.e(TAG, "❌ Ошибка загрузки ChatMeta: ${e.message}", e)
            }
        }
    }

    /**
     * Возвращает ID текущего пользователя.
     * Приоритет: currentAccountId (из resolve) -> chatMeta -> HARDCODED_USER_ID
     */
    fun getCurrentUserId(): String {
        return currentAccountId
            ?: chatMeta?.account_id
            ?: HARDCODED_USER_ID
    }

    /**
     * Возвращает accountId ТОЛЬКО если он реально известен (resolve/chatMeta),
     * без fallback на HARDCODED_USER_ID.
     *
     * Нужен для привязок/токенов, которые нельзя отправлять как "test_user" по умолчанию.
     */
    fun getResolvedAccountIdOrNull(): String? {
        return currentAccountId ?: chatMeta?.account_id
    }

    fun getAccessToken(): String? = accessToken

    /**
     * Основная авторизация на старте приложения: POST /auth/resolve
     *
     * - если ok -> сохраняем token + account_id
     * - если needs_registration -> отдаём состояние для UI
     */
    suspend fun resolveOnStartup(): AuthState {
        // Если уже идёт/готово — не дёргаем сеть повторно
        val st = _authState.value
        if (st is AuthState.Loading || st is AuthState.Ok || st is AuthState.NeedsRegistration || st is AuthState.NeedsDemoKey) return st

        if (demoKey.isBlank()) {
            Log.w(TAG, "🗝️ demo_key is blank -> show auth screen")
            return AuthState.NeedsDemoKey("Введи demo_key для авторизации.").also { _authState.value = it }
        }

        Log.d(TAG, "📡 resolveOnStartup -> calling /auth/resolve (demo_key=${demoKey.take(6)}..., len=${demoKey.length})")
        _authState.value = AuthState.Loading

        val result = authRepository.resolveDemo(demoKey)
        
        return result.fold(
            onSuccess = { body ->
                Log.d(TAG, "📡 resolve response status=${body.status} account_id=${body.account_id} hasToken=${body.access_token != null}")
                when (body.status) {
                    "ok" -> {
                        val accountId = body.account_id ?: HARDCODED_USER_ID
                        val token = body.access_token
                        applyAuth(accountId = accountId, token = token)
                        
                        // Проверяем, были ли уже показаны разрешения
                        val permissionsCompleted = prefs?.getBoolean(KEY_PERMISSIONS_COMPLETED, false) ?: false
                        if (!permissionsCompleted) {
                            Log.d(TAG, "📡 resolve ok -> but permissions not completed yet, showing permissions screen")
                            AuthState.NeedsPermissions(accountId = accountId, accessToken = token).also { _authState.value = it }
                        } else {
                            Log.d(TAG, "📡 resolve ok -> permissions already completed")
                            AuthState.Ok(accountId = accountId, accessToken = token).also { _authState.value = it }
                        }
                    }
                    "needs_registration" -> {
                        AuthState.NeedsRegistration(
                            message = body.message,
                            genderOptions = body.gender_options ?: emptyList()
                        ).also { _authState.value = it }
                    }
                    else -> {
                        val msg = "Unknown status: ${body.status}"
                        Log.e(TAG, "❌ $msg")
                        AuthState.Error(msg).also { _authState.value = it }
                    }
                }
            },
            onFailure = { e ->
                val msg = "resolve exception: ${e.javaClass.simpleName}: ${e.message}"
                Log.e(TAG, "❌ $msg", e)
                AuthState.Error(msg).also { _authState.value = it }
            }
        )
    }

    /**
     * Преобразует UI-формат гендера (MALE/FEMALE) в формат бэкенда (значения enum)
     * Backend Gender enum принимает: "мужчина", "девушка", "другое"
     */
    private fun mapGenderToBackend(uiGender: String): String {
        return when (uiGender.uppercase()) {
            "MALE" -> "мужчина"
            "FEMALE" -> "девушка"
            "OTHER" -> "другое"
            else -> uiGender  // fallback
        }
    }

    /**
     * "Регистрация" через /auth/register (передаём demo_key, account_id + gender)
     * Gender должен быть одним из значений enum: "мужчина", "девушка", "другое"
     */
    suspend fun submitRegistration(accountId: String, gender: String): AuthState {
        _authState.value = AuthState.Loading

        val backendGender = mapGenderToBackend(gender)
        Log.d(TAG, "Registering: account_id=$accountId, ui_gender=$gender, backend_gender=$backendGender")
        
        val result = authRepository.registerDemo(demoKey, accountId, backendGender)
        
        return result.fold(
            onSuccess = { body ->
                val resolvedAccountId = body.account_id
                val token = body.access_token
                applyAuth(accountId = resolvedAccountId, token = token)
                Log.i(TAG, "✅ Registration successful: account_id=$resolvedAccountId -> moving to permissions screen")
                // После успешной регистрации переходим к экрану разрешений
                AuthState.NeedsPermissions(accountId = resolvedAccountId, accessToken = token).also { _authState.value = it }
            },
            onFailure = { e ->
                val msg = "registration exception: ${e.javaClass.simpleName}: ${e.message}"
                Log.e(TAG, "❌ $msg", e)
                AuthState.Error(msg).also { _authState.value = it }
            }
        )
    }

    /**
     * Возвращает полные данные пользователя из ChatMeta (если загружены)
     */
    fun getChatMeta(): ChatMetaResponse? {
        return chatMeta
    }

    /**
     * Переход к экрану запроса разрешений после регистрации
     */
    fun moveToPermissionsScreen() {
        val accountId = currentAccountId ?: return
        val token = accessToken
        _authState.value = AuthState.NeedsPermissions(accountId = accountId, accessToken = token)
        Log.d(TAG, "🔐 Moved to permissions screen: account_id=$accountId")
    }

    /**
     * Завершение запроса разрешений и переход в основное приложение
     */
    fun completePermissions() {
        val accountId = currentAccountId ?: return
        val token = accessToken
        
        // Сохраняем флаг, что разрешения уже были показаны
        prefs?.edit()
            ?.putBoolean(KEY_PERMISSIONS_COMPLETED, true)
            ?.apply()
        
        _authState.value = AuthState.Ok(accountId = accountId, accessToken = token)
        Log.d(TAG, "✅ Permissions completed, moved to Ok state")
    }

    fun logout() {
        Log.i(TAG, "🚪 logout")
        chatMeta = null
        currentAccountId = null
        accessToken = null
        prefs?.edit()
            ?.remove(KEY_ACCOUNT_ID)
            ?.remove(KEY_ACCESS_TOKEN)
            ?.apply()
        _authState.value = AuthState.Idle
    }

    private fun applyAuth(accountId: String, token: String?) {
        currentAccountId = accountId
        accessToken = token
        prefs?.edit()
            ?.putString(KEY_ACCOUNT_ID, accountId)
            ?.putString(KEY_ACCESS_TOKEN, token)
            ?.putString(KEY_DEMO_KEY, demoKey)
            ?.apply()
        Log.i(TAG, "✅ applyAuth accountId=$accountId hasToken=${token != null}")
    }
}
