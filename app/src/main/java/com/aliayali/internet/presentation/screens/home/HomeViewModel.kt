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
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appContext: Application,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val okHttpClient = OkHttpClient()

    fun testInternet() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(connectionState = ConnectionState.Scanning)

            val connectionType = NetworkUtils.getConnectionType(appContext)
            _uiState.value = _uiState.value.copy(connectionType = connectionType)

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
                    NetworkSpeedTester.measureDownloadOkHttp(uploadDeferred.await())
                }

                _uiState.value = _uiState.value.copy(
                    ping = pingDeferred.await(),
                    downloadSpeed = downloadDeferred.await(),
                    uploadSpeed = uploadDeferred.await(),
                    connectionState = ConnectionState.Connected
                )

                delay(3000)
                _uiState.value = _uiState.value.copy(connectionState = ConnectionState.Idle)

            } else {
                _uiState.value = _uiState.value.copy(
                    connectionState = ConnectionState.Disconnected,
                    ping = -1,
                    downloadSpeed = -1.0,
                    uploadSpeed = -1.0
                )
            }
        }
    }
}