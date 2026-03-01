package com.kastik.apps.core.analytics

import android.util.Log
import javax.inject.Inject

internal class AnalyticsDebugImpl @Inject constructor() : Analytics {

    override fun logEvent(name: String, params: Map<String, Any?>) {
        Log.d("Analytics", "logEvent($name, $params)")
    }

    override fun setUserProperty(name: String, value: String?) {
        Log.d("Analytics", "setUserProperty($name, $value)")
    }

    override fun logScreenView(screenName: String, params: Map<String, Any?>) {
        Log.d("Analytics", "logScreenView($screenName, $params)")
    }
}
