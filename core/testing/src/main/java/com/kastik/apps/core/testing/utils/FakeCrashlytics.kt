package com.kastik.apps.core.testing.utils

import com.kastik.apps.core.crashlytics.Crashlytics

class FakeCrashlytics : Crashlytics {

    override fun log(message: String) = Unit

    override fun recordException(throwable: Throwable) = Unit

    override fun setCustomKey(key: String, value: String) = Unit

}