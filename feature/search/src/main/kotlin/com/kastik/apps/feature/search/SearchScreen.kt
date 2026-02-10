package com.kastik.apps.feature.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kastik.apps.core.common.extensions.shareAnnouncement
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.paging.AnnouncementFeed
import com.kastik.apps.core.ui.sheet.GenericFilterSheet
import com.kastik.apps.core.ui.topbar.SearchBar
import com.kastik.apps.core.ui.topbar.SearchBarFilters
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    navigateBack: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val searchFeedAnnouncements = viewModel.searchFeedAnnouncements.collectAsLazyPagingItems()
    val textFieldState = viewModel.searchBarTextFieldState

    TrackScreenViewEvent("search_screen")

    SearchScreenContent(
        uiState = uiState.value,
        searchBarTextFieldState = textFieldState,
        searchFeedAnnouncements = searchFeedAnnouncements,
        navigateBack = navigateBack,
        navigateToAnnouncement = navigateToAnnouncement,
        onSearch = viewModel::onSearch
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SearchScreenContent(
    uiState: UiState,
    searchBarTextFieldState: TextFieldState,
    searchFeedAnnouncements: LazyPagingItems<Announcement>,
    navigateBack: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
    onSearch: (query: String, tagsId: ImmutableList<Int>, authorIds: ImmutableList<Int>) -> Unit,
) {

    val context = LocalContext.current
    val analytics = LocalAnalytics.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val tagSheetState = rememberModalBottomSheetState()
    val showTagSheet = rememberSaveable { mutableStateOf(false) }
    val authorSheetState = rememberModalBottomSheetState()
    val showAuthorSheet = rememberSaveable { mutableStateOf(false) }
    val searchBarState = rememberSearchBarState()

    val secondaryActions = remember(uiState.activeFilters) {
        movableContentOf {
            SearchBarFilters(
                selectedTagsCount = uiState.activeFilters.selectedTagIds.size,
                selectedAuthorsCount = uiState.activeFilters.selectedAuthorIds.size,
                openTagSheet = { showTagSheet.value = true },
                openAuthorSheet = { showAuthorSheet.value = true })
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing, topBar = {
            SearchBar(
                scrollBehavior = searchScroll,
                quickResults = uiState.quickResults,
                searchBarState = searchBarState,
                textFieldState = searchBarTextFieldState,
                expandedSecondaryActions = secondaryActions,
                collapsedSecondaryActions = secondaryActions,
                onAnnouncementQuickResultClick = navigateToAnnouncement,
                onSearch = { query ->
                    onSearch(
                        query,
                        uiState.activeFilters.selectedTagIds,
                        uiState.activeFilters.selectedAuthorIds,
                    )
                },
                onTagQuickResultClick = { tag ->
                    onSearch(
                        uiState.activeFilters.committedQuery,
                        (uiState.activeFilters.selectedTagIds + tag).toImmutableList(),
                        uiState.activeFilters.selectedAuthorIds,
                    )
                },
                onAuthorQuickResultClick = { author ->
                    onSearch(
                        uiState.activeFilters.committedQuery,
                        uiState.activeFilters.selectedTagIds,
                        (uiState.activeFilters.selectedAuthorIds + author).toImmutableList(),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            val refreshState = searchFeedAnnouncements.loadState.refresh
            val isEmpty = searchFeedAnnouncements.itemCount == 0

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedVisibility(
                    refreshState is LoadState.Loading && !isEmpty
                ) {
                    LinearWavyProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        trackColor = Color.Transparent,
                    )
                }
                AnnouncementFeed(
                    announcements = searchFeedAnnouncements,
                    lazyListState = lazyListState,
                    scrollBehavior = searchScroll,
                    onAnnouncementClick = { announcementId ->
                        analytics.logEvent(
                            "announcement_clicked",
                            mapOf("announcement_id" to announcementId)
                        )
                        navigateToAnnouncement(announcementId)
                    },
                    onAnnouncementLongClick = { announcementId ->
                        analytics.logEvent(
                            "announcement_shared",
                            mapOf("announcement_id" to announcementId)
                        )
                        context.shareAnnouncement(announcementId)
                    },
                    contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                )
            }
        }


        if (showTagSheet.value) {
            GenericFilterSheet(
                sheetState = tagSheetState,
                items = uiState.availableFilters.tags,
                selectedIds = uiState.activeFilters.selectedTagIds,
                idProvider = { it.id },
                labelProvider = { it.title },
                titlePlaceholder = "Search Tags...",
                applyText = "Apply Tags",
                onApply = { newTagIds ->
                    scope.launch {
                        analytics.logEvent(
                            "search_tags_updated",
                            mapOf("tags" to newTagIds.toList())
                        )
                        onSearch(
                            uiState.activeFilters.committedQuery,
                            newTagIds,
                            uiState.activeFilters.selectedAuthorIds,
                        )
                        showTagSheet.value = false
                        searchBarState.animateToCollapsed()
                    }
                },
                onDismiss = {
                    showTagSheet.value = false
                },
            )
        }
        if (showAuthorSheet.value) {
            GenericFilterSheet(
                sheetState = authorSheetState,
                items = uiState.availableFilters.authors,
                selectedIds = uiState.activeFilters.selectedAuthorIds,
                idProvider = { it.id },
                labelProvider = { "${it.name} [${it.announcementCount}]" },
                groupProvider = { it.name.first().uppercaseChar() },
                titlePlaceholder = "Search Authors...",
                applyText = "Apply Authors",
                onApply = { newAuthorIds ->
                    scope.launch {
                        analytics.logEvent(
                            "search_authors_updated", mapOf("authors" to newAuthorIds.toList())
                        )
                        onSearch(
                            uiState.activeFilters.committedQuery,
                            uiState.activeFilters.selectedTagIds,
                            newAuthorIds,
                        )
                        showAuthorSheet.value = false
                        searchBarState.animateToCollapsed()
                    }
                },
                onDismiss = {
                    showAuthorSheet.value = false
                },
            )
        }
    }
}