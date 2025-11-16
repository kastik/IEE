package com.kastik.apps.core.network.api

import com.kastik.apps.core.network.model.apps.AppsAuthTokenDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AppsApiClient {
    @FormUrlEncoded
    @POST("token")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "Accept: application/json"
    )
    suspend fun exchangeCodeForAppsToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
    ): AppsAuthTokenDto

}