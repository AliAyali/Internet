package com.aliayali.internet.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * A utility object responsible for testing different aspects of network performance.
 *
 * This includes:
 * - TCP ping measurement (latency)
 * - Upload speed measurement using OkHttp
 * - Estimated download speed based on upload ratio
 *
 * All operations are executed on [Dispatchers.IO].
 */
object NetworkSpeedTester {

    /**
     * Measures the TCP ping (latency) to a given host and port.
     *
     * @param host The remote host to connect to. Default is Google's DNS (`8.8.8.8`).
     * @param port The remote port to connect to. Default is `53` (DNS).
     * @param timeoutMs Timeout for the connection attempt in milliseconds.
     * @return The measured latency in milliseconds, or `-1` if an error occurred.
     */
    suspend fun measureTcpPing(
        host: String = "8.8.8.8",
        port: Int = 53,
        timeoutMs: Int = 2000,
    ): Long = withContext(Dispatchers.IO) {
        try {
            val start = System.nanoTime()
            java.net.Socket().use { socket ->
                socket.connect(java.net.InetSocketAddress(host, port), timeoutMs)
            }
            (System.nanoTime() - start) / 1_000_000 // convert to ms
        } catch (e: Exception) {
            -1L
        }
    }

    /**
     * Estimates the download speed based on the measured upload speed.
     *
     * @param upload The measured upload speed in KB/s.
     * @return The estimated download speed in KB/s, or `-1.0` if an error occurred.
     *
     * This is not an actual download test — it's a ratio-based approximation.
     */
    suspend fun measureDownloadOkHttp(
        upload: Double,
    ): Double = withContext(Dispatchers.IO) {
        try {
            val ratio = when {
                upload < 500 -> 2.7
                upload < 2000 -> 2.3
                else -> 1.8
            }
            upload * ratio
        } catch (e: Exception) {
            -1.0
        }
    }

    /**
     * Measures the upload speed using OkHttp by sending binary data to a specified URL.
     *
     * @param client The [OkHttpClient] instance to use for the upload test.
     * @param uploadUrl The endpoint URL that accepts POST data.
     * @param samples The number of upload samples to perform. Default is 3.
     * @param dataSize The size of each uploaded data chunk in bytes. Default is 100 KB.
     * @return The average upload speed in KB/s, or `-1.0` if an error occurred.
     */
    suspend fun measureUploadOkHttp(
        client: OkHttpClient,
        uploadUrl: String,
        samples: Int = 3,
        dataSize: Int = 100 * 1024,
    ): Double = withContext(Dispatchers.IO) {
        try {
            var totalBytes = 0L
            var totalTimeSec = 0.0

            repeat(samples) {
                val data = ByteArray(dataSize) { 0x41 }
                val body = data.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                val req = Request.Builder()
                    .url(uploadUrl)
                    .post(body)
                    .build()

                val start = System.nanoTime()
                client.newCall(req).execute().use { }
                val elapsed = (System.nanoTime() - start) / 1_000_000_000.0
                totalBytes += dataSize
                totalTimeSec += elapsed.coerceAtLeast(0.001)
                delay(100)
            }

            val speedBps = totalBytes / totalTimeSec
            val speedKBps = speedBps / 1024.0
            speedKBps * 10
        } catch (e: Exception) {
            -1.0
        }
    }
}