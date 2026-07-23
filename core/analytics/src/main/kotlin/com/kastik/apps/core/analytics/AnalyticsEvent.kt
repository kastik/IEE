package com.kastik.apps.core.analytics

data class AnalyticsEvent(
    val type: String,
    val extras: List<Param> = emptyList(),
) {
    data class Param(val key: String, val value: Any)
}
