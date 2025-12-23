package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.model.aboard.Author
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAuthorsUseCase @Inject constructor(
    private val authorRepository: AuthorRepository
) {
    operator fun invoke(): Flow<List<Author>> =
        authorRepository.getAuthors()
}

class RefreshAuthorsUseCase @Inject constructor(
    private val authorRepository: AuthorRepository
) {
    suspend operator fun invoke() =
        authorRepository.refreshAuthors()
}

class GetAuthorQuickResultsUseCase @Inject constructor(
    private val authorRepository: AuthorRepository
) {
    operator fun invoke(query: String): Flow<List<Author>> =
        authorRepository.getAuthors().map { authors ->
            authors.filter {
                it.name.contains(query, ignoreCase = true)
            }.take(5)

        }
}