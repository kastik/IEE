package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.network.model.aboard.AnnouncementMeta
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse

val pageOneAnnouncementResponses = AnnouncementPageResponse(
    data = testAnnouncementDtoList.subList(0, 9),
    meta = AnnouncementMeta(
        currentPage = 1,
        from = 1,
        lastPage = 3,
        path = "https://aboard.iee.ihu.gr/api/v2/announcements",
        perPage = 10,
        to = 30,
        total = 30
    )
)

val pageTwoAnnouncementResponses = AnnouncementPageResponse(
    data = testAnnouncementDtoList.subList(0, 9),
    meta = AnnouncementMeta(
        currentPage = 2,
        from = 1,
        lastPage = 3,
        path = "https://aboard.iee.ihu.gr/api/v2/announcements",
        perPage = 10,
        to = 30,
        total = 30
    )
)

val pageThreeAnnouncementResponses = AnnouncementPageResponse(
    data = testAnnouncementDtoList.subList(0, 9),
    meta = AnnouncementMeta(
        currentPage = 3,
        from = 1,
        lastPage = 3,
        path = "https://aboard.iee.ihu.gr/api/v2/announcements",
        perPage = 10,
        to = 30,
        total = 30
    )
)


val announcementResponses = listOf(
    pageOneAnnouncementResponses,
    pageTwoAnnouncementResponses,
    pageThreeAnnouncementResponses
)