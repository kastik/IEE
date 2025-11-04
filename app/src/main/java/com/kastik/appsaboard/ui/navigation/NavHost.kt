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
import androidx.navigation.toRoute
import com.kastik.appsaboard.ui.screens.home.HomeScreen
import com.kastik.appsaboard.ui.theme.AppsAboardTheme

@Composable
fun NavHost() {
    val navController = rememberNavController()
    AppsAboardTheme {
        NavHost(
            navController = navController,
            startDestination = HomeScreen,
        ) {
            composable<HomeScreen>(
                enterTransition = { scaleIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() }
            ) { backStackEntry ->
                backStackEntry.toRoute<HomeScreen>()
                HomeScreen()
            }
            composable<SettingsScreen>(
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