package com.kastik.apps.core.analytics

object NoOpAnalytics : Analytics {
    override val types: AnalyticsEventTypes = EmptyAnalyticsEventTypes
    override val paramKeys: AnalyticsParamKeys = EmptyAnalyticsParamKeys
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun setUserId(userId: String?) = Unit
    override fun setUserProperty(propertyName: String, value: String?) = Unit
}