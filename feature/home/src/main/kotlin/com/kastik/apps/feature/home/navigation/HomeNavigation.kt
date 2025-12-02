package com.kastik.apps.feature.home.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kastik.apps.feature.home.HomeScreenRoute
import kotlinx.serialization.Serializable


@Serializable
object HomeRoute

fun NavController.navigateToHome(
    navOptions: NavOptions,
) = navigate(route = HomeRoute, navOptions)


fun NavGraphBuilder.homeScreen(
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch: () -> Unit
) {
    composable<HomeRoute>(
        enterTransition = { scaleIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() }) { backStackEntry ->
        HomeScreenRoute(navigateToAnnouncement = {
            navigateToAnnouncement(it)
        }, navigateToSettings = {
            navigateToSettings()
        }, navigateToProfile = {
            navigateToProfile()
        }, navigateToSearch = { navigateToSearch() })
    }
}