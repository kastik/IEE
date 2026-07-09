package com.kastik.apps.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.kastik.apps.feature.announcement.navigation.announcementScreen
import com.kastik.apps.feature.announcement.navigation.navigateToAnnouncement
import com.kastik.apps.feature.auth.navigation.authenticationScreen
import com.kastik.apps.feature.home.navigation.HomeRoute
import com.kastik.apps.feature.home.navigation.homeScreen
import com.kastik.apps.feature.home.navigation.navigateToHome
import com.kastik.apps.feature.licenses.navigation.licenseScreen
import com.kastik.apps.feature.licenses.navigation.navigateToLicences
import com.kastik.apps.feature.onboarding.navigation.OnboardRoute
import com.kastik.apps.feature.onboarding.navigation.onboardScreen
import com.kastik.apps.feature.profile.navigation.navigateToProfile
import com.kastik.apps.feature.profile.navigation.profileScreen
import com.kastik.apps.feature.search.navigation.navigateToSearch
import com.kastik.apps.feature.search.navigation.searchScreen
import com.kastik.apps.feature.settings.navigation.navigateToSettings
import com.kastik.apps.feature.settings.navigation.settingsScreen

@Composable
fun IeeNavHost(
    hasFinishedOnboarding: Boolean,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (!hasFinishedOnboarding) OnboardRoute else HomeRoute(),
    ) {

        onboardScreen(
            onFinish = {
                navController.navigateToHome(
                    navOptions = navOptions {
                        popUpTo<OnboardRoute> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                )
            }
        )

        homeScreen(
            navigateToSearch = navController::navigateToSearch,
            navigateToAnnouncement = navController::navigateToAnnouncement,
            navigateToSettings = navController::navigateToSettings,
            navigateToProfile = navController::navigateToProfile,
        )

        searchScreen(
            navigateBack = navController::popBackStack,
            navigateToAnnouncement = navController::navigateToAnnouncement,
        )

        authenticationScreen(navigateBack = navController::popBackStack)

        settingsScreen(navigateToLicenses = navController::navigateToLicences)

        profileScreen(navigateBack = navController::popBackStack)

        announcementScreen(navigateBack = navController::popBackStack)

        licenseScreen()

    }
}