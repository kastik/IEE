package com.kastik.apps.core.ui.extensions

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

@Composable
fun TrackAnnouncementOpened(announcementId: Int) {
    val analytics = LocalAnalytics.current
    DisposableEffect(Unit) {
        analytics.logEvent(
            name = "opened_announcement",
            params = mapOf("announcement_id" to announcementId)
        )
        onDispose {}
    }
}

val LocalAnalytics = staticCompositionLocalOf<Analytics> {
    throw Exception("Not provided")
}