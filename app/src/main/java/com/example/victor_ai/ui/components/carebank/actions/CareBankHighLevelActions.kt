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

package com.example.victor_ai.ui.components.carebank.actions

import android.content.Context
import android.util.Log
import android.webkit.WebView
import androidx.compose.ui.geometry.Rect

/**
 * Высокоуровневые действия для автоматизации Care Bank
 * Эти функции выполняют реальные действия (жмут кнопки) и используются:
 * - В режиме setup - для проверки правильности координат
 * - В продовом режиме - для реального выполнения автоматизации
 */

/**
 * Тап по полю поиска
 * @param coords Screen координаты в формате "x,y"
 * @param webView WebView для выполнения
 * @param sessionId ID сессии
 * @param onComplete Callback по завершению
 * @param onError Callback при ошибке
 */
fun tapSearchField(
    coords: String,
    webView: WebView,
    sessionId: Long,
    onComplete: () -> Unit,
    onError: (String) -> Unit
) {
    Log.d("CareBankActions", "🎯 Тап по полю поиска: coords=$coords")
    
    // Парсим screen координаты "x,y"
    val coordParts = coords.split(",")
    if (coordParts.size != 2) {
        Log.e("CareBankActions", "❌ Некорректный формат координат: $coords")
        onError("Некорректный формат координат")
        return
    }
    
    val screenX = coordParts[0].toIntOrNull()
    val screenY = coordParts[1].toIntOrNull()
    
    if (screenX == null || screenY == null) {
        Log.e("CareBankActions", "❌ Не удалось распарсить координаты: $coords")
        onError("Не удалось распарсить координаты")
        return
    }
    
    // Получаем АКТУАЛЬНУЮ позицию WebView на экране
    val location = IntArray(2)
    webView.getLocationOnScreen(location)
    val actualWebViewLeft = location[0]
    val actualWebViewTop = location[1]
    
    // Преобразование: screen -> WebView relative
    val webViewX = screenX - actualWebViewLeft
    val webViewY = screenY - actualWebViewTop
    
    Log.d("CareBankActions", "📍 screen($screenX, $screenY) -> webViewRelative($webViewX, $webViewY)")
    
    // Тап по координатам
    performTapSimple(webView, webViewX, webViewY, sessionId) {
        Log.d("CareBankActions", "✅ Тап выполнен")
        // Ждём появления input поля
        webView.postDelayed({
            onComplete()
        }, 1500)
    }
}

/**
 * Фокус на input и ввод текста
 * @param text Текст для ввода
 * @param webView WebView для выполнения
 * @param sessionId ID сессии
 * @param onComplete Callback по завершению
 * @param onError Callback при ошибке
 */
fun focusAndTypeText(
    text: String,
    webView: WebView,
    sessionId: Long,
    onComplete: () -> Unit,
    onError: (String) -> Unit
) {
    Log.d("CareBankActions", "⌨️ Фокус и ввод текста: '$text'")
    
    focusSearchInput(webView, sessionId,
        onSuccess = {
            Log.d("CareBankActions", "✅ Фокус установлен")
            
            // Ввод текста
            typeText(webView, text, delayMs = 120, sessionId) {
                Log.d("CareBankActions", "✅ Текст введен")
                onComplete()
            }
        },
        onError = {
            Log.e("CareBankActions", "❌ Не удалось установить фокус")
            onError("Не удалось найти поле ввода")
        }
    )
}

/**
 * Выполнить поиск: тап + фокус + ввод + Enter
 * @param coords Screen координаты поля поиска
 * @param text Текст для поиска
 * @param webView WebView для выполнения
 * @param sessionId ID сессии
 * @param onComplete Callback по завершению
 * @param onError Callback при ошибке
 */
fun executeSearch(
    coords: String,
    text: String,
    webView: WebView,
    sessionId: Long,
    onComplete: () -> Unit,
    onError: (String) -> Unit
) {
    Log.d("CareBankActions", "🔍 Выполнение поиска: coords=$coords, text='$text'")
    
    // Шаг 1: Тап по полю поиска
    tapSearchField(coords, webView, sessionId,
        onComplete = {
            // Шаг 2: Фокус и ввод текста
            focusAndTypeText(text, webView, sessionId,
                onComplete = {
                    // Шаг 3: Enter
                    webView.postDelayed({
                        pressEnter(webView, sessionId) {
                            Log.d("CareBankActions", "✅ Enter нажат")
                            // Ждём результатов поиска
                            webView.postDelayed({
                                Log.d("CareBankActions", "🎉 Поиск завершён")
                                onComplete()
                            }, 2000)
                        }
                    }, 500)
                },
                onError = onError
            )
        },
        onError = onError
    )
}

