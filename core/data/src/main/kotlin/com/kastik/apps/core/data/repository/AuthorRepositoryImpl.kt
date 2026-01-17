package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toAuthor
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthorRepositoryImpl @Inject constructor(
    private val authorLocalDataSource: AuthorsDao,
    private val authorRemoteDataSource: AuthorRemoteDataSource,
) : AuthorRepository {

    override fun getAuthors(): Flow<List<Author>> {
        return authorLocalDataSource.getAuthors().map { it.map { it.toAuthor() } }
    }

    override suspend fun refreshAuthors() = withContext(Dispatchers.IO) {
        val authors = authorRemoteDataSource.fetchAuthors()
        authorLocalDataSource.insertOrReplaceAuthors(authors.map { it.toAuthorEntity() })
    }
}