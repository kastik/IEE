package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.FakeAboardApiClient
import com.kastik.apps.core.network.model.response.ProfileDto

class FakeProfileRemoteDataSource : ProfileRemoteDataSource {

    var profileOverride: ProfileDto? = null
    var exceptionToThrow: Exception? = null
    private val fakeAboardApiClient = FakeAboardApiClient()

    override suspend fun getProfile() = exceptionToThrow?.let { exception ->
        throw exception
    } ?: profileOverride ?: fakeAboardApiClient.getCurrentUser()

}