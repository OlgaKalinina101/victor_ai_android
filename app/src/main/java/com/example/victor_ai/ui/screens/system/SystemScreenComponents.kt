package com.example.victor_ai.ui.screens.system

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.victor_ai.R
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.AssistantMind
import com.example.victor_ai.data.network.ModelUsage
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.data.network.dto.ChatMetaUpdateRequest
import com.example.victor_ai.data.network.dto.MemoryResponse
import com.example.victor_ai.ui.components.EyeState
import com.example.victor_ai.ui.components.VictorEyes
import com.example.victor_ai.ui.memories.MemoriesViewModel
import com.example.victor_ai.utils.EmotionMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Индикатор связи с сервером: [связь: ✓]
 */
@Composable
fun ConnectionStatusIndicator(
    isOnline: Boolean,
    isChecking: Boolean,
    grayText: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    didactGothic: FontFamily,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            "[ связь: ",
            fontSize = fontSize,
            color = grayText,
            fontFamily = didactGothic
        )

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

        Text(
            " ]",
            fontSize = fontSize,
            color = grayText,
            fontFamily = didactGothic
        )
    }
}

/**
 * Секция с мыслями ассистента (бегущая строка)
 */
@Composable
fun ThoughtsSection(
    assistantMind: List<AssistantMind>,
    onMemoriesClick: () -> Unit,
    grayText: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    didactGothic: FontFamily,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onMemoriesClick() }
    ) {
        Text(
            "Мысли:",
            fontSize = fontSize,
            color = grayText,
            fontFamily = didactGothic
        )

        if (assistantMind.isEmpty()) {
            Text(
                "Тишина...",
                fontSize = 16.sp,
                color = grayText.copy(alpha = 0.7f),
                fontFamily = didactGothic
            )
        } else {
            val thoughtsText = assistantMind.joinToString(" ... ") { it.mind }
            InfiniteMarqueeText(
                text = thoughtsText,
                fontSize = 18.sp,
                color = grayText.copy(alpha = 0.8f),
                fontFamily = didactGothic
            )
        }
    }
}

/**
 * Орбитальные иконки: 🌐 + процент баланса + 😌
 */
@Composable
fun OrbitalIconsRow(
    balancePercent: String,
    assistantState: String?,
    onProviderClick: () -> Unit,
    grayText: Color,
    didactGothic: FontFamily,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🌐 Provider - кликабельная для открытия панели
        Text(
            "🌐",
            fontSize = 32.sp,
            modifier = Modifier.clickable { onProviderClick() }
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

/**
 * Шкала Trust Level с ползунком
 */
@Composable
fun TrustLevelSlider(
    trustLevel: Int,
    grayText: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    didactGothic: FontFamily,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
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
                        .width(10.dp)
                        .height(28.dp)
                        .background(grayText)
                        .align(Alignment.CenterStart)
                )
            }
        }
    }
}

/**
 * Панель с балансом токенов (expandable)
 */
@Composable
fun TokenBalancePanel(
    usageByProvider: Map<String, List<ModelUsage>>,
    displayProvider: String,
    showProviderDropdown: Boolean,
    onProviderDropdownToggle: () -> Unit,
    onProviderSelected: (String) -> Unit,
    modelUsageList: List<ModelUsage>,
    coroutineScope: CoroutineScope,
    grayText: Color,
    didactGothic: FontFamily,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
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

            // Отображаем только текущий провайдер
            val entries = usageByProvider[displayProvider] ?: emptyList()
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
                    // Провайдер с выпадающим списком
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProviderDropdownToggle() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🌐 $displayProvider",
                            fontSize = 16.sp,
                            color = grayText,
                            fontFamily = didactGothic
                        )

                        Icon(
                            imageVector = if (showProviderDropdown)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = "Переключатель провайдера",
                            tint = grayText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Выпадающий список других провайдеров
                    ProviderDropdownList(
                        showDropdown = showProviderDropdown,
                        usageByProvider = usageByProvider,
                        displayProvider = displayProvider,
                        modelUsageList = modelUsageList,
                        onProviderSelected = onProviderSelected,
                        coroutineScope = coroutineScope,
                        grayText = grayText,
                        didactGothic = didactGothic
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

/**
 * Выпадающий список для выбора провайдера
 */
@Composable
fun ProviderDropdownList(
    showDropdown: Boolean,
    usageByProvider: Map<String, List<ModelUsage>>,
    displayProvider: String,
    modelUsageList: List<ModelUsage>,
    onProviderSelected: (String) -> Unit,
    coroutineScope: CoroutineScope,
    grayText: Color,
    didactGothic: FontFamily
) {
    if (showDropdown) {
        val otherProviders = usageByProvider.keys.filter { it != displayProvider }
        if (otherProviders.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2A2A2A), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                otherProviders.forEach { provider ->
                    Text(
                        "🌐 $provider",
                        fontSize = 14.sp,
                        color = grayText.copy(alpha = 0.8f),
                        fontFamily = didactGothic,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    try {
                                        // Находим модель для выбранного провайдера
                                        val newModel = modelUsageList.find {
                                            it.provider == provider
                                        }?.model_name

                                        if (newModel != null) {
                                            // Отправляем PATCH запрос
                                            val response = RetrofitInstance.apiService.updateChatMeta(
                                                accountId = UserProvider.getCurrentUserId(),
                                                body = ChatMetaUpdateRequest(model = newModel)
                                            )

                                            if (response.isSuccessful) {
                                                // Обновляем локальное состояние
                                                onProviderSelected(newModel)
                                                Log.d("SystemMenu", "✅ Провайдер успешно обновлен на $provider (модель: $newModel)")
                                            } else {
                                                Log.e("SystemMenu", "❌ Ошибка обновления провайдера: ${response.code()}")
                                            }
                                        } else {
                                            Log.e("SystemMenu", "❌ Не найдена модель для провайдера $provider")
                                        }
                                    } catch (e: Exception) {
                                        Log.e("SystemMenu", "❌ Исключение при обновлении провайдера", e)
                                    }
                                }
                            }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Модальное окно с воспоминаниями
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoriesBottomSheet(
    showMemoriesSheet: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!showMemoriesSheet) return

    val viewModel: MemoriesViewModel = hiltViewModel()
    val memories by viewModel.memories.observeAsState(initial = emptyList())
    val error by viewModel.error.observeAsState(initial = null)
    val loading by viewModel.loading.observeAsState(initial = false)

    val configuration = LocalConfiguration.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Загрузка воспоминаний при открытии BottomSheet
    LaunchedEffect(showMemoriesSheet) {
        if (showMemoriesSheet) {
            Log.d("SystemMenu", "Запрашиваем воспоминания из Мысли")
            viewModel.fetchMemories(UserProvider.getCurrentUserId())
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
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

/**
 * Бегущая строка (marquee text)
 */
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
