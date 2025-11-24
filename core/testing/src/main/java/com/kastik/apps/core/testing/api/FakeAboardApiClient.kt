package com.kastik.apps.core.testing.api


import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.aboard.AnnouncementDto
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse
import com.kastik.apps.core.network.model.aboard.AuthorDto
import com.kastik.apps.core.network.model.aboard.SingleAnnouncementResponse
import com.kastik.apps.core.network.model.aboard.TagsResponse
import com.kastik.apps.core.network.model.aboard.UserProfileDto
import com.kastik.apps.core.network.model.aboard.UserSubscribedTagDto
import com.kastik.apps.core.testing.testdata.aboardAuthTokenDtoTestData
import com.kastik.apps.core.testing.testdata.announcementResponses
import com.kastik.apps.core.testing.testdata.studentUserProfileDto
import com.kastik.apps.core.testing.testdata.userSubscribedTagDtoListTestData
import okhttp3.ResponseBody


class FakeAboardApiClient : AboardApiClient {

    private var failUserInfo = false

    fun setThrowOnGetUserInfo(fail: Boolean) {
        failUserInfo = fail
    }

    override suspend fun getAnnouncements(
        sortId: Int,
        page: Int,
        perPage: Int,
        authorId: List<Int>?,
        tagsIds: List<Int>?,
        title: String?,
        body: String?
    ): AnnouncementPageResponse {
        return announcementResponses[page - 1]
    }


    override suspend fun getAnnouncement(id: Int): SingleAnnouncementResponse {
        announcementResponses.forEach {
            it.data.forEach { announcement ->
                if (announcement.id == id) {
                    return it.data
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
        } else studentUserProfileDto
    }

    override suspend fun getUserSubscriptions(): List<UserSubscribedTagDto> {
        return userSubscribedTagDtoListTestData
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

    override suspend fun getTags(): TagsResponse {
        throw Exception("Not implemented")
    }

    override suspend fun getAuthors(): List<AuthorDto> {
        throw Exception("Not implemented")
    }


}
