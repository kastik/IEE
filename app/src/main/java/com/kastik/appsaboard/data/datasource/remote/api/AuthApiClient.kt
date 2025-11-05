package com.kastik.appsaboard.data.datasource.remote.api

import com.kastik.appsaboard.data.datasource.remote.dto.AuthTokenDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiClient {

    @FormUrlEncoded
    @POST("token")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "Accept: application/json"
    )
    suspend fun exchangeCodeForToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
    ): AuthTokenDto


}