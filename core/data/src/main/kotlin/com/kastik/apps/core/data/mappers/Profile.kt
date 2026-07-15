package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.network.model.response.ProfileDto

fun ProfileDto.toProfileProto(): ProfileProto = ProfileProto.newBuilder().apply {
    id = this@toProfileProto.id
    uid = this@toProfileProto.uid
    name = this@toProfileProto.name
    email = this@toProfileProto.email
    isAdmin = this@toProfileProto.isAdmin
    isAuthor = this@toProfileProto.isAuthor

    this@toProfileProto.createdAt?.toTimestamp()?.let { createdAtTimestamp = it }
    this@toProfileProto.lastLoginAt?.toTimestamp()?.let { lastLoginAtTimestamp = it }
}.build()

fun ProfileProto.toProfile(): Profile = Profile(
    id = id,
    uid = uid,
    name = name,
    email = email,
    isAdmin = isAdmin,
    isAuthor = isAuthor,
    createdAt = createdAtTimestamp.toInstant(),
    lastLoginAt = lastLoginAtTimestamp.toInstant()
)