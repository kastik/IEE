package com.kastik.apps.core.database.entities

import androidx.room.Entity
import com.kastik.apps.core.model.aboard.SortType

@Entity(
    tableName = "remote_keys",
    primaryKeys = [
        "titleQuery",
        "bodyQuery",
        "authorIds",
        "tagIds",
        "sortType",
        "announcementId"
    ]
)
data class RemoteKeys(
    val announcementId: Int,
    val titleQuery: String,
    val bodyQuery: String,
    val authorIds: List<Int>,
    val tagIds: List<Int>,
    val sortType: SortType,
    val prevKey: Int?,
    val nextKey: Int?
)