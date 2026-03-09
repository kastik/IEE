package com.kastik.apps.core.network.datasource


import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.api.FakeAboardApiClient
import com.kastik.apps.core.network.testdata.announcementPageResponseTestData
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AnnouncementRemoteDataSourceImplTest {

    private val fakeAboardApiClient = FakeAboardApiClient()
    private val dataSourceImpl = AnnouncementRemoteDataSourceImpl(fakeAboardApiClient)


    @Test
    fun fetchAnnouncementsForwardsCorrectPageTest() = runTest {
        val responsePageOne = fakeAboardApiClient.getAnnouncements(
            sortType = SortType.DESC,
            page = 1,
            perPage = 10
        )

        val resultPageOne = dataSourceImpl.fetchPagedAnnouncements(
            page = 1,
            perPage = 10,
            sortBy = SortType.Priority
        )
        assertThat(resultPageOne.meta.currentPage).isEqualTo(responsePageOne.meta.currentPage)

        val responsePageTwo = fakeAboardApiClient.getAnnouncements(
            sortType = SortType.DESC,
            page = 2,
            perPage = 10
        )
        val resultPageTwo =
            dataSourceImpl.fetchPagedAnnouncements(page = 2, perPage = 10, SortType.Priority)
        assertThat(resultPageTwo.meta.currentPage).isEqualTo(responsePageTwo.meta.currentPage)

        val responsePageThree = fakeAboardApiClient.getAnnouncements(
            sortType = SortType.DESC,
            page = 3,
            perPage = 10
        )
        val resultPageThree =
            dataSourceImpl.fetchPagedAnnouncements(page = 3, perPage = 10, SortType.Priority)
        assertThat(resultPageThree.meta.currentPage).isEqualTo(responsePageThree.meta.currentPage)
    }

    @Test
    fun fetchAnnouncementWithIdForwardsCorrectIdToApi() = runTest {
        val ids = announcementPageResponseTestData.map { it.data.first().id }
        ids.forEach { id ->
            val remote = fakeAboardApiClient.getAnnouncement(id)
            val result = dataSourceImpl.fetchAnnouncementWithId(id)
            assertThat(result).isEqualTo(remote)
        }
    }
}

