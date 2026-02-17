package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.RemoteKeys
import com.kastik.apps.core.model.aboard.SortType

val noFilteredRemoteKeys = (1..20).map { id ->
    RemoteKeys(
        announcementId = id,
        titleQuery = "",
        bodyQuery = "",
        authorIds = emptyList(),
        tagIds = emptyList(),
        sortType = SortType.DESC,
        prevKey = if (id == 1) null else (id / 10),
        nextKey = if (id == 20) null else (id / 10) + 2
    )
}