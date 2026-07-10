package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.model.response.AuthorDto
import javax.inject.Inject
import javax.inject.Singleton

interface AuthorRemoteDataSource {
    suspend fun fetchAuthors(): List<AuthorDto>
}

@Singleton
internal class AuthorRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient
) : AuthorRemoteDataSource {

    override suspend fun fetchAuthors(): List<AuthorDto> =
        aboardApiClient.getAuthors()
}