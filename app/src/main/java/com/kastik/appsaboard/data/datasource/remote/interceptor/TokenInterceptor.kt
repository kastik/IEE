package com.kastik.appsaboard.data.datasource.remote.interceptor

import com.kastik.appsaboard.domain.repository.AuthenticationRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class TokenInterceptor(
    private val authRepo: AuthenticationRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { authRepo.getSavedToken() }
        val newRequest = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("x-access-token", token)
                .build()
        } else chain.request()
        return chain.proceed(newRequest)
    }
}