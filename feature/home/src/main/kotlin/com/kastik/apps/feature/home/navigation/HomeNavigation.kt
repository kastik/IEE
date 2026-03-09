package com.kastik.apps.feature.home.navigation

import androidx.annotation.Keep
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
import com.kastik.apps.feature.home.HomeScreenRoute
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class HomeTab {
    HOME,
    FOR_YOU
}

@Serializable
data class HomeRoute(
    val initialTab: HomeTab? = null
)

fun NavController.navigateToHome(
    navOptions: NavOptions = navOptions {
        popUpTo(HomeRoute) {
            inclusive = false
            saveState = true
        }
        restoreState = true
        launchSingleTop = true
    },
) = navigate(route = HomeRoute, navOptions)


fun NavGraphBuilder.homeScreen(
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch: (query: String, tagsId: ImmutableList<Int>, authorIds: ImmutableList<Int>) -> Unit,
) {
    composable<HomeRoute>(
        deepLinks = listOf(
            navDeepLink<HomeRoute>(
                basePath = "com.kastik.apps://home"
            )
        ),
        enterTransition = { scaleIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() }) { backStackEntry ->
        val route: HomeRoute = backStackEntry.toRoute()
        HomeScreenRoute(
            initialTab = route.initialTab ?: HomeTab.HOME,
            navigateToAnnouncement = navigateToAnnouncement,
            navigateToSettings = navigateToSettings,
            navigateToProfile = navigateToProfile,
            navigateToSearch = navigateToSearch,
        )
    }
}