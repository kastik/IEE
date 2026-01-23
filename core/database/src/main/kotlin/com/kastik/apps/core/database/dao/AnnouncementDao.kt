package com.kastik.apps.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.relations.AnnouncementDetailRelation
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation
import com.kastik.apps.core.model.aboard.SortType
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Upsert
    suspend fun upsertAnnouncements(announcements: List<AnnouncementEntity>)

    @Upsert
    suspend fun upsertAnnouncements(announcement: AnnouncementEntity)

    @Upsert
    suspend fun upsertTagCrossRefs(crossRefs: List<TagsCrossRefEntity>)

    @Upsert
    suspend fun upsertBodies(bodies: List<BodyEntity>)

    @Upsert
    suspend fun upsertBodies(body: BodyEntity)

    @Upsert
    suspend fun upsertAttachments(attachments: List<AttachmentEntity>)

    //TODO Add support for tags, authors
    @Transaction
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
    CASE WHEN :sortType = 'Priority' THEN updatedAt END DESC,
    CASE WHEN :sortType = 'ASC' THEN updatedAt END ASC,
    CASE WHEN :sortType = 'DESC' THEN updatedAt END DESC
    LIMIT 5
"""
    )
    fun getQuickSearchAnnouncements(
        query: String,
        sortType: SortType
    ): Flow<List<AnnouncementPreviewRelation>>

    @Transaction
    @Query(
        """
        SELECT announcemententity.* FROM announcemententity
        INNER JOIN remote_keys ON announcemententity.id = remote_keys.announcementId
        WHERE remote_keys.titleQuery = :titleQuery
        AND remote_keys.bodyQuery = :bodyQuery
        AND remote_keys.authorIds = :authorIds
        AND remote_keys.tagIds = :tagIds
        AND remote_keys.sortType = :sortType
        ORDER BY
            CASE WHEN :sortType = 'Priority' THEN 
            (CASE WHEN isPinned = 1 AND (pinnedUntil IS NULL OR pinnedUntil > strftime('%Y-%m-%d %H:%M', 'now', 'localtime')) THEN 1 ELSE 0 END)
            END DESC,
        CASE WHEN :sortType = 'Priority' THEN updatedAt END DESC,
        CASE WHEN :sortType = 'ASC' THEN updatedAt END ASC,
        CASE WHEN :sortType = 'DESC' THEN updatedAt END DESC
    """
    )
    fun getPagedAnnouncements(
        titleQuery: String = "",
        bodyQuery: String = "",
        tagIds: List<Int> = emptyList(),
        authorIds: List<Int> = emptyList(),
        sortType: SortType = SortType.DESC
    ): PagingSource<Int, AnnouncementPreviewRelation>

    @Transaction
    @Query("SELECT * FROM announcemententity WHERE id = :id")
    fun getAnnouncementWithId(id: Int): Flow<AnnouncementDetailRelation?>

    @Transaction
    @Query("SELECT attachmentUrl FROM attachmententity WHERE id = :id")
    suspend fun getAttachmentWithId(id: Int): String

    @Query("DELETE FROM announcemententity")
    suspend fun clearAllAnnouncements()

    @Query("DELETE FROM bodyentity")
    suspend fun clearBodies()

    @Query("DELETE FROM attachmententity")
    suspend fun clearAttachments()

    @Query("DELETE FROM tagscrossrefentity")
    suspend fun clearTagCrossRefs()
}