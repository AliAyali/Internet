package com.aliayali.internet.navigation

/**
 * Defines all navigation destinations (routes) used in the app.
 *
 * Each object represents a screen and holds its unique [route] string
 * for use with the navigation graph.
 *
 * Example usage:
 * ```
 * navController.navigate(NavigationScreen.Home.route)
 * ```
 */
sealed class NavigationScreen(val route: String) {
    /** Splash screen shown when the app launches */
    object Splash : NavigationScreen("splash")

    /** Main Home screen displayed after splash */
    object Home : NavigationScreen("home")
}