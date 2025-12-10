package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.model.aboard.Author
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthorsUseCase @Inject constructor(
    private val authorRepository: AuthorRepository
) {
    suspend operator fun invoke(): Flow<List<Author>> =
        authorRepository.getAuthors()
}