package com.example.e_shop.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home)
    object ProductAdd : BottomNavItem(Screen.ProductAdd.route, "Add", Icons.Default.Add)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}
