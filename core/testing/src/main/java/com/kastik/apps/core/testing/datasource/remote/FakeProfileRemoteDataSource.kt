package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.ProfileRemoteDataSource
import com.kastik.apps.core.network.model.aboard.profile.ProfileResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import com.kastik.apps.core.testing.testdata.baseSubscribedTagResponseDto
import com.kastik.apps.core.testing.testdata.userProfileDtoTestData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeProfileRemoteDataSource : ProfileRemoteDataSource {

    private val _subscribableTags = MutableStateFlow<List<Int>>(emptyList())
    var profileToReturn: ProfileResponseDto = userProfileDtoTestData.first()

    override suspend fun getProfile(): ProfileResponseDto {
        return profileToReturn
    }

    override suspend fun getEmailSubscriptions(): List<SubscribedTagResponseDto> {
        return listOf(baseSubscribedTagResponseDto)
    }

    override suspend fun subscribeToEmailTags(tags: List<Int>) {
        _subscribableTags.update {
            tags
        }
    }
}