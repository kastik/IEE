package com.kastik.apps.core.ui.paging

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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

private enum class FeedState {
    Loading,
    Error,
    Empty,
    Content
}

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
    val analytics = LocalAnalytics.current


    val currentFeedState = when (refreshState) {
        is LoadState.Loading if announcements.itemCount == 0 -> FeedState.Loading
        is LoadState.Error if announcements.itemCount == 0 -> FeedState.Error
        is LoadState.NotLoading if announcements.itemCount == 0 -> FeedState.Empty
        else -> FeedState.Content
    }

    AnimatedContent(
        targetState = currentFeedState,
        transitionSpec = {
            fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
        },
        label = "feed_state_transition",
        modifier = modifier
    ) { state ->

        when (state) {
            FeedState.Loading -> {
                LoadingContent(
                    message = "Fetching Announcements...",
                    modifier = Modifier.fillMaxSize(),
                )
            }

            FeedState.Error -> {
                StatusContent(
                    message = "Something went wrong.",
                    modifier = Modifier.fillMaxSize(),
                )
            }

            FeedState.Empty -> {
                StatusContent(
                    message = "Nothing matched your search.",
                    modifier = Modifier.fillMaxSize(),
                )
            }

            FeedState.Content -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .semantics { contentDescription = "announcement_feed" },
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
                                    analytics.logEvent(
                                        "announcement_click",
                                        mapOf("announcement_id" to item.id)
                                    )
                                    vibrator.performHapticFeedback(HapticFeedbackType.Confirm)
                                    onAnnouncementClick(item.id)
                                },
                                onLonClick = {
                                    analytics.logEvent(
                                        "announcement_long_click",
                                        mapOf("announcement_id" to item.id)
                                    )
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
                                isPinned = item.pinned,
                                modifier = Modifier.animateItem()
                            )
                        } ?: AnnouncementCardShimmer(
                            modifier = Modifier.animateItem()
                        )
                    }
                    when (appendState) {
                        is LoadState.Loading -> item {
                            LoadingContent(
                                message = "Getting next page...",
                                progressIndicatorSize = 32.dp,
                                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
                            )
                        }

                        is LoadState.Error -> item {
                            StatusContent(
                                message = "Failed to load more announcements.",
                                action = { announcements.retry() },
                                actionText = "Retry"
                            )
                        }

                        else -> Unit
                    }
                }
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
            tags = persistentListOf(Tag(1, "General", false), Tag(2, "New", false)),
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
            tags = persistentListOf(Tag(1, "General", false)),
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


