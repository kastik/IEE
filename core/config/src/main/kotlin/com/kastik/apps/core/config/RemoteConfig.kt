package com.kastik.apps.core.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FirebaseConfigRepository @Inject constructor(
    private val crashlytics: Crashlytics,
    private val remoteConfig: FirebaseRemoteConfig,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RemoteConfigRepository {

    companion object {
        private const val FCM_ENABLED = "fcm_enabled"
        private const val AUTHENTICATOR_ENABLED = "authenticator_enabled"
    }


    override fun isFcmEnabled() = remoteConfig.getBoolean(FCM_ENABLED)

    override fun isAuthenticatorEnabled() = remoteConfig.getBoolean(AUTHENTICATOR_ENABLED)

    //TODO Covert this to a WM sync request like NIA
    override suspend fun fetchAndActivate(): Unit = withContext(ioDispatcher) {
        try {
            remoteConfig.fetchAndActivate()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
        }
    }
}