package com.kastik.apps.core.network.api


import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.aboard.AnnouncementDto
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse
import com.kastik.apps.core.network.model.aboard.AuthorDto
import com.kastik.apps.core.network.model.aboard.SingleAnnouncementResponse
import com.kastik.apps.core.network.model.aboard.SubscribableTagsDto
import com.kastik.apps.core.network.model.aboard.SubscribedTagDto
import com.kastik.apps.core.network.model.aboard.TagsResponse
import com.kastik.apps.core.network.model.aboard.UpdateUserSubscriptionsDto
import com.kastik.apps.core.network.model.aboard.UserProfileDto
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
        @Query("sortId") sortId: Int,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("users[]") authorId: List<Int>? = null,
        @Query("tags[]") tagsIds: List<Int>? = null,
        @Query("title") title: String? = null,
        @Query("body") body: String? = null,
    ): AnnouncementPageResponse

    @GET("announcements/{id}")
    suspend fun getAnnouncement(
        @Path("id") id: Int
    ): SingleAnnouncementResponse

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
    ): TagsResponse

    @GET("authors")
    suspend fun getAuthors(
    ): List<AuthorDto>


    @GET("authenticate")
    suspend fun exchangeCodeForAboardToken(
        @Query("code") code: String,
    ): AboardAuthTokenDto

    @GET("auth/whoami")
    suspend fun getUserInfo(): UserProfileDto

    @GET("auth/subscriptions")
    suspend fun getUserSubscriptions(): List<SubscribedTagDto>

    @GET("subscribetags")
    suspend fun getUserSubscribableTags(): List<SubscribableTagsDto>

    @POST("auth/subscribe")
    suspend fun subscribeToTags(
        @Body updatedTags: UpdateUserSubscriptionsDto
    )

}