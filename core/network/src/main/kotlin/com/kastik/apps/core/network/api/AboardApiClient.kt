package com.kastik.apps.core.network.api


import com.kastik.apps.core.model.aboard.SortType
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
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface AboardApiClient {
    @GET("announcements")
    @Headers("Accept: application/json")
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
    @Headers("Accept: application/json")
    suspend fun getAnnouncement(
        @Path("id") id: Int
    ): AnnouncementResponseDto

    @Streaming
    @GET("announcements/{id}/attachments/{attachmentId}")
    suspend fun searchAnnouncements(
        @Path("id") id: Int,
        @Path("attachmentId") attachmentId: Int
    ): ResponseBody


    @GET("tags")
    @Headers("Accept: application/json")
    suspend fun getTags(
    ): TagsResponseDto

    @GET("authors")
    @Headers("Accept: application/json")
    suspend fun getAuthors(
    ): List<AuthorResponseDto>


    @GET("authenticate")
    @Headers("Accept: application/json")
    suspend fun exchangeCodeForAboardToken(
        @Query("code") code: String,
    ): AboardTokenResponseDto

    @POST("auth/token")
    @Headers("Accept: application/json")
    suspend fun refreshToken(
        @Body aboardTokenRefreshRequestDto: AboardTokenRefreshRequestDto
    ): AboardTokenResponseDto

    @POST("auth/refresh")
    @Headers("Accept: application/json")
    suspend fun refreshExpiredToken(
    ): AboardTokenResponseDto

    @GET("auth/whoami")
    @Headers("Accept: application/json")
    suspend fun getUserInfo(): ProfileResponseDto

    @GET("auth/subscriptions")
    @Headers("Accept: application/json")
    suspend fun getUserSubscriptions(): List<SubscribedTagResponseDto>

    @GET("subscribetags")
    @Headers("Accept: application/json")
    suspend fun getUserSubscribableTags(): List<SubscribableTagsResponseDto>

    @POST("auth/subscribe")
    suspend fun subscribeToTags(
        @Body updatedTags: SubscribeToTagsRequestDto
    )

}