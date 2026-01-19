package com.example.e_shop.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.e_shop.R
import com.example.e_shop.ui.common.ToastHelper
import com.example.e_shop.ui.components.AppText
import com.example.e_shop.ui.navigation.Screen

import androidx.compose.ui.res.stringResource

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            ToastHelper.showToast(context, it)
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppText(text = stringResource(R.string.login), style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xxl)))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { AppText(stringResource(R.string.email)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { AppText(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_large)))

        Button(
            onClick = { viewModel.login(email, password) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                AppText(stringResource(R.string.login))
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            AppText(stringResource(R.string.dont_have_account))
        }
    }
}