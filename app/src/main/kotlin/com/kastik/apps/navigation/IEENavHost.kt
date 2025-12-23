package com.kastik.apps.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.kastik.apps.feature.announcement.navigation.announcementScreen
import com.kastik.apps.feature.announcement.navigation.navigateToAnnouncement
import com.kastik.apps.feature.auth.navigation.authenticationScreen
import com.kastik.apps.feature.home.navigation.HomeRoute
import com.kastik.apps.feature.home.navigation.homeScreen
import com.kastik.apps.feature.licenses.navigation.licenseScreen
import com.kastik.apps.feature.licenses.navigation.navigateToLicences
import com.kastik.apps.feature.profile.navigation.navigateToProfile
import com.kastik.apps.feature.profile.navigation.profileScreen
import com.kastik.apps.feature.search.navigation.navigateToSearch
import com.kastik.apps.feature.search.navigation.searchScreen
import com.kastik.apps.feature.settings.navigation.navigateToSettings
import com.kastik.apps.feature.settings.navigation.settingsScreen

@Composable
fun IEENavHost() {
    val navController = rememberNavController()

    fun getTopLevelNavOptions() = navOptions {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        homeScreen(
            navigateToSearch = { query: String, tags: List<Int>, authors: List<Int> ->
                navController.navigateToSearch(
                    navOptions = getTopLevelNavOptions(),
                    query = query,
                    tags = tags,
                    authors = authors
                )
            },
            navigateToAnnouncement = {
                navController.navigateToAnnouncement(
                    getTopLevelNavOptions(), it
                )
            },
            navigateToSettings = { navController.navigateToSettings(navOptions = getTopLevelNavOptions()) },
            navigateToProfile = { navController.navigateToProfile(navOptions = getTopLevelNavOptions()) },
        )

        searchScreen(
            navigateBack = {
                navController.navigate(HomeRoute) {
                    popUpTo(HomeRoute) {
                        inclusive = true
                    }
                }
            },
            navigateToAnnouncement = {
                navController.navigateToAnnouncement(
                    getTopLevelNavOptions(), it
                )
            },
        )



        authenticationScreen({ navController.popBackStack() })

        settingsScreen(navigateToLicenses = { navController.navigateToLicences(navOptions = getTopLevelNavOptions()) })

        profileScreen({ navController.popBackStack() })

        announcementScreen({ navController.popBackStack() })

        licenseScreen()

    }
}