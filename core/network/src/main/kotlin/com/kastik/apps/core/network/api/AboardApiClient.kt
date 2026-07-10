package com.kastik.apps.core.network.api


import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.model.common.ListResponseDto
import com.kastik.apps.core.network.model.common.PagedResponseDto
import com.kastik.apps.core.network.model.common.SingleResponseDto
import com.kastik.apps.core.network.model.request.SubscribeDto
import com.kastik.apps.core.network.model.response.AnnouncementDto
import com.kastik.apps.core.network.model.response.AuthorDto
import com.kastik.apps.core.network.model.response.ProfileDto
import com.kastik.apps.core.network.model.response.TagDto
import com.kastik.apps.core.network.model.response.TokenDto
import kotlinx.datetime.LocalDateTime
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AboardApiClient {
    @GET("v2/announcements")
    @Headers("Accept: application/json")
    suspend fun getAnnouncements(
        @Query("sortId") sortType: SortType,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("users[]") authorIds: List<Int>? = null,
        @Query("tags[]") tagIds: List<Int>? = null,
        @Query("title") title: String? = null,
        @Query("body") body: String? = null,
        @Query("updatedAfter") updatedAfter: LocalDateTime? = null,
    ): PagedResponseDto<AnnouncementDto>

    @GET("v2/announcements/{id}")
    @Headers("Accept: application/json")
    suspend fun getAnnouncement(
        @Path("id") id: Int
    ): SingleResponseDto<AnnouncementDto>


    @GET("v2/tags")
    @Headers("Accept: application/json")
    suspend fun getTags(
    ): ListResponseDto<TagDto>

    @GET("v2/authors")
    @Headers("Accept: application/json")
    suspend fun getAuthors(
    ): List<AuthorDto>


    @GET("v2/subscribetags")
    @Headers("Accept: application/json")
    suspend fun getAvailableTags(): List<TagDto>




    @GET("v2/authenticate")
    @Headers("Accept: application/json")
    suspend fun exchangeAuthCode(
        @Query("code") code: String,
    ): TokenDto

    @GET("v2/auth/whoami")
    @Headers("Accept: application/json")
    suspend fun getCurrentUser(): ProfileDto

    @POST("v2/auth/refresh")
    @Headers("Accept: application/json")
    suspend fun refreshToken(
    ): TokenDto

    @POST("v2/auth/subscribe")
    @Headers("Accept: application/json")
    suspend fun subscribeToTags(
        @Body tags: SubscribeDto
    )

    @GET("v2/auth/subscriptions")
    @Headers("Accept: application/json")
    suspend fun getSubscribedTags(): List<TagDto>

}