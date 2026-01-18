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

package com.example.victor_ai.data.network

import android.util.Log
import com.example.victor_ai.data.network.dto.MemoryResponse

private const val MEMORIES_PAGING_TAG = "MemoriesPaging"

/**
 * Пытается загрузить воспоминания страницами (если бэкенд поддерживает limit/offset).
 * Если limit/offset не поддерживаются — fallback на полный запрос.
 */
suspend fun MemoriesApi.getMemoriesPaged(
    accountId: String,
    pageSize: Int = 50,
    maxPages: Int = 200
): List<MemoryResponse> {
    return try {
        val firstPage = getMemories(accountId = accountId, limit = pageSize, offset = 0)
        if (firstPage.size > pageSize) {
            Log.w(
                MEMORIES_PAGING_TAG,
                "limit ignored (size=${firstPage.size} > $pageSize). Fallback to full list."
            )
            return firstPage
        }

        val all = firstPage.toMutableList()
        val seenIds = firstPage.associateBy { it.id }.toMutableMap()
        var offset = firstPage.size
        var page = 2

        while (page <= maxPages) {
            val pageItems = getMemories(accountId = accountId, limit = pageSize, offset = offset)
            if (pageItems.isEmpty()) break

            val newItems = pageItems.filter { seenIds.putIfAbsent(it.id, it) == null }
            if (newItems.isEmpty()) {
                Log.w(
                    MEMORIES_PAGING_TAG,
                    "offset ignored or no new items at page=$page (size=${pageItems.size})."
                )
                break
            }

            all.addAll(newItems)
            offset += pageItems.size
            page += 1
        }

        Log.d(MEMORIES_PAGING_TAG, "Loaded memories pages=$page total=${all.size}")
        all
    } catch (e: Exception) {
        Log.w(MEMORIES_PAGING_TAG, "Paged load failed, fallback to full: ${e.message}")
        getMemories(accountId = accountId)
    }
}
