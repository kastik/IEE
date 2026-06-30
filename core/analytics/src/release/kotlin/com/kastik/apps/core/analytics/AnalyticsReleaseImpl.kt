package com.kastik.apps.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject


internal class AnalyticsReleaseImpl @Inject constructor(
    private val analytics: FirebaseAnalytics,
    override val types: AnalyticsEventTypes,
    override val paramKeys: AnalyticsParamKeys
) : Analytics {

    override fun setUserId(userId: String?) =
        analytics.setUserId(userId)

    override fun setUserProperty(propertyName: String, value: String?) =
        analytics.setUserProperty(propertyName, value)

    override fun logEvent(event: AnalyticsEvent) {
        analytics.logEvent(event.type) {
            event.extras.forEach { extra ->
                val safeKey = extra.key.take(40)
                when (val value = extra.value) {
                    is String -> param(safeKey, value.take(100))
                    is Long -> param(safeKey, value)
                    is Int -> param(safeKey, value.toLong())
                    is Double -> param(safeKey, value)
                    is Float -> param(safeKey, value.toDouble())
                    is Boolean -> param(safeKey, value.toString())
                    else -> param(safeKey, value.toString().take(100))
                }
            }
        }
    }

}