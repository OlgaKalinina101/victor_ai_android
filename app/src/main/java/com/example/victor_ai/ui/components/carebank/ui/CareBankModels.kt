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

package com.example.victor_ai.ui.components.carebank.ui

/**
 * Сценарий автоматизации поиска для Care Bank
 * @param name Название сценария (для логов)
 * @param tapSearchYdp Координата Y для тапа по полю поиска (в dp от верха)
 * @param tapAddYdp Координата Y для тапа по кнопке "добавить в корзину" (в dp)
 * @param searchText Текст для поиска
 */
data class SearchScenario(
    val name: String,
    val tapSearchYdp: Int,
    val tapAddYdp: Int,
    val searchText: String
)

/**
 * Список сценариев для автоматизации Care Bank
 * Можно добавлять сколько угодно сценариев
 */
val scenarios = listOf(
    SearchScenario("Блинчики", tapSearchYdp = 138, tapAddYdp = 80, searchText = "блинчики"),
    // Добавляем сколько хотим
)

/**
 * Индекс текущего выбранного сценария (по умолчанию первый)
 */
var currentScenarioIndex = 0

