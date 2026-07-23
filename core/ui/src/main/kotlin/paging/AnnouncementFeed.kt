package com.kastik.apps.core.ui.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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
import kotlin.time.Clock
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnnouncementFeed(
    refreshEmptyText: String,
    refreshLoadingText: String,
    refreshErrorText: String,
    refreshRetryText: String,
    appendLoadingText: String,
    appendErrorText: String,
    appendErrorRetryText: String,
    endOfPaginationText: String,
    announcements: LazyPagingItems<Announcement>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    scrollBehavior: SearchBarScrollBehavior =
        SearchBarDefaults.enterAlwaysSearchBarScrollBehavior(),
    onAnnouncementClick: (Int) -> Unit = {},
    onAnnouncementLongClick: (Int) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {

    val vibrator = LocalHapticFeedback.current

    val refreshState = announcements.loadState.refresh
    val appendState = announcements.loadState.append
    val isEmpty = announcements.itemCount == 0

    LaunchedEffect(announcements) {
        snapshotFlow { announcements.itemSnapshotList.firstOrNull()?.id }
            .filterNotNull()
            .distinctUntilChanged()
            .collect {
                lazyListState.animateScrollToItem(0)
            }
    }

    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .testTag("announcement_feed"),
        state = lazyListState,
        contentPadding = contentPadding,
    ) {
        when (refreshState) {
            is LoadState.Loading ->
                if (isEmpty) {
                    item(
                        key = "refresh_state_loading",
                        contentType = "refresh_indicators",
                    ) {
                        StatusContent(
                            modifier =
                                Modifier.fillParentMaxSize()
                                    .animateItem(
                                        fadeInSpec = ieeListSpring(),
                                        fadeOutSpec = ieeListSpring(),
                                        placementSpec = ieeListSpring(),
                                    ),
                            message = {
                                LoadingContent(message = refreshLoadingText)
                            },
                        )
                    }
                }

            is LoadState.Error ->
                if (isEmpty) {
                    item(
                        key = "refresh_state_error",
                        contentType = "refresh_indicators",
                    ) {
                        StatusContent(
                            modifier =
                                Modifier.fillParentMaxSize()
                                    .animateItem(
                                        fadeInSpec = ieeListSpring(),
                                        fadeOutSpec = ieeListSpring(),
                                        placementSpec = ieeListSpring(),
                                    ),
                            message = {
                                Text(
                                    text = refreshErrorText,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                            action = {
                                FilledTonalButton(onClick = announcements::retry) {
                                    Text(
                                        text = refreshRetryText,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            },
                        )
                    }
                }

            is LoadState.NotLoading -> {
                if (isEmpty) {
                    item(
                        key = "refresh_state_not_loading",
                        contentType = "refresh_indicators",
                    ) {
                        StatusContent(
                            modifier =
                                Modifier.fillParentMaxSize()
                                    .animateItem(
                                        fadeInSpec = ieeListSpring(),
                                        fadeOutSpec = ieeListSpring(),
                                        placementSpec = ieeListSpring(),
                                    ),
                            message = {
                                Text(
                                    text = refreshEmptyText,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                        )
                    }
                }
            }
        }

        items(
            count = announcements.itemCount,
            key = announcements.itemKey { it.id },
            contentType = announcements.itemContentType { "announcement_card" },
        ) { index ->
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
                    categories =
                        remember(item.tags) {
                            item.tags.map { it.title }.toImmutableList()
                        },
                    date = item.date.toFormattedString(),
                    content = remember(item.preview) { item.preview },
                    isPinned = item.isPinned,
                    modifier =
                        Modifier.padding(6.dp)
                            .animateItem(
                                fadeInSpec = ieeListSpring(),
                                fadeOutSpec = ieeListSpring(),
                                placementSpec = ieeListSpring(),
                            ),
                )
            }
                ?: AnnouncementCardShimmer(
                    modifier =
                        Modifier.padding(6.dp)
                            .animateItem(
                                fadeInSpec = ieeListSpring(),
                                fadeOutSpec = ieeListSpring(),
                                placementSpec = ieeListSpring(),
                            )
                )
        }

        when (appendState) {
            is LoadState.Loading -> {
                item(
                    key = "append_state_loading",
                    contentType = "append_indicators",
                ) {
                    LoadingContent(
                        message = appendLoadingText,
                        progressIndicatorSize = 32.dp,
                        modifier =
                            Modifier.padding(top = 16.dp, bottom = 32.dp)
                                .animateItem(
                                    fadeInSpec = ieeListSpring(),
                                    fadeOutSpec = ieeListSpring(),
                                    placementSpec = ieeListSpring(),
                                ),
                    )
                }
            }

            is LoadState.Error ->
                item(
                    key = "append_state_error",
                    contentType = "append_indicators",
                ) {
                    StatusContent(
                        modifier =
                            Modifier.padding(24.dp)
                                .animateItem(
                                    fadeInSpec = ieeListSpring(),
                                    fadeOutSpec = ieeListSpring(),
                                    placementSpec = ieeListSpring(),
                                ),
                        message = {
                            Text(
                                text = appendErrorText,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        },
                        action = {
                            FilledTonalButton(onClick = announcements::retry) {
                                Text(
                                    text = appendErrorRetryText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        },
                    )
                }

            is LoadState.NotLoading -> {
                if (appendState.endOfPaginationReached && !isEmpty) {
                    item(
                        key = "append_state_end_of_pagination",
                        contentType = "append_indicators",
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 32.dp)
                                    .animateItem(
                                        fadeInSpec = ieeListSpring(),
                                        fadeOutSpec = ieeListSpring(),
                                        placementSpec = ieeListSpring(),
                                    ),
                        ) {
                            Text(
                                text = endOfPaginationText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Icon(
                                Icons.Default.Celebration,
                                contentDescription = null,
                            )
                        }
                    }
                } else if (!isEmpty) {

                    val hasError =
                        announcements.loadState.append as? LoadState.Error
                            ?: announcements.loadState.refresh as? LoadState.Error

                    if (hasError != null) {
                        item(
                            key = "append_state_has_error",
                            contentType = "append_indicators",
                        ) {
                            StatusContent(
                                modifier =
                                    Modifier.padding(24.dp)
                                        .animateItem(
                                            fadeInSpec = ieeListSpring(),
                                            fadeOutSpec = ieeListSpring(),
                                            placementSpec = ieeListSpring(),
                                        ),
                                message = {
                                    Text(
                                        text = appendErrorText,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                },
                                action = {
                                    FilledTonalButton(onClick = announcements::retry) {
                                        Text(
                                            text = appendErrorRetryText,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                    }
                                },
                            )
                        }
                    } else {
                        item(
                            key = "append_state_spacer",
                            contentType = "append_indicators",
                        ) {
                            Spacer(
                                modifier =
                                    Modifier.height(116.dp)
                                        .animateItem(
                                            fadeInSpec = ieeListSpring(),
                                            fadeOutSpec = ieeListSpring(),
                                            placementSpec = ieeListSpring(),
                                        )
                            )
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
    val sampleAnnouncements =
        persistentListOf(
            Announcement(
                id = 1,
                author = "Admin",
                title = "Welcome to AppsAboard!",
                date = Clock.System.now(),
                tags = persistentListOf(Tag(1, "General"), Tag(2, "New")),
                preview = "This is the first announcement.",
                isPinned = true,
                body = "",
            ),
            Announcement(
                id = 2,
                author = "Admin",
                title = "Second Announcement",
                date = Clock.System.now(),
                tags = persistentListOf(Tag(1, "General")),
                preview =
                    "This is the second announcement with a bit longer preview text to see how it renders.",
                isPinned = false,
                body = "",
            ),
        )
    val lazyPagingItems = flowOf(PagingData.from(sampleAnnouncements)).collectAsLazyPagingItems()

    IeePreview {
        AnnouncementFeed(
            announcements = lazyPagingItems,
            refreshEmptyText = "",
            refreshLoadingText = "",
            refreshErrorText = "",
            refreshRetryText = "",
            appendLoadingText = "",
            appendErrorText = "",
            appendErrorRetryText = "",
            endOfPaginationText = "",
        )
    }
}

@Preview
@Composable
fun LoadingContentPagingPreview() {
    IeePreview {
        LoadingContent(
            message = "Getting next page...",
            progressIndicatorSize = 32.dp,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
        )
    }
}
