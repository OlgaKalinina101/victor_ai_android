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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.R
import com.example.victor_ai.ui.memories.MemoriesViewModel
import com.example.victor_ai.data.network.dto.MemoryResponse
import com.example.victor_ai.data.network.AssistantMind
import com.example.victor_ai.data.network.AssistantState
import com.example.victor_ai.data.network.ModelUsage
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.network.RetrofitInstance.assistantApi
import com.example.victor_ai.logic.UsageRepository
import com.example.victor_ai.ui.components.EyeState
import com.example.victor_ai.ui.components.VictorEyes
import com.example.victor_ai.utils.EmotionMapper
import kotlinx.coroutines.isActive
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
    var trustLevel by remember { mutableStateOf(0) }

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

        // 🔐 Загрузка ChatMeta для trust_level
        try {
            UserProvider.loadUserData()
                .onSuccess { meta ->
                    trustLevel = meta.trust_level
                    Log.d("SystemMenu", "✅ ChatMeta загружена: trust_level=$trustLevel")
                }
                .onFailure { e ->
                    Log.e("SystemMenu", "❌ Ошибка загрузки ChatMeta: ${e.message}")
                }
        } catch (e: Exception) {
            Log.e("SystemMenu", "❌ Исключение при загрузке ChatMeta", e)
        }

        modelUsageList = usageRepository.getModelUsage(UserProvider.getCurrentUserId())

        // ✅ Получение состояния и фокусов
        try {
            val stateResponse = assistantApi.getAssistantState(UserProvider.getCurrentUserId())
            assistantStateList = stateResponse
            assistantState = stateResponse.lastOrNull()?.state

            assistantMind = assistantApi.getAssistantMind(UserProvider.getCurrentUserId())
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
            assistantMind = assistantMind,
            trustLevel = trustLevel
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
    trustLevel: Int,
    modifier: Modifier = Modifier
) {
    val grayText = Color(0xFFA6A6A6)
    val fontSize = 18.sp
    val didactGothic = FontFamily(Font(R.font.didact_gothic))

    // Состояние для expandable панели балансов
    var showBalancePanel by remember { mutableStateOf(false) }

    // Группировка по провайдеру для орбитальных иконок
    val usageByProvider = modelUsageList.groupBy { it.provider }
    val firstProvider = usageByProvider.keys.firstOrNull() ?: "N/A"

    // Расчет процента баланса для первого провайдера
    val balancePercent = if (usageByProvider.isNotEmpty()) {
        val entries = usageByProvider[firstProvider] ?: emptyList()
        if (entries.isNotEmpty()) {
            val totalSpent = entries.sumOf {
                (it.input_tokens_used * it.input_token_price + it.output_tokens_used * it.output_token_price).toDouble()
            }
            val balance = entries.first().account_balance.toDouble().coerceAtLeast(0.01)
            val percentRemaining = (1.0 - totalSpent / balance).coerceIn(0.0, 1.0)
            "${(percentRemaining * 100).toInt()}%"
        } else "N/A"
    } else "N/A"

    // Парсинг эмоционального сдвига для эмодзи
    val emotionEmojis = emotionalShift.split(" → ")
        .map { EmotionMapper.getEmoji(it.trim()) }
        .joinToString(" → ")

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // ✓/✗ Минималистичный индикатор связи
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            when {
                isChecking -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = grayText
                    )
                }
                isOnline -> {
                    Text("✓", fontSize = 28.sp, color = Color(0xFF77FF77))
                }
                else -> {
                    Text("✗", fontSize = 28.sp, color = Color(0xFFFF7777))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 👀 VictorEyes
        VictorEyes(
            state = EyeState.IDLE,
            showTime = false,
            trailingText = null
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 💭 Мысли
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "Мысли:",
                fontSize = fontSize,
                color = grayText,
                fontFamily = didactGothic
            )

            if (assistantMind.isEmpty()) {
                Text(
                    "Нет активных фокусов",
                    fontSize = 16.sp,
                    color = grayText.copy(alpha = 0.7f),
                    fontFamily = didactGothic
                )
            } else {
                val thoughtsText = assistantMind.joinToString(" ... ") { it.mind }
                InfiniteMarqueeText(
                    text = thoughtsText,
                    fontSize = 16.sp,
                    color = grayText.copy(alpha = 0.8f),
                    fontFamily = didactGothic
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🌀 Эмоциональный сдвиг с эмодзи
        if (emotionEmojis != "🤖 → 🤖") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    emotionEmojis,
                    fontSize = 24.sp,
                    color = grayText,
                    fontFamily = didactGothic
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 🌐 Орбитальные иконки (provider, balance, mood)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🌐 Provider - кликабельная для открытия панели
            Text(
                "🌐",
                fontSize = 32.sp,
                modifier = Modifier.clickable { showBalancePanel = !showBalancePanel }
            )

            // 95% Balance
            Text(
                balancePercent,
                fontSize = 18.sp,
                color = grayText,
                fontFamily = didactGothic
            )

            // 😌 Mood emoji
            Text(
                EmotionMapper.getEmoji(assistantState),
                fontSize = 32.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔄 Trust Level Bar
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "Trust Level: $trustLevel",
                fontSize = fontSize,
                color = grayText,
                fontFamily = didactGothic
            )

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFF333333), shape = RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(trustLevel / 100f)
                        .height(8.dp)
                        .background(Color(0xFF77FF77), shape = RoundedCornerShape(4.dp))
                )
            }
        }

        // 💰 Expandable Token Balance Panel
        if (showBalancePanel && usageByProvider.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Баланс токенов",
                        fontSize = 18.sp,
                        color = grayText,
                        fontFamily = didactGothic
                    )

                    usageByProvider.forEach { (provider, entries) ->
                        if (entries.isNotEmpty()) {
                            val totalSpent = entries.sumOf {
                                (it.input_tokens_used * it.input_token_price + it.output_tokens_used * it.output_token_price).toDouble()
                            }
                            val balance = entries.first().account_balance.toDouble().coerceAtLeast(0.01)
                            val percentRemaining = (1.0 - totalSpent / balance).coerceIn(0.0, 1.0)

                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    "🌐 $provider",
                                    fontSize = 16.sp,
                                    color = grayText,
                                    fontFamily = didactGothic
                                )

                                // Progress bar
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .background(Color(0xFF333333), shape = RoundedCornerShape(3.dp))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(percentRemaining.toFloat())
                                            .height(6.dp)
                                            .background(Color(0xFF77FF77), shape = RoundedCornerShape(3.dp))
                                    )
                                }

                                Text(
                                    "${"%.2f".format(balance - totalSpent)} из ${"%.2f".format(balance)}",
                                    fontSize = 14.sp,
                                    color = grayText.copy(alpha = 0.7f),
                                    fontFamily = didactGothic
                                )
                            }
                        }
                    }
                }
            }
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
    fontFamily: FontFamily? = null,
    speed: Float = 360f, // px per second
    space: String = " ... ", // Пробелы между повторами
    modifier: Modifier = Modifier
) {
    val repeatedText = remember(text) { "$text$space$text" }
    val scrollState = rememberScrollState()

    LaunchedEffect(repeatedText) {
        while (isActive) {  // ✅ Проверка isActive - останавливаем при выходе из composition
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
        fontFamily = fontFamily,
        maxLines = 1,
        softWrap = false,
        modifier = modifier
            .horizontalScroll(scrollState, enabled = false)
            .fillMaxWidth()
    )
}



