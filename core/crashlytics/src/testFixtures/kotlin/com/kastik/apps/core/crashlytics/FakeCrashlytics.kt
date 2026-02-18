package com.kastik.apps.core.crashlytics

class FakeCrashlytics : Crashlytics {

    override fun log(message: String) = Unit

    override fun recordException(throwable: Throwable) = Unit

    override fun setCustomKey(key: String, value: String) = Unit

}