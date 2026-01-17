package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.aboard.UserDataDto
import com.kastik.apps.core.network.model.apps.AppsAuthTokenDto

val appsAuthTokenDtoTestData = AppsAuthTokenDto(
    accessToken = "token",
    userId = "it192168",
    refreshToken = "refresh",
)

val aboardAuthTokenDtoTestData = AboardAuthTokenDto(
    accessToken = "Token",
    tokenType = "Bearer",
    userData = UserDataDto(id = 12),
    expiresIn = 6400,
)

