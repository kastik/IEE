package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementTag
import kotlinx.coroutines.flow.flowOf


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PagingRefreshLoading(
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularWavyProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Fetching Announcements...",
            style = MaterialTheme.typography.bodyLarge

        )
    }
}

@Composable
fun PagingRefreshError(
    retry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Failed to load. Tap to retry.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(16.dp)
                .clickable { retry() })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PagingRefreshSuccess(
    navigateToAnnouncement: (Int) -> Unit,
    lazyAnnouncements: LazyPagingItems<AnnouncementPreview>,
    lazyListState: LazyListState,
    searchScroll: SearchBarScrollBehavior
) {

    val pullRefreshState = rememberPullToRefreshState()
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
                .nestedScroll(searchScroll.nestedScrollConnection),
            state = lazyListState
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
                        content = remember(item.preview) { item.preview.orEmpty() }
                    )
                } else {
                    AnnouncementCardShimmer()
                }
            }
            when (lazyAnnouncements.loadState.append) {
                is LoadState.Loading -> item {
                    PagingAppendLoading()
                }

                is LoadState.Error -> item {
                    PagingAppendError { lazyAnnouncements.retry() }

                }

                is LoadState.NotLoading -> {}
            }
        }
    }
}

@Preview
@Composable
private fun PagingRefreshLoadingPreview() {
    AppsAboardTheme {
        Surface {
            PagingRefreshLoading()
        }

    }
}

@Preview
@Composable
private fun PagingRefreshErrorPreview() {
    AppsAboardTheme {
        Surface {
            PagingRefreshError {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PagingRefreshSuccessPreview() {
    val sampleAnnouncements = listOf(
        AnnouncementPreview(
            id = 1,
            author = "Admin",
            title = "Welcome to AppsAboard!",
            date = "2024-01-01",
            tags = listOf(AnnouncementTag(1, "General"), AnnouncementTag(2, "New")),
            preview = "This is the first announcement."
        ),
        AnnouncementPreview(
            id = 2,
            author = "Admin",
            title = "Second Announcement",
            date = "2024-01-02",
            tags = listOf(AnnouncementTag(1, "General")),
            preview = "This is the second announcement with a bit longer preview text to see how it renders."
        )
    )
    val lazyPagingItems = flowOf(PagingData.from(sampleAnnouncements)).collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    Surface {
        AppsAboardTheme {
            PagingRefreshSuccess(
                navigateToAnnouncement = {},
                lazyAnnouncements = lazyPagingItems,
                lazyListState = lazyListState,
                searchScroll = scrollBehavior
            )
        }
    }
}
