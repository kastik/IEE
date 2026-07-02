package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.api.FakeAboardApiClient
import com.kastik.apps.core.network.model.common.PagedResponseDto
import com.kastik.apps.core.network.model.common.SingleResponseDto
import com.kastik.apps.core.network.model.response.AnnouncementDto
import kotlinx.datetime.LocalDateTime

class FakeAnnouncementRemoteDataSource(

) : AnnouncementRemoteDataSource {

    var throwOnApiRequest: Throwable? = null
    val fakeAboardApiClient = FakeAboardApiClient()

    override suspend fun fetchPagedAnnouncements(
        page: Int,
        perPage: Int,
        sortBy: SortType,
        title: String?,
        body: String?,
        tagIds: List<Int>?,
        authorIds: List<Int>?,
        updatedAfter: LocalDateTime?
    ): PagedResponseDto<AnnouncementDto> {

        throwOnApiRequest?.let { throw it }

        return fakeAboardApiClient.getAnnouncements(
            sortType = sortBy,
            page = page,
            perPage = perPage,
            authorIds = authorIds,
            tagIds = tagIds,
            title = title,
            body = body,
            updatedAfter = updatedAfter
        )

    }

    override suspend fun fetchAnnouncementWithId(id: Int): SingleResponseDto<AnnouncementDto> {
        throwOnApiRequest?.let { throw it }

        return fakeAboardApiClient.getAnnouncement(id)
    }
}



