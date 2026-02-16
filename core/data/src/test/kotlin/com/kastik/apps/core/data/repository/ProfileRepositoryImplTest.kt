package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.crashlytics.FakeCrashlytics
import com.kastik.apps.core.data.mappers.toProfile
import com.kastik.apps.core.data.mappers.toProfileProto
import com.kastik.apps.core.datastore.datasource.FakeProfileLocalDataSource
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.testdata.subscribedTagProtoTestData
import com.kastik.apps.core.datastore.testdata.userProfileProtoTestData
import com.kastik.apps.core.network.datasource.FakeProfileRemoteDataSource
import com.kastik.apps.core.network.testdata.userProfileDtoTestData
import com.kastik.apps.core.notifications.PushNotificationsDatasource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockkClass
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProfileRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val profileLocalDataSource = FakeProfileLocalDataSource()
    private val profileRemoteDataSource = FakeProfileRemoteDataSource()
    private val pushNotificationsDatasource = mockkClass(PushNotificationsDatasource::class)

    private val profileRepository = ProfileRepositoryImpl(
        crashlytics = FakeCrashlytics(),
        profileLocalDataSource = profileLocalDataSource,
        profileRemoteDataSource = profileRemoteDataSource,
        pushNotificationsDatasource = pushNotificationsDatasource,
        ioDispatcher = testDispatcher,
    )

    @Before
    fun setup() {
        coEvery { pushNotificationsDatasource.subscribeToTopics(any()) } just Runs
        coEvery { pushNotificationsDatasource.unsubscribeFromTopics(any()) } just Runs
        coEvery { pushNotificationsDatasource.unsubscribeFromAllTopics() } just Runs
    }

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
    fun getEmailSubscriptionsAreEmptyWhenNotSet() = runTest(testDispatcher) {
        val result = profileRepository.getEmailSubscriptions().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun getEmailSubscriptionsReturnsSubscribedTagsWhenSet() = runTest(testDispatcher) {
        val tags = subscribedTagProtoTestData
        profileLocalDataSource.setTagSubscriptions(tags)
        val result = profileRepository.getEmailSubscriptions().first()
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(tags.size)

        tags.zip(result).forEach { (tag, domain) ->
            assertThat(domain.id).isEqualTo(tag.id)
            assertThat(domain.title).isEqualTo(tag.title)
        }
    }

    /**
     * TODO Prompt Engineered
     */

    @Test
    fun `refreshEmailSubscriptionsFetchesFromRemoteAnd savesToLocal`() = runTest(testDispatcher) {
        profileRepository.refreshEmailSubscriptions()

        val result = profileRepository.getEmailSubscriptions().first()
        assertThat(result).isNotEmpty()
    }

    @Test
    fun subscribeToTopicsCallsPushDatasource() = runTest(testDispatcher) {
        val tags = listOf(1, 2, 3)

        profileRepository.syncTopicSubscriptions()

        coVerify { pushNotificationsDatasource.subscribeToTopics(tags) }
    }

    @Test
    fun unsubscribeFromAllTopicsCallsPushDatasource() = runTest(testDispatcher) {
        profileRepository.unsubscribeFromAllTopics()

        coVerify { pushNotificationsDatasource.unsubscribeFromAllTopics() }
    }

    @Test
    fun clearLocalDataClearsProfileAndSubscriptions() = runTest(testDispatcher) {
        profileLocalDataSource.setProfile(userProfileProtoTestData.first())
        profileLocalDataSource.setTagSubscriptions(subscribedTagProtoTestData)

        profileRepository.clearLocalData()

        val profileResult = profileRepository.getProfile().first()
        val subsResult = profileRepository.getEmailSubscriptions().first()

        assertThat(profileResult.id).isEqualTo(0)
        assertThat(subsResult).isEmpty()
    }

    @Test
    fun subscribeToEmailTagsDelegatesToRemote() = runTest(testDispatcher) {
        val tags = listOf(10, 20)

        profileRepository.subscribeToEmailTags(tags)
        //TODO
    }

}

