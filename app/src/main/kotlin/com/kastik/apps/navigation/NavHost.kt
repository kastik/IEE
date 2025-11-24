package com.kastik.apps.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kastik.apps.feature.announcement.AnnouncementScreen
import com.kastik.apps.feature.auth.AuthenticationScreen
import com.kastik.apps.feature.home.HomeScreen
import com.kastik.apps.feature.profile.ProfileScreen
import com.kastik.apps.feature.search.SearchScreen
import com.kastik.apps.feature.settings.SettingsScreen

@Composable
fun NavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        navigation<HomeRoute>(
            startDestination = AnnouncementListRoute,
        ) {

            composable<AnnouncementListRoute>(
                enterTransition = { scaleIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() }) { backStackEntry ->
                HomeScreen(navigateToAnnouncement = {
                    navController.navigate(AnnouncementRoute(it))
                }, navigateToSettings = {
                    navController.navigate(SettingsRoute)
                }, navigateToProfile = {
                    navController.navigate(ProfileRoute)
                }, navigateToSearch = { navController.navigate(AnnouncementSearchRoute) })
            }

            composable<AnnouncementSearchRoute>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(350)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(350)
                    )
                },
                popEnterTransition = {
                    fadeIn()
                },
                exitTransition = {
                    fadeOut()
                }
            ) { backStackEntry ->
                SearchScreen(
                    navigateBack = { navController.popBackStack() },
                    navigateToAnnouncement = {
                        navController.navigate(AnnouncementRoute(it))
                    })
            }
        }

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
            AuthenticationScreen(
                code = args.code,
                state = args.state,
                error = args.error,
                errorDescription = args.error_description,
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<SettingsRoute>(enterTransition = {
            slideInVertically(
                initialOffsetY = { it },
            )
        }, exitTransition = {
            slideOutVertically(
                targetOffsetY = { it },
            )
        }) { backStackEntry ->
            backStackEntry.toRoute<SettingsRoute>()
            SettingsScreen(

            )
        }
        composable<ProfileRoute>(enterTransition = {
            slideInVertically(
                initialOffsetY = { it },
            )
        }, exitTransition = {
            slideOutVertically(
                targetOffsetY = { it },
            )
        }) { backStackEntry ->
            backStackEntry.toRoute<SettingsRoute>()
            ProfileScreen()
        }
        composable<AnnouncementRoute>(
            enterTransition = { scaleIn() },
            exitTransition = { fadeOut() },
            popEnterTransition = { fadeIn() }) { backStackEntry ->
            val args = backStackEntry.toRoute<AnnouncementRoute>()
            AnnouncementScreen(
                navigateBack = { navController.popBackStack() },
                announcementId = args.id,
            )
        }
    }
}