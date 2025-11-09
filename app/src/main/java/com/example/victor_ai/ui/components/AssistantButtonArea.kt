package com.example.victor_ai.ui.components
import com.example.victor_ai.R

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.ui.menu.components.AssistantMenu
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

     // 🔥 Убрали fillMaxSize() чтобы Box не перекрывал весь экран
     Box(
         modifier = modifier
             .padding(24.dp)
     ) {
         FloatingActionButton(
             onClick = { showAssistantMenu = !showAssistantMenu },
             containerColor = Color.Transparent,
             contentColor = Color.White,
             modifier = Modifier.size(48.dp)
         ) {
             Icon(
                 painter = painterResource(id = R.drawable.ic_assistant),
                 contentDescription = "Меню ассистента",
                 modifier = Modifier.size(48.dp)
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
    if (showAssistantMenu) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            AssistantMenu(
                modifier = Modifier.padding(top = 48.dp),
                navController = navController,  // 🔥 Передаём navController
                playlistViewModel = playlistViewModel,
                placesViewModel = placesViewModel,
                onRequestVoice = onStartVoiceRecognition,
                onRequestPermission = onRequestMicrophone,
                onClose = { showAssistantMenu = false }  // 🔥 Закрываем меню после навигации
            )
        }
    }
}
