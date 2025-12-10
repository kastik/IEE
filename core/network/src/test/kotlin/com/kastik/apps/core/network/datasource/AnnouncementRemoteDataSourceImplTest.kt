package com.kastik.apps.core.network.datasource


import com.kastik.apps.core.testing.api.FakeAboardApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AnnouncementRemoteDataSourceImplTest {

    lateinit var api: FakeAboardApiClient
    lateinit var dataSourceImpl: AnnouncementRemoteDataSourceImpl

    @Before
    fun setUp() {
        api = FakeAboardApiClient()
        dataSourceImpl = AnnouncementRemoteDataSourceImpl(api)
    }


    @Test
    fun fetchAnnouncementsForwardsCorrectPageTest() = runTest {
        val responsePageOne = api.getAnnouncements(
            sortId = 1,
            page = 1,
            perPage = 10
        )

        val resultPageOne = dataSourceImpl.fetchPagedAnnouncements(page = 1, perPage = 10)
        assertEquals(responsePageOne.meta.currentPage, resultPageOne.meta.currentPage)

        val responsePageTwo = api.getAnnouncements(
            sortId = 1,
            page = 2,
            perPage = 10
        )
        val resultPageTwo = dataSourceImpl.fetchPagedAnnouncements(page = 2, perPage = 10)
        assertEquals(responsePageTwo.meta.currentPage, resultPageTwo.meta.currentPage)

        val responsePageThree = api.getAnnouncements(
            sortId = 1,
            page = 3,
            perPage = 10
        )
        val resultPageThree = dataSourceImpl.fetchPagedAnnouncements(page = 3, perPage = 10)
        assertEquals(responsePageThree.meta.currentPage, resultPageThree.meta.currentPage)

    }
}

