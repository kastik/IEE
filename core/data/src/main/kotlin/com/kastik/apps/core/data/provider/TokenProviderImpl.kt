package com.kastik.apps.core.data.provider

import com.kastik.apps.core.common.di.ApplicationScope
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.network.interceptor.TokenProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProviderImpl @Inject constructor(
    @ApplicationScope appScope: CoroutineScope,
    localDataSource: AuthenticationLocalDataSource,
) : TokenProvider {
    override val token: StateFlow<String?> =
        localDataSource.getAboardAccessToken()
            .distinctUntilChanged()
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )
}