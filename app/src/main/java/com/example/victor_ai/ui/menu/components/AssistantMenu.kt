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
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.logic.UsageRepository
import com.example.victor_ai.ui.menu.MenuState
import com.example.victor_ai.ui.places.PlacesMenu
import com.example.victor_ai.ui.places.PlacesViewModel
import com.example.victor_ai.ui.playlist.PlaylistScreen
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import com.example.victor_ai.ui.screens.CalendarScreenWithReminders
import com.example.victor_ai.ui.screens.SystemMenuScreen

@Composable
fun AssistantMenu(
    modifier: Modifier = Modifier,
    playlistViewModel: PlaylistViewModel,  // 🔥 Получаем извне
    placesViewModel: PlacesViewModel,  // ← добавляем!
    onRequestVoice: () -> Unit,
    onRequestPermission: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    var currentMenu: MenuState by remember { mutableStateOf(MenuState.ROOT) }

    // 🔥 Логируем когда меняется currentMenu
    LaunchedEffect(currentMenu) {
        Log.d("AssistantMenu", "📍 currentMenu changed to: $currentMenu")
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
                when (item) {
                    "Плейлист" -> currentMenu = MenuState.PLAYLIST
                    "Места" -> currentMenu = MenuState.PLACES  // ← ДОБАВЬ ЭТО!
                    else -> Unit
                }
            }
        )

        MenuState.CHAT -> ChatMenu(onBack = { currentMenu = MenuState.ROOT })

        MenuState.SYSTEM -> SystemMenuScreen(
            usageRepository = UsageRepository(RetrofitInstance.apiService),
            modifier = Modifier.fillMaxSize()
        )

        MenuState.PLACES -> PlacesMenu(
            onBack = { currentMenu = MenuState.ROOT },
            viewModel = placesViewModel
        )
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