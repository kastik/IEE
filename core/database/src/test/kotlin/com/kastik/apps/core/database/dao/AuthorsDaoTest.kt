package com.kastik.apps.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.announcementAuthorEntityTestData
import com.kastik.apps.core.testing.testdata.authorEntitiesTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertNotEquals


@RunWith(RoboDatabaseTestRunner::class)
internal class AuthorsDaoTest : MemoryDatabase() {

    @Test
    fun insertAuthorsListAddsAuthors() = runTest {
        val authorEntities = authorEntitiesTestData

        authorsDao.insertOrIgnoreAuthors(authorEntities)

        val result = authorsDao.getAuthors().first()
        val uniqueAuthors = result.distinct()
        assertThat(uniqueAuthors).containsExactlyElementsIn(result)
    }

    @Test
    fun insertAuthorsListOverridesExistingOrIgnoreAuthors() = runTest {
        val initialAuthors = announcementAuthorEntityTestData
        val newAuthors = announcementAuthorEntityTestData.map { it.copy(id = 100) }

        authorsDao.insertOrIgnoreAuthors(initialAuthors)
        authorsDao.insertOrIgnoreAuthors(newAuthors)

        val result = authorsDao.getAuthors().first()

        assertThat(result).containsExactlyElementsIn(newAuthors)

        initialAuthors.zip(newAuthors).forEach { (initial, new) ->
            assertNotEquals(new.id, initial.id)
            assertEquals(new.name, initial.name)
        }
    }

    @Test
    fun insertSingleAuthorInsertsAuthor() = runTest {
        val authorEntities = authorEntitiesTestData
        val firstAuthor = authorEntities.first()
        authorsDao.insertOrIgnoreAuthors(listOf(firstAuthor))

        val result = authorsDao.getAuthors().first()

        assertEquals(authorEntitiesTestData.first(), result.first())
    }

    @Test
    fun insertSingleAuthorDoesNotOverrideAuthor() = runTest {
        val authorEntities = authorEntitiesTestData
        val firstAuthor = authorEntities.first()
        authorsDao.insertOrIgnoreAuthors(listOf(firstAuthor))

        val newAuthor = authorEntities.first().copy(
            id = 100,
            name = "Jane Dough"
        )

        val result = authorsDao.getAuthors().first()

        assertNotEquals(newAuthor, result.first())
    }


    @Test
    fun clearAuthorsRemovesAuthors() = runTest {
        val authorEntities = authorEntitiesTestData

        authorsDao.insertOrIgnoreAuthors(authorEntities)
        authorsDao.clearAuthors()

        val result = authorsDao.getAuthors().first()
        assertEquals(emptyList<AuthorEntity>(), result)
    }
}