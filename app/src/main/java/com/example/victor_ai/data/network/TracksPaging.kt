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

private const val TRACKS_PAGING_TAG = "TracksPaging"

/**
 * Пытается загрузить треки страницами (если бэкенд поддерживает limit/offset).
 * Если сервер игнорирует limit/offset или возвращает ошибку — fallback на один полный запрос.
 */
suspend fun MusicApi.getTracksPaged(
    accountId: String,
    pageSize: Int = 50,
    maxPages: Int = 100
): List<Track> {
    return try {
        val firstPage = getTracks(accountId = accountId, limit = pageSize, offset = 0)
        if (firstPage.size > pageSize) {
            Log.w(
                TRACKS_PAGING_TAG,
                "limit ignored (size=${firstPage.size} > $pageSize). Fallback to full list."
            )
            return firstPage
        }

        val all = firstPage.toMutableList()
        val seen = firstPage.associateBy { it.id }.toMutableMap()
        var offset = firstPage.size
        var page = 2

        while (page <= maxPages) {
            val pageTracks = getTracks(accountId = accountId, limit = pageSize, offset = offset)
            if (pageTracks.isEmpty()) break

            val newItems = pageTracks.filter { seen.putIfAbsent(it.id, it) == null }
            if (newItems.isEmpty()) {
                Log.w(
                    TRACKS_PAGING_TAG,
                    "offset ignored or no new items at page=$page (size=${pageTracks.size})."
                )
                break
            }

            all.addAll(newItems)
            offset += pageTracks.size
            page += 1
        }

        Log.d(TRACKS_PAGING_TAG, "Loaded tracks pages=$page total=${all.size}")
        all
    } catch (e: Exception) {
        Log.w(TRACKS_PAGING_TAG, "Paged load failed, fallback to full: ${e.message}")
        getTracks(accountId = accountId)
    }
}

/**
 * Extension для MusicApiImpl - делегирует к базовому MusicApi методу
 */
suspend fun MusicApiImpl.getTracksPaged(
    accountId: String,
    pageSize: Int = 50,
    maxPages: Int = 100
): List<Track> {
    return try {
        val firstPage = getTracks(accountId = accountId, limit = pageSize, offset = 0)
        if (firstPage.size > pageSize) {
            Log.w(
                TRACKS_PAGING_TAG,
                "limit ignored (size=${firstPage.size} > $pageSize). Fallback to full list."
            )
            return firstPage
        }

        val all = firstPage.toMutableList()
        val seen = firstPage.associateBy { it.id }.toMutableMap()
        var offset = firstPage.size
        var page = 2

        while (page <= maxPages) {
            val pageTracks = getTracks(accountId = accountId, limit = pageSize, offset = offset)
            if (pageTracks.isEmpty()) break

            val newItems = pageTracks.filter { seen.putIfAbsent(it.id, it) == null }
            if (newItems.isEmpty()) {
                Log.w(
                    TRACKS_PAGING_TAG,
                    "offset ignored or no new items at page=$page (size=${pageTracks.size})."
                )
                break
            }

            all.addAll(newItems)
            offset += pageTracks.size
            page += 1
        }

        Log.d(TRACKS_PAGING_TAG, "Loaded tracks pages=$page total=${all.size}")
        all
    } catch (e: Exception) {
        Log.w(TRACKS_PAGING_TAG, "Paged load failed, fallback to full: ${e.message}")
        getTracks(accountId = accountId)
    }
}