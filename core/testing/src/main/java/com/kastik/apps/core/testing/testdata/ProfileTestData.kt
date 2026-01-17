package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.data.mappers.toProfileProto
import com.kastik.apps.core.network.model.aboard.UserProfileDto

private val baseUserProfileDto = UserProfileDto(
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
    baseUserProfileDto.copy(id = 1),
    baseUserProfileDto.copy(id = 2, isAuthor = true),
    baseUserProfileDto.copy(id = 3, isAdmin = true),
    baseUserProfileDto.copy(id = 4, deletedAt = null)
)

val userProfileProtoTestData = userProfileDtoTestData.map { it.toProfileProto() }