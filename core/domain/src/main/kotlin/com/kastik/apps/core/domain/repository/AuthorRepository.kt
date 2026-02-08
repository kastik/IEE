package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface AuthorRepository {
    fun getAuthors(): Flow<List<Author>>
    suspend fun refreshAuthors(): Result<Unit, GeneralRefreshError>
}