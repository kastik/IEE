package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.network.model.aboard.SubscribedTagDto


fun SubscribedTagDto.toTag() = Tag(
    id = id,
    title = title,
)

fun SubscribedTagDto.toSubscribedTagProto(): SubscribedTagProto = let { dto ->
    SubscribedTagProto.newBuilder().apply {
        setId(dto.id)
        setTitle(dto.title)
    }.build()
}

fun SubscribedTagProto.toTag() = Tag(
    id = id,
    title = title,
)