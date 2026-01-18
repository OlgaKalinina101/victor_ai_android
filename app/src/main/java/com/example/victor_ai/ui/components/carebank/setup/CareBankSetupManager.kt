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

import android.content.Context
import android.util.Log
import android.webkit.WebView
import androidx.compose.ui.geometry.Rect
import com.example.victor_ai.data.repository.CareBankRepository
import com.example.victor_ai.ui.components.carebank.actions.addItemsToCart
import com.example.victor_ai.ui.components.carebank.actions.executeSearchWithCoords
import com.example.victor_ai.ui.components.carebank.actions.openCart

/**
 * Менеджер настройки автоматизации Care Bank
 * Управляет процессом настройки: сбор координат, тестирование, сохранение
 */

/**
 * Получение инструкции для CoordinatePicker в зависимости от шага настройки
 * @param setupStep Текущий шаг настройки
 * @return Текст инструкции для пользователя
 */
fun getCoordinatePickerInstruction(setupStep: Int): String {
    return when (setupStep) {
        1 -> "Перетащи кружочек на поле поиска"
        3 -> "Перетащи кружочек на корзинку"
        4 -> "Перетащи кружочек на кнопку оформления заказа"
        else -> "Перетащи кружочек на нужное место"
    }
}

/**
 * Обработка ответа пользователя в режиме настройки
 * @param answer Ответ пользователя (текст)
 * @param setupMode Флаг режима настройки
 * @param repository Репозиторий для сохранения данных
 * @param emoji Эмодзи для которого настраивается автоматизация
 * @param currentSetupStep Текущий шаг настройки
 * @param currentSavedSearchText Сохраненный текст поиска
 * @param currentSavedSearchUrl Сохраненный URL поиска
 * @param currentSavedSearchFieldCoords Сохраненные координаты поля поиска
 * @param currentUrl Текущий URL страницы
 * @param webView WebView для выполнения действий
 * @param context Context приложения
 * @param updateState Callback для обновления состояния UI
 * @param webViewBounds Позиция WebView на экране
 */
fun handleUserAnswer(
    answer: String,
    setupMode: Boolean,
    repository: CareBankRepository?,
    emoji: String?,
    currentSetupStep: Int,
    currentSavedSearchText: String?,
    currentSavedSearchUrl: String?,
    currentSavedSearchFieldCoords: String?, // Screen координаты в формате "x,y"
    currentUrl: String,
    webView: WebView?,
    context: Context,
    updateState: (message: String, step: Int, showPicker: Boolean, showMultiPicker: Boolean) -> Unit,
    webViewBounds: Rect? = null // Позиция WebView на экране
) {
    if (!setupMode || repository == null || emoji == null) return

    when (currentSetupStep) {
        0 -> {
            // Шаг 0: Пользователь должен ввести "готово" для подтверждения открытия поиска
            Log.d("CareBankSetupManager", "handleUserAnswer шаг 0: answer='$answer', currentUrl='$currentUrl'")
            if (answer.lowercase() == "готово") {
                // Сохраняем текущий URL как searchUrl
                saveAutomationData(repository, emoji, searchUrl = currentUrl)
                Log.d("CareBankSetupManager", "Поиск подтвержден, URL сохранен: $currentUrl, вызываем updateState с showPicker=true")
                updateState("Теперь перемести кружочек на поле поиска", 1, true, false)
            } else {
                Log.d("CareBankSetupManager", "Ответ не 'готово', остаемся на шаге 0")
                updateState("Напиши 'готово' когда откроется поле поиска", 0, false, false)
            }
        }
        1 -> {
            // Шаг 1: Пользователь ввел текст для тестирования поиска
            Log.d("CareBankSetupManager", "Шаг 1: Получен текст для тестирования: $answer, координаты: $currentSavedSearchFieldCoords")
            if (currentSavedSearchFieldCoords != null && webViewBounds != null) {
                // Показываем сообщение о начале тестирования
                updateState("Тестирую поиск с текстом '$answer'... Подожди результатов", 1, false, false)
                
                // Запускаем реальное тестирование автоматизации
                executeSearchWithCoords(
                    coords = currentSavedSearchFieldCoords,
                    testText = answer,
                    webView = webView,
                    webViewBounds = webViewBounds,
                    updateState = { newMessage, newStep, showPicker, showMultiPicker ->
                        updateState(newMessage, newStep, showPicker, showMultiPicker)
                    }
                )
            } else {
                Log.e("CareBankSetupManager", "Ошибка: координаты или webViewBounds не доступны!")
                updateState("Ошибка: координаты поля поиска не сохранены", 1, false, false)
            }
        }
    }
}

