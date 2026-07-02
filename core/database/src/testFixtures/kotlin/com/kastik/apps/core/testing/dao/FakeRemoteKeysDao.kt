package com.kastik.apps.core.testing.dao

import com.kastik.apps.core.database.dao.RemoteKeysDao
import com.kastik.apps.core.database.entities.RemoteKeysEntity
import com.kastik.apps.core.model.aboard.SortType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeRemoteKeysDao : RemoteKeysDao {

    var clearKeysCalled = false

    private val _remoteKeysEntity: MutableStateFlow<List<RemoteKeysEntity>> =
        MutableStateFlow(emptyList())

    override suspend fun getKeyByAnnouncementId(
        id: Int,
        sortType: SortType,
        titleQuery: String,
        bodyQuery: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    ): RemoteKeysEntity? {
        return _remoteKeysEntity.value.find {
            it.announcementId == id && it.sortType == sortType && it.titleQuery == titleQuery && it.bodyQuery == bodyQuery && it.authorIds == authorIds && it.tagIds == tagIds

        }
    }

    override suspend fun insertOrReplaceKeys(keys: List<RemoteKeysEntity>) {
        _remoteKeysEntity.update { current ->
            val filteredCurrent = current.filter { existing ->
                keys.none { new ->
                    new.tagIds == existing.tagIds &&
                            new.authorIds == existing.authorIds &&
                            new.titleQuery == existing.titleQuery &&
                            new.bodyQuery == existing.bodyQuery &&
                            new.announcementId == existing.announcementId &&
                            new.prevKey == existing.prevKey &&
                            new.nextKey == existing.nextKey
                }
            }
            filteredCurrent + keys
        }
    }

    override suspend fun clearKeys(
        sortType: SortType,
        titleQuery: String,
        bodyQuery: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    ) {
        clearKeysCalled = true
        _remoteKeysEntity.update { current ->
            current.filterNot { it.sortType == sortType && it.titleQuery == titleQuery && it.bodyQuery == bodyQuery && it.authorIds == authorIds && it.tagIds == tagIds }
        }
    }
}