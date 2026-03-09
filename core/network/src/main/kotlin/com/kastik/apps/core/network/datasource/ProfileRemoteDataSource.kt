package com.kastik.apps.core.network.datasource


import com.kastik.apps.core.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.profile.ProfileResponseDto
import javax.inject.Inject

interface ProfileRemoteDataSource {
    suspend fun getProfile(): ProfileResponseDto
}

internal class ProfileRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient,
) : ProfileRemoteDataSource {

    override suspend fun getProfile(): ProfileResponseDto {
        return aboardApiClient.getUserInfo()
    }

}