package com.example.victor_ai.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.victor_ai.data.network.ReminderApi
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.logic.UsageRepository
import com.example.victor_ai.ui.playlist.PlaylistScreen
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.screens.CalendarScreen
import com.example.victor_ai.ui.screens.CalendarScreenWithReminders
import com.example.victor_ai.ui.screens.SystemMenuScreen
import com.example.victor_ai.ui.theme.MenuState

@Composable
fun AssistantMenu(
    modifier: Modifier = Modifier,
    onRequestVoice: () -> Unit,
    onRequestPermission: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    var currentMenu: MenuState by remember { mutableStateOf(MenuState.ROOT) }

    // Получаем context для cacheDir
    val context = LocalContext.current

    // ViewModel для плейлиста (создаём один раз)
    val playlistViewModel: PlaylistViewModel = remember {
        PlaylistViewModel(
            apiService = RetrofitInstance.apiService,
            accountId = "test_user",
            cacheDir = context.cacheDir  // ← передаём cacheDir
        )
    }
    when (currentMenu) {
        MenuState.ROOT -> RootMenu(onClick = { currentMenu = it })

        MenuState.MAIN -> MainMenu(
            menuState = currentMenu,
            onChangeMenu = { newMenu -> currentMenu = newMenu },
            onBack = { currentMenu = MenuState.ROOT },
            onItemClick = { item ->
                text = item
                // Если кликнули на "Плейлист", переходим
                if (item == "Плейлист") {
                    currentMenu = MenuState.PLAYLIST
                }
            }
        )

        MenuState.CHAT -> ChatMenu(onBack = { currentMenu = MenuState.ROOT })

        MenuState.SYSTEM -> SystemMenuScreen(
            usageRepository = UsageRepository(RetrofitInstance.apiService),
            modifier = Modifier.fillMaxSize()
        )

        MenuState.PLACES -> PlacesMenu(onBack = { currentMenu = MenuState.ROOT })

        MenuState.CALENDAR -> CalendarScreenWithReminders {
            RetrofitInstance.reminderApi.getReminders(accountId = "test_user")
        }

        MenuState.PLAYLIST -> PlaylistScreen(
            viewModel = playlistViewModel,
            onBackClick = { currentMenu = MenuState.MAIN }  // ← возврат в главное меню
        )
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}