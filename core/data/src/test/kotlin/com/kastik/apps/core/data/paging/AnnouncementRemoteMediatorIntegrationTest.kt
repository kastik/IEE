package com.kastik.apps.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.testing.asSnapshot
import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation
import com.kastik.apps.core.model.user.SortType
import com.kastik.apps.core.testing.datasource.remote.FakeAnnouncementRemoteDataSource
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RoboDatabaseTestRunner::class)
@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediatorIntegrationTest : MemoryDatabase(
    dispatcher = StandardTestDispatcher()
) {

    private val fakeAnnouncementRemoteDataSource = FakeAnnouncementRemoteDataSource()

    private val announcementRemoteMediator =
        AnnouncementRemoteMediator(
            query = "",
            tagIds = emptyList(),
            authorIds = emptyList(),
            sortType = SortType.DESC,
            database = db,
            announcementRemoteDataSource = fakeAnnouncementRemoteDataSource,
        )


    private val pagerConfig = PagingConfig(
        pageSize = 20,
        initialLoadSize = 60,
        prefetchDistance = 10,
        enablePlaceholders = false
    )

    private val pagingSourceFactory = { db.announcementDao().getPagedAnnouncements() }

    private val pager: Pager<Int, AnnouncementPreviewRelation> = Pager(
        config = pagerConfig,
        remoteMediator = announcementRemoteMediator,
        pagingSourceFactory = pagingSourceFactory
    )

    @Test
    fun doesNotIgnoreInitialLoad() = runTest(dispatcher) {

        val pageOneItemIds = fakeAnnouncementRemoteDataSource.fetchPagedAnnouncements(
            page = 1,
            perPage = pagerConfig.initialLoadSize,
            sortBy = SortType.DESC,
        ).data.map { it.id }

        val mediatorItems: List<AnnouncementPreviewRelation> = pager.flow.asSnapshot {
            scrollTo(0)

        }

        assertThat(mediatorItems).isNotEmpty()
        val mediatorItemIds = mediatorItems.map { it.announcement.id }

        assertThat(mediatorItems.size).isEqualTo(pagerConfig.initialLoadSize)
        assertThat(mediatorItemIds).containsExactlyElementsIn(pageOneItemIds).inOrder()
    }


    @Test
    fun appendCanFetchTheNextPageTest() = runTest(dispatcher) {
        val expectedItemCount = pagerConfig.initialLoadSize + pagerConfig.pageSize

        val pageOneItemIds = fakeAnnouncementRemoteDataSource.fetchPagedAnnouncements(
            page = 1,
            perPage = expectedItemCount,
            sortBy = SortType.DESC,
        ).data.map { it.id }

        val mediatorItems: List<AnnouncementPreviewRelation> = pager.flow.asSnapshot {
            scrollTo(60)

        }

        assertThat(mediatorItems).isNotEmpty()
        val mediatorItemIds = mediatorItems.map { it.announcement.id }

        assertThat(mediatorItems.size).isEqualTo(expectedItemCount)
        assertThat(mediatorItemIds).containsExactlyElementsIn(pageOneItemIds).inOrder()
    }

    @Test
    fun triggersPrefetchDistanceTest() = runTest(dispatcher) {
        val expectedItemCount = pagerConfig.initialLoadSize + pagerConfig.pageSize

        val pageOneItemIds = fakeAnnouncementRemoteDataSource.fetchPagedAnnouncements(
            page = 1,
            perPage = expectedItemCount,
            sortBy = SortType.DESC,
        ).data.map { it.id }
        val items: List<AnnouncementPreviewRelation> = pager.flow.asSnapshot {
            scrollTo(index = pagerConfig.initialLoadSize)

        }
        assertThat(items).isNotEmpty()
        assertThat(items.size).isEqualTo(expectedItemCount)
        assertThat(items.map { it.announcement.id }).containsExactlyElementsIn(pageOneItemIds)
            .inOrder()
    }

    @Test
    fun appendTillTheEndStopsTest() = runTest(dispatcher) {
        val lastPageResponse = fakeAnnouncementRemoteDataSource.fetchPagedAnnouncements(
            page = 1,
            perPage = pagerConfig.initialLoadSize,
            sortBy = SortType.DESC,
        )
        val lastPage = lastPageResponse.meta.lastPage
        val allPagesItemIds = lastPageResponse.data.map { it.id }


        val lastItemIndex = lastPage * pagerConfig.pageSize - 1

        val items: List<AnnouncementPreviewRelation> = pager.flow.asSnapshot {
            scrollTo(index = lastItemIndex)

        }

        println("Final item count: ${items.size}")
        println(allPagesItemIds)

        assertThat(items).isNotEmpty()
        assertThat(items.size).isEqualTo(allPagesItemIds.size)
        assertThat(items.map { it.announcement.id }).containsExactlyElementsIn(allPagesItemIds)
            .inOrder()
    }

}