package com.kastik.apps.core.ui.sheet

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.common.extensions.removeAccents
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericRecursiveSheet(
    items: ImmutableList<T>,
    subscribedTags: ImmutableList<Int>,
    applySelectedTags: (ImmutableList<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    idProvider: (T) -> Int,
    labelProvider: (T) -> String,
    childrenProvider: (T) -> List<T>
) {
    var query by remember { mutableStateOf("") }
    val selectedIds = remember { mutableStateListOf<Int>().apply { addAll(subscribedTags) } }

    val flatList = remember(query, items) {
        val result = mutableListOf<FlatNode<T>>()

        fun traverse(nodes: List<T>, depth: Int): Boolean {
            var anyMatch = false
            for (item in nodes) {
                val selfMatch = query.isBlank() || labelProvider(item).removeAccents()
                    .contains(query.removeAccents(), ignoreCase = true)

                val startIndex = result.size

                val childrenMatch = traverse(childrenProvider(item), depth + 1)

                if (selfMatch || childrenMatch) {
                    result.add(startIndex, FlatNode(item, depth))
                    anyMatch = true
                }
            }
            return anyMatch
        }
        traverse(items, 0)
        result
    }

    ModalBottomSheet(sheetState = sheetState, onDismissRequest = onDismiss) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search...") },
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = flatList,
                key = { idProvider(it.item) }) { node ->
                val currentId = idProvider(node.item)
                val isSelected = currentId in selectedIds

                SelectableTagItem(
                    modifier = Modifier.padding(start = (node.depth * 16).dp),
                    title = labelProvider(node.item), isSelected = isSelected, onToggle = {
                        val descendants = collectAllIds(node.item, childrenProvider, idProvider)

                        if (isSelected) {
                            selectedIds.remove(currentId)
                            selectedIds.removeAll(descendants)
                        } else {
                            selectedIds.add(currentId)
                            selectedIds.addAll(descendants)
                        }
                    })
            }
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                applySelectedTags(selectedIds.toImmutableList())
                onDismiss()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Apply")
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