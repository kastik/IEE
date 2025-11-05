package com.kastik.appsaboard.domain.repository

import com.kastik.appsaboard.domain.models.AuthToken

interface AuthenticationRepository {
    suspend fun exchangeCodeForToken(code: String): AuthToken
    suspend fun getSavedToken(): String?
}