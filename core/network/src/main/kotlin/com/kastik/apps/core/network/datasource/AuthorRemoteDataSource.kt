package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.AuthorDto

interface AuthorRemoteDataSource {
    suspend fun fetchAuthors(): List<AuthorDto>
}

internal class AuthorRemoteDataSourceImpl(
    @param:AnnRetrofit private val aboardApiClient: AboardApiClient
) : AuthorRemoteDataSource {

    override suspend fun fetchAuthors(): List<AuthorDto> =
        aboardApiClient.getAuthors()
}