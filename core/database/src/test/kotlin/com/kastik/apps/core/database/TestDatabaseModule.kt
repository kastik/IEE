package com.kastik.apps.core.database

import com.kastik.apps.core.database.converters.ListIntConverterTest
import com.kastik.apps.core.database.converters.SortTypeConverterTest
import com.kastik.apps.core.database.dao.AnnouncementDaoTest
import com.kastik.apps.core.database.dao.AuthorsDaoTest
import com.kastik.apps.core.database.dao.RemoteKeysDaoTest
import com.kastik.apps.core.database.dao.TagsDaoTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    ListIntConverterTest::class,
    SortTypeConverterTest::class,
    AnnouncementDaoTest::class,
    AuthorsDaoTest::class,
    TagsDaoTest::class,
    RemoteKeysDaoTest::class
)

class TestDatabaseModule