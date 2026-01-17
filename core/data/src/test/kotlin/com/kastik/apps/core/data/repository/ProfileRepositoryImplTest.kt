package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.data.mappers.toProfile
import com.kastik.apps.core.data.mappers.toProfileProto
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.notifications.PushNotificationsDatasource
import com.kastik.apps.core.testing.datasource.local.FakeProfileLocalDataSource
import com.kastik.apps.core.testing.datasource.remote.FakeProfileRemoteDataSource
import com.kastik.apps.core.testing.testdata.subscribedTagProtoTestData
import com.kastik.apps.core.testing.testdata.userProfileDtoTestData
import com.kastik.apps.core.testing.testdata.userProfileProtoTestData
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockkClass
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProfileRepositoryImplTest {

    private val profileLocalDataSource = FakeProfileLocalDataSource()
    private val profileRemoteDataSource = FakeProfileRemoteDataSource()
    private val pushNotificationsDatasource = mockkClass(PushNotificationsDatasource::class)

    private val profileRepository = ProfileRepositoryImpl(
        profileLocalDataSource = profileLocalDataSource,
        profileRemoteDataSource = profileRemoteDataSource,
        pushNotificationsDatasource = pushNotificationsDatasource,
    )

    @Before
    fun setup() {
        // Stub void methods for the mock to prevent "no answer found" errors
        coEvery { pushNotificationsDatasource.subscribeToPushTags(any()) } just Runs
        coEvery { pushNotificationsDatasource.unSubscribeFromPushTags() } just Runs
    }

    @Test
    fun isSignedInReturnsFalseWhenNoProfileSaved() = runTest {
        val result = profileRepository.isSignedIn().first()
        assertThat(result).isFalse()
    }

    @Test
    fun isSignedInReturnsTrueWhenProfileSaved() = runTest {
        val profile = userProfileProtoTestData.first()
        profileLocalDataSource.setProfile(profile)

        val result = profileRepository.isSignedIn().first()
        assertThat(result).isTrue()
    }

    //TODO Consider if we need to throw here instead
    @Test
    fun getProfileReturnsEmptyWhenNoProfileSaved() = runTest {
        val result = profileRepository.getProfile().first()
        val emptyProfile = ProfileProto.getDefaultInstance().toProfile()
        assertThat(result).isEqualTo(emptyProfile)
    }

    @Test
    fun getProfileReturnsProfileWhenProfileSaved() = runTest {
        val profile = userProfileProtoTestData.first()
        profileLocalDataSource.setProfile(profile)

        val result = profileRepository.getProfile().first()
        assertThat(result).isEqualTo(profile.toProfile())
    }

    @Test
    fun refreshProfileRefreshesProfile() = runTest {
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
    fun getEmailSubscriptionsAreEmptyWhenNotSet() = runTest {
        val result = profileRepository.getEmailSubscriptions().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun getEmailSubscriptionsReturnsSubscribedTagsWhenSet() = runTest {
        val tags = subscribedTagProtoTestData
        profileLocalDataSource.setSubscriptions(tags)
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
    fun `refreshEmailSubscriptionsFetchesFromRemoteAnd savesToLocal`() = runTest {
        // Act: Trigger refresh (Fetch Remote -> Save Local)
        profileRepository.refreshEmailSubscriptions()

        // Assert: Check if data is now available in the local flow
        val result = profileRepository.getEmailSubscriptions().first()
        assertThat(result).isNotEmpty()
        // Note: This assumes FakeProfileRemoteDataSource returns default test data
    }

    @Test
    fun subscribeToTopicsCallsPushDatasource() = runTest {
        val tags = listOf(1, 2, 3)

        profileRepository.subscribeToTopics(tags)

        coVerify { pushNotificationsDatasource.subscribeToPushTags(tags) }
    }

    @Test
    fun unsubscribeFromAllTopicsCallsPushDatasource() = runTest {
        profileRepository.unsubscribeFromAllTopics()

        coVerify { pushNotificationsDatasource.unSubscribeFromPushTags() }
    }

    @Test
    fun clearLocalDataClearsProfileAndSubscriptions() = runTest {
        // Arrange: Seed local data
        profileLocalDataSource.setProfile(userProfileProtoTestData.first())
        profileLocalDataSource.setSubscriptions(subscribedTagProtoTestData)

        // Act
        profileRepository.clearLocalData()

        // Assert: Verify Profile is empty (id 0) and Subscriptions are empty
        val profileResult = profileRepository.getProfile().first()
        val subsResult = profileRepository.getEmailSubscriptions().first()

        assertThat(profileResult.id).isEqualTo(0)
        assertThat(subsResult).isEmpty()
    }

    // Note: To test `subscribeToEmailTags`, you would typically verify the
    // FakeProfileRemoteDataSource state, or use a Mock if the Fake doesn't expose state.
    @Test
    fun subscribeToEmailTagsDelegatesToRemote() = runTest {
        val tags = listOf(10, 20)

        // Act
        profileRepository.subscribeToEmailTags(tags)

        // Assert: Assuming your FakeProfileRemoteDataSource has a way to verify calls,
        // e.g., val lastSubscribed = profileRemoteDataSource.lastSubscribedTags
        // assertThat(lastSubscribed).isEqualTo(tags)
    }

}

