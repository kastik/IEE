package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.domain.repository.SortType
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse
import com.kastik.apps.core.network.model.aboard.SingleAnnouncementResponse

interface AnnouncementRemoteDataSource {
    suspend fun fetchPagedAnnouncements(
        page: Int,
        perPage: Int,
        sortBy: SortType,
        title: String? = null,
        body: String? = null,
        authorId: List<Int>? = null,
        tagId: List<Int>? = null
    ): AnnouncementPageResponse

    suspend fun fetchAnnouncementWithId(id: Int): SingleAnnouncementResponse
}

internal class AnnouncementRemoteDataSourceImpl(
    @param:AnnRetrofit private val aboardApiClient: AboardApiClient
) : AnnouncementRemoteDataSource {
    override suspend fun fetchPagedAnnouncements(
        page: Int,
        perPage: Int,
        sortBy: SortType,
        title: String?,
        body: String?,
        authorId: List<Int>?,
        tagId: List<Int>?
    ): AnnouncementPageResponse = aboardApiClient.getAnnouncements(
        page = page,
        perPage = perPage,
        sortId = sortBy.ordinal,
        title = title?.ifEmpty { null },
        body = body?.ifEmpty { null },
        authorId = authorId,
        tagsIds = tagId
    )

    override suspend fun fetchAnnouncementWithId(id: Int): SingleAnnouncementResponse =
        aboardApiClient.getAnnouncement(id)

}