package com.kastik.appsaboard.data.datasource.remote.api

import com.kastik.appsaboard.data.datasource.remote.dto.AnnouncementDto
import retrofit2.http.GET

interface ItApiClient {
    @GET("announcements/public")
    suspend fun getPublicAnnouncements(
    ): List<AnnouncementDto>
}