package com.example.e_shop.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.e_shop.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.e_shop.ui.auth.AuthViewModel
import com.example.e_shop.ui.components.AppText
import com.example.e_shop.ui.navigation.Screen

import androidx.compose.ui.res.stringResource

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.logoutSuccess) {
        if (uiState.logoutSuccess) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppText(text = stringResource(R.string.profile), style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xxl)))
        
        Button(onClick = { viewModel.logout() }) {
            AppText(text = stringResource(R.string.logout))
        }
    }
}