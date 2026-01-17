package com.kastik.apps.core.database.dao

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.user.SortType
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.announcementBodyEntityTestData
import com.kastik.apps.core.testing.testdata.announcementEntityTestData
import com.kastik.apps.core.testing.testdata.announcementTagsCrossRefEntityTestData
import com.kastik.apps.core.testing.testdata.authorEntitiesTestData
import com.kastik.apps.core.testing.testdata.remoteKeys
import com.kastik.apps.core.testing.testdata.tagEntitiesTestData
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals


@RunWith(RoboDatabaseTestRunner::class)
internal class AnnouncementDaoTest : MemoryDatabase() {

    @Test
    fun getAnnouncementWithIdTest() = runTest {
        insertAnnouncements()
        val announcement =
            announcementDao.getAnnouncementWithId(announcementEntityTestData.first().id)
        assertEquals(
            announcementEntityTestData.first().title,
            announcement.first()?.announcement?.title
        )
    }

    //TODO Test for more pages and errors
    @Test
    fun getPagingAnnouncementPreviewsTest() = runTest {
        insertAnnouncements()
        val announcement = announcementDao.getPagedAnnouncements(
            query = "",
            authorIds = emptyList(),
            tagIds = emptyList(),
            sortType = SortType.ASC,
        )
        val pager = TestPager(PagingConfig(pageSize = 20), announcement)
        val result = pager.refresh() as PagingSource.LoadResult.Page
        assertEquals(result.data.firstOrNull()?.announcement?.id ?: -10, 1)

        assertThat(result.data.map { it.announcement.id }).containsExactlyElementsIn(
            announcementEntityTestData.map { it.id }).inOrder()
    }


    @Test
    fun deleteAnnouncementTest() = runTest {
        insertAnnouncements()
        announcementDao.clearAllAnnouncements()
        val announcement = announcementDao.getPagedAnnouncements(
            query = "",
            authorIds = emptyList(),
            tagIds = emptyList(),
            sortType = SortType.Priority,
        )

        val page = announcement.load(
            PagingSource.LoadParams.Refresh(
                key = null, loadSize = 20, placeholdersEnabled = false
            )
        )
        when (page) {
            is PagingSource.LoadResult.Page -> TestCase.assertTrue(page.data.isEmpty())
            is PagingSource.LoadResult.Error -> throw page.throwable
            is PagingSource.LoadResult.Invalid<*, *> -> throw IllegalStateException()
        }
    }

    //TODO More robust testing with edge cases

    private suspend fun insertAnnouncements() {
        remoteKeysDao.insertOrReplaceKeys(remoteKeys)
        tagsDao.insertOrIgnoreTags(tagEntitiesTestData)
        authorsDao.insertOrIgnoreAuthors(authorEntitiesTestData)
        announcementDao.insertOrIgnoreAnnouncements(announcementEntityTestData)
        announcementDao.insertOrIgnoreAnnouncementBody(announcementBodyEntityTestData)
        announcementDao.insertOrIgnoreTagCrossRefs(announcementTagsCrossRefEntityTestData)
    }
}