package com.kastik.apps.core.network.interceptor


interface TokenManager {
    suspend fun getToken(): String?
    suspend fun updateToken(newToken: String)
}