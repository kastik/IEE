package com.kastik.apps.feature.home

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.kastik.apps.core.designsystem.component.IEEDialog
import com.kastik.apps.core.designsystem.component.IEEFloatingToolBar
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.extensions.isScrollingUp
import com.kastik.apps.core.ui.paging.AnnouncementFeed
import com.kastik.apps.core.ui.sheet.AuthorSheet
import com.kastik.apps.core.ui.sheet.TagSheet
import com.kastik.apps.core.ui.topbar.SearchBar
import com.kastik.apps.core.ui.topbar.SearchBarFilters
import com.kastik.apps.feature.home.navigation.HomeTab
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenRoute(
    initialTab: HomeTab,
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch: (query: String, tagsId: ImmutableList<Int>, authorIds: ImmutableList<Int>) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val homeFeedAnnouncements = viewModel.homeFeedAnnouncements.collectAsLazyPagingItems()
    val forYouAnnouncements = viewModel.forYouFeedAnnouncements.collectAsLazyPagingItems()
    val textFieldState = viewModel.searchBarTextFieldState

    TrackScreenViewEvent("home_screen")

    HomeScreenContent(
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
private fun HomeScreenContent(
    uiState: UiState,
    initialTab: HomeTab,
    searchBarTextFieldState: TextFieldState,
    homeFeedAnnouncements: LazyPagingItems<Announcement>,
    forYouAnnouncements: LazyPagingItems<Announcement>,
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSearch: (query: String, tagIds: ImmutableList<Int>, authorIds: ImmutableList<Int>) -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSettings: () -> Unit,
    onSignInNoticeDismissed: () -> Unit,
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
    val titles = remember(uiState.enableForYou) {
        if (uiState.enableForYou) listOf(
            R.string.home_feed_label, R.string.for_you_feed_label
        ) else listOf(R.string.home_feed_label)
    }

    LaunchedEffect(uiState.enableForYou) {
        val targetPage = when (initialTab) {
            HomeTab.HOME -> 0
            HomeTab.FOR_YOU -> 1
        }

        if (targetPage < pagerState.pageCount) {
            pagerState.scrollToPage(targetPage)
        }
    }


    AnimatedVisibility(uiState.showSignInNotice) {
        SignInDialog(onConfirm = {
            analytics.logEvent("sign_in_clicked", mapOf("source" to "home_screen"))
            context.launchSignIn()
        }, onDismiss = {
            analytics.logEvent("sign_in_dismissed", mapOf("source" to "home_screen"))
            onSignInNoticeDismissed()
        })
    }

    AnimatedVisibility(uiState.isSignedIn) {
        NotificationRationale()
    }

    LaunchedEffect(searchBarState.isAnimating) {
        if (searchBarState.targetValue == SearchBarValue.Collapsed) {
            searchBarTextFieldState.clearText()
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 0) {
            analytics.logEvent(
                "tab_switched", mapOf(
                    "tab" to "home", "source" to "home_screen"
                )
            )
        } else {
            analytics.logEvent(
                "tab_switched", mapOf(
                    "tab" to "for_you", "source" to "home_screen"
                )
            )
        }
    }

    Scaffold(contentWindowInsets = WindowInsets.safeDrawing, floatingActionButton = {
        AnimatedVisibility(
            visible = pagerState.currentPage == 0, enter = scaleIn(), exit = scaleOut()
        ) {
            IEEFloatingToolBar(
                expanded = (homeFeedLazyListState.isScrollingUp()),
                expandedAction = {
                    analytics.logEvent(
                        "scroll_up_clicked", mapOf("source" to "home_screen")
                    )
                    scope.launch {
                        searchScroll.scrollOffset = 0f
                        homeFeedLazyListState.animateScrollToItem(0)
                    }
                },
                collapsedAction = {
                    analytics.logEvent(
                        "search_clicked", mapOf("source" to "home_screen")
                    )
                    scope.launch {
                        searchBarState.animateToExpanded()
                    }
                },
                expandedIcon = { Icon(Icons.Filled.ArrowUpward, "Scroll to top") },
                collapsedIcon = { Icon(Icons.Filled.Search, "Go to search") },
                collapsedSecondaryIcons = if (uiState.enableFabFilters) {
                    {
                        IconButton(onClick = {
                            showTagSheet.value = true
                        }) {
                            Icon(
                                Icons.Filled.Tag,
                                "Filter",
                            )
                        }
                        IconButton(onClick = {
                            showAuthorSheet.value = true
                        }) {
                            Icon(
                                Icons.Filled.Person,
                                "Filter",
                            )
                        }

                    }
                } else null

            )
        }


    }, topBar = {
        SearchBar(
            scrollBehavior = searchScroll,
            quickResults = uiState.quickResults,
            searchBarState = searchBarState,
            searchHint = stringResource(R.string.search_bar_hint),
            textFieldState = searchBarTextFieldState,
            onSearch = { query ->
                analytics.logEvent(
                    "search", mapOf(
                        "search_term" to query, "source" to "home_screen"
                    )
                )
                searchBarTextFieldState.clearText()
                navigateToSearch(query, persistentListOf(), persistentListOf())
            },
            onAnnouncementQuickResultClick = { announcementId ->
                analytics.logEvent(
                    "quick_result_click", mapOf(
                        "result_type" to "announcement",
                        "item_id" to announcementId,
                        "source" to "home_screen"
                    )
                )
                navigateToAnnouncement(announcementId)
            },
            onTagQuickResultClick = { tag ->
                analytics.logEvent(
                    "quick_result_click", mapOf(
                        "result_type" to "tag", "item_id" to tag, "source" to "home_screen"
                    )
                )
                searchBarTextFieldState.clearText()
                navigateToSearch("", persistentListOf(tag), persistentListOf())
            },
            onAuthorQuickResultClick = { author ->
                analytics.logEvent(
                    "quick_result_click", mapOf(
                        "result_type" to "author", "item_id" to author, "source" to "home_screen"
                    )
                )
                searchBarTextFieldState.clearText()
                navigateToSearch("", persistentListOf(), persistentListOf(author))
            },
            expandedSecondaryActions = {
                SearchBarFilters(
                    tagLabel = stringResource(R.string.search_bar_tag_label),
                    authorLabel = stringResource(R.string.search_bar_author_label),
                    selectedTagsCount = 0,
                    selectedAuthorsCount = 0,
                    openTagSheet = { showTagSheet.value = true },
                    openAuthorSheet = { showAuthorSheet.value = true })
            },
            collapsedSecondaryActions = {
                AnimatedVisibility(
                    visible = uiState.enableForYou,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    SecondaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                selectedContentColor = MaterialTheme.colorScheme.primary,
                                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                text = {
                                    Text(
                                        text = stringResource(title),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                })
                        }
                    }
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (uiState.isSignedIn) {
                            analytics.logEvent(
                                "profile_clicked", mapOf("source" to "home_screen")
                            )
                            navigateToProfile()
                        } else {
                            analytics.logEvent(
                                "sign_in_clicked", mapOf("source" to "home_screen")
                            )
                            context.launchSignIn()
                        }
                    }) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = if (uiState.isSignedIn) "Profile" else "Sign in",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    analytics.logEvent(
                        "settings_clicked", mapOf("source" to "home_screen")
                    )
                    navigateToSettings()
                }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
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
            HorizontalPager(
                state = pagerState,
            ) { page ->
                when (page) {
                    0 -> {
                        val refreshState = homeFeedAnnouncements.loadState.refresh

                        PullToRefreshBox(
                            isRefreshing = refreshState is LoadState.Loading,
                            state = homeFeedPullToRefreshState,
                            onRefresh = {
                                vibrator.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                homeFeedAnnouncements.refresh()
                            },
                            indicator = {
                                LoadingIndicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    state = homeFeedPullToRefreshState,
                                    isRefreshing = refreshState is LoadState.Loading,
                                )
                            }) {
                            AnnouncementFeed(
                                loadingPlaceHolderText = stringResource(R.string.feed_placeholder),
                                nextPagePlaceHolderText = stringResource(R.string.feed_footer_loading),
                                emptyPlaceHolderText = stringResource(R.string.feed_empty),
                                errorPlaceHolderText = stringResource(R.string.error_generic),
                                errorPlaceHolderRetryText = stringResource(R.string.action_retry),
                                errorNextPagePlaceHolderText = stringResource(R.string.feed_footer_failed),
                                endOfPaginationText = stringResource(R.string.feed_footer_finished),
                                announcements = homeFeedAnnouncements,
                                lazyListState = homeFeedLazyListState,
                                scrollBehavior = searchScroll,
                                onAnnouncementClick = { announcementId ->
                                    analytics.logEvent(
                                        "announcement_clicked", mapOf(
                                            "item_id" to announcementId, "source" to "home_screen"
                                        )
                                    )
                                    navigateToAnnouncement(announcementId)
                                },
                                onAnnouncementLongClick = { announcementId ->
                                    analytics.logEvent(
                                        "announcement_shared", mapOf(
                                            "item_id" to announcementId, "source" to "home_screen"
                                        )
                                    )
                                    context.shareAnnouncement(announcementId)
                                },
                                contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                            )
                        }

                    }

                    1 -> {
                        val refreshState = forYouAnnouncements.loadState.refresh

                        PullToRefreshBox(
                            isRefreshing = refreshState is LoadState.Loading,
                            state = forYouFeedPullToRefreshState,
                            onRefresh = {
                                vibrator.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                forYouAnnouncements.refresh()
                            },
                            indicator = {
                                LoadingIndicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    state = forYouFeedPullToRefreshState,
                                    isRefreshing = refreshState is LoadState.Loading,
                                )
                            },
                            content = {
                                AnnouncementFeed(
                                    loadingPlaceHolderText = stringResource(R.string.feed_placeholder),
                                    nextPagePlaceHolderText = stringResource(R.string.feed_footer_loading),
                                    emptyPlaceHolderText = stringResource(R.string.feed_for_you_empty),
                                    errorPlaceHolderText = stringResource(R.string.error_generic),
                                    errorPlaceHolderRetryText = stringResource(R.string.action_retry),
                                    errorNextPagePlaceHolderText = stringResource(R.string.feed_footer_failed),
                                    endOfPaginationText = stringResource(R.string.feed_footer_finished),
                                    announcements = forYouAnnouncements,
                                    lazyListState = forYouLazyListState,
                                    scrollBehavior = searchScroll,
                                    onAnnouncementClick = { announcementId ->
                                        analytics.logEvent(
                                            "announcement_clicked", mapOf(
                                                "item_id" to announcementId,
                                                "source" to "for_you_screen"
                                            )
                                        )
                                        navigateToAnnouncement(announcementId)
                                    },
                                    onAnnouncementLongClick = { announcementId ->
                                        analytics.logEvent(
                                            "announcement_shared", mapOf(
                                                "item_id" to announcementId,
                                                "source" to "for_you_screen"
                                            )
                                        )
                                        context.shareAnnouncement(announcementId)
                                    },
                                    contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
                                )
                            })
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
                    analytics.logEvent(
                        "tags_applied", mapOf(
                            "item_id" to newTagIds, "source" to "home_screen"
                        )
                    )
                    showTagSheet.value = false
                    searchBarState.animateToCollapsed()
                    navigateToSearch("", newTagIds, persistentListOf())
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
            onApply = { newAuthorIds ->
                scope.launch {
                    analytics.logEvent(
                        "authors_applied",
                        mapOf("item_id" to newAuthorIds, "source" to "home_screen")
                    )
                    showAuthorSheet.value = false
                    searchBarState.animateToCollapsed()
                    navigateToSearch("", persistentListOf(), newAuthorIds)
                }
            },
            onDismiss = {
                showAuthorSheet.value = false
            },
        )
    }
}


@Composable
fun SignInDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    IEEDialog(
        icon = Icons.AutoMirrored.Default.Login,
        title = stringResource(R.string.login_dialog_title),
        text = stringResource(R.string.login_dialog_description),
        confirmText = stringResource(R.string.action_sign_in),
        onConfirm = onConfirm,
        dismissText = stringResource(R.string.action_dismiss),
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationRationale() {
    var showRationale by rememberSaveable { mutableStateOf(true) }
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    notificationPermissionState?.let {
        if (!notificationPermissionState.status.isGranted && showRationale) {
            if (notificationPermissionState.status.shouldShowRationale) {
                IEEDialog(
                    icon = Icons.Default.NotificationsActive,
                    title = stringResource(R.string.notification_dialog_title),
                    text = stringResource(R.string.notification_dialog_description),
                    confirmText = stringResource(R.string.action_allow),
                    onConfirm = { notificationPermissionState.launchPermissionRequest() },
                    dismissText = stringResource(R.string.action_dismiss),
                    onDismiss = { showRationale = false })
            } else {
                LaunchedEffect(Unit) {
                    delay(2000)
                    notificationPermissionState.launchPermissionRequest()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    val pagedAnnouncements = flowOf(PagingData.from(FakeAnnouncements)).collectAsLazyPagingItems()
    AppsAboardTheme {
        Surface {
            HomeScreenContent(
                uiState = UiState(),
                initialTab = HomeTab.HOME,
                homeFeedAnnouncements = pagedAnnouncements,
                forYouAnnouncements = pagedAnnouncements,
                navigateToAnnouncement = {},
                navigateToSettings = {},
                navigateToProfile = {},
                onSignInNoticeDismissed = {},
                navigateToSearch = { _, _, _ -> },
                searchBarTextFieldState = TextFieldState(),
            )
        }
    }
}

val FakeTags = listOf(
    Tag(
        id = 1, title = "Tag1", false
    ), Tag(
        id = 2, title = "Tag2", false
    )
)

val FakeAttachments = listOf(
    Attachment(
        id = 1,
        filename = "image.jpg",
        fileSize = 1000,
        mimeType = "TODO()",
    )
)

val FakeAnnouncements = listOf(
    Announcement(
        id = 1,
        title = "Announcement Title",
        preview = "The quick brow fox jumps over the lazy dog",
        author = "Kostas",
        tags = FakeTags,
        attachments = FakeAttachments,
        date = "10-12-2025 11:45",
        pinned = false,
        body = "",
    )
)