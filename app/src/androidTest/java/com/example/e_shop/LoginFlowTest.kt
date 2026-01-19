package com.example.e_shop

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testLoginScreenIsDisplayed() {
        // Wait for the app to settle
        composeTestRule.waitForIdle()

        // Check if "Login" title is displayed
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        
        // Check if Email field is displayed
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        
        // Check if Password field is displayed
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
    }
}
