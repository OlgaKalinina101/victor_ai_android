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

package com.example.victor_ai.util

import android.util.Log
import com.example.victor_ai.BuildConfig

/**
 * 🔒 Безопасное логирование для production
 * 
 * В release сборке все логи будут удалены ProGuard'ом
 * В debug сборке логи работают как обычно
 */
object SafeLog {
    
    private const val ENABLED = BuildConfig.ENABLE_LOGGING
    
    fun d(tag: String, message: String) {
        if (ENABLED) {
            Log.d(tag, message)
        }
    }
    
    fun i(tag: String, message: String) {
        if (ENABLED) {
            Log.i(tag, message)
        }
    }
    
    fun w(tag: String, message: String) {
        if (ENABLED) {
            Log.w(tag, message)
        }
    }
    
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (ENABLED) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }
    
    fun v(tag: String, message: String) {
        if (ENABLED) {
            Log.v(tag, message)
        }
    }
    
    /**
     * 🔥 Редактирует чувствительные данные в строке
     */
    fun redactSensitive(message: String): String {
        return message
            .replace(Regex("demo_key[\"']?\\s*[:=]\\s*[\"']?([^\"',\\s}]+)"), "demo_key=***")
            .replace(Regex("access_token[\"']?\\s*[:=]\\s*[\"']?([^\"',\\s}]+)"), "access_token=***")
            .replace(Regex("token[\"']?\\s*[:=]\\s*[\"']?([^\"',\\s}]+)"), "token=***")
            .replace(Regex("password[\"']?\\s*[:=]\\s*[\"']?([^\"',\\s}]+)"), "password=***")
            .replace(Regex("Authorization:\\s*Bearer\\s+[^\\s]+"), "Authorization: Bearer ***")
    }
}

