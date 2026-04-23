package com.example.myapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.navigation.compose.rememberNavController

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun submenu1_displaysTextAndButton() {
        // Start the app
        composeTestRule.setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                Submenu1(navController = navController)
            }
        }

        // Verify the title and the text are displayed
        composeTestRule.onNodeWithText("Acerca de nosotros").assertIsDisplayed()
        composeTestRule.onNodeWithText("App de Estudiantes - Clase 5").assertIsDisplayed()
        
        // Verify the button
        composeTestRule.onNodeWithText("Ir al menu").assertIsDisplayed()
    }
}
