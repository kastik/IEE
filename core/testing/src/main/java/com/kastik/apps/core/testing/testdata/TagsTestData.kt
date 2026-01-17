package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.data.mappers.toSubscribableTagProto
import com.kastik.apps.core.data.mappers.toSubscribedTagProto
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.network.model.aboard.AnnouncementTagDto
import com.kastik.apps.core.network.model.aboard.PivotDto
import com.kastik.apps.core.network.model.aboard.SubscribableTagsDto
import com.kastik.apps.core.network.model.aboard.SubscribedTagDto
import com.kastik.apps.core.network.model.aboard.TagsResponseDto

val baseAnnouncementTagDtoTestData = AnnouncementTagDto(
    id = 1,
    title = "Root Tag",
    parentId = null,
    isPublic = true,
    mailListName = "root_tag_mail_list"
)

val announcementTagDtoTestData = listOf(
    baseAnnouncementTagDtoTestData.copy(id = 2, parentId = 1, title = "Sub Tag 1"),
    baseAnnouncementTagDtoTestData.copy(id = 3, parentId = 1, title = "Sub Tag 2"),
    baseAnnouncementTagDtoTestData.copy(id = 4, parentId = 2, title = "Sub Tag 3"),
    baseAnnouncementTagDtoTestData.copy(id = 5, parentId = 2, title = "Sub Tag 4"),
)

val tagsResponseDtoTestData = TagsResponseDto(
    data = announcementTagDtoTestData
)
val announcementTagEntityTestData = announcementTagDtoTestData.map { it.toTagEntity() }


private val baseSubscribableTagsDtoTestData = SubscribableTagsDto(
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
    baseSubscribableTagsDtoTestData.copy(
        id = 1,
        title = "Root Tag",
        subTags = listOf(
            baseSubscribableTagsDtoTestData.copy(
                id = 2,
                parentId = 1,
                title = "Sub Tag 1",
                subTags = listOf(
                    baseSubscribableTagsDtoTestData.copy(id = 4, parentId = 2, title = "Sub Tag 3"),
                    baseSubscribableTagsDtoTestData.copy(id = 5, parentId = 2, title = "Sub Tag 4")
                )
            ),
            baseSubscribableTagsDtoTestData.copy(
                id = 3,
                parentId = 1,
                title = "Sub Tag 2",
                deletedAt = "20-12-2025",
                createdAt = "20-12-2025"
            )
        )
    )
)

val baseSubscribedTagDto = SubscribedTagDto(
    id = 0,
    title = "Root tag",
    parentId = null,
    isPublic = true,
    createdAt = "25-12-2025",
    updatedAt = "25-12-2025",
    deletedAt = "25-12-2025",
    mailListName = "tag1-mail-list",
    pivot = PivotDto(
        userId = 0,
        tagId = 1
    )
)

val subscribedTagDtoTestData = listOf(
    baseSubscribedTagDto.copy(id = 1, title = "Tag 1"),
    baseSubscribedTagDto.copy(id = 2, title = "Tag 2"),
    baseSubscribedTagDto.copy(id = 3, title = "Tag 3"),
    baseSubscribedTagDto.copy(id = 4, title = "Tag 4", updatedAt = "20-12-2025"),
    baseSubscribedTagDto.copy(id = 5, title = "Tag 5", deletedAt = "20-12-2025"),
)

val subscribedTagProtoTestData = subscribedTagDtoTestData.map { it.toSubscribedTagProto() }


val subscribableTagProtoTestData = subscribableTagsDtoTestData.map { it.toSubscribableTagProto() }
val subscribableTagsProtoTestData =
    SubscribableTagsProto.newBuilder()
        .addAllTags(subscribableTagProtoTestData)
        .build()
