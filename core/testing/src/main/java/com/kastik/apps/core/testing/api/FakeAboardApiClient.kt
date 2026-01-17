package com.kastik.apps.core.testing.api


import com.kastik.apps.core.model.user.SortType
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.aboard.AnnouncementDto
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse
import com.kastik.apps.core.network.model.aboard.AuthorDto
import com.kastik.apps.core.network.model.aboard.SingleAnnouncementResponse
import com.kastik.apps.core.network.model.aboard.SubscribableTagsDto
import com.kastik.apps.core.network.model.aboard.SubscribedTagDto
import com.kastik.apps.core.network.model.aboard.TagsResponseDto
import com.kastik.apps.core.network.model.aboard.UpdateUserSubscriptionsDto
import com.kastik.apps.core.network.model.aboard.UserProfileDto
import com.kastik.apps.core.testing.testdata.aboardAuthTokenDtoTestData
import com.kastik.apps.core.testing.testdata.announcementPageResponseTestData
import com.kastik.apps.core.testing.testdata.authorDtoTestData
import com.kastik.apps.core.testing.testdata.subscribableTagsDtoTestData
import com.kastik.apps.core.testing.testdata.subscribedTagDtoTestData
import com.kastik.apps.core.testing.testdata.tagsResponseDtoTestData
import com.kastik.apps.core.testing.testdata.userProfileDtoTestData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.ResponseBody


class FakeAboardApiClient : AboardApiClient {

    private var failUserInfo = false
    private val subscribedIds = MutableStateFlow<List<Int>>(emptyList())

    fun getSubscribedIds(): MutableStateFlow<List<Int>> = subscribedIds
    fun setThrowOnGetUserInfo(fail: Boolean) {
        failUserInfo = fail
    }

    override suspend fun getAnnouncements(
        sortType: SortType,
        page: Int,
        perPage: Int,
        authorId: List<Int>?,
        tagsIds: List<Int>?,
        title: String?,
        body: String?
    ): AnnouncementPageResponse {
        return announcementPageResponseTestData[page - 1]
    }


    override suspend fun getAnnouncement(id: Int): SingleAnnouncementResponse {
        announcementPageResponseTestData.forEach { page -> // Rename 'it' to 'page' for clarity
            page.data.forEach { announcement ->
                if (announcement.id == id) {
                    return SingleAnnouncementResponse(data = announcement)
                }
            }
        }
        throw Exception("Announcement not found")
    }

    override suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto {
        return aboardAuthTokenDtoTestData
    }

    override suspend fun getUserInfo(): UserProfileDto {
        return if (failUserInfo) {
            throw Exception("User info failed")
        } else userProfileDtoTestData.first()
    }

    override suspend fun getUserSubscriptions(): List<SubscribedTagDto> {
        return subscribedTagDtoTestData
    }

    override suspend fun getUserSubscribableTags(): List<SubscribableTagsDto> {
        return subscribableTagsDtoTestData
    }

    override suspend fun subscribeToTags(updatedTags: UpdateUserSubscriptionsDto) {
        subscribedIds.update { updatedTags.tags }
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

    override suspend fun getAuthors(): List<AuthorDto> {
        return authorDtoTestData
    }

}
