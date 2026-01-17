package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import com.kastik.apps.core.testing.dao.FakeTagsDao
import com.kastik.apps.core.testing.datasource.local.FakeTagsLocalDataSource
import com.kastik.apps.core.testing.datasource.remote.FakeTagsRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TagsRepositoryImplTest {

    private val announcementTagsLocalDataSource: TagsDao = FakeTagsDao()
    private val subscribableTagsLocalDataSource: TagsLocalDataSource = FakeTagsLocalDataSource()
    private val tagsRemoteDataSource: TagsRemoteDataSource = FakeTagsRemoteDataSource()

    private lateinit var repository: TagsRepositoryImpl

    @Before
    fun setUp() {
        repository = TagsRepositoryImpl(
            announcementTagsLocalDataSource = announcementTagsLocalDataSource,
            subscribableTagsLocalDataSource = subscribableTagsLocalDataSource,
            tagsRemoteDataSource = tagsRemoteDataSource,
        )
    }

    @Test
    fun `getAnnouncementTags emits mapped data from local source`() = runTest {
        val result = repository.getAnnouncementTags().first()
        assertThat(result).isEmpty()
        val test = tagsRemoteDataSource.fetchAnnouncementTags().data
        announcementTagsLocalDataSource.insertOrIgnoreTags(test.map { it.toTagEntity() })
        val newResult = repository.getAnnouncementTags().first()
        assertThat(newResult).isNotEmpty()
        assertThat(newResult).containsExactly(test.map { it.toTagEntity().toTag() })
    }

    @Test
    fun `refreshAnnouncementTags fetches remote and saves to local`() = runTest {
        val test = tagsRemoteDataSource.fetchAnnouncementTags().data
        repository.refreshAnnouncementTags()
        assertEquals(test.size, repository.getAnnouncementTags().first().size)
    }

    @Test
    fun `getSubscribableTags emits mapped data from local data store`() = runTest {
        val test = subscribableTagsLocalDataSource.getSubscribableTags()
        val result = repository.getSubscribableTags().first()
        assertEquals(test.first().tagsList.size, result.size)
    }

    @Test
    fun `refreshSubscribableTags fetches remote and sets to local`() = runTest {
        val test = tagsRemoteDataSource.fetchSubscribableTags()
        repository.refreshSubscribableTags()
        assertEquals(test.size, repository.getSubscribableTags().first().size)
    }
}