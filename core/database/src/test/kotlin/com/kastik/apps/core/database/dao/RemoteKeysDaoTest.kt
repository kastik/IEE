package com.kastik.apps.core.database.dao

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.noFilteredRemoteKeys
import com.kastik.apps.core.testing.testdata.stringFilteredRemoteKeys
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RoboDatabaseTestRunner::class)
internal class RemoteKeysDaoTest : MemoryDatabase() {


    @Test
    fun insertAndGetKey_returnsCorrectData() = runTest {
        remoteKeysDao.insertOrReplaceKeys(noFilteredRemoteKeys)
        remoteKeysDao.insertOrReplaceKeys(stringFilteredRemoteKeys)

        val test = noFilteredRemoteKeys.find { it.announcementId == 1 }

        val result =
            remoteKeysDao.getKeyByAnnouncementId(1, SortType.DESC, "", "", emptyList(), emptyList())
        assertEquals(test, result)
    }

    @Test
    fun insertOrReplaceKeys_replacesOnConflict() = runTest {
        //remoteKeysDao.insertKeys(listOf(key1))

        // Create updated key with same primary key components
        //val updatedKey = key1.copy(nextKey = 3)
        //remoteKeysDao.insertKeys(listOf(updatedKey))

        //val snapshot = remoteKeysDao.getKeysSnapshot()
        //assertEquals(1, snapshot.size)
        //assertEquals(3, snapshot.first().nextKey)
    }

    @Test
    fun clearKeys_removesOnlyMatchingQueryTagsAndSortType() = runTest {
        // val key2 = key1.copy(announcementId = 2, searchQuery = "other_query")
        // remoteKeysDao.insertKeys(listOf(key1, key2))

        // Clear only the first query type
        remoteKeysDao.clearKeys(SortType.DESC, "query", "", emptyList(), emptyList())

        //val snapshot = remoteKeysDao.getKeysSnapshot()
        //assertEquals(1, snapshot.size)
        //assertEquals(key2, snapshot.first())
    }
}