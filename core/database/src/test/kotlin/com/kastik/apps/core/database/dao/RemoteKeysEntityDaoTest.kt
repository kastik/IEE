package com.kastik.apps.core.database.dao

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.testing.db.MemoryDatabase
import com.kastik.apps.core.testing.runner.RoboDatabaseTestRunner
import com.kastik.apps.core.testing.testdata.baseRemoteKeyEntities
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(RoboDatabaseTestRunner::class)
internal class RemoteKeysEntityDaoTest : MemoryDatabase() {

    @Test
    fun insertOrReplaceKeysInsertsNewKeys() = runTest {
        remoteKeysDao.insertOrReplaceKeys(baseRemoteKeyEntities)

        val retrievedKey =
            remoteKeysDao.getKeyByAnnouncementId(
                id = 5,
                sortType = SortType.DESC,
                titleQuery = "",
                bodyQuery = "",
                authorIds = emptyList(),
                tagIds = emptyList(),
            )

        assertThat(retrievedKey).isNotNull()
        assertThat(retrievedKey).isEqualTo(baseRemoteKeyEntities[4]) // Index 4 is id 5
    }

    @Test
    fun insertOrReplaceKeysReplacesKeysOnConflict() = runTest {
        val targetKey = baseRemoteKeyEntities.first()
        remoteKeysDao.insertOrReplaceKeys(listOf(targetKey))

        val updatedKey = targetKey.copy(nextKey = 999, prevKey = -1)

        remoteKeysDao.insertOrReplaceKeys(listOf(updatedKey))

        val retrievedKey =
            remoteKeysDao.getKeyByAnnouncementId(
                id = 1,
                sortType = SortType.DESC,
                titleQuery = "",
                bodyQuery = "",
                authorIds = emptyList(),
                tagIds = emptyList(),
            )

        assertThat(retrievedKey).isNotNull()
        assertThat(retrievedKey?.nextKey).isEqualTo(999)
        assertThat(retrievedKey?.prevKey).isEqualTo(-1)
    }

    @Test
    fun getKeyByAnnouncementIdReturnsNullWhenNoMatch() = runTest {
        remoteKeysDao.insertOrReplaceKeys(baseRemoteKeyEntities)

        val retrievedKey =
            remoteKeysDao.getKeyByAnnouncementId(
                id = 1,
                sortType = SortType.DESC,
                titleQuery = "Android", // Does not match base data ("")
                bodyQuery = "",
                authorIds = emptyList(),
                tagIds = emptyList(),
            )

        assertThat(retrievedKey).isNull()
    }

    @Test
    fun clearKeysRemovesKeysMatchingTheParameters() = runTest {
        remoteKeysDao.insertOrReplaceKeys(baseRemoteKeyEntities)

        val searchKeys = baseRemoteKeyEntities.map {
            it.copy(titleQuery = "Kotlin", sortType = SortType.ASC)
        }
        remoteKeysDao.insertOrReplaceKeys(searchKeys)

        remoteKeysDao.clearKeys(
            sortType = SortType.DESC,
            titleQuery = "",
            bodyQuery = "",
            authorIds = emptyList(),
            tagIds = emptyList(),
        )

        val missingBaseKey =
            remoteKeysDao.getKeyByAnnouncementId(
                id = 1,
                sortType = SortType.DESC,
                titleQuery = "",
                bodyQuery = "",
                authorIds = emptyList(),
                tagIds = emptyList(),
            )
        assertThat(missingBaseKey).isNull()

        val intactSearchKey =
            remoteKeysDao.getKeyByAnnouncementId(
                id = 1,
                sortType = SortType.ASC,
                titleQuery = "Kotlin",
                bodyQuery = "",
                authorIds = emptyList(),
                tagIds = emptyList(),
            )
        assertThat(intactSearchKey).isNotNull()
        assertThat(intactSearchKey?.titleQuery).isEqualTo("Kotlin")
    }
}
