package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.Author
import kotlinx.coroutines.flow.Flow

interface AuthorRepository {
    fun getAuthors(): Flow<List<Author>>
    suspend fun refreshAuthors()
}