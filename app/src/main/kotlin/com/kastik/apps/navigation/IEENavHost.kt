package com.kastik.apps.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
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
import kotlinx.collections.immutable.ImmutableList

@Composable
fun IEENavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        homeScreen(
            navigateToSearch = { query: String, tags: ImmutableList<Int>, authors: ImmutableList<Int> ->
                navController.navigateToSearch(query = query, tags = tags, authors = authors)
            },
            navigateToAnnouncement = { navController.navigateToAnnouncement(it) },
            navigateToSettings = { navController.navigateToSettings() },
            navigateToProfile = { navController.navigateToProfile() },
        )

        searchScreen(
            navigateBack = { navController.popBackStack() },
            navigateToAnnouncement = { navController.navigateToAnnouncement(it) },
        )

        authenticationScreen({ navController.popBackStack() })

        settingsScreen(navigateToLicenses = { navController.navigateToLicences() })

        profileScreen({ navController.popBackStack() })

        announcementScreen({ navController.popBackStack() })

        licenseScreen()

    }
}