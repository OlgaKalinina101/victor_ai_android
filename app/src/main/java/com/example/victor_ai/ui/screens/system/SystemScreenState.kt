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

package com.example.victor_ai.ui.screens.system

import com.example.victor_ai.data.network.AssistantMind
import com.example.victor_ai.data.network.ModelUsage

/**
 * Состояние экрана SystemScreen
 * Объединяет все параметры состояния в один data класс
 */
data class SystemScreenState(
    val isOnline: Boolean = false,
    val isChecking: Boolean = true,
    val modelUsageList: List<ModelUsage> = emptyList(),
    val assistantState: String? = null,
    val emotionalShift: String? = null,
    val assistantMind: List<AssistantMind> = emptyList(),
    val trustLevel: Int = 0,
    val currentModel: String? = null
) {
    /**
     * Группировка моделей по провайдерам
     */
    val usageByProvider: Map<String, List<ModelUsage>>
        get() = modelUsageList.groupBy { it.provider }

    /**
     * Текущий провайдер для выбранной модели
     */
    val currentProvider: String?
        get() = if (currentModel != null) {
            modelUsageList.find { it.model_name == currentModel }?.provider
        } else null

    /**
     * Отображаемый провайдер (текущий или первый доступный)
     */
    val displayProvider: String
        get() = currentProvider ?: usageByProvider.keys.firstOrNull() ?: "N/A"

    /**
     * Процент оставшегося баланса
     */
    val balancePercent: String
        get() {
            if (usageByProvider.isEmpty()) return "N/A"

            val entries = usageByProvider[displayProvider] ?: emptyList()
            if (entries.isEmpty()) return "N/A"

            val totalSpent = entries.sumOf {
                (it.input_tokens_used * it.input_token_price +
                 it.output_tokens_used * it.output_token_price).toDouble()
            }
            val balance = entries.first().account_balance.toDouble().coerceAtLeast(0.01)
            val percentRemaining = (1.0 - totalSpent / balance).coerceIn(0.0, 1.0)

            return "${(percentRemaining * 100).toInt()}%"
        }
}
