package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.network.model.aboard.UserProfileDto

val studentUserProfileDto = UserProfileDto(
    id = 1,
    name = "Some Name",
    nameEng = "",
    email = "someemail@domain.com",
    createdAt = "25-10-2025 13:14",
    updatedAt = "25-10-2025 13:14",
    isAuthor = false,
    isAdmin = false,
    lastLoginAt = "25-10-2025 13:14",
    uid = "it192168",
    deletedAt = ""
)

val authorUserProfileDto = UserProfileDto(
    id = 2,
    name = "Some Name",
    nameEng = "",
    email = "someemail@domain.com",
    createdAt = "25-10-2025 13:14",
    updatedAt = "25-10-2025 13:14",
    isAuthor = true,
    isAdmin = false,
    lastLoginAt = "25-10-2025 13:14",
    uid = "it192168",
    deletedAt = ""
)

val adminUserProfileDto = UserProfileDto(
    id = 3,
    name = "Some Name",
    nameEng = "",
    email = "someemail@domain.com",
    createdAt = "25-10-2025 13:14",
    updatedAt = "25-10-2025 13:14",
    isAuthor = false,
    isAdmin = true,
    lastLoginAt = "25-10-2025 13:14",
    uid = "it192168",
    deletedAt = ""
)

val userWithoutGreekName = UserProfileDto(
    id = 4,
    name = "",
    nameEng = "Some name",
    email = "someemail@domain.com",
    createdAt = "25-10-2025 13:14",
    updatedAt = "25-10-2025 13:14",
    isAuthor = false,
    isAdmin = false,
    lastLoginAt = "25-10-2025 13:14",
    uid = "it192168",
    deletedAt = ""
)


val userProfilesTestData = listOf(
    studentUserProfileDto,
    authorUserProfileDto,
    userWithoutGreekName
)