/**
 * Выполнить поиск по координатам (бывший testSearchAutomation)
 * @param coords Screen координаты в формате "x,y"
 * @param testText Текст для поиска
 * @param webView WebView для выполнения действий
 * @param webViewBounds Позиция WebView (НЕ ИСПОЛЬЗУЕТСЯ - получаем актуальную)
 * @param updateState Callback для обновления состояния UI
 */
fun executeSearchWithCoords(
    coords: String, 
    testText: String, 
    webView: WebView?,
    webViewBounds: Rect, // Оставляем для совместимости, но не используем
    updateState: (String, Int, Boolean, Boolean) -> Unit
) {
    Log.d("CareBankActions", "🧪 Выполняю поиск с координатами: $coords, текстом: $testText")
    
    if (webView == null) {
        Log.e("CareBankActions", "❌ WebView is null, не могу выполнить поиск")
        updateState("Ошибка: WebView не доступен", 1, false, false)
        return
    }
    
    // Парсим screen координаты "x,y"
    val coordParts = coords.split(",")
    if (coordParts.size != 2) {
        Log.e("CareBankActions", "❌ Некорректный формат координат: $coords")
        updateState("Ошибка: некорректные координаты", 1, false, false)
        return
    }
    
    val screenX = coordParts[0].toIntOrNull()
    val screenY = coordParts[1].toIntOrNull()
    
    if (screenX == null || screenY == null) {
        Log.e("CareBankActions", "❌ Не удалось распарсить координаты: $coords")
        updateState("Ошибка: некорректные координаты", 1, false, false)
        return
    }
    
    // Получаем АКТУАЛЬНУЮ позицию WebView на экране
    val location = IntArray(2)
    webView.getLocationOnScreen(location)
    val actualWebViewLeft = location[0]
    val actualWebViewTop = location[1]
    
    // Преобразование: screen -> WebView relative
    val webViewX = screenX - actualWebViewLeft
    val webViewY = screenY - actualWebViewTop
    
    Log.d("CareBankActions", "📍 screen($screenX, $screenY)")
    Log.d("CareBankActions", "📍 actualWebView($actualWebViewLeft, $actualWebViewTop)")
    Log.d("CareBankActions", "🎯 -> webViewRelative($webViewX, $webViewY)")
    
    val sessionId = System.currentTimeMillis()
    
    Log.d("CareBankActions", "🎯 Начинаем выполнение поиска (session=$sessionId)")
    
    // Шаг 1: Тап по координатам поля поиска
    performTapSimple(webView, webViewX, webViewY, sessionId) {
        Log.d("CareBankActions", "✅ Шаг 1/4: Тап выполнен, ждем появления input поля...")
        
        // Проверяем что появилось после тапа
        webView.evaluateJavascript("""
            (function() {
                const inputs = document.querySelectorAll('input');
                const textareas = document.querySelectorAll('textarea');
                console.log('После тапа: inputs=' + inputs.length + ', textareas=' + textareas.length);
                
                // Выводим все найденные элементы
                inputs.forEach((inp, idx) => {
                    console.log('Input ' + idx + ':', inp.type, inp.placeholder, 'visible:', inp.offsetParent !== null);
                });
                
                return 'inputs:' + inputs.length + ',textareas:' + textareas.length;
            })();
        """.trimIndent()) { result ->
            Log.d("CareBankActions", "🔍 Элементы после тапа: $result")
        }
        
        // Увеличиваем задержку до 1500ms - даем странице время отрендерить поле поиска
        webView.postDelayed({
            // Шаг 2: Фокус на input поле
            focusSearchInput(webView, sessionId, 
                onSuccess = {
                    Log.d("CareBankActions", "✅ Шаг 2/4: Фокус установлен")
                    
                    // Шаг 3: Ввод текста
                    typeText(webView, testText, delayMs = 120, sessionId) {
                        Log.d("CareBankActions", "✅ Шаг 3/4: Текст введен")
                        
                        // Небольшая задержка перед Enter
                        webView.postDelayed({
                            // Шаг 4: Нажатие Enter
                            pressEnter(webView, sessionId) {
                                Log.d("CareBankActions", "✅ Шаг 4/4: Enter нажат")
                                
                                // Ждем результатов поиска
                                webView.postDelayed({
                                    Log.d("CareBankActions", "🎉 Поиск выполнен успешно!")
                                    updateState("Поиск работает! Теперь перетащи кружочки на кнопки 'добавить в корзину'", 2, false, true)
                                }, 2000) // 2 секунды для загрузки результатов
                            }
                        }, 500)
                    }
                },
                onError = {
                    Log.e("CareBankActions", "❌ Не удалось найти input поле")
                    updateState("Ошибка: не найдено поле поиска. Попробуй выбрать другие координаты", 1, true, false)
                }
            )
        }, 1500) // Увеличили задержку для появления поля поиска
    }
}

