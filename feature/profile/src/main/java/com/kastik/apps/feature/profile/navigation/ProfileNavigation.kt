package com.kastik.apps.feature.profile.navigation

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kastik.apps.feature.profile.ProfileRoute
import kotlinx.serialization.Serializable

@Serializable
object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions) =
    navigate(route = ProfileRoute, navOptions)

fun NavGraphBuilder.profileScreen(
    navigateBack: () -> Unit
) {
    composable<ProfileRoute>(enterTransition = {
        slideInVertically(
            initialOffsetY = { it },
        )
    }, exitTransition = {
        slideOutVertically(
            targetOffsetY = { it },
        )
    }) {
        ProfileRoute(navigateBack = navigateBack)
    }
}