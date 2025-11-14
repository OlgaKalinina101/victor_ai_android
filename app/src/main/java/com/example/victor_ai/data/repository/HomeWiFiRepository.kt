package com.example.victor_ai.data.repository

import android.content.Context
import android.content.SharedPreferences

/**
 * 🏠 Repository для управления домашним WiFi
 *
 * Хранит информацию о домашней WiFi сети:
 * - SSID (имя сети)
 * - BSSID (MAC-адрес точки доступа)
 * - GPS координаты дома (широта, долгота)
 */
class HomeWiFiRepository(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "home_wifi_prefs"
        private const val KEY_HOME_SSID = "home_ssid"
        private const val KEY_HOME_BSSID = "home_bssid"
        private const val KEY_HOME_LATITUDE = "home_latitude"
        private const val KEY_HOME_LONGITUDE = "home_longitude"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Сохранить домашний WiFi с координатами
     */
    fun saveHomeWiFi(ssid: String, bssid: String, latitude: Double, longitude: Double) {
        prefs.edit().apply {
            putString(KEY_HOME_SSID, ssid)
            putString(KEY_HOME_BSSID, bssid)
            putFloat(KEY_HOME_LATITUDE, latitude.toFloat())
            putFloat(KEY_HOME_LONGITUDE, longitude.toFloat())
            apply()
        }
    }

    /**
     * Получить SSID домашнего WiFi
     */
    fun getHomeSSID(): String? = prefs.getString(KEY_HOME_SSID, null)

    /**
     * Получить BSSID домашнего WiFi
     */
    fun getHomeBSSID(): String? = prefs.getString(KEY_HOME_BSSID, null)

    /**
     * Получить координаты дома
     */
    fun getHomeCoordinates(): Pair<Double, Double>? {
        val latitude = prefs.getFloat(KEY_HOME_LATITUDE, Float.MIN_VALUE)
        val longitude = prefs.getFloat(KEY_HOME_LONGITUDE, Float.MIN_VALUE)

        return if (latitude != Float.MIN_VALUE && longitude != Float.MIN_VALUE) {
            Pair(latitude.toDouble(), longitude.toDouble())
        } else {
            null
        }
    }

    /**
     * Проверить, установлен ли домашний WiFi
     */
    fun isHomeWiFiSet(): Boolean = getHomeSSID() != null

    /**
     * Удалить домашний WiFi
     */
    fun clearHomeWiFi() {
        prefs.edit().clear().apply()
    }
}
