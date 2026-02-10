package com.example.e_shop.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.e_shop.R
import com.example.e_shop.ui.components.AppText
import com.example.e_shop.ui.navigation.Screen

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLogoutSuccess) {
        if (uiState.isLogoutSuccess) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
            viewModel.resetLogoutState()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Section
                ProfileHeader(
                    name = uiState.user?.safeName ?: "Guest",
                    email = uiState.user?.safeEmail ?: "guest@example.com",
                    avatarUrl = uiState.user?.safeAvatar,
                    role = uiState.user?.safeRole ?: "User"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Settings Section
                AppText(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                // Theme Toggle
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = if (uiState.isDarkMode) stringResource(R.string.light_mode) else stringResource(R.string.dark_mode),
                    showArrow = false,
                    trailingContent = {
                        Switch(
                            checked = uiState.isDarkMode,
                            onCheckedChange = { viewModel.toggleTheme(it) }
                        )
                    }
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                
                // Language Selection
                var showLanguageMenu by remember { mutableStateOf(false) }
                Box {
                    ProfileMenuItem(
                        icon = Icons.Default.Info, // Placeholder for Language icon if not available
                        title = stringResource(
                            R.string.language_selected,
                            if (uiState.language == "bn") stringResource(R.string.bangla) else stringResource(R.string.english)
                        ),
                        onClick = { showLanguageMenu = true }
                    )
                    
                    DropdownMenu(
                        expanded = showLanguageMenu,
                        onDismissRequest = { showLanguageMenu = false },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.english)) },
                            onClick = {
                                viewModel.setLanguage("en")
                                showLanguageMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.bangla)) },
                            onClick = {
                                viewModel.setLanguage("bn")
                                showLanguageMenu = false
                            }
                        )
                    }
                }

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                // Edit Profile (Navigation placeholder)
                ProfileMenuItem(
                    icon = Icons.Default.Edit,
                    title = stringResource(R.string.edit_profile),
                    onClick = { navController.navigate(Screen.EditProfile.route) }
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                // Privacy Policy (Navigation placeholder)
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.privacy_policy),
                    onClick = { navController.navigate(Screen.PrivacyPolicy.route) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.logout))
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    email: String,
    avatarUrl: String?,
    role: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop,
            error = null, // Can add error placeholder
            placeholder = null // Can add placeholder
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = name, style = MaterialTheme.typography.headlineSmall)
        Text(text = email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = CircleShape
        ) {
            Text(
                text = role.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    showArrow: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        if (trailingContent != null) {
            trailingContent()
        } else if (showArrow) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}