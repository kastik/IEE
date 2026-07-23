package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.AnnouncementEntity
import kotlin.time.Instant

val baseAnnouncementEntity =
    AnnouncementEntity(
        id = 1,
        title = "Test Announcement",
        preview = "Test Preview",
        createdAt = Instant.fromEpochSeconds(1782774327),
        updatedAt = Instant.fromEpochSeconds(1782774327),
        isPinned = false,
        pinnedUntil = null,
        authorId = 1,
    )
