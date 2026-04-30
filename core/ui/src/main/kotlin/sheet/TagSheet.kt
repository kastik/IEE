package com.kastik.apps.core.ui.sheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IEESelectableItem
import com.kastik.apps.core.designsystem.component.IEESheet
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.designsystem.theme.ieeListSpring
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagSheet(
    tagSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    tags: ImmutableList<Tag> = persistentListOf(),
    selectedTagIds: ImmutableList<Int> = persistentListOf(),
    onApply: (ImmutableList<Int>) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    var query by remember { mutableStateOf("") }

    val tagRoots = remember(tags) {
        buildTreeFromFlat(
            items = tags,
            idProvider = { it.id },
            parentIdProvider = { it.parentId },
            titleProvider = { it.title },
        )
    }

    val state = rememberHierarchicalSelection(
        roots = tagRoots,
        initialSelectedIds = selectedTagIds,
        query = query,
        idProvider = { it.data.id },
        titleProvider = { it.data.title },
        childrenProvider = { it.children },
    )

    IEESheet(
        sheetState = tagSheetState,
        onDismiss = onDismiss,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        searchHint = stringResource(R.string.sheet_hint_tag),
        hasSelection = state.selectedIds.isNotEmpty(),
        onClearSelection = { state.selectedIds.clear() },
        clearLabel = stringResource(R.string.action_clear),
        applyLabel = stringResource(R.string.action_apply_tags),
        onApply = {
            val appliedIds = getOnlyTopLevelSelected(
                roots = tagRoots,
                selectedIds = state.selectedIds.toSet(),
                idProvider = { it.data.id },
                childrenProvider = { it.children },
            )
            onApply(appliedIds.toImmutableList())
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items = state.flatList, key = { it.item.data.id }) { flatNode ->
                IEESelectableItem(
                    modifier = Modifier
                        .padding(start = (flatNode.depth * 16).dp)
                        .animateItem(
                            fadeInSpec = ieeListSpring(),
                            fadeOutSpec = ieeListSpring(),
                            placementSpec = ieeListSpring(),
                        ),
                    title = flatNode.item.data.title,
                    isSelected = flatNode.item.data.id in state.selectedIds,
                    onClick = { state.onToggle(flatNode.item) },
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
        Tag(id = 1, title = "Tag 1"),
        Tag(id = 2, title = "Tag 2"),
        Tag(id = 3, title = "Tag 3"),
    )
    AppsAboardTheme {
        TagSheet(
            tags = sampleTags,
            selectedTagIds = persistentListOf(1)
        )
    }
}