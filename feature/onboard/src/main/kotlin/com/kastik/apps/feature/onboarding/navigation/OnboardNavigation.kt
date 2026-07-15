package com.kastik.apps.feature.onboarding.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.kastik.apps.feature.onboarding.OnboardRoute
import kotlinx.serialization.Serializable


@Serializable
data object OnboardRoute

fun NavController.navigateToOnboard(
    navOptions: NavOptions = navOptions {
        launchSingleTop = true
    },
) = navigate(route = OnboardRoute, navOptions)


fun NavGraphBuilder.onboardScreen(
    onFinish: () -> Unit
) {
    composable<OnboardRoute>(
        enterTransition = { scaleIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() }) { backStackEntry ->
        OnboardRoute(
            onFinish = onFinish
        )
    }
}