package com.kastik.apps.core.data.pagingsource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kastik.apps.core.data.mappers.toAnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class AnnouncementPagingSource(
    private val remote: AnnouncementRemoteDataSource,
    private val query: String?,
    private val tagIds: List<Int>?,
    private val authorIds: List<Int>?,
) : PagingSource<Int, AnnouncementPreview>() {


    override fun getRefreshKey(state: PagingState<Int, AnnouncementPreview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnnouncementPreview> {
        val nextPageNumber = params.key ?: 1
        try {
            val response = remote.fetchAnnouncements(
                page = nextPageNumber,
                perPage = params.loadSize,
                body = query,
                title = query,
                authorId = authorIds,
                tagId = tagIds
            )

            return LoadResult.Page(
                data = response.data.map { it.toAnnouncementPreview() },
                prevKey = null,
                nextKey = if (response.meta.currentPage >= response.meta.lastPage) null else response.meta.currentPage + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }

    }
}