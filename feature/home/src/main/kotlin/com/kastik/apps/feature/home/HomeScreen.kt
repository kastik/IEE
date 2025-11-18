package com.kastik.apps.feature.home

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.kastik.apps.core.model.aboard.AnnouncementAttachment
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementTag
import com.kastik.apps.feature.home.components.AnnouncementCard
import com.kastik.apps.feature.home.components.FloatingToolBar
import com.kastik.apps.feature.home.components.TestNewSearchBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit
) {
    val uiState = viewModel.uiState.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onScreenViewed()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.OpenUrl -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        event.url.toUri()
                    )
                    context.startActivity(intent)
                }
            }
        }
    }

    HomeScreenContent(
        navigateToAnnouncement = navigateToAnnouncement,
        navigateToSettings = navigateToSettings,
        navigateToProfile = navigateToProfile,
        announcements = uiState.announcements,
        isSignedIn = uiState.isSignedIn,
        hasEvaluatedAuth = uiState.hasEvaluatedAuth,
        showSignInNotice = uiState.showSignInNotice,
        onSignInNoticeDismissed = viewModel::onSignInNoticeDismiss,
        onSignInClicked = viewModel::onSignInClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreenContent(
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    announcements: Flow<PagingData<AnnouncementPreview>>,
    isSignedIn: Boolean,
    hasEvaluatedAuth: Boolean,
    showSignInNotice: Boolean,
    onSignInNoticeDismissed: () -> Unit,
    onSignInClicked: () -> Unit,
) {

    val scope = rememberCoroutineScope()
    val searchBarState = rememberSearchBarState()
    val pullRefreshState = rememberPullToRefreshState()
    val textFieldState = rememberTextFieldState()
    val lazyListState = rememberLazyListState()
    val lazyAnnouncements = announcements.collectAsLazyPagingItems()
    val isScrollingUp = lazyListState.isScrollingUp()
    val expandFab by remember {
        derivedStateOf {
            isScrollingUp && lazyListState.firstVisibleItemIndex < 6
        }
    }

    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()


    LaunchedEffect(isSignedIn) {
        Log.d("MyLog", "hasEvaluatedAuth: $hasEvaluatedAuth")
        if (hasEvaluatedAuth) {
            Log.d("MyLog", "hasEvaluatedAuth: refreshing")
            lazyAnnouncements.refresh()
        }
    }

    AnimatedVisibility(showSignInNotice) {
        AlertDialog(onDismissRequest = onSignInNoticeDismissed, confirmButton = {
            Button(
                onClick = onSignInClicked
            ) {
                Text("Sign-in")
            }
        }, title = {
            Text("Sign In Required")
        }, text = {
            Text(
                "You’re currently browsing limited content. " + "Sign-ing to view all the announcements\n\n" + "Would you like to sign in now?",
            )
        }, dismissButton = {
            TextButton(onClick = onSignInNoticeDismissed) {
                Text("Dismiss")
            }
        })
    }

    Scaffold(
        topBar = {
            TestNewSearchBar(
                scrollBehavior = searchScroll,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                navigateToSettings = navigateToSettings,
                navigateToProfile = navigateToProfile,
                isSignedIn = isSignedIn,
                onSignInClick = onSignInClicked
            )
        },
        floatingActionButton = {
            FloatingToolBar(expanded = (expandFab), onFabClick = {
                scope.launch {
                    lazyListState.animateScrollToItem(0)
                }
            }, nextPage = {
                scope.launch {
                    lazyListState.animateScrollToItem(lazyListState.firstVisibleItemIndex + 20)

                }
            }, prevPage = {
                scope.launch {
                    lazyListState.animateScrollToItem(lazyListState.firstVisibleItemIndex - 20)
                }
            })
        },
        floatingActionButtonPosition = FabPosition.End,


        ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (lazyAnnouncements.loadState.refresh) {
                is LoadState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularWavyProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("Fetching Announcements...")
                    }
                }

                is LoadState.Error -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Failed to load. Tap to retry.",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { lazyAnnouncements.retry() })
                    }
                }

                else -> Unit
            }

            PullToRefreshBox(
                isRefreshing = lazyAnnouncements.loadState.refresh is LoadState.Loading,
                state = pullRefreshState,
                onRefresh = { lazyAnnouncements.refresh() },
                indicator = {
                    LoadingIndicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = pullRefreshState,
                        isRefreshing = lazyAnnouncements.loadState.refresh is LoadState.Loading,
                    )
                }) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        //.nestedScroll(toolbarScroll)
                        .nestedScroll(searchScroll.nestedScrollConnection), state = lazyListState
                ) {
                    items(
                        count = lazyAnnouncements.itemCount,
                        key = lazyAnnouncements.itemKey { it.id },
                        contentType = lazyAnnouncements.itemContentType { "announcement_card" }) { index ->
                        val item = lazyAnnouncements[index]
                        if (item != null) {
                            AnnouncementCard(
                                onClick = { navigateToAnnouncement(item.id) },
                                publisher = item.author,
                                title = item.title,
                                categories = remember(item.tags) { item.tags.map { it.title } },
                                date = item.date,
                                content = remember(item.preview) { item.preview.orEmpty() })
                        }
                    }
                    when (lazyAnnouncements.loadState.append) {
                        is LoadState.Loading -> item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                CircularWavyProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text("Getting next page...")
                            }
                        }

                        is LoadState.Error -> item {
                            Text(
                                "Retry",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable { lazyAnnouncements.retry() })

                        }

                        else -> Unit
                    }
                }
            }
        }
        ExpandedFullScreenSearchBar(
            state = searchBarState, inputField = {
                SearchBarDefaults.InputField(
                    textFieldState = textFieldState,
                    searchBarState = searchBarState,
                    onSearch = {},
                    placeholder = { Text("Search...") })
            }) {
            LazyRow {
                item {
                    ElevatedFilterChip(
                        selected = true,
                        onClick = { /*TODO*/ },
                        label = { Text("All") },
                    )
                }
                item {
                    ElevatedFilterChip(
                        selected = false,
                        onClick = { /*TODO*/ },
                        label = { Text("None") },
                    )
                }
                item {
                    InputChip(
                        selected = false,
                        onClick = { /*TODO*/ },
                        label = { Text("None") },
                    )
                }
                item {
                    InputChip(
                        selected = true,
                        onClick = { /*TODO*/ },
                        label = { Text("None") },
                    )
                }
                item {
                    ElevatedFilterChip(
                        selected = false,
                        onClick = { /*TODO*/ },
                        label = { Text("None") },
                    )
                }


            }

        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
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

@Preview
@Composable
fun PreviewHomeScreenContent() {
    HomeScreenContent(
        navigateToAnnouncement = {},
        navigateToSettings = {},
        navigateToProfile = {},
        announcements = flowOf(PagingData.from(FakeAnnouncements)),
        isSignedIn = true,
        hasEvaluatedAuth = false,
        showSignInNotice = false,
        onSignInNoticeDismissed = {},
        onSignInClicked = {},
    )
}