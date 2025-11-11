package com.kastik.network.api


import com.kastik.network.model.aboard.AboardAuthTokenDto
import com.kastik.network.model.aboard.AnnouncementDto
import com.kastik.network.model.aboard.AnnouncementResponse
import com.kastik.network.model.aboard.AnnouncementTagDto
import com.kastik.network.model.aboard.UserProfileDto
import com.kastik.network.model.aboard.UserSubscribedTagDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface AboardApiClient {
    @GET("announcements")
    suspend fun getAnnouncements(
        @Query("sortId") sortId: Int = 1,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): AnnouncementResponse

    @GET("announcements/{id}")
    suspend fun getAnnouncement(
        @Path("id") id: Int
    ): AnnouncementResponse

    @Streaming
    @GET("announcements/{id}/attachments/{attachmentId}")
    suspend fun searchAnnouncements(
        @Path("id") id: Int,
        @Path("attachmentId") attachmentId: Int
    ): ResponseBody

    @POST("announcements")
    suspend fun uploadAnnouncement(
        @Body announcement: AnnouncementDto
    )

    @DELETE("announcements/{id}")
    suspend fun deleteAnnouncement(
        @Path("id") id: Int
    )

    @PUT("announcements/{id}")
    suspend fun updateAnnouncement(
        @Path("id") id: Int
    )

    @GET("tags")
    suspend fun getTags(
    ): AnnouncementTagDto


    @GET("authenticate")
    suspend fun exchangeCodeForAboardToken(
        @Query("code") code: String,
    ): AboardAuthTokenDto

    @GET("auth/whoami")
    suspend fun getUserInfo(): UserProfileDto

    @GET("auth/subscriptions")
    suspend fun getUserSubscriptions(): List<UserSubscribedTagDto>

}