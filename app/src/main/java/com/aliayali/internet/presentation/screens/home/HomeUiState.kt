package com.aliayali.internet.presentation.screens.home

import com.aliayali.internet.core.ConnectionState
import com.aliayali.internet.core.ConnectionType

/**
 * UI state for the Home screen.
 *
 * Holds all network-related information such as current connection state,
 * connection type, and measured ping/download/upload speeds.
 */
data class HomeUiState(
    val connectionState: ConnectionState = ConnectionState.Idle,
    val connectionType: ConnectionType = ConnectionType.NONE,
    val ping: Long = -1L,
    val downloadSpeed: Double = -1.0,
    val uploadSpeed: Double = -1.0
)