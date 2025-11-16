package com.kastik.apps.core.database.dao

import androidx.paging.PagingSource
import com.kastik.apps.core.database.db.MemoryDatabase
import com.kastik.apps.core.database.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.testAnnouncementEntityWrapperList
import junit.framework.TestCase
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
            announcementDao.getAnnouncementWithId(testAnnouncementEntityWrapperList.first().announcement.id)
        assertEquals(
            testAnnouncementEntityWrapperList.first().announcement.title,
            announcement.announcement.title
        )
    }

    @Test
    fun getPagingAnnouncementPreviewsTest() = runTest {
        insertAnnouncements()
        val announcement = announcementDao.getPagingAnnouncementPreviews()
        val page = announcement.load(
            PagingSource.LoadParams.Refresh(
                key = null, loadSize = 20, placeholdersEnabled = false
            )
        )
        when (page) {
            is PagingSource.LoadResult.Page -> assertEquals(
                page.data.firstOrNull()?.announcement?.id ?: -10, 1
            )

            is PagingSource.LoadResult.Error -> throw page.throwable
            is PagingSource.LoadResult.Invalid<*, *> -> throw IllegalStateException()
        }
    }


    @Test
    fun deleteAnnouncementTest() = runTest {
        insertAnnouncements()
        announcementDao.clearAnnouncements()
        val announcement = announcementDao.getPagingAnnouncementPreviews()

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
        testAnnouncementEntityWrapperList.forEach {
            announcementDao.addAnnouncement(
                it
            )
        }

    }
}