package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.crashlytics.FakeCrashlytics
import com.kastik.apps.core.data.mappers.toAuthor
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.datasource.FakeAuthorRemoteDataSource
import com.kastik.apps.core.network.testdata.authorDtoTestData
import com.kastik.apps.core.testing.dao.FakeAuthorsDao
import com.kastik.apps.core.testing.testdata.announcementAuthorEntityTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class AuthorRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeAuthorLocalDataSource = FakeAuthorsDao()
    private val fakeAuthorRemoteDataSource = FakeAuthorRemoteDataSource()
    private val repository = AuthorRepositoryImpl(
        crashlytics = FakeCrashlytics(),
        authorLocalDataSource = fakeAuthorLocalDataSource,
        authorRemoteDataSource = fakeAuthorRemoteDataSource,
        ioDispatcher = testDispatcher,
    )

    @Test
    fun getAuthorsReturnsEmptyWhenNoAuthorsSaved() = runTest(testDispatcher) {
        assertThat(repository.getAuthors().first()).isEmpty()
    }

    @Test
    fun getAuthorsReturnsAuthorsWhenSaved() = runTest(testDispatcher) {
        val authors = announcementAuthorEntityTestData
        fakeAuthorLocalDataSource.upsertAuthors(authors)

        assertThat(repository.getAuthors().first()).isNotEmpty()

        val authorsDomain = authors.map { it.toAuthor() }
        assertThat(repository.getAuthors().first()).containsExactlyElementsIn(authorsDomain)
    }

    @Test
    fun refreshAuthorsFetchesFromRemoteAndSavesToLocal() = runTest(testDispatcher) {
        val authors = authorDtoTestData
        fakeAuthorRemoteDataSource.authorsToReturn = authors
        repository.refreshAuthors()
        val expectedDomain = authors.map { it.toAuthorEntity().toAuthor() }

        assertThat(repository.getAuthors().first())
            .containsExactlyElementsIn(expectedDomain)
    }

    @Test
    fun getAuthorsFlowEmitsUpdatesWhenDataChanges() = runTest(testDispatcher) {
        val emissions = mutableListOf<List<Author>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.getAuthors().collect { authors ->
                emissions.add(authors)
            }
        }

        assertThat(emissions).hasSize(1)
        assertThat(emissions.last()).isEmpty()

        val newAuthors = authorDtoTestData
        fakeAuthorRemoteDataSource.authorsToReturn = newAuthors
        repository.refreshAuthors()

        assertThat(emissions).hasSize(2)

        val expectedDomain = newAuthors.map { it.toAuthorEntity().toAuthor() }
        assertThat(emissions.last()).containsExactlyElementsIn(expectedDomain)
    }


    @Test
    fun `getAnnouncementTags emits mapped data from local source`() = runTest(testDispatcher) {
        val result = repository.getAuthors().first()
        assertEquals(0, result.size)
        val test = fakeAuthorRemoteDataSource.fetchAuthors()
        fakeAuthorLocalDataSource.upsertAuthors(test.map { it.toAuthorEntity() })
        assertEquals(test.size, result.size)
    }

    @Test
    fun `refreshAnnouncementTags fetches remote and saves to local`() = runTest(testDispatcher) {
        val test = fakeAuthorRemoteDataSource.fetchAuthors()
        repository.refreshAuthors()
        assertEquals(test.size, repository.getAuthors().first().size)
    }

}


