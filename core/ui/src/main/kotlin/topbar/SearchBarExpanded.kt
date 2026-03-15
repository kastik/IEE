package com.kastik.apps.core.ui.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.model.search.QuickResults
import com.kastik.apps.core.ui.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarExpanded(
    quickResults: QuickResults,
    searchBarState: SearchBarState,
    inputField: @Composable () -> Unit,
    expandedSecondaryActions: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onTagQuickResultClick: (tagId: Int) -> Unit = {},
    onAuthorQuickResultClick: (authorId: Int) -> Unit = {},
    onAnnouncementQuickResultClick: (announcementId: Int) -> Unit = {},
) {
    val quickResultLazyListState = rememberLazyListState()

    LaunchedEffect(quickResults) {
        quickResultLazyListState.animateScrollToItem(0)
    }

    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                expandedSecondaryActions()
                LazyColumn(
                    state = quickResultLazyListState,
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    searchBarQuickResults(
                        titleKey = "tags",
                        title = { stringResource(R.string.quick_result_title_tag) },
                        items = quickResults.tags,
                        icon = Icons.Default.Tag,
                        onItemClick = { tag -> onTagQuickResultClick(tag.id) },
                        labelProvider = { tag -> tag.title },
                        keyProvider = { tag -> "tagId:${tag.id}" },
                    )

                    searchBarQuickResults(
                        titleKey = "authors",
                        title = { stringResource(R.string.quick_result_title_author) },
                        items = quickResults.authors,
                        icon = Icons.Default.Person,
                        onItemClick = { author ->
                            onAuthorQuickResultClick(author.id)
                        },
                        labelProvider = { it.name },
                        keyProvider = { "authorId:${it.id}" },
                    )



                    searchBarQuickResults(
                        titleKey = "announcements",
                        title = { stringResource(R.string.quick_result_title_announcements) },
                        items = quickResults.announcements,
                        onItemClick = { announcement ->
                            onAnnouncementQuickResultClick(announcement.id)
                        },
                        keyProvider = { "announcementId:${it.id}" },
                    )

                }
            }
        }
    }
}
