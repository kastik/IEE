package com.kastik.apps.core.testing.dao

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.relations.AnnouncementDetailRelation
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.testing.testdata.announcementBodyEntityTestData
import com.kastik.apps.core.testing.testdata.announcementEntityTestData
import com.kastik.apps.core.testing.testdata.announcementTagEntityTestData
import com.kastik.apps.core.testing.testdata.announcementTagsCrossRefEntityTestData
import com.kastik.apps.core.testing.testdata.attachmentEntityTestData
import com.kastik.apps.core.testing.testdata.authorEntitiesTestData
import com.kastik.apps.core.testing.testdata.tagEntitiesTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeAnnouncementDao : AnnouncementDao {
    private val _announcements: MutableStateFlow<List<AnnouncementEntity>> =
        MutableStateFlow(emptyList())
    val announcements: StateFlow<List<AnnouncementEntity>> = _announcements.asStateFlow()
    private val _bodies: MutableStateFlow<List<BodyEntity>> = MutableStateFlow(emptyList())
    val bodies: StateFlow<List<BodyEntity>> = _bodies.asStateFlow()
    private val _tagsCrossRefs: MutableStateFlow<List<TagsCrossRefEntity>> =
        MutableStateFlow(emptyList())
    val tagsCrossRefs: StateFlow<List<TagsCrossRefEntity>> = _tagsCrossRefs.asStateFlow()
    private val _attachments: MutableStateFlow<List<AttachmentEntity>> =
        MutableStateFlow(emptyList())
    val attachments: StateFlow<List<AttachmentEntity>> = _attachments.asStateFlow()

    private val pagingSources = mutableListOf<PagingSource<Int, AnnouncementPreviewRelation>>()


    // --- Reactive Queries ---

    override fun getAnnouncementWithId(id: Int): Flow<AnnouncementDetailRelation> {
        return combine(
            _announcements,
            _bodies,
            _tagsCrossRefs,
            _attachments
        ) { announcements, bodies, tagsCrossRefs, attachments ->

            val announcement = announcements.first { it.id == id }
            val body = bodies.first { it.announcementId == id }

            val author = authorEntitiesTestData.first { it.id == announcement.authorId }
            val tagIds = tagsCrossRefs.filter { it.announcementId == id }.map { it.tagId }.toSet()
            val specificTags = announcementTagEntityTestData.filter { it.id in tagIds }
            val specificAttachments = attachments.filter { it.announcementId == id }

            AnnouncementDetailRelation(
                announcement = announcement,
                body = body,
                author = author,
                tags = specificTags,
                attachments = specificAttachments
            )
        }
    }

    override fun getQuickSearchAnnouncements(
        query: String,
        sortType: SortType
    ): Flow<List<AnnouncementPreviewRelation>> {
        return _announcements.map { list ->
            list.filter { it.title.contains(query, ignoreCase = true) }
                .map { announcement ->
                    AnnouncementPreviewRelation(
                        announcement = announcement,
                        author = authorEntitiesTestData.first { it.id == announcement.authorId },
                        tags = announcementTagEntityTestData.filter { it.id == announcement.authorId }, // check if this should be announcementId
                        attachments = attachmentEntityTestData.filter { it.announcementId == announcement.id }
                    )
                }
                .let { relations ->
                    when (sortType) {
                        SortType.DESC -> relations.sortedByDescending { it.announcement.createdAt }
                        else -> relations.sortedBy { it.announcement.createdAt }
                    }
                }
        }
    }


    override fun getPagedAnnouncements(
        titleQuery: String,
        bodyQuery: String,
        tagIds: List<Int>,
        authorIds: List<Int>,
        sortType: SortType
    ): PagingSource<Int, AnnouncementPreviewRelation> {
        val rawData = _announcements.value
        val source = object : PagingSource<Int, AnnouncementPreviewRelation>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnnouncementPreviewRelation> {
                val pageIndex = params.key ?: 0
                val pageSize = params.loadSize

                // 2. Filter (mimic DB query)
                val filtered = rawData.filter { announcement ->
                    val matchesQuery = announcement.title.contains(titleQuery, ignoreCase = true)
                    val matchesAuthor =
                        if (authorIds.isNotEmpty()) announcement.authorId in authorIds else true
                    matchesQuery && matchesAuthor
                } // .sortedBy { ... apply sortType here ... }

                // 3. Calculate Indices for Pagination
                val fromIndex = pageIndex * pageSize
                if (fromIndex >= filtered.size) {
                    return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
                }

                val toIndex = minOf(fromIndex + pageSize, filtered.size)
                val pageSegment = filtered.subList(fromIndex, toIndex)

                // 4. Map only the requested page
                val mappedData = pageSegment.map { announcement ->
                    AnnouncementPreviewRelation(
                        announcement = announcement,
                        author = authorEntitiesTestData.first { it.id == announcement.authorId },
                        tags = tagEntitiesTestData,
                        attachments = attachmentEntityTestData.filter { it.announcementId == announcement.id }
                    )
                }

                return LoadResult.Page(
                    data = mappedData,
                    prevKey = if (pageIndex > 0) pageIndex - 1 else null,
                    nextKey = if (toIndex < filtered.size) pageIndex + 1 else null
                )

            }

            override fun getRefreshKey(state: PagingState<Int, AnnouncementPreviewRelation>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
        }
        pagingSources.add(source)


        source.registerInvalidatedCallback {
            pagingSources.remove(source)
        }

        return source
    }


    override suspend fun getAttachmentWithId(id: Int): String {
        return _attachments.value.first { it.id == id }.attachmentUrl
    }

    override suspend fun upsertAttachments(attachments: List<AttachmentEntity>) {
        _attachments.update { currentList ->
            val existingIds = currentList.map { it.id }.toSet()
            currentList + attachments.filter { it.id !in existingIds }
        }
    }

    override suspend fun upsertAnnouncements(announcements: List<AnnouncementEntity>) {
        if (announcements.isEmpty()) return // 1. Fast exit for empty lists

        var hasChanged = false
        _announcements.update { currentList ->
            val existingIds = currentList.map { it.id }.toSet()
            val newItems = announcements.filter { it.id !in existingIds }

            if (newItems.isNotEmpty()) {
                hasChanged = true
                currentList + newItems
            } else {
                currentList
            }
        }

        // 2. Only invalidate if we actually added something
        if (hasChanged) {
            invalidatePagingSources()
        }
    }

    override suspend fun upsertTagCrossRefs(crossRefs: List<TagsCrossRefEntity>) {
        _tagsCrossRefs.update { currentList ->
            val existingIds = currentList.map { it.announcementId to it.tagId }.toSet()
            currentList + crossRefs.filter { it.announcementId to it.tagId !in existingIds }
        }
    }

    override suspend fun upsertBodies(bodies: List<BodyEntity>) {
        _bodies.update { currentList ->
            val existingIds = currentList.map { it.announcementId }.toSet()
            currentList + bodies.filter { it.announcementId !in existingIds }
        }
    }

    // --- Updates ---

    override suspend fun upsertAnnouncements(announcement: AnnouncementEntity) {
        _announcements.update { currentList ->
            currentList.filterNot { it.id == announcement.id } + announcement
        }
    }

    override suspend fun upsertBodies(body: BodyEntity) {
        _bodies.update { currentList ->
            currentList.filterNot { it.announcementId == body.announcementId } + body
        }
    }


    // --- Clearing ---

    override suspend fun clearAllAnnouncements() {
        _announcements.update {
            emptyList()
        }
    }

    override suspend fun clearBodies() {
        _bodies.update {
            emptyList()
        }
    }

    override suspend fun clearAttachments() {
        _attachments.update {
            emptyList()
        }
    }

    override suspend fun clearTagCrossRefs() {
        _tagsCrossRefs.update {
            emptyList()
        }
    }

    private fun invalidatePagingSources() {
        pagingSources.toList().forEach { it.invalidate() }
    }

    suspend fun insertTestData() {
        upsertAnnouncements(announcementEntityTestData)
        upsertBodies(announcementBodyEntityTestData)
        upsertTagCrossRefs(announcementTagsCrossRefEntityTestData)
        upsertAttachments(attachmentEntityTestData)
    }
}