/**
 * Добавить товары в корзину (бывший testAddToCartButtons)
 * @param screenCoords Screen координаты кнопок
 * @param webView WebView для выполнения действий
 * @param webViewBounds Позиция WebView в screen координатах (НЕ ИСПОЛЬЗУЕТСЯ - получаем актуальную)
 * @param updateState Callback для обновления состояния UI
 */
fun addItemsToCart(
    screenCoords: List<Pair<Int, Int>>,
    webView: WebView?,
    webViewBounds: Rect, // Оставляем для совместимости, но не используем
    updateState: (String, Int, Boolean, Boolean) -> Unit
) {
    Log.d("CareBankActions", "🧪 Добавляю ${screenCoords.size} товаров в корзину")
    
    if (webView == null) {
        Log.e("CareBankActions", "❌ WebView is null")
        updateState("Ошибка: WebView не доступен", 2, false, true)
        return
    }
    
    if (screenCoords.isEmpty()) {
        Log.e("CareBankActions", "❌ Нет координат для добавления товаров")
        updateState("Ошибка: нет координат кнопок", 2, false, true)
        return
    }
    
    val sessionId = System.currentTimeMillis()
    
    // Последовательно тапаем по всем кнопкам с задержками
    fun tapNextButton(index: Int) {
        if (index >= screenCoords.size) {
            // Все кнопки обработаны
            Log.d("CareBankActions", "✅ Все ${screenCoords.size} товара добавлены в корзину!")
            webView.postDelayed({
                updateState("Кнопки работают! Теперь покажи где корзинка", 3, true, false)
            }, 1000)
            return
        }
        
        val (screenX, screenY) = screenCoords[index]
        
        // Получаем АКТУАЛЬНУЮ позицию WebView на экране (не из state!)
        val location = IntArray(2)
        webView.getLocationOnScreen(location)
        val actualWebViewLeft = location[0]
        val actualWebViewTop = location[1]
        
        // Преобразование: screen -> WebView relative
        val webViewX = screenX - actualWebViewLeft
        val webViewY = screenY - actualWebViewTop
        
        Log.d("CareBankActions", "🎯 Товар ${index + 1}/${screenCoords.size}:")
        Log.d("CareBankActions", "   screen($screenX, $screenY)")
        Log.d("CareBankActions", "   actualWebView($actualWebViewLeft, $actualWebViewTop)")
        Log.d("CareBankActions", "   -> webViewRelative($webViewX, $webViewY)")
        
        performTapSimple(webView, webViewX, webViewY, sessionId) {
            Log.d("CareBankActions", "✅ Товар ${index + 1} добавлен")
            
            // Задержка между кнопками
            webView.postDelayed({
                tapNextButton(index + 1)
            }, 500)
        }
    }
    
    // Начинаем с первой кнопки
    tapNextButton(0)
}

/**
 * Вычисляет количество свайпов в зависимости от количества позиций в корзине
 * @param itemCount количество позиций в сценарии
 * @return количество свайпов
 */
fun swipeCountForItems(itemCount: Int): Int {
    // 1-2 товара: 1 свайп
    // 3 товара: 2 свайпа
    // 4 товара: 3 свайпа и т.д.
    val swipeCount = if (itemCount <= 2) 1 else itemCount - 1
    Log.d("CareBankActions", "📏 swipeCountForItems: itemCount=$itemCount -> swipeCount=$swipeCount")
    return swipeCount
}

/**
 * Открыть корзину без callback (для боевого оркестратора)
 * @param screenX Screen координата X
 * @param screenY Screen координата Y
 * @param webView WebView для выполнения действий
 * @param itemCount количество позиций в сценарии для расчета скролла
 * @param onComplete Callback после завершения скролла
 */
