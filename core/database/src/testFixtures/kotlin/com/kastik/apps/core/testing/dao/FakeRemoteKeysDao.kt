package com.kastik.apps.core.testing.dao

import com.kastik.apps.core.database.dao.RemoteKeysDao
import com.kastik.apps.core.database.entities.RemoteKeys
import com.kastik.apps.core.model.aboard.SortType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeRemoteKeysDao : RemoteKeysDao {

    var clearKeysCalled = false

    private val _remoteKeys: MutableStateFlow<List<RemoteKeys>> = MutableStateFlow(emptyList())
    val remoteKeys: StateFlow<List<RemoteKeys>> = _remoteKeys.asStateFlow()

    override suspend fun getKeyByAnnouncementId(
        id: Int,
        sortType: SortType,
        titleQuery: String,
        bodyQuery: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    ): RemoteKeys? {
        return _remoteKeys.value.find {
            it.announcementId == id && it.sortType == sortType && it.titleQuery == titleQuery && it.bodyQuery == bodyQuery && it.authorIds == authorIds && it.tagIds == tagIds

        }
    }

    override suspend fun insertOrReplaceKeys(keys: List<RemoteKeys>) {
        _remoteKeys.update { current ->
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
        _remoteKeys.update { current ->
            current.filterNot { it.sortType == sortType && it.titleQuery == titleQuery && it.bodyQuery == bodyQuery && it.authorIds == authorIds && it.tagIds == tagIds }
        }
    }
}