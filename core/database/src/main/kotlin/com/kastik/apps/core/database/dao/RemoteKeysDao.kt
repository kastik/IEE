package com.kastik.apps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kastik.apps.core.database.entities.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM REMOTE_KEYS WHERE announcementId = :id")
    suspend fun getKeyByAnnouncementId(id: Int): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(keys: List<RemoteKeys>)

    @Query(
        """
        DELETE FROM REMOTE_KEYS
        WHERE searchQuery LIKE :query 
        AND authorIds LIKE :authorIds
        AND tagIds LIKE :tagIds
    """
    )
    suspend fun clearKeys(
        query: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    )
}