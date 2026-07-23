package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.network.model.response.TagDto

fun TagDto.toTagEntity() =
    TagEntity(
        id = id,
        title = title,
        parentId = parentId,
        isPublic = isPublic,
        mailListName = mailListName,
    )

fun TagEntity.toTag() =
    Tag(
        id = id,
        title = title,
        isPublic = isPublic,
        parentId = parentId,
    )

fun TagDto.toSubscribedTagProto(): SubscribedTagProto =
    SubscribedTagProto.newBuilder()
        .apply {
            id = this@toSubscribedTagProto.id
            title = this@toSubscribedTagProto.title
        }
        .build()

fun SubscribedTagProto.toTag(): Tag =
    Tag(
        id = id,
        title = title,
    )

fun TagDto.toSubscribableTagProto(): SubscribableTagProto =
    SubscribableTagProto.newBuilder()
        .apply {
            id = this@toSubscribableTagProto.id
            title = this@toSubscribableTagProto.title
            isPublic = this@toSubscribableTagProto.isPublic

            this@toSubscribableTagProto.parentId?.let {
                parentId = it
            }

            this@toSubscribableTagProto.subTags?.let { tags ->
                addAllSubTags(tags.map { it.toSubscribableTagProto() })
            }
        }
        .build()

fun SubscribableTagProto.toTag(): Tag =
    Tag(
        id = id,
        title = title,
        parentId = parentId,
        isPublic = isPublic,
        subTags = subTagsList.map { it.toTag() },
    )
