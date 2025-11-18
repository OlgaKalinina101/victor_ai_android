package com.example.victor_ai.logic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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

    companion object {
        private const val TAG = "WiFiNetworkManager"
    }

    private val wifiManager: WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Получить информацию о текущей подключенной WiFi сети
     * @return Pair<SSID, BSSID> или null если не подключен
     */
    fun getCurrentWiFi(): Pair<String, String>? {
        Log.d(TAG, "hasLocationPermission = ${hasLocationPermission()}")

        if (!hasLocationPermission()) {
            Log.d(TAG, "No location permission!")
            return null
        }

        return try {
            val network: Network? = connectivityManager.activeNetwork
            Log.d(TAG, "network = $network")
            val capabilities = network?.let { connectivityManager.getNetworkCapabilities(it) }
            Log.d(TAG, "capabilities = $capabilities")

            if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                Log.d(TAG, "Has WIFI transport")
                val wifiInfo = capabilities.transportInfo as? WifiInfo

                // Если wifiInfo == null (VPN активен), пробуем получить из underlying network
                val actualWifiInfo = if (wifiInfo == null) {
                    getWiFiFromVPN(capabilities)
                } else {
                    wifiInfo
                }

                Log.d(TAG, "actualWifiInfo = $actualWifiInfo")

                val ssid = actualWifiInfo?.ssid?.removeSurrounding("\"")
                val bssid = actualWifiInfo?.bssid

                if (ssid == null || bssid == null) {
                    Log.d(TAG, "ssid or bssid is null")
                    return null
                }

                Log.d(TAG, "ssid = $ssid, bssid = $bssid")
                Pair(ssid, bssid)
            } else {
                Log.d(TAG, "No WIFI transport")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception getting WiFi info", e)
            null
        }
    }

    /**
     * Получить WiFi из-под VPN (Android 12+)
     */
    @RequiresApi(Build.VERSION_CODES.S)
    @Suppress("NewApi")
    private fun getWiFiFromVPN(capabilities: NetworkCapabilities): WifiInfo? {
        return try {
            // Используем рефлексию для доступа к underlyingNetworks
            val method = NetworkCapabilities::class.java.getMethod("getUnderlyingNetworks")
            @Suppress("UNCHECKED_CAST")
            val underlyingNetworks = method.invoke(capabilities) as? Array<Network>
            Log.d(TAG, "VPN detected, underlyingNetworks = ${underlyingNetworks?.toList()}")

            underlyingNetworks?.firstOrNull()?.let { underlyingNetwork ->
                val underlyingCaps = connectivityManager.getNetworkCapabilities(underlyingNetwork)
                underlyingCaps?.transportInfo as? WifiInfo
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get underlying networks: ${e.message}")
            null
        }
    }

    /**
     * Получить список доступных WiFi сетей
     * @return List<Pair<SSID, BSSID>>
     */
    @SuppressLint("MissingPermission")
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
