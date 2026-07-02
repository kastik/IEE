package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toNetworkError
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.data.mappers.toTagProto
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.datastore.datasource.TagsLocalDataSource
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
internal class TagsRepositoryImpl @Inject constructor(
    private val crashlytics: Crashlytics,
    private val announcementTagsLocalDataSource: TagsDao,
    private val subscribableTagsLocalDataSource: TagsLocalDataSource,
    private val tagsRemoteDataSource: TagsRemoteDataSource,
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


    override suspend fun refreshAnnouncementTags() = withContext(ioDispatcher) {
        try {
            val remoteTags =
                tagsRemoteDataSource.fetchAnnouncementTags().data.map { it.toTagEntity() }
            announcementTagsLocalDataSource.upsertTags(remoteTags)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toNetworkError())
        }
    }

    override suspend fun refreshSubscribableTags() = withContext(ioDispatcher) {
        try {
            val subscribableTags = tagsRemoteDataSource.fetchSubscribableTags()
            subscribableTagsLocalDataSource.setSubscribableTags(subscribableTags.map { tag -> tag.toTagProto() })
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toNetworkError())
        }
    }


    override suspend fun refreshSubscribedTags() = withContext(ioDispatcher) {
        try {
            val subscribedTags = tagsRemoteDataSource.fetchSubscriptions()
            subscribableTagsLocalDataSource.setSubscriptions(subscribedTags.map { tag -> tag.toTagProto() })
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toNetworkError())
        }
    }

    override suspend fun subscribeToTags(tagIds: List<Int>): Result<Unit, NetworkError> =
        withContext(ioDispatcher) {
            try {
                tagsRemoteDataSource.subscribeToTags(tagIds)
                Result.Success(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                crashlytics.recordException(e)
                Result.Error(e.toNetworkError())
            }
        }
}