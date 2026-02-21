package com.kastik.apps.core.network.api


import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementDto
import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementResponseDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedAnnouncementsResponseDto
import com.kastik.apps.core.network.model.aboard.auth.AboardTokenRefreshRequestDto
import com.kastik.apps.core.network.model.aboard.auth.AboardTokenResponseDto
import com.kastik.apps.core.network.model.aboard.author.AuthorResponseDto
import com.kastik.apps.core.network.model.aboard.profile.ProfileResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribableTagsResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribeToTagsRequestDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagsResponseDto
import com.kastik.apps.core.network.testdata.aboardTokenResponseDtoTestData
import com.kastik.apps.core.network.testdata.announcementPageResponseTestData
import com.kastik.apps.core.network.testdata.authorDtoTestData
import com.kastik.apps.core.network.testdata.subscribableTagsDtoTestData
import com.kastik.apps.core.network.testdata.subscribedTagDtoTestData
import com.kastik.apps.core.network.testdata.tagsResponseDtoTestData
import com.kastik.apps.core.network.testdata.userProfileDtoTestData
import okhttp3.ResponseBody


class FakeAboardApiClient : AboardApiClient {

    private var throwOnApiCall: Exception? = null
    private val _subscribedIds = mutableListOf<Int>()

    fun getSubscribedIds(): List<Int> = _subscribedIds
    fun setThrowOnGetUserInfo(exception: Exception) {
        throwOnApiCall = exception
    }

    override suspend fun getAnnouncements(
        sortType: SortType,
        page: Int,
        perPage: Int,
        authorId: List<Int>?,
        tagsIds: List<Int>?,
        title: String?,
        body: String?
    ): PagedAnnouncementsResponseDto {
        return announcementPageResponseTestData[page - 1]
    }


    override suspend fun getAnnouncement(id: Int): AnnouncementResponseDto {
        announcementPageResponseTestData.forEach { page ->
            page.data.forEach { announcement ->
                if (announcement.id == id) {
                    return AnnouncementResponseDto(data = announcement)
                }
            }
        }
        throw Exception("Announcement not found")
    }

    override suspend fun exchangeCodeForAboardToken(code: String): AboardTokenResponseDto {
        return aboardTokenResponseDtoTestData
    }

    override suspend fun refreshToken(aboardTokenRefreshRequestDto: AboardTokenRefreshRequestDto): AboardTokenResponseDto {
        return aboardTokenResponseDtoTestData
    }

    override suspend fun refreshExpiredToken(): AboardTokenResponseDto {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfo(): ProfileResponseDto {
        throwOnApiCall?.let {
            throw it
        }
        return userProfileDtoTestData.first()
    }

    override suspend fun getUserSubscriptions(): List<SubscribedTagResponseDto> {
        return subscribedTagDtoTestData
    }

    override suspend fun getUserSubscribableTags(): List<SubscribableTagsResponseDto> {
        return subscribableTagsDtoTestData
    }

    override suspend fun subscribeToTags(updatedTags: SubscribeToTagsRequestDto) {
        _subscribedIds.clear()
        _subscribedIds.addAll(updatedTags.tags)
    }

    override suspend fun searchAnnouncements(id: Int, attachmentId: Int): ResponseBody {
        throw Exception("Not implemented")
    }

    override suspend fun uploadAnnouncement(announcement: AnnouncementDto) {
        throw Exception("Not implemented")
    }

    override suspend fun deleteAnnouncement(id: Int) {
        throw Exception("Not implemented")
    }

    override suspend fun updateAnnouncement(id: Int) {
        throw Exception("Not implemented")
    }

    override suspend fun getTags(): TagsResponseDto {
        return tagsResponseDtoTestData
    }

    override suspend fun getAuthors(): List<AuthorResponseDto> {
        return authorDtoTestData
    }

}
