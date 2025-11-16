package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.model.aboard.UserProfile
import com.kastik.apps.core.model.aboard.UserSubscribedTag
import com.kastik.apps.core.network.model.aboard.UserProfileDto
import com.kastik.apps.core.network.model.aboard.UserSubscribedTagDto

fun UserSubscribedTagDto.toDomain() = UserSubscribedTag(
    id = id,
    title = title,
)

fun UserProfileDto.toDomain() = UserProfile(
    id = id,
    name = name.ifEmpty { nameEng },
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isAuthor = isAuthor,
    isAdmin = isAdmin,
    lastLoginAt = lastLoginAt,
    uid = uid,
    deletedAt = deletedAt
)