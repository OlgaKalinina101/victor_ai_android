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
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView

/**
 * Базовые действия для автоматизации работы с WebView
 * Каждая функция выполняет одно конкретное действие и вызывает колбэк по завершению
 * Эти функции переиспользуемые и могут использоваться не только для Care Bank
 */

/**
 * Действие: Тап по координатам с визуализацией
 * @param webView WebView для выполнения тапа
 * @param context Context для получения метрик экрана
 * @param xPx X координата в пикселях (null = центр экрана)
 * @param yPx Y координата в пикселях
 * @param onComplete Колбэк по завершению
 */
fun performTap(
    webView: WebView,
    context: Context,
    xPx: Int? = null,
    yPx: Int,
    sessionId: Long,
    onComplete: () -> Unit
) {
    val density = context.resources.displayMetrics.density
    val xInPx = xPx ?: (context.resources.displayMetrics.widthPixels / 2)
    val yInPx = yPx
    
    // Получаем scale и размеры WebView
    val scale = webView.scale
    val webViewWidth = webView.width
    val webViewHeight = webView.height
    
    Log.d("WebViewAutomation", "")
    Log.d("WebViewAutomation", "🖱️ === PERFORM TAP [Session $sessionId] ===")
    Log.d("WebViewAutomation", "  📥 Входные координаты (относительно WebView): PX($xInPx, $yInPx)")
    Log.d("WebViewAutomation", "  📊 WebView info:")
    Log.d("WebViewAutomation", "     size: ${webViewWidth}x${webViewHeight}px")
    Log.d("WebViewAutomation", "     density: $density")
    Log.d("WebViewAutomation", "     scale: $scale")
    
    // Конвертируем в CSS пиксели для визуализации
    val xInCssPx = xInPx / (scale * density)
    val yInCssPx = yInPx / (scale * density)
    
    Log.d("WebViewAutomation", "  🔄 Конвертация в CSS для визуализации:")
    Log.d("WebViewAutomation", "     xInCssPx = $xInPx / ($scale * $density) = $xInCssPx")
    Log.d("WebViewAutomation", "     yInCssPx = $yInPx / ($scale * $density) = $yInCssPx")
    Log.d("WebViewAutomation", "  ✅ Отправляем Android тап в: PX($xInPx, $yInPx)")
    
    // Визуализируем место тапа красным кружочком через JavaScript
    webView.evaluateJavascript("""
        (function() {
            // Удаляем предыдущие маркеры
            document.querySelectorAll('.tap-marker').forEach(el => el.remove());
            
            // Создаем красный кружочек
            const marker = document.createElement('div');
            marker.className = 'tap-marker';
            marker.style.position = 'fixed';
            marker.style.left = ($xInCssPx - 25) + 'px';
            marker.style.top = ($yInCssPx - 25) + 'px';
            marker.style.width = '50px';
            marker.style.height = '50px';
            marker.style.borderRadius = '50%';
            marker.style.backgroundColor = 'rgba(255, 0, 0, 0.5)';
            marker.style.border = '3px solid red';
            marker.style.zIndex = '999999';
            marker.style.pointerEvents = 'none';
            document.body.appendChild(marker);
            
            console.log('🔴 Визуальный маркер тапа: (' + $xInCssPx.toFixed(1) + ', ' + $yInCssPx.toFixed(1) + ')');
            
            // Удаляем через 2 секунды
            setTimeout(() => marker.remove(), 2000);
            
            return 'marker added';
        })();
    """.trimIndent(), null)
    
    // Выполняем РЕАЛЬНЫЙ тап через Android API
    val downTime = SystemClock.uptimeMillis()
    val eventTime = SystemClock.uptimeMillis()
    
    // Тапаем в пикселях (не делим на density/scale - WebView сам конвертирует)
    val downEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xInPx.toFloat(), yInPx.toFloat(), 0)
    val upEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xInPx.toFloat(), yInPx.toFloat(), 0)
    
    webView.dispatchTouchEvent(downEvent)
    webView.dispatchTouchEvent(upEvent)
    
    downEvent.recycle()
    upEvent.recycle()
    
    Log.d("WebViewAutomation", "✅ [Session $sessionId] Android тап выполнен")
    
    // Проверяем через JavaScript, что произошло
    webView.postDelayed({
        webView.evaluateJavascript("""
        (function() {
            const x = $xInCssPx;
            const y = $yInCssPx;
            
            console.log('🔍 Диагностика Android тапа в точку: CSS(' + x.toFixed(1) + ', ' + y.toFixed(1) + ')');
            
            // Проверяем, какой элемент находится в этой точке
            const element = document.elementFromPoint(x, y);
            
            if (element) {
                const rect = element.getBoundingClientRect();
                let className = '';
                try {
                    className = element.className.toString ? element.className.toString() : String(element.className);
                    if (className.length > 60) className = className.substring(0, 60) + '...';
                } catch(e) {
                    className = '[no class]';
                }
                const text = (element.textContent || '').trim().substring(0, 40);
                console.log('🎯 Элемент под тапом: ' + element.tagName);
                console.log('   class: ' + className);
                console.log('   bounds: x=' + rect.x.toFixed(0) + ', y=' + rect.y.toFixed(0) + 
                            ', w=' + rect.width.toFixed(0) + ', h=' + rect.height.toFixed(0));
                console.log('   text: "' + text + '"');
                
                return 'element: ' + element.tagName;
            } else {
                console.log('❌ Элемент не найден в точке');
                return 'no element';
            }
        })();
    """.trimIndent()) { result ->
        Log.d("WebViewAutomation", "📊 [Session $sessionId] Диагностика: $result")
        onComplete()
    }
    }, 100)
}

