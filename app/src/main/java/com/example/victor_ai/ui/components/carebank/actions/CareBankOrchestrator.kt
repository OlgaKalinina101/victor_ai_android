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
import com.example.victor_ai.data.network.CareBankApi
import com.example.victor_ai.domain.model.CareBankEntry
import com.example.victor_ai.data.repository.CareBankRepository
import com.example.victor_ai.ui.components.carebank.helpers.captureScreenshotAndAnalyze
import com.example.victor_ai.ui.components.carebank.ui.SearchScenario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Оркестратор для выполнения сценариев автоматизации Care Bank
 * Управляет последовательностью действий и координирует их выполнение
 */

/**
 * Проверка готовности web страницы
 * @param webView WebView для проверки
 * @param onReady Callback когда страница готова
 */
internal fun waitUntilPageIsReallyReady(webView: WebView?, onReady: () -> Unit) {
    webView?.evaluateJavascript("""
        (function() {
            console.log('🔍 Проверяем готовность страницы...');
            console.log('document.readyState:', document.readyState);
            // Разрешаем 'interactive' и 'complete' (оба означают что DOM готов)
            if (document.readyState !== 'complete' && document.readyState !== 'interactive') {
                return 'loading:readyState=' + document.readyState;
            }
            
            console.log('document.body:', document.body);
            console.log('document.body.offsetHeight:', document.body ? document.body.offsetHeight : 'null');
            if (!document.body || document.body.offsetHeight < 100) return 'empty:height=' + (document.body ? document.body.offsetHeight : 0);
            
            const hasVisibleLoader = document.querySelector('.loader, .spinner, .loading, [class*="spinner"], [class*="Loader"], [class*="Spinner"]') !== null;
            console.log('hasVisibleLoader:', hasVisibleLoader);
            if (hasVisibleLoader) {
                const loader = document.querySelector('.loader, .spinner, .loading, [class*="spinner"], [class*="Loader"], [class*="Spinner"]');
                console.log('Найден loader:', loader.className);
                return 'loading:hasLoader=' + loader.className;
            }
            
            console.log('✅ Страница полностью готова!');
            return 'ready';
        })();
    """.trimIndent()) { result ->
        val status = result?.replace("\"", "")
        Log.d("CareBankOrchestrator", "🔍 waitUntilPageIsReallyReady: статус='$status', результат='$result'")
        when (status) {
            "ready" -> {
                Log.d("CareBankOrchestrator", "✅ Страница готова! Вызываем onReady()")
                onReady()
            }
            else -> {
                Log.d("CareBankOrchestrator", "⏳ Страница не готова (статус: $status), ждём 300ms...")
                webView?.postDelayed({ waitUntilPageIsReallyReady(webView, onReady) }, 300)
            }
        }
    } ?: run {
        Log.d("CareBankOrchestrator", "⚠️ webView is null, вызываем onReady() сразу")
        onReady()
    }
}

/**
 * Функция-оркестратор: выполняет последовательность действий для автоматизации поиска
 * В будущем порядок и параметры действий будут настраиваться через UI-конфигуратор
 * 
 * @param webView WebView для выполнения действий
 * @param context Context
 * @param scenario Сценарий для выполнения (будет приходить с бэкенда)
 * @param careBankApi API для отправки скриншотов
 * @param onComplete Колбэк по завершению всего сценария (успешно)
 * @param onError Колбэк при ошибке
 */
