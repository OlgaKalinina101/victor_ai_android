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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationDialog(
    genderOptions: List<String>,
    message: String?,
    onSubmit: (accountId: String, gender: String) -> Unit,
    onDismiss: () -> Unit = {}
) {
    var accountId by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(genderOptions.firstOrNull().orEmpty()) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Регистрация") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = accountId,
                    onValueChange = { accountId = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("account_id") },
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("gender") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Придумай все. Я не запомню твое имя.",
                    color = Color(0xFFA6A6A6)
                )

                message?.takeIf { it.isNotBlank() }?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(text = it, color = Color(0xFFA6A6A6))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(accountId, gender) },
                enabled = accountId.trim().isNotEmpty() && gender.trim().isNotEmpty()
            ) {
                Text("Продолжить")
            }
        }
    )
}


