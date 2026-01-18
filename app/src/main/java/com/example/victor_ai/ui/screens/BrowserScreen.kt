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

package com.example.victor_ai.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import android.util.Log
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.rememberCoroutineScope
import com.example.victor_ai.R
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.dto.CareBankSettingsUpdate
import com.example.victor_ai.data.network.dto.TaxiClass
import com.example.victor_ai.data.repository.CareBankRepository
import com.example.victor_ai.ui.components.CoordinatePicker
import com.example.victor_ai.ui.components.MultiCoordinatePicker
import com.example.victor_ai.ui.components.carebank.ui.WebViewSheet
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    modifier: Modifier = Modifier
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    var showBrowserSheet by remember { mutableStateOf(false) }
    
    // Состояние для оверлея координат (выносим на уровень BrowserScreen)
    var showCoordinatePicker by remember { mutableStateOf(false) }
    var showMultiCoordinatePicker by remember { mutableStateOf(false) }
    var coordinatePickerInstruction by remember { mutableStateOf("") }
    var onCoordinateSelected by remember { mutableStateOf<((Int?, Int?) -> Unit)?>(null) }
    var onCoordinatesSelected by remember { mutableStateOf<((List<Pair<Int, Int>>?) -> Unit)?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2B2929)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "/* TODO: Браузер в разработке */",
            color = Color(0xFFFFD700),
            fontSize = 24.sp,
            fontFamily = didactGothic
        )

        // Левый нижний угол: шестерёнка
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = 35.dp,
                    bottom = 38.dp
                )   // ↔ симметрично потом сделаем справа end = 20.dp
                .size(48.dp)
                .offset(y = (-3).dp)
                .background(Color.Transparent, shape = CircleShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    showBrowserSheet = true
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Настройки",
                tint = Color(0xFFA6A6A6),
                modifier = Modifier.size(39.dp) // чтобы вписалась красиво в 48.dp кружок
            )
        }

        // 🔽 Шторка «как у плейлиста»
        if (showBrowserSheet) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )

            ModalBottomSheet(
                onDismissRequest = { showBrowserSheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFF2B2929)
            ) {
                // 👇 вот здесь растягиваем контент на всю высоту
                BrowserSettingsSheet(
                    modifier = Modifier.fillMaxSize(),
                    onShowCoordinatePicker = { instruction, callback ->
                        coordinatePickerInstruction = instruction
                        onCoordinateSelected = callback
                        showCoordinatePicker = true
                    },
                    onShowMultiCoordinatePicker = { instruction, callback ->
                        coordinatePickerInstruction = instruction
                        onCoordinatesSelected = callback
                        showMultiCoordinatePicker = true
                    }
                )
            }
        }
    }
    
    // Оверлей для выбора одной координаты (используем Dialog для рисования ПОВЕРХ ВСЕГО)
    if (showCoordinatePicker) {
        Log.d("BrowserScreen", "🎯 Рисуем CoordinatePicker через Dialog")
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showCoordinatePicker = false },
            properties = androidx.compose.ui.window.DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false // Растягиваем на весь экран
            )
        ) {
            CoordinatePicker(
                onCoordinateSelected = { x, y ->
                    if (x != null && y != null) {
                        Log.d("BrowserScreen", "Координата выбрана: x=$x, y=$y")
                    } else {
                        Log.d("BrowserScreen", "Пункт отсутствует")
                    }
                    onCoordinateSelected?.invoke(x, y)
                    showCoordinatePicker = false
                },
                onDismiss = {
                    Log.d("BrowserScreen", "CoordinatePicker отменен")
                    showCoordinatePicker = false
                },
                modifier = Modifier.fillMaxSize(),
                instruction = coordinatePickerInstruction
            )
        }
    }
    
    // Оверлей для выбора нескольких координат
    if (showMultiCoordinatePicker) {
        Log.d("BrowserScreen", "🎯 Рисуем MultiCoordinatePicker через Dialog")
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showMultiCoordinatePicker = false },
            properties = androidx.compose.ui.window.DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            MultiCoordinatePicker(
                onCoordinatesSelected = { coords ->
                    if (coords != null) {
                        Log.d("BrowserScreen", "Координаты выбраны: ${coords.size} точек")
                    } else {
                        Log.d("BrowserScreen", "Пункт отсутствует")
                    }
                    onCoordinatesSelected?.invoke(coords)
                    showMultiCoordinatePicker = false
                },
                onDismiss = {
                    Log.d("BrowserScreen", "MultiCoordinatePicker отменен")
                    showMultiCoordinatePicker = false
                },
                modifier = Modifier.fillMaxSize(),
                instruction = coordinatePickerInstruction
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserSettingsSheet(
    modifier: Modifier = Modifier,
    viewModel: BrowserViewModel = hiltViewModel(),
    repository: CareBankRepository? = null,
    onShowCoordinatePicker: (instruction: String, callback: (Int?, Int?) -> Unit) -> Unit = { _, _ -> },
    onShowMultiCoordinatePicker: (instruction: String, callback: (List<Pair<Int, Int>>?) -> Unit) -> Unit = { _, _ -> }
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val grayText = Color(0xFF777777)
    val barEmpty = Color(0xFF555555)

    var cappuccinoText by remember { mutableStateOf("") }
    var showWebViewSheet by remember { mutableStateOf(false) }
    var setupMode by remember { mutableStateOf(false) } // Режим настройки автоматизации
    var setupEmoji by remember { mutableStateOf<String?>(null) } // Эмодзи для настройки
    var showCareBankSettingsSheet by remember { mutableStateOf(false) } // Шторка настроек банка заботы
    val careBankEntries by viewModel.careBankEntries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Получаем repository и API через ViewModel
    val repository = viewModel.getRepository()
    val careBankApi = viewModel.getCareBankApi()

    // Получаем сохраненный URL для кнопки курсора
    val savedUrl = careBankEntries.find { it.emoji == "☕" }?.value
    Log.d("BrowserScreen", "savedUrl = $savedUrl, entries count = ${careBankEntries.size}")

    // Синхронизация с бэкендом при открытии шторки
    LaunchedEffect(Unit) {
        viewModel.syncWithBackend()
    }

    // Загружаем сохраненное значение для ☕ в поле ввода
    LaunchedEffect(careBankEntries) {
        val coffeeEntry = careBankEntries.find { it.emoji == "☕" }
        if (coffeeEntry != null && cappuccinoText.isEmpty()) {
            cappuccinoText = coffeeEntry.value
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .nestedScroll(rememberNestedScrollInteropConnection()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Банк заботы",
                fontSize = 20.sp,
                color = grayText,
                fontFamily = didactGothic
            )

            IconButton(
                onClick = { showCareBankSettingsSheet = true },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Настройки банка заботы",
                    tint = grayText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ☕ + контур ввода + кнопка сохранения
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "☕", // можно поменять на любой другой стаканчик, который понравится
                fontSize = 24.sp,
                fontFamily = didactGothic,
                color = grayText
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .border(1.dp, barEmpty, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (cappuccinoText.isEmpty()) {
                    Text(
                        text = "завтрак не вставая с кровати",
                        fontSize = 14.sp,
                        color = Color(0xFF777777),
                        fontFamily = didactGothic,
                        fontStyle = FontStyle.Italic
                    )
                }

                BasicTextField(
                    value = cappuccinoText,
                    onValueChange = { cappuccinoText = it },
                    textStyle = TextStyle(
                        fontFamily = didactGothic,
                        color = grayText,
                        fontSize = 14.sp
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Кнопка сохранения
            Button(
                onClick = {
                    if (cappuccinoText.isNotBlank()) {
                        viewModel.saveEntry("☕", cappuccinoText)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF777777),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading && cappuccinoText.isNotBlank()
            ) {
                Text(
                    text = "+",
                    fontSize = 18.sp,
                    fontFamily = didactGothic
                )
            }

            // Кнопка настройки автоматизации (появляется только если URL сохранен)
            Log.d("BrowserScreen", "Проверяем отображение кнопок: savedUrl != null = ${savedUrl != null}")
            if (savedUrl != null) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        setupMode = true
                        setupEmoji = "☕"
                        showWebViewSheet = true
                        Log.d("BrowserScreen", "Кнопка настройки нажата: setupMode=$setupMode, setupEmoji=$setupEmoji, showWebViewSheet=$showWebViewSheet")
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.TouchApp,
                        contentDescription = "Настроить автоматизацию",
                        tint = Color(0xFF777777),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Показать ошибку, если есть
        errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = didactGothic
            )
        }
    }

    // WebView шторка для открытия сохраненного URL или настройки автоматизации
    if (showWebViewSheet && savedUrl != null) {
        Log.d("BrowserScreen", "Открываем WebViewSheet: setupMode=$setupMode, emoji=$setupEmoji, url=$savedUrl")
        WebViewSheet(
            url = savedUrl,
            onDismiss = {
                showWebViewSheet = false
                setupMode = false
                setupEmoji = null
            },
            enableAutomation = false,
            setupMode = setupMode,
            emoji = setupEmoji,
            repository = repository,
            careBankApi = careBankApi,
            onShowCoordinatePicker = { instruction, callback ->
                onShowCoordinatePicker(instruction, callback)
            },
            onShowMultiCoordinatePicker = { instruction, callback ->
                onShowMultiCoordinatePicker(instruction, callback)
            }
        )
    }

    // Шторка настроек банка заботы
    if (showCareBankSettingsSheet) {
        CareBankSettingsSheet(
            onDismiss = { showCareBankSettingsSheet = false },
            repository = repository
        )
    }
}

/**
 * Шторка настроек банка заботы
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareBankSettingsSheet(
    onDismiss: () -> Unit,
    repository: CareBankRepository?
) {
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val grayText = Color(0xFFE0E0E0)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // Состояние настроек
    var autoApproved by remember { mutableStateOf(false) }
    var presenceAddress by remember { mutableStateOf("") }
    var maxOrderCost by remember { mutableStateOf("") }
    var preferredTaxiClass by remember { mutableStateOf<TaxiClass?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Загружаем текущие настройки при открытии
    LaunchedEffect(Unit) {
        repository?.let { repo ->
            isLoading = true
            repo.getCareBankSettings().fold(
                onSuccess = { settings ->
                    autoApproved = settings.autoApproved
                    presenceAddress = settings.presenceAddress ?: ""
                    maxOrderCost = settings.maxOrderCost?.toString() ?: ""
                    preferredTaxiClass = settings.preferredTaxiClass
                    isLoading = false
                },
                onFailure = { error ->
                    Log.e("CareBankSettingsSheet", "Ошибка загрузки настроек", error)
                    errorMessage = "Не удалось загрузить настройки"
                    isLoading = false
                }
            )
        }
    }

    // Сохраняем настройки при закрытии
    val saveSettings = {
        scope.launch {
            repository?.let { repo ->
                isLoading = true
                val settings = CareBankSettingsUpdate(
                    accountId = UserProvider.getCurrentUserId(),
                    autoApproved = if (autoApproved) true else null,
                    presenceAddress = presenceAddress.takeIf { it.isNotBlank() },
                    maxOrderCost = maxOrderCost.toIntOrNull(),
                    preferredTaxiClass = preferredTaxiClass
                )

                repo.updateCareBankSettings(settings).fold(
                    onSuccess = {
                        Log.d("CareBankSettingsSheet", "Настройки сохранены успешно")
                        isLoading = false
                    },
                    onFailure = { error ->
                        Log.e("CareBankSettingsSheet", "Ошибка сохранения настроек", error)
                        errorMessage = "Не удалось сохранить настройки"
                        isLoading = false
                    }
                )
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            saveSettings()
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = Color(0xFF2B2929)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .nestedScroll(rememberNestedScrollInteropConnection()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Заголовок
            Text(
                text = "Настройки банка заботы",
                fontSize = 20.sp,
                color = grayText,
                fontFamily = didactGothic
            )

            // Автоматическое одобрение
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Автоматическое одобрение",
                    color = grayText,
                    fontFamily = didactGothic,
                    fontSize = 16.sp
                )
                Switch(
                    checked = autoApproved,
                    onCheckedChange = { autoApproved = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4CAF50),
                        checkedTrackColor = Color(0xFF4CAF50).copy(alpha = 0.5f)
                    )
                )
            }

            // Адрес присутствия
            OutlinedTextField(
                value = presenceAddress,
                onValueChange = { presenceAddress = it },
                label = {
                    Text(
                        "Адрес присутствия",
                        color = grayText,
                        fontFamily = didactGothic
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = grayText,
                    unfocusedTextColor = grayText,
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF555555)
                ),
                textStyle = TextStyle(
                    fontFamily = didactGothic,
                    fontSize = 16.sp
                )
            )

            // Максимальная стоимость заказа
            OutlinedTextField(
                value = maxOrderCost,
                onValueChange = { maxOrderCost = it },
                label = {
                    Text(
                        "Макс. стоимость заказа (руб)",
                        color = grayText,
                        fontFamily = didactGothic
                    )
                },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = grayText,
                    unfocusedTextColor = grayText,
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF555555)
                ),
                textStyle = TextStyle(
                    fontFamily = didactGothic,
                    fontSize = 16.sp
                )
            )

            // Предпочитаемый класс такси
            Text(
                text = "Предпочитаемый класс такси",
                color = grayText,
                fontFamily = didactGothic,
                fontSize = 16.sp
            )

            TaxiClass.values().forEach { taxiClass ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = when (taxiClass) {
                            TaxiClass.COMFORT_PLUS -> "Комфорт+"
                            TaxiClass.COMFORT -> "Комфорт"
                            TaxiClass.ECONOMY -> "Эконом"
                            TaxiClass.BUSINESS -> "Бизнес"
                            TaxiClass.MINIVAN -> "Минивэн"
                        },
                        color = grayText,
                        fontFamily = didactGothic,
                        fontSize = 14.sp
                    )
                    RadioButton(
                        selected = preferredTaxiClass == taxiClass,
                        onClick = { preferredTaxiClass = taxiClass },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF4CAF50)
                        )
                    )
                }
            }

            // Сообщение об ошибке
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontFamily = didactGothic
                )
            }

            // Индикатор загрузки
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

