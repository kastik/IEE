package com.kastik.apps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kastik.apps.core.database.entities.RemoteKeys
import com.kastik.apps.core.model.aboard.SortType

@Dao
interface RemoteKeysDao {
    @Query(
        """
    SELECT * FROM remote_keys 
    WHERE sortType = :sortType
    AND announcementId = :id 
    AND searchQuery = :query 
    AND authorIds = :authorIds 
    AND tagIds = :tagIds
"""
    )
    suspend fun getKeyByAnnouncementId(
        id: Int,
        sortType: SortType,
        query: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    ): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceKeys(keys: List<RemoteKeys>)

    @Query(
        """
        DELETE FROM REMOTE_KEYS
        WHERE sortType = :sortType
        AND searchQuery = :query 
        AND authorIds = :authorIds
        AND tagIds = :tagIds
    """
    )
    suspend fun clearKeys(
        sortType: SortType,
        query: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    )
}