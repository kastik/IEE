package com.kastik.apps.feature.settings.navigation

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.kastik.apps.feature.settings.SettingsRoute
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

fun NavController.navigateToSettings(
    navOptions: NavOptions = navOptions {
        launchSingleTop = true
    }
) = navigate(route = SettingsRoute, navOptions)

fun NavGraphBuilder.settingsScreen(
    navigateToLicenses: () -> Unit,
) {
    composable<SettingsRoute>(enterTransition = {
        slideInVertically(
            initialOffsetY = { it },
        )
    }, exitTransition = {
        slideOutVertically(
            targetOffsetY = { it },
        )
    }) {
        SettingsRoute(
            navigateToLicenses = navigateToLicenses,
        )
    }
}
