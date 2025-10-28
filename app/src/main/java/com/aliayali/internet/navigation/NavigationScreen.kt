package com.aliayali.internet.navigation

sealed class NavigationScreen(val route: String) {
    object Splash : NavigationScreen("splash")
    object Home : NavigationScreen("home")
}