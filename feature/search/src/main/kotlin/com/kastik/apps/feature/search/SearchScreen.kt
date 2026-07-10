package com.kastik.apps.feature.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kastik.apps.core.common.extensions.shareAnnouncement
import com.kastik.apps.core.designsystem.component.IeeLinearWavyProgressIndicator
import com.kastik.apps.core.designsystem.component.IeeStatusBanner
import com.kastik.apps.core.designsystem.extensions.LocalAnalytics
import com.kastik.apps.core.designsystem.extensions.TrackScreenViewEvent
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.search.FilterOptions
import com.kastik.apps.core.model.search.QuickResults
import com.kastik.apps.core.ui.extensions.logAnnouncementShared
import com.kastik.apps.core.ui.extensions.logFiltersApplied
import com.kastik.apps.core.ui.extensions.logItemSelection
import com.kastik.apps.core.ui.extensions.logSearch
import com.kastik.apps.core.ui.extensions.logSheetOpened
import com.kastik.apps.core.ui.paging.AnnouncementFeed
import com.kastik.apps.core.ui.sheet.AuthorSheet
import com.kastik.apps.core.ui.sheet.TagSheet
import com.kastik.apps.core.ui.topbar.ActiveFilters
import com.kastik.apps.core.ui.topbar.SearchBar
import com.kastik.apps.core.ui.topbar.SearchBarFilters
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchRoute(
    navigateBack: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchFeedAnnouncements = viewModel.searchFeedAnnouncements.collectAsLazyPagingItems()
    val textFieldState = viewModel.searchBarTextFieldState

    TrackScreenViewEvent(
        screenClass = "search_route",
        screenName = "search_screen",
    )

    SearchScreen(
        activeFilters = uiState.activeFilters,
        availableFilters = uiState.availableFilters,
        quickResults = uiState.quickResults,
        searchBarTextFieldState = textFieldState,
        searchFeedAnnouncements = searchFeedAnnouncements,
        navigateBack = navigateBack,
        navigateToAnnouncement = navigateToAnnouncement,
        onSearch = viewModel::onSearch
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SearchScreen(
    activeFilters: ActiveFilters,
    availableFilters: FilterOptions,
    quickResults: QuickResults,
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
    val showTagSheet = rememberSaveable { mutableStateOf(false) }
    val showAuthorSheet = rememberSaveable { mutableStateOf(false) }
    val searchBarState = rememberSearchBarState()
    val searchRefreshLoadState = searchFeedAnnouncements.loadState.refresh

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing, topBar = {
            SearchBar(
                scrollBehavior = searchScroll,
                quickResults = quickResults,
                searchHint = stringResource(R.string.search_bar_hint),
                searchBarState = searchBarState,
                textFieldState = searchBarTextFieldState,
                expandedSecondaryActions = {
                    SearchBarFilters(
                        tagLabel = stringResource(R.string.tag_chip_label),
                        authorLabel = stringResource(R.string.author_chip_label),
                        selectedTagsCount = activeFilters.selectedTagIds.size,
                        selectedAuthorsCount = activeFilters.selectedAuthorIds.size,
                        openTagSheet = {
                            analytics.logSheetOpened("search_tags_sheet")
                            showTagSheet.value = true
                        },
                        openAuthorSheet = {
                            analytics.logSheetOpened("search_authors_sheet")
                            showAuthorSheet.value = true
                        }
                    )
                },
                collapsedSecondaryActions = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SearchBarFilters(
                            tagLabel = stringResource(R.string.tag_chip_label),
                            authorLabel = stringResource(R.string.author_chip_label),
                            selectedTagsCount = activeFilters.selectedTagIds.size,
                            selectedAuthorsCount = activeFilters.selectedAuthorIds.size,
                            openTagSheet = {
                                analytics.logSheetOpened("search_tags_sheet")
                                showTagSheet.value = true
                            },
                            openAuthorSheet = {
                                analytics.logSheetOpened("search_authors_sheet")
                                showAuthorSheet.value = true
                            }
                        )
                        AnimatedVisibility(
                            visible = searchRefreshLoadState is LoadState.Error && searchFeedAnnouncements.itemCount > 0,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            IeeStatusBanner(
                                text = "Sync failed.",
                                icon = Icons.Default.CloudOff,
                                actionLabel = "Retry",
                                onActionClick = searchFeedAnnouncements::retry,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                },
                onSearch = { query ->
                    analytics.logSearch(
                        query,
                        activeFilters.selectedTagIds,
                        activeFilters.selectedAuthorIds,
                    )
                    onSearch(
                        query,
                        activeFilters.selectedTagIds,
                        activeFilters.selectedAuthorIds,
                    )
                },
                onAnnouncementQuickResultClick = { announcementId ->
                    analytics.logItemSelection(
                        announcementId.toString(),
                        "announcement_quick_result"
                    )
                    navigateToAnnouncement(announcementId)
                },
                onTagQuickResultClick = { tagId ->
                    analytics.logItemSelection(tagId.toString(), "tag_quick_result")
                    onSearch(
                        activeFilters.committedQuery,
                        (activeFilters.selectedTagIds + tagId).toImmutableList(),
                        activeFilters.selectedAuthorIds,
                    )
                },
                onAuthorQuickResultClick = { authorId ->
                    analytics.logItemSelection(authorId.toString(), "author_quick_result")
                    onSearch(
                        activeFilters.committedQuery,
                        activeFilters.selectedTagIds,
                        (activeFilters.selectedAuthorIds + authorId).toImmutableList(),
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

            Column(modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(refreshState is LoadState.Loading && !isEmpty) {
                    IeeLinearWavyProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                AnnouncementFeed(
                    refreshEmptyText = stringResource(R.string.feed_empty),
                    refreshLoadingText = stringResource(R.string.feed_placeholder),
                    refreshErrorText = stringResource(R.string.error_generic),
                    refreshRetryText = stringResource(R.string.action_retry),
                    appendLoadingText = stringResource(R.string.feed_footer_loading),
                    appendErrorText = stringResource(R.string.error_generic),
                    appendErrorRetryText = stringResource(R.string.action_retry),
                    endOfPaginationText = stringResource(R.string.feed_footer_finished),
                    announcements = searchFeedAnnouncements,
                    lazyListState = lazyListState,
                    scrollBehavior = searchScroll,
                    onAnnouncementClick = { announcementId ->
                        analytics.logItemSelection(announcementId.toString(), "announcement")
                        navigateToAnnouncement(announcementId)
                    },
                    onAnnouncementLongClick = { announcementId ->
                        analytics.logAnnouncementShared(announcementId)
                        context.shareAnnouncement(announcementId)
                    },
                    contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                )
            }
        }

        if (showTagSheet.value) {
            TagSheet(
                tags = availableFilters.tags,
                selectedTagIds = activeFilters.selectedTagIds,
                onApply = { newTagIds ->
                    scope.launch {
                        analytics.logFiltersApplied("tags", newTagIds)
                        analytics.logSearch(
                            activeFilters.committedQuery,
                            newTagIds,
                            activeFilters.selectedAuthorIds,
                        )
                        onSearch(
                            activeFilters.committedQuery,
                            newTagIds,
                            activeFilters.selectedAuthorIds,
                        )
                        showTagSheet.value = false
                        searchBarState.animateToCollapsed()
                    }
                },
                onDismiss = { showTagSheet.value = false },
            )
        }
        if (showAuthorSheet.value) {
            AuthorSheet(
                authors = availableFilters.authors,
                preSelectedAuthorIds = activeFilters.selectedAuthorIds,
                onApply = { newAuthorIds ->
                    scope.launch {
                        analytics.logFiltersApplied("authors", newAuthorIds)
                        analytics.logSearch(
                            activeFilters.committedQuery,
                            activeFilters.selectedTagIds,
                            newAuthorIds,
                        )
                        onSearch(
                            activeFilters.committedQuery,
                            activeFilters.selectedTagIds,
                            newAuthorIds,
                        )
                        showAuthorSheet.value = false
                        searchBarState.animateToCollapsed()
                    }
                },
                onDismiss = { showAuthorSheet.value = false },
            )
        }
    }
}