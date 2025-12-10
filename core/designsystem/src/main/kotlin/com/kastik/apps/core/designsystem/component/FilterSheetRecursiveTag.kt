package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.model.aboard.SubscribableTag

private fun collectDescendantIds(tag: SubscribableTag): List<Int> {
    val result = mutableListOf<Int>()

    fun traverse(node: SubscribableTag) {
        for (child in node.subTags) {
            result += child.id
            traverse(child)
        }
    }

    traverse(tag)
    return result
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheetRecursiveTag(
    tags: List<SubscribableTag>,
    applySelectedTags: () -> Unit,
    selectedRootIds: List<Int>,
    updateSelectedTagsIds: (List<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
) {
    var tagQuery by remember { mutableStateOf("") }
    val selectedIds = remember { mutableStateListOf<Int>().apply { addAll(selectedRootIds) } }

    val flatList = remember(tagQuery, tags) {
        val result = mutableListOf<FlatTagNode>()

        fun traverse(nodes: List<SubscribableTag>, depth: Int): Boolean {
            var anyMatch = false
            for (tag in nodes) {
                // Check simple match
                val selfMatch = tagQuery.isBlank() || tag.title.contains(tagQuery, true)

                // Track where the children start in the result list
                val startIndex = result.size

                // Recursively check children
                val childrenMatch = traverse(tag.subTags, depth + 1)

                if (selfMatch || childrenMatch) {
                    // Insert parent before children if match found
                    result.add(startIndex, FlatTagNode(tag, depth))
                    anyMatch = true
                }
            }
            return anyMatch
        }

        traverse(tags, 0)
        result
    }

    ModalBottomSheet(
        sheetState = sheetState, onDismissRequest = onDismiss
    ) {
        OutlinedTextField(
            value = tagQuery,
            onValueChange = { tagQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search tags...") },
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = flatList, key = { it.tag.id }) { node ->
                SelectableTagItem(
                    modifier = Modifier.padding(start = (node.depth * 16).dp),
                    title = node.tag.title,
                    isSelected = node.tag.id in selectedIds,
                    onToggle = {
                        val descendants = collectDescendantIds(node.tag)

                        if (node.tag.id in selectedIds) {
                            // Unselect parent AND all descendants
                            selectedIds.remove(node.tag.id)
                            selectedIds.removeAll(descendants)
                        } else {
                            // Select parent AND all descendants
                            selectedIds.add(node.tag.id)
                            selectedIds.addAll(descendants)
                        }
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                updateSelectedTagsIds(selectedIds)
                applySelectedTags()
                onDismiss()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Apply Tags")
        }
    }
}

@Immutable
private data class FlatTagNode(
    val tag: SubscribableTag, val depth: Int
)