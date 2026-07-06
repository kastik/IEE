package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toNetworkError
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.data.mappers.toTagProto
import com.kastik.apps.core.data.utils.safeCall
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.datastore.datasource.AuthenticationLocalDataSource
import com.kastik.apps.core.datastore.datasource.TagsLocalDataSource
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TagsRepositoryImpl @Inject constructor(
    private val crashlytics: Crashlytics,
    private val announcementTagsLocalDataSource: TagsDao,
    private val subscribableTagsLocalDataSource: TagsLocalDataSource,
    private val tagsRemoteDataSource: TagsRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TagsRepository {

    override val announcementTags: Flow<List<Tag>> =
        announcementTagsLocalDataSource.getTags().map { it.map { tag -> tag.toTag() } }

    override val subscribedTags: Flow<List<Tag>> =
        subscribableTagsLocalDataSource.subscriptions
            .map { it.tagsList.map { tag -> tag.toTag() } }

    override val subscribableTags: Flow<List<Tag>> =
        subscribableTagsLocalDataSource.subscribableTags
            .map { subscribableTags -> subscribableTags.tagsList.map { tag -> tag.toTag() } }


    override suspend fun syncAnnouncementTags() = withContext(ioDispatcher) {
        safeCall(
            mapException = Exception::toNetworkError,
            recordException = crashlytics::recordException
        ) {
            val remoteTags =
                tagsRemoteDataSource.fetchAnnouncementTags().data.map { it.toTagEntity() }
            announcementTagsLocalDataSource.upsertTags(remoteTags)
        }
    }

    override suspend fun syncSubscribableTags() = withContext(ioDispatcher) {
        safeCall(
            mapException = Exception::toNetworkError,
            recordException = crashlytics::recordException
        ) {
            val subscribableTags = tagsRemoteDataSource.fetchSubscribableTags()
            subscribableTagsLocalDataSource.setSubscribableTags(subscribableTags.map { tag -> tag.toTagProto() })

        }
    }


    override suspend fun syncSubscribedTags() = withContext(ioDispatcher) {

        if (!authenticationLocalDataSource.isSignedIn.first()) {
            return@withContext Result.Success(Unit)
        }

        safeCall(
            mapException = Exception::toNetworkError,
            recordException = crashlytics::recordException
        ) {
            val subscribedTags = tagsRemoteDataSource.fetchSubscriptions()
            subscribableTagsLocalDataSource.setSubscriptions(subscribedTags.map { tag -> tag.toTagProto() })
        }
    }

    override suspend fun subscribeToTags(tagIds: List<Int>): Result<Unit, NetworkError> =
        withContext(ioDispatcher) {
            safeCall(
                mapException = Exception::toNetworkError,
                recordException = crashlytics::recordException
            ) {
                tagsRemoteDataSource.subscribeToTags(tagIds)

                val syncStatus = syncSubscribedTags()
                if (syncStatus is Result.Error) {
                    throw Exception("Failed to sync tags after subscribing")
                }
            }
        }
}