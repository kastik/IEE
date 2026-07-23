package com.kastik.apps.core.network.api

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.model.common.ListResponseDto
import com.kastik.apps.core.network.model.common.SingleResponseDto
import com.kastik.apps.core.network.model.request.SubscribeDto
import com.kastik.apps.core.network.model.response.AnnouncementDto
import com.kastik.apps.core.network.model.response.ProfileDto
import com.kastik.apps.core.network.model.response.TokenDto
import com.kastik.apps.core.network.testdata.baseAuthorDto
import com.kastik.apps.core.network.testdata.basePagedAnnouncementDto
import com.kastik.apps.core.network.testdata.baseProfileDto
import com.kastik.apps.core.network.testdata.baseSingleResponseAnnouncementDto
import com.kastik.apps.core.network.testdata.baseTagDto
import com.kastik.apps.core.network.testdata.baseTokenDto
import kotlinx.datetime.LocalDateTime

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
        authorIds: List<Int>?,
        tagIds: List<Int>?,
        title: String?,
        body: String?,
        updatedAfter: LocalDateTime?,
    ) = basePagedAnnouncementDto

    override suspend fun getAnnouncement(id: Int): SingleResponseDto<AnnouncementDto> {
        throwOnApiCall?.let { exception ->
            throw exception
        }

        return baseSingleResponseAnnouncementDto
    }

    override suspend fun exchangeAuthCode(code: String): TokenDto {
        throwOnApiCall?.let {
            throw it
        }
        return baseTokenDto
    }

    override suspend fun refreshToken(): TokenDto {
        return baseTokenDto
    }

    override suspend fun getCurrentUser(): ProfileDto {
        throwOnApiCall?.let {
            throw it
        }
        return baseProfileDto
    }

    override suspend fun getSubscribedTags() = listOf(baseTagDto)

    override suspend fun getAvailableTags() = listOf(baseTagDto)

    override suspend fun subscribeToTags(tags: SubscribeDto) {
        _subscribedIds.clear()
        _subscribedIds.addAll(tags.tags)
    }

    override suspend fun getTags() = ListResponseDto(listOf(baseTagDto))

    override suspend fun getAuthors() = listOf(baseAuthorDto)
}
