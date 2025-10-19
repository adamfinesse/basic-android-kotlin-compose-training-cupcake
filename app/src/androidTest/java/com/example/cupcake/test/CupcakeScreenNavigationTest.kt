package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cupcake.CupcakeApp
import com.example.cupcake.CupcakeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CupcakeScreenNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupCupcakeNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            CupcakeApp(navController = navController)
        }
    }

    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        val backText = composeTestRule.activity.getString(com.example.cupcake.R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun cupcakeNavHost_clickOneCupcake_navigatesToSelectFlavorScreen() {
        navigateToFlavorScreen()
        navController.assertCurrentRouteName(CupcakeScreen.Flavor.name)
    }

    @Test
    fun cupcakeNavHost_clickDate_navigatesToDateScreen() {
        navigateToDateScreen()
        navController.assertCurrentRouteName(CupcakeScreen.Pickup.name)
    }

    @Test
    fun cupcakeNavHost_clickSummary_navigatesToSummaryScreen() {
        navigateToSummaryScreen()
        navController.assertCurrentRouteName(CupcakeScreen.Summary.name)
    }

    @Test
    fun cupcakeNavHost_clickBack_navigatesToHomeFromFlavorScreen() {
        navigateToFlavorScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_clickCancel_navigatesToHomeFromFlavorScreen() {
        navigateToFlavorScreen()
        performCancelClick()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_clickBack_navigatesToSelectFlavorScreenFromPickup() {
        navigateToDateScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(CupcakeScreen.Flavor.name)
    }

    @Test
    fun cupcakeNavHost_clickCancel_navigatesToHomeFromPickupScreen() {
        navigateToDateScreen()
        performCancelClick()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_clickCancel_navigatesToHomeFromSummaryScreen() {
        navigateToSummaryScreen()
        performCancelClick()
        navController.assertCurrentRouteName(CupcakeScreen.Summary.name)
    }

    private fun performCancelClick() {
        val cancelTest = composeTestRule.activity.getString(com.example.cupcake.R.string.cancel)
        composeTestRule.onNodeWithText(cancelTest).performClick()
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(com.example.cupcake.R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }

    private fun navigateToFlavorScreen() {
        composeTestRule.onNodeWithStringId(com.example.cupcake.R.string.one_cupcake)
            .performClick()
        composeTestRule.onNodeWithStringId(com.example.cupcake.R.string.chocolate)
            .performClick()
    }

    private fun navigateToDateScreen() {
        navigateToFlavorScreen()
        composeTestRule.onNodeWithStringId(com.example.cupcake.R.string.next)
            .performClick()
        composeTestRule.onNodeWithText(getFormattedDate())
            .performClick()
    }

    private fun navigateToSummaryScreen() {
        navigateToDateScreen()
        composeTestRule.onNodeWithStringId(com.example.cupcake.R.string.next)
            .performClick()
    }

    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(java.util.Calendar.DATE, 1)
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        return formatter.format(calendar.time)
    }
}

