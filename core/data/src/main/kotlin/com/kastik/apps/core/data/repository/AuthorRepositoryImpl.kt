package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toAuthor
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.data.mappers.toNetworkError
import com.kastik.apps.core.data.utils.safeCall
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthorRepositoryImpl
@Inject
constructor(
    private val crashlytics: Crashlytics,
    private val authorLocalDataSource: AuthorsDao,
    private val authorRemoteDataSource: AuthorRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthorRepository {

    override val authors: Flow<List<Author>> =
        authorLocalDataSource.getAuthors().map { it.map { it.toAuthor() } }

    override suspend fun syncAuthors() =
        withContext(ioDispatcher) {
            safeCall(
                mapException = Exception::toNetworkError,
                recordException = crashlytics::recordException,
            ) {
                val authors = authorRemoteDataSource.fetchAuthors()
                authorLocalDataSource.upsertAuthors(authors.map { it.toAuthorEntity() })
            }
        }
}
