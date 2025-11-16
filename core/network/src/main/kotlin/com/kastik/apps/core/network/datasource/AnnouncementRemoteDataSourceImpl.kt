package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.AnnouncementResponse

interface AnnouncementRemoteDataSource {
    suspend fun fetchAnnouncements(page: Int, perPage: Int): AnnouncementResponse

}

class AnnouncementRemoteDataSourceImpl(
    @param:AnnRetrofit private val api: AboardApiClient
) : AnnouncementRemoteDataSource {
    override suspend fun fetchAnnouncements(page: Int, perPage: Int): AnnouncementResponse =
        api.getAnnouncements(page = page, perPage = perPage)
}