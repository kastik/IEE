package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toPrivateRefreshError
import com.kastik.apps.core.data.mappers.toProfile
import com.kastik.apps.core.data.mappers.toProfileProto
import com.kastik.apps.core.data.mappers.toSubscribedTagProto
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.datastore.proto.SubscribedTopicProto
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSource
import com.kastik.apps.core.notifications.PushNotificationsDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
    private val pushNotificationsDatasource: PushNotificationsDatasource,
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
            Result.Error(e.toPrivateRefreshError())
        }

    }

    override fun getEmailSubscriptions(): Flow<List<Tag>> {
        return profileLocalDataSource.getSubscriptions()
            .map { tagList -> tagList.subscribedTagsList.map { it.toTag() } }
    }

    override fun getTopicSubscriptions(): Flow<List<Int>> {
        return profileLocalDataSource.getSubscriptions()
            .map { tagList -> tagList.subscribedTopicsList.map { it.id } }
    }

    override suspend fun refreshEmailSubscriptions() = withContext(ioDispatcher) {
        try {
            val subscribedTags = profileRemoteDataSource.getEmailSubscriptions()
            profileLocalDataSource.setTagSubscriptions(subscribedTags.map { tag -> tag.toSubscribedTagProto() })
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toPrivateRefreshError())
        }
    }

    override suspend fun subscribeToEmailTags(tagIds: List<Int>) =
        withContext(NonCancellable + ioDispatcher) {
            try {
                profileRemoteDataSource.subscribeToEmailTags(tagIds)
                Result.Success(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                crashlytics.recordException(e)
                Result.Error(e.toPrivateRefreshError())
            }
        }

    override suspend fun syncTopicSubscriptions() = withContext(NonCancellable + ioDispatcher) {
        val emailSubscriptionIds =
            profileLocalDataSource.getSubscriptions().map { it.subscribedTagsList }.firstOrNull()
                ?.map { it.id }?.toSet() ?: emptySet()
        val currentTopics =
            profileLocalDataSource.getSubscriptions().map { it.subscribedTopicsList.map { it.id } }
                .firstOrNull()?.toSet() ?: emptySet()
        val topicIdsToSubscribe = emailSubscriptionIds - currentTopics
        val topicIdsToUnsubscribe = currentTopics - emailSubscriptionIds
        val subscribeToTopicsDeferred =
            async {
                pushNotificationsDatasource.subscribeToTopics(topicIdsToSubscribe.toList())
            }
        val unsubscribeFromTopicsDeferred =
            async {
                pushNotificationsDatasource.unsubscribeFromTopics(topicIdsToUnsubscribe.toList())
            }
        awaitAll(subscribeToTopicsDeferred, unsubscribeFromTopicsDeferred)
        profileLocalDataSource.setTopicSubscriptions(topicIdsToSubscribe.map {
            SubscribedTopicProto.newBuilder().setId(it).build()
        })
        Result.Success(Unit)
    }

    override suspend fun unsubscribeFromAllTopics() = withContext(ioDispatcher) {
        val subscribedTopicIds =
            profileLocalDataSource.getSubscriptions().map { it.subscribedTopicsList.map { it.id } }
                .first()

        pushNotificationsDatasource.unsubscribeFromTopics(subscribedTopicIds.toList())
        profileLocalDataSource.setTopicSubscriptions(emptyList())
    }

    override suspend fun clearLocalData() = withContext(ioDispatcher) {
        try {
            profileLocalDataSource.clearProfile()
            profileLocalDataSource.clearSubscriptions()
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(StorageError)
        }
    }
}