fun openCartWithoutUI(
    screenX: Int,
    screenY: Int,
    webView: WebView,
    itemCount: Int,
    onComplete: () -> Unit
) {
    Log.d("CareBankActions", "🧪 Открываю корзину (без UI), screen($screenX, $screenY)")
    
    // Получаем АКТУАЛЬНУЮ позицию WebView на экране
    val location = IntArray(2)
    webView.getLocationOnScreen(location)
    val actualWebViewLeft = location[0]
    val actualWebViewTop = location[1]
    
    // Преобразование: screen -> WebView relative
    val webViewX = screenX - actualWebViewLeft
    val webViewY = screenY - actualWebViewTop
    
    Log.d("CareBankActions", "📍 actualWebView($actualWebViewLeft, $actualWebViewTop)")
    Log.d("CareBankActions", "🎯 -> webViewRelative($webViewX, $webViewY)")
    
    val sessionId = System.currentTimeMillis()
    
    // Тап по корзинке (открыть)
    performTapSimple(webView, webViewX, webViewY, sessionId) {
        Log.d("CareBankActions", "✅ Тап по корзинке выполнен (открытие)")
        webView.postDelayed({
            val swipes = swipeCountForItems(itemCount)
            Log.d("CareBankActions", "📏 Расчет скролла: $itemCount позиций → $swipes свайпов")
            
            scrollDownSmall(webView, swipeCount = swipes) {
                Log.d("CareBankActions", "✅ Скроллинг корзины завершён!")
                
                // Даём время на рендеринг после скролла
                webView.postDelayed({
                    Log.d("CareBankActions", "✅ Страница отрендерилась, вызываем onComplete")
                    onComplete()
                }, 500) // 500ms на рендеринг после скролла
            }
        }, 800)
    }
}

/**
 * Открыть корзину (бывший testCartButton)
 * @param screenX Screen координата X
 * @param screenY Screen координата Y
 * @param webView WebView для выполнения действий
 * @param webViewBounds Позиция WebView (НЕ ИСПОЛЬЗУЕТСЯ - получаем актуальную)
 * @param itemCount количество позиций в сценарии для расчета скролла
 * @param updateState Callback для обновления состояния UI
 */
fun openCart(
    screenX: Int,
    screenY: Int,
    webView: WebView,
    webViewBounds: Rect, // Оставляем для совместимости, но не используем
    itemCount: Int = 2, // По умолчанию минимальное значение
    updateState: (String, Int, Boolean, Boolean) -> Unit
) {
    Log.d("CareBankActions", "🧪 Открываю корзину, screen($screenX, $screenY)")
    
    // Получаем АКТУАЛЬНУЮ позицию WebView на экране
    val location = IntArray(2)
    webView.getLocationOnScreen(location)
    val actualWebViewLeft = location[0]
    val actualWebViewTop = location[1]
    
    // Преобразование: screen -> WebView relative
    val webViewX = screenX - actualWebViewLeft
    val webViewY = screenY - actualWebViewTop
    
    Log.d("CareBankActions", "📍 actualWebView($actualWebViewLeft, $actualWebViewTop)")
    Log.d("CareBankActions", "🎯 -> webViewRelative($webViewX, $webViewY)")
    
    val sessionId = System.currentTimeMillis()
    
    // Тап по корзинке
    performTapSimple(webView, webViewX, webViewY, sessionId) {
        Log.d("CareBankActions", "✅ Тап по корзинке выполнен!")
        webView.postDelayed({
            val swipes = swipeCountForItems(itemCount)
            Log.d("CareBankActions", "📏 Расчет скролла: $itemCount позиций → $swipes свайпов")
            updateState("Корзинка работает! Скроллю немного вниз...", 3, false, false)
            scrollDownSmall(webView, swipeCount = swipes) {
                Log.d("CareBankActions", "✅ Скроллинг завершён!")
                updateState("Теперь покажи где кнопка оформления заказа", 4, true, false)
            }
        }, 800)
    }
}

/**
 * Скролл вниз с переменным расстоянием для каждого свайпа
 * - Первый свайп: 80dp
 * - Последующие свайпы: 100dp
 * @param webView WebView для выполнения скролла
 * @param swipeCount Количество свайпов для выполнения
 * @param onComplete Callback по завершению скролла
 */
