package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.network.model.aboard.auth.AboardTokenResponseDto

val aboardTokenResponseDtoTestData = AboardTokenResponseDto(
    accessToken = "Token",
    tokenType = "Bearer",
    expiresIn = 6400,
)