/**
 * Упрощенный тап по WebView
 * Принимает координаты ОТНОСИТЕЛЬНО WebView (уже преобразованные из screen)
 * Без лишних преобразований - просто отправляет MotionEvent
 * 
 * @param webView WebView для выполнения тапа
 * @param x X координата относительно WebView
 * @param y Y координата относительно WebView
 * @param sessionId ID сессии для логирования
 * @param onComplete Колбэк по завершению
 */
fun performTapSimple(
    webView: WebView,
    x: Int,
    y: Int,
    sessionId: Long,
    onComplete: () -> Unit
) {
    Log.d("WebViewAutomation", "🖱️ [Session $sessionId] performTapSimple: ($x, $y)")
    
    // Получаем актуальную позицию WebView на экране для логирования
    val location = IntArray(2)
    webView.getLocationOnScreen(location)
    val density = webView.context.resources.displayMetrics.density
    val scale = webView.scale
    
    Log.d("WebViewAutomation", "📍 WebView on screen: (${location[0]}, ${location[1]})")
    Log.d("WebViewAutomation", "📍 WebView size: ${webView.width}x${webView.height}")
    Log.d("WebViewAutomation", "📍 density: $density, scale: $scale")
    
    // CSS координаты = View координаты / density (без scale!)
    // scale влияет на масштаб контента, но не на систему координат CSS
    val cssX = x / density
    val cssY = y / density
    
    Log.d("WebViewAutomation", "📍 ViewPx($x, $y) -> CSS($cssX, $cssY)")
    
    webView.evaluateJavascript("""
        (function() {
            // Удаляем предыдущие маркеры
            document.querySelectorAll('.tap-marker').forEach(el => el.remove());
            
            const cssX = $cssX;
            const cssY = $cssY;
            
            // Проверяем какой элемент находится в этой точке
            const elementAtPoint = document.elementFromPoint(cssX, cssY);
            console.log('🎯 Element at (' + cssX.toFixed(0) + ',' + cssY.toFixed(0) + '):', 
                elementAtPoint ? elementAtPoint.tagName + ' ' + (elementAtPoint.className || '') : 'null');
            
            // Создаем красный кружочек
            const marker = document.createElement('div');
            marker.className = 'tap-marker';
            marker.style.cssText = 'position:fixed; left:' + (cssX - 25) + 'px; top:' + (cssY - 25) + 'px; ' +
                'width:50px; height:50px; border-radius:50%; background:rgba(255,0,0,0.6); ' +
                'border:3px solid red; z-index:999999; pointer-events:none;';
            document.body.appendChild(marker);
            
            // Удаляем через 3 секунды
            setTimeout(() => marker.remove(), 3000);
            
            return 'CSS(' + cssX.toFixed(0) + ',' + cssY.toFixed(0) + ') elem:' + 
                (elementAtPoint ? elementAtPoint.tagName : 'null');
        })();
    """.trimIndent()) { result ->
        Log.d("WebViewAutomation", "🔴 Визуализация: $result")
    }
    
    // Отправляем MotionEvent
    val downTime = SystemClock.uptimeMillis()
    val eventTime = SystemClock.uptimeMillis()
    
    val downEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x.toFloat(), y.toFloat(), 0)
    val upEvent = MotionEvent.obtain(downTime, eventTime + 50, MotionEvent.ACTION_UP, x.toFloat(), y.toFloat(), 0)
    
    webView.dispatchTouchEvent(downEvent)
    webView.dispatchTouchEvent(upEvent)
    
    downEvent.recycle()
    upEvent.recycle()
    
    Log.d("WebViewAutomation", "✅ [Session $sessionId] Тап отправлен в WebView($x, $y)")
    
    // Небольшая задержка перед колбэком
    webView.postDelayed({
        onComplete()
    }, 100)
}

