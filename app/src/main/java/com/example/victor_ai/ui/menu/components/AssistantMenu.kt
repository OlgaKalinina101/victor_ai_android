package com.example.victor_ai.ui.menu.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.victor_ai.ui.menu.MenuState
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.playlist.PlaylistViewModel

@Composable
fun AssistantMenu(
    modifier: Modifier = Modifier,
    navController: NavController,  // 🔥 Используем navController для навигации
    playlistViewModel: PlaylistViewModel,
    placesViewModel: PlacesViewModel,
    onRequestVoice: () -> Unit,
    onRequestPermission: () -> Unit,
    onClose: () -> Unit  // 🔥 Callback для закрытия меню
) {
    var text by remember { mutableStateOf("") }
    var currentMenu: MenuState by remember { mutableStateOf(MenuState.ROOT) }

    // 🔥 Логируем когда меняется currentMenu
    LaunchedEffect(currentMenu) {
        Log.d("AssistantMenu", "📍 currentMenu changed to: $currentMenu")
    }

    when (currentMenu) {
        MenuState.ROOT -> RootMenu(
            onClick = { menuState ->
                when (menuState) {
                    MenuState.MAIN -> currentMenu = MenuState.MAIN
                    MenuState.PLACES -> {
                        navController.navigate("places")
                        onClose()
                    }
                    MenuState.SYSTEM -> {
                        navController.navigate("system")
                        onClose()
                    }
                    else -> currentMenu = menuState
                }
            }
        )

        MenuState.MAIN -> MainMenu(
            menuState = currentMenu,
            onChangeMenu = { newMenu ->
                when (newMenu) {
                    MenuState.CALENDAR -> {
                        navController.navigate("calendar")
                        onClose()
                    }
                    else -> currentMenu = newMenu
                }
            },
            onBack = { currentMenu = MenuState.ROOT },
            onItemClick = { item ->
                text = item
                when (item) {
                    "Плейлист" -> {
                        navController.navigate("playlist")
                        onClose()
                    }
                    else -> Unit
                }
            }
        )

        MenuState.CHAT -> ChatMenu(
            onBack = { currentMenu = MenuState.ROOT }
        )

        // Остальные экраны теперь рендерятся через NavHost
        else -> Unit
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}