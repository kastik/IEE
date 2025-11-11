package com.kastik.repository


interface UserPreferencesRepository {
    suspend fun getHasSkippedSignIn(): Boolean

    suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean)

}