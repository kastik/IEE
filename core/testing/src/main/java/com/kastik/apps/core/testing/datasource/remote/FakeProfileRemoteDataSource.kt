package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.ProfileRemoteDataSource
import com.kastik.apps.core.network.model.aboard.SubscribedTagDto
import com.kastik.apps.core.network.model.aboard.UserProfileDto
import com.kastik.apps.core.testing.testdata.baseSubscribedTagDto
import com.kastik.apps.core.testing.testdata.userProfileDtoTestData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeProfileRemoteDataSource : ProfileRemoteDataSource {

    private val _subscribableTags = MutableStateFlow<List<Int>>(emptyList())
    var profileToReturn: UserProfileDto = userProfileDtoTestData.first()

    override suspend fun getProfile(): UserProfileDto {
        return profileToReturn
    }

    override suspend fun getEmailSubscriptions(): List<SubscribedTagDto> {
        return listOf(baseSubscribedTagDto)
    }

    override suspend fun subscribeToEmailTags(tags: List<Int>) {
        _subscribableTags.update {
            tags
        }
    }
}