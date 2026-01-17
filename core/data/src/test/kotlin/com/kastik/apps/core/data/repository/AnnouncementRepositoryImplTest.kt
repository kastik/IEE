package com.kastik.apps.core.data.repository


import androidx.paging.ExperimentalPagingApi
import androidx.paging.testing.R
import androidx.paging.testing.asSnapshot
import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.user.SortType
import com.kastik.apps.core.testing.dao.FakeAnnouncementDao
import com.kastik.apps.core.testing.datasource.remote.FakeAnnouncementRemoteDataSource
import com.kastik.apps.core.testing.db.FakeAppDatabase
import com.kastik.apps.core.testing.testdata.announcementDetailsRelationTestData
import com.kastik.apps.core.testing.testdata.attachmentEntityTestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class AnnouncementRepositoryImplTest {
    private val fakeDatabase = FakeAppDatabase()
    private val announcementDao = fakeDatabase.announcementDao() as FakeAnnouncementDao
    private val fakeAnnouncementRemoteDataSource = FakeAnnouncementRemoteDataSource()

    private val announcementRepoImpl =
        AnnouncementRepositoryImpl(
            database = fakeDatabase,
            announcementRemoteDataSource = fakeAnnouncementRemoteDataSource
        )

    @Before
    fun initMocks() {
        MockKAnnotations.init(this)

        mockkStatic("androidx.room.RoomDatabaseKt")

        val transactionLambda = slot<suspend () -> R>()
        coEvery { fakeDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
    }


    @Test
    fun getPagedAnnouncements_returnsMappedData() = runTest {
        // 1. Setup: Ensure remote source has data to load
        // Assuming your fake remote has a method to add test data

        val firstItems = fakeAnnouncementRemoteDataSource.fetchPagedAnnouncements(
            1,
            perPage = 20,
            sortBy = SortType.DESC
        )
        // 2. Act: Collect the flow using asSnapshot()
        // This triggers the Pager -> Mediator -> DB -> UI flow and waits for data
        val result: List<Announcement> = announcementRepoImpl.getPagedAnnouncements(
            sortType = SortType.DESC,
            query = "",
            authorIds = emptyList(),
            tagIds = emptyList()
        ).asSnapshot()

        // 3. Assert: Verify the result is mapped and correct
        println(firstItems)
        println(result)
        assertThat(result).isNotEmpty()
        assertThat(result.first().id).isEqualTo(announcementDetailsRelationTestData.first().announcement.id)
    }

    @Test
    fun getAnnouncementWithIdFetchesTheCorrectDataTest() = runTest {
        announcementDao.insertTestData()
        announcementDetailsRelationTestData.forEach { announcementDetail ->
            val result =
                announcementRepoImpl.getAnnouncementWithId(announcementDetail.announcement.id)
                    .first()!!
            assertThat(result).isNotNull()
            assertThat(announcementDetail.announcement.id).isEqualTo(result.id)
            assertThat(announcementDetail.announcement.title).isEqualTo(result.title)
            assertThat(announcementDetail.body.body).isEqualTo(result.body)
            assertThat(announcementDetail.author.name).isEqualTo(result.author)
        }
    }

    //TODO Create test for sorting, limiting, etc
    @Test
    fun getAnnouncementsQuickResultsReturnsQuickResults() = runTest {
        announcementDao.insertTestData()
        val result =
            announcementRepoImpl.getAnnouncementsQuickResults(SortType.DESC, "Test").first()
        assertThat(result).isNotEmpty()
    }


    @Test
    fun getAttachmentUrlReturnsAttachmentUrl() = runTest {
        announcementDao.insertTestData()
        val attachments = announcementDao.attachments.value

        assertThat(attachments).isNotEmpty()

        attachments.forEach { attachment ->
            val result = announcementRepoImpl.getAttachmentUrl(attachment.id)
            assertThat(result).isEqualTo(attachment.attachmentUrl)
        }
    }


    @Test
    fun clearAnnouncementCacheClearsLocallyStoredAnnouncements() = runTest {
        announcementDao.insertTestData()
        announcementRepoImpl.clearAnnouncementCache()
        val result = announcementDao.announcements.value
        assertThat(result).isEmpty()
    }


}
