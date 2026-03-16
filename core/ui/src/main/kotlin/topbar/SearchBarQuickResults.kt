package com.kastik.apps.core.ui.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.theme.ieeListSpring
import com.kastik.apps.core.designsystem.theme.roundBottomShape
import com.kastik.apps.core.designsystem.theme.roundShape
import com.kastik.apps.core.designsystem.theme.roundTopShape
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.ui.announcement.AnnouncementCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun <T> LazyListScope.searchBarQuickResults(
    titleKey: String,
    title: @Composable () -> String,
    items: ImmutableList<T>,
    icon: ImageVector,
    onItemClick: (T) -> Unit,
    labelProvider: (T) -> String,
    keyProvider: (T) -> Any,
    modifier: Modifier = Modifier,
) {


    if (items.isNotEmpty()) {
        item(key = titleKey) {
            QuickResultsTitle(
                title = title(),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .animateItem(
                        fadeInSpec = ieeListSpring(),
                        fadeOutSpec = ieeListSpring(),
                        placementSpec = ieeListSpring()
                    )
            )
        }
    }

    itemsIndexed(
        items = items, key = { _, item -> keyProvider(item) }) { index, item ->

        val shape = when {
            items.size <= 1 -> roundShape
            index == 0 -> roundTopShape
            index == items.size - 1 -> roundBottomShape
            else -> RectangleShape
        }

        Surface(
            modifier = modifier
                .padding(vertical = 2.dp)
                .animateItem(
                    fadeInSpec = ieeListSpring(),
                    fadeOutSpec = ieeListSpring(),
                    placementSpec = ieeListSpring()
                ),
            shape = shape,
            color = MaterialTheme.colorScheme.surfaceContainer,
            shadowElevation = 2.dp,
            contentColor = MaterialTheme.colorScheme.onSurface,

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item) }
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(icon, contentDescription = null)
                Text(
                    text = labelProvider(item),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

fun LazyListScope.searchBarQuickResults(
    titleKey: String,
    title: @Composable () -> String,
    items: ImmutableList<Announcement>,
    onItemClick: (Announcement) -> Unit,
    keyProvider: (Announcement) -> String,
    modifier: Modifier = Modifier,
) {

    if (items.isNotEmpty()) {
        item(key = titleKey) {
            QuickResultsTitle(
                title = title(),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .animateItem(
                        fadeInSpec = ieeListSpring(),
                        fadeOutSpec = ieeListSpring(),
                        placementSpec = ieeListSpring()
                    )
            )
        }
    }

    items(
        items = items, key = { keyProvider(it) }) { item ->
        AnnouncementCard(
            modifier = modifier
                .padding(vertical = 2.dp)
                .animateItem(
                    fadeInSpec = ieeListSpring(),
                    fadeOutSpec = ieeListSpring(),
                    placementSpec = ieeListSpring()
                ),
            onClick = { onItemClick(item) },
            publisher = item.author,
            title = item.title,
            categories = remember(item.tags) {
                item.tags.map { it.title }.toImmutableList()
            },
            date = item.date,
            content = remember(item.preview) { item.preview },
            isPinned = item.pinned
        )
    }
}


@Composable
private fun QuickResultsTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}