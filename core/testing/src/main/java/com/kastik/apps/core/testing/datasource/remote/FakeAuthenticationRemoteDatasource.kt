package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.apps.AppsAuthTokenDto
import com.kastik.apps.core.testing.testdata.aboardAuthTokenDtoTestData
import com.kastik.apps.core.testing.testdata.appsAuthTokenDtoTestData
import com.kastik.apps.core.testing.testdata.userProfilesTestData
import com.kastik.apps.core.testing.testdata.userSubscribedTagDtoListTestData

class FakeAuthenticationRemoteDatasource : AuthenticationRemoteDataSource {

    private var appsAccessToken: AppsAuthTokenDto? = null
    private var aboardAccessToken: AboardAuthTokenDto? = null


    override suspend fun exchangeCodeForAppsToken(code: String): AppsAuthTokenDto {
        appsAccessToken = appsAuthTokenDtoTestData
        return appsAuthTokenDtoTestData
    }

    override suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto {
        aboardAccessToken = aboardAuthTokenDtoTestData
        return aboardAuthTokenDtoTestData
    }

    override suspend fun checkIfTokenIsValid(): Boolean = aboardAccessToken != null

    override suspend fun getUserProfile() =
        if (aboardAccessToken != null) userProfilesTestData.first() else {
            throw Exception("Not logged in")
        }

    override suspend fun getUserSubscriptions() =
        if (aboardAccessToken != null) userSubscribedTagDtoListTestData else {
            throw Exception("Not logged in")
        }
}
