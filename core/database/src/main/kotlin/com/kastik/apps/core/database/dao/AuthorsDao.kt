package com.kastik.apps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.kastik.apps.core.database.entities.AuthorEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AuthorsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreAuthors(authors: List<AuthorEntity>)

    @Upsert
    suspend fun insertOrReplaceAuthors(authors: List<AuthorEntity>)

    @Query("SELECT * FROM authorentity")
    fun getAuthors(): Flow<List<AuthorEntity>>

    @Query("DELETE FROM authorentity")
    suspend fun clearAuthors()

}