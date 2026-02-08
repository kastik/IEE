package com.kastik.apps.core.crashlytics

interface Crashlytics {
    fun log(message: String)
    fun recordException(throwable: Throwable)
    fun setCustomKey(key: String, value: String)
}