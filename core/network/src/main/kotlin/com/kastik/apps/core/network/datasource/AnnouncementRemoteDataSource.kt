package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AuthenticatorAboardClient
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementResponseDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedAnnouncementsResponseDto
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

interface AnnouncementRemoteDataSource {
    suspend fun fetchPagedAnnouncements(
        page: Int,
        perPage: Int,
        sortBy: SortType,
        title: String? = null,
        body: String? = null,
        authorId: List<Int>? = null,
        tagId: List<Int>? = null,
        updatedAfter: LocalDateTime? = null,
    ): PagedAnnouncementsResponseDto

    suspend fun fetchAnnouncementWithId(id: Int): AnnouncementResponseDto
}

internal class AnnouncementRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient
) : AnnouncementRemoteDataSource {
    override suspend fun fetchPagedAnnouncements(
        page: Int,
        perPage: Int,
        sortBy: SortType,
        title: String?,
        body: String?,
        authorId: List<Int>?,
        tagId: List<Int>?,
        updatedAfter: LocalDateTime?
    ): PagedAnnouncementsResponseDto = aboardApiClient.getAnnouncements(
        page = page,
        perPage = perPage,
        sortType = sortBy,
        title = title?.ifEmpty { null },
        body = body?.ifEmpty { null },
        authorId = authorId,
        tagsIds = tagId,
        updatedAfter = updatedAfter
    )

    override suspend fun fetchAnnouncementWithId(id: Int): AnnouncementResponseDto =
        aboardApiClient.getAnnouncement(id)

}