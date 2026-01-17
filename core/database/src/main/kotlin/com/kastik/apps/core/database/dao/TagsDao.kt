package com.kastik.apps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.kastik.apps.core.database.entities.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreTags(tags: List<TagEntity>)

    @Upsert
    suspend fun insertOrReplaceTags(tags: List<TagEntity>)

    @Query("SELECT * FROM tagentity")
    fun getTags(): Flow<List<TagEntity>>

    @Query("DELETE FROM tagentity")
    suspend fun clearTags()
}