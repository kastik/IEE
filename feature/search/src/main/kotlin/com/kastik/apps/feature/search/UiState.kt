package com.kastik.apps.feature.search

import androidx.paging.PagingData
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.flow.Flow

data class UiState(
    val searchResults: Flow<PagingData<AnnouncementPreview>>? = null,
    val query: String = "",
    val tags: List<Tag>? = null,
    val selectedTagIds: List<Int> = emptyList(),
    val authors: List<Author>? = null,
    val selectedAuthorIds: List<Int> = emptyList(),
    val showTagSheet: Boolean = false,
    val showAuthorSheet: Boolean = false,
)