package com.kastik.apps.core.network.datasource


import com.kastik.apps.core.model.user.SortType
import com.kastik.apps.core.testing.api.FakeAboardApiClient
import com.kastik.apps.core.testing.testdata.announcementPageResponseTestData
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AnnouncementRemoteDataSourceImplTest {

    lateinit var api: FakeAboardApiClient
    private lateinit var dataSourceImpl: AnnouncementRemoteDataSourceImpl

    @Before
    fun setUp() {
        api = FakeAboardApiClient()
        dataSourceImpl = AnnouncementRemoteDataSourceImpl(api)
    }


    @Test
    fun fetchAnnouncementsForwardsCorrectPageTest() = runTest {
        val responsePageOne = api.getAnnouncements(
            sortType = SortType.DESC,
            page = 1,
            perPage = 10
        )

        val resultPageOne = dataSourceImpl.fetchPagedAnnouncements(
            page = 1,
            perPage = 10,
            sortBy = SortType.Priority
        )
        assertEquals(responsePageOne.meta.currentPage, resultPageOne.meta.currentPage)

        val responsePageTwo = api.getAnnouncements(
            sortType = SortType.DESC,
            page = 2,
            perPage = 10
        )
        val resultPageTwo =
            dataSourceImpl.fetchPagedAnnouncements(page = 2, perPage = 10, SortType.Priority)
        assertEquals(responsePageTwo.meta.currentPage, resultPageTwo.meta.currentPage)

        val responsePageThree = api.getAnnouncements(
            sortType = SortType.DESC,
            page = 3,
            perPage = 10
        )
        val resultPageThree =
            dataSourceImpl.fetchPagedAnnouncements(page = 3, perPage = 10, SortType.Priority)
        assertEquals(responsePageThree.meta.currentPage, resultPageThree.meta.currentPage)

    }

    @Test
    fun fetchAnnouncementWithId_forwards_id_to_api() = runTest {
        val ids = announcementPageResponseTestData.map { it.data.first().id }
        ids.forEach { id ->
            val expected = api.getAnnouncement(id)
            val result = dataSourceImpl.fetchAnnouncementWithId(id)
            assertEquals(expected, result)
        }
    }
}

