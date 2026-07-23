package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toNetworkError
import com.kastik.apps.core.data.mappers.toProfile
import com.kastik.apps.core.data.mappers.toProfileProto
import com.kastik.apps.core.data.utils.safeCall
import com.kastik.apps.core.datastore.datasource.AuthenticationLocalDataSource
import com.kastik.apps.core.datastore.datasource.ProfileLocalDataSource
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileRepositoryImpl
@Inject
constructor(
    private val crashlytics: Crashlytics,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ProfileRepository {

    override val profile: Flow<Profile?> =
        profileLocalDataSource.profile.map { profile -> profile?.toProfile() }

    override suspend fun syncProfile() =
        withContext(ioDispatcher) {
            if (!authenticationLocalDataSource.isSignedIn.first()) {
                return@withContext Result.Success(Unit)
            }

            safeCall(
                mapException = Exception::toNetworkError,
                recordException = crashlytics::recordException,
            ) {
                val userProfile = profileRemoteDataSource.getProfile()
                profileLocalDataSource.setProfile(userProfile.toProfileProto())
            }
        }

    override suspend fun clearProfile() =
        withContext(ioDispatcher) {
            profileLocalDataSource.clearProfile()
        }
}
