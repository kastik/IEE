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
            navDeepLink {
                uriPattern =
                    "com.kastik.apps://auth?code={code}&state={state}&error={error}&error_description={error_description}"
            }),
        enterTransition = { scaleIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() }) { backStackEntry ->
        val args = backStackEntry.toRoute<AuthRoute>()
        AuthenticationRoute(
            code = args.code,
            state = args.state,
            error = args.error,
            errorDescription = args.error_description,
            navigateBack = navigateBack,
        )
    }
}