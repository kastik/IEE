package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.data.mappers.toSubscribableTag
import com.kastik.apps.core.data.mappers.toSubscribableTagProto
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import com.kastik.apps.core.testing.dao.FakeTagsDao
import com.kastik.apps.core.testing.datasource.local.FakeTagsLocalDataSource
import com.kastik.apps.core.testing.datasource.remote.FakeTagsRemoteDataSource
import com.kastik.apps.core.testing.testdata.announcementTagDtoTestData
import com.kastik.apps.core.testing.testdata.subscribableTagsProtoTestData
import com.kastik.apps.core.testing.utils.FakeCrashlytics
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TagsRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val announcementTagsLocalDataSource: TagsDao = FakeTagsDao()
    private val subscribableTagsLocalDataSource: TagsLocalDataSource = FakeTagsLocalDataSource()
    private val tagsRemoteDataSource: TagsRemoteDataSource = FakeTagsRemoteDataSource()

    private lateinit var repository: TagsRepositoryImpl

    @Before
    fun setUp() {
        repository = TagsRepositoryImpl(
            crashlytics = FakeCrashlytics(),
            announcementTagsLocalDataSource = announcementTagsLocalDataSource,
            subscribableTagsLocalDataSource = subscribableTagsLocalDataSource,
            tagsRemoteDataSource = tagsRemoteDataSource,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun getAnnouncementTagsReturnsEmptyWhenNotSet() = runTest(testDispatcher) {
        val result = repository.getAnnouncementTags().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun getAnnouncementTagsEmitsFromLocalSource() = runTest(testDispatcher) {
        val tags = announcementTagDtoTestData
        announcementTagsLocalDataSource.upsertTags(tags.map { it.toTagEntity() })

        val result = repository.getAnnouncementTags().first()
        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(tags.map { it.toTagEntity().toTag() })
    }

    @Test
    fun refreshAnnouncementTagsFetchesRemoteTagsAndSavesToLocal() = runTest(testDispatcher) {
        val remote = tagsRemoteDataSource.fetchAnnouncementTags().data
        repository.refreshAnnouncementTags()
        val result = repository.getAnnouncementTags().first()
        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(remote.map { it.toTagEntity().toTag() })
    }

    @Test
    fun getSubscribableTagsReturnsEmptyWhenNotSet() = runTest(testDispatcher) {
        val result = repository.getSubscribableTags().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun getSubscribableTagsEmitsFromLocalSource() = runTest(testDispatcher) {
        val subscribableTags = subscribableTagsProtoTestData
        subscribableTagsLocalDataSource.setSubscribableTags(subscribableTags.tagsList)

        val result = repository.getSubscribableTags().first()
        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(subscribableTags.tagsList.map { it.toSubscribableTag() })
    }


    @Test
    fun refreshSubscribableTagsFetchesRemoteTagsAndSavesToLocal() = runTest(testDispatcher) {
        val remote = tagsRemoteDataSource.fetchSubscribableTags()
        repository.refreshSubscribableTags()

        val result = repository.getSubscribableTags().first()
        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(remote.map {
            it.toSubscribableTagProto().toSubscribableTag()
        })
    }
}