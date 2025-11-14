package com.example.victor_ai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

            // Кнопка выбора домашнего WiFi
            Button(
                onClick = { viewModel.scanWiFiNetworks() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F4650),
                    contentColor = grayText
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = if (state.homeWiFi == null) "выбрать домашний wifi" else "сменить домашний wifi",
                    fontFamily = didactGothic,
                    fontSize = 18.sp
                )
            }

            // Выпадающий список WiFi сетей
            if (state.showNetworkDropdown) {
                WiFiNetworkList(
                    networks = state.availableNetworks,
                    currentHomeSSID = state.homeWiFi,
                    isScanning = state.isScanning,
                    onNetworkSelected = { ssid, bssid ->
                        viewModel.setHomeWiFi(ssid, bssid)
                    },
                    onDismiss = { viewModel.toggleNetworkDropdown() },
                    didactGothic = didactGothic,
                    grayText = grayText
                )
            }

            // Кнопка удаления домашнего WiFi (если установлен)
            if (state.homeWiFi != null) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { viewModel.clearHomeWiFi() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF6B6B))
                ) {
                    Text(
                        text = "удалить домашний wifi",
                        fontFamily = didactGothic,
                        fontSize = 16.sp
                    )
                }
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
    if (homeWiFi != null) {
        val statusText = if (isAtHome) {
            "[дома: ✓]"
        } else if (distanceToHome != null) {
            "[дома: ✗ - $distanceToHome м]"
        } else {
            "[дома: ?]"
        }

        Text(
            text = statusText,
            color = grayText,
            fontSize = 22.sp,
            fontFamily = didactGothic
        )
    } else {
        Text(
            text = "[домашний wifi не установлен]",
            color = grayText,
            fontSize = 18.sp,
            fontFamily = didactGothic
        )
    }
}

@Composable
private fun WiFiNetworkList(
    networks: List<Pair<String, String>>,
    currentHomeSSID: String?,
    isScanning: Boolean,
    onNetworkSelected: (String, String) -> Unit,
    onDismiss: () -> Unit,
    didactGothic: FontFamily,
    grayText: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color(0xFF3F4650))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "доступные сети:",
            color = grayText,
            fontSize = 18.sp,
            fontFamily = didactGothic
        )

        if (isScanning) {
            CircularProgressIndicator(
                color = grayText,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else if (networks.isEmpty()) {
            Text(
                text = "сети не найдены",
                color = grayText.copy(alpha = 0.6f),
                fontSize = 16.sp,
                fontFamily = didactGothic
            )
        } else {
            networks.forEach { (ssid, bssid) ->
                val isCurrentHome = ssid == currentHomeSSID
                Text(
                    text = if (isCurrentHome) "→ $ssid (дом)" else "  $ssid",
                    color = if (isCurrentHome) Color(0xFFFFD700) else grayText,
                    fontSize = 16.sp,
                    fontFamily = didactGothic,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNetworkSelected(ssid, bssid) }
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onDismiss,
            colors = ButtonDefaults.textButtonColors(contentColor = grayText)
        ) {
            Text(
                text = "закрыть",
                fontFamily = didactGothic,
                fontSize = 16.sp
            )
        }
    }
}
