package com.kastik.apps.core.database.dao

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.announcementAuthorEntityTestData
import com.kastik.apps.core.testing.testdata.announcementBodyEntityTestData
import com.kastik.apps.core.testing.testdata.announcementEntityTestData
import com.kastik.apps.core.testing.testdata.announcementTagEntityTestData
import com.kastik.apps.core.testing.testdata.announcementTagsCrossRefEntityTestData
import com.kastik.apps.core.testing.testdata.noFilteredRemoteKeys
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
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

    @Test
    fun getPagingAnnouncementPreviewsTest() = runTest {
        insertAnnouncements()

        val pagingSource = announcementDao.getPagedAnnouncements(
            titleQuery = "",
            bodyQuery = "",
            authorIds = emptyList(),
            tagIds = emptyList(),
            sortType = SortType.DESC,
        )

        val pager = TestPager(PagingConfig(pageSize = 20), pagingSource)
        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertThat(result.data.map { it.announcement.id })
            .containsExactlyElementsIn(announcementEntityTestData.map { it.id })
            .inOrder()
    }


    @Test
    fun deleteAnnouncementTest() = runTest {
        insertAnnouncements()
        announcementDao.clearAllAnnouncements()
        val announcement = announcementDao.getPagedAnnouncements(
            titleQuery = "",
            authorIds = emptyList(),
            tagIds = emptyList(),
            sortType = SortType.DESC,
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

    private suspend fun insertAnnouncements() {
        remoteKeysDao.insertOrReplaceKeys(noFilteredRemoteKeys)
        tagsDao.upsertTags(announcementTagEntityTestData)
        authorsDao.upsertAuthors(announcementAuthorEntityTestData)
        announcementDao.upsertAnnouncements(announcementEntityTestData)
        announcementDao.upsertBodies(announcementBodyEntityTestData)
        announcementDao.upsertTagCrossRefs(announcementTagsCrossRefEntityTestData)
    }
}