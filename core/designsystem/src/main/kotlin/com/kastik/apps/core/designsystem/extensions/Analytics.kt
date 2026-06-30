package com.kastik.apps.core.designsystem.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.analytics.AnalyticsEvent
import com.kastik.apps.core.analytics.AnalyticsEvent.Param

@Composable
fun TrackScreenViewEvent(
    screenClass: String,
    screenName: String,
    params: List<Param> = emptyList(),
) {
    val analytics = LocalAnalytics.current
    LaunchedEffect(screenClass, screenName, params) {
        analytics.logScreenView(
            screenClass = screenClass,
            screenName = screenName,
            params = params
        )
    }
}

fun Analytics.logScreenView(
    screenClass: String,
    screenName: String,
    params: List<Param> = emptyList()
) {
    logEvent(
        AnalyticsEvent(
            type = types.SCREEN_VIEW,
            extras = listOf(
                Param(paramKeys.SCREEN_CLASS, screenClass),
                Param(paramKeys.SCREEN_NAME, screenName),
            ) + params,
        ),
    )
}

val LocalAnalytics = staticCompositionLocalOf<Analytics> {
    error("Analytics instance not provided")
}