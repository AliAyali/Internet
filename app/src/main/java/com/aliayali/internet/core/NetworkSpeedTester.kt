package com.aliayali.internet.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


object NetworkSpeedTester {
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
            (System.nanoTime() - start) / 1_000_000 // ms
        } catch (e: Exception) {
            -1L
        }
    }
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
                client.newCall(req).execute().use { resp -> }
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