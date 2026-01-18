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

package com.example.victor_ai.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DemoKeyDialog(
    message: String?,
    onSubmit: (demoKey: String) -> Unit
) {
    var demoKey by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Авторизация") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = demoKey,
                    onValueChange = { demoKey = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("demo_key") },
                    singleLine = true
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = message ?: "Введи demo_key для авторизации.",
                    color = Color(0xFFA6A6A6)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(demoKey) },
                enabled = demoKey.trim().isNotEmpty()
            ) {
                Text("Продолжить")
            }
        }
    )
}


