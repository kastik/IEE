package com.kastik.apps.core.network.api


import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementDto
import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementResponseDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedAnnouncementsResponseDto
import com.kastik.apps.core.network.model.aboard.auth.AboardTokenRefreshRequestDto
import com.kastik.apps.core.network.model.aboard.auth.AboardTokenResponseDto
import com.kastik.apps.core.network.model.aboard.author.AuthorResponseDto
import com.kastik.apps.core.network.model.aboard.profile.ProfileResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribableTagsResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribeToTagsRequestDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagsResponseDto
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
        @Query("sortId") sortType: SortType,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("users[]") authorId: List<Int>? = null,
        @Query("tags[]") tagsIds: List<Int>? = null,
        @Query("title") title: String? = null,
        @Query("body") body: String? = null,
    ): PagedAnnouncementsResponseDto

    @GET("announcements/{id}")
    suspend fun getAnnouncement(
        @Path("id") id: Int
    ): AnnouncementResponseDto

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
    ): TagsResponseDto

    @GET("authors")
    suspend fun getAuthors(
    ): List<AuthorResponseDto>


    @GET("authenticate")
    suspend fun exchangeCodeForAboardToken(
        @Query("code") code: String,
    ): AboardTokenResponseDto

    @POST("auth/token")
    suspend fun refreshToken(
        @Body aboardTokenRefreshRequestDto: AboardTokenRefreshRequestDto
    ): AboardTokenResponseDto

    @POST("auth/refresh")
    suspend fun refreshExpiredToken(
    ): AboardTokenResponseDto

    @GET("auth/whoami")
    suspend fun getUserInfo(): ProfileResponseDto

    @GET("auth/subscriptions")
    suspend fun getUserSubscriptions(): List<SubscribedTagResponseDto>

    @GET("subscribetags")
    suspend fun getUserSubscribableTags(): List<SubscribableTagsResponseDto>

    @POST("auth/subscribe")
    suspend fun subscribeToTags(
        @Body updatedTags: SubscribeToTagsRequestDto
    )

}