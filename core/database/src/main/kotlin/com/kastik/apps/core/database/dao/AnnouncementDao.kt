package com.kastik.apps.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.model.AnnouncementEntityWrapper
import com.kastik.apps.core.database.model.AnnouncementWithBody
import com.kastik.apps.core.database.model.AnnouncementWithoutBody
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTagCrossRefs(crossRefs: List<TagsCrossRefEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnnouncementBody(body: BodyEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnnouncementAttachments(attachments: List<AttachmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAnnouncement(announcement: AnnouncementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTagCrossRefs(crossRefs: List<TagsCrossRefEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAnnouncementBody(body: BodyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAnnouncementAttachments(attachments: List<AttachmentEntity>)

    //TODO Add support for tags, authors
    @Query(
        """
    SELECT announcemententity.* FROM announcemententity
    WHERE title LIKE '%' || :query || '%' 
    OR engTitle LIKE '%' || :query || '%'
    OR preview LIKE '%' || :query || '%'
    ORDER BY
    CASE WHEN :sortType = 'Priority' THEN 
        (CASE WHEN isPinned = 1 AND (pinnedUntil IS NULL OR pinnedUntil > strftime('%Y-%m-%d %H:%M', 'now', 'localtime')) THEN 1 ELSE 0 END)
    END DESC,
    CASE WHEN :sortType = 'Priority' THEN createdAt END DESC,
    CASE WHEN :sortType = 'ASC' THEN createdAt END ASC,
    CASE WHEN :sortType = 'DESC' THEN createdAt END DESC
    LIMIT 5
"""
    )
    fun getQuickSearchAnnouncements(
        query: String,
        sortType: String
    ): Flow<List<AnnouncementWithoutBody>>

    @Transaction
    @Query(
        """
        SELECT announcemententity.* FROM announcemententity
        INNER JOIN remote_keys ON announcemententity.id = remote_keys.announcementId
        WHERE searchQuery LIKE :query
        AND authorIds LIKE :authorIds
        AND tagIds LIKE :tagIds
        ORDER BY
            CASE WHEN :sortType = 'Priority' THEN 
            (CASE WHEN isPinned = 1 AND (pinnedUntil IS NULL OR pinnedUntil > strftime('%Y-%m-%d %H:%M', 'now', 'localtime')) THEN 1 ELSE 0 END)
            END DESC,
        CASE WHEN :sortType = 'Priority' THEN createdAt END DESC,
        CASE WHEN :sortType = 'ASC' THEN createdAt END ASC,
        CASE WHEN :sortType = 'DESC' THEN createdAt END DESC
    """
    )
    fun getPagedAnnouncements(
        query: String,
        tagIds: List<Int>,
        authorIds: List<Int>,
        sortType: String
    ): PagingSource<Int, AnnouncementWithoutBody>

    @Transaction
    @Query("SELECT * FROM announcemententity WHERE id = :id")
    fun getAnnouncementWithId(id: Int): Flow<AnnouncementWithBody?>

    @Transaction
    @Query("SELECT attachmentUrl FROM attachmententity WHERE id = :id")
    suspend fun getAttachmentWithId(id: Int): String

    @Query("DELETE FROM announcemententity")
    suspend fun clearAllAnnouncements()

    @Query("DELETE FROM attachmententity")
    suspend fun clearAttachments()

    @Query("DELETE FROM tagscrossrefentity")
    suspend fun clearTagCrossRefs()

    suspend fun addAnnouncement(
        announcement: AnnouncementEntityWrapper
    ) {
        insertAnnouncement(announcement.announcement)
        insertAnnouncementBody(announcement.body)
        insertAnnouncementAttachments(announcement.attachments)
        insertTagCrossRefs(announcement.tagCrossRefs)
    }

    suspend fun updateAnnouncement(
        announcement: AnnouncementEntityWrapper
    ) {
        updateAnnouncement(announcement.announcement)
        updateAnnouncementBody(announcement.body)
        updateAnnouncementAttachments(announcement.attachments)
        updateTagCrossRefs(announcement.tagCrossRefs)
    }

    @Transaction
    suspend fun clearAnnouncements() {
        clearAllAnnouncements()
        clearAttachments()
        clearTagCrossRefs()
    }
}