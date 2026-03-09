package com.kastik.apps.feature.auth.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.kastik.apps.feature.auth.AuthenticationRoute
import kotlinx.serialization.Serializable


@Serializable
data class AuthRoute(
    val code: String? = null,
    val state: String? = null,
    val error: String? = null,
    val error_description: String? = null
)

fun NavController.navigateToAuthentication(
    navOptions: NavOptions,
) = navigate(route = AuthRoute(), navOptions)

fun NavGraphBuilder.authenticationScreen(
    navigateBack: () -> Unit
) {
    composable<AuthRoute>(
        deepLinks = listOf(
            navDeepLink<AuthRoute>(
                basePath = "com.kastik.apps://auth"
            )
        ),
        enterTransition = { scaleIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() }) { backStackEntry ->
        val _ = backStackEntry.toRoute<AuthRoute>()
        AuthenticationRoute(
            navigateBack = navigateBack,
        )
    }
}