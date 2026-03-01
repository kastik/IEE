package com.kastik.apps.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.kastik.apps.core.analytics.Analytics


@Composable
fun TrackScreenViewEvent(
    screenName: String,
    params: Map<String, Any?> = emptyMap()
) {
    val analytics = LocalAnalytics.current
    LaunchedEffect(screenName) {
        analytics.logScreenView(screenName, params)
    }
}

val LocalAnalytics = staticCompositionLocalOf<Analytics> {
    throw Exception("Not provided")
}