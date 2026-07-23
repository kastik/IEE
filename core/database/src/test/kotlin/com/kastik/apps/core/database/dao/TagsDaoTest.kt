package com.kastik.apps.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.baseTagEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RoboDatabaseTestRunner::class)
internal class TagsDaoTest : MemoryDatabase() {

    @Test
    fun upsertTagsAddsNewTag() = runTest {
        val tags = listOf(baseTagEntity)
        tagsDao.upsertTags(tags)

        val result = tagsDao.getTags().first()
        assertThat(result).containsExactly(baseTagEntity)
    }

    @Test
    fun upsertTagsUpdatesExistingTags() = runTest {
        tagsDao.upsertTags(listOf(baseTagEntity))

        val updatedTag =
            baseTagEntity.copy(
                title = "Updated Root Tag",
                isPublic = false,
            )

        tagsDao.upsertTags(listOf(updatedTag))

        val retrievedTags = tagsDao.getTags().first()
        assertThat(retrievedTags).hasSize(1)

        val resultingTag = retrievedTags.first()
        assertThat(resultingTag.title).isEqualTo("Updated Root Tag")
        assertThat(resultingTag.isPublic).isFalse()
    }

    @Test
    fun getTagsReturnInsertedTags() = runTest {
        val tag2 = baseTagEntity.copy(id = 2, title = "Child Tag 1")
        val tag3 = baseTagEntity.copy(id = 3, title = "Child Tag 2")
        val tagsToInsert = listOf(baseTagEntity, tag2, tag3)

        tagsDao.upsertTags(tagsToInsert)

        val retrievedTags = tagsDao.getTags().first()

        assertThat(retrievedTags).containsExactlyElementsIn(tagsToInsert)
    }

    @Test
    fun clearTagsClearsTags() = runTest {
        val tag2 = baseTagEntity.copy(id = 2, title = "Another Tag")
        tagsDao.upsertTags(listOf(baseTagEntity, tag2))

        assertThat(tagsDao.getTags().first()).hasSize(2)

        tagsDao.clearTags()

        val retrievedTags = tagsDao.getTags().first()
        assertThat(retrievedTags).isEmpty()
    }
}