/**
 * Обработка выбранной координаты (одной точки)
 * @param x Screen координата X (null если пункта нет)
 * @param y Screen координата Y (null если пункта нет)
 * @param setupMode Флаг режима настройки
 * @param repository Репозиторий для сохранения данных
 * @param emoji Эмодзи для которого настраивается автоматизация
 * @param currentSetupStep Текущий шаг настройки
 * @param currentSavedSearchText Сохраненный текст поиска
 * @param currentSavedSearchUrl Сохраненный URL поиска
 * @param updateState Callback для обновления состояния UI
 * @param onCoordsSaved Callback для сохранения координат в локальное состояние
 * @param webView WebView для выполнения действий
 * @param context Context приложения
 * @param webViewBounds Позиция WebView на экране
 */
fun handleCoordinateSelected(
    x: Int?, // Screen координата X (null если пункта нет)
    y: Int?, // Screen координата Y (null если пункта нет)
    setupMode: Boolean,
    repository: CareBankRepository?,
    emoji: String?,
    currentSetupStep: Int,
    currentSavedSearchText: String?,
    currentSavedSearchUrl: String?,
    updateState: (message: String, step: Int, showPicker: Boolean, showMultiPicker: Boolean) -> Unit,
    onCoordsSaved: (String?) -> Unit = {},
    webView: WebView? = null,
    context: Context? = null,
    webViewBounds: Rect? = null // Позиция WebView на экране
) {
    if (!setupMode || repository == null || emoji == null) return

    val coords = if (x != null && y != null) "$x,$y" else null

    when (currentSetupStep) {
        1 -> {
            // Координаты поля поиска сохранены
            Log.d("CareBankSetupManager", "Шаг 1: Сохраняем координаты поля поиска: $coords")
            saveAutomationData(
                repository = repository,
                emoji = emoji,
                searchField = coords
            )
            // Сохраняем координаты в локальном состоянии через callback
            onCoordsSaved(coords)
            
            if (coords != null) {
            // Переходим к вводу текста для тестирования
            updateState("Отлично! Теперь введи слово для тестирования поиска (например 'блинчики')", 1, false, false)
            } else {
                // Пропускаем этап, так как пункта нет
                updateState("Понял, этого пункта нет. Переходим к следующему шагу", 2, false, true)
            }
        }
        3 -> {
            // Координаты корзинки - сохраняем и тестируем
            Log.d("CareBankSetupManager", "Шаг 3: Сохраняем координаты корзинки: $coords")
            saveAutomationData(repository = repository, emoji = emoji, openCartCoords = coords)
            
            if (coords != null && x != null && y != null) {
            // Тестируем тап по корзинке
            if (webView != null && context != null && webViewBounds != null) {
                updateState("Тестирую тап по корзинке... Подожди", 3, false, false)
                openCart(
                    screenX = x,
                    screenY = y,
                    webView = webView,
                    webViewBounds = webViewBounds,
                    updateState = updateState
                )
                } else {
                        updateState("Теперь покажи где кнопка оформления заказа", 4, true, false)
                }
            } else {
                // Пропускаем этап, так как пункта нет
                updateState("Понял, этого пункта нет. Теперь покажи где кнопка оформления заказа", 4, true, false)
            }
        }
        4 -> {
            // Координаты оформления заказа - только сохраняем
            Log.d("CareBankSetupManager", "Шаг 4: Сохраняем координаты оформления заказа: $coords")
            saveAutomationData(repository = repository, emoji = emoji, placeOrderCoords = coords)
            
            if (coords != null) {
            updateState("Тут не тапаю, верю на слово ✨ Настройки автоматизации сохранены!", 5, false, false)
            } else {
                updateState("Понял, этого пункта нет. Настройки автоматизации сохранены!", 5, false, false)
            }
        }
    }
}

