package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.response.ProfileDto
import kotlin.time.Instant

val baseProfileDto =
    ProfileDto(
        id = 0,
        uid = "it192168",
        name = "Some Name",
        email = "someemail@domain.com",
        isAdmin = false,
        isAuthor = false,
        createdAt = Instant.fromEpochSeconds(1782774327),
        lastLoginAt = Instant.fromEpochSeconds(1782774327),
    )
