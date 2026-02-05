package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementResponseDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedAnnouncementsResponseDto
import javax.inject.Inject

interface AnnouncementRemoteDataSource {
    suspend fun fetchPagedAnnouncements(
        page: Int,
        perPage: Int,
        sortBy: SortType,
        title: String? = null,
        body: String? = null,
        authorId: List<Int>? = null,
        tagId: List<Int>? = null
    ): PagedAnnouncementsResponseDto

    suspend fun fetchAnnouncementWithId(id: Int): AnnouncementResponseDto
}

internal class AnnouncementRemoteDataSourceImpl @Inject constructor(
    private val aboardApiClient: AboardApiClient
) : AnnouncementRemoteDataSource {
    override suspend fun fetchPagedAnnouncements(
        page: Int,
        perPage: Int,
        sortBy: SortType,
        title: String?,
        body: String?,
        authorId: List<Int>?,
        tagId: List<Int>?
    ): PagedAnnouncementsResponseDto = aboardApiClient.getAnnouncements(
        page = page,
        perPage = perPage,
        sortType = sortBy,
        title = title?.ifEmpty { null },
        body = body?.ifEmpty { null },
        authorId = authorId,
        tagsIds = tagId
    )

    override suspend fun fetchAnnouncementWithId(id: Int): AnnouncementResponseDto =
        aboardApiClient.getAnnouncement(id)

}