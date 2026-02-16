package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.aboard.tags.SubscribableTagsResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagsResponseDto

internal val baseTagResponseDtoTestData = TagResponseDto(
    id = 1,
    title = "Root Tag",
    parentId = null,
    isPublic = true,
    mailListName = "root_tag_mail_list"
)

val announcementTagDtoTestData = listOf(
    baseTagResponseDtoTestData.copy(id = 2, parentId = 1, title = "Sub Tag 1"),
    baseTagResponseDtoTestData.copy(id = 3, parentId = 1, title = "Sub Tag 2"),
    baseTagResponseDtoTestData.copy(id = 4, parentId = 2, title = "Sub Tag 3"),
    baseTagResponseDtoTestData.copy(id = 5, parentId = 2, title = "Sub Tag 4"),
)

val tagsResponseDtoTestData = TagsResponseDto(
    data = announcementTagDtoTestData
)

internal val baseSubscribableTagsResponseDtoTestData = SubscribableTagsResponseDto(
    id = 0,
    title = "",
    parentId = null,
    isPublic = true,
    createdAt = "",
    updatedAt = "",
    deletedAt = "",
    mailListName = "root_tag_mail_list",
    subTags = emptyList()
)

val subscribableTagsDtoTestData = listOf(
    baseSubscribableTagsResponseDtoTestData.copy(
        id = 1,
        title = "Root Tag",
        subTags = listOf(
            baseSubscribableTagsResponseDtoTestData.copy(
                id = 2,
                parentId = 1,
                title = "Sub Tag 1",
                subTags = listOf(
                    baseSubscribableTagsResponseDtoTestData.copy(
                        id = 4,
                        parentId = 2,
                        title = "Sub Tag 3"
                    ),
                    baseSubscribableTagsResponseDtoTestData.copy(
                        id = 5,
                        parentId = 2,
                        title = "Sub Tag 4"
                    )
                )
            ),
            baseSubscribableTagsResponseDtoTestData.copy(
                id = 3,
                parentId = 1,
                title = "Sub Tag 2",
                deletedAt = "20-12-2025",
                createdAt = "20-12-2025"
            )
        )
    )
)

internal val baseSubscribedTagResponseDto = SubscribedTagResponseDto(
    id = 0,
    title = "Root tag",
    parentId = null,
    isPublic = true,
    createdAt = "25-12-2025",
    updatedAt = "25-12-2025",
    deletedAt = "25-12-2025",
    mailListName = "tag1-mail-list",
)

val subscribedTagDtoTestData = listOf(
    baseSubscribedTagResponseDto.copy(id = 1, title = "Tag 1"),
    baseSubscribedTagResponseDto.copy(id = 2, title = "Tag 2"),
    baseSubscribedTagResponseDto.copy(id = 3, title = "Tag 3"),
    baseSubscribedTagResponseDto.copy(id = 4, title = "Tag 4", updatedAt = "20-12-2025"),
    baseSubscribedTagResponseDto.copy(id = 5, title = "Tag 5", deletedAt = "20-12-2025"),
)