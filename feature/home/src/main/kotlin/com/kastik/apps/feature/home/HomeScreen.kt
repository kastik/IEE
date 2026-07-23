package com.kastik.apps.feature.home

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.kastik.apps.core.common.extensions.launchSignIn
import com.kastik.apps.core.common.extensions.shareAnnouncement
import com.kastik.apps.core.designsystem.component.IeeDialog
import com.kastik.apps.core.designsystem.component.IeeFloatingToolBar
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.component.IeeStatusBanner
import com.kastik.apps.core.designsystem.extensions.LocalAnalytics
import com.kastik.apps.core.designsystem.extensions.TrackScreenViewEvent
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.ui.extensions.isScrollingUp
import com.kastik.apps.core.ui.extensions.logAnnouncementShared
import com.kastik.apps.core.ui.extensions.logButtonClick
import com.kastik.apps.core.ui.extensions.logFiltersApplied
import com.kastik.apps.core.ui.extensions.logItemSelection
import com.kastik.apps.core.ui.extensions.logNavigationAction
import com.kastik.apps.core.ui.extensions.logSearch
import com.kastik.apps.core.ui.extensions.logSheetOpened
import com.kastik.apps.core.ui.paging.AnnouncementFeed
import com.kastik.apps.core.ui.sheet.AuthorSheet
import com.kastik.apps.core.ui.sheet.TagSheet
import com.kastik.apps.core.ui.topbar.SearchBar
import com.kastik.apps.core.ui.topbar.SearchBarFilters
import com.kastik.apps.feature.home.navigation.HomeTab
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
internal fun HomeScreenRoute(
    initialTab: HomeTab,
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch:
        (query: String, tagsId: ImmutableList<Int>, authorIds: ImmutableList<Int>) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    TrackScreenViewEvent(
        screenClass = "home_route",
        screenName = "home_screen",
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val homeFeedAnnouncements = viewModel.homeFeedAnnouncements.collectAsLazyPagingItems()
    val forYouAnnouncements = viewModel.forYouFeedAnnouncements.collectAsLazyPagingItems()
    val textFieldState = viewModel.searchBarTextFieldState

    HomeScreen(
        uiState = uiState,
        initialTab = initialTab,
        searchBarTextFieldState = textFieldState,
        homeFeedAnnouncements = homeFeedAnnouncements,
        forYouAnnouncements = forYouAnnouncements,
        navigateToSearch = navigateToSearch,
        navigateToProfile = navigateToProfile,
        navigateToSettings = navigateToSettings,
        navigateToAnnouncement = navigateToAnnouncement,
        onSignInNoticeDismissed = viewModel::onSignInNoticeDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState = HomeUiState(),
    initialTab: HomeTab = HomeTab.HOME,
    searchBarTextFieldState: TextFieldState = rememberTextFieldState(),
    homeFeedAnnouncements: LazyPagingItems<Announcement>,
    forYouAnnouncements: LazyPagingItems<Announcement>,
    navigateToAnnouncement: (Int) -> Unit = {},
    navigateToSearch:
        (query: String, tagIds: ImmutableList<Int>, authorIds: ImmutableList<Int>) -> Unit =
        { _, _, _ ->
        },
    navigateToProfile: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    onSignInNoticeDismissed: () -> Unit = {},
) {
    val context = LocalContext.current
    val vibrator = LocalHapticFeedback.current
    val analytics = LocalAnalytics.current
    val scope = rememberCoroutineScope()
    val homeFeedLazyListState = rememberLazyListState()
    val forYouLazyListState = rememberLazyListState()
    val homeFeedPullToRefreshState = rememberPullToRefreshState()
    val forYouFeedPullToRefreshState = rememberPullToRefreshState()
    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val showTagSheet = rememberSaveable { mutableStateOf(false) }
    val showAuthorSheet = rememberSaveable { mutableStateOf(false) }
    val searchBarState = rememberSearchBarState()
    val pageCount = if (uiState.enableForYou) 2 else 1
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val titles =
        remember(uiState.enableForYou) {
            if (uiState.enableForYou)
                listOf(
                    R.string.home_feed_label,
                    R.string.for_you_feed_label,
                )
            else listOf(R.string.home_feed_label)
        }

    val homeRefreshLoadState = homeFeedAnnouncements.loadState.refresh
    val forYouRefreshLoadState = forYouAnnouncements.loadState.refresh

    LaunchedEffect(uiState.enableForYou) {
        val targetPage =
            when (initialTab) {
                HomeTab.HOME -> 0
                HomeTab.FOR_YOU -> 1
            }

        if (targetPage < pagerState.pageCount) {
            pagerState.scrollToPage(targetPage)
        }
    }

    LaunchedEffect(uiState.userId) {
        analytics.setUserId(uiState.userId)
    }

    AnimatedVisibility(uiState.showSignInNotice) {
        SignInDialog(
            onConfirm = {
                analytics.logButtonClick("dialog_sign_in_confirm")
                context.launchSignIn()
            },
            onDismiss = {
                analytics.logButtonClick("dialog_sign_in_dismiss")
                onSignInNoticeDismissed()
            },
        )
    }

    AnimatedVisibility(uiState.isSignedIn) {
        NotificationRationale()
    }

    LaunchedEffect(searchBarState.isAnimating) {
        if (searchBarState.targetValue == SearchBarValue.Collapsed) {
            searchBarTextFieldState.clearText()
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .drop(1)
            .collect { page ->
                val tabName = if (page == 0) "home" else "for_you"
                analytics.logNavigationAction("tab_switch", tabName)
            }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        floatingActionButton = {
            AnimatedVisibility(
                visible = pagerState.currentPage == 0,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                IeeFloatingToolBar(
                    expanded = (homeFeedLazyListState.isScrollingUp()),
                    expandedAction = {
                        analytics.logButtonClick("fab_scroll_up")
                        scope.launch {
                            searchScroll.scrollOffset = 0f
                            homeFeedLazyListState.animateScrollToItem(0)
                        }
                    },
                    collapsedAction = {
                        analytics.logButtonClick("fab_search")
                        scope.launch { searchBarState.animateToExpanded() }
                    },
                    expandedIcon = { Icon(Icons.Filled.ArrowUpward, "Scroll to top") },
                    collapsedIcon = { Icon(Icons.Filled.Search, "Go to search") },
                    collapsedSecondaryIcons =
                        if (uiState.enableFabFilters) {
                            {
                                IconButton(
                                    onClick = {
                                        analytics.logButtonClick("toolbar_tags")
                                        analytics.logSheetOpened("tags_sheet")
                                        showTagSheet.value = true
                                    }
                                ) {
                                    Icon(Icons.Filled.Tag, "Filter")
                                }

                                IconButton(
                                    onClick = {
                                        analytics.logButtonClick("toolbar_authors")
                                        analytics.logSheetOpened("authors_sheet")
                                        showAuthorSheet.value = true
                                    }
                                ) {
                                    Icon(Icons.Filled.Person, "Filter")
                                }
                            }
                        } else null,
                )
            }
        },
        topBar = {
            SearchBar(
                scrollBehavior = searchScroll,
                quickResults = uiState.quickResults,
                searchBarState = searchBarState,
                searchHint = stringResource(R.string.search_bar_hint),
                textFieldState = searchBarTextFieldState,
                onSearch = { query ->
                    analytics.logSearch(query = query)
                    searchBarTextFieldState.clearText()
                    navigateToSearch(query, persistentListOf(), persistentListOf())
                },
                onAnnouncementQuickResultClick = { announcementId ->
                    analytics.logItemSelection(
                        announcementId.toString(),
                        "announcement_quick_result",
                    )
                    navigateToAnnouncement(announcementId)
                },
                onTagQuickResultClick = { tag ->
                    analytics.logItemSelection(tag.toString(), "tag_quick_result")
                    analytics.logSearch(tagIds = listOf(tag))
                    searchBarTextFieldState.clearText()
                    navigateToSearch("", persistentListOf(tag), persistentListOf())
                },
                onAuthorQuickResultClick = { author ->
                    analytics.logItemSelection(author.toString(), "author_quick_result")
                    analytics.logSearch(authorIds = listOf(author))
                    searchBarTextFieldState.clearText()
                    navigateToSearch("", persistentListOf(), persistentListOf(author))
                },
                expandedSecondaryActions = {
                    SearchBarFilters(
                        tagLabel = stringResource(R.string.search_bar_tag_label),
                        authorLabel = stringResource(R.string.search_bar_author_label),
                        selectedTagsCount = 0,
                        selectedAuthorsCount = 0,
                        openTagSheet = {
                            analytics.logSheetOpened("tags_sheet")
                            showTagSheet.value = true
                        },
                        openAuthorSheet = {
                            analytics.logSheetOpened("authors_sheet")
                            showAuthorSheet.value = true
                        },
                    )
                },
                collapsedSecondaryActions = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.animateContentSize(),
                    ) {
                        AnimatedVisibility(
                            visible = uiState.enableForYou,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut(),
                        ) {
                            SecondaryTabRow(selectedTabIndex = pagerState.currentPage) {
                                titles.forEachIndexed { index, title ->
                                    Tab(
                                        selected = pagerState.currentPage == index,
                                        selectedContentColor = MaterialTheme.colorScheme.primary,
                                        unselectedContentColor =
                                            MaterialTheme.colorScheme.onSurfaceVariant,
                                        onClick = {
                                            scope.launch { pagerState.animateScrollToPage(index) }
                                        },
                                        text = {
                                            Text(
                                                text = stringResource(title),
                                                style = MaterialTheme.typography.titleSmall,
                                            )
                                        },
                                    )
                                }
                            }
                        }

                        val showHomeError =
                            pagerState.currentPage == 0 &&
                                homeRefreshLoadState is LoadState.Error &&
                                homeFeedAnnouncements.itemCount > 0

                        val showForYouError =
                            pagerState.currentPage == 1 &&
                                forYouRefreshLoadState is LoadState.Error &&
                                forYouAnnouncements.itemCount > 0

                        AnimatedVisibility(
                            visible = showHomeError || showForYouError,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut(),
                        ) {
                            AnimatedContent(
                                targetState = pagerState.currentPage,
                                transitionSpec = {
                                    fadeIn() togetherWith
                                        fadeOut() using
                                        SizeTransform(clip = false)
                                },
                                label = "ErrorBannerTransition",
                            ) { page ->
                                when (page) {
                                    0 -> {
                                        IeeStatusBanner(
                                            text = "Home Feed Sync Failed.",
                                            icon = Icons.Default.CloudOff,
                                            actionLabel = "Retry",
                                            onActionClick = homeFeedAnnouncements::retry,
                                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                                        )
                                    }

                                    1 -> {
                                        IeeStatusBanner(
                                            text = "For You Feed Sync Failed.",
                                            icon = Icons.Default.CloudOff,
                                            actionLabel = "Retry",
                                            onActionClick = forYouAnnouncements::retry,
                                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (uiState.isSignedIn) {
                                analytics.logButtonClick("profile_icon")
                                navigateToProfile()
                            } else {
                                analytics.logButtonClick("sign_in_icon")
                                context.launchSignIn()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = if (uiState.isSignedIn) "Profile" else "Sign in",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            analytics.logButtonClick("settings_icon")
                            navigateToSettings()
                        }
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding())) {
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> {

                        PullToRefreshBox(
                            isRefreshing = homeRefreshLoadState is LoadState.Loading,
                            state = homeFeedPullToRefreshState,
                            onRefresh = {
                                analytics.logNavigationAction("refresh_gesture", "home")
                                vibrator.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                homeFeedAnnouncements.refresh()
                            },
                            indicator = {
                                LoadingIndicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    state = homeFeedPullToRefreshState,
                                    isRefreshing = homeRefreshLoadState is LoadState.Loading,
                                )
                            },
                        ) {
                            AnnouncementFeed(
                                refreshEmptyText = stringResource(R.string.feed_empty),
                                refreshLoadingText = stringResource(R.string.feed_placeholder),
                                refreshErrorText = stringResource(R.string.error_generic),
                                refreshRetryText = stringResource(R.string.action_retry),
                                appendLoadingText = stringResource(R.string.feed_footer_loading),
                                appendErrorText = stringResource(R.string.error_generic),
                                appendErrorRetryText = stringResource(R.string.action_retry),
                                endOfPaginationText = stringResource(R.string.feed_footer_finished),
                                announcements = homeFeedAnnouncements,
                                lazyListState = homeFeedLazyListState,
                                scrollBehavior = searchScroll,
                                onAnnouncementClick = { announcementId ->
                                    analytics.logItemSelection(
                                        announcementId.toString(),
                                        "announcement",
                                    )
                                    navigateToAnnouncement(announcementId)
                                },
                                onAnnouncementLongClick = { announcementId ->
                                    analytics.logAnnouncementShared(announcementId)
                                    context.shareAnnouncement(announcementId)
                                },
                                contentPadding =
                                    PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                            )
                        }
                    }

                    1 -> {
                        PullToRefreshBox(
                            isRefreshing = forYouRefreshLoadState is LoadState.Loading,
                            state = forYouFeedPullToRefreshState,
                            onRefresh = {
                                analytics.logNavigationAction("refresh_gesture", "for_you")
                                vibrator.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                forYouAnnouncements.refresh()
                            },
                            indicator = {
                                LoadingIndicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    state = forYouFeedPullToRefreshState,
                                    isRefreshing = forYouRefreshLoadState is LoadState.Loading,
                                )
                            },
                            content = {
                                AnnouncementFeed(
                                    refreshEmptyText = stringResource(R.string.feed_empty),
                                    refreshLoadingText = stringResource(R.string.feed_placeholder),
                                    refreshErrorText = stringResource(R.string.error_generic),
                                    refreshRetryText = stringResource(R.string.action_retry),
                                    appendLoadingText =
                                        stringResource(R.string.feed_footer_loading),
                                    appendErrorText = stringResource(R.string.error_generic),
                                    appendErrorRetryText = stringResource(R.string.action_retry),
                                    endOfPaginationText =
                                        stringResource(R.string.feed_footer_finished),
                                    announcements = forYouAnnouncements,
                                    lazyListState = forYouLazyListState,
                                    scrollBehavior = searchScroll,
                                    onAnnouncementClick = { announcementId ->
                                        analytics.logItemSelection(
                                            announcementId.toString(),
                                            "announcement",
                                        )
                                        navigateToAnnouncement(announcementId)
                                    },
                                    onAnnouncementLongClick = { announcementId ->
                                        analytics.logAnnouncementShared(announcementId)
                                        context.shareAnnouncement(announcementId)
                                    },
                                    contentPadding =
                                        PaddingValues(
                                            bottom = paddingValues.calculateBottomPadding()
                                        ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    if (showTagSheet.value) {
        TagSheet(
            tags = uiState.availableFilters.tags,
            onApply = { newTagIds ->
                scope.launch {
                    analytics.logFiltersApplied("tags", newTagIds)
                    analytics.logSearch(tagIds = newTagIds)
                    showTagSheet.value = false
                    searchBarState.animateToCollapsed()
                    navigateToSearch("", newTagIds, persistentListOf())
                }
            },
            onDismiss = { showTagSheet.value = false },
        )
    }

    if (showAuthorSheet.value) {
        AuthorSheet(
            authors = uiState.availableFilters.authors,
            onApply = { newAuthorIds ->
                scope.launch {
                    analytics.logFiltersApplied("authors", newAuthorIds)
                    analytics.logSearch(authorIds = newAuthorIds)
                    showAuthorSheet.value = false
                    searchBarState.animateToCollapsed()
                    navigateToSearch("", persistentListOf(), newAuthorIds)
                }
            },
            onDismiss = { showAuthorSheet.value = false },
        )
    }
}

@Composable
private fun SignInDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    IeeDialog(
        icon = Icons.AutoMirrored.Default.Login,
        title = stringResource(R.string.login_dialog_title),
        text = stringResource(R.string.login_dialog_description),
        confirmText = stringResource(R.string.action_sign_in),
        onConfirm = onConfirm,
        dismissText = stringResource(R.string.action_dismiss),
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationRationale() {
    var showRationale by rememberSaveable { mutableStateOf(true) }
    val notificationPermissionState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }

    notificationPermissionState?.let {
        if (!notificationPermissionState.status.isGranted && showRationale) {
            if (notificationPermissionState.status.shouldShowRationale) {
                IeeDialog(
                    icon = Icons.Default.NotificationsActive,
                    title = stringResource(R.string.notification_dialog_title),
                    text = stringResource(R.string.notification_dialog_description),
                    confirmText = stringResource(R.string.action_allow),
                    onConfirm = { notificationPermissionState.launchPermissionRequest() },
                    dismissText = stringResource(R.string.action_dismiss),
                    onDismiss = { showRationale = false },
                )
            } else {
                LaunchedEffect(Unit) {
                    delay(2000)
                    notificationPermissionState.launchPermissionRequest()
                }
            }
        }
    }
}

@Preview
@Composable
internal fun HomeScreenInitialLoadPreview() {
    val emptyFlow = flowOf(PagingData.empty<Announcement>())
    val lazyItems = emptyFlow.collectAsLazyPagingItems()
    IeePreview {
        HomeScreen(
            homeFeedAnnouncements = lazyItems,
            forYouAnnouncements = lazyItems,
        )
    }
}
