package com.kastik.apps.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.announcementAuthorEntityTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(RoboDatabaseTestRunner::class)
internal class AuthorsDaoTest : MemoryDatabase() {

    @Test
    fun upsertListAuthorsInsertsNewAuthor() = runTest {
        val authorEntities = announcementAuthorEntityTestData
        authorsDao.upsertAuthors(authorEntities)

        val result = authorsDao.getAuthors().first()

        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(result)
    }

    @Test
    fun upsertSingleAuthorInsertsNewAuthor() = runTest {
        val authorEntity = announcementAuthorEntityTestData.first()
        authorsDao.upsertAuthors(authorEntity)

        val result = authorsDao.getAuthors().first()

        assertThat(result).hasSize(1)
        assertThat(result.first()).isEqualTo(authorEntity)
    }

    @Test
    fun upsertListAuthorsUpdatesExisting() = runTest {
        val authorEntities = announcementAuthorEntityTestData
        authorsDao.upsertAuthors(authorEntities)

        val updatedAuthors =
            announcementAuthorEntityTestData.map { it.copy(announcementCount = 500) }
        authorsDao.upsertAuthors(updatedAuthors)

        val result = authorsDao.getAuthors().first()
        assertThat(result).isNotEmpty()
        assertThat(result).containsExactlyElementsIn(updatedAuthors)
    }

    @Test
    fun upsertSingleAuthorsUpdatesExisting() = runTest {
        val authorEntity = announcementAuthorEntityTestData.first()
        authorsDao.upsertAuthors(authorEntity)

        val updatedAuthor = authorEntity.copy(announcementCount = 500)
        authorsDao.upsertAuthors(updatedAuthor)

        val result = authorsDao.getAuthors().first()

        assertThat(result).hasSize(1)
        assertThat(result.first()).isEqualTo(updatedAuthor)
    }

    @Test
    fun clearAuthorsClearsAuthorTable() = runTest {
        val authorEntities = announcementAuthorEntityTestData
        authorsDao.upsertAuthors(authorEntities)
        authorsDao.clearAuthors()

        val result = authorsDao.getAuthors().first()
        assertThat(result).isEmpty()
    }
}