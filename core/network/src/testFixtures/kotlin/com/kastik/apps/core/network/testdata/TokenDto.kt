package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.response.TokenDto

val baseTokenDto = TokenDto(
    accessToken = "Token",
    tokenType = "Bearer",
    expiresIn = 6400,
)