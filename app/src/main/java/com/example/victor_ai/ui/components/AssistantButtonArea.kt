package com.example.victor_ai.ui.components
import com.example.victor_ai.R

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
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

// ui/assistant/AssistantButtonArea.kt
@Composable
fun AssistantButtonArea(
    modifier: Modifier = Modifier,  // ← вот он
    playlistViewModel: PlaylistViewModel,  // 🔥 Получаем извне
    placesViewModel: PlacesViewModel,
    reminderManager: ReminderManager,
    navController: NavController,  // 🔥 Передаём navController вместо callbacks
    onStartVoiceRecognition: () -> Unit,
    onRequestMicrophone: () -> Unit,
    onOpenChat: () -> Unit
)
 {
    var showAssistantMenu by remember { mutableStateOf(false) }

    // 🔥 Закрываем меню при переходе на другие экраны
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    LaunchedEffect(currentRoute) {
        if (currentRoute != "main" && currentRoute != null) {
            showAssistantMenu = false
        }
    }

     // 🔥 Убрали fillMaxSize() чтобы Box не перекрывал весь экран
     Box(
         modifier = modifier
             .padding(24.dp)
     ) {
         FloatingActionButton(
             onClick = {
                 // 🔥 При открытии меню сначала переходим на главный экран
                 navController.navigate("main") {
                     popUpTo("main") { inclusive = false }
                     launchSingleTop = true
                 }
                 showAssistantMenu = !showAssistantMenu
             },
             containerColor = Color.Transparent,
             contentColor = Color(0xFFA6A6A6),
             modifier = Modifier.size(48.dp)
         ) {
             Text(
                 text = "≡",
                 style = TextStyle(
                     fontFamily = FontFamily(Font(R.font.didact_gothic)),
                     color = Color(0xFFA6A6A6),
                     fontSize = 28.sp
                 )
             )
         }
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
    // Горизонтальное меню
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 72.dp, top = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        HorizontalScrollMenu(
            visible = showAssistantMenu && currentRoute == "main",
            onMenuItemClick = { menuState ->
                when (menuState) {
                    MenuState.MAIN -> {
                        // Уже на главном, ничего не делаем
                    }
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
}
