package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface AuthorRepository {
    val authors: Flow<List<Author>>

    suspend fun syncAuthors(): Result<Unit, NetworkError>
}