/**
 * Действие: Поиск и фокус на input поле
 * @param webView WebView для выполнения JavaScript
 * @param sessionId ID сессии для логирования
 * @param onSuccess Колбэк при успешном нахождении input
 * @param onError Колбэк при ошибке
 */
fun focusSearchInput(
    webView: WebView,
    sessionId: Long,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    Log.d("WebViewAutomation", "✍️ [Session $sessionId] Ищем input и устанавливаем фокус...")
    
    webView.evaluateJavascript("""
        (function() {
            // Ищем input разными способами
            const input = document.querySelector('input[type="search"]') ||
                          document.querySelector('input[type="text"]') ||
                          document.querySelector('input[placeholder*="Поиск"]') ||
                          document.querySelector('input[placeholder*="поиск"]') ||
                          document.querySelector('input') ||
                          document.querySelector('textarea');
            
            if (input) {
                console.log('✅ Нашли input:', input.tagName, input.type);
                input.value = ''; // Очищаем
                input.focus(); // Устанавливаем фокус
                console.log('✅ Фокус установлен на input');
                return 'success';
            } else {
                console.log('❌ Input не найден');
                return 'error';
            }
        })();
    """.trimIndent()) { result ->
        Log.d("WebViewAutomation", "🔎 [Session $sessionId] Результат поиска input: $result")
        
        if (result == "\"success\"") {
            onSuccess()
        } else {
            Log.e("WebViewAutomation", "❌ [Session $sessionId] Не удалось найти input")
            onError()
        }
    }
}

/**
 * Действие: Ввод текста посимвольно
 * @param webView WebView для выполнения JavaScript
 * @param text Текст для ввода
 * @param delayMs Задержка между символами в миллисекундах
 * @param sessionId ID сессии для логирования
 * @param onComplete Колбэк по завершению ввода всего текста
 */
