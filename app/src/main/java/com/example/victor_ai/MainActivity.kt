package com.example.victor_ai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.victor_ai.data.network.dto.AssistantRequest
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.ui.theme.Victor_AITheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.layout.Box
import android.content.Context
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import androidx.navigation.compose.rememberNavController
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.dto.GeoLocation
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.logic.SoundPlayer
import com.example.victor_ai.logic.VoiceRecognizer
import com.example.victor_ai.logic.processStreamingMessage
import com.example.victor_ai.logic.updateChatHistory
import com.example.victor_ai.logic.ChatHistoryHelper
import com.example.victor_ai.data.local.entity.ChatMessageEntity
import com.example.victor_ai.domain.model.ChatMessage
import com.example.victor_ai.domain.model.ReminderPopup
import com.example.victor_ai.ui.main.MainViewModel
import com.example.victor_ai.ui.main.PlaylistViewModelFactory
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.common.AnimatedBackgroundBox
import com.example.victor_ai.ui.components.AssistantButtonArea
import com.example.victor_ai.ui.components.ReminderOverlay
import com.example.victor_ai.ui.navigation.AppNavHost
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.places.PlacesViewModelFactory
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.screens.PresencePlaceholder
import com.example.victor_ai.data.repository.StatsRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.pushy.sdk.Pushy
import kotlin.getValue
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.currentBackStackEntryAsState


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var voiceRecognizer: VoiceRecognizer
    private val isListeningState = mutableStateOf(false)

    private lateinit var reminderManager: ReminderManager
    private val _popup = MutableStateFlow<ReminderPopup?>(null)
    val popup: StateFlow<ReminderPopup?> = _popup

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    // Пагинация чата
    private var oldestMessageId: Int? = null

    private lateinit var permissionManager: PermissionManager

    private var latestGeo: GeoLocation? = null

    private lateinit var soundPlayer: SoundPlayer

    private val mainViewModel: MainViewModel by viewModels()

    private val playlistViewModel: PlaylistViewModel by viewModels {
        PlaylistViewModelFactory(
            apiService = RetrofitInstance.api,
            accountId = UserProvider.getCurrentUserId(),
            cacheDir = cacheDir,
            application = application  // 🔥 Передаём application для Wake Lock
        )
    }

    private val placesViewModel: PlacesViewModel by viewModels {
        PlacesViewModelFactory(
            placesApi = RetrofitInstance.placesApi,
            statsRepository = StatsRepository(this, RetrofitInstance.placesApi)
        )
    }

    private fun handleLocationResult(geo: GeoLocation) {
        Log.d("Geo", "Location ready: $geo")
        latestGeo = geo
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
        Log.d("ReminderManager","[DEBUG] onNewIntent вызван: action=${intent.action}, extras=${intent.extras?.toString()}")
        setIntent(intent)
        reminderManager.handleReminderIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Victor_AI)

        // 🔹 1. Инициализация голосового ввода
        voiceRecognizer = VoiceRecognizer(
            context = this,
            onTextRecognized = { recognizedText ->
                _chatMessages.value += ChatMessage(text = recognizedText, isUser = true, timestamp = System.currentTimeMillis())
                sendTextToAssistant(recognizedText)
            },
            onListeningStateChanged = { isListening ->
                isListeningState.value = isListening
            }
        )

        // 🔹 2. Инициализация напоминаний
        reminderManager = ReminderManager(
            activity = this,
            api = RetrofitInstance.api,
            onSnackbar = { msg -> _snackbarMessage.value = msg },
            onReminder = { popup -> _popup.value = popup },
            coroutineScope = lifecycleScope  // ✅ Передаём lifecycleScope - корутины отменятся при onDestroy
        )

        // 🔹 4. Permission Manager
        permissionManager = PermissionManager(
            activity = this,
            onAudioGranted = { startVoiceRecognition() },
            onLocationGranted = { startFetchingLocation() }
        )

        permissionManager.register()
        permissionManager.checkAndRequestNotificationPermission()
        permissionManager.requestLocationPermission()


        // 🔹 5. Получение FCM токена
        //FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        //    if (task.isSuccessful) {
        //        val token = task.result
        //        Log.d("FCM", "Текущий токен: $token")
        //        TokenSender.send(token)
        //    } else {
        //        Log.w("FCM", "Fetching FCM registration token failed", task.exception)
        //    }
        //}

        // 🔹 5. Регистрация Pushy
        lifecycleScope.launch {
            registerPushy()
        }

        soundPlayer = SoundPlayer(this)

        mainViewModel.setPlaylistViewModel(playlistViewModel)

        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val snackbarMessage by snackbarMessage.collectAsState()
            val popup by reminderManager.reminderPopup.collectAsState()

            LaunchedEffect(snackbarMessage) {
                snackbarMessage?.let {
                    snackbarHostState.showSnackbar(
                        message = it,
                        duration = SnackbarDuration.Short
                    )
                    _snackbarMessage.value = null
                }
            }

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        snackbar = { data ->
                            Snackbar(
                                modifier = Modifier.padding(16.dp),
                                containerColor = Color(0xFF1C1C1E),
                                contentColor = Color.White
                            ) {
                                Text(text = data.visuals.message)
                            }
                        }
                    )
                }
            ) { innerPadding ->
                AnimatedBackgroundBox {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavHost(
                            navController = navController,
                            playlistViewModel = playlistViewModel,  // 🔥 Передаём
                            placesViewModel = placesViewModel,
                            reminderManager = reminderManager,
                            chatMessages = chatMessages.collectAsState().value,
                            onSendMessage = { userText ->
                                _chatMessages.value += ChatMessage(
                                    userText,
                                    isUser = true,
                                    timestamp = System.currentTimeMillis() / 1000
                                )
                                sendTextToAssistant(userText)
                            },
                            onEditMessage = { index, newText ->
                                _chatMessages.value = _chatMessages.value.toMutableList().apply {
                                    this[index] = this[index].copy(text = newText)
                                }

                                // Отправляем на бэкенд
                                lifecycleScope.launch {
                                    val success = updateChatHistory(_chatMessages.value)
                                    if (success) {
                                        _snackbarMessage.value = "✓ Сообщение обновлено"
                                    } else {
                                        _snackbarMessage.value = "⚠ Ошибка обновления"
                                    }
                                }
                            },
                            onInitHistory = { history ->
                                // Бэкенд отправляет сообщения в правильном порядке
                                // Просто используем их как есть, без сортировки и манипуляций
                                _chatMessages.value = history.toMutableList()

                                Log.d("Chat", "📦 Инициализация: всего ${history.size} сообщений")
                                if (history.isNotEmpty()) {
                                    Log.d("Chat", "📊 Первые 5 IDs: ${history.take(5).map { it.id }}")
                                    Log.d("Chat", "📊 Последние 5 IDs: ${history.takeLast(5).map { it.id }}")
                                }
                            },
                            onPaginationInfo = { oldestId, hasMore ->
                                oldestMessageId = oldestId
                                Log.d("Chat", "📋 Пагинация: oldestId=$oldestId, hasMore=$hasMore")
                            },
                            onLoadMoreHistory = { beforeId ->
                                loadMoreChatHistory(beforeId)
                            },
                            onStartVoiceRecognition = { startVoiceRecognition() },
                            onRequestMicrophone = {
                                permissionManager.requestMicrophonePermission()
                            },
                            isListeningState = isListeningState,
                            isTypingState = isTyping.collectAsState(),
                            permissionManager = permissionManager,
                            onStopListening = { voiceRecognizer.stopListening() }
                        )

                        // 🔹 PresencePlaceholder — только на главном экране
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                        if (currentRoute == "main" || currentRoute == null) {
                            PresencePlaceholder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.End)
                                    .padding(top = 180.dp)
                                    .offset(x = (50).dp) // смещаем влево от края
                            )
                        }

                        // TODO: Добавить PresencePlaceholder на другие экраны после определения правильного расположения
                        // - playlist: "Уснуть под музыку хорошая идея. Ты уже в кровати?"
                        // - places: "Ты же не идёшь гулять, да?"
                        // - calendar: "Просматриваешь планы? Не забудь про отдых."
                        // - system: "Настраиваешь систему? Я помогу, если нужно."

                        // 🔹 AssistantButtonArea — доступна со всех экранов кроме чата
                        if (currentRoute != "chat") {
                            AssistantButtonArea(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                playlistViewModel = playlistViewModel,
                                placesViewModel = placesViewModel,
                                reminderManager = reminderManager,
                                navController = navController,
                                onStartVoiceRecognition = { startVoiceRecognition() },
                                onRequestMicrophone = {
                                    permissionManager.requestMicrophonePermission()
                                },
                                onOpenChat = { navController.navigate("chat") }
                            )
                        }

                        popup?.let {
                            ReminderOverlay(
                                popup = it,
                                onOk = {
                                    reminderManager.sendReminderActionCoroutine("done", it.id)
                                    reminderManager.clearPopup()
                                },
                                onDelay = {
                                    reminderManager.sendReminderActionCoroutine("delay", it.id)
                                    reminderManager.clearPopup()
                                },
                                onDismiss = {
                                    reminderManager.clearPopup()
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    } // ← Закрытие onCreate

    private suspend fun registerPushy() {
        val context: Context = this@MainActivity

        try {
            val deviceToken = withContext<String>(Dispatchers.IO) {
                Pushy.register(context)
            }

            Log.d("Pushy", "Device token: $deviceToken")
            sendTokenToBackend(deviceToken)

        } catch (e: Exception) {
            Log.e("Pushy", "Ошибка регистрации: ${e.message}")
        }
    }

    private suspend fun sendTokenToBackend(token: String) {
        withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val json = """{"user_id":"${UserProvider.getCurrentUserId()}","token":"$token"}"""
                val request = Request.Builder()
                    .url("${RetrofitInstance.BASE_URL}assistant/register_token")
                    .post(json.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                Log.d("Pushy", "Backend response: ${response.code}")
            } catch (e: Exception) {
                Log.e("Pushy", "Ошибка отправки токена: ${e.message}")
            }
        }
    }

    // Остальные методы класса (startVoiceRecognition, sendTextToAssistant и т.д.)

                // ✅ Запуск распознавания речи
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

    private fun startFetchingLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        Log.d("Geo", "Location: $lat, $lon")

                        val geo = GeoLocation(lat = lat, lon = lon)
                        handleLocationResult(geo)
                    } else {
                        Log.w("Geo", "Location is null (disabled or no fix yet)")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Geo", "Failed to get location", e)
                }

        } catch (e: SecurityException) {
            Log.e("Geo", "Location permission missing", e)
        }
    }


    // ✅ Отправляем текст ассистенту
    private fun sendTextToAssistant(text: String) {
        lifecycleScope.launch {
            try {
                _isTyping.value = true // 🔥 Включаем анимацию

                val request = AssistantRequest(
                    sessionId = UserProvider.getCurrentUserId(),
                    text = text,
                    geo = latestGeo
                )

                // 🔥 Сохраняем user сообщение в локальную БД
                val userMessage = _chatMessages.value.last() // последнее сообщение - это user message
                ChatHistoryHelper.repository.addMessage(userMessage.toEntity())

                val assistantMessage = ChatMessage(
                    text = "",
                    isUser = false,
                    timestamp = System.currentTimeMillis() / 1000
                )

                val currentMessages = _chatMessages.value.toMutableList()
                currentMessages.add(assistantMessage)
                val messageIndex = currentMessages.size - 1
                _chatMessages.value = currentMessages

                val charQueue = Channel<Char>(Channel.UNLIMITED)

                // Корутина для печати
                val typingJob = launch {
                    var charCount = 0
                    for (char in charQueue) {
                        val messages = _chatMessages.value.toMutableList()
                        messages[messageIndex] = messages[messageIndex].copy(
                            text = messages[messageIndex].text + char
                        )
                        _chatMessages.value = messages

                        soundPlayer.playKeypress()

                        val progress = (charCount.toFloat() / 15f).coerceAtMost(1f)
                        val delayTime = (48 - (48 - 16) * progress).toLong()

                        delay(delayTime)
                        charCount++
                    }
                    Log.d("Typing", "✅ Печать завершена")
                }

                val streamJob = launch(Dispatchers.IO) {
                    val result = processStreamingMessage(
                        request = request,
                        onChunkReceived = { chunk ->
                            for (char in chunk) {
                                charQueue.send(char)
                            }
                        },
                        onMetadataReceived = { metadata ->
                            val trackId = metadata["track_id"] as? Int
                            if (trackId != null) {
                                Log.d("Assistant", "🎵 Получен track_id: $trackId")
                                // Запускаем воспроизведение трека
                                launch {
                                    mainViewModel.playTrack(trackId)
                                }
                            }
                        }
                    )

                    result.onFailure { error ->
                        Log.e("Assistant", "❌ Ошибка стрима: ${error.message}")
                    }
                }

                streamJob.join()
                charQueue.close()
                typingJob.join()

                _isTyping.value = false // 🔥 Выключаем анимацию

                // 🔥 Сохраняем assistant сообщение в локальную БД
                val finalAssistantMessage = _chatMessages.value[messageIndex]
                ChatHistoryHelper.repository.addMessage(finalAssistantMessage.toEntity())
                Log.d("Assistant", "✅ Сообщения сохранены в локальную БД")

            } catch (e: Exception) {
                Log.e("Assistant", "❌ Ошибка отправки: ${e.message}")
                _isTyping.value = false // 🔥 Выключаем анимацию при ошибке
            }
        }
    }

    // ✅ Загрузка дополнительной истории чата (пагинация)
    private suspend fun loadMoreChatHistory(beforeId: Int): Pair<Boolean, Int?> {
        return withContext(Dispatchers.Main) {
            try {
                Log.d("Chat", "📥 Загрузка истории: beforeId=$beforeId")

                val result = withContext(Dispatchers.IO) {
                    ChatHistoryHelper.repository.loadMoreHistory(beforeId)
                }

                result.onSuccess { response ->
                    Log.d("Chat", "✅ Загружено ${response.messages.size} сообщений, has_more=${response.hasMore}, newOldestId=${response.oldestId}")

                    if (response.messages.isNotEmpty()) {
                        // Бэкенд отправляет старые сообщения в правильном порядке
                        // Просто добавляем их в конец списка (они старше текущих)
                        val currentMessages = _chatMessages.value.toMutableList()
                        currentMessages.addAll(response.messages)
                        _chatMessages.value = currentMessages

                        Log.d("Chat", "📦 Обновлено: всего ${currentMessages.size} сообщений")
                        Log.d("Chat", "📊 Новые IDs: ${response.messages.take(3).map { it.id }}...${response.messages.takeLast(3).map { it.id }}")
                    }

                    return@withContext (response.hasMore to response.oldestId)
                }.onFailure { error ->
                    Log.e("Chat", "❌ Ошибка загрузки истории: ${error.message}")
                    return@withContext (false to null)
                }

                false to null
            } catch (e: Exception) {
                Log.e("Chat", "❌ Ошибка загрузки истории", e)
                false to null
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        voiceRecognizer.destroy()
    }
}

// Маппер ChatMessage -> ChatMessageEntity
private fun ChatMessage.toEntity() = ChatMessageEntity(
    text = text,
    isUser = isUser,
    timestamp = timestamp
)


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Victor_AITheme {
    }
}
