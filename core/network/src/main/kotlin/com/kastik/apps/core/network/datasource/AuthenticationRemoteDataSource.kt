package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.model.request.SubscribeDto
import com.kastik.apps.core.network.model.response.TagResponseDto
import com.kastik.apps.core.network.model.response.TokenDto
import javax.inject.Inject

interface AuthenticationRemoteDataSource {
    suspend fun exchangeCodeForAboardToken(code: String): TokenDto
    suspend fun checkIfTokenIsValid(): Boolean
    suspend fun fetchSubscriptions(): List<TagResponseDto>
    suspend fun subscribeToTags(tagIds: List<Int>)
}


internal class AuthenticationRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient,
) : AuthenticationRemoteDataSource {

    override suspend fun exchangeCodeForAboardToken(code: String): TokenDto =
        aboardApiClient.exchangeAuthCode(code = code)

    override suspend fun checkIfTokenIsValid(): Boolean {
            aboardApiClient.getCurrentUser()
        return true
    }

    override suspend fun fetchSubscriptions(): List<TagResponseDto> {
        return aboardApiClient.getSubscribedTags()
    }

    override suspend fun subscribeToTags(tagIds: List<Int>) {
        aboardApiClient.subscribeToTags(SubscribeDto(tagIds))
    }

}