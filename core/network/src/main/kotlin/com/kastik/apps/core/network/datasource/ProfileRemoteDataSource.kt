package com.kastik.apps.core.network.datasource


import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.model.response.ProfileDto
import javax.inject.Inject

interface ProfileRemoteDataSource {
    suspend fun getProfile(): ProfileDto
}

internal class ProfileRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient,
) : ProfileRemoteDataSource {

    override suspend fun getProfile(): ProfileDto {
        return aboardApiClient.getCurrentUser()
    }

}