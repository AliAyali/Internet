package com.aliayali.internet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.aliayali.internet.navigation.SetupNavigation
import com.aliayali.internet.ui.theme.InternetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            InternetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SetupNavigation(
                        innerPadding,
                        navController
                    )
                }
            }
        }
    }
}