package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.network.model.aboard.SubscribableTagsDto

fun SubscribableTagsDto.toSubscribableTag(): SubscribableTag = SubscribableTag(
    id = id,
    title = title,
    parentId = parentId,
    isPublic = isPublic,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt,
    mailListName = mailListName,
    subTags = subTags.map { it.toSubscribableTag() })


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

fun SubscribableTagsDto.toSubscribableTagProto(): SubscribableTagProto =
    SubscribableTagProto.newBuilder()
        .setId(id)
        .setTitle(title)
        .setIsPublic(isPublic)
        .setCreatedAt(createdAt)
        .setMailListName(mailListName)
        .addAllSubTags(subTags.map { it.toSubscribableTagProto() })
        .apply {
            if (parentId != null) setParentId(parentId)
            if (updatedAt != null) setUpdatedAt(updatedAt)
            if (deletedAt != null) setDeletedAt(deletedAt)
        }
        .build()