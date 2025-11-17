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
import com.example.victor_ai.data.location.LocationProvider
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.notification.PushyTokenManager
import com.example.victor_ai.data.repository.StatsRepository
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.logic.SoundPlayer
import com.example.victor_ai.logic.VoiceRecognizer
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.chat.ChatViewModel
import com.example.victor_ai.ui.main.MainScreen
import com.example.victor_ai.ui.main.MainViewModel
import com.example.victor_ai.ui.main.PlaylistViewModelFactory
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.places.PlacesViewModelFactory
import com.example.victor_ai.ui.playlist.PlaylistViewModel
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

    // ==================== ViewModels ====================
    private val chatViewModel: ChatViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val playlistViewModel: PlaylistViewModel by viewModels {
        PlaylistViewModelFactory(
            apiService = RetrofitInstance.api,
            accountId = UserProvider.getCurrentUserId(),
            cacheDir = cacheDir,
            application = application
        )
    }

    private val placesViewModel: PlacesViewModel by viewModels {
        PlacesViewModelFactory(
            placesApi = RetrofitInstance.placesApi,
            statsRepository = StatsRepository(this, RetrofitInstance.placesApi)
        )
    }

    // ==================== Managers ====================
    private lateinit var voiceRecognizer: VoiceRecognizer
    private lateinit var reminderManager: ReminderManager
    private lateinit var permissionManager: PermissionManager

    private val isListeningState = mutableStateOf(false)

    // ==================== Lifecycle Methods ====================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Victor_AI)

        initializeDependencies()
        setupPermissions()
        registerPushNotifications()

        setContent {
            Victor_AITheme {
                val chatMessages by chatViewModel.chatMessages.collectAsState()
                val snackbarMessage by chatViewModel.snackbarMessage.collectAsState()
                val isTyping by chatViewModel.isTyping.collectAsState()
                val reminderPopup by reminderManager.reminderPopup.collectAsState()
                val location by locationProvider.currentLocation.collectAsState()

                // Обновляем геолокацию в ChatViewModel при изменении
                LaunchedEffect(location) {
                    chatViewModel.setLocation(location)
                }

                MainScreen(
                    playlistViewModel = playlistViewModel,
                    placesViewModel = placesViewModel,
                    reminderManager = reminderManager,
                    chatMessages = chatMessages,
                    onSendMessage = { userText ->
                        chatViewModel.addUserMessage(userText)
                        chatViewModel.sendTextToAssistant(userText)
                    },
                    onEditMessage = { index, newText ->
                        chatViewModel.editMessage(index, newText)
                    },
                    onInitHistory = { history ->
                        chatViewModel.initHistory(history)
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
                    onStartVoiceRecognition = { startVoiceRecognition() },
                    onRequestMicrophone = {
                        permissionManager.requestMicrophonePermission()
                    },
                    isListeningState = isListeningState,
                    isTypingState = remember { derivedStateOf { isTyping } },
                    permissionManager = permissionManager,
                    onStopListening = { voiceRecognizer.stopListening() },
                    snackbarMessage = snackbarMessage,
                    onClearSnackbar = { chatViewModel.clearSnackbar() },
                    reminderPopup = reminderPopup
                )
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
        Log.d("ReminderManager", "[DEBUG] onNewIntent вызван: action=${intent.action}, extras=${intent.extras?.toString()}")
        setIntent(intent)
        reminderManager.handleReminderIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceRecognizer.destroy()
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
        reminderManager = ReminderManager(
            activity = this,
            api = RetrofitInstance.api,
            onSnackbar = { msg ->
                // Можно использовать ChatViewModel для snackbar, но пока оставим как есть
            },
            onReminder = { /* popup управляется через reminderManager.reminderPopup */ },
            coroutineScope = lifecycleScope
        )

        // Настройка ChatViewModel
        chatViewModel.setSessionId(UserProvider.getCurrentUserId())
        chatViewModel.setMainViewModel(mainViewModel)

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
        permissionManager.checkAndRequestNotificationPermission()
        permissionManager.requestLocationPermission()
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
