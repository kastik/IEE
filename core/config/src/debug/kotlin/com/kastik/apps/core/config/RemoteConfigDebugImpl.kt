package com.kastik.apps.core.config

import android.util.Log
import javax.inject.Inject

internal class RemoteConfigDebugImpl @Inject constructor(
) : RemoteConfig {

    override suspend fun refresh() {
        Log.d("RemoteConfig", "Refresh called")
    }
}