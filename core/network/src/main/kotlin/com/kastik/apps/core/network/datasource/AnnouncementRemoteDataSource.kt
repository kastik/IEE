package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.model.common.PagedResponseDto
import com.kastik.apps.core.network.model.common.SingleResponseDto
import com.kastik.apps.core.network.model.response.AnnouncementDto
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

interface AnnouncementRemoteDataSource {
    suspend fun fetchPagedAnnouncements(
        page: Int = 1,
        perPage: Int = 20,
        sortBy: SortType = SortType.Priority,
        title: String? = null,
        body: String? = null,
        tagIds: List<Int>? = null,
        authorIds: List<Int>? = null,
        updatedAfter: LocalDateTime? = null,
    ): PagedResponseDto<AnnouncementDto>

    suspend fun fetchAnnouncementWithId(id: Int): SingleResponseDto<AnnouncementDto>
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
        authorIds: List<Int>?,
        tagIds: List<Int>?,
        updatedAfter: LocalDateTime?
    ): PagedResponseDto<AnnouncementDto> = aboardApiClient.getAnnouncements(
        page = page,
        perPage = perPage,
        sortType = sortBy,
        title = title?.ifEmpty { null },
        body = body?.ifEmpty { null },
        tagIds = tagIds,
        authorIds = authorIds,
        updatedAfter = updatedAfter
    )

    override suspend fun fetchAnnouncementWithId(id: Int): SingleResponseDto<AnnouncementDto> =
        aboardApiClient.getAnnouncement(id)

}