fun scrollDownSmall(webView: WebView, swipeCount: Int = 1, onComplete: () -> Unit) {
    val firstSwipeDp = 80  // Первый свайп
    val nextSwipeDp = 100  // Последующие свайпы
    
    Log.d("CareBankActions", "📜 Скроллю вниз: $swipeCount свайпов (первый: ${firstSwipeDp}dp, остальные: ${nextSwipeDp}dp)")
    
    val density = webView.context.resources.displayMetrics.density
    
    val webViewWidth = webView.width
    val webViewHeight = webView.height
    
    // Центр экрана по X
    val startX = webViewWidth / 2f
    // Начинаем с середины экрана
    val startY = webViewHeight / 2f
    
    Log.d("CareBankActions", "📜 WebView size: ${webViewWidth}x${webViewHeight}")
    
    // Функция ожидания завершения скроллинга
    fun waitForScrollToComplete(previousScrollY: Int, checkCount: Int = 0, onScrollComplete: () -> Unit) {
        val maxChecks = 20 // Максимум 2 секунды ожидания (20 x 100ms)
        
        if (checkCount >= maxChecks) {
            Log.d("CareBankActions", "⚠️ Превышено время ожидания скроллинга, продолжаю...")
            onScrollComplete()
            return
        }
        
        webView.postDelayed({
            val currentScrollY = webView.scrollY
            
            // Проверяем стабилизировался ли scrollY
            if (currentScrollY == previousScrollY) {
                Log.d("CareBankActions", "✅ Скроллинг завершён на позиции $currentScrollY")
                onScrollComplete()
            } else {
                // Еще скроллится, проверяем снова
                Log.d("CareBankActions", "⏳ Ожидаю завершения скроллинга... ($previousScrollY -> $currentScrollY)")
                waitForScrollToComplete(currentScrollY, checkCount + 1, onScrollComplete)
            }
        }, 100) // Проверяем каждые 100ms
    }
    
    // Рекурсивная функция для выполнения серии свайпов
    fun performSwipe(swipeIndex: Int) {
        if (swipeIndex >= swipeCount) {
            Log.d("CareBankActions", "✅ Все $swipeCount свайпов выполнены")
            onComplete()
            return
        }
        
        // Вычисляем расстояние для текущего свайпа
        val currentSwipeDp = if (swipeIndex == 0) firstSwipeDp else nextSwipeDp
        val currentSwipePx = (currentSwipeDp * density).toInt()
        val endY = startY - currentSwipePx
        
        Log.d("CareBankActions", "📜 Выполняю свайп ${swipeIndex + 1}/$swipeCount (${currentSwipeDp}dp = ${currentSwipePx}px)")
        
        val scrollYBeforeSwipe = webView.scrollY
        Log.d("CareBankActions", "📜 Текущая позиция до свайпа: $scrollYBeforeSwipe")
        
        val downTime = android.os.SystemClock.uptimeMillis()
        
        // DOWN event
        val downEvent = android.view.MotionEvent.obtain(
            downTime, downTime, 
            android.view.MotionEvent.ACTION_DOWN, 
            startX, startY, 0
        )
        webView.dispatchTouchEvent(downEvent)
        downEvent.recycle()
        
        // MOVE events - делаем ОЧЕНЬ МЕДЛЕННО (50ms между шагами вместо 20ms)
        // Это убирает инерцию!
        val steps = 20 // больше шагов = плавнее
        val stepY = (endY - startY) / steps
        val stepDelay = 50L // 50ms между шагами = медленно = без инерции
        
        fun performStep(stepIndex: Int) {
            if (stepIndex == 1) {
                Log.d("CareBankActions", "🎬 Начинаем движение свайпа ${swipeIndex + 1} (${currentSwipeDp}dp)")
            }
            
            if (stepIndex > steps) {
                // Все шаги выполнены - отпускаем палец
                Log.d("CareBankActions", "🏁 Все $steps шагов свайпа ${swipeIndex + 1} выполнены, отпускаем палец")
                val upTime = android.os.SystemClock.uptimeMillis()
                val upEvent = android.view.MotionEvent.obtain(
                    downTime, upTime,
                    android.view.MotionEvent.ACTION_UP,
                    startX, endY, 0
                )
                webView.dispatchTouchEvent(upEvent)
                upEvent.recycle()
                
                Log.d("CareBankActions", "✅ Свайп ${swipeIndex + 1} выполнен, жду завершения скроллинга...")
                
                // Ждем завершения скроллинга перед следующим свайпом
                webView.postDelayed({
                    Log.d("CareBankActions", "🔍 Проверяю завершение скроллинга после свайпа ${swipeIndex + 1}")
                    waitForScrollToComplete(webView.scrollY) {
                        Log.d("CareBankActions", "✅ Скроллинг завершён, переход к следующему свайпу")
                        // Скроллинг завершён, переход к следующему свайпу
                        performSwipe(swipeIndex + 1)
                    }
                }, 100)
                return
            }
            
            if (stepIndex % 5 == 0) {
                Log.d("CareBankActions", "📍 Свайп ${swipeIndex + 1}: шаг $stepIndex/$steps")
            }
            
            val moveTime = android.os.SystemClock.uptimeMillis()
            val currentY = startY + (stepY * stepIndex)
            val moveEvent = android.view.MotionEvent.obtain(
                downTime, moveTime,
                android.view.MotionEvent.ACTION_MOVE,
                startX, currentY, 0
            )
            webView.dispatchTouchEvent(moveEvent)
            moveEvent.recycle()
            
            // Следующий шаг через задержку
            webView.postDelayed({ performStep(stepIndex + 1) }, stepDelay)
        }
        
        // Начинаем движение
        Log.d("CareBankActions", "⏰ Запускаю performStep через ${stepDelay}ms")
        webView.postDelayed({ performStep(1) }, stepDelay)
    }
    
    // Начинаем с первого свайпа
    performSwipe(0)
}

