package com.example.victor_ai.ui.menu.components

import androidx.compose.runtime.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.victor_ai.R
import com.example.victor_ai.data.network.RetrofitInstance
import com.example.victor_ai.ui.menu.MenuState
import com.example.victor_ai.ui.screens.CalendarScreenWithReminders
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainMenu(
    menuState: MenuState,
    onChangeMenu: (MenuState) -> Unit,
    onBack: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val context = LocalContext.current
    var showCalendar by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Кнопка "Расписание"
        Image(
            painter = painterResource(id = R.drawable.btn_calendar),
            contentDescription = "Расписание",
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
                .clickable {
                    onChangeMenu(MenuState.CALENDAR)
                }
        )

        // Кнопка "Плейлист"
        Image(
            painter = painterResource(id = R.drawable.btn_playlist),
            contentDescription = "Плейлист",
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
                .clickable {
                    onItemClick("Плейлист")  // ← это вызовет переход в MenuState.PLAYLIST
                }
        )

        // Кнопка "Дневник"
        Image(
            painter = painterResource(id = R.drawable.btn_diary),
            contentDescription = "Дневник",
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
                .clickable { onItemClick("Дневник") }
        )

        // Кнопка "Назад"
        Image(
            painter = painterResource(id = R.drawable.btn_back),
            contentDescription = "Назад",
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
                .clickable { onBack() }
        )

        // ✅ Вставляем календарь ниже всех кнопок, если надо
        if (showCalendar) {
            CalendarScreenWithReminders {
                com.example.victor_ai.logic.getRemindersFromRepository("test_user")
            }
        }
    }
}
