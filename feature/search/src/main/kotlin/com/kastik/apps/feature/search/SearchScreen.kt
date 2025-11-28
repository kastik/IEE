package com.kastik.apps.feature.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kastik.apps.core.designsystem.component.FilterSheetAuthor
import com.kastik.apps.core.designsystem.component.FilterSheetTag
import com.kastik.apps.core.designsystem.component.PagingRefreshError
import com.kastik.apps.core.designsystem.component.PagingRefreshLoading
import com.kastik.apps.core.designsystem.component.PagingRefreshSuccess
import com.kastik.apps.core.designsystem.component.SearchBarExpanded


@Composable
fun SearchScreen(
    navigateBack: () -> Unit, navigateToAnnouncement: (Int) -> Unit
) {
    val viewModel: SearchScreenViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }

    SearchScreenContent(
        uiState = uiState.value,
        navigateBack = navigateBack,
        navigateToAnnouncement = navigateToAnnouncement,
        updateQuery = viewModel::updateQuery,
        toggleTagSheet = viewModel::toggleTagSheet,
        toggleAuthorSheet = viewModel::toggleAuthorSheet,
        updateSelectedTagIds = viewModel::updateSelectedTagIds,
        updateSelectedAuthorIds = viewModel::updateSelectedAuthorIdsIds,
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: UiState,
    updateQuery: (String) -> Unit,
    updateSelectedTagIds: (List<Int>) -> Unit,
    updateSelectedAuthorIds: (List<Int>) -> Unit,
    navigateBack: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
    toggleTagSheet: () -> Unit,
    toggleAuthorSheet: () -> Unit
) {
    val lazyResults = uiState.searchResults?.collectAsLazyPagingItems()
    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    val tagSheetState = rememberModalBottomSheetState()
    val authorSheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBarExpanded(
            scrollBehavior = searchScroll,
            query = uiState.query,
            updateQuery = updateQuery,
            navigateBack = navigateBack,
            toggleTagSheet = toggleTagSheet,
            toggleAuthorSheet = toggleAuthorSheet,
            selectedTagCount = uiState.selectedTagIds.size,
            selectedAuthorCount = uiState.selectedAuthorIds.size
        )
        Box {
            if (uiState.showTagSheet) {
                FilterSheetTag(
                    tags = uiState.tags ?: emptyList(),
                    updateSelectedTagsIds = updateSelectedTagIds,
                    sheetState = tagSheetState,
                    onDismiss = { toggleTagSheet() },
                    selectedIds = uiState.selectedTagIds
                )
            }
            if (uiState.showAuthorSheet) {
                FilterSheetAuthor(
                    authors = uiState.authors ?: emptyList(),
                    updateSelectedAuthorIds = updateSelectedAuthorIds,
                    sheetState = authorSheetState,
                    onDismiss = { toggleAuthorSheet() },
                    selectedIds = uiState.selectedAuthorIds
                )
            }

            if (lazyResults == null) return@Box
            val lazyListState = rememberLazyListState()

            when (lazyResults.loadState.refresh) {
                is LoadState.Loading -> {
                    PagingRefreshLoading()
                }

                is LoadState.Error -> {
                    PagingRefreshError(retry = lazyResults::retry)
                }

                is LoadState.NotLoading -> {
                    PagingRefreshSuccess(
                        navigateToAnnouncement = navigateToAnnouncement,
                        lazyAnnouncements = lazyResults,
                        lazyListState = lazyListState,
                        searchScroll = searchScroll
                    )
                }
            }

        }
    }
}
