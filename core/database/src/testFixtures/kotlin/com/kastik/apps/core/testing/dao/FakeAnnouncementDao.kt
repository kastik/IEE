package com.kastik.apps.core.testing.dao

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.RemoteKeysEntity
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.relations.AnnouncementDetailRelation
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation
import com.kastik.apps.core.model.aboard.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeAnnouncementDao : AnnouncementDao {

    private val announcementsFlow = MutableStateFlow<List<AnnouncementEntity>>(emptyList())
    private val bodiesFlow = MutableStateFlow<List<BodyEntity>>(emptyList())
    private val attachmentsFlow = MutableStateFlow<List<AttachmentEntity>>(emptyList())
    private val tagCrossRefsFlow = MutableStateFlow<List<TagsCrossRefEntity>>(emptyList())

    val fakeAuthors = mutableListOf<AuthorEntity>()
    val fakeTags = mutableListOf<TagEntity>()
    val fakeRemoteKeys = mutableListOf<RemoteKeysEntity>()

    override suspend fun upsertAnnouncements(announcements: List<AnnouncementEntity>) {
        val current = announcementsFlow.value.toMutableList()
        announcements.forEach { entity ->
            current.removeIf { it.id == entity.id }
            current.add(entity)
        }
        announcementsFlow.value = current
    }

    override suspend fun upsertAnnouncements(announcement: AnnouncementEntity) {
        upsertAnnouncements(listOf(announcement))
    }

    override suspend fun upsertTagCrossRefs(crossRefs: List<TagsCrossRefEntity>) {
        val current = tagCrossRefsFlow.value.toMutableList()
        crossRefs.forEach { entity ->
            current.removeIf {
                it.announcementId == entity.announcementId && it.tagId == entity.tagId
            }
            current.add(entity)
        }
        tagCrossRefsFlow.value = current
    }

    override suspend fun upsertBodies(bodies: List<BodyEntity>) {
        val current = bodiesFlow.value.toMutableList()
        bodies.forEach { entity ->
            current.removeIf { it.announcementId == entity.announcementId }
            current.add(entity)
        }
        bodiesFlow.value = current
    }

    override suspend fun upsertBodies(body: BodyEntity) {
        upsertBodies(listOf(body))
    }

    override suspend fun upsertAttachments(attachments: List<AttachmentEntity>) {
        val current = attachmentsFlow.value.toMutableList()
        attachments.forEach { entity ->
            current.removeIf { it.id == entity.id }
            current.add(entity)
        }
        attachmentsFlow.value = current
    }

    override fun getQuickSearchAnnouncements(
        query: String,
        sortType: SortType,
    ): Flow<List<AnnouncementPreviewRelation>> {
        return announcementsFlow.map { announcements ->
            announcements
                .filter {
                    it.title.contains(query, ignoreCase = true) ||
                        it.preview.contains(
                            query,
                            ignoreCase = true,
                        )
                }
                .let { sortAnnouncements(it, sortType) }
                .map { mapToPreviewRelation(it) }
                .take(5)
        }
    }

    override fun getPagedAnnouncements(
        titleQuery: String,
        bodyQuery: String,
        tagIds: List<Int>,
        authorIds: List<Int>,
        sortType: SortType,
    ): PagingSource<Int, AnnouncementPreviewRelation> {
        val validAnnouncementIds =
            fakeRemoteKeys
                .filter {
                    it.titleQuery == titleQuery &&
                        it.bodyQuery == bodyQuery &&
                        it.sortType == sortType &&
                        (authorIds.isEmpty() || it.authorIds == authorIds) &&
                        (tagIds.isEmpty() || it.tagIds == tagIds)
                }
                .map { it.announcementId }

        val filteredAnnouncements = announcementsFlow.value.filter { it.id in validAnnouncementIds }
        val sortedAnnouncements = sortAnnouncements(filteredAnnouncements, sortType)
        val relations = sortedAnnouncements.map { mapToPreviewRelation(it) }

        return FakePagingSource(relations)
    }

    override fun getAnnouncementWithId(id: Int): Flow<AnnouncementDetailRelation?> {
        return announcementsFlow.map { announcements ->
            val announcement = announcements.find { it.id == id } ?: return@map null
            mapToDetailRelation(announcement)
        }
    }

    override suspend fun getAttachmentWithId(id: Int): String {
        return attachmentsFlow.value.find { it.id == id }?.attachmentUrl ?: ""
    }

    override suspend fun clearAllAnnouncements() {
        announcementsFlow.value = emptyList()
        bodiesFlow.value = emptyList()
        attachmentsFlow.value = emptyList()
        tagCrossRefsFlow.value = emptyList()
    }

    override suspend fun clearBodies() {
        bodiesFlow.value = emptyList()
    }

    override suspend fun clearAttachments() {
        attachmentsFlow.value = emptyList()
    }

    override suspend fun clearTagCrossRefs() {
        tagCrossRefsFlow.value = emptyList()
    }

    private fun sortAnnouncements(
        list: List<AnnouncementEntity>,
        sortType: SortType,
    ): List<AnnouncementEntity> {
        return when (sortType) {
            SortType.Priority ->
                list.sortedWith(
                    compareByDescending<AnnouncementEntity> { it.isPinned }
                        .thenByDescending { it.updatedAt }
                )

            SortType.DESC -> list.sortedByDescending { it.updatedAt }
            SortType.ASC -> list.sortedBy { it.updatedAt }
        }
    }

    private fun mapToPreviewRelation(
        announcement: AnnouncementEntity
    ): AnnouncementPreviewRelation {
        val author =
            fakeAuthors.find { it.id == announcement.authorId }
                ?: AuthorEntity(announcement.authorId, "Unknown Fake Author")

        val tags =
            tagCrossRefsFlow.value
                .filter { it.announcementId == announcement.id }
                .mapNotNull { crossRef -> fakeTags.find { it.id == crossRef.tagId } }

        val attachments = attachmentsFlow.value.filter { it.announcementId == announcement.id }

        return AnnouncementPreviewRelation(announcement, author, tags, attachments)
    }

    private fun mapToDetailRelation(announcement: AnnouncementEntity): AnnouncementDetailRelation {
        val author =
            fakeAuthors.find { it.id == announcement.authorId }
                ?: AuthorEntity(announcement.authorId, "Unknown Fake Author")

        val body =
            bodiesFlow.value.find { it.announcementId == announcement.id }
                ?: BodyEntity(announcement.id, "")

        val tags =
            tagCrossRefsFlow.value
                .filter { it.announcementId == announcement.id }
                .mapNotNull { crossRef -> fakeTags.find { it.id == crossRef.tagId } }

        val attachments = attachmentsFlow.value.filter { it.announcementId == announcement.id }

        return AnnouncementDetailRelation(announcement, body, author, tags, attachments)
    }
}

private class FakePagingSource<T : Any>(private val data: List<T>) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = null,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
}
