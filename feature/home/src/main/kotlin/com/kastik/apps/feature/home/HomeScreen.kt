package com.kastik.apps.feature.home

import android.content.Intent
import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.kastik.apps.core.designsystem.component.FloatingToolBar
import com.kastik.apps.core.designsystem.component.PagingRefreshError
import com.kastik.apps.core.designsystem.component.PagingRefreshLoading
import com.kastik.apps.core.designsystem.component.PagingRefreshSuccess
import com.kastik.apps.core.designsystem.component.SearchBarCollapsed
import com.kastik.apps.core.designsystem.component.SignInNotice
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.designsystem.utils.TrackScreenViewEvent
import com.kastik.apps.core.designsystem.utils.isScrollingUp
import com.kastik.apps.core.model.aboard.AnnouncementAttachment
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementTag
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
internal fun HomeScreenRoute(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyAnnouncementPagingItems = viewModel.announcements.collectAsLazyPagingItems()

    TrackScreenViewEvent("home_screen")

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.OpenUrl -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW, event.url.toUri()
                    )
                    context.startActivity(intent)
                }
            }
        }
    }

    HomeScreenContent(
        uiState = uiState,
        announcements = lazyAnnouncementPagingItems,
        navigateToAnnouncement = navigateToAnnouncement,
        navigateToSettings = navigateToSettings,
        navigateToProfile = navigateToProfile,
        onSignInNoticeDismissed = viewModel::onSignInNoticeDismiss,
        onSignInClicked = viewModel::onSignInClick,
        navigateToSearch = navigateToSearch
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HomeScreenContent(
    uiState: UiState,
    announcements: LazyPagingItems<AnnouncementPreview>,
    navigateToSearch: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
    onSignInClicked: () -> Unit,
    onSignInNoticeDismissed: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    LaunchedEffect(uiState.isSignedIn) {
        Log.d("MyLog", "hasEvaluatedAuth: ${uiState.hasEvaluatedAuth}")
        if (uiState.hasEvaluatedAuth) {
            Log.d("MyLog", "hasEvaluatedAuth: refreshing")
            announcements.refresh()
        }
    }

    AnimatedVisibility(uiState.showSignInNotice) {
        SignInNotice(
            onDismiss = onSignInNoticeDismissed, onSignIn = onSignInClicked
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchBarCollapsed(
            scrollBehavior = searchScroll,
            navigateToSettings = navigateToSettings,
            navigateToProfile = navigateToProfile,
            isSignedIn = uiState.isSignedIn,
            onSignInClick = onSignInClicked,
            navigateToSearch = navigateToSearch
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
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                expanded = (lazyListState.isScrollingUp()),
                expandedAction = { scope.launch { lazyListState.animateScrollToItem(0) } },
                collapsedAction = { navigateToSearch() },
                expandedIcon = { Icon(Icons.Filled.ArrowUpward, null) },
                collapsedIcon = { Icon(Icons.Filled.Search, null) },
            )

        }
    }
}


@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    val pagedAnnouncements = flowOf(PagingData.from(FakeAnnouncements))
        .collectAsLazyPagingItems()
    AppsAboardTheme {
        Surface {
            HomeScreenContent(
                uiState = UiState(),
                announcements = pagedAnnouncements,
                navigateToAnnouncement = {},
                navigateToSettings = {},
                navigateToProfile = {},
                onSignInNoticeDismissed = {},
                onSignInClicked = {},
                navigateToSearch = {},
            )
        }
    }
}

val FakeAnnouncementTags = listOf(
    AnnouncementTag(
        id = 1, title = "Tag1",
    ), AnnouncementTag(
        id = 2, title = "Tag2",
    )
)

val FakeAttachments = listOf(
    AnnouncementAttachment(
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
        tags = FakeAnnouncementTags,
        attachments = FakeAttachments,
        date = "10-12-2025 11:45"
    )
)