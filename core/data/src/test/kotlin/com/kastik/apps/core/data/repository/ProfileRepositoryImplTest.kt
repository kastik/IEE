package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.crashlytics.FakeCrashlytics
import com.kastik.apps.core.data.mappers.toProfile
import com.kastik.apps.core.data.mappers.toProfileProto
import com.kastik.apps.core.datastore.datasource.FakeProfileLocalDataSource
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.testdata.userProfileProtoTestData
import com.kastik.apps.core.network.datasource.FakeProfileRemoteDataSource
import com.kastik.apps.core.network.testdata.userProfileDtoTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProfileRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val profileLocalDataSource = FakeProfileLocalDataSource()
    private val profileRemoteDataSource = FakeProfileRemoteDataSource()

    private val profileRepository = ProfileRepositoryImpl(
        crashlytics = FakeCrashlytics(),
        profileLocalDataSource = profileLocalDataSource,
        profileRemoteDataSource = profileRemoteDataSource,
        ioDispatcher = testDispatcher,
    )

    //TODO Consider if we need to throw here instead
    @Test
    fun getProfileReturnsEmptyWhenNoProfileSaved() = runTest(testDispatcher) {
        val result = profileRepository.getProfile().first()
        val emptyProfile = ProfileProto.getDefaultInstance().toProfile()
        assertThat(result).isEqualTo(emptyProfile)
    }

    @Test
    fun getProfileReturnsProfileWhenProfileSaved() = runTest(testDispatcher) {
        val profile = userProfileProtoTestData.first()
        profileLocalDataSource.setProfile(profile)

        val result = profileRepository.getProfile().first()
        assertThat(result).isEqualTo(profile.toProfile())
    }

    @Test
    fun refreshProfileRefreshesProfile() = runTest(testDispatcher) {
        val freshRemoteProfile = userProfileDtoTestData.last()
        profileRemoteDataSource.profileToReturn = freshRemoteProfile

        val staleLocalProfile = userProfileProtoTestData.first()
        profileLocalDataSource.setProfile(staleLocalProfile)


        profileRepository.refreshProfile()


        val currentLocalProfile = profileLocalDataSource.getProfile().first()


        assertThat(currentLocalProfile.toProfile()).isEqualTo(
            freshRemoteProfile.toProfileProto().toProfile()
        )
        assertThat(currentLocalProfile.toProfile()).isNotEqualTo(staleLocalProfile.toProfile())
    }

    @Test
    fun clearLocalDataClearsProfileAndSubscriptions() = runTest(testDispatcher) {
        profileLocalDataSource.setProfile(userProfileProtoTestData.first())
        profileRepository.clearLocalData()
        val profileResult = profileRepository.getProfile().first()
        assertThat(profileResult.id).isEqualTo(0)

    }


}

