package com.kastik.apps.core.database

import com.kastik.apps.core.database.dao.AnnouncementDaoTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    AnnouncementDaoTest::class,
)

class TestDatabaseModule