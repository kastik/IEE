package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.model.aboard.profile.ProfileResponseDto
import com.kastik.apps.core.network.testdata.userProfileDtoTestData

class FakeProfileRemoteDataSource : ProfileRemoteDataSource {
    var profileToReturn: ProfileResponseDto = userProfileDtoTestData.first()

    override suspend fun getProfile(): ProfileResponseDto {
        return profileToReturn
    }

}