package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.author.AuthorResponseDto
import javax.inject.Inject

interface AuthorRemoteDataSource {
    suspend fun fetchAuthors(): List<AuthorResponseDto>
}

internal class AuthorRemoteDataSourceImpl @Inject constructor(
    private val aboardApiClient: AboardApiClient
) : AuthorRemoteDataSource {

    override suspend fun fetchAuthors(): List<AuthorResponseDto> =
        aboardApiClient.getAuthors()
}