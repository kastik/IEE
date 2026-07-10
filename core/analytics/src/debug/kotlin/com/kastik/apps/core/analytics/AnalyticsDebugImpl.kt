package com.kastik.apps.core.analytics

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class AnalyticsDebugImpl @Inject constructor(
    override val types: AnalyticsEventTypes,
    override val paramKeys: AnalyticsParamKeys,
) : Analytics {

    override fun setUserId(userId: String?) {
        Log.d("Analytics", "setUserId:$userId")
    }

    override fun setUserProperty(propertyName: String, value: String?) {
        Log.d("Analytics", "propertyName:$propertyName, value:$value")
    }

    override fun logEvent(event: AnalyticsEvent) {
        Log.d("Analytics", "logging event $event")
    }

}
