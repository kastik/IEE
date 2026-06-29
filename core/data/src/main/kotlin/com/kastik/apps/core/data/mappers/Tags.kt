package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.datastore.proto.TagProto
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.network.model.response.TagResponseDto

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
    isPublic = isPublic,
    parentId = parentId,
)

fun TagResponseDto.toTagProto(): TagProto = TagProto.newBuilder().apply {
    id = this@toTagProto.id
    title = this@toTagProto.title
    isPublic = this@toTagProto.isPublic
    mailListName = this@toTagProto.mailListName

    this@toTagProto.subTags?.let { tags ->
        addAllSubTags(tags.map { tag -> tag.toTagProto() })
    }

    this@toTagProto.parentId?.let { parentId = it }

    this@toTagProto.createdAt?.toTimestamp()?.let { createdAt = it }
    this@toTagProto.updatedAt?.toTimestamp()?.let { updatedAt = it }
    this@toTagProto.deletedAt?.toTimestamp()?.let { deletedAt = it }
}.build()

fun TagProto.toTag(): Tag = Tag(
    id = id,
    title = title,
    parentId = if (hasParentId()) parentId else null,
    isPublic = isPublic,
    createdAt = if(hasCreatedAt()) createdAt.toInstant() else null,
    updatedAt = if (hasUpdatedAt()) updatedAt.toInstant() else null,
    deletedAt = if (hasDeletedAt()) deletedAt.toInstant() else null,
    mailListName = mailListName,
    subTags = subTagsList.map { it.toTag() } // Recursive mapping
)

