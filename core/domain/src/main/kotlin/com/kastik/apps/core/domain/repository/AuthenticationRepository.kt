package com.kastik.apps.core.domain.repository


interface AuthenticationRepository {
    suspend fun exchangeCodeForAppsToken(code: String)
    suspend fun exchangeCodeForAbroadToken(code: String)
    suspend fun checkTokenValidity(): Boolean
    suspend fun getAboardToken(): String?
    suspend fun clearTokens()

}