package com.example.exp10

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

// LOCATION: app/src/androidTest/java/com/example/exp10/TipCalculatorScreenTest.kt  (the "(androidTest)" folder)
// Compose UI TESTS – need a running emulator or phone
class TipCalculatorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun launchScreen() {
        composeTestRule.setContent {
            MaterialTheme {
                TipCalculatorScreen()
            }
        }
    }

    @Test
    fun default_tip_is_15_percent() {
        launchScreen()
        composeTestRule.onNodeWithTag("tip15").assertIsSelected()
    }

    @Test
    fun bill_100_with_15_percent_shows_correct_tip_and_total() {
        launchScreen()
        composeTestRule.onNodeWithTag("billInput").performTextInput("100")
        composeTestRule.onNodeWithTag("tip15").performClick()

        composeTestRule.onNodeWithTag("tipAmount").assertTextEquals("Tip Amount: ₹15.00")
        composeTestRule.onNodeWithTag("totalAmount").assertTextEquals("Total Bill: ₹115.00")
    }

    @Test
    fun bill_200_with_20_percent_shows_correct_tip_and_total() {
        launchScreen()
        composeTestRule.onNodeWithTag("billInput").performTextInput("200")
        composeTestRule.onNodeWithTag("tip20").performClick()

        composeTestRule.onNodeWithTag("tipAmount").assertTextEquals("Tip Amount: ₹40.00")
        composeTestRule.onNodeWithTag("totalAmount").assertTextEquals("Total Bill: ₹240.00")
    }

    @Test
    fun bill_50_with_10_percent_shows_correct_tip_and_total() {
        launchScreen()
        composeTestRule.onNodeWithTag("billInput").performTextInput("50")
        composeTestRule.onNodeWithTag("tip10").performClick()

        composeTestRule.onNodeWithTag("tipAmount").assertTextEquals("Tip Amount: ₹5.00")
        composeTestRule.onNodeWithTag("totalAmount").assertTextEquals("Total Bill: ₹55.00")
    }
}
