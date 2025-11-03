package com.aliayali.internet.core

import androidx.compose.ui.graphics.Color

/**
 * Utility object responsible for evaluating network quality based on
 * connection state, ping, and download speed.
 *
 * Returns a [NetworkQualityResult] that includes both a descriptive
 * status message and a corresponding color for UI feedback.
 */
object NetworkQualityEvaluator {

    /**
     * Evaluates the current network quality.
     *
     * @param connectionState The current [ConnectionState] of the device.
     * @param ping The measured ping in milliseconds.
     * @param download The measured download speed in KB/s.
     * @return A [NetworkQualityResult] containing the status text and color.
     */
    fun evaluate(
        connectionState: ConnectionState,
        ping: Long,
        download: Double,
    ): NetworkQualityResult {
        return when {
            connectionState == ConnectionState.Disconnected -> NetworkQualityResult(
                statusText = "Status -> No Internet Connection",
                statusColor = Color.Red
            )

            connectionState == ConnectionState.Scanning -> NetworkQualityResult(
                statusText = "Status -> Testing...",
                statusColor = Color.Gray
            )

            ping < 0 || download < 0 -> NetworkQualityResult(
                statusText = "Status -> Click on the eye",
                statusColor = Color.Gray
            )

            ping > 300 || download < 300 -> NetworkQualityResult(
                statusText = "Status -> Poor Connection",
                statusColor = Color.Red
            )

            ping in 150..300 || download in 300.0..700.0 -> NetworkQualityResult(
                statusText = "Status -> Moderate Connection",
                statusColor = Color.Yellow
            )

            else -> NetworkQualityResult(
                statusText = "Status -> Good Connection",
                statusColor = Color.Green
            )
        }
    }
}
