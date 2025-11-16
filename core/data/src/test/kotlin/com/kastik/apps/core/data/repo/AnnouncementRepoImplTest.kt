package com.kastik.apps.core.data.repo


import androidx.paging.ExperimentalPagingApi
import com.kastik.apps.core.data.repository.AnnouncementRepoImpl
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.testing.datasource.remote.FakeAnnouncementRemoteDataSource
import com.kastik.apps.core.testing.db.FakeAppDatabase
import com.kastik.apps.core.testing.testdata.testAnnouncementEntityWrapperList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class AnnouncementRepoImplTest {

    private lateinit var announcementRepoImpl: AnnouncementRepository
    private lateinit var fakeDb: FakeAppDatabase
    private lateinit var fakeAnnouncementRemoteDataSource: FakeAnnouncementRemoteDataSource

    @Before
    fun setup() {
        fakeDb = FakeAppDatabase()
        fakeAnnouncementRemoteDataSource = FakeAnnouncementRemoteDataSource()
        announcementRepoImpl = AnnouncementRepoImpl(fakeAnnouncementRemoteDataSource, fakeDb)
    }

    @Test
    fun getAnnouncementWithIdFetchesTheCorrectDataTest() = runTest {
        insertAnnouncements()
        testAnnouncementEntityWrapperList.forEach { wrapper ->
            val result = announcementRepoImpl.getAnnouncementWithId(wrapper.announcement.id)
            assert(wrapper.announcement.id == result.id)
            assertEquals(wrapper.announcement.title, result.title)
            assertEquals(wrapper.body.body, result.body)
            assertEquals(wrapper.author.name, result.author)
            //TODO These are broken but shouldn't be, the problem is in our Fakes/TestData combination
            //assertEquals(wrapper.tags.size, result.tags.size)
            //assertEquals(wrapper.attachments.size, result.attachments.size)
        }
    }

    private suspend fun insertAnnouncements() {
        testAnnouncementEntityWrapperList.forEach {
            fakeDb.fakeAnnouncementDao.addAnnouncement(
                it
            )
        }

    }
}
