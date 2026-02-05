package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.network.model.aboard.tags.SubscribableTagsResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagResponseDto

fun TagResponseDto.toTagEntity() = TagEntity(
    id = id,
    title = title,
    parentId = parentId,
    isPublic = isPublic,
    mailListName = mailListName
)

fun TagEntity.toTag() = Tag(
    id = id,
    title = title,
    isPublic = isPublic
)

fun SubscribedTagResponseDto.toSubscribedTagProto(): SubscribedTagProto = let { dto ->
    SubscribedTagProto.newBuilder().apply {
        setId(dto.id)
        setTitle(dto.title)
    }.build()
}

fun SubscribedTagProto.toTag() = Tag(
    id = id,
    title = title,
    isPublic = false
)

fun SubscribableTagsResponseDto.toSubscribableTagProto(): SubscribableTagProto =
    SubscribableTagProto.newBuilder()
        .setId(id)
        .setTitle(title)
        .setIsPublic(isPublic)
        .setCreatedAt(createdAt)
        .setMailListName(mailListName)
        .addAllSubTags(subTags.map { it.toSubscribableTagProto() })
        .also { builder ->
            parentId?.let { builder.setParentId(it) }
            updatedAt?.let { builder.setUpdatedAt(it) }
            deletedAt?.let { builder.setDeletedAt(it) }
        }
        .build()

fun SubscribableTagProto.toSubscribableTag(): SubscribableTag = SubscribableTag(
    id = id,
    title = title,
    parentId = if (hasParentId()) parentId else null,
    isPublic = isPublic,
    createdAt = createdAt,
    updatedAt = if (hasUpdatedAt()) updatedAt else null,
    deletedAt = if (hasDeletedAt()) deletedAt else null,
    mailListName = mailListName,
    subTags = subTagsList.map { it.toSubscribableTag() }
)

