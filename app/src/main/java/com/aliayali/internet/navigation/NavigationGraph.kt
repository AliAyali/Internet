package com.aliayali.internet.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aliayali.internet.presentation.screens.home.HomeScreen
import com.aliayali.internet.presentation.screens.splash.SplashScreen

/**
 * Sets up the navigation graph for the app using [NavHost].
 *
 * @param padding Padding values provided by Scaffold or parent composable.
 * @param navController Controller responsible for navigation between screens.
 *
 * Defines two destinations:
 * - SplashScreen: the initial screen of the app.
 * - HomeScreen: the main screen displayed after splash.
 */
@Composable
fun SetupNavigation(
    padding: PaddingValues,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Splash.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(route = NavigationScreen.Splash.route) {
            SplashScreen(navController)
        }
        composable(route = NavigationScreen.Home.route) {
            HomeScreen()
        }
    }
}