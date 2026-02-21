package com.kastik.apps.core.network.interceptor

import com.kastik.apps.core.di.BaseAboardClient
import com.kastik.apps.core.network.api.AboardApiClient
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AboardAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    @BaseAboardClient private val aboardRefreshApiClient: AboardApiClient,
) : Authenticator {


    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.priorResponse?.priorResponse != null) return null

        val requestToken = response.request.header("Authorization")?.substringAfter("Bearer ")

        return synchronized(this) {
            runBlocking {
                val currentToken = tokenManager.getToken()
                if (requestToken != currentToken && currentToken != null) {
                    return@runBlocking buildRequest(response.request, currentToken)
                }


                try {
                    val newToken = aboardRefreshApiClient.refreshExpiredToken().accessToken
                    tokenManager.updateToken(newToken)
                    return@runBlocking buildRequest(response.request, newToken)
                } catch (e: Exception) {
                    return@runBlocking null
                }
            }
        }
    }

    private fun buildRequest(request: Request, token: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    }
}