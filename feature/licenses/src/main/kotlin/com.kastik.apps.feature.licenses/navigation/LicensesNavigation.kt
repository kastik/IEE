package com.kastik.apps.feature.licenses.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kastik.apps.feature.licenses.LicensesRoute
import kotlinx.serialization.Serializable


@Serializable
object LicensesRoute

fun NavController.navigateToLicences(
    navOptions: NavOptions,
) = navigate(route = LicensesRoute, navOptions)


fun NavGraphBuilder.licenseScreen(
) {
    composable<LicensesRoute>(
        enterTransition = { scaleIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() }) { backStackEntry ->
        LicensesRoute()
    }
}