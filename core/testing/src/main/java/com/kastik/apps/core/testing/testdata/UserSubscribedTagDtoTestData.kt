package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.network.model.aboard.PivotDto
import com.kastik.apps.core.network.model.aboard.UserSubscribedTagDto


val pivotDtoTestData = PivotDto(
    userId = 1, tagId = 1
)

val userSubscribedTagDtoTestData = UserSubscribedTagDto(
    id = 1,
    title = "Tag Name",
    parentId = 0,
    isPublic = false,
    createdAt = "25-10-2025 11:34",
    updatedAt = "25-10-2025 11:34",
    deletedAt = "25-10-2025 11:34",
    mailListName = "mail",
    pivot = pivotDtoTestData
)


val userSubscribedTagDtoListTestData = listOf(
    userSubscribedTagDtoTestData,
    userSubscribedTagDtoTestData.copy(
        id = 2,
        deletedAt = null
    ),
)