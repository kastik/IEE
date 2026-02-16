package com.kastik.apps.core.testing.dao

import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.database.entities.AuthorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeAuthorsDao : AuthorsDao {
    private val _authors: MutableStateFlow<List<AuthorEntity>> = MutableStateFlow(emptyList())

    override suspend fun upsertAuthors(authors: AuthorEntity) {
        _authors.update { current ->
            val nonDuplicates = current.filter { new ->
                current.none { existing -> existing.id == new.id }
            }
            current + nonDuplicates
        }
    }

    override suspend fun upsertAuthors(authors: List<AuthorEntity>) {
        _authors.update { current ->
            val filteredCurrent = current.filter { existing ->
                authors.none { new -> new.id == existing.id }
            }
            filteredCurrent + authors
        }
    }

    override fun getAuthors(): Flow<List<AuthorEntity>> = _authors

    override suspend fun clearAuthors() {
        _authors.update {
            emptyList()
        }
    }
}