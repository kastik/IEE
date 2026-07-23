package com.kastik.apps.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.baseAuthorEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RoboDatabaseTestRunner::class)
internal class AuthorsDaoTest : MemoryDatabase() {

    @Test
    fun insertOrIgnoreAuthorsInsertsNewAuthors() = runTest {
        val authors = listOf(baseAuthorEntity)

        authorsDao.insertOrIgnoreAuthors(authors)

        val retrievedAuthors = authorsDao.getAuthors().first()
        assertThat(retrievedAuthors).containsExactly(baseAuthorEntity)
    }

    @Test
    fun insertOrIgnoreAuthorIgnoresExistingAuthors() = runTest {
        authorsDao.insertOrIgnoreAuthors(listOf(baseAuthorEntity))

        val conflictingAuthor =
            baseAuthorEntity.copy(
                name = "John Doe",
                announcementCount = 5,
            )

        authorsDao.insertOrIgnoreAuthors(listOf(conflictingAuthor))

        val retrievedAuthors = authorsDao.getAuthors().first()
        assertThat(retrievedAuthors).hasSize(1)

        val resultingAuthor = retrievedAuthors.first()
        assertThat(resultingAuthor.name).isEqualTo("John") // Not "John Doe"
        assertThat(resultingAuthor.announcementCount).isEqualTo(0)
    }

    @Test
    fun upsertListAuthorsInsertsNewAuthor() = runTest {
        val authors = baseAuthorEntity

        authorsDao.upsertAuthors(authors)

        val retrievedAuthors = authorsDao.getAuthors().first()
        assertThat(retrievedAuthors).containsExactly(baseAuthorEntity)
    }

    @Test
    fun upsertSingleAuthorInsertsNewAuthor() = runTest {
        val authors = listOf(baseAuthorEntity)

        authorsDao.upsertAuthors(authors)

        val retrievedAuthors = authorsDao.getAuthors().first()
        assertThat(retrievedAuthors).containsExactly(baseAuthorEntity)
    }

    @Test
    fun upsertListAuthorsUpdatesExisting() = runTest {
        authorsDao.upsertAuthors(listOf(baseAuthorEntity))

        val updatedAuthor =
            baseAuthorEntity.copy(
                name = "John Updated",
                announcementCount = 10,
            )

        authorsDao.upsertAuthors(listOf(updatedAuthor))

        val retrievedAuthors = authorsDao.getAuthors().first()
        assertThat(retrievedAuthors).hasSize(1)
        assertThat(retrievedAuthors.first().name).isEqualTo("John Updated")
        assertThat(retrievedAuthors.first().announcementCount).isEqualTo(10)
    }

    @Test
    fun upsertSingleAuthorsUpdatesExisting() = runTest {
        authorsDao.upsertAuthors(listOf(baseAuthorEntity))

        val updatedAuthor =
            baseAuthorEntity.copy(
                name = "Johnny",
                announcementCount = 3,
            )

        authorsDao.upsertAuthors(updatedAuthor)

        val retrievedAuthors = authorsDao.getAuthors().first()
        assertThat(retrievedAuthors).hasSize(1)
        assertThat(retrievedAuthors).containsExactly(updatedAuthor)
    }

    @Test
    fun getAuthors_returnsAllInsertedAuthors() = runTest {
        val author2 = baseAuthorEntity.copy(id = 2, name = "Alice")
        val author3 = baseAuthorEntity.copy(id = 3, name = "Bob")
        val authorsToInsert = listOf(baseAuthorEntity, author2, author3)

        authorsDao.upsertAuthors(authorsToInsert)

        val retrievedAuthors = authorsDao.getAuthors().first()

        assertThat(retrievedAuthors).containsExactlyElementsIn(authorsToInsert)
    }

    @Test
    fun clearAuthorsClearsAuthors() = runTest {
        val author2 = baseAuthorEntity.copy(id = 2, name = "Alice")
        authorsDao.upsertAuthors(listOf(baseAuthorEntity, author2))

        assertThat(authorsDao.getAuthors().first()).hasSize(2)

        authorsDao.clearAuthors()

        val retrievedAuthors = authorsDao.getAuthors().first()
        assertThat(retrievedAuthors).isEmpty()
    }
}
