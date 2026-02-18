package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.model.aboard.author.AuthorResponseDto

class FakeAuthorRemoteDataSource : AuthorRemoteDataSource {

    var authorsToReturn: List<AuthorResponseDto> = emptyList()

    override suspend fun fetchAuthors(): List<AuthorResponseDto> {
        return authorsToReturn
    }
}