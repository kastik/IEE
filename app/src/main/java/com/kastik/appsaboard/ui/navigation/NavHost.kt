package com.kastik.appsaboard.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.kastik.appsaboard.ui.screens.auth.AuthenticationScreen
import com.kastik.appsaboard.ui.screens.home.HomeScreen
import com.kastik.appsaboard.ui.theme.AppsAboardTheme

@Composable
fun NavHost() {
    val navController = rememberNavController()
    AppsAboardTheme {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
        ) {
            composable<HomeRoute>(
                enterTransition = { scaleIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() }
            ) { backStackEntry ->
                HomeScreen()
            }
            composable<AuthRoute>(
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern =
                            "com.kastik.apps://auth?code={code}&state={state}&error={error}&error_description={error_description}"
                    }
                ),
                enterTransition = { scaleIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() }
            ) { backStackEntry ->
                val args = backStackEntry.toRoute<AuthRoute>()
                AuthenticationScreen(
                    navigateBack = { navController.popBackStack() },
                    arguments = args
                )
            }
            composable<SettingsRoute>(
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { it },
                    )
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { it },
                    )
                }
            ) {
            }
        }
    }
}