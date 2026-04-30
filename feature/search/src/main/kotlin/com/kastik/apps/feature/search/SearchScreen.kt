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
import androidx.compose.ui.res.stringResource
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
import com.kastik.apps.core.ui.sheet.AuthorSheet
import com.kastik.apps.core.ui.sheet.TagSheet
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
    val showTagSheet = rememberSaveable { mutableStateOf(false) }
    val showAuthorSheet = rememberSaveable { mutableStateOf(false) }
    val searchBarState = rememberSearchBarState()

    val secondaryActions = remember(uiState.activeFilters) {
        movableContentOf {
            SearchBarFilters(
                tagLabel = stringResource(R.string.tag_chip_label),
                authorLabel = stringResource(R.string.author_chip_label),
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
                searchHint = stringResource(R.string.search_bar_hint),
                searchBarState = searchBarState,
                textFieldState = searchBarTextFieldState,
                expandedSecondaryActions = secondaryActions,
                collapsedSecondaryActions = secondaryActions,
                onSearch = { query ->
                    analytics.logEvent(
                        "search", mapOf(
                            "search_term" to query,
                            "source" to "search_screen"
                        )
                    )
                    onSearch(
                        query,
                        uiState.activeFilters.selectedTagIds,
                        uiState.activeFilters.selectedAuthorIds,
                    )
                },
                onAnnouncementQuickResultClick = { announcementId ->
                    analytics.logEvent(
                        "quick_result_click", mapOf(
                            "result_type" to "announcement",
                            "item_id" to announcementId,
                            "source" to "search_screen"
                        )
                    )
                    navigateToAnnouncement(announcementId)
                },
                onTagQuickResultClick = { tag ->
                    analytics.logEvent(
                        "quick_result_click", mapOf(
                            "result_type" to "tag",
                            "item_id" to tag,
                            "source" to "search_screen"
                        )
                    )
                    onSearch(
                        uiState.activeFilters.committedQuery,
                        (uiState.activeFilters.selectedTagIds + tag).toImmutableList(),
                        uiState.activeFilters.selectedAuthorIds,
                    )
                },
                onAuthorQuickResultClick = { author ->
                    analytics.logEvent(
                        "quick_result_click", mapOf(
                            "result_type" to "author",
                            "item_id" to author,
                            "source" to "search_screen"
                        )
                    )
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
                    loadingPlaceHolderText = stringResource(R.string.feed_placeholder),
                    nextPagePlaceHolderText = stringResource(R.string.feed_footer_loading),
                    emptyPlaceHolderText = stringResource(R.string.feed_empty),
                    errorPlaceHolderText = stringResource(R.string.error_generic),
                    errorPlaceHolderRetryText = stringResource(R.string.action_retry),
                    errorNextPagePlaceHolderText = stringResource(R.string.feed_footer_failed),
                    endOfPaginationText = stringResource(R.string.feed_footer_finished),
                    announcements = searchFeedAnnouncements,
                    lazyListState = lazyListState,
                    scrollBehavior = searchScroll,
                    onAnnouncementClick = { announcementId ->
                        analytics.logEvent(
                            "announcement_clicked",
                            mapOf(
                                "item_id" to announcementId,
                                "source" to "search_screen"
                            )
                        )
                        navigateToAnnouncement(announcementId)
                    },
                    onAnnouncementLongClick = { announcementId ->
                        analytics.logEvent(
                            "announcement_shared",
                            mapOf(
                                "item_id" to announcementId,
                                "source" to "search_screen"
                            )
                        )
                        context.shareAnnouncement(announcementId)
                    },
                    contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                )
            }
        }


        if (showTagSheet.value) {
            TagSheet(
                tags = uiState.availableFilters.tags,
                selectedTagIds = uiState.activeFilters.selectedTagIds,
                onApply = { newTagIds ->
                    scope.launch {
                        analytics.logEvent(
                            "tags_applied",
                            mapOf(
                                "item_id" to newTagIds,
                                "source" to "search_screen"
                            )
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
            AuthorSheet(
                authors = uiState.availableFilters.authors,
                preSelectedAuthorIds = uiState.activeFilters.selectedAuthorIds,
                onApply = { newAuthorIds ->
                    scope.launch {
                        analytics.logEvent(
                            "authors_applied",
                            mapOf(
                                "item_id" to newAuthorIds,
                                "source" to "search_screen"
                            )
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