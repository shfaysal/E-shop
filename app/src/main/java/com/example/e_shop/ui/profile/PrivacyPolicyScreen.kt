package com.example.e_shop.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Privacy Policy for E-shop",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = """
                    Last updated: January 16, 2026

                    At E-shop, we take your privacy seriously. This Privacy Policy explains how we collect, use, and protect your information.

                    1. Information We Collect
                    We collect information you provide directly to us, such as when you create an account (name, email, password) and when you update your profile.

                    2. How We Use Your Information
                    We use the information we collect to provide, maintain, and improve our services, including processing your orders and personalizing your experience.

                    3. Data Security
                    We implement appropriate security measures to protect your personal information from unauthorized access or disclosure.

                    4. Third-Party Services
                    We use the Platzi Fake Store API for demonstration purposes. Please be aware that data sent to this API is public and should not contain real personal information.

                    5. Changes to This Policy
                    We may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page.

                    Contact Us
                    If you have any questions about this Privacy Policy, please contact us at support@e-shop.com.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
