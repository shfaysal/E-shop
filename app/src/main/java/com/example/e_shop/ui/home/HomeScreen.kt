package com.example.e_shop.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.e_shop.ui.components.AppText
import com.example.e_shop.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppText(text = "Home Screen", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(onClick = { navController.navigate(Screen.ProductAdd.route) }) {
            AppText(text = "Go to Add Product")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { navController.navigate(Screen.Profile.route) }) {
            AppText(text = "Go to Profile")
        }
    }
}