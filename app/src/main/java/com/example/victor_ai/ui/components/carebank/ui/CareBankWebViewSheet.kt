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

package com.example.victor_ai.ui.components.carebank.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.webkit.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.victor_ai.R
import com.example.victor_ai.data.network.CareBankApi
import com.example.victor_ai.data.repository.CareBankRepository
import com.example.victor_ai.ui.components.carebank.actions.executeAutomationScenario
import com.example.victor_ai.ui.components.carebank.actions.executeCareBankAutomation
import com.example.victor_ai.ui.components.carebank.actions.hideKeyboard
import com.example.victor_ai.ui.components.carebank.actions.waitUntilPageIsReallyReady
import com.example.victor_ai.ui.components.carebank.setup.handleCoordinateSelected
import com.example.victor_ai.ui.components.carebank.setup.handleCoordinatesSelected
import com.example.victor_ai.ui.components.carebank.setup.handleUserAnswer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * WebView шторка для открытия ссылок внутри приложения с автоматизацией Care Bank
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewSheet(
    url: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    enableAutomation: Boolean = false, // Флаг для включения/выключения автоматизации
    automationScenario: SearchScenario? = null, // Сценарий для автоматизации (будет приходить с бэкенда)
    setupMode: Boolean = false, // Режим настройки автоматизации
    emoji: String? = null, // Эмодзи для которого настраивается автоматизация
    repository: CareBankRepository? = null, // Репозиторий для сохранения настроек
    careBankApi: CareBankApi? = null, // API для отправки скриншотов
    onShowCoordinatePicker: (instruction: String, callback: (Int?, Int?) -> Unit) -> Unit = { _, _ -> }, // Callback для показа оверлея на уровне BrowserScreen
    onShowMultiCoordinatePicker: (instruction: String, callback: (List<Pair<Int, Int>>?) -> Unit) -> Unit = { _, _ -> }, // Callback для мульти-оверлея
    onAddChatMessage: (String) -> Unit = {}, // Callback для добавления сообщения в чат
    onSendSystemEvent: (String) -> Unit = {}, // Callback для отправки системного события
    automationData: Map<String, String> = emptyMap() // Данные автоматизации от бэкенда
) {
    Log.d("WebViewSheet", "WebViewSheet created with: url=$url, setupMode=$setupMode, emoji=$emoji, repository=${repository != null}")
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var currentUrl by remember { mutableStateOf(url) }
    var jarvisMessage by remember(setupMode) {
        mutableStateOf(if (setupMode) "тапни в поле поиска и нажми готово" else "ищу... 👀")
    } // Сообщение от Джарвиса

    // Состояние настройки автоматизации
    var showCoordinatePicker by remember { mutableStateOf(false) }
    var showMultiCoordinatePicker by remember { mutableStateOf(false) }
    var setupStep by remember(setupMode) {
        mutableStateOf(if (setupMode) 0 else -1)
    } // Текущий шаг настройки
    var savedSearchText by remember { mutableStateOf<String?>(null) } // Сохраненный текст для тестирования
    var savedSearchUrl by remember { mutableStateOf<String?>(null) } // Сохраненный URL поиска
    var savedSearchFieldCoords by remember { mutableStateOf<String?>(null) } // Сохраненные координаты поля поиска
    var webViewBounds by remember { mutableStateOf<Rect?>(null) } // Позиция WebView на экране (screen координаты)

    // Логируем состояние UI элементов
    Log.d("WebViewSheet", "UI State: setupMode=$setupMode, setupStep=$setupStep, showCoordinatePicker=$showCoordinatePicker, showMultiCoordinatePicker=$showMultiCoordinatePicker")

    // Логируем текущие значения состояния
    Log.d("WebViewSheet", "Current state: setupMode=$setupMode, setupStep=$setupStep")

    // Скрываем клавиатуру перед показом CoordinatePicker
    LaunchedEffect(showCoordinatePicker) {
        if (showCoordinatePicker && webViewRef != null) {
            Log.d("WebViewSheet", "🎯 Скрываем клавиатуру перед показом CoordinatePicker")
            hideKeyboard(webViewRef!!, context, 0L) {
                Log.d("WebViewSheet", "✅ Клавиатура скрыта, вызываем onShowCoordinatePicker")
                onShowCoordinatePicker(com.example.victor_ai.ui.components.carebank.setup.getCoordinatePickerInstruction(setupStep)) { x, y ->
                    // Обрабатываем выбранную координату (x, y - screen координаты)
                    handleCoordinateSelected(
                        x = x,
                        y = y,
                        setupMode = setupMode,
                        repository = repository,
                        emoji = emoji,
                        currentSetupStep = setupStep,
                        currentSavedSearchText = savedSearchText,
                        currentSavedSearchUrl = savedSearchUrl,
                        updateState = { newMessage, newStep, newShowPicker, newShowMultiPicker ->
                            jarvisMessage = newMessage
                            setupStep = newStep
                            showCoordinatePicker = newShowPicker
                            showMultiCoordinatePicker = newShowMultiPicker
                        },
                        onCoordsSaved = { coords ->
                            savedSearchFieldCoords = coords
                        },
                        webView = webViewRef,
                        context = context,
                        webViewBounds = webViewBounds
                    )
                }
            }
        }
    }
    
    // Скрываем клавиатуру перед показом MultiCoordinatePicker
    LaunchedEffect(showMultiCoordinatePicker) {
        if (showMultiCoordinatePicker && webViewRef != null) {
            Log.d("WebViewSheet", "🎯 Скрываем клавиатуру перед показом MultiCoordinatePicker")
            hideKeyboard(webViewRef!!, context, 0L) {
                Log.d("WebViewSheet", "✅ Клавиатура скрыта, вызываем onShowMultiCoordinatePicker")
                onShowMultiCoordinatePicker("Перетащи кружочки на кнопки 'добавить в корзину'") { coords ->
                    // Обрабатываем выбранные координаты (coords - screen координаты)
                    handleCoordinatesSelected(
                        coords = coords,
                        setupMode = setupMode,
                        repository = repository,
                        emoji = emoji,
                        currentSetupStep = setupStep,
                        currentSavedSearchText = savedSearchText,
                        currentSavedSearchUrl = savedSearchUrl,
                        webView = webViewRef,
                        context = context,
                        updateState = { newMessage, newStep, newShowPicker, newShowMultiPicker ->
                            jarvisMessage = newMessage
                            setupStep = newStep
                            showCoordinatePicker = newShowPicker
                            showMultiCoordinatePicker = newShowMultiPicker
                        },
                        webViewBounds = webViewBounds
                    )
                }
            }
        }
    }
    
    // Auto-close WebViewSheet after step 5 (setup complete)
    LaunchedEffect(setupStep) {
        if (setupStep == 5) {
            Log.d("WebViewSheet", "✅ Setup complete, closing in 2.5 seconds...")
            kotlinx.coroutines.delay(2500)
            onDismiss()
        }
    }

    // Инициализация режима настройки
    SideEffect {
        Log.d("WebViewSheet", "SideEffect: setupMode=$setupMode, emoji=$emoji, jarvisMessage=$jarvisMessage, setupStep=$setupStep")
        if (setupMode && setupStep == 0) {
            Log.d("WebViewSheet", "Режим настройки активирован для эмодзи: $emoji")
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF2B2929),
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Заголовок
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Браузер",
                    fontSize = 18.sp,
                    color = Color(0xFFE0E0E0),
                    fontFamily = didactGothic
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = Color(0xFFE0E0E0)
                    )
                }
            }

            // Фиксированный диалог с Джарвисом - не расширяется, не двигается
            JarvisOneLineDialog(
                message = jarvisMessage, // Динамическое сообщение от Джарвиса
                onUserAnswer = { answer ->
                    handleUserAnswer(
                        answer = answer,
                        setupMode = setupMode,
                        repository = repository,
                        emoji = emoji,
                        currentSetupStep = setupStep,
                        currentSavedSearchText = savedSearchText,
                        currentSavedSearchUrl = savedSearchUrl,
                        currentSavedSearchFieldCoords = savedSearchFieldCoords,
                        currentUrl = currentUrl,
                        webView = webViewRef,
                        context = context,
                        updateState = { newMessage, newStep, showPicker, showMultiPicker ->
                            Log.d("WebViewSheet", "updateState вызван: message='$newMessage', step=$newStep, showPicker=$showPicker, showMultiPicker=$showMultiPicker")
                            jarvisMessage = newMessage
                            setupStep = newStep
                            showCoordinatePicker = showPicker
                            showMultiCoordinatePicker = showMultiPicker
                            Log.d("WebViewSheet", "После updateState: jarvisMessage='$jarvisMessage', setupStep=$setupStep, showCoordinatePicker=$showCoordinatePicker")
                        },
                        webViewBounds = webViewBounds
                    )
                },
                modifier = Modifier.wrapContentHeight()
            )

            HorizontalDivider(color = Color(0xFF444444), thickness = 1.dp)

            // Прогресс
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF4CAF50)
                )
            }

            // WebView (с тапами и вставкой)
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            allowFileAccess = true
                            allowContentAccess = true
                            setSupportMultipleWindows(true)
                            javaScriptCanOpenWindowsAutomatically = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            builtInZoomControls = true
                            displayZoomControls = false
                            setSupportZoom(true)
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            mediaPlaybackRequiresUserGesture = false

                            // User Agent - делаем вид что это обычный браузер
                            userAgentString = "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36"
                        }

                        val cookieManager = CookieManager.getInstance()
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setAcceptThirdPartyCookies(this, true)

                        // Флаг чтобы не выполнять автозаполнение дважды
                        var autoFillExecuted = false

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                                currentUrl = url ?: ""
                                Log.d("WebViewSheet", "Начало загрузки: $url")
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                Log.d("WebViewSheet", "Загрузка завершена: $url")

                                // Для режима настройки шаг 0 - просто логируем загрузку
                                if (setupMode && setupStep == 0) {
                                    Log.d("WebViewSheet", "Страница загружена в режиме настройки: $url")
                                }

                                // Проверяем, нужна ли автоматизация
                                if (!enableAutomation || autoFillExecuted) {
                                    if (autoFillExecuted) {
                                        Log.d("WebViewSheet", "⏭️ Автоматизация уже выполнена, пропускаем")
                                    }
                                    return
                                }

                                // ← ТУТ ВСЁ И НАЧИНАЕТСЯ! После полной загрузки и рендера страницы
                                Log.d("WebViewSheet", "🚀 Запускаем waitUntilPageIsReallyReady для URL: $url")
                                // Устанавливаем флаг СРАЗУ, чтобы предотвратить двойной вызов
                                autoFillExecuted = true
                                
                                // Определяем сценарий (приоритет: параметр функции -> сценарий из списка)
                                val scenario = automationScenario ?: scenarios.getOrNull(currentScenarioIndex)
                                
                                if (scenario == null) {
                                    Log.e("WebViewSheet", "❌ Сценарий не найден, автоматизация отменена")
                                    jarvisMessage = "Хмм. Что-то пошло не так 🤔"
                                    return
                                }
                                
                                waitUntilPageIsReallyReady(view) {
                                    Log.d("WebViewSheet", "ГОТОВО! Страница 100% отрендерена, запускаем оркестратор ❤️")
                                    
                                    // ╔══════════════════════════════════════════════════════════
                                    // ║           ЗАПУСК ОРКЕСТРАТОРА АВТОМАТИЗАЦИИ
                                    // ╚══════════════════════════════════════════════════════════
                                    view?.let { webView ->
                                        if (setupMode) {
                                            // Режим настройки - старая логика с простым сценарием
                                            if (careBankApi == null) {
                                                Log.e("WebViewSheet", "❌ careBankApi is null")
                                                jarvisMessage = "Хмм. Что-то пошло не так 🤔"
                                                return@waitUntilPageIsReallyReady
                                            }
                                            executeAutomationScenario(
                                                webView = webView,
                                                context = context,
                                                scenario = scenario,
                                                careBankApi = careBankApi,
                                                onComplete = {
                                                    Log.d("WebViewSheet", "🎉 Автоматизация завершена успешно")
                                                    jarvisMessage = "готово! ✨"
                                                },
                                                onError = {
                                                    Log.e("WebViewSheet", "❌ Ошибка в процессе автоматизации")
                                                    jarvisMessage = "Хмм. Что-то пошло не так 🤔"
                                                }
                                            )
                                        } else {
                                            // Автоматизация от бэкенда - новый оркестратор
                                            Log.d("WebViewSheet", "🤖 Запуск автоматизации от бэкенда с ${automationData.size} элементами")
                                            
                                            if (repository == null || emoji == null || careBankApi == null) {
                                                Log.e("WebViewSheet", "❌ Отсутствует repository, emoji или careBankApi для автоматизации")
                                                jarvisMessage = "Хмм. Что-то пошло не так 🤔"
                                                return@waitUntilPageIsReallyReady
                                            }
                                            
                                            // Загружаем CareBankEntry из репозитория
                                            kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
                                                val careBankEntry = repository.getEntryByEmoji(emoji)
                                                
                                                if (careBankEntry == null) {
                                                    Log.e("WebViewSheet", "❌ Не найдена запись Care Bank для emoji: $emoji")
                                                    kotlinx.coroutines.withContext(Dispatchers.Main) {
                                                        jarvisMessage = "Хмм. Что-то пошло не так 🤔"
                                                    }
                                                    return@launch
                                                }
                                                
                                                kotlinx.coroutines.withContext(Dispatchers.Main) {
                                                    executeCareBankAutomation(
                                                        webView = webView,
                                                        context = context,
                                                        careBankEntry = careBankEntry,
                                                        automationData = automationData,
                                                        repository = repository,
                                                        careBankApi = careBankApi,
                                                        onJarvisMessage = { message ->
                                                            jarvisMessage = message
                                                        },
                                                        onAddChatMessage = onAddChatMessage,
                                                        onSendSystemEvent = onSendSystemEvent,
                                                        onComplete = {
                                                            Log.d("WebViewSheet", "🎉 Автоматизация завершена, закрываем WebView")
                                                            onDismiss()
                                                        },
                                                        onError = { error ->
                                                            Log.e("WebViewSheet", "❌ Ошибка автоматизации: $error")
                                                            jarvisMessage = "Хмм. $error 🤔"
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                Log.e("WebViewSheet", "Ошибка загрузки: ${error?.description}")
                                isLoading = false
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                return false
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                isLoading = newProgress < 100
                            }

                            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                                Log.d("WebViewSheet", "Console: ${consoleMessage?.message()}")
                                return true
                            }
                        }

                        Log.d("WebViewSheet", "Загрузка URL: $url")
                        loadUrl(url)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        val posInWindow = coordinates.positionInWindow()
                        val size = coordinates.size
                        webViewBounds = Rect(
                            left = posInWindow.x,
                            top = posInWindow.y,
                            right = posInWindow.x + size.width,
                            bottom = posInWindow.y + size.height
                        )
                        Log.d("WebViewSheet", "📏 WebView bounds: $webViewBounds")
                    },
                update = { webView ->
                    webViewRef = webView
                    if (webView.url != url) {
                        webView.loadUrl(url)
                    }
                }
            )
        }
    }
}

