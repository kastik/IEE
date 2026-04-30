package com.kastik.apps.core.ui.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
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
import com.kastik.apps.core.common.extensions.removeAccents
import com.kastik.apps.core.designsystem.component.IEESelectableItem
import com.kastik.apps.core.designsystem.component.IEESheet
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagSheet(
    tagSheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    tags: ImmutableList<Tag> = persistentListOf(),
    selectedTagIds: ImmutableList<Int> = persistentListOf(),
    onApply: (ImmutableList<Int>) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    val currentSelection =
        remember(selectedTagIds) { mutableStateListOf<Int>().apply { addAll(selectedTagIds) } }

    val filteredItems = remember(query, tags) {
        if (query.isBlank()) tags
        else tags.filter {
            it.title.removeAccents().contains(query.removeAccents(), ignoreCase = true)
        }
    }

    IEESheet(
        sheetState = tagSheetState,
        onDismiss = onDismiss,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        searchHint = stringResource(R.string.sheet_hint_tag),
        hasSelection = currentSelection.isNotEmpty(),
        onClearSelection = { currentSelection.clear() },
        clearLabel = stringResource(R.string.action_clear),
        applyLabel = stringResource(R.string.action_apply_tags),
        onApply = { onApply(currentSelection.toImmutableList()) },
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(filteredItems) { tag ->
                val id = tag.id
                val isSelected = id in currentSelection
                IEESelectableItem(
                    title = tag.title,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TagSheetPreview() {
    val sampleTags = persistentListOf(
        Tag(id = 1, title = "Tag 1", isPublic = true),
        Tag(id = 2, title = "Tag 2", isPublic = true),
        Tag(id = 3, title = "Tag 3", isPublic = true),
    )
    AppsAboardTheme {
        TagSheet(
            tags = sampleTags,
            selectedTagIds = persistentListOf(1)
        )
    }
}
