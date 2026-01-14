package com.example.e_shop.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home)
    object Cart : BottomNavItem(Screen.Cart.route, "Cart", Icons.Default.ShoppingCart)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}
