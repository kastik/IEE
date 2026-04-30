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
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribableTagSheet(
    subscribeTagSheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    subscribableTags: ImmutableList<SubscribableTag>,
    subscribedTags: ImmutableList<Int>,
    onApply: (ImmutableList<Int>) -> Unit,
    onDismiss: () -> Unit,
) {
    var query by remember { mutableStateOf("") }

    val state = rememberHierarchicalSelection(
        roots = subscribableTags,
        initialSelectedIds = subscribedTags,
        query = query,
        idProvider = { it.id },
        titleProvider = { it.title },
        childrenProvider = { it.subTags },
    )

    IEESheet(
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
                roots = subscribableTags,
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
                IEESelectableItem(
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
        SubscribableTag(
            id = 2,
            title = "Sub Tag 1",
            parentId = 1,
            isPublic = true,
            createdAt = "",
            updatedAt = null,
            deletedAt = null,
            mailListName = "sub_tag_1",
            subTags = emptyList()
        ),
        SubscribableTag(
            id = 3,
            title = "Sub Tag 2",
            parentId = 1,
            isPublic = true,
            createdAt = "",
            updatedAt = null,
            deletedAt = null,
            mailListName = "sub_tag_2",
            subTags = listOf(
                SubscribableTag(
                    id = 4,
                    title = "Sub Tag 3",
                    parentId = 3,
                    isPublic = true,
                    createdAt = "",
                    updatedAt = null,
                    deletedAt = null,
                    mailListName = "sub_tag_3",
                    subTags = emptyList()
                ), SubscribableTag(
                    id = 5,
                    title = "Sub Tag 4",
                    parentId = 3,
                    isPublic = true,
                    createdAt = "",
                    updatedAt = null,
                    deletedAt = null,
                    mailListName = "sub_tag_4",
                    subTags = emptyList()
                )
            )
        ),
        SubscribableTag(
            id = 6,
            title = "Sub Tag 5",
            parentId = 1,
            isPublic = true,
            createdAt = "",
            updatedAt = null,
            deletedAt = null,
            mailListName = "sub_tag_5",
            subTags = emptyList()
        )
    )
    val sampleTags = persistentListOf(
        SubscribableTag(
            id = 1,
            title = "Root Tag",
            parentId = null,
            isPublic = true,
            createdAt = "",
            updatedAt = null,
            deletedAt = null,
            mailListName = "root_tag",
            subTags = sampleSubTags
        )
    )
    AppsAboardTheme {
        SubscribableTagSheet(
            subscribableTags = sampleTags,
            subscribedTags = persistentListOf(2, 4, 6),
            onApply = {},
            onDismiss = {}
        )
    }
}
