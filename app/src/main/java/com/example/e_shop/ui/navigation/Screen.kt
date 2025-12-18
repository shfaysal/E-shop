package com.example.e_shop.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object ProductAdd : Screen("product_add")
    object Profile : Screen("profile")
}
