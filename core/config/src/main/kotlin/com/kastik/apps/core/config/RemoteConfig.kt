package com.kastik.apps.core.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FirebaseConfigRepository @Inject constructor(
    private val crashlytics: Crashlytics,
    private val remoteConfig: FirebaseRemoteConfig
) : RemoteConfigRepository {

    companion object {
        private const val FCM_ENABLED = "fcm_enabled"
        private const val AUTHENTICATOR_ENABLED = "authenticator_enabled"
    }


    override fun isFcmEnabled() = remoteConfig.getBoolean(FCM_ENABLED)

    override fun isAuthenticatorEnabled() = remoteConfig.getBoolean(AUTHENTICATOR_ENABLED)

    override suspend fun fetchAndActivate(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (e: Exception) {
            crashlytics.recordException(e)
            false
        }
    }
}