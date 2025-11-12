package com.example.victor_ai.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.victor_ai.R

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.ui.menu.components.HorizontalScrollMenu
import com.example.victor_ai.ui.menu.MenuState
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.playlist.PlaylistViewModel

@Composable
fun AssistantButtonArea(
    modifier: Modifier = Modifier,
    playlistViewModel: PlaylistViewModel,
    placesViewModel: PlacesViewModel,
    reminderManager: ReminderManager,
    navController: NavController,
    onStartVoiceRecognition: () -> Unit,
    onRequestMicrophone: () -> Unit,
    onOpenChat: () -> Unit
) {
    var showAssistantMenu by remember { mutableStateOf(false) }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    LaunchedEffect(currentRoute) {
        if (currentRoute != "main" && currentRoute != null) showAssistantMenu = false
    }

    val popup by reminderManager.reminderPopup.collectAsState()
    popup?.let {
        ReminderOverlay(
            popup = it,
            onOk = {
                reminderManager.clearPopup()
                reminderManager.sendReminderActionCoroutine("done", it.id)
            },
            onDelay = {
                reminderManager.clearPopup()
                reminderManager.sendReminderActionCoroutine("delay", it.id)
            },
            onDismiss = { reminderManager.clearPopup() }
        )
    }

    // 🔥 Объединяем кнопку и меню в один Box
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = 24.dp, end = 24.dp)
    ) {
        // Горизонтальное меню
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, end = 72.dp) // 72dp отступ под кнопку
        ) {
            HorizontalScrollMenu(
                visible = showAssistantMenu && currentRoute == "main",
                onMenuItemClick = { menuState ->
                    when (menuState) {
                        MenuState.MAIN -> Unit
                        MenuState.PLACES -> {
                            navController.navigate("places")
                            showAssistantMenu = false
                        }
                        MenuState.PLAYLIST -> {
                            navController.navigate("playlist")
                            showAssistantMenu = false
                        }
                        MenuState.SYSTEM -> {
                            navController.navigate("system")
                            showAssistantMenu = false
                        }
                        MenuState.CALENDAR -> {
                            navController.navigate("calendar")
                            showAssistantMenu = false
                        }
                        else -> Unit
                    }
                }
            )
        }

        // 🔘 Кнопка ≡ (без FloatingActionButton)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(48.dp)
                .offset(y = (-3).dp)
                .background(Color.Transparent, shape = CircleShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ){
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = false }
                        launchSingleTop = true
                    }
                    showAssistantMenu = !showAssistantMenu
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "≡",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.didact_gothic)),
                    color = Color(0xFFA6A6A6),
                    fontSize = 48.sp,      // 🔽 меньше, чтобы не резалось
                    lineHeight = 32.sp
                )
            )
        }
    }
}

