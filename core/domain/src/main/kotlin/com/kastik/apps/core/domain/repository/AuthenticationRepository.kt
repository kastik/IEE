package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.UserProfile
import com.kastik.apps.core.model.aboard.UserSubscribedTag


interface AuthenticationRepository {
    suspend fun exchangeCodeForAppsToken(code: String)

    suspend fun exchangeCodeForAbroadToken(code: String)

    suspend fun checkIfUserIsAuthenticated(): Boolean

    suspend fun getSavedToken(): String?

    suspend fun getUserProfile(): UserProfile

    suspend fun getUserSubscriptions(): List<UserSubscribedTag>
}