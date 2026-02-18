package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementResponseDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedAnnouncementsResponseDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedMetaResponseDto
import com.kastik.apps.core.network.testdata.announcementPageResponseTestData

class FakeAnnouncementRemoteDataSource(
    val fakeAnnouncementResponses: List<PagedAnnouncementsResponseDto> = announcementPageResponseTestData
) : AnnouncementRemoteDataSource {

    var throwOnApiRequest: Throwable? = null
    var pagedAnnouncementResponseOverride: PagedAnnouncementsResponseDto? = null

    //TODO IMPLEMENT FILTERING
    override suspend fun fetchPagedAnnouncements(
        page: Int,
        perPage: Int,
        sortBy: SortType,
        title: String?,
        body: String?,
        authorId: List<Int>?,
        tagId: List<Int>?
    ): PagedAnnouncementsResponseDto {
        throwOnApiRequest?.let { throw it }
        pagedAnnouncementResponseOverride?.let { return it }

        // 1. Combine all test pages into a single "Database" list
        val allAnnouncements = announcementPageResponseTestData.flatMap { it.data }

        // 2. Apply Filtering
        val filteredData = allAnnouncements.filter { announcement ->
            val matchTitle = title.isNullOrBlank() || announcement.title.contains(
                title,
                true
            ) || announcement.engTitle?.contains(title, true) == true

            val matchBody = body.isNullOrBlank() || announcement.body.contains(
                body,
                true
            ) || announcement.engBody?.contains(body, true) == true

            val matchAuthor = authorId.isNullOrEmpty() ||
                    (announcement.author.id in authorId)

            val matchTags = tagId.isNullOrEmpty() ||
                    announcement.tags.any { it.id in tagId }

            matchTitle && matchBody && matchAuthor && matchTags
        }

        // 3. Apply Sorting
        val sortedData = when (sortBy) {
            SortType.ASC -> filteredData.sortedBy { it.createdAt }
            SortType.DESC -> filteredData.sortedByDescending { it.createdAt }
            else -> {
                TODO("Not implemented")
            }
        }

        // 4. Calculate Pagination
        val total = sortedData.size
        val fromIndex = (page - 1) * perPage
        val toIndex = (fromIndex + perPage).coerceAtMost(total)

        val lastPage = if (total > 0) (total + perPage - 1) / perPage else 1

        val pagedData = if (fromIndex < total) {
            sortedData.subList(fromIndex, toIndex)
        } else {
            emptyList()
        }

        // 5. Return Dynamic Response
        return PagedAnnouncementsResponseDto(
            data = pagedData,
            meta = PagedMetaResponseDto(
                currentPage = page,
                from = if (pagedData.isNotEmpty()) fromIndex + 1 else 0,
                to = toIndex,
                lastPage = lastPage,
                path = "https://aboard.iee.ihu.gr/api/v2/announcements",
                perPage = perPage,
                total = total
            )
        )
    }

    override suspend fun fetchAnnouncementWithId(id: Int): AnnouncementResponseDto {
        throwOnApiRequest?.let { throw it }

        val found = fakeAnnouncementResponses.flatMap { it.data }.firstOrNull { it.id == id }

        return found?.let { AnnouncementResponseDto(it) }
            ?: throw NoSuchElementException("Announcement $id not found in Fake")
    }
}



