package com.kastik.apps.core.config.impl

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.config.RemoteConfig
import com.kastik.apps.core.crashlytics.Crashlytics
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RemoteConfigReleaseImpl @Inject constructor(
    private val crashlytics: Crashlytics,
    private val remoteConfig: FirebaseRemoteConfig,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RemoteConfig {

    override suspend fun refresh(): Unit = withContext(ioDispatcher) {
        try {
            remoteConfig.fetchAndActivate()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
        }
    }
}