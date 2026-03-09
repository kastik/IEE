package com.kastik.apps.feature.announcement.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.kastik.apps.feature.announcement.AnnouncementRoute
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementRoute(
    val id: Int
)

fun NavController.navigateToAnnouncement(
    announcementId: Int,
    navOptions: NavOptions = navOptions {
        launchSingleTop = true
    },
) = navigate(route = AnnouncementRoute(announcementId), navOptions)

fun NavGraphBuilder.announcementScreen(
    navigateBack: () -> Unit
) {
    composable<AnnouncementRoute>(
        deepLinks = listOf(
            navDeepLink<AnnouncementRoute>(
                basePath = "com.kastik.apps://announcement"
            ),
            navDeepLink<AnnouncementRoute>(
                basePath = "https://aboard.iee.ihu.gr/announcements"
            )
        ),
        enterTransition = { scaleIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() }) { backStackEntry ->
        val args = backStackEntry.toRoute<AnnouncementRoute>()
        AnnouncementRoute(
            navigateBack = navigateBack,
            announcementId = args.id,
        )
    }
}




