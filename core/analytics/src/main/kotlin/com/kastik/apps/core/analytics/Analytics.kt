package com.kastik.apps.core.analytics

interface Analytics {
    val types: AnalyticsEventTypes
    val paramKeys: AnalyticsParamKeys
    fun setUserId(userId: String?)
    fun setUserProperty(propertyName: String, value: String?)
    fun logEvent(event: AnalyticsEvent)
}