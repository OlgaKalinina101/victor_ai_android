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

package com.example.victor_ai.ui.components

/**
 * Состояния настройки автоматизации для веб-сайтов
 */
sealed class AutomationSetupState {
    object Idle : AutomationSetupState()

    // Этап 1: Настройка поиска
    data class SetupSearch(
        val searchUrl: String? = null,
        val searchFieldCoords: Pair<Int, Int>? = null,
        val testText: String? = null
    ) : AutomationSetupState()

    // Этап 2: Настройка добавления в корзину
    data class SetupCartItems(
        val cartCoords: List<Pair<Int, Int>> = emptyList()
    ) : AutomationSetupState()

    // Этап 3: Настройка корзинки
    data class SetupCart(
        val cartCoords: Pair<Int, Int>? = null
    ) : AutomationSetupState()

    // Этап 4: Настройка оформления заказа
    data class SetupOrder(
        val orderCoords: Pair<Int, Int>? = null
    ) : AutomationSetupState()

    object Completed : AutomationSetupState()
}

/**
 * Действия для перехода между состояниями
 */
sealed class AutomationSetupAction {
    data class StartSetup(val emoji: String) : AutomationSetupAction()
    data class SetSearchUrl(val url: String) : AutomationSetupAction()
    data class SetSearchFieldCoords(val coords: Pair<Int, Int>, val testText: String) : AutomationSetupAction()
    data class SetCartItemCoords(val coords: List<Pair<Int, Int>>) : AutomationSetupAction()
    data class SetCartCoords(val coords: Pair<Int, Int>) : AutomationSetupAction()
    data class SetOrderCoords(val coords: Pair<Int, Int>) : AutomationSetupAction()
    object CompleteSetup : AutomationSetupAction()
    object CancelSetup : AutomationSetupAction()
}
