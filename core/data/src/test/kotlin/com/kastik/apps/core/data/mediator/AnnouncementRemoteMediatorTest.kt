package com.kastik.apps.core.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediator
import com.kastik.apps.core.database.model.AnnouncementWithoutBody
import com.kastik.apps.core.testing.datasource.remote.FakeAnnouncementRemoteDataSource
import com.kastik.apps.core.testing.db.FakeAppDatabase
import com.kastik.apps.core.testing.testdata.announcementResponses
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediatorTest {

    private lateinit var remote: FakeAnnouncementRemoteDataSource
    private lateinit var db: FakeAppDatabase
    private lateinit var mediator: AnnouncementRemoteMediator
    private lateinit var pagingState: PagingState<Int, AnnouncementWithoutBody>

    @Before
    fun setUp() {
        db = FakeAppDatabase()
        remote = FakeAnnouncementRemoteDataSource()
        mediator = AnnouncementRemoteMediator(remote, null, db)

        pagingState = PagingState(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0
        )
    }

    @Test
    fun initializeSkipsRefreshTest() = runTest {
        val result = mediator.initialize()
        assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, result)
    }

    @Test
    fun refreshClearsDbAndFetchesFirstPageTest() = runTest {
        mediator.load(LoadType.REFRESH, pagingState) // Initializer mediator data
        mediator.load(LoadType.APPEND, pagingState)  // Add some data
        val result =
            mediator.load(LoadType.REFRESH, pagingState) //Re run refresh to check the result
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result.endOfPaginationReached))
        assertTrue(db.fakeAnnouncementDao.clearAllCalled)
        assertEquals(3, remote.calls.size)
        assertEquals(
            announcementResponses.first().data.first().id,
            db.fakeAnnouncementDao.announcements.first().id
        )
    }


    @Test
    fun prependEndsPaginationTest() = runTest {
        val result = mediator.load(LoadType.PREPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(result.endOfPaginationReached)
        assertTrue(remote.calls.isEmpty())
    }

    @Test
    fun appendFetchesNextPageTest() = runTest {
        var result = mediator.load(LoadType.REFRESH, pagingState) //Page 1
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse(result.endOfPaginationReached)
        result = mediator.load(LoadType.APPEND, pagingState) //Page 2
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse(result.endOfPaginationReached)
        result = mediator.load(LoadType.APPEND, pagingState) //Page 3
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(3, remote.calls.last().first)
    }

    @Test
    fun appendFetchesNextPageDynamicTest() = runTest {
        for (i in 1..announcementResponses.lastIndex) {
            if (i == announcementResponses.lastIndex) {
                val result = mediator.load(LoadType.APPEND, pagingState)
                assertTrue(result is RemoteMediator.MediatorResult.Success)
                assertTrue(result.endOfPaginationReached)
            } else {
                val result = mediator.load(LoadType.APPEND, pagingState)
                assertTrue(result is RemoteMediator.MediatorResult.Success)
                assertFalse(result.endOfPaginationReached)
            }
        }
    }

    @Test
    fun appendWorksTillLastPageTest() = runTest {
        for (i in 0..announcementResponses.lastIndex) {
            val result = mediator.load(LoadType.APPEND, pagingState)
            assertTrue(result is RemoteMediator.MediatorResult.Success)
        }
        val result = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(result.endOfPaginationReached)
    }


    @Test
    fun handlesUnknownHostExceptionGracefully() = runTest {
        remote.setError(UnknownHostException("No internet"))
        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue(result.throwable is UnknownHostException)
        assertFalse(db.fakeAnnouncementDao.clearAllCalled)
    }

    @Test
    fun handlesGenericExceptionGracefully() = runTest {
        remote.setError(RuntimeException("API crashed"))
        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue(result.throwable is RuntimeException)
        assertFalse(db.fakeAnnouncementDao.clearAllCalled)
    }
}



