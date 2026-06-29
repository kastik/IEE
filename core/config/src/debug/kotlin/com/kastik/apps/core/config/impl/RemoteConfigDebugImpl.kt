package com.kastik.apps.core.config.impl


import android.util.Log
import com.kastik.apps.core.config.RemoteConfig
import javax.inject.Inject

internal class RemoteConfigDebugImpl @Inject constructor(
) : RemoteConfig {

    override suspend fun refresh(){
        Log.d("RemoteConfig", "Refresh called")
    }
}