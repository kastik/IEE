package com.kastik.apps.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kastik.apps.HiltComponentActivity
import com.kastik.apps.feature.home.navigation.HomeRoute
import com.kastik.apps.feature.onboarding.navigation.OnboardRoute
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class IeeNavHostTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        // 4. Initialize Hilt before each test
        hiltRule.inject()
    }

    /**
     * Helper function to initialize the NavHost with a specific onboarding state.
     */
    private fun setupIeeNavHost(hasFinishedOnboarding: Boolean) {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            IeeNavHost(
                hasFinishedOnboarding = hasFinishedOnboarding,
                navController = navController
            )
        }
    }

    @Test
    fun startDestination_isOnboarding_whenNotFinished() {
        // Arrange & Act
        setupIeeNavHost(hasFinishedOnboarding = false)

        // Assert
        // If using Navigation 2.8+ Type-Safe routes, the route string contains the fully qualified class name.
        val currentRoute = navController.currentDestination?.route
        assertTrue(
            "Expected start destination to be OnboardRoute, but was $currentRoute",
            currentRoute?.contains(OnboardRoute::class.simpleName.toString()) == true
        )
    }

    @Test
    fun startDestination_isHome_whenOnboardingFinished() {
        // Arrange & Act
        setupIeeNavHost(hasFinishedOnboarding = true)

        // Assert
        val currentRoute = navController.currentDestination?.route
        assertTrue(
            "Expected start destination to be HomeRoute, but was $currentRoute",
            currentRoute?.contains(HomeRoute::class.simpleName.toString()) == true
        )
    }

    @Test
    fun onboarding_onFinish_navigatesToHome_andClearsBackstack() {
        // Arrange
        setupIeeNavHost(hasFinishedOnboarding = false)

        // Act
        // NOTE: Replace "Finish" with the actual text, contentDescription, or testTag
        // of the button inside your `onboardScreen` that triggers `onFinish`.
        composeTestRule.onNodeWithText("Finish").performClick()

        // Assert 1: We arrived at the Home Route
        val currentRoute = navController.currentDestination?.route
        assertTrue(
            "Expected to navigate to HomeRoute, but was $currentRoute",
            currentRoute?.contains(HomeRoute::class.simpleName.toString()) == true
        )

        // Assert 2: OnboardRoute is popped from the backstack (inclusive = true)
        // Fix: Use currentBackStack.value instead of the restricted backQueue
        val backStackHasOnboarding = navController.currentBackStack.value.any { backStackEntry ->
            backStackEntry.destination.route?.contains(OnboardRoute::class.simpleName.toString()) == true
        }

        assertFalse(
            "OnboardRoute should have been cleared from the backstack",
            backStackHasOnboarding
        )
    }

    @Test
    fun home_navigateToSearch_worksCorrectly() {
        // Arrange
        setupIeeNavHost(hasFinishedOnboarding = true)

        // Act
        // NOTE: Replace "Search" with the actual testTag or text of the search icon/button in Home.
        composeTestRule.onNodeWithText("Search").performClick()

        // Assert
        val currentRoute = navController.currentDestination?.route
        assertTrue(
            "Expected to navigate to SearchRoute, but was $currentRoute",
            currentRoute?.contains("SearchRoute") == true // Adjust to match your Search route name
        )
    }
}