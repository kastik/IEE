package com.kastik.apps.core.ui.paging

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.theme.ieeListSpring
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.announcement.AnnouncementCard
import com.kastik.apps.core.ui.announcement.AnnouncementCardShimmer
import com.kastik.apps.core.ui.extensions.toFormattedString
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Clock

private enum class FeedState {
    Loading, Error, Empty, Content
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnnouncementFeed(
    loadingPlaceHolderText: String,
    nextPagePlaceHolderText: String,
    emptyPlaceHolderText: String,
    errorPlaceHolderText: String,
    errorPlaceHolderRetryText: String,
    errorNextPagePlaceHolderText: String,
    endOfPaginationText: String,
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


    var shouldShowErrorBanner by remember { mutableStateOf(false) }

    LaunchedEffect(refreshState) {
        when (refreshState) {
            is LoadState.Error -> shouldShowErrorBanner = true
            is LoadState.NotLoading -> shouldShowErrorBanner = false
            is LoadState.Loading -> Unit
        }
    }

    val currentFeedState = when (refreshState) {
        is LoadState.Loading if announcements.itemCount == 0 -> FeedState.Loading
        is LoadState.Error if announcements.itemCount == 0 -> FeedState.Error
        is LoadState.NotLoading if announcements.itemCount == 0 -> FeedState.Empty
        else -> FeedState.Content
    }

    AnimatedContent(
        targetState = currentFeedState, transitionSpec = {
            fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
        }, label = "feed_state_transition", modifier = modifier
    ) { state ->

        when (state) {
            FeedState.Loading -> {
                LoadingContent(
                    message = loadingPlaceHolderText,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            FeedState.Error -> {
                StatusContent(
                    message = errorPlaceHolderText,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            FeedState.Empty -> {
                StatusContent(
                    message = emptyPlaceHolderText,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            FeedState.Content -> {

                val isError = refreshState is LoadState.Error
                val firstItemKey = announcements.itemSnapshotList.firstOrNull()?.id

                LaunchedEffect(firstItemKey, isError) {
                    lazyListState.animateScrollToItem(0)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .testTag("announcement_feed"),
                    state = lazyListState,
                    contentPadding = contentPadding
                ) {

                    if (shouldShowErrorBanner) {
                        item(
                            key = "refresh_error_warning", contentType = "warning_banner"
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 8.dp)
                                    .animateItem(
                                        fadeInSpec = ieeListSpring(),
                                        fadeOutSpec = ieeListSpring(),
                                        placementSpec = ieeListSpring()
                                    ),
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Warning"
                                    )
                                    Text(
                                        text = errorPlaceHolderText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

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
                                date = item.date.toFormattedString(),
                                content = remember(item.preview) { item.preview },
                                isPinned = item.isPinned,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .animateItem(
                                        fadeInSpec = ieeListSpring(),
                                        fadeOutSpec = ieeListSpring(),
                                        placementSpec = ieeListSpring()
                                    )
                            )
                        } ?: AnnouncementCardShimmer(
                            modifier = Modifier
                                .padding(6.dp)
                                .animateItem(
                                    fadeInSpec = ieeListSpring(),
                                    fadeOutSpec = ieeListSpring(),
                                    placementSpec = ieeListSpring()
                                )
                        )
                    }

                    item(
                        key = "append_state",
                        contentType = "append_indicators"
                    ) {
                        when (appendState) {
                            is LoadState.Loading -> LoadingContent(
                                message = nextPagePlaceHolderText,
                                progressIndicatorSize = 32.dp,
                                modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
                            )

                            is LoadState.Error -> StatusContent(
                                message = errorNextPagePlaceHolderText,
                                action = { announcements.retry() },
                                actionText = errorPlaceHolderRetryText
                            )

                            is LoadState.NotLoading -> {
                                if (appendState.endOfPaginationReached) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp, bottom = 32.dp)
                                    ) {
                                        Text(
                                            text = endOfPaginationText,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Icon(
                                            Icons.Default.Celebration,
                                            contentDescription = null,
                                        )
                                    }
                                } else {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(
                                            12.dp, Alignment.CenterHorizontally
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(116.dp)
                                    ) {
                                        Text(
                                            text = "Please wait for the refresh to finish",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Icon(Icons.Default.Warning, null)
                                    }

                                }
                            }
                        }
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
            date = Clock.System.now(),
            tags = persistentListOf(Tag(1, "General"), Tag(2, "New")),
            preview = "This is the first announcement.",
            isPinned = true,
            body = "",
        ), Announcement(
            id = 2,
            author = "Admin",
            title = "Second Announcement",
            date = Clock.System.now(),
            tags = persistentListOf(Tag(1, "General")),
            preview = "This is the second announcement with a bit longer preview text to see how it renders.",
            isPinned = false,
            body = "",
        )
    )
    val lazyPagingItems = flowOf(PagingData.from(sampleAnnouncements)).collectAsLazyPagingItems()

    IeePreview {
        AnnouncementFeed(
            announcements = lazyPagingItems,
            loadingPlaceHolderText = "",
            nextPagePlaceHolderText = "",
            emptyPlaceHolderText = "",
            errorPlaceHolderText = "",
            errorPlaceHolderRetryText = "",
            errorNextPagePlaceHolderText = "",
            endOfPaginationText = ""
        )
    }
}


