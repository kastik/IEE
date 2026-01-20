package com.kastik.apps.core.ui.pagging

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.announcement.AnnouncementCard
import com.kastik.apps.core.ui.announcement.AnnouncementCardShimmer
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnnouncementFeed(
    announcements: LazyPagingItems<Announcement>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    scrollBehavior: SearchBarScrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior(),
    onAnnouncementClick: (Int) -> Unit = {},
    onAnnouncementLongClick: (Int) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val refreshState = announcements.loadState.refresh
    val appendState = announcements.loadState.append
    val vibrator = LocalHapticFeedback.current
    val _ = LocalAnalytics.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        state = lazyListState,
        contentPadding = contentPadding
    ) {
        items(
            count = announcements.itemCount,
            key = announcements.itemKey { it.id },
            contentType = announcements.itemContentType { "announcement_card" }) { index ->
            val item = announcements[index]
            item?.let {
                AnnouncementCard(
                    onClick = {
                        vibrator.performHapticFeedback(HapticFeedbackType.Confirm)
                        onAnnouncementClick(item.id)
                    },
                    onLonClick = {
                        vibrator.performHapticFeedback(HapticFeedbackType.LongPress)
                        onAnnouncementLongClick(item.id)
                    },
                    publisher = item.author,
                    title = item.title,
                    categories = remember(item.tags) {
                        item.tags.map { it.title }.toImmutableList()
                    },
                    date = item.date,
                    content = remember(item.preview) { item.preview.orEmpty() },
                    isPinned = item.pinned
                )
            } ?: AnnouncementCardShimmer()
        }
        when {
            (appendState is LoadState.Loading) && (refreshState is LoadState.NotLoading) -> item {
                LoadingContent(
                    message = "Getting next page...",
                    progressIndicatorSize = 32.dp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
                )
            }

            (appendState is LoadState.Error) && (refreshState is LoadState.NotLoading) -> item {
                StatusContent(
                    message = "Failed to load more announcements.",
                    action = { announcements.retry() },
                    actionText = "Retry"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AnnouncementFeedPreview() {
    val sampleAnnouncements = persistentListOf(
        Announcement(
            id = 1,
            author = "Admin",
            title = "Welcome to AppsAboard!",
            date = "2024-01-01",
            tags = persistentListOf(Tag(1, "General"), Tag(2, "New")),
            preview = "This is the first announcement.",
            pinned = true,
            body = "",
            attachments = emptyList(),
        ),
        Announcement(
            id = 2,
            author = "Admin",
            title = "Second Announcement",
            date = "2024-01-02",
            tags = persistentListOf(Tag(1, "General")),
            preview = "This is the second announcement with a bit longer preview text to see how it renders.",
            pinned = false,
            body = "",
            attachments = emptyList(),
        )
    )
    val lazyPagingItems = flowOf(PagingData.from(sampleAnnouncements)).collectAsLazyPagingItems()

    Surface {
        AppsAboardTheme {
            AnnouncementFeed(
                announcements = lazyPagingItems,
            )
        }
    }
}


