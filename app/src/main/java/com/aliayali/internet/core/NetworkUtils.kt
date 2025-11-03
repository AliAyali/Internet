package com.aliayali.internet.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Utility object for retrieving information about the current network connection.
 *
 * Provides methods to check connection type (Wi-Fi, Mobile, Ethernet)
 * and whether the device is currently connected to the internet.
 */
object NetworkUtils {
    /**
     * Returns the current [ConnectionType] of the device.
     *
     * @param context Application context.
     * @return [ConnectionType] representing the active network transport type:
     *  - [ConnectionType.WIFI] → Connected via Wi-Fi
     *  - [ConnectionType.MOBILE] → Connected via Mobile Data
     *  - [ConnectionType.ETHERNET] → Connected via Ethernet (wired)
     *  - [ConnectionType.NONE] → No active network connection
     */
    fun getConnectionType(context: Context): ConnectionType {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return ConnectionType.NONE
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return ConnectionType.NONE

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.MOBILE
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
            else -> ConnectionType.NONE
        }
    }

    /**
     * Checks if the device currently has any active internet connection.
     *
     * @param context Application context.
     * @return `true` if connected (Wi-Fi, Mobile, or Ethernet), otherwise `false`.
     */
    fun isConnected(context: Context): Boolean {
        return getConnectionType(context) != ConnectionType.NONE
    }
}