fun executeAutomationScenario(
    webView: WebView,
    context: Context,
    scenario: SearchScenario,
    careBankApi: CareBankApi,
    onComplete: () -> Unit,
    onError: () -> Unit
) {
    val sessionId = System.currentTimeMillis()
    Log.d("CareBankOrchestrator", "🚀 [Orchestrator $sessionId] Запуск сценария: ${scenario.name}")
    
    // Конвертируем DP в пиксели для сценария
    val density = context.resources.displayMetrics.density
    val tapSearchYPx = (scenario.tapSearchYdp * density).toInt()
    
    // Шаг 1: Тап по координатам поиска
    performTap(webView, context, null, tapSearchYPx, sessionId) {
        // Шаг 2: Ждём готовности страницы
        waitUntilPageIsReallyReady(webView) {
            Log.d("CareBankOrchestrator", "🎯 [Orchestrator $sessionId] Страница готова после тапа")
            
            // Шаг 3: Фокус на input
            focusSearchInput(
                webView,
                sessionId,
                onSuccess = {
                    // Шаг 4: Ввод текста
                    typeText(webView, scenario.searchText, 120, sessionId) {
                        // Шаг 5: Ждём готовности
                        waitUntilPageIsReallyReady(webView) {
                            // Шаг 6: Enter
                            pressEnter(webView, sessionId) {
                                // Шаг 7: Ждём готовности
                                waitUntilPageIsReallyReady(webView) {
                                    // Шаг 8: Закрыть клавиатуру
                                    hideKeyboard(webView, context, sessionId) {
                                        // Шаг 9: Анализ скриншота
                                        captureScreenshotAndAnalyze(webView, context, scenario.searchText, careBankApi) { response ->
                                            if (response != null) {
                                                Log.d("CareBankOrchestrator", "🎉 [Orchestrator $sessionId] Сценарий '${scenario.name}' завершён успешно")
                                                Log.d("CareBankOrchestrator", "📸 Результат: id=${response.id}, matchType=${response.matchType}, message='${response.userMessage}'")
                                            } else {
                                                Log.w("CareBankOrchestrator", "⚠️ [Orchestrator $sessionId] Не удалось проанализировать скриншот")
                                            }
                                            onComplete()
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                onError = {
                    Log.e("CareBankOrchestrator", "❌ [Orchestrator $sessionId] Input не найден, прерываем сценарий")
                    onError()
                }
            )
        }
    }
}

/**
 * Оркестратор автоматизации Care Bank с бэкенда
 * Обрабатывает все элементы из automationData, добавляет в корзину, оформляет заказ
 * 
 * @param webView WebView для выполнения действий
 * @param context Android Context
 * @param careBankEntry Запись из БД с координатами
 * @param automationData Сценарии от бэкенда {"1": "Блинчики", "2": "Американо", ...}
 * @param repository Repository для загрузки настроек
 * @param careBankApi API для отправки скриншотов
 * @param onJarvisMessage Callback для обновления сообщения Jarvis
 * @param onAddChatMessage Callback для добавления сообщения в чат
 * @param onSendSystemEvent Callback для отправки системного события на бэкенд
 * @param onComplete Callback при успешном завершении (закрыть WebView)
 * @param onError Callback при критической ошибке
 */
fun executeCareBankAutomation(
    webView: WebView,
    context: Context,
    careBankEntry: CareBankEntry,
    automationData: Map<String, String>,
    repository: CareBankRepository,
    careBankApi: CareBankApi,
    onJarvisMessage: (String) -> Unit,
    onAddChatMessage: (String) -> Unit,
    onSendSystemEvent: (String) -> Unit,
    onComplete: () -> Unit,
    onError: (String) -> Unit
) {
    val sessionId = System.currentTimeMillis()
    Log.d("CareBankOrchestrator", "🚀 [Automation $sessionId] Начало автоматизации Care Bank")
    Log.d("CareBankOrchestrator", "📦 Элементов для обработки: ${automationData.size}")
    
    // Проверка наличия searchField
    if (careBankEntry.searchField == null) {
        Log.e("CareBankOrchestrator", "❌ searchField отсутствует в CareBankEntry")
        onError("Не настроены координаты поля поиска")
        return
    }
    
    val searchFieldCoords = careBankEntry.searchField
    
    // Сортируем элементы по ключу (1, 2, 3, ...)
    val sortedItems = automationData.entries.sortedBy { it.key }
    Log.d("CareBankOrchestrator", "📋 Порядок обработки: ${sortedItems.map { "${it.key}=${it.value}" }}")
    
    // Общее количество элементов сценария (для расчета скролла корзины)
    val totalItemCount = sortedItems.size
    
    // Рекурсивная функция для обработки элементов
    fun processNextItem(index: Int) {
        if (index >= sortedItems.size) {
            // Все элементы обработаны - переходим к корзине
            Log.d("CareBankOrchestrator", "✅ Все элементы обработаны, переходим к корзине")
            handleCartAndCheckout(
                webView = webView,
                context = context,
                entry = careBankEntry,
                repository = repository,
                onJarvisMessage = onJarvisMessage,
                onAddChatMessage = onAddChatMessage,
                onSendSystemEvent = onSendSystemEvent,
                itemCount = totalItemCount,
                onComplete = onComplete,
                sessionId = sessionId
            )
            return
        }
        
        val (itemId, itemName) = sortedItems[index]
        Log.d("CareBankOrchestrator", "🔄 Обработка элемента ${index + 1}/${sortedItems.size}: id=$itemId, name='$itemName'")
        
        // Шаг 1: Поиск товара
        executeSearch(searchFieldCoords, itemName, webView, sessionId,
            onComplete = {
                Log.d("CareBankOrchestrator", "✅ Поиск '$itemName' завершён")
                
                // Шаг 2: Скрыть клавиатуру
                hideKeyboard(webView, context, sessionId) {
                    Log.d("CareBankOrchestrator", "✅ Клавиатура скрыта")
                    
                    // Шаг 3: Скриншот и анализ
                    captureScreenshotAndAnalyze(webView, context, itemName, careBankApi) { response ->
                        if (response == null) {
                            Log.w("CareBankOrchestrator", "⚠️ Ошибка анализа скриншота для '$itemName', пропускаем")
                            onJarvisMessage("Не удалось найти $itemName 😕")
                            
                            // Проверяем, это последний товар или нет
                            val isLastItem = (index + 1) >= sortedItems.size
                            
                            if (isLastItem) {
                                Log.d("CareBankOrchestrator", "🏁 Это был последний товар, переходим к корзине без очистки")
                                processNextItem(index + 1)
                            } else {
                                Log.d("CareBankOrchestrator", "➡️ Очищаем поле для следующего товара")
                                // Тапаем на поле поиска, чтобы вернуть фокус перед очисткой
                                tapOnSearchField(searchFieldCoords, webView, sessionId) {
                                    // Переместить курсор в конец текста
                                    moveCursorToEnd(webView, sessionId) {
                                        // Очистить поле и продолжить
                                        clearInputField(webView, itemName.length, sessionId) {
                                            processNextItem(index + 1)
                                        }
                                    }
                                }
                            }
                            return@captureScreenshotAndAnalyze
                        }
                        
                        Log.d("CareBankOrchestrator", "📸 Анализ: id=${response.id}, matchType=${response.matchType}, message='${response.userMessage}'")
                        onJarvisMessage(response.userMessage)
                        
                        // Шаг 4: Добавить в корзину (независимо от matchType)
                        // Получить координаты кнопки по ID
                        val addToCartCoords = getAddToCartCoords(careBankEntry, response.id)
                        
                        if (addToCartCoords != null) {
                            Log.d("CareBankOrchestrator", "🛒 Добавляем в корзину: coords=$addToCartCoords (matchType=${response.matchType})")
                            tapAddToCartButton(addToCartCoords, webView, sessionId) {
                                Log.d("CareBankOrchestrator", "✅ Товар добавлен в корзину")
                                // Ждём готовности страницы после добавления товара
                                waitUntilPageIsReallyReady(webView) {
                                    Log.d("CareBankOrchestrator", "📄 Страница готова после добавления в корзину")
                                    
                                    // Проверяем, это последний товар или нет
                                    val isLastItem = (index + 1) >= sortedItems.size
                                    
                                    if (isLastItem) {
                                        Log.d("CareBankOrchestrator", "🏁 Это был последний товар, переходим к корзине без очистки")
                                        processNextItem(index + 1)
                                    } else {
                                        Log.d("CareBankOrchestrator", "➡️ Очищаем поле для следующего товара")
                                        // Тапаем на поле поиска, чтобы вернуть фокус перед очисткой
                                        tapOnSearchField(searchFieldCoords, webView, sessionId) {
                                            // Переместить курсор в конец текста
                                            moveCursorToEnd(webView, sessionId) {
                                                // Очистить поле и продолжить
                                                clearInputField(webView, itemName.length, sessionId) {
                                                    processNextItem(index + 1)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.w("CareBankOrchestrator", "⚠️ Координаты addToCart${response.id} отсутствуют, пропускаем")
                            
                            // Проверяем, это последний товар или нет
                            val isLastItem = (index + 1) >= sortedItems.size
                            
                            if (isLastItem) {
                                Log.d("CareBankOrchestrator", "🏁 Это был последний товар, переходим к корзине без очистки")
                                processNextItem(index + 1)
                            } else {
                                Log.d("CareBankOrchestrator", "➡️ Очищаем поле для следующего товара")
                                // Тапаем на поле поиска, чтобы вернуть фокус перед очисткой
                                tapOnSearchField(searchFieldCoords, webView, sessionId) {
                                    // Переместить курсор в конец текста
                                    moveCursorToEnd(webView, sessionId) {
                                        // Очистить поле и продолжить
                                        clearInputField(webView, itemName.length, sessionId) {
                                            processNextItem(index + 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            onError = { error ->
                Log.w("CareBankOrchestrator", "⚠️ Ошибка поиска '$itemName': $error, пропускаем")
                onJarvisMessage("Что-то пошло не так с $itemName 🤔")
                // Продолжаем со следующим элементом
                processNextItem(index + 1)
            }
        )
    }
    
    // Начинаем с первого элемента
    processNextItem(0)
}

/**
 * Получить координаты кнопки "Добавить в корзину" по ID
 */
private fun getAddToCartCoords(entry: CareBankEntry, id: String): String? {
    return when (id) {
        "1" -> entry.addToCart1Coords
        "2" -> entry.addToCart2Coords
        "3" -> entry.addToCart3Coords
        "4" -> entry.addToCart4Coords
        "5" -> entry.addToCart5Coords
        else -> {
            Log.w("CareBankOrchestrator", "⚠️ Неизвестный ID: $id")
            null
        }
    }
}

/**
 * Тап по полю поиска для возврата фокуса
 */
private fun tapOnSearchField(
    coords: String,
    webView: WebView,
    sessionId: Long,
    onComplete: () -> Unit
) {
    // Парсим координаты
    val coordParts = coords.split(",")
    if (coordParts.size != 2) {
        Log.e("CareBankOrchestrator", "❌ Некорректный формат координат поля поиска: $coords")
        onComplete()
        return
    }
    
    val screenX = coordParts[0].toIntOrNull()
    val screenY = coordParts[1].toIntOrNull()
    
    if (screenX == null || screenY == null) {
        Log.e("CareBankOrchestrator", "❌ Не удалось распарсить координаты поля поиска: $coords")
        onComplete()
        return
    }
    
    // Получаем позицию WebView
    val location = IntArray(2)
    webView.getLocationOnScreen(location)
    val webViewX = screenX - location[0]
    val webViewY = screenY - location[1]
    
    Log.d("CareBankOrchestrator", "🔍 [Session $sessionId] Тап по полю поиска для возврата фокуса: screen($screenX,$screenY) -> webView($webViewX,$webViewY)")
    
    performTapSimple(webView, webViewX, webViewY, sessionId) {
        // Даём время на установку фокуса и показ клавиатуры
        webView.postDelayed({
            Log.d("CareBankOrchestrator", "✅ [Session $sessionId] Фокус установлен на поле поиска")
            onComplete()
        }, 500)
    }
}

/**
 * Тап по кнопке "Добавить в корзину"
 */
private fun tapAddToCartButton(
    coords: String,
    webView: WebView,
    sessionId: Long,
    onComplete: () -> Unit
) {
    // Парсим координаты
    val coordParts = coords.split(",")
    if (coordParts.size != 2) {
        Log.e("CareBankOrchestrator", "❌ Некорректный формат координат: $coords")
        onComplete()
        return
    }
    
    val screenX = coordParts[0].toIntOrNull()
    val screenY = coordParts[1].toIntOrNull()
    
    if (screenX == null || screenY == null) {
        Log.e("CareBankOrchestrator", "❌ Не удалось распарсить координаты: $coords")
        onComplete()
        return
    }
    
    // Получаем позицию WebView
    val location = IntArray(2)
    webView.getLocationOnScreen(location)
    val webViewX = screenX - location[0]
    val webViewY = screenY - location[1]
    
    Log.d("CareBankOrchestrator", "🎯 Тап по кнопке: screen($screenX,$screenY) -> webView($webViewX,$webViewY)")
    
    performTapSimple(webView, webViewX, webViewY, sessionId) {
        // Увеличена задержка для рендеринга страницы после добавления товара
        webView.postDelayed({
            Log.d("CareBankOrchestrator", "⏱️ Ожидание завершено после добавления в корзину")
            onComplete()
        }, 1200)
    }
}

/**
 * Обработка корзины и оформление заказа
 */
private fun handleCartAndCheckout(
    webView: WebView,
    context: Context,
    entry: CareBankEntry,
    repository: CareBankRepository,
    onJarvisMessage: (String) -> Unit,
    onAddChatMessage: (String) -> Unit,
    onSendSystemEvent: (String) -> Unit,
    itemCount: Int,
    onComplete: () -> Unit,
    sessionId: Long
) {
    Log.d("CareBankOrchestrator", "🛒 Переход к обработке корзины")
    
    // Проверяем наличие координат корзины
    if (entry.openCartCoords == null) {
        Log.w("CareBankOrchestrator", "⚠️ openCartCoords отсутствует, завершаем")
        onJarvisMessage("Накидал тебе корзинку, посмотришь?")
        // Не вызываем onComplete - оставляем WebView открытым
        return
    }
    
    // Скрываем клавиатуру перед открытием корзины
    Log.d("CareBankOrchestrator", "⌨️ Скрываем клавиатуру перед открытием корзины")
    hideKeyboard(webView, context, sessionId) {
        Log.d("CareBankOrchestrator", "✅ Клавиатура скрыта, открываем корзину")
        openCartInternal(webView, context, entry, repository, onJarvisMessage, onAddChatMessage, onSendSystemEvent, itemCount, onComplete, sessionId)
    }
}

/**
 * Внутренняя функция открытия корзины (после скрытия клавиатуры)
 */
private fun openCartInternal(
    webView: WebView,
    context: Context,
    entry: CareBankEntry,
    repository: CareBankRepository,
    onJarvisMessage: (String) -> Unit,
    onAddChatMessage: (String) -> Unit,
    onSendSystemEvent: (String) -> Unit,
    itemCount: Int,
    onComplete: () -> Unit,
    sessionId: Long
) {
    // Открываем корзину
    val cartCoordParts = entry.openCartCoords!!.split(",")
    if (cartCoordParts.size != 2) {
        Log.e("CareBankOrchestrator", "❌ Некорректный формат openCartCoords")
        onJarvisMessage("Накидал тебе корзинку, посмотришь?")
        return
    }
    
    val cartScreenX = cartCoordParts[0].toIntOrNull()
    val cartScreenY = cartCoordParts[1].toIntOrNull()
    
    if (cartScreenX == null || cartScreenY == null) {
        Log.e("CareBankOrchestrator", "❌ Не удалось распарсить openCartCoords")
        onJarvisMessage("Накидал тебе корзинку, посмотришь?")
        return
    }
    
    // itemCount приходит из боевого оркестратора (кол-во позиций в сценарии),
    // в режиме обучения openCart вызывается отдельно и там всегда 2
    Log.d("CareBankOrchestrator", "🛒 Открываем корзину с itemCount=$itemCount")
    
    // Используем openCartWithoutUI для боевого оркестратора (без промежуточного callback)
    openCartWithoutUI(cartScreenX, cartScreenY, webView, itemCount) {
        Log.d("CareBankOrchestrator", "✅ Корзина открыта и прокручена")
        
        // Загружаем настройки в фоновом потоке, затем работаем с WebView на главном
        CoroutineScope(Dispatchers.IO).launch {
            val settingsResult = repository.getCareBankSettings()
            
            settingsResult.onSuccess { settings ->
                Log.d("CareBankOrchestrator", "⚙️ Настройки загружены: autoApproved=${settings.autoApproved}")
                
                // Переключаемся на главный поток для работы с WebView
                withContext(Dispatchers.Main) {
                    if (settings.autoApproved) {
                        // Автоматическое оформление заказа
                        Log.d("CareBankOrchestrator", "🤖 Автоматическое оформление заказа")
                        
                        if (entry.placeOrderCoords != null) {
                            val orderCoordParts = entry.placeOrderCoords.split(",")
                            if (orderCoordParts.size == 2) {
                                val orderX = orderCoordParts[0].toIntOrNull()
                                val orderY = orderCoordParts[1].toIntOrNull()
                                
                                if (orderX != null && orderY != null) {
                                    // Тап по кнопке оформления (первый)
                                    val location = IntArray(2)
                                    webView.getLocationOnScreen(location)
                                    val webViewX = orderX - location[0]
                                    val webViewY = orderY - location[1]
                                    
                                    Log.d("CareBankOrchestrator", "🎯 Первый тап по кнопке 'Оформить заказ': ($webViewX, $webViewY)")
                                    performTapSimple(webView, webViewX, webViewY, sessionId) {
                                        Log.d("CareBankOrchestrator", "✅ Первый тап выполнен, ждём рендеринга...")
                                        
                                        // Ждём рендеринга страницы после первого тапа
                                        webView.postDelayed({
                                            Log.d("CareBankOrchestrator", "🔄 Повторный тап по кнопке 'Оформить заказ'")
                                            
                                            // Повторный тап по тем же координатам
                                            performTapSimple(webView, webViewX, webViewY, sessionId) {
                                                Log.d("CareBankOrchestrator", "✅ Заказ оформлен (повторный тап)")
                                                
                                                // Отправляем системное событие на бэкенд (без добавления в чат)
                                                Log.d("CareBankOrchestrator", "📤 Отправка системного события: food_flow_completed")
                                                onSendSystemEvent("food_flow_completed")
                                                
                                                // Задержка перед закрытием WebView
                                                webView.postDelayed({
                                                    Log.d("CareBankOrchestrator", "🏁 Закрываем WebView после завершения заказа")
                                                    onComplete()
                                                }, 2000)
                                            }
                                        }, 1000) // 1 секунда на рендеринг после первого тапа
                                    }
                                } else {
                                    Log.w("CareBankOrchestrator", "⚠️ Ошибка парсинга placeOrderCoords")
                                    onJarvisMessage("Накидал тебе корзинку, посмотришь?")
                                }
                            } else {
                                Log.w("CareBankOrchestrator", "⚠️ Некорректный формат placeOrderCoords")
                                onJarvisMessage("Накидал тебе корзинку, посмотришь?")
                            }
                        } else {
                            Log.w("CareBankOrchestrator", "⚠️ placeOrderCoords отсутствует")
                            onJarvisMessage("Накидал тебе корзинку, посмотришь?")
                        }
                    } else {
                        // Ручное подтверждение
                        Log.d("CareBankOrchestrator", "👤 Ручное подтверждение")
                        onJarvisMessage("Накидал тебе корзинку, посмотришь?")
                        // Не вызываем onComplete - оставляем WebView открытым
                    }
                }
            }.onFailure { error ->
                Log.e("CareBankOrchestrator", "❌ Ошибка загрузки настроек: ${error.message}")
                withContext(Dispatchers.Main) {
                    onJarvisMessage("Накидал тебе корзинку, посмотришь?")
                }
            }
        }
    }
}

