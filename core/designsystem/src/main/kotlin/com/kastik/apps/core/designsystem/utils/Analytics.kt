package com.kastik.apps.core.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.kastik.apps.core.analytics.Analytics


@Composable
fun TrackScreenViewEvent(screenName: String) {
    val analytics = LocalAnalytics.current

    DisposableEffect(Unit) {
        analytics.logScreenView(screenName)
        onDispose {}
    }
}

val LocalAnalytics = staticCompositionLocalOf<Analytics> {
    throw Exception("Not provided")
}