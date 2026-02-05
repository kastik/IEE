package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import com.kastik.apps.core.network.model.aboard.author.AuthorResponseDto

class FakeAuthorRemoteDataSource : AuthorRemoteDataSource {

    var authorsToReturn: List<AuthorResponseDto> = emptyList()

    override suspend fun fetchAuthors(): List<AuthorResponseDto> {
        return authorsToReturn
    }
}