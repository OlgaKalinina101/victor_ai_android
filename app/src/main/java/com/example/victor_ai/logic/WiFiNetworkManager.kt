package com.example.victor_ai.logic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

/**
 * 📡 Менеджер для работы с WiFi сетями
 *
 * Функции:
 * - Получение списка доступных WiFi сетей
 * - Получение текущей подключенной сети
 * - Проверка подключения к конкретной сети
 */
class WiFiNetworkManager(private val context: Context) {

    private val wifiManager: WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Получить информацию о текущей подключенной WiFi сети
     * @return Pair<SSID, BSSID> или null если не подключен
     */
    fun getCurrentWiFi(): Pair<String, String>? {
        println("DEBUG: hasLocationPermission = ${hasLocationPermission()}")

        if (!hasLocationPermission()) {
            println("DEBUG: No location permission!")
            return null
        }

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                println("DEBUG: Android 10+")
                getCurrentWiFiQ()
            } else {
                // Android 9 и ниже
                println("DEBUG: Android 9-")
                @Suppress("DEPRECATION")
                val wifiInfo = wifiManager.connectionInfo
                if (wifiInfo != null && wifiInfo.networkId != -1) {
                    val ssid = wifiInfo.ssid.removeSurrounding("\"")
                    val bssid = wifiInfo.bssid
                    if (ssid.isNotEmpty() && bssid != null) {
                        Pair(ssid, bssid)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            println("DEBUG: Exception! ${e.message}")
            e.printStackTrace()
            null
        }
    }

    /**
     * Получить WiFi для Android 10+
     */
    @androidx.annotation.RequiresApi(Build.VERSION_CODES.Q)
    private fun getCurrentWiFiQ(): Pair<String, String>? {
        val network: Network? = connectivityManager.activeNetwork
        println("DEBUG: network = $network")
        val capabilities = network?.let { connectivityManager.getNetworkCapabilities(it) }
        println("DEBUG: capabilities = $capabilities")

        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
            println("DEBUG: Has WIFI transport")
            val wifiInfo = capabilities.transportInfo as? WifiInfo

            // Если wifiInfo == null (VPN активен), пробуем получить из underlying network
            val actualWifiInfo = if (wifiInfo == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                getWiFiFromVPN(capabilities)
            } else {
                wifiInfo
            }

            println("DEBUG: actualWifiInfo = $actualWifiInfo")

            val ssid = actualWifiInfo?.ssid?.removeSurrounding("\"")
            val bssid = actualWifiInfo?.bssid

            if (ssid == null || bssid == null) {
                println("DEBUG: ssid or bssid is null")
                return null
            }

            println("DEBUG: ssid = $ssid, bssid = $bssid")
            return Pair(ssid, bssid)
        } else {
            println("DEBUG: No WIFI transport")
            return null
        }
    }

    /**
     * Получить WiFi из-под VPN (Android 12+)
     */
    @androidx.annotation.RequiresApi(Build.VERSION_CODES.S)
    private fun getWiFiFromVPN(capabilities: NetworkCapabilities): WifiInfo? {
        // Android 12+ - underlyingNetworks доступны
        val underlyingNetworks = capabilities.underlyingNetworks
        println("DEBUG: VPN detected, underlyingNetworks = ${underlyingNetworks?.toList()}")

        // Ищем WiFi среди underlying
        return underlyingNetworks?.firstOrNull()?.let { underlyingNetwork ->
            val underlyingCaps = connectivityManager.getNetworkCapabilities(underlyingNetwork)
            underlyingCaps?.transportInfo as? WifiInfo
        }
    }

    /**
     * Получить список доступных WiFi сетей
     * @return List<Pair<SSID, BSSID>>
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getAvailableNetworks(): List<Pair<String, String>> {
        if (!hasLocationPermission()) {
            return emptyList()
        }

        return try {
            @Suppress("DEPRECATION")
            val scanResults = wifiManager.scanResults
            scanResults.mapNotNull { result ->
                val ssid = result.SSID
                val bssid = result.BSSID
                if (ssid.isNotEmpty() && bssid != null) {
                    Pair(ssid, bssid)
                } else {
                    null
                }
            }.distinctBy { it.first } // убираем дубликаты по SSID
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Запустить сканирование WiFi сетей
     */
    fun startScan(): Boolean {
        if (!hasLocationPermission()) {
            return false
        }

        return try {
            @Suppress("DEPRECATION")
            wifiManager.startScan()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Проверить, подключен ли к конкретной сети
     */
    fun isConnectedTo(ssid: String, bssid: String): Boolean {
        val current = getCurrentWiFi() ?: return false
        return current.first == ssid && current.second == bssid
    }

    /**
     * Проверить, есть ли разрешение на доступ к местоположению
     */
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
