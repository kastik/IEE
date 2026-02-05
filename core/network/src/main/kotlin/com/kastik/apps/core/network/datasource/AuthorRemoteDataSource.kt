package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.author.AuthorResponseDto

interface AuthorRemoteDataSource {
    suspend fun fetchAuthors(): List<AuthorResponseDto>
}

internal class AuthorRemoteDataSourceImpl(
    @AnnRetrofit private val aboardApiClient: AboardApiClient
) : AuthorRemoteDataSource {

    override suspend fun fetchAuthors(): List<AuthorResponseDto> =
        aboardApiClient.getAuthors()
}