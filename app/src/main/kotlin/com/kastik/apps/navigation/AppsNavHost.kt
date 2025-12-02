package com.kastik.apps.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.kastik.apps.feature.announcement.navigation.announcementScreen
import com.kastik.apps.feature.announcement.navigation.navigateToAnnouncement
import com.kastik.apps.feature.auth.navigation.authenticationScreen
import com.kastik.apps.feature.home.navigation.HomeRoute
import com.kastik.apps.feature.home.navigation.homeScreen
import com.kastik.apps.feature.profile.navigation.navigateToProfile
import com.kastik.apps.feature.profile.navigation.profileScreen
import com.kastik.apps.feature.search.navigation.navigateToSearch
import com.kastik.apps.feature.search.navigation.searchScreen
import com.kastik.apps.feature.settings.navigation.navigateToSettings
import com.kastik.apps.feature.settings.navigation.settingsScreen

@Composable
fun AppsNavHost() {
    val navController = rememberNavController()
    val topLevelNavOptions = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        //popUpTo(navController.graph.findStartDestination().id) {
        //    saveState = true
        //}
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        homeScreen(
            navigateToSearch = { navController.navigateToSearch(navOptions = topLevelNavOptions) },
            navigateToAnnouncement = {
                navController.navigateToAnnouncement(
                    topLevelNavOptions,
                    it
                )
            },
            navigateToSettings = { navController.navigateToSettings(navOptions = topLevelNavOptions) },
            navigateToProfile = { navController.navigateToProfile(navOptions = topLevelNavOptions) }
        )

        searchScreen(
            navigateBack = { navController.popBackStack() },
            navigateToAnnouncement = {
                navController.navigateToAnnouncement(
                    topLevelNavOptions,
                    it
                )
            }
        )

        authenticationScreen({ navController.popBackStack() })

        settingsScreen()

        profileScreen({ navController.popBackStack() })

        announcementScreen({ navController.popBackStack() })

    }
}