/**
 * Фиксированная строка диалога с Джарвисом - для управления поиском
 * ВАЖНО: Все высоты фиксированы чтобы WebView не сдвигался при фокусе!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JarvisOneLineDialog(
    message: String = "ищу... 👀", // Динамическое сообщение от Джарвиса
    onUserAnswer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var userInput by remember { mutableStateOf("") }

    // ФИКСИРОВАННАЯ высота всего компонента = 90dp (38dp сообщение + 52dp поле ввода)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp) // ФИКСИРОВАННАЯ ВЫСОТА!
    ) {
        // === Динамическое сообщение от Джарвиса ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp) // ФИКСИРОВАННАЯ высота строки сообщения
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\uD83E\uDD16: $message",
                color = Color(0xFFAAAAAA),
                fontSize = 13.sp,
                maxLines = 1, // Не переносить на новую строку
                modifier = Modifier
                    .background(Color(0xFF3A3A3A), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // === Поле ввода + кнопка отправить ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp) // ФИКСИРОВАННАЯ высота
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // BasicTextField с кастомной декорацией - не прыгает при фокусе
            BasicTextField(
                value = userInput,
                onValueChange = { userInput = it },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = Color.White
                ),
                cursorBrush = SolidColor(Color(0xFF4CAF50)),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .background(Color(0xFF333333), RoundedCornerShape(18.dp))
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (userInput.isEmpty()) {
                            Text("...", color = Color.Gray, fontSize = 13.sp)
                        }
                        innerTextField()
                    }
                }
            )

            // Кнопка "отправить" — всегда занимает место, но видна только когда есть текст
            IconButton(
                onClick = {
                    if (userInput.isNotBlank()) {
                        onUserAnswer(userInput.trim())
                        userInput = "" // очищаем поле после отправки
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .alpha(if (userInput.isNotBlank()) 1f else 0f), // Невидимая когда нет текста, но место занимает
                enabled = userInput.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Отправить",
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}

