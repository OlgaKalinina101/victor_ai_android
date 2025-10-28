package com.example.victor_ai.ui.screens

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.victor_ai.ui.memories.MemoriesViewModel
import com.example.victor_ai.data.network.dto.MemoryResponse
import com.example.victor_ai.data.network.AssistantMind
import com.example.victor_ai.data.network.AssistantState
import com.example.victor_ai.data.network.ModelUsage
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.network.RetrofitInstance.assistantApi
import com.example.victor_ai.logic.UsageRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SystemMenuScreen(
    usageRepository: UsageRepository,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val apiService = RetrofitInstance.apiService

    var modelUsageList by remember { mutableStateOf<List<ModelUsage>>(emptyList()) }
    var isOnline by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(true) }

    var assistantStateList by remember { mutableStateOf<List<AssistantState>>(emptyList()) }
    var assistantState by remember { mutableStateOf<String?>(null) }
    var assistantMind by remember { mutableStateOf<List<AssistantMind>>(emptyList()) }

    val emotionalShift = if (assistantStateList.size >= 3) {
        assistantStateList
            .takeLast(10) // можно взять чуть больше, чтобы были данные для уникальности
            .distinctBy { it.state } // убираем дубликаты, сохраняя порядок
            .takeLast(2) // а затем берём последние 3 уникальных
            .joinToString(" → ") { it.state }
    } else {
        "Недостаточно данных"
    }



    LaunchedEffect(true) {
        isChecking = true
        isOnline = try {
            val response = apiService.checkConnection()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
        isChecking = false

        modelUsageList = usageRepository.getModelUsage("test_user")

        // ✅ Получение состояния и фокусов
        try {
            val stateResponse = assistantApi.getAssistantState("test_user")
            assistantStateList = stateResponse
            assistantState = stateResponse.lastOrNull()?.state

            assistantMind = assistantApi.getAssistantMind("test_user")
                .filter { it.type == "focus" || it.type == "anchor" }

            Log.d("SystemMenu", "Получен список состояний: $assistantStateList")

        } catch (e: Exception) {
            Log.e("SystemMenu", "Ошибка запроса состояния или mind: ${e.message}")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SystemStatusCard(
            isOnline = isOnline,
            isChecking = isChecking,
            modelUsageList = modelUsageList,
            assistantState = assistantState,
            emotionalShift = emotionalShift,
            assistantMind = assistantMind
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemStatusCard(
    isOnline: Boolean,
    isChecking: Boolean,
    modelUsageList: List<ModelUsage>,
    assistantState: String?,
    emotionalShift: String,
    assistantMind: List<AssistantMind>,
    modifier: Modifier = Modifier
) {
    val grayText = Color(0xFFE0E0E0)
    val backgroundCard = Color.Transparent
    val barFilled = Color(0xFFCCCCCC)
    val barEmpty = Color(0xFF555555)
    val fontSize = 18.sp

    // Группировка по провайдеру
    val usageByProvider = modelUsageList.groupBy { it.provider }

    // Состояние для ModalBottomSheet
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showMemoriesSheet by remember { mutableStateOf(false) }

    // ViewModel для воспоминаний
    val viewModel: MemoriesViewModel = viewModel()
    val memories by viewModel.memories.observeAsState(initial = emptyList())
    val error by viewModel.error.observeAsState(initial = null)
    val loading by viewModel.loading.observeAsState(initial = false)

    // Состояние для Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Основной Column с явным указанием высоты
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight() // Ограничиваем высоту контента
            .padding(16.dp)
    ) {
        // 📶 Связь
        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundCard),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Связь:", fontSize = fontSize, color = grayText)
                Spacer(modifier = Modifier.height(8.dp))
                when {
                    isChecking -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = grayText
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Загрузка...", color = grayText, fontSize = fontSize)
                        }
                    }
                    isOnline -> {
                        Text("Связь стабильная ✅", color = Color(0xFF77FF77), fontSize = fontSize)
                    }
                    else -> {
                        Text("Нет подключения ❌", color = Color(0xFFFF7777), fontSize = fontSize)
                    }
                }
            }
        }

        // 🎯 Баланс токенов по провайдерам
        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundCard),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Баланс токенов:", fontSize = fontSize, color = grayText)
                Spacer(modifier = Modifier.height(8.dp))
                if (usageByProvider.isEmpty()) {
                    Text("Нет данных", fontSize = fontSize, color = grayText)
                } else {
                    // Состояние для dropdown
                    var expanded by remember { mutableStateOf(false) }
                    var selectedProvider by remember { mutableStateOf(usageByProvider.keys.firstOrNull() ?: "") }

                    // Кнопка выбора провайдера
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight() // Ограничиваем высоту
                    ) {
                        OutlinedButton(
                            onClick = {
                                Log.d("SystemMenu", "Кнопка провайдера кликнута")
                                expanded = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = grayText
                            ),
                            border = BorderStroke(1.dp, Color(0xFF555555))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🌐 $selectedProvider", fontSize = fontSize)
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = grayText
                                )
                            }
                        }
                        // Выпадающий список
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color(0xFF2B2929))
                                .wrapContentHeight() // Ограничиваем высоту меню
                        ) {
                            usageByProvider.keys.forEach { provider ->
                                DropdownMenuItem(
                                    text = { Text("🌐 $provider", color = grayText, fontSize = fontSize) },
                                    onClick = {
                                        selectedProvider = provider
                                        expanded = false
                                        Log.d("SystemMenu", "Выбран провайдер: $provider")
                                    },
                                    colors = MenuDefaults.itemColors(textColor = grayText)
                                )
                            }
                        }
                    }

                    // Отображаем баланс выбранного провайдера
                    Spacer(modifier = Modifier.height(12.dp))
                    val entries = usageByProvider[selectedProvider] ?: emptyList()
                    if (entries.isNotEmpty()) {
                        val totalSpent = entries.sumOf {
                            (it.input_tokens_used * it.input_token_price + it.output_tokens_used * it.output_token_price).toDouble()
                        }
                        val balance = entries.first().account_balance.toDouble().coerceAtLeast(0.01)
                        val percentRemaining = (1.0 - totalSpent / balance).coerceIn(0.0, 1.0)
                        val blocks = (percentRemaining * 10).toInt()
                        Row {
                            repeat(blocks) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(barFilled)
                                        .padding(1.dp)
                                )
                            }
                            repeat(10 - blocks) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(barEmpty)
                                        .padding(1.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "${(percentRemaining * 100).toInt()}% осталось",
                            fontSize = fontSize,
                            color = grayText
                        )
                    }
                }
            }
        }

        // 🧠 Состояние Victor AI
        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundCard),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Настроение Victor AI:", fontSize = fontSize, color = grayText)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    assistantState ?: "Неизвестно",
                    fontSize = fontSize,
                    color = grayText
                )
            }
        }

        // 🧠 Эмоциональный сдвиг
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = backgroundCard),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🌀 Эмоциональный сдвиг:", fontSize = fontSize, color = grayText)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = emotionalShift,
                    fontSize = fontSize,
                    color = grayText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // 💭 Мысли (фокусы и якоря)
        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundCard),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight() // Ограничиваем высоту карточки
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        Log.d("SystemMenu", "Карточка Мысли кликнута")
                        showMemoriesSheet = true
                    }
                ),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("\uD83E\uDDE0 Мысли:", fontSize = fontSize, color = grayText)
                Spacer(modifier = Modifier.height(8.dp))
                if (assistantMind.isEmpty()) {
                    Text("Нет активных фокусов", fontSize = fontSize, color = grayText)
                } else {
                    val textFlow = assistantMind.joinToString(" ... ") { it.mind }
                    InfiniteMarqueeText(
                        text = textFlow,
                        fontSize = fontSize,
                        color = grayText
                    )
                }
            }
        }

        // ModalBottomSheet для воспоминаний
        if (showMemoriesSheet) {
            Log.d("SystemMenu", "ModalBottomSheet отображается")
            Box(
                modifier = Modifier
                    .heightIn(max = screenHeight * 6 / 6)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {
                        detectTapGestures { /* ничего не делаем */ }
                    }
            ) {
                ModalBottomSheet(
                    onDismissRequest = {
                        Log.d("SystemMenu", "Шторка закрыта через onDismissRequest")
                        showMemoriesSheet = false
                    },
                    sheetState = sheetState,
                    containerColor = Color(0xFF2B2929),
                    contentColor = Color(0xFFE0E0E0),
                    scrimColor = Color.Transparent,
                    dragHandle = {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .width(32.dp)
                                .height(4.dp)
                                .background(Color(0xFF555555), shape = RoundedCornerShape(2.dp))
                        )
                    }
                ) {
                    Log.d("SystemMenu", "Внутри контента ModalBottomSheet")
                    MemoriesSheet(
                        memories = memories,
                        loading = loading,
                        error = error,
                        onDelete = { recordId ->
                            viewModel.deleteMemories("test_user", listOf(recordId))
                        },
                        onUpdate = { id, newText ->
                            val memory = memories.find { it.id == id }
                            if (memory != null) {
                                viewModel.updateMemory(id, "test_user", newText, memory.metadata)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Показ ошибок через Snackbar
        error?.let { errorMessage ->
            LaunchedEffect(errorMessage) {
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.clearError()
            }
        }

        // SnackbarHost
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    // Загрузка воспоминаний при открытии BottomSheet
    LaunchedEffect(showMemoriesSheet) {
        if (showMemoriesSheet) {
            Log.d("SystemMenu", "Запрашиваем воспоминания")
            viewModel.fetchMemories("test_user")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoriesSheet(
    memories: List<MemoryResponse>,
    loading: Boolean,
    error: String?,
    onDelete: (String) -> Unit,
    onUpdate: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val grayText = Color(0xFFE0E0E0)
    val backgroundCard = Color.Transparent
    val barFilled = Color(0xFFCCCCCC)
    val barEmpty = Color(0xFF555555)
    val fontSize = 18.sp

    // Состояния для фильтров
    var hasCriticalFilter by remember { mutableStateOf<Boolean?>(null) }
    var categoryFilter by remember { mutableStateOf<String?>(null) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    // Состояния для сортировки
    var sortBy by remember { mutableStateOf("last_used") }
    var showSortDropdown by remember { mutableStateOf(false) }

    // Состояние списка для скролла
    val listState = rememberLazyListState()

    // Уникальные категории для фильтра
    val categories = memories.map { it.metadata["category"]?.toString() ?: "Без категории" }
        .distinct()
        .sorted()

    // Фильтрация и сортировка воспоминаний
    val filteredAndSortedMemories = memories
        .filter { memory ->
            val hasCritical = memory.metadata["has_critical"] as? Boolean ?: false
            val category = memory.metadata["category"]?.toString() ?: "Без категории"
            (hasCriticalFilter == null || hasCritical == hasCriticalFilter) &&
                    (categoryFilter == null || category == categoryFilter)
        }
        .sortedByDescending { memory ->
            when (sortBy) {
                "impressive" -> {
                    val value = memory.metadata["impressive"]
                    when (value) {
                        is Int -> value.toLong()
                        is Double -> value.toLong()
                        is String -> value.toDoubleOrNull()?.toLong() ?: 0L
                        else -> 0L
                    }
                }
                "frequency" -> {
                    val value = memory.metadata["frequency"]
                    when (value) {
                        is Int -> value.toLong()
                        is Double -> value.toLong()
                        is String -> value.toDoubleOrNull()?.toLong() ?: 0L
                        else -> 0L
                    }
                }
                "last_used" -> {
                    val lastUsed = memory.metadata["last_used"]?.toString()
                    if (lastUsed != null) {
                        try {
                            ZonedDateTime.parse(lastUsed).toEpochSecond()
                        } catch (e: Exception) {
                            0L
                        }
                    } else {
                        0L
                    }
                }
                else -> 0L
            }
        }

    // Автоскролл при смене сортировки
    LaunchedEffect(sortBy) {
        if (filteredAndSortedMemories.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .nestedScroll(rememberNestedScrollInteropConnection()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Заголовок
        Text(
            text = "Воспоминания",
            fontSize = 20.sp,
            color = grayText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Фильтры и сортировка — два уровня
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Первая строка: чекбокс + категория
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Фильтр по has_critical
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = hasCriticalFilter == true,
                        onCheckedChange = { checked ->
                            hasCriticalFilter = if (checked) true else null
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF77FF77),
                            uncheckedColor = barEmpty,
                            checkmarkColor = Color.Black
                        )
                    )
                    Text("Критичные", fontSize = 14.sp, color = grayText)
                }

                // Фильтр по категории
                Box {
                    OutlinedButton(
                        onClick = { showCategoryDropdown = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = grayText
                        ),
                        border = BorderStroke(1.dp, barEmpty),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(
                            text = categoryFilter ?: "Все категории",
                            fontSize = 14.sp,
                            color = grayText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    DropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все категории", fontSize = 14.sp) },
                            onClick = {
                                categoryFilter = null
                                showCategoryDropdown = false
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category, fontSize = 14.sp) },
                                onClick = {
                                    categoryFilter = category
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Вторая строка: сортировка
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { showSortDropdown = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = grayText
                    ),
                    border = BorderStroke(1.dp, barEmpty),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        text = when (sortBy) {
                            "impressive" -> "Сортировка: По значимости"
                            "frequency" -> "Сортировка: По частоте"
                            "last_used" -> "Сортировка: По дате"
                            else -> "Сортировка: По дате"
                        },
                        fontSize = 14.sp,
                        color = grayText
                    )
                }
                DropdownMenu(
                    expanded = showSortDropdown,
                    onDismissRequest = { showSortDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("По значимости", fontSize = 14.sp) },
                        onClick = {
                            sortBy = "impressive"
                            showSortDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("По частоте", fontSize = 14.sp) },
                        onClick = {
                            sortBy = "frequency"
                            showSortDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("По дате", fontSize = 14.sp) },
                        onClick = {
                            sortBy = "last_used"
                            showSortDropdown = false
                        }
                    )
                }
            }
        }

        // Список воспоминаний
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
                strokeWidth = 2.dp,
                color = barFilled
            )
        } else if (error != null) {
            Text(
                text = "Ошибка: $error",
                fontSize = fontSize,
                color = Color(0xFFFF7777),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (filteredAndSortedMemories.isEmpty()) {
            Text(
                text = "Нет воспоминаний",
                fontSize = fontSize,
                color = barEmpty,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                state = listState, // ← добавлено
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredAndSortedMemories, key = { it.id }) { memory ->
                    MemoryItem(
                        memory = memory,
                        onDelete = { recordId ->
                            onDelete(recordId)
                        },
                        onUpdate = { id, newText -> onUpdate(id, newText) },
                        fontSize = fontSize,
                        grayText = grayText,
                        barEmpty = barEmpty,
                        backgroundCard = backgroundCard,
                    )
                }
            }
        }
    }
}

@Composable
fun MemoryItem(
    memory: MemoryResponse,
    onDelete: (String) -> Unit,
    onUpdate: (String, String) -> Unit, // ← новый коллбэк (id, новый текст)
    fontSize: TextUnit,
    grayText: Color,
    barEmpty: Color,
    backgroundCard: Color
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedText by remember { mutableStateOf(memory.text) }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundCard),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, barEmpty),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Текст воспоминания (редактируемый или обычный)
            if (isEditing) {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = fontSize,
                        color = grayText
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF77FF77),
                        unfocusedBorderColor = barEmpty,
                        cursorColor = grayText,
                        focusedTextColor = grayText,
                        unfocusedTextColor = grayText
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            } else {
                Text(
                    text = memory.text,
                    fontSize = fontSize,
                    color = grayText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { isEditing = true } // ← клик для редактирования
                )
            }

            // Разделитель
            HorizontalDivider(
                thickness = 1.dp,
                color = barEmpty.copy(alpha = 0.3f)
            )

            // Метаданные + действия
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Метаданные (только если не редактируем)
                if (!isEditing) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "📁 ${memory.metadata["category"]?.toString() ?: "Без категории"}",
                            fontSize = 13.sp,
                            color = grayText.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "⭐ ${memory.metadata["impressive"]?.toString() ?: "0"} | 🔄 ${memory.metadata["frequency"]?.toString() ?: "0"}",
                            fontSize = 13.sp,
                            color = grayText.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "🕒 ${
                                memory.metadata["last_used"]?.toString()?.let {
                                    try {
                                        ZonedDateTime.parse(it).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                                    } catch (e: Exception) {
                                        "—"
                                    }
                                } ?: "—"
                            }",
                            fontSize = 13.sp,
                            color = grayText.copy(alpha = 0.8f)
                        )
                    }

                    // Кнопка удаления
                    IconButton(
                        onClick = { onDelete(memory.id) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint = Color(0xFFFF7777)
                        )
                    }
                } else {
                    // Кнопки сохранить/отменить при редактировании
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Отмена
                        IconButton(
                            onClick = {
                                editedText = memory.text
                                isEditing = false
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Отмена",
                                tint = Color(0xFFFF7777)
                            )
                        }

                        // Сохранить
                        IconButton(
                            onClick = {
                                if (editedText.isNotBlank() && editedText != memory.text) {
                                    onUpdate(memory.id, editedText)
                                }
                                isEditing = false
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Сохранить",
                                tint = Color(0xFF77FF77)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun InfiniteMarqueeText(
    text: String,
    fontSize: TextUnit = 18.sp,
    color: Color = Color.Gray,
    speed: Float = 360f, // px per second
    space: String = " ... ", // Пробелы между повторами
    modifier: Modifier = Modifier
) {
    val repeatedText = remember(text) { "$text$space$text" }
    val scrollState = rememberScrollState()

    LaunchedEffect(repeatedText) {
        while (true) {
            val fullWidth = scrollState.maxValue.toFloat()
            scrollState.scrollTo(0)

            val duration = (fullWidth / speed * 1000).toInt()

            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = tween(durationMillis = duration, easing = LinearEasing)
            )
        }
    }

    Text(
        text = repeatedText,
        fontSize = fontSize,
        color = color,
        maxLines = 1,
        softWrap = false,
        modifier = modifier
            .horizontalScroll(scrollState, enabled = false)
            .fillMaxWidth()
    )
}



