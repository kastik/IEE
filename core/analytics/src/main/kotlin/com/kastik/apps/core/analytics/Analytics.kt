package com.kastik.apps.core.analytics

interface Analytics {
    fun logEvent(name: String, params: Map<String, Any?> = emptyMap())
    fun logScreenView(screenName: String, params: Map<String, Any?> = emptyMap())
    fun setUserProperty(name: String, value: String?)
}