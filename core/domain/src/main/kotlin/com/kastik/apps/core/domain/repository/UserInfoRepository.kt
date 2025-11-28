package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.UserProfile
import com.kastik.apps.core.model.aboard.UserSubscribableTag
import com.kastik.apps.core.model.aboard.UserSubscribedTag

interface UserInfoRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun getUserSubscriptions(): List<UserSubscribedTag>
    suspend fun getUserSubscribableTags(): List<UserSubscribableTag>
    suspend fun subscribeToTags(tags: List<Int>)
}