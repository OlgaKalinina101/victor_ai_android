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

package com.example.victor_ai.ui.components.carebank.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.webkit.WebView
import com.example.victor_ai.auth.UserProvider
import com.example.victor_ai.data.network.CareBankApi
import com.example.victor_ai.data.network.dto.ScreenshotAnalysisResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

/**
 * Делает скриншот WebView и отправляет на анализ для выбора лучшего варианта
 * @param webView WebView для скриншота
 * @param context Android Context
 * @param query Опциональный поисковый запрос (например, "блинчики")
 * @param careBankApi API для отправки скриншота
 * @param onAnalysisComplete Callback с результатом: (ScreenshotAnalysisResponse?) -> Unit
 */
fun captureScreenshotAndAnalyze(
    webView: WebView,
    context: Context,
    query: String? = null,
    careBankApi: CareBankApi,
    onAnalysisComplete: (ScreenshotAnalysisResponse?) -> Unit
) {
    try {
        Log.d("WebViewScreenshot", "📸 Начинаем создание скриншота...")
        
        // 1. Делаем скриншот ТОЛЬКО видимой части WebView
        val bitmap = Bitmap.createBitmap(
            webView.width,
            webView.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        webView.draw(canvas)
        
        Log.d("WebViewScreenshot", "✅ Скриншот создан: ${webView.width}x${webView.height}")
        
        // 2. Сжимаем в WebP (лучший выбор для Android)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 90, stream)
        val byteArray = stream.toByteArray()
        bitmap.recycle()
        
        Log.d("WebViewScreenshot", "✅ Изображение сжато: ${byteArray.size} байт")
        
        // 3. Отправляем на бэкенд
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accountId = UserProvider.getCurrentUserId()
                Log.d("WebViewScreenshot", "🌐 Отправляем на сервер для account_id=$accountId, query=$query")
                
                // Создаем RequestBody для account_id
                val accountIdBody = accountId.toRequestBody("text/plain".toMediaTypeOrNull())
                
                // Создаем RequestBody для query (если есть)
                val queryBody = query?.toRequestBody("text/plain".toMediaTypeOrNull())
                
                // Создаем MultipartBody.Part для скриншота
                val screenshotRequestBody = byteArray.toRequestBody("image/webp".toMediaTypeOrNull())
                val screenshotPart = MultipartBody.Part.createFormData(
                    "screenshot",
                    "screenshot_${System.currentTimeMillis()}.webp",
                    screenshotRequestBody
                )
                
                // Отправляем запрос
                val response = careBankApi.processScreenshot(
                    accountId = accountIdBody,
                    screenshot = screenshotPart,
                    query = queryBody
                )
                
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        Log.d("WebViewScreenshot", "✅ Получен ответ: id='${result.id}', selectedItem='${result.selectedItem}', matchType='${result.matchType}', userMessage='${result.userMessage}'")
                        
                        withContext(Dispatchers.Main) {
                            onAnalysisComplete(result)
                        }
                    } else {
                        Log.e("WebViewScreenshot", "❌ Пустой ответ от сервера")
                        withContext(Dispatchers.Main) {
                            onAnalysisComplete(null)
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("WebViewScreenshot", "❌ Ошибка сервера: ${response.code()}, $errorBody")
                    withContext(Dispatchers.Main) {
                        onAnalysisComplete(null)
                    }
                }
                
            } catch (e: Exception) {
                Log.e("WebViewScreenshot", "❌ Ошибка при отправке скриншота", e)
                withContext(Dispatchers.Main) {
                    onAnalysisComplete(null)
                }
            }
        }
        
    } catch (e: Exception) {
        Log.e("WebViewScreenshot", "❌ Ошибка при создании скриншота", e)
        onAnalysisComplete(null)
    }
}

