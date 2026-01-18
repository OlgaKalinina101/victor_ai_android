/**
Victor AI - Personal AI Companion for Android
Copyright (C) 2025-2026 Olga Kalinina

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.
 */

package com.example.victor_ai.ui.permissions

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.victor_ai.R

/**
 * Экран запроса разрешений после регистрации
 */
@Composable
fun PermissionsScreen(
    onComplete: () -> Unit,
    viewModel: PermissionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val didactGothic = FontFamily(Font(R.font.didact_gothic))
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var pendingComplete by remember { mutableStateOf(false) }
    var bulkGrantInProgress by remember { mutableStateOf(false) }
    var bulkAwaitingReturnFromSettings by remember { mutableStateOf(false) }
    var bulkLastLaunchedSettingsType by remember { mutableStateOf<PermissionType?>(null) }
    val bulkAttemptedSettingsTypes = remember { mutableStateListOf<PermissionType>() }

    val settingsBasedTypes = remember {
        setOf(
            PermissionType.FULL_SCREEN_INTENT,
            PermissionType.EXACT_ALARM,
            PermissionType.BATTERY_OPTIMIZATION
        )
    }

    // Launcher'ы для runtime-разрешений
    val singlePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        viewModel.checkPermissions()
        pendingComplete = true
    }

    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.checkPermissions()
        pendingComplete = true
    }

    fun requestPermission(type: PermissionType) {
        when (type) {
            PermissionType.MICROPHONE -> {
                singlePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            PermissionType.LOCATION -> {
                multiplePermissionsLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            PermissionType.NOTIFICATIONS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    singlePermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            PermissionType.PHOTOS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    singlePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    @Suppress("DEPRECATION")
                    singlePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            PermissionType.FULL_SCREEN_INTENT,
            PermissionType.EXACT_ALARM,
            PermissionType.BATTERY_OPTIMIZATION -> {
                viewModel.getPermissionSettingsIntent(type)?.let { intent ->
                    runCatching { context.startActivity(intent) }
                        .recoverCatching {
                            // Fallback: общие настройки приложения
                            val fallback = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                            context.startActivity(fallback)
                        }
                }
            }
        }
    }

    // Обновляем состояние при возврате со страниц настроек/после диалогов
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkPermissions()
                // Если мы в bulk-flow, то вернулись с экрана настроек
                bulkAwaitingReturnFromSettings = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        viewModel.checkPermissions()
    }

    LaunchedEffect(uiState.allGranted, pendingComplete) {
        if (pendingComplete && uiState.allGranted) {
            pendingComplete = false
            onComplete()
        }
    }

    // Bulk-flow: если нажали "Разрешить всё" — по очереди открываем нужные настройки
    LaunchedEffect(uiState.permissions, bulkGrantInProgress, bulkAwaitingReturnFromSettings) {
        if (!bulkGrantInProgress) return@LaunchedEffect

        // Если всё уже выдано — завершаем
        if (uiState.allGranted) {
            bulkGrantInProgress = false
            bulkAwaitingReturnFromSettings = false
            bulkLastLaunchedSettingsType = null
            bulkAttemptedSettingsTypes.clear()
            pendingComplete = false
            onComplete()
            return@LaunchedEffect
        }

        // Пока открыт экран настроек — не запускаем следующий
        if (bulkAwaitingReturnFromSettings) return@LaunchedEffect

        // Ищем первое недостающее "настроечное" разрешение
        val nextSettings = uiState.permissions
            .firstOrNull { !it.isGranted && settingsBasedTypes.contains(it.type) }
            ?.type

        if (nextSettings == null) return@LaunchedEffect

        // Если пользователь уже возвращался с этого экрана и так и не выдал — останавливаем bulk,
        // чтобы не зациклить открытия. Дальше можно добрать вручную карточками.
        if (bulkAttemptedSettingsTypes.contains(nextSettings)) {
            bulkGrantInProgress = false
            bulkAwaitingReturnFromSettings = false
            bulkLastLaunchedSettingsType = null
            return@LaunchedEffect
        }

        bulkAttemptedSettingsTypes.add(nextSettings)
        bulkLastLaunchedSettingsType = nextSettings
        bulkAwaitingReturnFromSettings = true
        requestPermission(nextSettings)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2929))
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "/* Разрешения */",
            color = Color(0xFFFFD700),
            fontSize = 32.sp,
            fontFamily = didactGothic
        )

        Spacer(Modifier.height(16.dp))

        // Описание
        Text(
            text = "Чтобы я мог полноценно работать, мне нужны некоторые разрешения:",
            color = Color(0xFFA6A6A6),
            fontSize = 16.sp,
            fontFamily = didactGothic
        )

        Spacer(Modifier.height(32.dp))

        // Список разрешений
        uiState.permissions.forEach { permission ->
            PermissionCard(
                permission = permission,
                onRequest = { requestPermission(it) },
                fontFamily = didactGothic
            )
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(32.dp))

        // Кнопка "Разрешить всё" - запрашивает разрешения и переходит дальше
        Button(
            onClick = {
                if (uiState.allGranted) {
                    onComplete()
                    return@Button
                }

                // Bulk-flow: попросить всё (runtime + затем настройки по очереди)
                bulkGrantInProgress = true
                bulkAttemptedSettingsTypes.clear()
                bulkLastLaunchedSettingsType = null
                bulkAwaitingReturnFromSettings = false
                pendingComplete = true

                // 1) runtime-разрешения
                val runtime = viewModel.getPermissionsToRequest()
                if (runtime.isNotEmpty()) {
                    multiplePermissionsLauncher.launch(runtime)
                } else {
                    // 2) если runtime нет — сразу начинаем цепочку настроек (сработает LaunchedEffect)
                    viewModel.checkPermissions()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !bulkAwaitingReturnFromSettings,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700),
                contentColor = Color(0xFF2B2929)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = when {
                    uiState.allGranted -> "Продолжить"
                    bulkGrantInProgress -> "Разрешаю..."
                    else -> "Разрешить всё"
                },
                fontFamily = didactGothic,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        // Предупреждение
        Text(
            text = "⚠️ Без разрешений некоторые функции не будут работать",
            color = Color(0xFF888888),
            fontSize = 14.sp,
            fontFamily = didactGothic
        )
    }
}

/**
 * Карточка отдельного разрешения
 */
@Composable
private fun PermissionCard(
    permission: PermissionItem,
    onRequest: (PermissionType) -> Unit,
    fontFamily: FontFamily
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (permission.isGranted) 
                Color(0xFF3A3838) 
            else 
                Color(0xFF333131)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка
            Text(
                text = permission.icon,
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            // Описание
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = permission.title,
                    color = if (permission.isGranted) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    fontSize = 18.sp,
                    fontFamily = fontFamily
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = permission.description,
                    color = Color(0xFFA6A6A6),
                    fontSize = 14.sp,
                    fontFamily = fontFamily
                )
            }

            // Статус
            if (permission.isGranted) {
                Text(
                    text = "✓",
                    color = Color(0xFF4CAF50),
                    fontSize = 24.sp
                )
            } else {
                TextButton(
                    onClick = { onRequest(permission.type) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFFD700)
                    )
                ) {
                    Text(
                        text = "→",
                        fontSize = 24.sp,
                        fontFamily = fontFamily
                    )
                }
            }
        }
    }
}

