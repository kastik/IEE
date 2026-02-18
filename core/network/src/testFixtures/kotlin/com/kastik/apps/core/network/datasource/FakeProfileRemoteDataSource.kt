package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.model.aboard.profile.ProfileResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import com.kastik.apps.core.network.testdata.baseSubscribedTagResponseDto
import com.kastik.apps.core.network.testdata.userProfileDtoTestData

class FakeProfileRemoteDataSource : ProfileRemoteDataSource {

    private val _subscribableTags = mutableListOf<Int>()
    var profileToReturn: ProfileResponseDto = userProfileDtoTestData.first()

    override suspend fun getProfile(): ProfileResponseDto {
        return profileToReturn
    }

    override suspend fun getEmailSubscriptions(): List<SubscribedTagResponseDto> {
        return listOf(baseSubscribedTagResponseDto)
    }

    override suspend fun subscribeToEmailTags(tags: List<Int>) {
        _subscribableTags.clear()
        _subscribableTags.addAll(tags)
    }
}