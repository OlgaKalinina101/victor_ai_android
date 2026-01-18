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

package com.example.victor_ai.data.repository

import android.content.Context
import android.util.Log
import com.example.victor_ai.data.local.dao.TrackCacheDao
import com.example.victor_ai.data.local.entity.TrackCacheEntity
import com.example.victor_ai.data.network.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 🎵 Репозиторий для управления кешированием музыкальных треков
 * 
 * Функции:
 * - Загрузка треков на устройство
 * - Хранение информации о кешированных треках
 * - Управление размером кеша
 * - Удаление устаревших треков
 */
@Singleton
class TrackCacheRepository @Inject constructor(
    private val trackCacheDao: TrackCacheDao,
    private val context: Context
) {
    
    companion object {
        private const val TAG = "TrackCacheRepository"
        private const val CACHE_DIR_NAME = "music_cache"
        private const val MAX_CACHE_SIZE_MB = 500L // 500 МБ максимальный размер кеша
    }
    
    // 🔥 ИСПРАВЛЕНО: Добавлен Protocol.HTTP_1_1 для совместимости с ngrok
    private val httpClient = OkHttpClient.Builder()
        .protocols(listOf(okhttp3.Protocol.HTTP_1_1))  // 🔥 ТОЛЬКО HTTP/1.1
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun isFileComplete(file: File, expectedBytes: Long?): Boolean {
        if (!file.exists()) return false
        val actual = file.length()
        if (actual <= 0L) return false
        val expected = expectedBytes?.takeIf { it > 0L }
        return expected?.let { actual == it } ?: true
    }

    private suspend fun invalidateCacheEntry(trackId: Int, localPath: String?) {
        try {
            if (!localPath.isNullOrBlank()) {
                File(localPath).delete()
            }
        } catch (_: Exception) {
            // ignore
        }
        try {
            trackCacheDao.deleteCachedTrack(trackId)
        } catch (_: Exception) {
            // ignore
        }
    }
    
    /**
     * Получить все кешированные треки
     */
    fun getAllCachedTracks(): Flow<List<TrackCacheEntity>> {
        return trackCacheDao.getAllCachedTracks()
    }
    
    /**
     * Проверить, закеширован ли трек
     */
    suspend fun isCached(trackId: Int, expectedSizeBytes: Long? = null): Boolean {
        val cached = trackCacheDao.getCachedTrack(trackId) ?: return false
        val file = File(cached.localPath)

        // Если expectedSizeBytes не передан — пробуем использовать fileSize из БД как expected
        val expected = expectedSizeBytes?.takeIf { it > 0L } ?: cached.fileSize.takeIf { it > 0L }

        val ok = isFileComplete(file, expected)
        if (!ok) {
            Log.w(TAG, "⚠️ Кеш поврежден/неполный: trackId=$trackId expected=$expected actual=${file.length()} path=${cached.localPath}")
            invalidateCacheEntry(trackId, cached.localPath)
        }
        return ok
    }
    
    /**
     * Получить путь к кешированному треку
     */
    suspend fun getCachedTrackPath(trackId: Int, expectedSizeBytes: Long? = null): String? {
        val ok = isCached(trackId, expectedSizeBytes)
        if (!ok) return null
        return trackCacheDao.getCachedTrack(trackId)?.localPath
    }
    
    /**
     * Получить информацию о кешированном треке в реальном времени
     */
    fun getCachedTrackFlow(trackId: Int): Flow<TrackCacheEntity?> {
        return trackCacheDao.getCachedTrackFlow(trackId)
    }
    
    /**
     * Загрузить и закешировать трек
     * 
     * @param track Трек для кеширования
     * @param accountId ID пользователя для API запроса
     * @return Result с путем к файлу или ошибкой
     */
    suspend fun cacheTrack(track: Track, accountId: String, baseUrl: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🎵 Начинаем кеширование трека: ${track.title}")
            
            // Проверяем, не закеширован ли уже
            trackCacheDao.getCachedTrack(track.id)?.let { cached ->
                val existingFile = File(cached.localPath)
                val ok = isFileComplete(existingFile, track.fileSize)
                if (ok) {
                    Log.d(TAG, "✅ Трек уже закеширован и целый: ${track.title}")
                    return@withContext Result.success(cached.localPath)
                }

                // Файл отсутствует или обрезан — вычищаем и качаем заново
                Log.w(TAG, "⚠️ Найден неполный кеш, удаляем и перекачиваем: trackId=${track.id} expected=${track.fileSize} actual=${existingFile.length()} path=${cached.localPath}")
                invalidateCacheEntry(track.id, cached.localPath)
            }
            
            // Создаем директорию для кеша
            val cacheDir = File(context.cacheDir, CACHE_DIR_NAME)
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            
            // Проверяем размер кеша
            checkCacheSizeAndCleanup()
            
            // 🔥 Используем streaming API endpoint (как и для воспроизведения)
            val downloadUrl = "${baseUrl.trimEnd('/')}/tracks/stream/${track.id}?account_id=$accountId"
            Log.d(TAG, "📥 Загружаем с: $downloadUrl")
            
            // Создаем файл для сохранения
            val fileName = "${track.id}_${track.title.replace(Regex("[^a-zA-Z0-9]"), "_")}.mp3"
            val destFile = File(cacheDir, fileName)
            val tmpFile = File(cacheDir, "$fileName.download")

            // На всякий случай чистим хвосты прошлых попыток
            if (tmpFile.exists()) tmpFile.delete()
            
            val expectedBytes = track.fileSize.takeIf { it > 0L }

            // 🔥 Качаем с 1 ретраем: если получился обрезанный файл — удаляем и пробуем еще раз
            var lastError: Exception? = null
            repeat(2) { attemptIdx ->
                try {
                    val attempt = attemptIdx + 1
                    Log.d(TAG, "⬇️ Download attempt $attempt/2: trackId=${track.id}")

                    val request = Request.Builder()
                        .url(downloadUrl)
                        // 🔍 Для диагностики на бэкенде: отличаем кеш-скачивание от ExoPlayer
                        .header("User-Agent", "VictorAI-Cache")
                        .header("X-VictorAI-Client", "cache")
                        .build()
                    httpClient.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) {
                            throw IOException("Ошибка загрузки: ${response.code}")
                        }

                        val body = response.body ?: throw IOException("Пустое тело ответа")

                        FileOutputStream(tmpFile).use { output ->
                            body.byteStream().copyTo(output)
                        }
                    }

                    val actualBytes = tmpFile.length()
                    if (!isFileComplete(tmpFile, expectedBytes)) {
                        tmpFile.delete()
                        throw IOException("Неполная загрузка: expected=$expectedBytes bytes, actual=$actualBytes bytes")
                    }

                    // Коммит: заменяем старый файл атомарно насколько возможно
                    if (destFile.exists()) destFile.delete()
                    val renamed = tmpFile.renameTo(destFile)
                    if (!renamed) {
                        // fallback copy
                        FileOutputStream(destFile).use { out ->
                            tmpFile.inputStream().use { it.copyTo(out) }
                        }
                        tmpFile.delete()
                    }

                    lastError = null
                    return@repeat
                } catch (e: Exception) {
                    lastError = e
                    Log.w(TAG, "⚠️ Download attempt failed (trackId=${track.id}): ${e.message}")
                    try { tmpFile.delete() } catch (_: Exception) {}
                }
            }

            if (lastError != null) throw lastError as Exception
            
            // Сохраняем информацию в БД
            val cacheEntity = TrackCacheEntity(
                trackId = track.id,
                localPath = destFile.absolutePath,
                // 🔥 Храним ожидаемый размер с бэкенда, чтобы потом валидировать целостность
                fileSize = track.fileSize,
                title = track.title,
                artist = track.artist
            )
            
            trackCacheDao.insertCachedTrack(cacheEntity)
            
            Log.d(TAG, "✅ Трек успешно закеширован: ${track.title} (${destFile.length() / 1024} KB)")
            Result.success(destFile.absolutePath)
            
        } catch (e: Exception) {
            // Если упали — стараемся удалить потенциальный мусор
            try { invalidateCacheEntry(track.id, trackCacheDao.getCachedTrack(track.id)?.localPath) } catch (_: Exception) {}
            Log.e(TAG, "❌ Ошибка кеширования трека ${track.title}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Удалить трек из кеша
     */
    suspend fun removeCachedTrack(trackId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val cached = trackCacheDao.getCachedTrack(trackId)
            if (cached != null) {
                // Удаляем файл
                File(cached.localPath).delete()
                
                // Удаляем запись из БД
                trackCacheDao.deleteCachedTrack(trackId)
                
                Log.d(TAG, "🗑️ Трек удален из кеша: ${cached.title}")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка удаления трека из кеша", e)
            Result.failure(e)
        }
    }
    
    /**
     * Очистить весь кеш
     */
    suspend fun clearCache(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Удаляем все файлы
            val cacheDir = File(context.cacheDir, CACHE_DIR_NAME)
            if (cacheDir.exists()) {
                cacheDir.listFiles()?.forEach { it.delete() }
            }
            
            // Очищаем БД
            trackCacheDao.clearAll()
            
            Log.d(TAG, "🗑️ Кеш полностью очищен")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка очистки кеша", e)
            Result.failure(e)
        }
    }
    
    /**
     * Получить статистику кеша
     */
    suspend fun getCacheStats(): CacheStats = withContext(Dispatchers.IO) {
        val count = trackCacheDao.getCachedCount()
        val totalSize = trackCacheDao.getTotalCacheSize() ?: 0L
        CacheStats(count, totalSize)
    }
    
    /**
     * Проверить размер кеша и удалить старые треки при необходимости
     */
    private suspend fun checkCacheSizeAndCleanup() {
        val totalSize = trackCacheDao.getTotalCacheSize() ?: 0L
        val maxSizeBytes = MAX_CACHE_SIZE_MB * 1024 * 1024
        
        if (totalSize > maxSizeBytes) {
            Log.d(TAG, "⚠️ Превышен лимит кеша (${totalSize / 1024 / 1024} MB), очищаем старые треки...")
            
            // Получаем все треки, сортируем по дате (старые первые)
            val allTracks = trackCacheDao.getAllCachedTracks()
            
            // TODO: Реализовать удаление старых треков
            // Пока просто логируем
            Log.d(TAG, "TODO: Реализовать автоматическую очистку старых треков")
        }
    }
}

/**
 * Статистика кеша
 */
data class CacheStats(
    val tracksCount: Int,
    val totalSizeBytes: Long
) {
    val totalSizeMB: Float
        get() = totalSizeBytes / 1024f / 1024f
}

