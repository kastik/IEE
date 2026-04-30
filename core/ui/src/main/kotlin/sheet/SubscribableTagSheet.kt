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

    val parentMap = remember(subscribableTags) {
        val map = mutableMapOf<Int, SubscribableTag>()
        fun buildParentMap(nodes: List<SubscribableTag>, parent: SubscribableTag?) {
            for (node in nodes) {
                if (parent != null) map[node.id] = parent
                buildParentMap(node.subTags, node)
            }
        }
        buildParentMap(subscribableTags, null)
        map
    }

    val selectedIds = remember(subscribableTags, subscribedTags) {
        val initialSelection = mutableSetOf<Int>()

        fun initializeSelection(nodes: List<SubscribableTag>, isParentSelected: Boolean) {
            for (node in nodes) {
                val isSelected = isParentSelected || subscribedTags.contains(node.id)
                if (isSelected) initialSelection.add(node.id)
                initializeSelection(node.subTags, isSelected)
            }
        }

        initializeSelection(subscribableTags, false)
        mutableStateListOf<Int>().apply { addAll(initialSelection) }
    }

    val flatList = remember(query, subscribableTags) {
        val result = mutableListOf<FlatNode<SubscribableTag>>()

        fun traverse(nodes: List<SubscribableTag>, depth: Int): Boolean {
            var anyMatch = false
            for (item in nodes.sortedBy { it.title }) {
                val selfMatch = query.isBlank() || item.title.removeAccents()
                    .contains(query.removeAccents(), ignoreCase = true)

                val startIndex = result.size
                val childrenMatch = traverse(item.subTags, depth + 1)

                if (selfMatch || childrenMatch) {
                    result.add(startIndex, FlatNode(item, depth))
                    anyMatch = true
                }
            }
            return anyMatch
        }
        traverse(subscribableTags, 0)
        result
    }

    IEESheet(
        sheetState = subscribeTagSheetState,
        onDismiss = onDismiss,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        searchHint = stringResource(R.string.sheet_hint_subscribe),
        hasSelection = selectedIds.isNotEmpty(),
        onClearSelection = { selectedIds.clear() },
        clearLabel = stringResource(R.string.action_clear),
        applyLabel = stringResource(R.string.action_subscribe),
        onApply = {
            val appliedIds = getOnlyTopLevelSelected(
                roots = subscribableTags,
                selectedIds = selectedIds.toSet(),
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
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(items = flatList, key = { it.item.id }) { node ->
                val isSelected = node.item.id in selectedIds

                IEESelectableItem(
                    modifier = Modifier
                        .padding(start = (node.depth * 16).dp)
                        .animateItem(
                            fadeInSpec = ieeListSpring(),
                            fadeOutSpec = ieeListSpring(),
                            placementSpec = ieeListSpring(),
                        ),
                    title = node.item.title,
                    isSelected = isSelected,
                    onClick = {
                        val descendants = collectAllIds(node.item, { it.subTags }, { it.id })

                        if (isSelected) {
                            selectedIds.remove(node.item.id)
                            selectedIds.removeAll(descendants)

                            var p = parentMap[node.item.id]
                            while (p != null) {
                                selectedIds.remove(p.id)
                                p = parentMap[p.id]
                            }
                        } else {
                            selectedIds.add(node.item.id)
                            selectedIds.addAll(descendants)

                            var p = parentMap[node.item.id]
                            while (p != null) {
                                val allChildrenSelected = p.subTags.all { it.id in selectedIds }
                                if (allChildrenSelected) {
                                    selectedIds.add(p.id)
                                    p = parentMap[p.id]
                                } else {
                                    break
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}

private data class FlatNode<T>(val item: T, val depth: Int)

private fun <T> collectAllIds(
    root: T, childrenProvider: (T) -> List<T>, idProvider: (T) -> Int
): List<Int> {
    val result = mutableListOf<Int>()
    fun traverse(node: T) {
        for (child in childrenProvider(node)) {
            result.add(idProvider(child))
            traverse(child)
        }
    }
    traverse(root)
    return result
}

private fun <T> getOnlyTopLevelSelected(
    roots: List<T>, selectedIds: Set<Int>, idProvider: (T) -> Int, childrenProvider: (T) -> List<T>
): List<Int> {
    val result = mutableListOf<Int>()

    fun traverse(nodes: List<T>, isAncestorSelected: Boolean) {
        for (node in nodes) {
            val id = idProvider(node)
            val isSelected = id in selectedIds

            if (isSelected && !isAncestorSelected) {
                result.add(id)
            }

            traverse(childrenProvider(node), isAncestorSelected || isSelected)
        }
    }

    traverse(roots, false)
    return result
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
