package com.kastik.apps.core.testing.api

import com.kastik.apps.core.network.api.AppsApiClient
import com.kastik.apps.core.network.model.apps.AppsAuthTokenDto
import com.kastik.apps.core.testing.testdata.appsAuthTokenDtoTestData

class FakeAppsApiClient : AppsApiClient {

    override suspend fun exchangeCodeForAppsToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        code: String
    ): AppsAuthTokenDto {
        return appsAuthTokenDtoTestData
    }
}
