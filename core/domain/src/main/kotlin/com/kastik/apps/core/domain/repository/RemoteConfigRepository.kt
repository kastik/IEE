package com.kastik.apps.core.domain.repository

interface RemoteConfigRepository {
    fun isFcmEnabled(): Boolean
    fun isAuthenticatorEnabled(): Boolean
    suspend fun fetchAndActivate(): Boolean
}