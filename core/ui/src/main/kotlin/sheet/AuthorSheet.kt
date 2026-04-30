package com.kastik.apps.core.ui.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.common.extensions.removeAccents
import com.kastik.apps.core.designsystem.component.IEESelectableItem
import com.kastik.apps.core.designsystem.component.IEESheet
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorSheet(
    modifier: Modifier = Modifier,
    authorSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    authors: ImmutableList<Author> = persistentListOf(),
    preSelectedAuthorIds: ImmutableList<Int> = persistentListOf(),
    onApply: (ImmutableList<Int>) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    var query by remember { mutableStateOf("") }

    val currentSelection = remember(preSelectedAuthorIds) {
        mutableStateListOf<Int>().apply { addAll(preSelectedAuthorIds) }
    }

    val filteredItems = remember(query, authors) {
        if (query.isBlank()) authors
        else authors.filter {
            val normalizedQuery = query.removeAccents()
            val label = it.announcementCount?.let { count -> "${it.name} [$count]" } ?: it.name
            label.removeAccents().contains(normalizedQuery, ignoreCase = true)
        }
    }

    val groupedItems = remember(filteredItems) {
        filteredItems
            .sortedBy { it.announcementCount?.let { count -> "${it.name} [$count]" } ?: it.name }
            .groupBy { it.name.first().uppercaseChar() }
    }

    IEESheet(
        modifier = modifier,
        sheetState = authorSheetState,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        hasSelection = currentSelection.isNotEmpty(),
        onClearSelection = { currentSelection.clear() },
        searchHint = stringResource(R.string.sheet_hint_author),
        clearLabel = stringResource(R.string.action_clear),
        applyLabel = stringResource(R.string.action_apply_authors),
        onApply = { onApply(currentSelection.toImmutableList()) },
        onDismiss = onDismiss,
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            groupedItems.keys.sorted().forEach { header ->
                stickyHeader { AlphabetHeader(header, Modifier.animateItem()) }
                items(groupedItems[header] ?: emptyList()) { author ->
                    val id = author.id
                    val isSelected = id in currentSelection
                    val label = author.announcementCount?.let { count -> "${author.name} [$count]" }
                        ?: author.name
                    IEESelectableItem(
                        title = label,
                        isSelected = isSelected,
                        onClick = {
                            if (isSelected) currentSelection.remove(id) else currentSelection.add(
                                id
                            )
                        },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@Composable
private fun AlphabetHeader(
    char: Char,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = char.toString(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AuthorSheetPreview() {
    AppsAboardTheme {
        AuthorSheet(
            authors = persistentListOf(
                Author(id = 1, name = "Alice", announcementCount = 3),
                Author(id = 2, name = "Bob", announcementCount = 5),
                Author(id = 3, name = "Charlie", announcementCount = null),
                Author(id = 4, name = "David", announcementCount = 1),
            ), preSelectedAuthorIds = persistentListOf(1, 3)
        )
    }
}
