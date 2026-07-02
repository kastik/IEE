package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.RemoteKeysEntity
import com.kastik.apps.core.model.aboard.SortType

val baseRemoteKeyEntities = (1..20).map { id ->
    RemoteKeysEntity(
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