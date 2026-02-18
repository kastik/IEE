package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.aboard.profile.ProfileResponseDto

internal val baseProfileResponseDto = ProfileResponseDto(
    id = 0,
    name = "Some Name",
    nameEng = "Eng name",
    email = "someemail@domain.com",
    createdAt = "25-10-2025 13:14",
    updatedAt = "25-10-2025 13:14",
    isAuthor = false,
    isAdmin = false,
    lastLoginAt = "25-10-2025 13:14",
    uid = "it192168",
    deletedAt = ""
)
val userProfileDtoTestData = listOf(
    baseProfileResponseDto.copy(id = 1),
    baseProfileResponseDto.copy(id = 2, isAuthor = true),
    baseProfileResponseDto.copy(id = 3, isAdmin = true),
    baseProfileResponseDto.copy(id = 4, deletedAt = null)
)

