package com.kastik.apps.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.announcementTagEntityTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RoboDatabaseTestRunner::class)
internal class TagsDaoTest : MemoryDatabase() {

    @Test
    fun upsertTagsAddsNewTag() = runTest {
        val tagEntities = announcementTagEntityTestData
        tagsDao.upsertTags(tagEntities)

        val result = tagsDao.getTags().first()
        assertThat(tagEntities).containsExactlyElementsIn(result)
    }

    @Test
    fun upsertTagsUpdatesExisting() = runTest {
        val tagEntities = announcementTagEntityTestData
        tagsDao.upsertTags(tagEntities)

        val modifiedTags = tagEntities.map { it.copy(title = "New Name") }
        tagsDao.upsertTags(modifiedTags)

        val result = tagsDao.getTags().first()
        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(modifiedTags)
    }

    @Test
    fun clearTagsClearsTagTable() = runTest {
        val tagEntities = announcementTagEntityTestData
        tagsDao.upsertTags(tagEntities)

        tagsDao.clearTags()
        val result = tagsDao.getTags().first()

        assertThat(result).isEmpty()
    }
}