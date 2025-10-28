package com.example.victor_ai.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.victor_ai.logic.ReminderManager
import com.example.victor_ai.domain.model.ReminderPopup
import com.example.victor_ai.ui.components.ReminderOverlay

@Composable
fun ReminderScreen(
    popup: ReminderPopup,
    reminderManager: ReminderManager,
    navController: NavController
) {
    ReminderOverlay(
        popup = popup,
        onOk = {
            reminderManager.sendReminderActionCoroutine("done", popup.id)
            reminderManager.clearPopup()
            navController.popBackStack()
        },
        onDelay = {
            reminderManager.sendReminderActionCoroutine("delay", popup.id)
            reminderManager.clearPopup()
            navController.popBackStack()
        },
        onDismiss = {
            reminderManager.clearPopup()
            navController.popBackStack()
        },
        modifier = Modifier.fillMaxSize()
    )
}