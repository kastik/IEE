package com.kastik.apps.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

internal class AnalyticsReleaseImpl @Inject constructor(
    private val analytics: FirebaseAnalytics
) : Analytics {

    override fun logEvent(name: String, params: Map<String, Any?>) {
        analytics.logEvent(name) {
            params.forEach { (key, value) ->
                when (value) {
                    null -> Unit
                    is String -> param(key, value)
                    is Long -> param(key, value)
                    is Int -> param(key, value.toLong())
                    is Float -> param(key, value.toDouble())
                    is Double -> param(key, value)
                    is Boolean -> param(key, if (value) 1L else 0L)
                }
            }
        }
    }

    override fun setUserProperty(name: String, value: String?) =
        analytics.setUserProperty(name, value)

    override fun logScreenView(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
    }
}