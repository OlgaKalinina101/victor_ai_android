package com.example.victor_ai.ui.screens.system

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.victor_ai.R
import com.example.victor_ai.data.network.AssistantMind
import com.example.victor_ai.data.network.ModelUsage
import com.example.victor_ai.ui.components.EyeState
import com.example.victor_ai.ui.components.VictorEyes
import com.example.victor_ai.utils.EmotionMapper

@Composable
fun SystemMenuScreen(
    modifier: Modifier = Modifier,
    viewModel: SystemScreenViewModel = hiltViewModel()
) {
    // Собираем состояние из ViewModel
    val isOnline by viewModel.isOnline.collectAsState()
    val isChecking by viewModel.isChecking.collectAsState()
    val modelUsageList by viewModel.modelUsageList.collectAsState()
    val assistantState by viewModel.assistantState.collectAsState()
    val assistantMind by viewModel.assistantMind.collectAsState()
    val trustLevel by viewModel.trustLevel.collectAsState()
    val currentModel by viewModel.currentModel.collectAsState()

    // Вычисляем эмоциональный сдвиг
    val emotionalShift = viewModel.getEmotionalShift()

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
            trustLevel = trustLevel,
            currentModel = currentModel,
            onModelChanged = viewModel::updateModel
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
    currentModel: String?,
    onModelChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val grayText = Color(0xFFA6A6A6)
    val fontSize = 20.sp
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val coroutineScope = rememberCoroutineScope()

    // Состояние для expandable панели балансов
    var showBalancePanel by remember { mutableStateOf(false) }
    var showProviderDropdown by remember { mutableStateOf(false) }
    var showMemoriesSheet by remember { mutableStateOf(false) }

    // Группировка по провайдеру
    val usageByProvider = modelUsageList.groupBy { it.provider }

    // Находим провайдер для текущей модели из ChatMeta
    val currentProvider = if (currentModel != null) {
        modelUsageList.find { it.model_name == currentModel }?.provider
    } else null

    // Используем текущий провайдер или первый доступный
    val displayProvider = currentProvider ?: usageByProvider.keys.firstOrNull() ?: "N/A"

    // Расчет процента баланса для отображаемого провайдера
    val balancePercent = if (usageByProvider.isNotEmpty()) {
        val entries = usageByProvider[displayProvider] ?: emptyList()
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
            shift
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
        ConnectionStatusIndicator(
            isOnline = isOnline,
            isChecking = isChecking,
            grayText = grayText,
            fontSize = fontSize,
            didactGothic = didactGothic,
            modifier = Modifier.offset(y = 60.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 👀 VictorEyes - по центру
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 120.dp)
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
                ThoughtsSection(
                    assistantMind = assistantMind,
                    onMemoriesClick = { showMemoriesSheet = true },
                    grayText = grayText,
                    fontSize = fontSize,
                    didactGothic = didactGothic
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🌀 Эмоциональный сдвиг
                emotionEmojis?.let { text ->
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        androidx.compose.material3.Text(
                            text = text,
                            fontSize = 20.sp,
                            color = grayText,
                            fontFamily = didactGothic
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        // 🌐 + 😌 Орбитальные иконки
        OrbitalIconsRow(
            balancePercent = balancePercent,
            assistantState = assistantState,
            onProviderClick = { showBalancePanel = !showBalancePanel },
            grayText = grayText,
            didactGothic = didactGothic,
            modifier = Modifier.offset(y = 180.dp)
        )

        // 🔄 Trust Level - тонкая шкала с ползунком
        TrustLevelSlider(
            trustLevel = trustLevel,
            grayText = grayText,
            fontSize = fontSize,
            didactGothic = didactGothic,
            modifier = Modifier.offset(y = 220.dp)
        )
    }

    // 💰 Expandable Token Balance Panel
    if (showBalancePanel && usageByProvider.isNotEmpty()) {
        TokenBalancePanel(
            usageByProvider = usageByProvider,
            displayProvider = displayProvider,
            showProviderDropdown = showProviderDropdown,
            onProviderDropdownToggle = { showProviderDropdown = !showProviderDropdown },
            onProviderSelected = { newModel ->
                onModelChanged(newModel)
                showProviderDropdown = false
            },
            modelUsageList = modelUsageList,
            coroutineScope = coroutineScope,
            grayText = grayText,
            didactGothic = didactGothic,
            modifier = Modifier.offset(y = 220.dp)
        )
    }

    // ModalBottomSheet для воспоминаний
    MemoriesBottomSheet(
        showMemoriesSheet = showMemoriesSheet,
        onDismiss = { showMemoriesSheet = false }
    )
}