/**
 * Плавный скролл страницы вниз (бывший smoothScrollToBottom)
 * Работает даже для сайтов с кастомными скролл-контейнерами
 * @param webView WebView для выполнения скролла
 * @param onComplete Callback по завершению скролла
 */
fun scrollToBottom(webView: WebView, onComplete: () -> Unit) {
    Log.d("CareBankActions", "📜 Начинаю скролл вниз через эмуляцию свайпа...")
    
    val webViewWidth = webView.width
    val webViewHeight = webView.height
    
    // Центр экрана по X, и делаем свайп от нижней части к верхней
    val startX = webViewWidth / 2f
    val startY = webViewHeight * 0.8f // Начинаем снизу (80% высоты)
    val endY = webViewHeight * 0.2f   // Заканчиваем сверху (20% высоты)
    
    Log.d("CareBankActions", "📜 WebView size: ${webViewWidth}x${webViewHeight}")
    Log.d("CareBankActions", "📜 Swipe: ($startX, $startY) -> ($startX, $endY)")
    
    // Количество свайпов для прокрутки до конца
    val swipeCount = 5
    var currentSwipe = 0
    
    fun performSwipe() {
        if (currentSwipe >= swipeCount) {
            Log.d("CareBankActions", "📜 Скролл завершён после $swipeCount свайпов")
            webView.postDelayed({ onComplete() }, 500)
            return
        }
        
        currentSwipe++
        Log.d("CareBankActions", "📜 Свайп $currentSwipe/$swipeCount")
        
        // Эмулируем свайп пальцем
        val downTime = android.os.SystemClock.uptimeMillis()
        
        // DOWN event
        val downEvent = android.view.MotionEvent.obtain(
            downTime, downTime, 
            android.view.MotionEvent.ACTION_DOWN, 
            startX, startY, 0
        )
        webView.dispatchTouchEvent(downEvent)
        downEvent.recycle()
        
        // MOVE events (плавное движение)
        val steps = 10
        val stepY = (endY - startY) / steps
        
        for (i in 1..steps) {
            val moveTime = downTime + (i * 20L)
            val currentY = startY + (stepY * i)
            val moveEvent = android.view.MotionEvent.obtain(
                downTime, moveTime,
                android.view.MotionEvent.ACTION_MOVE,
                startX, currentY, 0
            )
            webView.dispatchTouchEvent(moveEvent)
            moveEvent.recycle()
        }
        
        // UP event
        val upTime = downTime + 250L
        val upEvent = android.view.MotionEvent.obtain(
            downTime, upTime,
            android.view.MotionEvent.ACTION_UP,
            startX, endY, 0
        )
        webView.dispatchTouchEvent(upEvent)
        upEvent.recycle()
        
        // Следующий свайп через паузу (даём время на инерцию)
        webView.postDelayed({ performSwipe() }, 400)
    }
    
    performSwipe()
}

