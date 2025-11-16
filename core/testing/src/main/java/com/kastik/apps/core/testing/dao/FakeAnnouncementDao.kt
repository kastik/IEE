package com.kastik.apps.core.testing.dao

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.model.AnnouncementWithBody
import com.kastik.apps.core.database.model.AnnouncementWithoutBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class FakeAnnouncementDao : AnnouncementDao {

    var clearAllCalled = false

    val announcements = mutableListOf<AnnouncementEntity>()

    val bodies = mutableListOf<BodyEntity>()
    val authors = mutableListOf<AuthorEntity>()
    val tags = mutableListOf<TagEntity>()
    val attachments = mutableListOf<AttachmentEntity>()
    val tagsCrossRefs = mutableListOf<TagsCrossRefEntity>()

    private val authorsFlow = MutableStateFlow<List<AuthorEntity>>(emptyList())

    override fun getPagingAnnouncementPreviews(): PagingSource<Int, AnnouncementWithoutBody> {
        return object : PagingSource<Int, AnnouncementWithoutBody>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnnouncementWithoutBody> {
                //TODO Use a plethora of data here
                val data = announcements.map {
                    AnnouncementWithoutBody(
                        announcement = it,
                        author = authors.first(),
                        tags = tags,
                        attachments = attachments
                    )
                }
                return LoadResult.Page(data, prevKey = null, nextKey = null)
            }

            override fun getRefreshKey(state: PagingState<Int, AnnouncementWithoutBody>): Int? =
                null
        }
    }

    override suspend fun getAnnouncementWithId(id: Int): AnnouncementWithBody {
        val announcement = announcements.first { it.id == id }
        val body = bodies.first { it.announcementId == id }
        val author = authors.first { it.id == announcement.authorId }
        return AnnouncementWithBody(
            announcement = announcement,
            body = body,
            author = author,
            tags = tags,
            attachments = attachments
        )
    }

    override fun getAuthors(): Flow<List<AuthorEntity>> = authorsFlow.asStateFlow()

    override suspend fun insertAnnouncement(announcement: AnnouncementEntity) {
        announcements.add(announcement)
    }

    override suspend fun insertBody(body: BodyEntity) {
        bodies.add(body)
    }


    override suspend fun insertAuthor(author: AuthorEntity) {
        authors.add(author)
        authorsFlow.value = authors.toList()
    }

    override suspend fun insertTags(tags: List<TagEntity>) {
        this.tags.addAll(tags)
    }

    override suspend fun insertAttachments(attachments: List<AttachmentEntity>) {
        this.attachments.addAll(attachments)
    }

    override suspend fun insertTagCrossRefs(crossRefs: List<TagsCrossRefEntity>) {
        tagsCrossRefs.addAll(crossRefs)
    }

    override suspend fun clearAllAnnouncements() {
        announcements.clear()
    }

    override suspend fun clearAttachments() {
        announcements.clear()
    }

    override suspend fun clearTags() {
        tags.clear()
    }

    override suspend fun clearAuthors() {
        authors.clear()
        authorsFlow.value = emptyList()
    }

    override suspend fun clearTagCrossRefs() {
        tagsCrossRefs.clear()
    }

    override suspend fun clearAnnouncements() {
        clearAllCalled = true
        clearAllAnnouncements()
        clearAttachments()
        clearTags()
        clearAuthors()
        clearTagCrossRefs()
    }
}