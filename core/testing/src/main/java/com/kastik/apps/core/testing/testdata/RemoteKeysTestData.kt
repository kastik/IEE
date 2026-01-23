package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.RemoteKeys
import com.kastik.apps.core.model.aboard.SortType

val announcementOnePageOneNoFilter = RemoteKeys(
    announcementId = 1,
    titleQuery = "",
    bodyQuery = "",
    authorIds = emptyList(),
    tagIds = emptyList(),
    sortType = SortType.DESC,
    prevKey = null,
    nextKey = 2
)

val announcementTwoPageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 2)
val announcementThreePageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 3)
val announcementFourPageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 4)
val announcementFivePageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 5)
val announcementSixPageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 6)
val announcementSevenPageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 7)
val announcementEightPageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 8)
val announcementNinePageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 9)
val announcementTenPageOneNoFilter = announcementOnePageOneNoFilter.copy(announcementId = 10)

val noFilteredRemoteKeys = listOf(
    announcementOnePageOneNoFilter,
    announcementTwoPageOneNoFilter,
    announcementThreePageOneNoFilter,
    announcementFourPageOneNoFilter,
    announcementFivePageOneNoFilter,
    announcementSixPageOneNoFilter,
    announcementSevenPageOneNoFilter,
    announcementEightPageOneNoFilter,
    announcementNinePageOneNoFilter,
    announcementTenPageOneNoFilter
)

val announcementOnePageOneStringFilter = RemoteKeys(
    announcementId = 1,
    titleQuery = "filter",
    bodyQuery = "",
    authorIds = emptyList(),
    tagIds = emptyList(),
    sortType = SortType.DESC,
    prevKey = null,
    nextKey = 2
)

val announcementTwoPageOneStringFilter = announcementOnePageOneStringFilter.copy(announcementId = 2)
val announcementThreePageOneStringFilter =
    announcementOnePageOneStringFilter.copy(announcementId = 3)
val announcementFourPageOneStringFilter =
    announcementOnePageOneStringFilter.copy(announcementId = 4)
val announcementFivePageOneStringFilter =
    announcementOnePageOneStringFilter.copy(announcementId = 5)
val announcementSixPageOneStringFilter = announcementOnePageOneStringFilter.copy(announcementId = 6)
val announcementSevenPageOneStringFilter =
    announcementOnePageOneStringFilter.copy(announcementId = 7)
val announcementEightPageOneStringFilter =
    announcementOnePageOneStringFilter.copy(announcementId = 8)
val announcementNinePageOneStringFilter =
    announcementOnePageOneStringFilter.copy(announcementId = 9)
val announcementTenPageOneStringFilter =
    announcementOnePageOneStringFilter.copy(announcementId = 10)

val stringFilteredRemoteKeys = listOf(
    announcementOnePageOneStringFilter,
    announcementTwoPageOneStringFilter,
    announcementThreePageOneStringFilter,
    announcementFourPageOneStringFilter,
    announcementFivePageOneStringFilter,
    announcementSixPageOneStringFilter,
    announcementSevenPageOneStringFilter,
    announcementEightPageOneStringFilter,
    announcementNinePageOneStringFilter,
    announcementTenPageOneStringFilter
)


