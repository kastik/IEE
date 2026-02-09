package com.kastik.apps.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.R
import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.data.mappers.toAnnouncementEntity
import com.kastik.apps.core.data.mappers.toAttachmentEntity
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.model.aboard.announcement.PagedAnnouncementsResponseDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedMetaResponseDto
import com.kastik.apps.core.testing.datasource.remote.FakeAnnouncementRemoteDataSource
import com.kastik.apps.core.testing.db.FakeAppDatabase
import com.kastik.apps.core.testing.testdata.announcementDtoTestData
import com.kastik.apps.core.testing.testdata.announcementPageResponseTestData
import com.kastik.apps.core.testing.testdata.announcementPreviewRelationTestData
import com.kastik.apps.core.testing.testdata.tagEntitiesTestData
import com.kastik.apps.core.testing.utils.FakeBase64ImageExtractor
import com.kastik.apps.core.testing.utils.FakeCrashlytics
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediatorTest {
    private val fakeAppDatabase: FakeAppDatabase = FakeAppDatabase()
    private val fakeBase64ImageExtractor = FakeBase64ImageExtractor()
    private val fakeAnnouncementRemoteDataSource = FakeAnnouncementRemoteDataSource()

    private val pagerConfig = PagingConfig(
        pageSize = 10,
        initialLoadSize = 60,
        prefetchDistance = 10,
        enablePlaceholders = true
    )

    private val announcementRemoteMediator = AnnouncementRemoteMediator(
        crashlytics = FakeCrashlytics(),
        database = fakeAppDatabase,
        announcementRemoteDataSource = fakeAnnouncementRemoteDataSource,
        base64ImageExtractor = fakeBase64ImageExtractor
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
    fun refreshWithEmptyDataReturnsSuccessAndEndOfPagination() = runTest {
        fakeAnnouncementRemoteDataSource.pagedAnnouncementResponseOverride =
            PagedAnnouncementsResponseDto(
                data = emptyList(),
                meta = PagedMetaResponseDto(
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
        pagingState = PagingState(
            pages = listOf(pages.first()),
            anchorPosition = 0,
            config = pagerConfig,
            leadingPlaceholderCount = 0
        )

        val result = announcementRemoteMediator.load(LoadType.APPEND, pagingState)

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        result as RemoteMediator.MediatorResult.Success
        assertThat(result.endOfPaginationReached).isFalse()
    }

    @Test
    fun appendReturnsEndOfPaginationWhenNoMoreDataIsAvailable() = runTest {
        fakeAnnouncementRemoteDataSource.pagedAnnouncementResponseOverride =
            PagedAnnouncementsResponseDto(
                data = announcementDtoTestData,
                meta = PagedMetaResponseDto(
                    currentPage = 1,
                    from = 1,
                    lastPage = 1,
                    path = "",
                    perPage = announcementDtoTestData.size,
                    to = announcementDtoTestData.size,
                    total = announcementDtoTestData.size
                )
            )

        val pagingState = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = announcementPreviewRelationTestData,
                    prevKey = 1,
                    nextKey = null
                )
            ),
            anchorPosition = 0,
            config = PagingConfig(pageSize = announcementDtoTestData.size),
            leadingPlaceholderCount = 0
        )

        announcementRemoteMediator.load(LoadType.REFRESH, pagingState)
        val result = announcementRemoteMediator.load(LoadType.APPEND, pagingState)

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        result as RemoteMediator.MediatorResult.Success
        assertThat(result.endOfPaginationReached).isTrue()
    }


    @Test
    fun handlesExceptionGracefully() = runTest {
        fakeAnnouncementRemoteDataSource.throwOnApiRequest = RuntimeException("API crashed")
        val result = announcementRemoteMediator.load(LoadType.REFRESH, pagingState)

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
        result as RemoteMediator.MediatorResult.Error
        assertThat(result.throwable).isInstanceOf(Throwable::class.java)
    }
}