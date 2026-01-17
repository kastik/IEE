package com.kastik.apps.core.ui.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.QuickResultGroup
import com.kastik.apps.core.ui.announcement.AnnouncementCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarExpanded(
    navigateToAnnouncement: (Int) -> Unit,
    onSearch: (query: String, tagsId: List<Int>, authorIds: List<Int>) -> Unit,
    searchBarState: SearchBarState,
    inputField: @Composable () -> Unit,
    tagsQuickResults: List<Tag>,
    authorsQuickResults: List<Author>,
    announcements: List<Announcement>,
    modifier: Modifier = Modifier,
    supplementaryContent: @Composable () -> Unit = {},
) {
    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                supplementaryContent()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        QuickResultGroup(
                            items = tagsQuickResults,
                            icon = Icons.Default.Tag,
                            onItemClick = { tag ->
                                onSearch(
                                    "",
                                    listOf(tag.id),
                                    emptyList()
                                )
                            },
                            labelProvider = { it.title },
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            thickness = 0.dp
                        )
                        QuickResultGroup(
                            items = authorsQuickResults,
                            icon = Icons.Default.Person,
                            onItemClick = { author ->
                                onSearch(
                                    "",
                                    emptyList(),
                                    listOf(author.id)
                                )
                            },
                            labelProvider = { it.name },
                        )
                    }

                    if (announcements.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Quick results",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                        }
                        items(announcements) { item ->
                            AnnouncementCard(
                                onClick = { navigateToAnnouncement(item.id) },
                                publisher = item.author,
                                title = item.title,
                                categories = remember(item.tags) { item.tags.map { it.title } },
                                date = item.date,
                                content = remember(item.preview) { item.preview.orEmpty() },
                                isPinned = item.pinned
                            )
                        }
                    }
                }
            }
        }
    }
}

