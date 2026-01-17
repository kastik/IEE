package com.kastik.apps.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.testing.asSnapshot
import androidx.room.R
import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.data.mappers.toAnnouncementEntity
import com.kastik.apps.core.data.mappers.toAttachmentEntity
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.database.entities.RemoteKeys
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation
import com.kastik.apps.core.model.user.SortType
import com.kastik.apps.core.network.model.aboard.AnnouncementMeta
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse
import com.kastik.apps.core.testing.dao.FakeAnnouncementDao
import com.kastik.apps.core.testing.datasource.remote.FakeAnnouncementRemoteDataSource
import com.kastik.apps.core.testing.db.FakeAppDatabase
import com.kastik.apps.core.testing.testdata.announcementPageResponseTestData
import com.kastik.apps.core.testing.testdata.tagEntitiesTestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockkStatic
import io.mockk.slot
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertTrue


@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediatorTest {
    private val fakeAppDatabase: FakeAppDatabase = FakeAppDatabase()
    private val fakeAnnouncementRemoteDataSource = FakeAnnouncementRemoteDataSource()
    private val pagerConfig = PagingConfig(
        pageSize = 10,
        initialLoadSize = 60,
        prefetchDistance = 10,
        enablePlaceholders = true
    )

    private val announcementRemoteMediator = AnnouncementRemoteMediator(
        database = fakeAppDatabase,
        announcementRemoteDataSource = fakeAnnouncementRemoteDataSource,
    )
    private var pagingState = PagingState<Int, AnnouncementPreviewRelation>(
        pages = emptyList(),
        anchorPosition = null,
        config = pagerConfig,
        leadingPlaceholderCount = 0
    )

    private val pagingSourceFactory = { fakeAppDatabase.announcementDao().getPagedAnnouncements() }
    private var pager: Pager<Int, AnnouncementPreviewRelation> = Pager(
        config = pagerConfig,
        remoteMediator = announcementRemoteMediator,
        pagingSourceFactory = pagingSourceFactory
    )


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic("androidx.room.RoomDatabaseKt")
        val transactionLambda = slot<suspend () -> R>()
        coEvery { fakeAppDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
    }

    @Test
    fun initializeLaunchesInitialRefreshTest() = runTest {
        val result = announcementRemoteMediator.initialize()
        assertThat(result).isInstanceOf(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH::class.java)
    }

    @Test
    fun refreshClearsKeysTest() = runTest {
        announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        val result = fakeAppDatabase.fakeRemoteKeysDao.clearKeysCalled
        assertThat(result).isTrue()
    }

    @Test
    fun refreshFetchesFirstPageTest() = runTest {
        announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        val apiPageOne = fakeAnnouncementRemoteDataSource.fetchPagedAnnouncements(
            page = 1,
            perPage = pagerConfig.pageSize,
            sortBy = SortType.DESC,
        )
        val dbPageOne = fakeAppDatabase.fakeAnnouncementDao.announcements.value
        assertThat(dbPageOne).containsExactlyElementsIn(apiPageOne.data.map { it.toAnnouncementEntity() })
    }

    @Test
    fun refreshReturnsSuccessWithEndOfPaginationToFalseWhenMorePageAreAvailableTest() = runTest {
        val result = announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        result as RemoteMediator.MediatorResult.Success
        assertThat(result.endOfPaginationReached).isFalse()
    }

    @Test
    fun refreshReturnsSuccessWithEndOfPaginationToTrueWhenMorePagesAreAvailable() = runTest {
        fakeAnnouncementRemoteDataSource.pagedAnnouncementResponseOverride =
            AnnouncementPageResponse(
                data = emptyList(),
                meta = AnnouncementMeta(
                    currentPage = 1,
                    from = 1,
                    lastPage = 1,
                    path = "",
                    perPage = 0,
                    to = 0,
                    total = 0
                )
            )
        val result = announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        result as RemoteMediator.MediatorResult.Success
        assertThat(result.endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshLoadsPageOneToDbTest() = runTest {
        val apiPageOne = fakeAnnouncementRemoteDataSource.fetchPagedAnnouncements(
            page = 1,
            perPage = pagerConfig.pageSize,
            sortBy = SortType.DESC,
        )
        announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(fakeAppDatabase.fakeAnnouncementDao.announcements.value).containsExactlyElementsIn(
            apiPageOne.data.map { it.toAnnouncementEntity() })
    }


    @Test
    fun prependEndsPaginationTest() = runTest {
        val result = announcementRemoteMediator.load(LoadType.PREPEND, pagingState)
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        result as RemoteMediator.MediatorResult.Success
        assertThat(result.endOfPaginationReached).isTrue()
    }


    @Test
    fun appendFetchesNextPage() = runTest {
        // 1. PREPARE: Overwrite the default empty state with a POPULATED state
        // We need a page so the mediator knows we are at the end of the list
        val pages = announcementPageResponseTestData.map { response ->
            PagingSource.LoadResult.Page(
                data = response.data.map { announcementDto ->
                    AnnouncementPreviewRelation(
                        announcement = announcementDto.toAnnouncementEntity(),
                        author = announcementDto.author.toAuthorEntity(),
                        tags = tagEntitiesTestData, //TODO Get tags from DAO
                        attachments = announcementDto.attachments.map { it.toAttachmentEntity() },
                    )
                },
                prevKey = response.meta.currentPage - 1,
                nextKey = response.meta.currentPage + 1,
            )
        }

        println(pages.last().nextKey)


        pagingState = PagingState(
            pages = listOf(pages.first()),
            anchorPosition = 0, // We are scrolling at the first item
            config = pagerConfig,
            leadingPlaceholderCount = 0
        )

        // 2. ACT: Call load directly
        val result = announcementRemoteMediator.load(LoadType.APPEND, pagingState)

        // 3. ASSERT
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        result as RemoteMediator.MediatorResult.Success
        assertThat(result.endOfPaginationReached).isFalse()
    }
    //TODO !!!!!!!!!!!!!!!!!!!!!!!!

    @Test
    fun appendReturnsEndOfPaginationWhenNextKeyIsNull() = runTest {
        fakeAnnouncementRemoteDataSource.pagedAnnouncementResponseOverride =
            AnnouncementPageResponse(
                data = emptyList(),
                meta = AnnouncementMeta(
                    currentPage = 1,
                    from = 1,
                    lastPage = 1,
                    path = "",
                    perPage = 0,
                    to = 0,
                    total = 0
                )
            )
        // 1. PREPARE: State where the last loaded page has nextKey = null


        // 2. ACT
        announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        val result = announcementRemoteMediator.load(LoadType.APPEND, pagingState)

        // 3. ASSERT
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        result as RemoteMediator.MediatorResult.Success
        assertThat(result.endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshWithEmptyDataReturnsSuccessAndEndOfPagination() = runTest {
        // 1. PREPARE: Mock API to return an empty list
        // Note: Adjust 'service.getAnnouncements' to match your actual API call signature
        // coEvery {
        //   service.getAnnouncements(any(), any())
        // } returns emptyList() // or an Empty Response object depending on your API wrapper

        fakeAnnouncementRemoteDataSource.pagedAnnouncementResponseOverride =
            AnnouncementPageResponse(
                data = emptyList(),
                meta = AnnouncementMeta(
                    currentPage = 1,
                    from = 1,
                    lastPage = 1,
                    path = "",
                    perPage = 0,
                    to = 0,
                    total = 0
                )
            )

        // 2. ACT
        val result = announcementRemoteMediator.load(LoadType.REFRESH, pagingState)

        // 3. ASSERT
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        result as RemoteMediator.MediatorResult.Success
        assertThat(result.endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshReturnsErrorOnNetworkFailure() = runTest {
        fakeAnnouncementRemoteDataSource.throwOnApiRequest = Exception("Error Occurred")
        val result = announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
    }

    @Test
    fun appendFetchesNextTest() = runTest {
        // 1. Setup Pager (Use your existing config)
        // 2. Run the Pager and simulate UI behavior
        val items: List<AnnouncementPreviewRelation> = pager.flow.asSnapshot {
            // A. Initial Load happens automatically.

            // B. Simulate scrolling to the end of the currently loaded list
            // to trigger the Mediator to APPEND the next page.
            // (Assuming you have enough mock data to fill > 60 items)
            scrollTo(index = 10)
        }

        // 3. Assertions
        // Verify the final list contains items from Page 1 AND Page 2+
        println("Final item count: ${items.size}")

        // Example: If page 1 has 20 items and page 2 has 20 items.
        // If initialLoadSize=60, it likely already fetched pages 1, 2, and 3.
        // To strictly test APPEND, lower initialLoadSize or scroll further.
        assertTrue(items.isNotEmpty())

        // Verify DB state implicitly by checking the items returned by the Pager
        assertThat(items.size).isEqualTo(pagingState.config.initialLoadSize)
        //assertEquals("Expected Item from Page 2", items[9].announcement.title)
    }


    @Test
    fun appendAtLastPage_returnsEndOfPagination() = runTest {
        // 1. Setup Dependencies
        // Use the Fake instead of mocks


        // Configuration (Must match defaults in RemoteKeys entity)
        val testQuery = ""
        val testTags = emptyList<Int>()
        val testAuthors = emptyList<Int>()
        val testSort = SortType.DESC

        val mediator = AnnouncementRemoteMediator(
            database = fakeAppDatabase,
            query = "",
            tagIds = emptyList(),
            authorIds = emptyList(),
            sortType = SortType.DESC,
            announcementRemoteDataSource = fakeAnnouncementRemoteDataSource
        )

        // 2. Prepare Data
        // We grab Page 3 (the last valid page in your test data)
        val lastPageData = announcementPageResponseTestData.last()
        val lastItem = lastPageData.data.last()

        // We simulate that the DB thinks the NEXT page is 4
        val nextPageKey = lastPageData.meta.currentPage + 1

        // 3. Seed the DB
        // Insert a key for the last item on Page 3, pointing to Page 4
        val key = RemoteKeys(
            announcementId = lastItem.id,
            searchQuery = testQuery,
            authorIds = testAuthors,
            tagIds = testTags,
            sortType = testSort,
            prevKey = lastPageData.meta.currentPage - 1,
            nextKey = nextPageKey
        )
        fakeAppDatabase.remoteKeysDao().insertOrReplaceKeys(listOf(key))

        // 4. Setup PagingState
        // Mimic the UI displaying Page 3
        val pagingState = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = lastPageData.data.map { dto ->
                        AnnouncementPreviewRelation(
                            announcement = dto.toAnnouncementEntity(),
                            author = dto.author.toAuthorEntity(),
                            tags = dto.tags.map { it.toTagEntity() },
                            attachments = dto.attachments.map { it.toAttachmentEntity() }
                        )
                    },
                    prevKey = lastPageData.meta.currentPage - 1,
                    nextKey = nextPageKey
                )
            ),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        // 5. Action
        // Mediator reads DB -> finds nextKey=4 -> calls FakeApi(page=4)
        val result = mediator.load(LoadType.APPEND, pagingState)

        // 6. Assert
        // FakeApi(page=4) returns empty list (thanks to the fix above)
        // Mediator sees empty list -> returns Success(endOfPaginationReached=true)
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()

        // Optional: Verify the Fake was actually called with Page 4
        //assertThat(fakeApi.calls).contains(nextPageKey to 10)
    }

    @Test
    fun appendFetchesNextPage_success() = runTest {
        // 1. Prepare Data
        // Assume Page 1 is already in DB. We want to load Page 2.
        val pageOneData = announcementPageResponseTestData.first() // Page 1
        val lastItemOfPage1 = pageOneData.data.last()
        val nextKey = 2

        // 2. Seed DB with RemoteKey for the last item of Page 1
        val key = RemoteKeys(
            announcementId = lastItemOfPage1.id,
            searchQuery = "",
            authorIds = emptyList(),
            tagIds = emptyList(),
            sortType = SortType.DESC,
            prevKey = null,
            nextKey = nextKey // Pointing to Page 2
        )
        fakeAppDatabase.remoteKeysDao().insertOrReplaceKeys(listOf(key))

        // 3. Setup PagingState
        // The UI currently holds Page 1
        val pagingState = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = pageOneData.data.map { dto ->
                        // Map to the RELATION, not just the entity
                        AnnouncementPreviewRelation(
                            announcement = dto.toAnnouncementEntity(),
                            author = dto.author.toAuthorEntity(),
                            tags = dto.tags.map { it.toTagEntity() },
                            attachments = dto.attachments.map { it.toAttachmentEntity() }
                        )
                    },
                    prevKey = null,
                    nextKey = nextKey
                )
            ),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )

        // 4. Action: Trigger APPEND
        val result = announcementRemoteMediator.load(LoadType.APPEND, pagingState)

        // 5. Assert
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

        // Verify Page 2 data was inserted into DB
        val test = fakeAppDatabase.announcementDao() as FakeAnnouncementDao
        val allItems = test.announcements.value
        // Should contain Page 1 (seeded via state/assumption) + Page 2 (fetched)
        // Note: Since we didn't seed the 'items' table for Page 1, just checking Page 2 exists is sufficient for the Mediator test.
        val pageTwoData = announcementPageResponseTestData[1] // Get actual Page 2 test data
        val pageTwoItem = pageTwoData.data.first().toAnnouncementEntity()

        assertTrue(allItems.any { it.id == pageTwoItem.id })
    }


    @Test
    fun handlesUnknownHostExceptionGracefully() = runTest {
        fakeAnnouncementRemoteDataSource.throwOnApiRequest = UnknownHostException("No internet")
        val result = announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue(result.throwable is UnknownHostException)
    }

    @Test
    fun handlesGenericExceptionGracefully() = runTest {
        fakeAnnouncementRemoteDataSource.throwOnApiRequest = RuntimeException("API crashed")
        val result = announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue(result.throwable is RuntimeException)
    }
}