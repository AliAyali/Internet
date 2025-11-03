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

/**
 * Main entry point of the application.
 *
 * Hosts the navigation graph and initializes the Compose UI hierarchy.
 * Uses Hilt for dependency injection and sets up the app theme and Scaffold layout.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables system edge-to-edge rendering for modern layout styling
        enableEdgeToEdge()

        setContent {
            // Remember NavController for navigation between screens
            val navController = rememberNavController()

            // Apply app theme and main layout container
            InternetTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // Set up the navigation graph
                    SetupNavigation(
                        padding = innerPadding,
                        navController = navController
                    )
                }
            }
        }
    }
}