/**
 * Обработка выбранных координат (несколько точек)
 * @param coords Screen координаты списком (null если пункта нет)
 * @param setupMode Флаг режима настройки
 * @param repository Репозиторий для сохранения данных
 * @param emoji Эмодзи для которого настраивается автоматизация
 * @param currentSetupStep Текущий шаг настройки
 * @param currentSavedSearchText Сохраненный текст поиска
 * @param currentSavedSearchUrl Сохраненный URL поиска
 * @param webView WebView для выполнения действий
 * @param context Context приложения
 * @param updateState Callback для обновления состояния UI
 * @param webViewBounds Позиция WebView на экране
 */
fun handleCoordinatesSelected(
    coords: List<Pair<Int, Int>>?, // Screen координаты (null если пункта нет)
    setupMode: Boolean,
    repository: CareBankRepository?,
    emoji: String?,
    currentSetupStep: Int,
    currentSavedSearchText: String?,
    currentSavedSearchUrl: String?,
    webView: WebView?,
    context: Context,
    updateState: (message: String, step: Int, showPicker: Boolean, showMultiPicker: Boolean) -> Unit,
    webViewBounds: Rect? = null // Позиция WebView на экране
) {
    if (!setupMode || repository == null || emoji == null) return

    when (currentSetupStep) {
        2 -> {
            if (coords != null && coords.isNotEmpty()) {
            // Координаты кнопок "добавить в корзину" (screen координаты)
            Log.d("CareBankSetupManager", "Шаг 2: Сохраняем ${coords.size} screen координат кнопок 'добавить в корзину'")
            val coordStrings = coords.map { "${it.first},${it.second}" }
            saveAutomationData(
                repository = repository,
                emoji = emoji,
                addToCart1Coords = coordStrings.getOrNull(0),
                addToCart2Coords = coordStrings.getOrNull(1),
                addToCart3Coords = coordStrings.getOrNull(2),
                addToCart4Coords = coordStrings.getOrNull(3),
                addToCart5Coords = coordStrings.getOrNull(4)
            )
            
            // Тестируем кнопки "добавить в корзину"
            if (webViewBounds != null) {
                updateState("Тестирую кнопки 'добавить в корзину'... Подожди", 2, false, false)
                addItemsToCart(
                    screenCoords = coords,
                    webView = webView,
                    webViewBounds = webViewBounds,
                    updateState = { newMessage, newStep, showPicker, showMultiPicker ->
                        updateState(newMessage, newStep, showPicker, showMultiPicker)
                    }
                )
            } else {
                Log.e("CareBankSetupManager", "❌ webViewBounds is null")
                updateState("Ошибка: не удалось получить позицию WebView", 2, false, true)
                }
            } else {
                // Пропускаем этап, так как пункта нет
                Log.d("CareBankSetupManager", "Шаг 2: Пункт отсутствует, сохраняем null")
                saveAutomationData(
                    repository = repository,
                    emoji = emoji,
                    addToCart1Coords = null,
                    addToCart2Coords = null,
                    addToCart3Coords = null,
                    addToCart4Coords = null,
                    addToCart5Coords = null
                )
                
                // Переходим к следующему шагу
                updateState("Понял, этого пункта нет. Теперь покажи где корзинка", 3, true, false)
            }
        }
    }
}

