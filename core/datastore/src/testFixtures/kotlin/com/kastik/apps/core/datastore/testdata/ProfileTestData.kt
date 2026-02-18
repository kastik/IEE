package com.kastik.apps.core.datastore.testdata

import com.kastik.apps.core.datastore.proto.ProfileProto

internal fun baseProfileProtoBuilder() = ProfileProto.newBuilder()
    .setId(0)
    .setName("Some Name")
    .setEmail("someemail@domain.com")
    .setCreatedAt("25-10-2025 13:14")
    .setUpdatedAt("25-10-2025 13:14")
    .setIsAuthor(false)
    .setIsAdmin(false)
    .setLastLoginAt("25-10-2025 13:14")
    .setUid("it192168")
    .setDeletedAt("")

val userProfileProtoTestData = listOf(
    baseProfileProtoBuilder()
        .setId(1)
        .build(),
    baseProfileProtoBuilder()
        .setId(2)
        .setIsAuthor(true)
        .build(),
    baseProfileProtoBuilder()
        .setId(3)
        .setIsAdmin(true)
        .build(),
    baseProfileProtoBuilder()
        .setId(4)
        .clearDeletedAt() // Simulates the null/empty behavior
        .build()
)