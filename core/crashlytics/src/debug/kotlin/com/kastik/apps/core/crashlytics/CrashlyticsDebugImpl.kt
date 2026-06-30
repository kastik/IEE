package com.kastik.apps.core.crashlytics

import android.util.Log
import javax.inject.Inject

class CrashlyticsDebugImpl @Inject constructor() : Crashlytics {
    override fun log(message: String) {
        Log.e("Crashlytics", message)
    }

    override fun recordException(throwable: Throwable) {
        Log.e("Crashlytics", "Non-fatal exception recorded", throwable)
    }

    override fun setCustomKey(key: String, value: String) {
        Log.e("Crashlytics", "Key set: $key = $value")
    }
}