package com.kastik.apps.core.database.dao

import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.announcementTagEntityTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RoboDatabaseTestRunner::class)
internal class TagsDaoTest : MemoryDatabase() {

    @Test
    fun insertTagsInsertsOrIgnoreTags() = runTest {
        val tagEntities = announcementTagEntityTestData
        tagsDao.insertOrIgnoreTags(tagEntities)

        val result = tagsDao.getTags().first()
        assertEquals(tagEntities, result)
    }

    @Test
    fun clearTags_emptiesList() = runTest {
        val tagEntities = announcementTagEntityTestData
        tagsDao.insertOrIgnoreTags(tagEntities)

        tagsDao.clearTags()
        val result = tagsDao.getTags().first()

        assertEquals(emptyList<TagEntity>(), result)
    }
}