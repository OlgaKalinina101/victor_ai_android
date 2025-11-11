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

    val emotionalShift = if (assistantStateList.isNotEmpty()) {
        // Берём последние уникальные состояния (минимум 1, максимум 2)
        val uniqueStates = assistantStateList
            .takeLast(10)
            .distinctBy { it.state }
            .takeLast(2)

        if (uniqueStates.size >= 2) {
            // Если есть 2 или больше - показываем сдвиг
            uniqueStates.joinToString(" → ") { it.state }
        } else {
            // Если только 1 - показываем просто её
           "Эмоциональный сдвиг: Null"
        }
    } else {
        null // Не показываем блок вообще если нет данных
    }



    LaunchedEffect(true) {
        Log.d("SystemMenu", "▶️ LaunchedEffect started")

        isChecking = true
        Log.d("SystemMenu", "🌐 Проверяем связь...")
        isOnline = try {
            val response = apiService.checkConnection()
            Log.d("SystemMenu", "🌐 Связь проверена: ${response.isSuccessful}")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("SystemMenu", "🌐 Ошибка проверки связи", e)
            false
        }
        isChecking = false

        // 🔐 Загрузка ChatMeta для trust_level
        Log.d("SystemMenu", "🔐 Переходим к загрузке ChatMeta...")
        try {
            Log.d("SystemMenu", "🔄 Начинаем загрузку ChatMeta...")
            val result = UserProvider.loadUserData()
            Log.d("SystemMenu", "🔄 UserProvider.loadUserData() вызван, обрабатываем результат...")
            result
                .onSuccess { meta ->
                    trustLevel = meta.trust_level
                    Log.d("SystemMenu", "✅ ChatMeta загружена успешно!")
                    Log.d("SystemMenu", "   account_id: ${meta.account_id}")
                    Log.d("SystemMenu", "   trust_level: ${meta.trust_level}")
                    Log.d("SystemMenu", "   model: ${meta.model}")
                    Log.d("SystemMenu", "   Значение trustLevel в state: $trustLevel")
                }
                .onFailure { e ->
                    Log.e("SystemMenu", "❌ Ошибка загрузки ChatMeta: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e("SystemMenu", "❌ Исключение при загрузке ChatMeta", e)
        }
        Log.d("SystemMenu", "🔐 Загрузка ChatMeta завершена")

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
    emotionalShift: String?,
    assistantMind: List<AssistantMind>,
    trustLevel: Int,
    modifier: Modifier = Modifier
) {
    val grayText = Color(0xFFA6A6A6)
    val fontSize = 18.sp
    val didactGothic = FontFamily(Font(R.font.didact_gothic))

    // Состояние для expandable панели балансов
    var showBalancePanel by remember { mutableStateOf(false) }

    // Состояние для MemoriesSheet
    var showMemoriesSheet by remember { mutableStateOf(false) }

    // ViewModel для воспоминаний
    val viewModel: MemoriesViewModel = hiltViewModel()
    val memories by viewModel.memories.observeAsState(initial = emptyList())
    val error by viewModel.error.observeAsState(initial = null)
    val loading by viewModel.loading.observeAsState(initial = false)

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var balancePanelOffset by remember { mutableStateOf(0.dp) }

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
    val emotionEmojis = emotionalShift?.let { shift ->
        if (shift == "Эмоциональный сдвиг: Null") {
            shift // оставляем как есть
        } else {
            shift.split(" → ").joinToString(" → ") { EmotionMapper.getEmoji(it.trim()) }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // [связь: ✓] - индикатор связи
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 60.dp) // ⬇️ вниз
        ) {
            // Текст "[связь: " серый
            Text(
                "[связь: ",
                fontSize = fontSize,
                color = grayText,
                fontFamily = didactGothic
            )

            // Иконка цветная
            when {
                isChecking -> {
                    Text(
                        "⏳",
                        fontSize = fontSize,
                        color = grayText,
                        fontFamily = didactGothic
                    )
                }

                isOnline -> {
                    Text(
                        "✓",
                        fontSize = fontSize,
                        color = Color(0xFF77FF77),
                        fontFamily = didactGothic
                    )
                }

                else -> {
                    Text(
                        "✗",
                        fontSize = fontSize,
                        color = Color(0xFFFF7777),
                        fontFamily = didactGothic
                    )
                }
            }

            // Закрывающая скобка серая
            Text(
                "]",
                fontSize = fontSize,
                color = grayText,
                fontFamily = didactGothic
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 👀 VictorEyes - по центру
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 120.dp) // ⬇️ двигает весь блок вниз
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 👀 VictorEyes
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    VictorEyes(
                        state = EyeState.IDLE,
                        showTime = false,
                        trailingText = null
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 💭 Мысли
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showMemoriesSheet = true }
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

                // 🌀 Эмоциональный сдвиг
                emotionEmojis?.let { text ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = text,
                            fontSize = 16.sp,
                            color = grayText,
                            fontFamily = didactGothic
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }


// 🌐 + 😌 Орбитальные иконки
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 180.dp) // ⬇️ двигает блок вниз
        ) {
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
        }

// 🔄 Trust Level - тонкая шкала с ползунком
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 220.dp) // ⬇️ отдельно двигаем Trust Level ниже
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Trust Level: $trustLevel",
                    fontSize = fontSize,
                    color = grayText,
                    fontFamily = didactGothic
                )

                // Тонкая шкала с квадратным ползунком
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    val barWidth = maxWidth
                    val sliderPosition = barWidth * (trustLevel / 100f) - 6.dp

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                    ) {
                        // Линия шкалы
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color(0xFF555555))
                                .align(Alignment.Center)
                        )

                        // Квадратный ползунок
                        Box(
                            modifier = Modifier
                                .offset(x = sliderPosition)
                                .width(10.dp)   // ширина
                                .height(28.dp)  // а высоту увеличиваем
                                .background(grayText)
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            }
        }
    }

// 💰 Expandable Token Balance Panel
    if (showBalancePanel && usageByProvider.isNotEmpty()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 220.dp) // ⬇️ двигаем карточку вниз
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
                            (it.input_tokens_used * it.input_token_price +
                                    it.output_tokens_used * it.output_token_price).toDouble()
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
                                        .background(Color(0xFF3F4650), shape = RoundedCornerShape(3.dp))
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


    // ModalBottomSheet для воспоминаний
    if (showMemoriesSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showMemoriesSheet = false
            },
            sheetState = sheetState,
            containerColor = Color(0xFF2B2929),
            contentColor = Color(0xFFE0E0E0),
            scrimColor = Color.Black.copy(alpha = 0.5f),
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
            MemoriesSheet(
                memories = memories,
                loading = loading,
                error = error,
                onDelete = { recordId ->
                    viewModel.deleteMemories(UserProvider.getCurrentUserId(), listOf(recordId))
                },
                onUpdate = { id, newText ->
                    val memory = memories.find { it.id == id }
                    if (memory != null) {
                        viewModel.updateMemory(id, UserProvider.getCurrentUserId(), newText, memory.metadata)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Загрузка воспоминаний при открытии BottomSheet
    LaunchedEffect(showMemoriesSheet) {
        if (showMemoriesSheet) {
            Log.d("SystemMenu", "Запрашиваем воспоминания из Мысли")
            viewModel.fetchMemories(UserProvider.getCurrentUserId())
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



