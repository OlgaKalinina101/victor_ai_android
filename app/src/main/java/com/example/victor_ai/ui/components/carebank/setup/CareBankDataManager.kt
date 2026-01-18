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

package com.example.victor_ai.ui.components.carebank.setup

import android.util.Log
import com.example.victor_ai.data.repository.CareBankRepository
import kotlinx.coroutines.runBlocking

/**
 * Менеджер для сохранения и управления данными автоматизации Care Bank
 */

/**
 * Сохранение данных автоматизации в репозиторий
 * @param repository Репозиторий для сохранения данных
 * @param emoji Эмодзи для которого сохраняются данные
 * @param searchUrl URL страницы поиска
 * @param searchField Координаты поля поиска в формате "x,y"
 * @param addToCart1Coords Координаты первой кнопки "добавить в корзину"
 * @param addToCart2Coords Координаты второй кнопки "добавить в корзину"
 * @param addToCart3Coords Координаты третьей кнопки "добавить в корзину"
 * @param addToCart4Coords Координаты четвертой кнопки "добавить в корзину"
 * @param addToCart5Coords Координаты пятой кнопки "добавить в корзину"
 * @param openCartCoords Координаты кнопки открытия корзины
 * @param placeOrderCoords Координаты кнопки оформления заказа
 */
fun saveAutomationData(
    repository: CareBankRepository?,
    emoji: String?,
    searchUrl: String? = null,
    searchField: String? = null,
    addToCart1Coords: String? = null,
    addToCart2Coords: String? = null,
    addToCart3Coords: String? = null,
    addToCart4Coords: String? = null,
    addToCart5Coords: String? = null,
    openCartCoords: String? = null,
    placeOrderCoords: String? = null
) {
    if (repository == null || emoji == null) return

    runBlocking {
        // Получаем текущую запись
        val currentEntry = repository.getEntryByEmoji(emoji)

        if (currentEntry != null) {
            // Обновляем запись с новыми данными
            repository.saveEntry(
                emoji = currentEntry.emoji,
                value = currentEntry.value,
                searchUrl = searchUrl ?: currentEntry.searchUrl,
                searchField = searchField ?: currentEntry.searchField,
                addToCart1Coords = addToCart1Coords ?: currentEntry.addToCart1Coords,
                addToCart2Coords = addToCart2Coords ?: currentEntry.addToCart2Coords,
                addToCart3Coords = addToCart3Coords ?: currentEntry.addToCart3Coords,
                addToCart4Coords = addToCart4Coords ?: currentEntry.addToCart4Coords,
                addToCart5Coords = addToCart5Coords ?: currentEntry.addToCart5Coords,
                openCartCoords = openCartCoords ?: currentEntry.openCartCoords,
                placeOrderCoords = placeOrderCoords ?: currentEntry.placeOrderCoords
            )
            Log.d("CareBankDataManager", "Обновил данные автоматизации для $emoji")
        } else {
            Log.e("CareBankDataManager", "Не найдена запись для эмодзи $emoji")
        }
    }
}

