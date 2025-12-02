package com.kastik.apps.feature.search.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kastik.apps.feature.search.SearchScreen
import kotlinx.serialization.Serializable

@Serializable
object SearchRoute

fun NavController.navigateToSearch(
    navOptions: NavOptions,
) = navigate(route = SearchRoute, navOptions)


fun NavGraphBuilder.searchScreen(
    navigateBack: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
) {
    composable<SearchRoute>(
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
    ) {
        SearchScreen(
            navigateBack = navigateBack,
            navigateToAnnouncement = navigateToAnnouncement
        )
    }
}




