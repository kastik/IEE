package com.kastik.apps.feature.home

import android.content.Intent
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
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
import com.kastik.apps.core.designsystem.component.FloatingToolBar
import com.kastik.apps.core.designsystem.component.NotificationNotice
import com.kastik.apps.core.designsystem.component.PagingRefreshError
import com.kastik.apps.core.designsystem.component.PagingRefreshLoading
import com.kastik.apps.core.designsystem.component.PagingRefreshSuccess
import com.kastik.apps.core.designsystem.component.SearchBarCollapsed
import com.kastik.apps.core.designsystem.component.SignInNotice
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.designsystem.utils.TrackScreenViewEvent
import com.kastik.apps.core.designsystem.utils.isScrollingUp
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class, ExperimentalPermissionsApi::class,
)
@Composable
internal fun HomeScreenRoute(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyAnnouncementPagingItems = viewModel.announcements.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.refreshTrigger.collect {
            lazyAnnouncementPagingItems.refresh()
        }
    }

    TrackScreenViewEvent("home_screen")

    NotificationRationale()

    HomeScreenContent(
        uiState = uiState,
        announcements = lazyAnnouncementPagingItems,
        navigateToAnnouncement = navigateToAnnouncement,
        navigateToSettings = navigateToSettings,
        navigateToProfile = navigateToProfile,
        onSignInNoticeDismissed = viewModel::onSignInNoticeDismiss,
        navigateToSearch = navigateToSearch,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
private fun HomeScreenContent(
    uiState: UiState,
    announcements: LazyPagingItems<AnnouncementPreview>,
    navigateToSearch: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
    onSignInNoticeDismissed: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    val onSignIn = {
        val url =
            "https://login.it.teithe.gr/authorization?" +
                    "client_id=690a9861468c9b767cabdc40" +
                    "&response_type=code" +
                    "&scope=announcements,profile" +
                    "&redirect_uri=com.kastik.apps://auth"

        val intent = Intent(
            Intent.ACTION_VIEW, url.toUri()
        )
        context.startActivity(intent)
    }

    //TODO Re-implement refresh when changing sign states with usecase HERE!!!!


    AnimatedVisibility(uiState.showSignInNotice) {
        IEEDialog(
            icon = Icons.AutoMirrored.Default.Login,
            title = "Sign in",
            text = "Sign in to unlock all announcements. You are currently browsing with limited access.",
            confirmText = "Sign-in",
            onConfirm = { onSignIn(context) },
            dismissText = "Dismiss",
            onDismiss = onSignInNoticeDismissed
        )
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBarCollapsed(
            scrollBehavior = searchScroll,
            navigateToSearch = navigateToSearch,
            navigateToProfile = navigateToProfile,
            navigateToSettings = navigateToSettings,
            isSignedIn = uiState.isSignedIn,
            onSignInClick = onSignIn
        )
        Box {
            when (announcements.loadState.refresh) {
                is LoadState.Loading -> {
                    PagingRefreshLoading()
                }

                is LoadState.Error -> {
                    PagingRefreshError { announcements.retry() }
                }

                is LoadState.NotLoading -> {
                    PagingRefreshSuccess(
                        navigateToAnnouncement = navigateToAnnouncement,
                        lazyAnnouncements = announcements,
                        lazyListState = lazyListState,
                        searchScroll = searchScroll
                    )
                }
            }
            FloatingToolBar(
            IEEFloatingToolBar(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                expanded = (lazyListState.isScrollingUp()),
                expandedAction = {
                    analytics.logEvent("scroll_up_clicked")
                    scope.launch { lazyListState.animateScrollToItem(0) }
                },
                collapsedAction = {
                    analytics.logEvent("search_clicked")
                    navigateToSearch(
                        "", emptyList(), emptyList()
                    )
                },
                expandedIcon = { Icon(Icons.Filled.ArrowUpward, null) },
                collapsedIcon = { Icon(Icons.Filled.Search, null) },
            )

        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationRationale() {
    var showRationale by rememberSaveable { mutableStateOf(true) }
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        null
    }

    notificationPermissionState?.let {
        if (!notificationPermissionState.status.isGranted && showRationale) {
            if (notificationPermissionState.status.shouldShowRationale) {
                NotificationNotice(
                    onDismiss = { showRationale = false },
                    onConfirm = { notificationPermissionState.launchPermissionRequest() }
                )
            } else {
                LaunchedEffect(Unit) {
                    notificationPermissionState.launchPermissionRequest()
                }
            }
        }
    }
}



@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    val pagedAnnouncements = flowOf(PagingData.from(FakeAnnouncements)).collectAsLazyPagingItems()
    AppsAboardTheme {
        Surface {
            HomeScreenContent(
                uiState = UiState(),
                announcements = pagedAnnouncements,
                navigateToAnnouncement = {},
                navigateToSettings = {},
                navigateToProfile = {},
                onSignInNoticeDismissed = {},
                navigateToSearch = {},
            )
        }
    }
}

val FakeTags = listOf(
    Tag(
        id = 1, title = "Tag1",
    ), Tag(
        id = 2, title = "Tag2",
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

val FakeAnnouncements = listOf<AnnouncementPreview>(
    AnnouncementPreview(
        id = 1,
        title = "Announcement Title",
        preview = "The quick brow fox jumps over the lazy dog",
        author = "Kostas",
        tags = FakeTags,
        attachments = FakeAttachments,
        date = "10-12-2025 11:45"
    )
)