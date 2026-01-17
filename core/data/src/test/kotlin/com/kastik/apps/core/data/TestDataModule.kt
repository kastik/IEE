package com.kastik.apps.core.data

import com.kastik.apps.core.data.mappers.AnnouncementMappersTest
import com.kastik.apps.core.data.mappers.AttachmentMappersTest
import com.kastik.apps.core.data.mappers.AuthorMappersTest
import com.kastik.apps.core.data.mappers.PreferencesMappersTest
import com.kastik.apps.core.data.mappers.ProfileMappersTest
import com.kastik.apps.core.data.mappers.TagMappersTest
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediatorIntegrationTest
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediatorTest
import com.kastik.apps.core.data.repository.AnnouncementRepositoryImplTest
import com.kastik.apps.core.data.repository.AuthenticationRepositoryImplTest
import com.kastik.apps.core.data.repository.AuthorRepositoryImplTest
import com.kastik.apps.core.data.repository.ProfileRepositoryImplTest
import com.kastik.apps.core.data.repository.UserPreferencesRepositoryImplTest
import com.kastik.apps.core.data.repository.TagsRepositoryImplTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    AnnouncementMappersTest::class,
    AttachmentMappersTest::class,
    AuthorMappersTest::class,
    PreferencesMappersTest::class,
    ProfileMappersTest::class,
    TagMappersTest::class,
    AnnouncementRemoteMediatorTest::class,
    AnnouncementRemoteMediatorIntegrationTest::class,
    AnnouncementRepositoryImplTest::class,
    AuthenticationRepositoryImplTest::class,
    AuthorRepositoryImplTest::class,
    ProfileRepositoryImplTest::class,
    TagsRepositoryImplTest::class,
    UserPreferencesRepositoryImplTest::class
)
class TestDataModule