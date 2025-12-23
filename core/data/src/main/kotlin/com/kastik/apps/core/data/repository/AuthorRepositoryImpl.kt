package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toAuthor
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AuthorRepositoryImpl(
    private val authorLocalDataSource: AuthorsDao,
    private val authorRemoteDataSource: AuthorRemoteDataSource,
) : AuthorRepository {

    override fun getAuthors(): Flow<List<Author>> {
        return authorLocalDataSource.getAuthors().map { it.map { it.toAuthor() } }
    }

    override suspend fun refreshAuthors() {
        val authors = authorRemoteDataSource.fetchAuthors()
        authorLocalDataSource.insertAuthors(authors.map { it.toAuthorEntity() })
    }
}