fun typeText(
    webView: WebView,
    text: String,
    delayMs: Long = 120,
    sessionId: Long,
    onComplete: () -> Unit
) {
    Log.d("WebViewAutomation", "✍️ [Session $sessionId] Начинаем вводить текст '$text' посимвольно...")
    
    // Рекурсивная функция для последовательного ввода символов
    fun typeCharacter(charIndex: Int) {
        if (charIndex >= text.length) {
            // Все символы введены
            Log.d("WebViewAutomation", "✅ [Session $sessionId] Все символы введены: $text")
            onComplete()
            return
        }
        
        // Вводим текущий символ
        val char = text[charIndex].toString()
        Log.d("WebViewAutomation", "⌨️ [Session $sessionId] Вводим символ ${charIndex + 1}/${text.length}: '$char'")
        
        // Вставляем символ через JavaScript (самый надежный способ для WebView)
        webView.evaluateJavascript("""
            (function() {
                // Ищем input (активный или первый найденный)
                let input = document.activeElement;
                
                // Если activeElement не input, ищем вручную
                if (!input || (input.tagName !== 'INPUT' && input.tagName !== 'TEXTAREA')) {
                    input = document.querySelector('input[type="search"]') ||
                            document.querySelector('input[type="text"]') ||
                            document.querySelector('input[placeholder*="Поиск"]') ||
                            document.querySelector('input[placeholder*="поиск"]') ||
                            document.querySelector('input') ||
                            document.querySelector('textarea');
                }
                
                if (input && (input.tagName === 'INPUT' || input.tagName === 'TEXTAREA')) {
                    const char = '$char';
                    
                    // Убеждаемся что фокус на элементе
                    input.focus();
                    
                    // Получаем текущее значение
                    const currentValue = input.value || '';
                    
                    // Добавляем символ
                    const newValue = currentValue + char;
                    
                    // Устанавливаем через native setter для React
                    const nativeInputValueSetter = Object.getOwnPropertyDescriptor(
                        window.HTMLInputElement.prototype,
                        'value'
                    ).set;
                    nativeInputValueSetter.call(input, newValue);
                    
                    // Отправляем ТОЛЬКО InputEvent (не два события!)
                    input.dispatchEvent(new InputEvent('input', { 
                        bubbles: true, 
                        cancelable: true,
                        data: char,
                        inputType: 'insertText'
                    }));
                    
                    return newValue;
                }
                return 'error: input not found';
            })();
        """.trimIndent()) { result ->
            Log.d("WebViewAutomation", "Символ добавлен, текущее значение: $result")
            
            // Следующий символ через delayMs
            webView.postDelayed({
                typeCharacter(charIndex + 1)
            }, delayMs)
        }
    }
    
    // Начинаем ввод с первого символа
    typeCharacter(0)
}

/**
 * Действие: Нажатие Enter
 * @param webView WebView для отправки KeyEvent
 * @param sessionId ID сессии для логирования
 * @param onComplete Колбэк по завершению
 */
fun pressEnter(
    webView: WebView,
    sessionId: Long,
    onComplete: () -> Unit
) {
    Log.d("WebViewAutomation", "⌨️ [Session $sessionId] Отправляем РЕАЛЬНОЕ нажатие Enter...")
    
    try {
        // ╔══════════════════════════════════════════════════════════
        // ║               ОТПРАВКА ENTER
        // ╚══════════════════════════════════════════════════════════
        val eventDown = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
        val eventUp = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER)
        
        val resultDown = webView.dispatchKeyEvent(eventDown)
        Log.d("WebViewAutomation", "⬇️ [Session $sessionId] KeyEvent DOWN отправлен, результат: $resultDown")
        
        val resultUp = webView.dispatchKeyEvent(eventUp)
        Log.d("WebViewAutomation", "⬆️ [Session $sessionId] KeyEvent UP отправлен, результат: $resultUp")
        
        // Проверяем результат через JavaScript
        webView.evaluateJavascript("""
            (function() {
                console.log('🔍 Проверяем результат после Enter...');
                const input = document.querySelector('input[type="search"]') ||
                              document.querySelector('input[type="text"]') ||
                              document.querySelector('input');
                if (input) {
                    console.log('Input value:', input.value);
                    console.log('Current URL:', window.location.href);
                }
                return 'checked';
            })();
        """.trimIndent()) { result ->
            Log.d("WebViewAutomation", "✅ [Session $sessionId] Проверка завершена: $result")
            onComplete()
        }
        
    } catch (e: Exception) {
        Log.e("WebViewAutomation", "❌ [Session $sessionId] Ошибка при отправке KeyEvent: ${e.message}", e)
        onComplete() // Продолжаем даже при ошибке
    }
}

