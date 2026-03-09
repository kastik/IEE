package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toLocalError
import com.kastik.apps.core.data.mappers.toNetworkError
import com.kastik.apps.core.data.mappers.toProfile
import com.kastik.apps.core.data.mappers.toProfileProto
import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
internal class ProfileRepositoryImpl @Inject constructor(
    private val crashlytics: Crashlytics,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ProfileRepository {

    override fun getProfile(): Flow<Profile> {
        return profileLocalDataSource.getProfile().map { profile -> profile.toProfile() }
    }

    override suspend fun refreshProfile() = withContext(ioDispatcher) {
        try {
            val userProfile = profileRemoteDataSource.getProfile()
            profileLocalDataSource.setProfile(userProfile.toProfileProto())
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toNetworkError())
        }

    }


    override suspend fun clearLocalData() = withContext(ioDispatcher) {
        try {
            profileLocalDataSource.clearProfile()
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toLocalError())
        }
    }
}