package com.kastik.repository

import com.kastik.model.aboard.AboardToken
import com.kastik.model.aboard.UserProfile
import com.kastik.model.aboard.UserSubscribedTag
import com.kastik.model.apps.AppsToken


interface AuthenticationRepository {
    suspend fun exchangeCodeForAppsToken(code: String): AppsToken

    suspend fun exchangeCodeForAbroadToken(code: String): AboardToken

    suspend fun checkIfUserIsAuthenticated(): Boolean

    suspend fun getSavedToken(): String?

    suspend fun getUserProfile(): UserProfile

    suspend fun getUserSubscriptions(): List<UserSubscribedTag>
}