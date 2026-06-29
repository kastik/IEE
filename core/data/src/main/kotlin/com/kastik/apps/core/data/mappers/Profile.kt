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

    this@toProfileProto.createdAt?.toTimestamp()?.let { createdAt = it }
    this@toProfileProto.updatedAt?.toTimestamp()?.let { updatedAt = it }
    this@toProfileProto.deletedAt?.toTimestamp()?.let { deletedAt = it }
    this@toProfileProto.lastLoginAt?.toTimestamp()?.let { lastLoginAt = it }
}.build()

fun ProfileProto.toProfile(): Profile = Profile(
    id = id,
    uid = uid,
    name = name,
    email = email,
    isAdmin = isAdmin,
    isAuthor = isAuthor,
    createdAt = if (hasCreatedAt()) createdAt.toInstant() else null,
    updatedAt = if (hasUpdatedAt()) updatedAt.toInstant() else null,
    deletedAt = if (hasDeletedAt()) deletedAt.toInstant() else null,
    lastLoginAt = lastLoginAt.toInstant()
)