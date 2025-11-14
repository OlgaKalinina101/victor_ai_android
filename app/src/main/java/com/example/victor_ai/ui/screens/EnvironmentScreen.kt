package com.example.victor_ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.victor_ai.R
import com.example.victor_ai.ui.screens.environment.EnvironmentViewModel
import kotlin.math.min

@Composable
fun EnvironmentScreen(
    modifier: Modifier = Modifier,
    viewModel: EnvironmentViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val grayText = Color(0xFFA6A6A6)
    val yellowText = Color(0xFFFFD700)
    val bgColor = Color(0xFF2B2929)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // TODO заглушка
            Text(
                text = "/* TODO: Среда в разработке */",
                color = yellowText,
                fontSize = 20.sp,
                fontFamily = didactGothic
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Статус "дома"
            HomeStatusSection(
                isAtHome = state.isAtHome,
                distanceToHome = state.distanceToHome,
                homeWiFi = state.homeWiFi,
                didactGothic = didactGothic,
                grayText = grayText
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Текущий WiFi
            if (state.currentWiFi != null) {
                Text(
                    text = "текущий wifi: ${state.currentWiFi}",
                    color = grayText,
                    fontSize = 18.sp,
                    fontFamily = didactGothic
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка для открытия/закрытия списка WiFi
            Button(
                onClick = { viewModel.toggleNetworkDropdown() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F4650),
                    contentColor = grayText
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = if (state.showNetworkDropdown) "закрыть список wifi" else if (state.homeWiFi == null) "выбрать домашний wifi" else "сменить домашний wifi",
                    fontFamily = didactGothic,
                    fontSize = 18.sp
                )
            }

            // Выпадающий список WiFi сетей
            if (state.showNetworkDropdown) {
                WiFiNetworkList(
                    networks = state.availableNetworks,
                    currentHomeSSID = state.homeWiFi,
                    currentPage = state.currentPage,
                    isScanning = state.isScanning,
                    homeIsSet = state.homeWiFi != null,
                    onNetworkSelected = { ssid, bssid ->
                        viewModel.setHomeWiFi(ssid, bssid)
                    },
                    onClearHome = { viewModel.clearHomeWiFi() },
                    onNextPage = { viewModel.nextPage() },
                    onPreviousPage = { viewModel.previousPage() },
                    didactGothic = didactGothic,
                    grayText = grayText
                )
            }
        }
    }
}

@Composable
private fun HomeStatusSection(
    isAtHome: Boolean,
    distanceToHome: Int?,
    homeWiFi: String?,
    didactGothic: FontFamily,
    grayText: Color
) {
    val statusText = if (homeWiFi != null) {
        if (isAtHome) {
            "[дома: ✓]"
        } else if (distanceToHome != null) {
            "[дома: ✗ - $distanceToHome м]"
        } else {
            "[дома: ?]"
        }
    } else {
        "[дома: Null]"
    }

    Text(
        text = statusText,
        color = grayText,
        fontSize = 22.sp,
        fontFamily = didactGothic
    )
}

@Composable
private fun WiFiNetworkList(
    networks: List<Pair<String, String>>,
    currentHomeSSID: String?,
    currentPage: Int,
    isScanning: Boolean,
    homeIsSet: Boolean,
    onNetworkSelected: (String, String) -> Unit,
    onClearHome: () -> Unit,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    didactGothic: FontFamily,
    grayText: Color
) {
    val itemsPerPage = 5
    val totalPages = (networks.size + itemsPerPage - 1) / itemsPerPage
    val startIndex = currentPage * itemsPerPage
    val endIndex = minOf(startIndex + itemsPerPage, networks.size)
    val currentPageNetworks = networks.subList(startIndex, endIndex)

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 8.dp)
    ) {
        if (isScanning) {
            CircularProgressIndicator(
                color = grayText,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (networks.isEmpty()) {
            Text(
                text = "  сети не найдены",
                color = grayText.copy(alpha = 0.6f),
                fontSize = 14.sp,
                fontFamily = didactGothic,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            // Кнопка "предыдущие 5"
            if (currentPage > 0) {
                WiFiMenuItem(
                    text = "---- предыдущие 5 ----",
                    isSelected = false,
                    onClick = onPreviousPage,
                    didactGothic = didactGothic,
                    grayText = grayText
                )
            }

            // Список сетей на текущей странице
            currentPageNetworks.forEach { (ssid, bssid) ->
                val isCurrentHome = ssid == currentHomeSSID
                WiFiMenuItem(
                    text = if (isCurrentHome) "$ssid (дом)" else ssid,
                    isSelected = isCurrentHome,
                    onClick = { onNetworkSelected(ssid, bssid) },
                    didactGothic = didactGothic,
                    grayText = grayText
                )
            }

            // Кнопка "следующие 5"
            if (currentPage < totalPages - 1) {
                WiFiMenuItem(
                    text = "---- следующие 5 ----",
                    isSelected = false,
                    onClick = onNextPage,
                    didactGothic = didactGothic,
                    grayText = grayText
                )
            }

            // Кнопка "удалить домашний wifi" в конце списка (если установлен)
            if (homeIsSet) {
                Spacer(modifier = Modifier.height(8.dp))
                WiFiMenuItem(
                    text = "удалить домашний wifi",
                    isSelected = false,
                    onClick = onClearHome,
                    didactGothic = didactGothic,
                    grayText = grayText
                )
            }
        }
    }
}

@Composable
private fun WiFiMenuItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    didactGothic: FontFamily,
    grayText: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isSelected) "> " else "  ",
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFFFFD700) else grayText,
            modifier = Modifier.width(20.dp),
            fontFamily = didactGothic
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFFFFD700) else grayText,
            fontFamily = didactGothic
        )
    }
}
