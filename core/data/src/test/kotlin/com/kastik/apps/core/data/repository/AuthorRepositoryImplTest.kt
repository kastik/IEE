package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.crashlytics.FakeCrashlytics
import com.kastik.apps.core.data.mappers.toAuthor
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import com.kastik.apps.core.network.datasource.FakeAuthorRemoteDataSource
import com.kastik.apps.core.testing.dao.FakeAuthorsDao
import com.kastik.apps.core.testing.testdata.baseAuthorEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class AuthorRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeCrashlytics = FakeCrashlytics()
    private val fakeAuthorLocalDataSource = FakeAuthorsDao()
    private val fakeAuthorRemoteDataSource = FakeAuthorRemoteDataSource()
    private val repository = AuthorRepositoryImpl(
        crashlytics = fakeCrashlytics,
        authorLocalDataSource = fakeAuthorLocalDataSource,
        authorRemoteDataSource = fakeAuthorRemoteDataSource,
        ioDispatcher = testDispatcher,
    )

    @Test
    fun authorsReturnsEmptyWhenNoAuthorsSaved() = runTest(testDispatcher) {
        val result = repository.authors.first()
        assertThat(result).isEmpty()
    }

    @Test
    fun authorsReturnsMappedAuthorsWhenSaved() = runTest(testDispatcher) {
        val authors = listOf(baseAuthorEntity)
        fakeAuthorLocalDataSource.upsertAuthors(authors)

        val result = repository.authors.first()

        assertThat(result).isNotEmpty()
        // Ensure we compare domain mapped models against the result
        assertThat(result).containsExactlyElementsIn(authors.map { it.toAuthor() })
    }

    @Test
    fun syncAuthorsFetchesFromRemoteAndSavesToLocal() = runTest(testDispatcher) {
        // Fetch the data straight from the fake API client representation
        val remoteAuthors = fakeAuthorRemoteDataSource.fetchAuthors()

        val result = repository.syncAuthors()

        assertThat(result).isInstanceOf(Result.Success::class.java)

        val localSavedAuthors = repository.authors.first()
        val expectedDomainAuthors = remoteAuthors.map { it.toAuthorEntity().toAuthor() }

        assertThat(localSavedAuthors).isNotEmpty()
        assertThat(localSavedAuthors).containsExactlyElementsIn(expectedDomainAuthors)
    }

    @Test
    fun authorsFlowEmitsUpdatesWhenDataChanges() = runTest(testDispatcher) {
        val emissions = mutableListOf<List<Author>>()

        // UnconfinedTestDispatcher is perfect for eagerly collecting StateFlow updates in tests
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.authors.collect { authors ->
                emissions.add(authors)
            }
        }

        assertThat(emissions).hasSize(1)
        assertThat(emissions.last()).isEmpty()

        repository.syncAuthors()

        assertThat(emissions).hasSize(2)

        val expectedDomain = fakeAuthorRemoteDataSource.fetchAuthors()
            .map { it.toAuthorEntity().toAuthor() }

        assertThat(emissions.last()).containsExactlyElementsIn(expectedDomain)

        job.cancel()
    }

    @Test
    fun syncAuthorsReturnsErrorOnException() = runTest(testDispatcher) {
        // Use delegation to force an error purely for this test without modifying the base Fake
        val errorRepo = AuthorRepositoryImpl(
            crashlytics = fakeCrashlytics,
            authorLocalDataSource = fakeAuthorLocalDataSource,
            authorRemoteDataSource = object : AuthorRemoteDataSource by fakeAuthorRemoteDataSource {
                override suspend fun fetchAuthors() = throw RuntimeException("Network Error")
            },
            ioDispatcher = testDispatcher
        )

        val result = errorRepo.syncAuthors()

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun syncAuthorsRethrowsCancellationException() = runTest(testDispatcher) {
        val errorRepo = AuthorRepositoryImpl(
            crashlytics = fakeCrashlytics,
            authorLocalDataSource = fakeAuthorLocalDataSource,
            authorRemoteDataSource = object : AuthorRemoteDataSource by fakeAuthorRemoteDataSource {
                override suspend fun fetchAuthors() = throw CancellationException()
            },
            ioDispatcher = testDispatcher
        )

        var caughtCancellation = false
        try {
            errorRepo.syncAuthors()
        } catch (e: CancellationException) {
            caughtCancellation = true
        }

        assertThat(caughtCancellation).isTrue()
    }

}


