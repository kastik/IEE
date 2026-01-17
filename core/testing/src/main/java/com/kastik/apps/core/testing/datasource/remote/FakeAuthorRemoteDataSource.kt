package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import com.kastik.apps.core.network.model.aboard.AuthorDto

class FakeAuthorRemoteDataSource : AuthorRemoteDataSource {

    var authorsToReturn: List<AuthorDto> = emptyList()

    override suspend fun fetchAuthors(): List<AuthorDto> {
        return authorsToReturn
    }
}