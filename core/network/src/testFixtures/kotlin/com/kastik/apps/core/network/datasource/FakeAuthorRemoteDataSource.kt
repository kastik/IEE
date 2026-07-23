package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.FakeAboardApiClient
import com.kastik.apps.core.network.model.response.AuthorDto

class FakeAuthorRemoteDataSource : AuthorRemoteDataSource {

    private val fakeAboardApiClient = FakeAboardApiClient()

    override suspend fun fetchAuthors(): List<AuthorDto> = fakeAboardApiClient.getAuthors()
}
