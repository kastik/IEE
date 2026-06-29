package com.kastik.apps.core.ui.sheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.component.IeeSelectableItem
import com.kastik.apps.core.designsystem.component.IeeSheet
import com.kastik.apps.core.designsystem.theme.ieeListSpring
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribableTagSheet(
    subscribeTagSheetState: SheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    ),
    tags: ImmutableList<Tag>,
    subscribedTags: ImmutableList<Int>,
    onApply: (ImmutableList<Int>) -> Unit,
    onDismiss: () -> Unit,
) {
    var query by remember { mutableStateOf("") }

    val state = rememberHierarchicalSelection(
        roots = tags,
        initialSelectedIds = subscribedTags,
        query = query,
        idProvider = { it.id },
        titleProvider = { it.title },
        childrenProvider = { it.subTags },
    )

    IeeSheet(
        sheetState = subscribeTagSheetState,
        onDismiss = onDismiss,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        searchHint = stringResource(R.string.sheet_hint_subscribe),
        hasSelection = state.selectedIds.isNotEmpty(),
        onClearSelection = { state.selectedIds.clear() },
        clearLabel = stringResource(R.string.action_clear),
        applyLabel = stringResource(R.string.action_subscribe),
        onApply = {
            val appliedIds = getOnlyTopLevelSelected(
                roots = tags,
                selectedIds = state.selectedIds.toSet(),
                idProvider = { it.id },
                childrenProvider = { it.subTags },
            )
            onApply(appliedIds.toImmutableList())
            onDismiss()
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(items = state.flatList, key = { it.item.id }) { flatNode ->
                IeeSelectableItem(
                    modifier = Modifier
                        .padding(start = (flatNode.depth * 16).dp)
                        .animateItem(ieeListSpring(), ieeListSpring(), ieeListSpring()),
                    title = flatNode.item.title,
                    isSelected = flatNode.item.id in state.selectedIds,
                    onClick = { state.onToggle(flatNode.item) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SubscribableTagSheetPreview() {
    val sampleSubTags = listOf(
        Tag(
            id = 2,
            title = "Sub Tag 1",
        ),
        Tag(
            id = 3,
            title = "Sub Tag 2",
            subTags = listOf(
                Tag(
                    id = 4,
                    title = "Sub Tag 3",
                    parentId = 3,
                ), Tag(
                    id = 5,
                    title = "Sub Tag 4",
                )
            )
        ),
        Tag(
            id = 6,
            title = "Sub Tag 5",
        )
    )
    val sampleTags = persistentListOf(
        Tag(
            id = 1,
            title = "Root Tag",
            subTags = sampleSubTags
        )
    )
    IeePreview {
        SubscribableTagSheet(
            tags = sampleTags,
            subscribedTags = persistentListOf(2, 4, 6),
            onApply = {},
            onDismiss = {}
        )
    }
}
