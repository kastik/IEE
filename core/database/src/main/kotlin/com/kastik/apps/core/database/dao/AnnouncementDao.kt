package com.kastik.apps.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.model.AnnouncementEntityWrapper
import com.kastik.apps.core.database.model.AnnouncementWithBody
import com.kastik.apps.core.database.model.AnnouncementWithoutBody
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Transaction
    @Query("SELECT * FROM announcemententity ORDER BY updatedAt DESC")
    fun getPagingAnnouncementPreviews(): PagingSource<Int, AnnouncementWithoutBody>

    @Transaction
    @Query("SELECT * FROM announcemententity WHERE id = :id")
    suspend fun getAnnouncementWithId(id: Int): AnnouncementWithBody


    @Query("SELECT * FROM authorentity")
    fun getAuthors(): Flow<List<AuthorEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBody(body: BodyEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAttachments(attachments: List<AttachmentEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: List<TagEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTagCrossRefs(crossRefs: List<TagsCrossRefEntity>)


    @Query("DELETE FROM announcemententity")
    suspend fun clearAllAnnouncements()

    @Query("DELETE FROM attachmententity")
    suspend fun clearAttachments()

    @Query("DELETE FROM tagentity")
    suspend fun clearTags()

    @Query("DELETE FROM authorentity")
    suspend fun clearAuthors()

    @Query("DELETE FROM tagscrossrefentity")
    suspend fun clearTagCrossRefs()


    @Transaction
    suspend fun addAnnouncement(
        announcement: AnnouncementEntityWrapper
    ) {
        insertTags(announcement.tags)
        insertAuthor(announcement.author)
        insertAnnouncement(announcement.announcement)
        insertBody(announcement.body)
        insertAttachments(announcement.attachments)
        insertTagCrossRefs(announcement.tagCrossRefs)
    }

    @Transaction
    suspend fun clearAnnouncements() {
        clearAllAnnouncements()
        clearAttachments()
        clearTagCrossRefs()
    }
}