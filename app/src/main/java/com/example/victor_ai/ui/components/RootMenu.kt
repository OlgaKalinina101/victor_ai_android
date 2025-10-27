package com.example.victor_ai.ui.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.victor_ai.R
import com.example.victor_ai.ui.theme.MenuState

@Composable
fun RootMenu(onClick: (MenuState) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Кнопка "Основное"
        Image(
            painter = painterResource(id = R.drawable.btn_main),
            contentDescription = "Основное",
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
                .clickable { onClick(MenuState.MAIN) }  // ← вот здесь будет вызов
        )

        // Кнопка "Места"
        Image(
            painter = painterResource(id = R.drawable.btn_places),
            contentDescription = "Места",
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
                .clickable { println("Места: в разработке") }
        )

        // Кнопка "Браузер"
        Image(
            painter = painterResource(id = R.drawable.btn_browser),
            contentDescription = "Браузер",
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
                .clickable { println("Браузер: в разработке") }
        )

        // ✅ Кнопка "Система"
        Image(
            painter = painterResource(id = R.drawable.btn_system),
            contentDescription = "Система",
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
                .clickable { onClick(MenuState.SYSTEM) }
        )
    }
}