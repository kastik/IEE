package com.kastik.apps.core.database.entities

import androidx.room.Entity

@Entity(
    tableName = "remote_keys",
    primaryKeys = [
        "searchQuery",
        "authorIds",
        "tagIds",
        "announcementId"
    ]
)
data class RemoteKeys(
    val announcementId: Int,
    val searchQuery: String,
    val authorIds: List<Int>,
    val tagIds: List<Int>,
    val prevKey: Int?,
    val nextKey: Int?
)