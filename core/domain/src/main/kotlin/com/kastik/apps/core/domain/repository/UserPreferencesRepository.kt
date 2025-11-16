package com.kastik.apps.core.domain.repository


interface UserPreferencesRepository {
    suspend fun getHasSkippedSignIn(): Boolean

    suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean)

}