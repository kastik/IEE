package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.crashlytics.FakeCrashlytics
import com.kastik.apps.core.data.mappers.toProfile
import com.kastik.apps.core.datastore.datasource.FakeAuthenticationLocalDataSource
import com.kastik.apps.core.datastore.datasource.FakeProfileLocalDataSource
import com.kastik.apps.core.datastore.testdata.baseProfileProto
import com.kastik.apps.core.network.datasource.FakeProfileRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProfileRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeCrashlytics = FakeCrashlytics()
    private val profileLocalDataSource = FakeProfileLocalDataSource()
    private val profileRemoteDataSource = FakeProfileRemoteDataSource()
    private val fakeAuthenticationLocalDataSource = FakeAuthenticationLocalDataSource()

    private val profileRepository = ProfileRepositoryImpl(
        crashlytics = fakeCrashlytics,
        profileLocalDataSource = profileLocalDataSource,
        profileRemoteDataSource = profileRemoteDataSource,
        ioDispatcher = testDispatcher,
        authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
    )

//    @Test
//    fun getProfileReturnsEmptyWhenNoProfileSaved() = runTest(testDispatcher) {
//        val result = profileRepository.profile.first()
//        val emptyProfile = ProfileProto.getDefaultInstance().toProfile()
//
//        assertThat(result).isEqualTo(emptyProfile)
//    }

    @Test
    fun getProfileReturnsProfileWhenProfileSaved() = runTest(testDispatcher) {
        val profile = baseProfileProto
        profileLocalDataSource.setProfile(profile)

        val result = profileRepository.profile.first()
        assertThat(result).isEqualTo(profile.toProfile())
    }

//    @Test
//    fun syncProfileSuccessRefreshesProfile() = runTest(testDispatcher) {
//        val result = profileRepository.syncProfile()
//
//        assertThat(result).isInstanceOf(Result.Success::class.java)
//
//        val savedLocalProfile = profileLocalDataSource.profile.first()
//        assertThat(savedLocalProfile).isEqualTo(baseProfileDto.toProfileProto())
//    }
//
//    @Test
//    fun clearProfileClearsProfileAndSubscriptions() = runTest(testDispatcher) {
//        profileLocalDataSource.setProfile(baseProfileProto)
//        profileRepository.clearProfile()
//
//        val profileResult = profileRepository.profile.first()
//        assertThat(profileResult?.id).isEqualTo(0)
//
//    }

}

