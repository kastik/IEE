package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.testing.testdata.userProfilesTestData
import com.kastik.apps.core.testing.testdata.userSubscribedTagDtoListTestData
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class UserMappersTest {

    @Test
    fun userTagsDtoMapsToDomainTest() {
        userSubscribedTagDtoListTestData.forEach { subscribedTagDto ->
            val domainTags = subscribedTagDto.toTag()
            assertEquals(subscribedTagDto.id, domainTags.id)
        }
    }

    @Test
    fun userProfileDtoMapsToDomainTest() {
        userProfilesTestData.forEach { userProfileDto ->
            val domainProfile = userProfileDto.toUserTheme()
            assertEquals(userProfileDto.id, domainProfile.id)
        }
    }

    @Test
    fun userNameFallsBackToEngNameWhenEmptyTest() {
        userProfilesTestData.forEach { userProfileDto ->
            if (userProfileDto.name.isEmpty()) {
                val domainProfile = userProfileDto.toUserTheme()
                assertTrue(userProfileDto.name.isEmpty())
                assertEquals(userProfileDto.nameEng, domainProfile.name)
            }
        }
    }
}