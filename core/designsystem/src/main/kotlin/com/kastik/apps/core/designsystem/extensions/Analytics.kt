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
            params = params,
        )
    }
}

fun Analytics.logScreenView(
    screenClass: String,
    screenName: String,
    params: List<Param> = emptyList(),
) {
    logEvent(
        AnalyticsEvent(
            type = types.screenView,
            extras =
                listOf(
                    Param(paramKeys.screenClass, screenClass),
                    Param(paramKeys.screenName, screenName),
                ) + params,
        )
    )
}

val LocalAnalytics =
    staticCompositionLocalOf<Analytics> {
        error("Analytics instance not provided")
    }
