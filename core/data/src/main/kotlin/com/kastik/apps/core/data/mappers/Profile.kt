package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.network.model.aboard.UserProfileDto

fun ProfileProto.toProfile() = Profile(
    id = id,
    name = name,
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isAuthor = isAuthor,
    isAdmin = isAdmin,
    lastLoginAt = lastLoginAt,
    uid = uid,
    deletedAt = deletedAt
)

fun UserProfileDto.toProfileProto(): ProfileProto = let { dto ->
    ProfileProto.newBuilder().apply {
        setId(dto.id)
        setName(dto.name)
        setName(dto.name.ifEmpty { nameEng })
        setEmail(dto.email)
        setCreatedAt(dto.createdAt)
        setUpdatedAt(dto.updatedAt)
        setIsAuthor(dto.isAuthor)
        setIsAdmin(dto.isAdmin)
        setLastLoginAt(dto.lastLoginAt)
        setUid(dto.uid)
        setDeletedAt(dto.deletedAt ?: "")
    }.build()
}