/**
 * Действие: Закрыть клавиатуру
 * @param webView WebView для получения windowToken
 * @param context Context для InputMethodManager
 * @param sessionId ID сессии для логирования
 * @param onComplete Колбэк по завершению
 */
fun hideKeyboard(
    webView: WebView,
    context: Context,
    sessionId: Long,
    onComplete: () -> Unit
) {
    Log.d("WebViewAutomation", "⌨️ [Session $sessionId] Закрываем клавиатуру...")
    
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(webView.windowToken, 0)
    Log.d("WebViewAutomation", "✅ [Session $sessionId] Клавиатура закрыта")
    
    // Ждём закрытия клавиатуры
    webView.postDelayed({
        val isKeyboardClosed = !imm.isAcceptingText
        Log.d("WebViewAutomation", "⌨️ [Session $sessionId] Проверка клавиатуры: закрыта=$isKeyboardClosed")
        
        if (isKeyboardClosed) {
            onComplete()
        } else {
            Log.w("WebViewAutomation", "⌨️ [Session $sessionId] Клавиатура ещё не закрыта, ждём ещё немного...")
            // Если клавиатура не закрыта, ждём ещё 200мс
            webView.postDelayed({
                onComplete()
            }, 200)
        }
    }, 300)
}

/**
 * Перемещает курсор в конец текста в поле ввода
 * Отправляет KeyEvent.KEYCODE_MOVE_END
 * @param webView WebView для выполнения
 * @param sessionId ID сессии для логирования
 * @param onComplete Callback по завершению
 */
fun moveCursorToEnd(
    webView: WebView,
    sessionId: Long,
    onComplete: () -> Unit
) {
    Log.d("WebViewAutomation", "➡️ [Session $sessionId] Перемещаем курсор в конец текста")
    
    // Отправляем MOVE_END event
    val moveEndDown = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MOVE_END)
    val moveEndUp = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MOVE_END)
    
    webView.dispatchKeyEvent(moveEndDown)
    webView.dispatchKeyEvent(moveEndUp)
    
    Log.d("WebViewAutomation", "✅ [Session $sessionId] Курсор перемещён в конец")
    
    // Ждём применения
    webView.postDelayed({
        onComplete()
    }, 100)
}

/**
 * Очистка поля ввода через Android API
 * Отправляет нужное количество Backspace событий для удаления текста
 * @param webView WebView для выполнения
 * @param textLength Длина текста, который нужно удалить
 * @param sessionId ID сессии для логирования
 * @param onComplete Callback по завершению
 */
fun clearInputField(
    webView: WebView,
    textLength: Int,
    sessionId: Long,
    onComplete: () -> Unit
) {
    Log.d("WebViewAutomation", "🧹 [Session $sessionId] Очистка поля ввода (удаляем $textLength символов)")
    
    if (textLength <= 0) {
        Log.d("WebViewAutomation", "⚠️ [Session $sessionId] Нечего удалять (length=$textLength)")
        onComplete()
        return
    }
    
    // Рекурсивная функция для отправки Backspace событий
    fun sendBackspaces(remaining: Int) {
        if (remaining <= 0) {
            Log.d("WebViewAutomation", "✅ [Session $sessionId] Поле очищено ($textLength символов удалено)")
            webView.postDelayed({
                onComplete()
            }, 100)
            return
        }
        
        // Отправляем Backspace
        val deleteDownEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)
        val deleteUpEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL)
        
        webView.dispatchKeyEvent(deleteDownEvent)
        webView.dispatchKeyEvent(deleteUpEvent)
        
        // Ждём немного перед следующим Backspace (для стабильности)
        webView.postDelayed({
            sendBackspaces(remaining - 1)
        }, 30) // 30ms между символами
    }
    
    // Начинаем удаление
    sendBackspaces(textLength)
}

