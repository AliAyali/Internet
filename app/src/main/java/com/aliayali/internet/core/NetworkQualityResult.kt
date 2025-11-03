package com.aliayali.internet.core

import androidx.compose.ui.graphics.Color

/**
 * Represents the result of evaluating network quality.
 *
 * @param statusText Descriptive text of the current connection status.
 * @param statusColor Associated color used for displaying the status in UI.
 */
data class NetworkQualityResult(
    val statusText: String,
    val statusColor: Color,
)
