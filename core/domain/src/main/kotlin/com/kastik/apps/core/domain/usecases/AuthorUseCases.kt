package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.common.extensions.removeAccents
import com.kastik.apps.core.domain.repository.AuthorRepository
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map

class GetAuthorsUseCase @Inject constructor(private val authorRepository: AuthorRepository) {
    operator fun invoke() = authorRepository.authors.map { it.toImmutableList() }
}

class SyncAuthorsUseCase @Inject constructor(private val authorRepository: AuthorRepository) {
    suspend operator fun invoke() = authorRepository.syncAuthors()
}

class GetAuthorQuickResultsUseCase
@Inject
constructor(private val authorRepository: AuthorRepository) {
    operator fun invoke(query: String) =
        authorRepository.authors.map { authors ->
            authors
                .filter {
                    it.name.removeAccents().contains(query.removeAccents(), ignoreCase = true)
                }
                .take(5)
                .toImmutableList()
        }
}
