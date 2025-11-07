package com.example.victor_ai.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.network.dto.Achievement
import com.example.victor_ai.data.network.dto.JournalEntry
import com.example.victor_ai.data.network.dto.StatsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * 📊 Repository для управления статистикой пользователя
 *
 * Хранит данные локально в SharedPreferences и синхронизирует с API:
 * - Статистика (расстояние, шаги, стрик)
 * - Достижения
 * - Последние записи из дневника
 */
class StatsRepository(
    private val context: Context,
    private val placesApi: PlacesApi
) {

    companion object {
        private const val TAG = "StatsRepository"
        private const val PREFS_NAME = "user_stats"
        private const val KEY_TODAY_DISTANCE = "today_distance"
        private const val KEY_TODAY_STEPS = "today_steps"
        private const val KEY_WEEKLY_CHART = "weekly_chart"
        private const val KEY_STREAK = "streak"
        private const val KEY_ACHIEVEMENTS = "achievements"
        private const val KEY_JOURNAL_ENTRIES = "journal_entries"
        private const val KEY_LAST_UPDATE = "last_update"
        private const val ACCOUNT_ID = "test_user" // TODO: Получать из настроек

        // 🔥 TEMPORARY: Mock данные для тестирования пока бэкенд не возвращает реальные данные
        private const val USE_MOCK_DATA = false  // Убрали моки - ищем реальную проблему!
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Локальная модель статистики
     */
    data class LocalStats(
        val todayDistance: Float = 0f,
        val todaySteps: Int = 0,
        val weeklyChart: List<Float> = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f),
        val streak: Int = 0,
        val achievements: List<String> = emptyList(),
        val lastUpdate: Long = 0L
    )

    /**
     * Получает статистику из локального хранилища
     */
    fun getLocalStats(): LocalStats {
        return LocalStats(
            todayDistance = prefs.getFloat(KEY_TODAY_DISTANCE, 0f),
            todaySteps = prefs.getInt(KEY_TODAY_STEPS, 0),
            weeklyChart = getWeeklyChart(),
            streak = prefs.getInt(KEY_STREAK, 0),
            achievements = getAchievements(),
            lastUpdate = prefs.getLong(KEY_LAST_UPDATE, 0L)
        )
    }

    /**
     * Сохраняет статистику локально
     */
    fun saveStats(stats: StatsResponse) {
        prefs.edit().apply {
            putFloat(KEY_TODAY_DISTANCE, stats.today_distance)
            putInt(KEY_TODAY_STEPS, stats.today_steps)
            putInt(KEY_STREAK, stats.streak)
            putLong(KEY_LAST_UPDATE, System.currentTimeMillis())
            apply()
        }

        // Сохраняем weekly chart
        saveWeeklyChart(stats.weekly_chart)

        // Сохраняем достижения
        saveAchievements(stats.achievements)

        Log.d(TAG, "✅ Статистика сохранена локально: ${stats.today_distance}м, ${stats.today_steps} шагов")
    }

    /**
     * Обновляет сегодняшнюю дистанцию (добавляет к текущей)
     */
    fun addTodayDistance(meters: Float) {
        val current = prefs.getFloat(KEY_TODAY_DISTANCE, 0f)
        prefs.edit().putFloat(KEY_TODAY_DISTANCE, current + meters).apply()
    }

    /**
     * Обновляет сегодняшние шаги (добавляет к текущим)
     */
    fun addTodaySteps(steps: Int) {
        val current = prefs.getInt(KEY_TODAY_STEPS, 0)
        prefs.edit().putInt(KEY_TODAY_STEPS, current + steps).apply()
    }

    /**
     * Получает достижения
     */
    fun getAchievements(): List<String> {
        val json = prefs.getString(KEY_ACHIEVEMENTS, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    /**
     * Сохраняет достижения
     */
    private fun saveAchievements(achievements: List<String>) {
        val json = gson.toJson(achievements)
        prefs.edit().putString(KEY_ACHIEVEMENTS, json).apply()
    }

    /**
     * Получает график за неделю
     */
    private fun getWeeklyChart(): List<Float> {
        val json = prefs.getString(KEY_WEEKLY_CHART, null) ?: return List(7) { 0f }
        val type = object : TypeToken<List<Float>>() {}.type
        return gson.fromJson(json, type) ?: List(7) { 0f }
    }

    /**
     * Сохраняет график за неделю
     */
    private fun saveWeeklyChart(chart: List<Float>) {
        val json = gson.toJson(chart)
        prefs.edit().putString(KEY_WEEKLY_CHART, json).apply()
    }

    /**
     * Получает последние записи из дневника (локально)
     */
    fun getLocalJournalEntries(): List<JournalEntry> {
        val json = prefs.getString(KEY_JOURNAL_ENTRIES, null) ?: return emptyList()
        val type = object : TypeToken<List<JournalEntry>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    /**
     * Сохраняет записи дневника локально
     */
    private fun saveJournalEntries(entries: List<JournalEntry>) {
        val json = gson.toJson(entries)
        prefs.edit().putString(KEY_JOURNAL_ENTRIES, json).apply()
    }

    /**
     * Синхронизирует статистику с API
     */
    suspend fun syncWithAPI(): Result<LocalStats> = withContext(Dispatchers.IO) {
        try {
            // Загружаем статистику
            Log.d(TAG, "🔍 Запрашиваем статистику для account_id: $ACCOUNT_ID")
            val statsResponse = placesApi.getStats(ACCOUNT_ID)

            Log.d(TAG, "🔍 HTTP код ответа: ${statsResponse.code()}")
            Log.d(TAG, "🔍 Успешный ответ: ${statsResponse.isSuccessful}")

            if (statsResponse.isSuccessful && statsResponse.body() != null) {
                val stats = statsResponse.body()!!
                Log.d(TAG, "✅ Получена статистика:")
                Log.d(TAG, "   - today_distance: ${stats.today_distance}")
                Log.d(TAG, "   - today_steps: ${stats.today_steps}")
                Log.d(TAG, "   - weekly_chart: ${stats.weekly_chart}")
                Log.d(TAG, "   - streak: ${stats.streak}")
                Log.d(TAG, "   - achievements: ${stats.achievements}")

                // 🔥 TEMPORARY: Если бэкенд вернул пустые данные, используем mock
                if (USE_MOCK_DATA && stats.today_distance == 0f && stats.today_steps == 0) {
                    Log.w(TAG, "⚠️ Бэкенд вернул нулевые данные! Используем MOCK данные для тестирования...")
                    val mockStats = createMockStats()
                    saveStats(mockStats)
                    Log.d(TAG, "✅ Mock статистика сохранена")
                } else {
                    saveStats(stats)
                    Log.d(TAG, "✅ Статистика синхронизирована с API")
                }
            } else {
                val errorBody = statsResponse.errorBody()?.string()
                Log.e(TAG, "❌ Ошибка загрузки статистики:")
                Log.e(TAG, "   HTTP код: ${statsResponse.code()}")
                Log.e(TAG, "   Тело ошибки: $errorBody")
            }

            // Загружаем журнал
            Log.d(TAG, "🔍 Запрашиваем записи дневника для account_id: $ACCOUNT_ID")
            val journalResponse = placesApi.getJournalEntries(ACCOUNT_ID)

            Log.d(TAG, "🔍 Journal HTTP код: ${journalResponse.code()}")
            Log.d(TAG, "🔍 Journal успешный: ${journalResponse.isSuccessful}")

            if (journalResponse.isSuccessful && journalResponse.body() != null) {
                val entries = journalResponse.body()!!
                Log.d(TAG, "✅ Получено записей дневника: ${entries.size}")
                entries.take(3).forEach { entry ->
                    Log.d(TAG, "   📔 id=${entry.id}, date=${entry.date}, text='${entry.text.take(30)}...', poi_name=${entry.poi_name}")
                }

                // 🔥 TEMPORARY: Если дневник пустой, добавляем mock данные
                if (USE_MOCK_DATA && entries.isEmpty()) {
                    Log.w(TAG, "⚠️ Дневник пустой! Используем MOCK данные для тестирования...")
                    val mockEntries = createMockJournalEntries()
                    saveJournalEntries(mockEntries)
                    Log.d(TAG, "✅ Mock записи дневника сохранены: ${mockEntries.size} записей")
                } else {
                    // ✅ Сортируем по дате в убывающем порядке (новые -> старые) и берем топ-5
                    val sortedEntries = entries.sortedByDescending { it.date }
                    saveJournalEntries(sortedEntries.take(5))
                    Log.d(TAG, "✅ Дневник синхронизирован: ${entries.size} записей, сохранено топ-5")
                }
            } else {
                val errorBody = journalResponse.errorBody()?.string()
                Log.e(TAG, "❌ Ошибка загрузки дневника:")
                Log.e(TAG, "   HTTP код: ${journalResponse.code()}")
                Log.e(TAG, "   Тело ошибки: $errorBody")
            }

            val finalStats = getLocalStats()
            Log.d(TAG, "📊 Итоговая локальная статистика:")
            Log.d(TAG, "   - distance: ${finalStats.todayDistance}м")
            Log.d(TAG, "   - steps: ${finalStats.todaySteps}")
            Log.d(TAG, "   - streak: ${finalStats.streak}")
            Log.d(TAG, "   - achievements: ${finalStats.achievements.size}")

            Result.success(finalStats)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Ошибка синхронизации", e)
            Log.e(TAG, "   Exception: ${e.message}")
            Log.e(TAG, "   Stack trace: ", e)
            // Возвращаем локальные данные в случае ошибки
            Result.success(getLocalStats())
        }
    }

    /**
     * Получает последнюю запись из дневника (для отображения)
     */
    fun getLastJournalEntry(): JournalEntry? {
        return getLocalJournalEntries().maxByOrNull { it.date }
    }

    /**
     * Сбрасывает ежедневную статистику (вызывается в новый день)
     */
    fun resetDailyStats() {
        prefs.edit().apply {
            putFloat(KEY_TODAY_DISTANCE, 0f)
            putInt(KEY_TODAY_STEPS, 0)
            apply()
        }
    }

    /**
     * Проверяет, нужно ли обновить данные (если прошло больше 5 минут)
     */
    fun shouldSync(): Boolean {
        val lastUpdate = prefs.getLong(KEY_LAST_UPDATE, 0L)
        val fiveMinutesAgo = System.currentTimeMillis() - (5 * 60 * 1000)
        return lastUpdate < fiveMinutesAgo
    }

    /**
     * 🔥 TEMPORARY: Создает mock статистику для тестирования
     * Удали это когда бэкенд заработает!
     */
    private fun createMockStats(): StatsResponse {
        return StatsResponse(
            today_distance = 2350f,  // 2.35 км
            today_steps = 3200,
            weekly_chart = listOf(1800f, 2100f, 0f, 1500f, 2350f, 0f, 0f),  // последние 7 дней
            streak = 4,  // 4 дня подряд
            achievements = listOf("Первые 10 км", "Стрик 3 дня", "Открыл 5 мест")
        )
    }

    /**
     * 🔥 TEMPORARY: Создает mock записи дневника для тестирования
     * Удали это когда бэкенд заработает!
     */
    private fun createMockJournalEntries(): List<JournalEntry> {
        return listOf(
            JournalEntry(
                id = 1,
                date = "2025-11-06T12:59:12",
                text = "Посетил Тануки. Впечатление: Неплохо 🙂",
                photo_path = null,
                poi_name = "Тануки",
                session_id = 1
            ),
            JournalEntry(
                id = 2,
                date = "2025-11-05T14:30:00",
                text = "Прогулка в парке. Отлично провел время! 😊",
                photo_path = null,
                poi_name = "Парк Горького",
                session_id = 2
            ),
            JournalEntry(
                id = 3,
                date = "2025-11-04T10:15:00",
                text = "Утренняя пробежка. Заряд бодрости на весь день ⚡",
                photo_path = null,
                poi_name = null,
                session_id = 3
            )
        )
    }
}
