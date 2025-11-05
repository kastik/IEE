package com.kastik.appsaboard.data.datasource.remote.source

import com.kastik.appsaboard.data.datasource.remote.api.ItApiClient
import com.kastik.appsaboard.data.datasource.remote.dto.AnnouncementDto

class AnnouncementRemoteDataSource(
    private val apiService: ItApiClient
) {

    suspend fun fetchAnnouncements(): List<AnnouncementDto> =
        apiService.getPublicAnnouncements()

    suspend fun fetchAllAnnouncements(): List<AnnouncementDto> =
        apiService.getAllAnnouncements()
}