package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.aboard.UserDataDto

val userDataDto = UserDataDto(
    id = 12
)

val aboardAuthTokenDtoTestData = AboardAuthTokenDto(
    accessToken = "TheToken",
    tokenType = "Bearer",
    userData = userDataDto,
    expiresIn = 6400
)

