package com.kastik.apps.core.data

import com.kastik.apps.core.data.mappers.AnnouncementTest
import com.kastik.apps.core.data.mappers.AttachmentTest
import com.kastik.apps.core.data.mappers.AuthorTest
import com.kastik.apps.core.data.mappers.PreferencesTest
import com.kastik.apps.core.data.mappers.ProfileTest
import com.kastik.apps.core.data.mappers.TagTest
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediatorIntegrationTest
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediatorTest
import com.kastik.apps.core.data.repository.AnnouncementRepositoryImplTest
import com.kastik.apps.core.data.repository.AuthenticationRepositoryImplTest
import com.kastik.apps.core.data.repository.AuthorRepositoryImplTest
import com.kastik.apps.core.data.repository.ProfileRepositoryImplTest
import com.kastik.apps.core.data.repository.TagsRepositoryImplTest
import com.kastik.apps.core.data.repository.UserPreferencesRepositoryImplTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AnnouncementTest::class,
    AttachmentTest::class,
    AuthorTest::class,
    PreferencesTest::class,
    ProfileTest::class,
    TagTest::class,
    AnnouncementRemoteMediatorTest::class,
    AnnouncementRemoteMediatorIntegrationTest::class,
    AnnouncementRepositoryImplTest::class,
    AuthenticationRepositoryImplTest::class,
    AuthorRepositoryImplTest::class,
    ProfileRepositoryImplTest::class,
    TagsRepositoryImplTest::class,
    UserPreferencesRepositoryImplTest::class,
)
class TestDataModule
