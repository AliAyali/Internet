package com.aliayali.internet.presentation.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliayali.internet.core.ConnectionState
import com.aliayali.internet.core.ConnectionType
import com.aliayali.internet.core.NetworkSpeedTester
import com.aliayali.internet.core.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appContext: Application,
) : ViewModel() {

    private val _connectionState = MutableStateFlow(ConnectionState.Idle)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _ping = MutableStateFlow(-1L)
    val ping: StateFlow<Long> = _ping

    private val _downloadSpeed = MutableStateFlow(-1.0)
    val downloadSpeed: StateFlow<Double> = _downloadSpeed

    private val _uploadSpeed = MutableStateFlow(-1.0)
    val uploadSpeed: StateFlow<Double> = _uploadSpeed

    private val _connectionType = MutableStateFlow(ConnectionType.NONE)
    val connectionType: StateFlow<ConnectionType> = _connectionType

    private val okHttpClient = OkHttpClient()

    fun testInternet() {
        viewModelScope.launch {
            _connectionState.value = ConnectionState.Scanning

            _connectionType.value = NetworkUtils.getConnectionType(appContext)

            if (NetworkUtils.isConnected(appContext)) {
                val pingDeferred = async { NetworkSpeedTester.measureTcpPing() }

                val uploadDeferred = async {
                    NetworkSpeedTester.measureUploadOkHttp(
                        okHttpClient,
                        "https://httpbin.org/post",
                        dataSize = 100 * 1024
                    )
                }

                val downloadDeferred = async {
                    NetworkSpeedTester.measureDownloadOkHttp(
                        uploadDeferred.await(),
                    )
                }

                _ping.value = pingDeferred.await()
                _downloadSpeed.value = downloadDeferred.await()
                _uploadSpeed.value = uploadDeferred.await()
                _connectionState.value = ConnectionState.Connected
                delay(3000)
                _connectionState.value = ConnectionState.Idle
            } else {
                _ping.value = -1
                _downloadSpeed.value = -1.0
                _uploadSpeed.value = -1.0
                _connectionState.value = ConnectionState.Disconnected
            }
        }
    }
}