package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toAuthor
import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class AuthorRepositoryImpl(
    private val authorRemoteDataSource: AuthorRemoteDataSource,
) : AuthorRepository {

    override suspend fun getAuthors(): Flow<List<Author>> {
        //TODO Make this offline first by using room db
        return flowOf(authorRemoteDataSource.fetchAuthors().map { it.toAuthor() })
    }
}