package com.kastik.apps.core.ui.sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.kastik.apps.core.common.extensions.removeAccents

internal data class FlatNode<T>(
    val item: T,
    val depth: Int
)

internal data class TreeNode<T>(
    val data: T,
    val children: List<TreeNode<T>>,
)

internal fun <T> buildTreeFromFlat(
    items: List<T>,
    idProvider: (T) -> Int,
    parentIdProvider: (T) -> Int?,
    titleProvider: (T) -> String,
): List<TreeNode<T>> {
    val childrenMap = items.groupBy { parentIdProvider(it) }
    fun buildNode(item: T): TreeNode<T> {
        val children = childrenMap[idProvider(item)]
            ?.sortedBy { titleProvider(it) }
            ?.map { buildNode(it) }
            ?: emptyList()
        return TreeNode(item, children)
    }
    return items
        .filter { parentIdProvider(it) == null }
        .sortedBy { titleProvider(it) }
        .map { buildNode(it) }
}

internal fun <T> buildParentMap(
    roots: List<T>,
    idProvider: (T) -> Int,
    childrenProvider: (T) -> List<T>,
): Map<Int, T> {
    val map = mutableMapOf<Int, T>()
    fun build(nodes: List<T>, parent: T?) {
        for (node in nodes) {
            if (parent != null) map[idProvider(node)] = parent
            build(childrenProvider(node), node)
        }
    }
    build(roots, null)
    return map
}

internal fun <T> buildFlatList(
    roots: List<T>,
    query: String,
    titleProvider: (T) -> String,
    childrenProvider: (T) -> List<T>,
): List<FlatNode<T>> {
    val result = mutableListOf<FlatNode<T>>()
    fun traverse(nodes: List<T>, depth: Int): Boolean {
        var anyMatch = false
        for (node in nodes.sortedBy { titleProvider(it) }) {
            val selfMatch = query.isBlank() ||
                    titleProvider(node).removeAccents()
                        .contains(query.removeAccents(), ignoreCase = true)
            val insertIndex = result.size
            val childrenMatch = traverse(childrenProvider(node), depth + 1)
            if (selfMatch || childrenMatch) {
                result.add(insertIndex, FlatNode(node, depth))
                anyMatch = true
            }
        }
        return anyMatch
    }
    traverse(roots, 0)
    return result
}

internal fun <T> initializeSelection(
    roots: List<T>,
    selectedIds: Collection<Int>,
    idProvider: (T) -> Int,
    childrenProvider: (T) -> List<T>,
): Set<Int> {
    val result = mutableSetOf<Int>()
    fun init(nodes: List<T>, parentSelected: Boolean) {
        for (node in nodes) {
            val selected = parentSelected || idProvider(node) in selectedIds
            if (selected) result.add(idProvider(node))
            init(childrenProvider(node), selected)
        }
    }
    init(roots, false)
    return result
}

internal fun <T> collectDescendantIds(
    root: T,
    childrenProvider: (T) -> List<T>,
    idProvider: (T) -> Int,
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

internal fun <T> getOnlyTopLevelSelected(
    roots: List<T>,
    selectedIds: Set<Int>,
    idProvider: (T) -> Int,
    childrenProvider: (T) -> List<T>,
): List<Int> {
    val result = mutableListOf<Int>()
    fun traverse(nodes: List<T>, ancestorSelected: Boolean) {
        for (node in nodes) {
            val id = idProvider(node)
            val isSelected = id in selectedIds
            if (isSelected && !ancestorSelected) result.add(id)
            traverse(childrenProvider(node), ancestorSelected || isSelected)
        }
    }
    traverse(roots, false)
    return result
}

internal class HierarchicalSelectionState<T>(
    val selectedIds: androidx.compose.runtime.snapshots.SnapshotStateList<Int>,
    val flatList: List<FlatNode<T>>,
    val onToggle: (node: T) -> Unit,
)

@Composable
internal fun <T> rememberHierarchicalSelection(
    roots: List<T>,
    initialSelectedIds: Collection<Int>,
    query: String,
    idProvider: (T) -> Int,
    titleProvider: (T) -> String,
    childrenProvider: (T) -> List<T>,
): HierarchicalSelectionState<T> {
    val parentMap = remember(roots) {
        buildParentMap(roots, idProvider, childrenProvider)
    }

    val selectedIds = remember(roots, initialSelectedIds) {
        val initial = initializeSelection(roots, initialSelectedIds, idProvider, childrenProvider)
        mutableStateListOf<Int>().apply { addAll(initial) }
    }

    val flatList = remember(query, roots) {
        buildFlatList(roots, query, titleProvider, childrenProvider)
    }

    val onToggle: (T) -> Unit = { node ->
        val id = idProvider(node)
        val isSelected = id in selectedIds
        val descendants = collectDescendantIds(node, childrenProvider, idProvider)

        if (isSelected) {
            selectedIds.remove(id)
            selectedIds.removeAll(descendants.toSet())
            var parent = parentMap[id]
            while (parent != null) {
                selectedIds.remove(idProvider(parent))
                parent = parentMap[idProvider(parent)]
            }
        } else {
            selectedIds.add(id)
            selectedIds.addAll(descendants)
            var parent = parentMap[id]
            while (parent != null) {
                val allChildrenSelected =
                    childrenProvider(parent).all { idProvider(it) in selectedIds }
                if (allChildrenSelected) {
                    selectedIds.add(idProvider(parent))
                    parent = parentMap[idProvider(parent)]
                } else break
            }
        }
    }

    return HierarchicalSelectionState(selectedIds, flatList, onToggle)
}