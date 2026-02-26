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

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.local.AppDatabase
import com.example.victor_ai.data.location.LocationProvider
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.ReminderApi
import com.example.victor_ai.data.notification.PushyTokenManager
import com.example.victor_ai.data.repository.ReminderRepository
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.logic.SoundPlayer
import com.example.victor_ai.logic.VoiceRecognizer
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.chat.ChatViewModel
import com.example.victor_ai.ui.main.MainRootScreen
import com.example.victor_ai.ui.main.MainViewModel
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.auth.DemoKeyScreen
import com.example.victor_ai.ui.auth.RegistrationScreen
import com.example.victor_ai.ui.theme.Victor_AITheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // ==================== Dependencies ====================
    @Inject
    lateinit var soundPlayer: SoundPlayer

    @Inject
    lateinit var pushyTokenManager: PushyTokenManager

    @Inject
    lateinit var locationProvider: LocationProvider

    @Inject
    lateinit var careBankCommandHandler: com.example.victor_ai.logic.carebank.CareBankCommandHandler

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var reminderApi: ReminderApi

    @Inject
    lateinit var careBankApi: com.example.victor_ai.data.network.CareBankApi

    // ==================== ViewModels ====================
    private val chatViewModel: ChatViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private val placesViewModel: PlacesViewModel by viewModels()

    // ==================== Managers ====================
    private lateinit var voiceRecognizer: VoiceRecognizer
    private lateinit var reminderManager: ReminderManager
    private lateinit var permissionManager: PermissionManager

    private val isListeningState = mutableStateOf(false)

    // ==================== Lifecycle Methods ====================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Victor_AI)

        // 🔐 init auth storage before creating any network-dependent pieces
        UserProvider.init(this)

        initializeDependencies()
        setupPermissions()
        registerPushNotifications()

        setContent {
            Victor_AITheme {
                val authState by UserProvider.authState.collectAsState()

                // 🔐 Safety net: гарантированно дергаем /auth/resolve при старте UI.
                // Если MyApp уже начал resolve, вызов будет no-op (см. resolveOnStartup guard).
                LaunchedEffect(Unit) {
                    UserProvider.resolveOnStartup()
                }

                when (val st = authState) {
                    is UserProvider.AuthState.NeedsDemoKey -> {
                        DemoKeyScreen(
                            initialDemoKey = UserProvider.getDemoKey(),
                            hintText = st.message,
                            onSubmit = { demoKey ->
                                UserProvider.updateDemoKey(demoKey)
                                this@MainActivity.lifecycleScope.launch { UserProvider.resolveOnStartup() }
                            }
                        )
                    }

                    is UserProvider.AuthState.NeedsRegistration -> {
                        // Нормализуем gender_options с бэкенда в формат UI: MALE, FEMALE
                        val options = st.genderOptions
                            .ifEmpty { listOf("MALE", "FEMALE") }
                            .mapNotNull { option ->
                                when (option.lowercase()) {
                                    "male", "мужчина" -> "MALE"
                                    "female", "девушка" -> "FEMALE"
                                    "other", "другое" -> null  // Фильтруем OTHER
                                    else -> null
                                }
                            }
                            .ifEmpty { listOf("MALE", "FEMALE") }  // fallback если всё отфильтровалось
                        
                        RegistrationScreen(
                            genderOptions = options,
                            message = st.message,
                            onSubmit = { accountId, gender ->
                                this@MainActivity.lifecycleScope.launch {
                                    UserProvider.submitRegistration(accountId, gender)
                                }
                            }
                        )
                    }

                    is UserProvider.AuthState.NeedsPermissions -> {
                        com.example.victor_ai.ui.permissions.PermissionsScreen(
                            onComplete = {
                                UserProvider.completePermissions()
                            }
                        )
                    }

                    is UserProvider.AuthState.Ok -> {
                        // 🔥 ИСПРАВЛЕНО: Добавлено логирование для диагностики перехода на главный экран
                        Log.d("MainActivity", "✅ AuthState.Ok received, accountId=${st.accountId}, показываем MainRootScreen")
                        
                        val chatMessages by chatViewModel.chatMessages.collectAsState()
                        val snackbarMessage by chatViewModel.snackbarMessage.collectAsState()
                        val isTyping by chatViewModel.isTyping.collectAsState()
                        val searchMatchedMessageId by chatViewModel.searchMatchedMessageId.collectAsState()
                        val reminderPopup by reminderManager.reminderPopup.collectAsState()
                        val location by locationProvider.currentLocation.collectAsState()

                        // Care Bank WebView от бэкенда
                        val careBankWebViewUrl by chatViewModel.careBankWebViewUrl.collectAsState()
                        val careBankAutomationData by chatViewModel.careBankAutomationData.collectAsState()

                        // 🔥 Состояние пагинации из VM
                        val isLoadingMore by chatViewModel.isLoadingMore.collectAsState()
                        val hasMoreHistory by chatViewModel.hasMoreHistory.collectAsState()
                        val oldestId by chatViewModel.oldestId.collectAsState()

                        // Обновляем геолокацию в ChatViewModel при изменении
                        LaunchedEffect(location) {
                            chatViewModel.setLocation(location)
                        }

                        // 🔐 Когда авторизация "ок" — перезагружаем данные для нового аккаунта
                        LaunchedEffect(st.accountId) {
                            Log.d("MainActivity", "🔐 Auth OK: accountId=${st.accountId}, перезагружаем данные")
                            try {
                                chatViewModel.reloadForAccount(st.accountId)
                                Log.d("MainActivity", "✅ ChatViewModel reloaded")
                            } catch (e: Exception) {
                                Log.e("MainActivity", "❌ Ошибка reloadForAccount в ChatViewModel", e)
                            }
                            
                            try {
                                playlistViewModel.reinitialize(st.accountId)
                                Log.d("MainActivity", "✅ PlaylistViewModel reinitialized")
                            } catch (e: Exception) {
                                Log.e("MainActivity", "❌ Ошибка reinitialize в PlaylistViewModel", e)
                            }
                            
                            // ✅ Привязываем push-токен к реальному accountId (не к fallback "test_user")
                            try {
                                pushyTokenManager.bindTokenToAccount(st.accountId)
                                Log.d("MainActivity", "✅ Push token bound to account")
                            } catch (e: Exception) {
                                Log.e("MainActivity", "❌ Ошибка bindTokenToAccount", e)
                            }
                        }

                        MainRootScreen(
                            accountId = st.accountId,
                            playlistViewModel = playlistViewModel,
                            placesViewModel = placesViewModel,
                            reminderManager = reminderManager,
                            chatMessages = chatMessages,
                            onSendMessage = { userText, attachedImages, swipeMessageId ->
                                chatViewModel.addUserMessage(userText, attachedImages.size)
                                chatViewModel.sendTextToAssistant(userText, attachedImages, swipeMessageId)
                            },
                            onEditMessage = { index, newText ->
                                chatViewModel.editMessage(index, newText)
                            },
                            onPaginationInfo = { oldestId, hasMore ->
                                chatViewModel.updatePaginationInfo(oldestId, hasMore)
                            },
                            onLoadMoreHistory = { beforeId ->
                                chatViewModel.loadMoreHistory(beforeId)
                            },
                            onSearch = { query ->
                                chatViewModel.searchInHistory(query)
                            },
                            onSearchNext = {
                                chatViewModel.searchNext()
                            },
                            onClearSearch = {
                                chatViewModel.clearSearch()
                            },
                            searchMatchedMessageId = searchMatchedMessageId,
                            onStartVoiceRecognition = { startVoiceRecognition() },
                            onRequestMicrophone = {
                                permissionManager.requestMicrophonePermission()
                            },
                            isListeningState = isListeningState,
                            isTypingState = remember { derivedStateOf { isTyping } },
                            isLoadingMoreState = remember { derivedStateOf { isLoadingMore } },
                            hasMoreHistoryState = remember { derivedStateOf { hasMoreHistory } },
                            oldestIdState = remember { derivedStateOf { oldestId } },
                            permissionManager = permissionManager,
                            onStopListening = { voiceRecognizer.stopListening() },
                            snackbarMessage = snackbarMessage,
                            onClearSnackbar = { chatViewModel.clearSnackbar() },
                            reminderPopup = reminderPopup,
                            careBankCommandHandler = careBankCommandHandler,
                            careBankWebViewUrl = careBankWebViewUrl,
                            careBankAutomationData = careBankAutomationData,
                            onCloseCareBankWebView = { chatViewModel.closeCareBankWebView() },
                            careBankRepository = chatViewModel.careBankRepository,
                            careBankApi = careBankApi,
                            onAddChatMessage = { text ->
                                chatViewModel.addUserMessage(text)
                            },
                            onSendSystemEvent = { eventName ->
                                chatViewModel.sendSystemEvent(eventName)
                            },
                            onUpdateEmoji = { messageId, emoji ->
                                chatViewModel.updateMessageEmoji(messageId, emoji)
                            }
                        )
                    }

                    is UserProvider.AuthState.Loading,
                    is UserProvider.AuthState.Idle,
                    is UserProvider.AuthState.Error -> {
                        // Экран demo_key как fallback (и для ошибок тоже)
                        DemoKeyScreen(
                            initialDemoKey = UserProvider.getDemoKey(),
                            errorText = (st as? UserProvider.AuthState.Error)?.message,
                            hintText = "Подсказка: ключ выдает автор проекта.",
                            onSubmit = { demoKey ->
                                UserProvider.updateDemoKey(demoKey)
                                this@MainActivity.lifecycleScope.launch { UserProvider.resolveOnStartup() }
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        reminderManager.registerReceiver()
    }

    override fun onStop() {
        super.onStop()
        reminderManager.unregisterReceiver()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "[DEBUG] onNewIntent: action=${intent.action}, extras=${intent.extras?.toString()}")
        setIntent(intent)

        if (intent.action == "com.example.victor_ai.REFLECTION_MESSAGE") {
            handleReflectionMessage(intent)
        } else {
            reminderManager.handleReminderIntent(intent)
        }
    }

    private fun handleReflectionMessage(intent: Intent) {
        val text = intent.getStringExtra("text") ?: return
        Log.d("MainActivity", "💬 Reflection message received: '${text.take(80)}'")
        chatViewModel.addReflectionMessage(text)
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceRecognizer.destroy()
        soundPlayer.release() // Освобождаем ресурсы SoundPool
    }

    // ==================== Initialization ====================

    private fun initializeDependencies() {
        // Инициализация голосового ввода
        voiceRecognizer = VoiceRecognizer(
            context = this,
            onTextRecognized = { recognizedText ->
                chatViewModel.addUserMessage(recognizedText)
                chatViewModel.sendTextToAssistant(recognizedText)
            },
            onListeningStateChanged = { isListening ->
                isListeningState.value = isListening
            }
        )

        // Инициализация напоминаний
        val database = AppDatabase.getDatabase(this)
        val reminderRepository = ReminderRepository(
            reminderDao = database.reminderDao(),
            reminderApi = reminderApi
        )
        
        reminderManager = ReminderManager(
            activity = this,
            reminderApi = reminderApi,
            reminderRepository = reminderRepository,
            onSnackbar = { msg ->
                // Можно использовать ChatViewModel для snackbar, но пока оставим как есть
            },
            onReminder = { /* popup управляется через reminderManager.reminderPopup */ },
            coroutineScope = lifecycleScope
        )

        // Настройка ChatViewModel
        chatViewModel.setSessionId(UserProvider.getCurrentUserId())
        // 🔥 Используем интерфейс PlaybackController вместо прямой ссылки на MainViewModel
        chatViewModel.setPlaybackController(mainViewModel)

        // Настройка MainViewModel
        mainViewModel.setPlaylistViewModel(playlistViewModel)
    }

    private fun setupPermissions() {
        permissionManager = PermissionManager(
            activity = this,
            onAudioGranted = { startVoiceRecognition() },
            onLocationGranted = { locationProvider.startFetchingLocation() }
        )

        permissionManager.register()
        // ⚠️ ВАЖНО: НИЧЕГО не запрашиваем автоматически при старте приложения.
        // Все системные запросы разрешений должны происходить только на PermissionsScreen
        // (AuthState.NeedsPermissions) или по явному действию пользователя (нажатия кнопок).
    }

    /**
     * Настраивает все необходимые разрешения для будильника
     * Вызывается при первом запуске приложения
     */
    private fun setupAlarmPermissions() {
        // Проверяем все разрешения сразу
        val allGranted = permissionManager.checkAlarmPermissions()
        
        if (!allGranted) {
            Log.w("MainActivity", "⚠️ Не все разрешения для будильника предоставлены")
            
            // 🔥 КРИТИЧНО для Android 14+ (Pixel 8a): Full Screen Intent
            permissionManager.checkAndRequestFullScreenIntentPermission()
            
            // Оптимизация батареи
            permissionManager.checkAndRequestBatteryOptimizationPermission()
            
            // Разрешение на показ поверх окон (опционально, но может помочь)
            // permissionManager.checkAndRequestOverlayPermission()
        } else {
            Log.i("MainActivity", "✅ Все разрешения для будильника предоставлены")
        }
    }

    private fun registerPushNotifications() {
        lifecycleScope.launch {
            pushyTokenManager.registerPushy()
        }
    }

    // ==================== Voice Recognition ====================

    private fun startVoiceRecognition() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            voiceRecognizer.start()
        } else {
            permissionManager.requestMicrophonePermission()
        }
    }
}
