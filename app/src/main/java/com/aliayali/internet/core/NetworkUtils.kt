package com.aliayali.internet.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


object NetworkUtils {
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

    fun isConnected(context: Context): Boolean {
        return getConnectionType(context) != ConnectionType.NONE
    }
}