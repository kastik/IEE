package com.kastik.apps.core.data

import com.kastik.apps.core.data.mappers.AnnouncementMappersTest
import com.kastik.apps.core.data.mediator.AnnouncementRemoteMediatorTest
import com.kastik.apps.core.data.repo.AnnouncementRepoImplTest
import com.kastik.apps.core.data.repo.AuthenticationRepositoryImplTest
import com.kastik.apps.core.data.repo.UserPreferencesRepoImplTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    AnnouncementMappersTest::class,
    AnnouncementRemoteMediatorTest::class,
    AnnouncementRepoImplTest::class,
    AuthenticationRepositoryImplTest::class,
    UserPreferencesRepoImplTest::class
)
class TestDataModule