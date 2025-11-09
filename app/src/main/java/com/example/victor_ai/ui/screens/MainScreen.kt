package com.example.victor_ai.ui.screens

import com.example.victor_ai.R
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.permissions.PermissionManager
import com.example.victor_ai.ui.components.AssistantButtonArea
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.menu.MenuState
import com.example.victor_ai.ui.places.PlacesViewModel
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun MainScreen(
    navController: NavController,
    playlistViewModel: PlaylistViewModel,  // 🔥 Получаем извне
    placesViewModel: PlacesViewModel,
    reminderManager: ReminderManager,
    onStartVoiceRecognition: () -> Unit,
    onRequestMicrophone: () -> Unit,
    isListeningState: State<Boolean>,
    permissionManager: PermissionManager,
    onStopListening: () -> Unit
) {
    var showAssistantMenu by remember { mutableStateOf(false) }
    val menuState = remember { mutableStateOf(MenuState.ROOT) }
    val popup by reminderManager.reminderPopup.collectAsState()

    // 🔥 Проверка текущего экрана
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        val allowGestures = currentRoute == "main"
                && menuState.value == MenuState.ROOT
                && !showAssistantMenu
                && popup == null

        // 🔸 Слой жестов — только на главном экране
        if (allowGestures) {
            Box(
                modifier = Modifier
                    .fillMaxSize()  // можешь оставить, т.к. allowGestures теперь контролирует
                    .padding(start = 220.dp, top = 360.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                println("✅ TAP -> открываем чат")
                                navController.navigate("chat")
                            },
                            onLongPress = {
                                println("🎤 LONG TAP -> микрофон")
                                permissionManager.requestMicrophonePermission()
                            },
                            onPress = {
                                println("🛑 PRESS -> остановить прослушку")
                                tryAwaitRelease()
                                if (isListeningState.value) {
                                    onStopListening()
                                }
                            }
                        )
                    }
            )
        }

        // 🔹 Заглушка под qwen/geminy - ТОЛЬКО на главном экране
        val isMainScreen = currentRoute == "main" || currentRoute == null
        if (allowGestures && isMainScreen && menuState.value == MenuState.ROOT) {
            PresencePlaceholder()
        }

        // 🔹 Кнопка ассистента

        AssistantButtonArea(
            modifier = Modifier.align(Alignment.BottomEnd),
            playlistViewModel = playlistViewModel,  // 🔥 Передаём
            placesViewModel = placesViewModel,
            reminderManager = reminderManager,
            onStartVoiceRecognition = onStartVoiceRecognition,
            onRequestMicrophone = onRequestMicrophone,
            onOpenChat = { navController.navigate("chat") }
        )
    }
}

@Composable
fun PresencePlaceholder(modifier: Modifier = Modifier) {
    var timeText by remember { mutableStateOf("") }
    var greetingText by remember { mutableStateOf("") }
    var showFirstLine by remember { mutableStateOf(false) }
    var showSecondLine by remember { mutableStateOf(false) }
    var showThirdLine by remember { mutableStateOf(false) }

    // плавное дыхание
    val alpha by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val didactStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.didact_gothic)),
        color = Color(0xFFE0E0E0),
        fontSize = 26.sp,  // Увеличил размер шрифта
        textAlign = TextAlign.Start
    )

    // обновление времени и последовательная анимация появления
    LaunchedEffect(Unit) {
        while (true) {
            val now = LocalTime.now()
            timeText = "👀… ${now.format(DateTimeFormatter.ofPattern("HH:mm"))}."
            greetingText = when (now.hour) {
                in 6..11 -> "Доброе утро."
                in 12..17 -> "Хорошего дня."
                in 18..22 -> "Тёплого вечера."
                else -> "Иди спать."
            }

            // Последовательное появление строк
            showFirstLine = false
            showSecondLine = false
            showThirdLine = false

            delay(600) // начальная пауза
            showFirstLine = true

            delay(1000 + Random.nextLong(200, 700)) // неравномерная задержка
            showSecondLine = true

            delay(800 + Random.nextLong(300, 600)) // ещё одна неравномерная задержка
            showThirdLine = true

            delay(58_000) // обновляем почти каждую минуту
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 230.dp, top = 200.dp, end = 36.dp) // сместил вправо
            .alpha(alpha)
    ) {
        if (showFirstLine) {
            TypingText(text = timeText, style = didactStyle, speed = 40L)
        }

        Spacer(Modifier.height(18.dp))

        if (showSecondLine) {
            TypingText(text = "Я здесь.", style = didactStyle, speed = 45L)
        }

        Spacer(Modifier.height(14.dp))

        if (showThirdLine) {
            TypingText(text = greetingText, style = didactStyle, speed = 50L)
        }
    }
}

@Composable
fun TypingText(text: String, style: TextStyle, speed: Long = 35L) {
    var displayed by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        displayed = ""
        text.forEachIndexed { i, _ ->
            displayed = text.take(i + 1)
            delay(speed)
        }
    }

    Text(text = displayed, style = style)
}
