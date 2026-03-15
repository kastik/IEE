package com.kastik.apps.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.common.extensions.removeAccents
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> IEESheet(
    modifier: Modifier = Modifier,
    items: ImmutableList<T>,
    selectedIds: ImmutableList<Int>,
    onApply: (ImmutableList<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    idProvider: (T) -> Int,
    labelProvider: (T) -> String,
    groupProvider: ((T) -> Char),
    searchHint: String,
    applyLabel: String,
    clearLabel: String
) {

    var query by remember { mutableStateOf("") }
    val currentSelection =
        remember(selectedIds) { mutableStateListOf<Int>().apply { addAll(selectedIds) } }


    val filteredItems = remember(query, items) {
        if (query.isBlank()) items
        else items.filter {
            val normalizedQuery = query.removeAccents()
            labelProvider(it).removeAccents().contains(normalizedQuery, ignoreCase = true)
        }
    }

    val groupedItems = remember(filteredItems, groupProvider) {
        filteredItems.sortedBy { labelProvider(it) }.groupBy { groupProvider(it) }

    }

    IEEBaseSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismiss = onDismiss,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        searchHint = searchHint,
        hasSelection = currentSelection.isNotEmpty(),
        onClearSelection = { currentSelection.clear() },
        clearLabel = clearLabel,
        applyLabel = applyLabel,
        onApply = { onApply(currentSelection.toImmutableList()) }) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            groupedItems.keys.sorted().forEach { header ->
                stickyHeader { AlphabetHeader(header) }
                items(groupedItems[header] ?: emptyList()) { item ->
                    FilterRow(item, currentSelection, idProvider, labelProvider)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> IEESheet(
    modifier: Modifier = Modifier,
    items: ImmutableList<T>,
    selectedIds: ImmutableList<Int>,
    onApply: (ImmutableList<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    idProvider: (T) -> Int,
    labelProvider: (T) -> String,
    searchHint: String,
    applyLabel: String,
    clearLabel: String
) {

    var query by remember { mutableStateOf("") }
    val currentSelection =
        remember(selectedIds) { mutableStateListOf<Int>().apply { addAll(selectedIds) } }


    val filteredItems = remember(query, items) {
        if (query.isBlank()) items
        else items.filter {
            val normalizedQuery = query.removeAccents()
            labelProvider(it).removeAccents().contains(normalizedQuery, ignoreCase = true)
        }
    }

    IEEBaseSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismiss = onDismiss,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        searchHint = searchHint,
        hasSelection = currentSelection.isNotEmpty(),
        onClearSelection = { currentSelection.clear() },
        clearLabel = clearLabel,
        applyLabel = applyLabel,
        onApply = { onApply(currentSelection.toImmutableList()) }) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(filteredItems) { item ->
                FilterRow(item, currentSelection, idProvider, labelProvider)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> IEESheet(
    items: ImmutableList<T>,
    subscribedTags: ImmutableList<Int>,
    applySelectedTags: (ImmutableList<Int>) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    idProvider: (T) -> Int,
    labelProvider: (T) -> String,
    childrenProvider: (T) -> List<T>,
    searchHint: String,
    applyLabel: String,
    clearLabel: String,
) {
    var query by remember { mutableStateOf("") }

    val parentMap = remember(items) {
        val map = mutableMapOf<Int, T>()
        fun buildParentMap(nodes: List<T>, parent: T?) {
            for (node in nodes) {
                if (parent != null) map[idProvider(node)] = parent
                buildParentMap(childrenProvider(node), node)
            }
        }
        buildParentMap(items, null)
        map
    }

    val selectedIds = remember(items, subscribedTags) {
        val initialSelection = mutableSetOf<Int>()

        fun initializeSelection(nodes: List<T>, isParentSelected: Boolean) {
            for (node in nodes) {
                val id = idProvider(node)
                val isSelected = isParentSelected || subscribedTags.contains(id)

                if (isSelected) {
                    initialSelection.add(id)
                }

                initializeSelection(childrenProvider(node), isSelected)
            }
        }

        initializeSelection(items, false)
        mutableStateListOf<Int>().apply { addAll(initialSelection) }
    }

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

    IEEBaseSheet(
        sheetState = sheetState,
        onDismiss = onDismiss,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        searchHint = searchHint,
        hasSelection = selectedIds.isNotEmpty(),
        onClearSelection = { selectedIds.clear() },
        clearLabel = clearLabel,
        applyLabel = applyLabel,
        onApply = {
            val appliedIds = getOnlyTopLevelSelected(
                roots = items,
                selectedIds = selectedIds.toSet(),
                idProvider = idProvider,
                childrenProvider = childrenProvider
            )

            applySelectedTags(appliedIds.toImmutableList())
            onDismiss()
        }) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = flatList, key = { idProvider(it.item) }) { node ->
                val currentId = idProvider(node.item)
                val isSelected = currentId in selectedIds

                SelectableItem(
                    modifier = Modifier.padding(start = (node.depth * 16).dp),
                    title = labelProvider(node.item),
                    isSelected = isSelected,
                    onToggle = {
                        val descendants = collectAllIds(node.item, childrenProvider, idProvider)

                        if (isSelected) {
                            selectedIds.remove(currentId)
                            selectedIds.removeAll(descendants)

                            var p = parentMap[currentId]
                            while (p != null) {
                                selectedIds.remove(idProvider(p))
                                p = parentMap[idProvider(p)]
                            }
                        } else {
                            selectedIds.add(currentId)
                            selectedIds.addAll(descendants)

                            var p = parentMap[currentId]
                            while (p != null) {
                                val allChildrenSelected = childrenProvider(p).all {
                                    idProvider(it) in selectedIds
                                }
                                if (allChildrenSelected) {
                                    selectedIds.add(idProvider(p))
                                    p = parentMap[idProvider(p)]
                                } else {
                                    break
                                }
                            }
                        }
                    })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IEEBaseSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchHint: String,
    hasSelection: Boolean,
    onClearSelection: () -> Unit,
    clearLabel: String,
    applyLabel: String,
    onApply: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(searchHint, overflow = TextOverflow.Ellipsis, maxLines = 1)
                },
                singleLine = true,
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQuery.isNotEmpty(), enter = scaleIn(), exit = scaleOut()
                    ) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = "Clear text"
                            )
                        }
                    }
                })

            AnimatedVisibility(
                visible = hasSelection,
                enter = scaleIn() + fadeIn() + expandHorizontally(),
                exit = scaleOut() + fadeOut() + shrinkHorizontally()
            ) {
                Button(
                    onClick = onClearSelection,
                    modifier = Modifier.wrapContentWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(clearLabel, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }

        content()

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                onApply()
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(applyLabel)
        }
    }
}

@Composable
private fun AlphabetHeader(char: Char) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = char.toString(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun <T> FilterRow(
    item: T, selection: MutableList<Int>, idProvider: (T) -> Int, labelProvider: (T) -> String
) {
    val id = idProvider(item)
    val isSelected = id in selection

    SelectableItem(
        title = labelProvider(item), isSelected = isSelected, onToggle = {
            if (isSelected) selection.remove(id) else selection.add(id)
        })
}

@Composable
private fun SelectableItem(
    modifier: Modifier = Modifier, title: String, isSelected: Boolean, onToggle: () -> Unit
) {
    val shape = RoundedCornerShape(20)
    val containerColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer
    )
    val contentColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurface
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .clip(shape)
            .clickable(onClick = onToggle),
        color = containerColor,
        tonalElevation = if (isSelected) 3.dp else 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                modifier = Modifier.weight(1f)
            )

            AnimatedVisibility(
                visible = isSelected, enter = scaleIn(), exit = scaleOut()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
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

