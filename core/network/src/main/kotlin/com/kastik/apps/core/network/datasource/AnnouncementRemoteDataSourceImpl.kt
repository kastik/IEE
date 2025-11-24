package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse
import com.kastik.apps.core.network.model.aboard.AuthorDto
import com.kastik.apps.core.network.model.aboard.SingleAnnouncementResponse
import com.kastik.apps.core.network.model.aboard.TagsResponse

interface AnnouncementRemoteDataSource {
    suspend fun fetchAnnouncements(
        page: Int,
        perPage: Int,
        title: String? = null,
        body: String?,
        authorId: List<Int>?,
        tagId: List<Int>?
    ): AnnouncementPageResponse

    suspend fun fetchAnnouncement(id: Int): SingleAnnouncementResponse

    suspend fun fetchTags(): TagsResponse

    suspend fun fetchAuthors(): List<AuthorDto>

}

class AnnouncementRemoteDataSourceImpl(
    @param:AnnRetrofit private val api: AboardApiClient
) : AnnouncementRemoteDataSource {
    override suspend fun fetchAnnouncements(
        page: Int,
        perPage: Int,
        title: String?,
        body: String?,
        authorId: List<Int>?,
        tagId: List<Int>?
    ): AnnouncementPageResponse = api.getAnnouncements(
        page = page,
        perPage = perPage,
        title = title?.ifEmpty { null },
        body = body?.ifEmpty { null },
        authorId = authorId,
        tagsIds = tagId
    )

    override suspend fun fetchAnnouncement(id: Int): SingleAnnouncementResponse =
        api.getAnnouncement(id)

    override suspend fun fetchTags(): TagsResponse =
        api.getTags()

    override suspend fun fetchAuthors(): List<AuthorDto> =
        api.getAuthors()



}