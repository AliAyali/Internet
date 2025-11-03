package com.aliayali.internet.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.aliayali.internet.R
import com.aliayali.internet.core.ConnectionState
import com.aliayali.internet.core.ConnectionType
import com.aliayali.internet.core.NetworkQualityEvaluator
import com.aliayali.internet.presentation.components.Line
import com.aliayali.internet.presentation.components.SpeedResultRow
import com.aliayali.internet.ui.theme.Green

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val connectionState by homeViewModel.connectionState.collectAsState()
    val ping by homeViewModel.ping.collectAsState()
    val download by homeViewModel.downloadSpeed.collectAsState()
    val upload by homeViewModel.uploadSpeed.collectAsState()
    val connectionType by homeViewModel.connectionType.collectAsState()

    val compositionIdle by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loading),
    )
    val compositionScanning by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.eye),
    )
    val compositionConnected by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.right),
    )
    val compositionDisconnected by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.wrong),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = when (connectionType) {
                    ConnectionType.WIFI -> "Connected via Wi-Fi"
                    ConnectionType.MOBILE -> "Connected via Mobile Data"
                    ConnectionType.ETHERNET -> "Connected via Ethernet"
                    ConnectionType.NONE -> "No Connection"
                },
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                when (connectionType) {
                    ConnectionType.WIFI -> painterResource(R.drawable.wifi)
                    ConnectionType.MOBILE -> painterResource(R.drawable.mobile)
                    ConnectionType.ETHERNET -> painterResource(R.drawable.ethernet)
                    ConnectionType.NONE -> painterResource(R.drawable.none)
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        when (connectionState) {
            ConnectionState.Idle -> {
                LottieAnimation(
                    composition = compositionScanning,
                    iterations = Integer.MAX_VALUE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable {
                            homeViewModel.testInternet()
                        },
                    contentScale = ContentScale.Fit,
                    isPlaying = true,
                    reverseOnRepeat = true,
                )
            }

            ConnectionState.Scanning -> {
                LottieAnimation(
                    composition = compositionIdle,
                    iterations = Integer.MAX_VALUE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit,
                    isPlaying = true,
                )
            }

            ConnectionState.Connected -> {
                LottieAnimation(
                    composition = compositionConnected,
                    iterations = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit,
                    isPlaying = true,
                )
            }

            ConnectionState.Disconnected -> {
                LottieAnimation(
                    composition = compositionDisconnected,
                    iterations = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit,
                    isPlaying = true,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Green, RoundedCornerShape(10.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SpeedResultRow(label = "Ping", value = ping.toInt(), state = connectionState)
            Line()
            SpeedResultRow(label = "Download", value = download.toInt(), state = connectionState)
            Line()
            SpeedResultRow(label = "Upload", value = upload.toInt(), state = connectionState)
        }

        val quality = NetworkQualityEvaluator.evaluate(
            connectionState = connectionState,
            ping = ping,
            download = download
        )

        Text(
            text = quality.statusText,
            color = quality.statusColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}